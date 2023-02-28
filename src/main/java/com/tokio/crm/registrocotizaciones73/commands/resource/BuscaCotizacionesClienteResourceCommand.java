package com.tokio.crm.registrocotizaciones73.commands.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.CotizacionResponse;
import com.tokio.crm.crmservices73.Bean.ListaRegistro;
import com.tokio.crm.crmservices73.Bean.Registro;
import com.tokio.crm.crmservices73.Constants.CrmDatabaseKey;
import com.tokio.crm.crmservices73.Interface.CrmGenerico;
import com.tokio.crm.registrocotizaciones73.beans.Mapa;
import com.tokio.crm.registrocotizaciones73.constants.RegistroCotizacionesCrmPortlet73PortletKeys;
import com.tokio.crm.servicebuilder73.model.Agente;
import com.tokio.crm.servicebuilder73.model.Catalogo_Detalle;
import com.tokio.crm.servicebuilder73.model.Catalogo_DetalleModel;
import com.tokio.crm.servicebuilder73.model.Cotizacion;
import com.tokio.crm.servicebuilder73.service.AgenteLocalService;
import com.tokio.crm.servicebuilder73.service.Catalogo_DetalleLocalService;
import com.tokio.crm.servicebuilder73.service.CotizacionLocalService;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = {"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/resource/cotizacion/buscaCotizacionesCliente"}, service = MVCResourceCommand.class)
public class BuscaCotizacionesClienteResourceCommand extends BaseMVCResourceCommand {
    private static final Log _log = LogFactoryUtil.getLog(BuscaCotizacionesClienteResourceCommand.class);

    @Reference
    CrmGenerico _CrmGenericoService;

    User usuario;
    
    @Reference
	CotizacionLocalService _CotizacionLocalService;
    
    @Reference
	AgenteLocalService _AgenteLocalService;
    
