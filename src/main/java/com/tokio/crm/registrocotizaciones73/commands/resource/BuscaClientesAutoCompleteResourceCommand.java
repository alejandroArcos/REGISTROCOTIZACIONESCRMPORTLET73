package com.tokio.crm.registrocotizaciones73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.ClienteParcialResponse;
import com.tokio.crm.crmservices73.Interface.CrmGenerico;
import com.tokio.crm.registrocotizaciones73.constants.RegistroCotizacionesCrmPortlet73PortletKeys;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = {"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/resource/cotizacion/buscaClientesAutoComplete"}, service = MVCResourceCommand.class)
public class BuscaClientesAutoCompleteResourceCommand extends BaseMVCResourceCommand {

    private static final Log _log = LogFactoryUtil.getLog(BuscaClientesAutoCompleteResourceCommand.class);

    @Reference
    CrmGenerico _CrmGenericoService;

    User usuario;

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        ClienteParcialResponse respuesta = new ClienteParcialResponse();
        int tipo = ParamUtil.getInteger(resourceRequest, "tipo");
        String term = ParamUtil.getString(resourceRequest, "term");
        _log.debug(tipo);
        _log.debug(term);
        usuario = (User) resourceRequest.getAttribute(WebKeys.USER);
        Gson gson = new Gson();
        try {
            switch (tipo){
                case 1:
                    respuesta = _CrmGenericoService.obtenerClientesAutoComplete(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,"",term,"","");
                break;
                case 2:
                    respuesta = _CrmGenericoService.obtenerClientesAutoComplete(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,"","",term,"");
                break;
                case 3:
                    respuesta = _CrmGenericoService.obtenerClientesAutoComplete(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,term,"","","");
                break;
            }
            _log.debug(respuesta.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        String responseString = gson.toJson(respuesta);
        PrintWriter writer = resourceResponse.getWriter();
        writer.write(responseString);
    }
}
