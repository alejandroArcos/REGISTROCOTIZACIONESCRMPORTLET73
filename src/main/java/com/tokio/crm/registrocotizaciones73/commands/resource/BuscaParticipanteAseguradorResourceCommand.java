package com.tokio.crm.registrocotizaciones73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.ListaParticipantes;
import com.tokio.crm.crmservices73.Interface.CrmGenerico;
import com.tokio.crm.registrocotizaciones73.constants.RegistroCotizacionesCrmPortlet73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = {"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/resource/cotizacion/buscaParticipanteAsegurador"}, service = MVCResourceCommand.class)
public class BuscaParticipanteAseguradorResourceCommand extends BaseMVCResourceCommand {
    private static final Log _log = LogFactoryUtil.getLog(BuscaParticipanteAseguradorResourceCommand.class);

    @Reference
    CrmGenerico _CrmGenericoService;

    User usuario;

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        ListaParticipantes respuesta = new ListaParticipantes();
        String nombre = ParamUtil.getString(resourceRequest, "nombre");
        String participante = ParamUtil.getString(resourceRequest, "participante");
        String negocio = ParamUtil.getString(resourceRequest, "negocio");
        usuario = (User) resourceRequest.getAttribute(WebKeys.USER);
        _log.debug(nombre);
        _log.debug(participante);
        _log.debug(negocio);
        try {
            respuesta = _CrmGenericoService.getListaParticipantes(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,nombre,participante,negocio);
            _log.debug(respuesta);
        }catch (Exception e){
            _log.error("[BuscaCotizacionesClienteResourceCommand]: " + e.getStackTrace());
        }
        Gson gson = new Gson();
        String responseString = gson.toJson(respuesta);
        PrintWriter writer = resourceResponse.getWriter();
        writer.write(responseString);
    }
}
