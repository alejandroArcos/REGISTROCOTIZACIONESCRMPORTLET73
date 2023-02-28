package com.tokio.crm.registrocotizaciones73.commands.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.CRMResponse;
import com.tokio.crm.crmservices73.Bean.FolioResponse;
import com.tokio.crm.crmservices73.Constants.CrmDatabaseKey;
import com.tokio.crm.crmservices73.Interface.CrmGenerico;
import com.tokio.crm.registrocotizaciones73.constants.RegistroCotizacionesCrmPortlet73PortletKeys;
import com.tokio.crm.registrocotizaciones73.services.SendMailService;
import com.tokio.crm.servicebuilder73.model.Cotizacion;
import com.tokio.crm.servicebuilder73.model.Participante;
import com.tokio.crm.servicebuilder73.service.CotizacionLocalService;
import com.tokio.crm.servicebuilder73.service.ParticipanteLocalService;

import java.io.PrintWriter;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = {"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/resource/cotizacion/guardarCotizacion"}, service = MVCResourceCommand.class)
public class GuardaCotizacionResourceCommand extends BaseMVCResourceCommand {
	
	private static final Log _log = LogFactoryUtil.getLog(GuardaCotizacionResourceCommand.class);
	
	@Reference
	CrmGenerico _CrmGenericoService;
	
	@Reference
	SendMailService sms;
	
	User usuario;
	
	@Reference
	CotizacionLocalService _CotizacionLocalService;
	