    @Reference
	Catalogo_DetalleLocalService _Catalogo_DetalleLocalService;

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        CotizacionResponse respuesta;
        String codigo = ParamUtil.getString(resourceRequest, "codigo");
        usuario = (User) resourceRequest.getAttribute(WebKeys.USER);
        _log.debug(codigo);
        List<Cotizacion> cotizacionList ;
        List<Agente> agentes = new ArrayList<>();
        Map<String,String> mapaCanal = new HashMap<>();
        Map<String,String> mapaArea = new HashMap<>();
        Map<String,String> mapaProducto = new HashMap<>();
        Map<Long,String> mapaAgente = new HashMap<>();
        Map<Long,String> mapaAgenteClave = new HashMap<>();
        Gson gson = new Gson();
        String responseString = null;
        try {
            respuesta = _CrmGenericoService.obtenerCotizaciones(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,codigo);
            cotizacionList = _CotizacionLocalService.findByCodigoClienteAndEstatusCotizacion(codigo,CrmDatabaseKey.ESTATUS_PENDIENTE_AUTORIZAR);
            responseString = gson.toJson(respuesta);
            if(!cotizacionList.isEmpty()) {
                agentes.addAll(_AgenteLocalService.findByTipoNegocio(CrmDatabaseKey.NEGOCIO_M));
                agentes.addAll(_AgenteLocalService.findByTipoNegocio(CrmDatabaseKey.NEGOCIO_J));
                ListaRegistro registro = _CrmGenericoService.getCatalogo(0,"","CATCANAL",0,"",usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
                registro.getLista().forEach(f->{
                    mapaCanal.put(f.getCodigo(),f.getDescripcion());
                    mapaArea.put(f.getCodigo(),f.getValor());
                });
                registro = _CrmGenericoService.getCatalogo(0,"","CATPRODUCTO",0,usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
                registro.getLista().forEach(f -> mapaProducto.put(f.getCodigo(),f.getDescripcion()));
                registro = _CrmGenericoService.getCatalogo(0,"","CATPRODUCTO",0,"0004",usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
                registro.getLista().forEach(f ->{
                    if(!mapaProducto.containsKey(f.getCodigo())){
                        mapaProducto.put(f.getCodigo(),f.getDescripcion());
                    }
                });
                registro = _CrmGenericoService.getCatalogo(0,"","CATOFICINA",0,usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
                Map<Integer,String> mapaOficina = registro.getLista().stream().collect(Collectors.toMap(Registro::getId,Registro::getDescripcion));
                Map<Long,String> mapaEsatus = _Catalogo_DetalleLocalService.findByCodigo("CATESTATUS").stream().collect(Collectors.toMap(Catalogo_DetalleModel::getCatalogoDetalleId,Catalogo_Detalle::getDescripcion));
                List<Catalogo_Detalle> tiposSociedad = _Catalogo_DetalleLocalService.findByCodigo("CATTIPSOC");
                Map<Integer,String> mapaSociedad = tiposSociedad.stream().map(m -> new Mapa((int)m.getCatalogoDetalleId(),m.getDescripcion())).collect(Collectors.toMap(Mapa::getId,Mapa::getDescripcion));
                Map<Long,String> mapaAgenteOficina = new HashMap<>();
                List<Catalogo_Detalle> oficias = _Catalogo_DetalleLocalService.findByCodigo("CATOFICINA");
                Map<Long,String> mapaOficias = oficias.stream().parallel().collect(Collectors.toMap(Catalogo_Detalle::getCatalogoDetalleId, Catalogo_DetalleModel::getDescripcion));
                registro = _CrmGenericoService.getCatalogo(0,"","CATRIESGO",0,usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
                Map<Long,String> mapaSemaforo = new HashMap<>();
                for(Registro registrol:registro.getLista()){
                    mapaSemaforo.put((long)registrol.getId(),registrol.getDescripcion());
                }
                String nombre;
                for(Agente agente: agentes){
                    if(agente.getTipoPersona() == CrmDatabaseKey.PERSONA_FISICA){
                        nombre = agente.getNombre() + " " + agente.getApellidoP() + " " + agente.getApellidoM();
                    }else{
                        nombre = agente.getNombre() + " " + mapaSociedad.get(agente.getTipoSociedad());
                    }
                    mapaAgente.put(agente.getAgenteId(),nombre);
                    mapaAgenteClave.put(agente.getAgenteId(),"".equals(agente.getClave())?agente.getPreclave():agente.getClave());
                    mapaAgenteOficina.put(agente.getAgenteId(),mapaOficias.get(agente.getOficinaId()));
                }
                JsonArray jsonArray = new JsonArray();
                JsonObject jsonObject;
                for(Cotizacion cotizacion :cotizacionList){
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("folio",cotizacion.getFolio());
                    jsonObject.addProperty("estatus",mapaEsatus.get((long)cotizacion.getEstatus_cotizacion()));
                    jsonObject.addProperty("canal",mapaCanal.get(cotizacion.getCanal()));
                    jsonObject.addProperty("area",mapaArea.get(cotizacion.getCanal()));
                    jsonObject.addProperty("producto",mapaProducto.get(cotizacion.getProducto()));
                    jsonObject.addProperty("cve_agente",mapaAgenteClave.get(cotizacion.getId_agente()));
                    jsonObject.addProperty("agente",mapaAgente.get(cotizacion.getId_agente()));
                    jsonObject.addProperty("ejecutivo",cotizacion.getEjecutivo());
                    try{
                        jsonObject.addProperty("semaforo",mapaSemaforo.get(cotizacion.getId_semaforo()));
                    }catch (NullPointerException ignored){
                        jsonObject.addProperty("semaforo","VERDE");
                    }
                    jsonObject.addProperty("oficina",mapaAgenteOficina.get(cotizacion.getId_agente()));
                    jsonObject.addProperty("fec_mod",cotizacion.getFecha_modificacion().toString());
                    jsonObject.addProperty("poliza","");
                    jsonObject.addProperty("vigencia",cotizacion.getFecha_inicio().toString() + "-" + cotizacion.getFecha_fin().toString());
                    jsonObject.addProperty("cot_proceso",mapaEsatus.get((long)cotizacion.getEstatus_cotizacion()));
                    jsonObject.addProperty("carpeta","");
                    jsonArray.add(jsonObject);
                }
                String string = gson.toJson(jsonArray);
                if(respuesta.getLista().isEmpty()){
                    responseString = gson.toJson(respuesta);
                    respuesta.setCode(0);
                    respuesta.setMsg("Tiene cotizaciones pendientes");
                    responseString = gson.toJson(respuesta);
                    responseString = responseString.replace("[]}","") + string + "}";
                   _log.debug(responseString);
                }else{
                    responseString = responseString.replace("]}",",") + string.replace("[","") + "}";
                }

            }
            _log.debug(respuesta);
        }catch (Exception e){
            _log.error("[BuscaCotizacionesClienteResourceCommand]: ");
            e.printStackTrace();
        }
        PrintWriter writer = resourceResponse.getWriter();
        writer.write(responseString);
    }
}
