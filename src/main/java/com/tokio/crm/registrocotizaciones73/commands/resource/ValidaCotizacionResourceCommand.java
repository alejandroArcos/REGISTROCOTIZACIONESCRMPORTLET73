package com.tokio.crm.registrocotizaciones73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.SimpleResponse;
import com.tokio.crm.crmservices73.Interface.CrmGenerico;
import com.tokio.crm.registrocotizaciones73.constants.RegistroCotizacionesCrmPortlet73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = {"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/resource/cotizacion/validaCotizacion"}, service = MVCResourceCommand.class)
public class ValidaCotizacionResourceCommand extends BaseMVCResourceCommand {
    private static final Log _log = LogFactoryUtil.getLog(ValidaCotizacionResourceCommand.class);

    @Reference
    CrmGenerico _CrmGenericoService;

    User usuario;

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        SimpleResponse respuesta = new SimpleResponse();
        String cliente = ParamUtil.getString(resourceRequest, "cliente");
        String producto = ParamUtil.getString(resourceRequest, "producto");
        String canal = ParamUtil.getString(resourceRequest, "canal");
        String ejecutivo = ParamUtil.getString(resourceRequest, "ejecutivo");
        usuario = (User) resourceRequest.getAttribute(WebKeys.USER);
        _log.debug(cliente);
        _log.debug(producto);
        _log.debug(canal);
        _log.debug(ejecutivo);
        try {
            respuesta = _CrmGenericoService.validaCotizacion(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,cliente,canal,producto,ejecutivo);
            _log.debug(respuesta);
        }catch (Exception e){
            _log.error("[ValidaCotizacionResourceCommand]: " + e.getStackTrace());
        }
        Gson gson = new Gson();
        String responseString = gson.toJson(respuesta);
        PrintWriter writer = resourceResponse.getWriter();
        writer.write(responseString);
    }
}