	@Reference
    ParticipanteLocalService _ParticipanteLocalService;
	
    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        CRMResponse respuesta = new CRMResponse();
        String codigo = ParamUtil.getString(resourceRequest, "codigo");
        String nombreCliente = ParamUtil.getString(resourceRequest,"nombreCliente");
        long idAgente = ParamUtil.getLong(resourceRequest,"idAgente");
        String agente = ParamUtil.getString(resourceRequest, "agente");
        String tipoNegocio = ParamUtil.getString(resourceRequest, "tipoNegocio");
        String producto = ParamUtil.getString(resourceRequest, "producto");
        long moneda = ParamUtil.getLong(resourceRequest, "moneda");
        double prima = ParamUtil.getDouble(resourceRequest, "prima");
        int movimiento = ParamUtil.getInteger(resourceRequest,"movimiento");
        String fechaInicio = ParamUtil.getString(resourceRequest,"fechaInicio");
        String fechaTermino = ParamUtil.getString(resourceRequest,"fechaTermino");
        String fechaSolicitud = ParamUtil.getString(resourceRequest,"fechaSolicitud");
        String fechaAlta = ParamUtil.getString(resourceRequest,"fechaAlta");
        int controlSub = ParamUtil.getInteger(resourceRequest,"controlSub");
        String email = ParamUtil.getString(resourceRequest,"email");
        String canal = ParamUtil.getString(resourceRequest,"canal");
        long idEjecutivo = ParamUtil.getLong(resourceRequest,"idEjecutivo");
        String codigoEjecutivo = ParamUtil.getString(resourceRequest,"codigoEjecutivo");
        String ejecutivo = ParamUtil.getString(resourceRequest,"ejecutivo");
        String participantes = ParamUtil.getString(resourceRequest,"participantes");
        int gpFlag = ParamUtil.getInteger(resourceRequest,"gpFlag");
        boolean validacion = ParamUtil.getBoolean(resourceRequest,"validacion");
        String clave = ParamUtil.getString(resourceRequest,"clave");
        String preClave = ParamUtil.getString(resourceRequest,"preclave");
        long idSemaforo = ParamUtil.getLong(resourceRequest,"idSemaforo");
        usuario = (User) resourceRequest.getAttribute(WebKeys.USER);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(participantes,JsonArray.class);
        FolioResponse folioResponse;
        Cotizacion cotizacion;
        Participante participante;
        DecimalFormat df = new DecimalFormat("##########.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
        try{
            folioResponse = _CrmGenericoService.generaFolio(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            _log.debug(folioResponse);
            if(folioResponse.getCode() == 0) {
                cotizacion = _CotizacionLocalService.createCotizacion(0);
                cotizacion.setFolio(folioResponse.getFolio());
                cotizacion.setCodigo_cliente(codigo);
                cotizacion.setId_agente(idAgente);
                cotizacion.setTipo_negocio(tipoNegocio);
                cotizacion.setProducto(producto);
                cotizacion.setId_moneda(moneda);
                cotizacion.setPrima(prima);
                cotizacion.setPrima(Double.parseDouble(df.format(prima)));
                cotizacion.setTipo_movimiento(movimiento);
                cotizacion.setEjecutivo(ejecutivo);
                cotizacion.setId_ejecutivo(idEjecutivo);
                cotizacion.setEmail(email);
                cotizacion.setCanal(canal);
                cotizacion.setFecha_inicio(new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicio));
                cotizacion.setFecha_fin(new SimpleDateFormat("yyyy-MM-dd").parse(fechaTermino));
                cotizacion.setFecha_solicitud(new SimpleDateFormat("yyyy-MM-dd").parse(fechaSolicitud));
                cotizacion.setFecha_alta(new SimpleDateFormat("yyyy-MM-dd").parse(fechaAlta));
                cotizacion.setFecha_modificacion(new Timestamp(new Date().getTime()));
                cotizacion.setControl_subscripcion(controlSub);
                cotizacion.setGp_flag(gpFlag);
                cotizacion.setUser_creacion(usuario.getUserId());
                cotizacion.setId_semaforo(idSemaforo);
                if(!validacion) {
                    cotizacion.setEstatus_cotizacion(CrmDatabaseKey.ESTATUS_AUTORIZADO);
                    respuesta = _CrmGenericoService.guardaCotizacion(usuario.getScreenName(),
                        RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73, folioResponse.getFolio(), codigo,clave.equals("")?preClave:clave,producto,movimiento + "",
                        tipoNegocio,jsonArray,moneda + "",fechaInicio,fechaTermino,fechaSolicitud,fechaAlta,controlSub +"",
                        email,gpFlag + "",canal, codigoEjecutivo);
                    sms.enviarAutorizacion(folioResponse.getFolio(), usuario.getUserId(), nombreCliente,themeDisplay.getURLPortal() + themeDisplay.getURLCurrent().split("\\?")[0]);
                }else {
                    cotizacion.setEstatus_cotizacion(CrmDatabaseKey.ESTATUS_PENDIENTE_AUTORIZAR);
                    sms.enviarPendienteAutorizar(folioResponse.getFolio(), usuario.getUserId(), nombreCliente,themeDisplay.getURLPortal() + themeDisplay.getURLCurrent().split("\\?")[0]);
                }
                cotizacion.setEjecutivo_codigo(codigoEjecutivo);
                cotizacion = _CotizacionLocalService.addCotizacion(cotizacion);
                if(!Objects.equals(tipoNegocio, "1")){
                    JsonObject jsonObject;
                    for(int i = 0; i < jsonArray.size(); i++){
                        jsonObject = jsonArray.get(i).getAsJsonObject();
                        participante = _ParticipanteLocalService.createParticipante(0);
                        participante.setId_cotizacion(cotizacion.getId_cotizacion());
                        participante.setNombre_participante(jsonObject.get("nombre").getAsString());
                        participante.setParticipacion(jsonObject.get("porcentaje").getAsString());
                        _ParticipanteLocalService.addParticipante(participante);
                    }
                }
                respuesta.setCode(0);
                respuesta.setMsg("La cotización se registro con éxito");
            }else{
                respuesta.setCode(1);
                respuesta.setMsg("Error en la generación de folio");
            }
            _log.debug(respuesta);
        }catch (Exception e){
            respuesta.setCode(1);
            _log.error("[BuscaProductosResourceCommand]: ");
            e.printStackTrace();
            respuesta.setMsg(Arrays.toString(e.getStackTrace()));
        }
        String responseString = gson.toJson(respuesta);
        PrintWriter writer = resourceResponse.getWriter();
        writer.write(responseString);
    }
}
