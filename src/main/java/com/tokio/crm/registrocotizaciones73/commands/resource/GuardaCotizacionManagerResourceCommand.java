package com.tokio.crm.registrocotizaciones73.commands.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.CRMResponse;
import com.tokio.crm.crmservices73.Bean.ClienteInfoResponse;
import com.tokio.crm.crmservices73.Constants.CrmDatabaseKey;
import com.tokio.crm.crmservices73.Interface.CrmGenerico;
import com.tokio.crm.registrocotizaciones73.constants.RegistroCotizacionesCrmPortlet73PortletKeys;
import com.tokio.crm.registrocotizaciones73.services.SendMailService;
import com.tokio.crm.servicebuilder73.model.Agente;
import com.tokio.crm.servicebuilder73.model.Cotizacion;
import com.tokio.crm.servicebuilder73.model.Participante;
import com.tokio.crm.servicebuilder73.service.AgenteLocalService;
import com.tokio.crm.servicebuilder73.service.CotizacionLocalService;
import com.tokio.crm.servicebuilder73.service.ParticipanteLocalService;

import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = {"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/resource/cotizacion/guardarCotizacionManager"}, service = MVCResourceCommand.class)
public class GuardaCotizacionManagerResourceCommand extends BaseMVCResourceCommand {
    private static final Log _log = LogFactoryUtil.getLog(GuardaCotizacionManagerResourceCommand.class);

    @Reference
    CrmGenerico _CrmGenericoService;

    @Reference
    SendMailService sms;

    User usuario;
    
    @Reference
	CotizacionLocalService _CotizacionLocalService;
    
    @Reference
	AgenteLocalService _AgenteLocalService;
    
    @Reference
    ParticipanteLocalService _ParticipanteLocalService;
    
    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        CRMResponse respuesta = new CRMResponse();
        long idCotizacion = ParamUtil.getLong(resourceRequest,"id");
        boolean rechazo = ParamUtil.getBoolean(resourceRequest,"rechazo");
        usuario = (User) resourceRequest.getAttribute(WebKeys.USER);
        Gson gson = new Gson();
        Cotizacion cotizacion;
        DecimalFormat df = new DecimalFormat("##########.##");
        ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
        df.setRoundingMode(RoundingMode.FLOOR);
        _log.debug(idCotizacion);
        _log.debug(rechazo);
        try{
            cotizacion = _CotizacionLocalService.getCotizacion(idCotizacion);
            ClienteInfoResponse cliente = _CrmGenericoService.getClienteInfo(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,cotizacion.getCodigo_cliente());
            if (rechazo) {
                cotizacion.setEstatus_cotizacion(CrmDatabaseKey.ESTATUS_RECHAZADO_MANAGER);
                _CotizacionLocalService.updateCotizacion(cotizacion);
                sms.enviarRechazo(cotizacion.getFolio(), usuario.getUserId(), cliente.getNombre(),themeDisplay.getURLPortal() + themeDisplay.getURLCurrent().split("\\?")[0]);
            } else {
                Agente agente = _AgenteLocalService.getAgente(cotizacion.getId_agente());
                JsonArray jsonArray = new JsonArray();
                if(cotizacion.getTipo_negocio() != "1"){
                    List<Participante> participantes = _ParticipanteLocalService.findByCotizacion(cotizacion.getId_cotizacion());
                    jsonArray = gson.fromJson(gson.toJson(participantes),JsonArray.class);
                }
                respuesta = _CrmGenericoService.guardaCotizacion(usuario.getScreenName(),
                        RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73, cotizacion.getFolio(), cotizacion.getCodigo_cliente(),
                        agente.getClave().equals("")?agente.getPreclave():agente.getClave(),cotizacion.getProducto(),
                        cotizacion.getTipo_movimiento() +"", cotizacion.getTipo_negocio(),jsonArray,cotizacion.getId_moneda() + "",
                        cotizacion.getFecha_inicio().toString(),cotizacion.getFecha_fin().toString(),cotizacion.getFecha_solicitud().toString(),cotizacion.getFecha_alta().toString(),
                        cotizacion.getControl_subscripcion() +"", cotizacion.getEmail(),cotizacion.getGp_flag() + "", cotizacion.getCanal(),cotizacion.getEjecutivo_codigo());
                cotizacion.setEstatus_cotizacion(CrmDatabaseKey.ESTATUS_AUTORIZADO);
                cotizacion.setFecha_modificacion(new Date());
                cotizacion = _CotizacionLocalService.updateCotizacion(cotizacion);
                sms.enviarAutorizacion(cotizacion.getFolio(), usuario.getUserId(), cliente.getNombre(),themeDisplay.getURLPortal() + themeDisplay.getURLCurrent().split("\\?")[0]);
            }
            respuesta.setCode(0);
            respuesta.setMsg("La cotización se registro con éxito");
            _log.debug(respuesta);
        }catch (Exception e){
            respuesta.setCode(1);
            respuesta.setMsg(e.getMessage());
            _log.error("[BuscaProductosResourceCommand]: " + e.getMessage());
        }
        String responseString = gson.toJson(respuesta);
        PrintWriter writer = resourceResponse.getWriter();
        writer.write(responseString);
    }
}
