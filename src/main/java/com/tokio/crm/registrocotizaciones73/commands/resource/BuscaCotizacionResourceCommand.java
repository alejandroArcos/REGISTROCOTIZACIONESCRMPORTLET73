package com.tokio.crm.registrocotizaciones73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.CRMResponse;
import com.tokio.crm.crmservices73.Interface.CrmGenerico;
import com.tokio.crm.registrocotizaciones73.constants.RegistroCotizacionesCrmPortlet73PortletKeys;
import com.tokio.crm.servicebuilder73.exception.NoSuchCotizacionException;
import com.tokio.crm.servicebuilder73.model.Cotizacion;
import com.tokio.crm.servicebuilder73.service.CotizacionLocalService;

import java.io.PrintWriter;
import java.util.Objects;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = {"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/resource/cotizacion/BuscaCotizacionFolio"}, service = MVCResourceCommand.class)
public class BuscaCotizacionResourceCommand extends BaseMVCResourceCommand {
    private static final Log _log = LogFactoryUtil.getLog(BuscaCotizacionResourceCommand.class);

    @Reference
    CrmGenerico _CrmGenericoService;
    
    @Reference
	CotizacionLocalService _CotizacionLocalService;

    User usuario;

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        CRMResponse respuesta = new CRMResponse();
        String folio = ParamUtil.getString(resourceRequest, "folio");
        usuario = (User) resourceRequest.getAttribute(WebKeys.USER);
        _log.debug(folio);
        try {
            Cotizacion cotizacion = _CotizacionLocalService.findByFolio(folio);
            if(Objects.nonNull(cotizacion)){
                respuesta.setCode(0);
                respuesta.setMsg("Entra a la cotización");
            }
            _log.debug(respuesta);
        }catch (NoSuchCotizacionException cotizacionException){
            respuesta.setCode(1);
            respuesta.setMsg("Esta cotización no esta pendiente de autorizar");
        } catch(Exception e){
            _log.error("[ValidaCotizacionResourceCommand]: " + e.getMessage());
            respuesta.setCode(1);
            respuesta.setMsg(e.getMessage());
        }
        Gson gson = new Gson();
        String responseString = gson.toJson(respuesta);
        PrintWriter writer = resourceResponse.getWriter();
        writer.write(responseString);
    }
}
