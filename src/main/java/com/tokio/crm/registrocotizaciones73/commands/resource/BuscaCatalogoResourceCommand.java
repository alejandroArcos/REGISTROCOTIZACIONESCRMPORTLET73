package com.tokio.crm.registrocotizaciones73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.ListaRegistro;
import com.tokio.crm.crmservices73.Interface.CrmGenerico;
import com.tokio.crm.registrocotizaciones73.constants.RegistroCotizacionesCrmPortlet73PortletKeys;

import java.io.PrintWriter;
import java.util.Arrays;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = {"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/resource/cotizacion/buscaCatalogo"}, service = MVCResourceCommand.class)
public class BuscaCatalogoResourceCommand extends BaseMVCResourceCommand {
    private static final Log _log = LogFactoryUtil.getLog(BuscaCatalogoResourceCommand.class);

    @Reference
    CrmGenerico _CrmGenericoService;

    User usuario;
    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        ListaRegistro respuesta = new ListaRegistro();
        String valor = ParamUtil.getString(resourceRequest, "valor");
        String catalogo = ParamUtil.getString(resourceRequest, "catalogo");
        _log.debug(valor);
        _log.debug(catalogo);
        usuario = (User) resourceRequest.getAttribute(WebKeys.USER);
        Gson gson = new Gson();
        try{
            respuesta = _CrmGenericoService.getCatalogo(0,"",catalogo,1,valor,usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            _log.debug(respuesta);
        }catch (Exception e){
            _log.error("[BuscaProductosResourceCommand]: " + Arrays.toString(e.getStackTrace()));
        }
        String responseString = gson.toJson(respuesta);
        PrintWriter writer = resourceResponse.getWriter();
        writer.write(responseString);
    }
}
