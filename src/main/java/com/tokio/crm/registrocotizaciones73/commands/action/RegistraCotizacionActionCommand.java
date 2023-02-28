package com.tokio.crm.registrocotizaciones73.commands.action;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.CatalogoMoneda;
import com.tokio.crm.crmservices73.Bean.ClienteInfoResponse;
import com.tokio.crm.crmservices73.Bean.ListaCpData;
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
import com.tokio.crm.servicebuilder73.model.User_Crm;
import com.tokio.crm.servicebuilder73.service.AgenteLocalService;
import com.tokio.crm.servicebuilder73.service.Catalogo_DetalleLocalService;
import com.tokio.crm.servicebuilder73.service.CotizacionLocalService;
import com.tokio.crm.servicebuilder73.service.User_CrmLocalService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


@Component(immediate = true, property = {"javax.portlet.init-param.copy-request-parameters=true",
        "javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
        "mvc.command.name=/crm/action/cotizacion/registraCotizacion"}, service = MVCActionCommand.class)

public class RegistraCotizacionActionCommand extends BaseMVCActionCommand {
	
	private static final Log _log = LogFactoryUtil.getLog(RegistraCotizacionActionCommand.class);
	
	User usuario;
	
	@Reference
	CrmGenerico _CrmGenericoService;
	
	@Reference
	Catalogo_DetalleLocalService _Catalogo_DetalleLocalService;
	
	@Reference
	User_CrmLocalService _User_CrmLocalService;
	
	@Reference
	AgenteLocalService _AgenteLocalService;
	
	@Reference
	CotizacionLocalService _CotizacionLocalService;

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) {

        HttpServletRequest originalRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest));
        usuario = (User) originalRequest.getAttribute(WebKeys.USER);
        String codigoCliente = String.valueOf(originalRequest.getParameter("codigo"));
        String folio = String.valueOf(originalRequest.getParameter("folio"));
        if("null".equals(codigoCliente)){
            cargaCotizacion(actionRequest,folio);
        }else{
            cargaInformacion(actionRequest,codigoCliente);
        }
        cargaCatalogos(actionRequest);
        actionResponse.setRenderParameter("jspPage", "/registraCotizacion.jsp");
    }

    public void cargaInformacion(ActionRequest actionRequest, String codigoCliente) {
        ClienteInfoResponse clienteInfoResponse;
        try {
            clienteInfoResponse = _CrmGenericoService.getClienteInfo(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,codigoCliente);
            _log.debug(clienteInfoResponse);
            actionRequest.setAttribute("cliente",clienteInfoResponse);
            ListaCpData listaCpData=_CrmGenericoService.getCatalogoCP(clienteInfoResponse.getCp(), usuario.getScreenName(), RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            if(listaCpData.getCode() == 0){
                actionRequest.setAttribute("cpData",listaCpData.getCpData());
            }
            actionRequest.setAttribute("soloLectura",0);
        }catch (Exception e){
            _log.error("[RegistraCotizacionActionCommnad]" );
            e.printStackTrace();
        }
    }

    public void cargaCatalogos(ActionRequest actionRequest) {
        List<Agente> agentes = new ArrayList<>();
        ListaRegistro listaRegistro;
        CatalogoMoneda catalogoMoneda;
        User_Crm user_crm;
        try{
            user_crm = _User_CrmLocalService.getUser_Crm(new Long(usuario.getUserId()).intValue());
            agentes.addAll(_AgenteLocalService.findByTipoNegocio(CrmDatabaseKey.NEGOCIO_J));
            agentes.addAll(_AgenteLocalService.findByTipoNegocio(CrmDatabaseKey.NEGOCIO_M));
            listaRegistro = _CrmGenericoService.getCatalogo(0,"","CATCANAL",0,"",usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            actionRequest.setAttribute("listCanalNegocio",listaRegistro.getLista());
            listaRegistro = _CrmGenericoService.getCatalogo(0,"","CATRIESGO",0,"",usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            actionRequest.setAttribute("listaSemaforo",listaRegistro.getLista());
            catalogoMoneda = _CrmGenericoService.getCatalogoMoneda(usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            List<Catalogo_Detalle> tiposSociedad = _Catalogo_DetalleLocalService.findByCodigo("CATTIPSOC");
            actionRequest.setAttribute("mapaSociedad", tiposSociedad.stream().map(m -> new Mapa((int)m.getCatalogoDetalleId(),m.getDescripcion())).collect(Collectors.toMap(Mapa::getId,Mapa::getDescripcion)));
            actionRequest.setAttribute("mapaSociedad", tiposSociedad.stream().map(m -> new Mapa((int)m.getCatalogoDetalleId(),m.getDescripcion())).collect(Collectors.toMap(Mapa::getId,Mapa::getDescripcion)));
            actionRequest.setAttribute("agentes",agentes);
            actionRequest.setAttribute("listMoneda",catalogoMoneda.getLista());
            actionRequest.setAttribute("manager",user_crm.getPerfilId() == CrmDatabaseKey.ID_PERFIL_MANAGER_VENTAS?1:0);
        }catch (Exception e){
            _log.error("[RegistraCotizacionActionCommnad]" + Arrays.toString(e.getStackTrace()));
        }
    }

    public void cargaCotizacion(ActionRequest actionRequest, String folio){
        Cotizacion cotizacion;
        List<Agente> agentes = new ArrayList<>();
        try {
            agentes.addAll(_AgenteLocalService.findByTipoNegocio(CrmDatabaseKey.NEGOCIO_M));
            agentes.addAll(_AgenteLocalService.findByTipoNegocio(CrmDatabaseKey.NEGOCIO_J));
            cotizacion = _CotizacionLocalService.findByFolio(folio);
            cargaInformacion(actionRequest,cotizacion.getCodigo_cliente());
            List<Cotizacion> cotizacionesPrevias = new ArrayList<>(_CotizacionLocalService.findByCodigoCliente(cotizacion.getCodigo_cliente()));
            _log.debug(cotizacion);
            Map<Long,String> mapaAgente = new HashMap<>();
            List<Catalogo_Detalle> tiposSociedad = _Catalogo_DetalleLocalService.findByCodigo("CATTIPSOC");
            Map<Integer,String> mapaSociedad = tiposSociedad.stream().map(m -> new Mapa((int)m.getCatalogoDetalleId(),m.getDescripcion())).collect(Collectors.toMap(Mapa::getId,Mapa::getDescripcion));
            List<Catalogo_Detalle> oficias = _Catalogo_DetalleLocalService.findByCodigo("CATOFICINA");
            Map<Long,String> mapaOficias = oficias.stream().parallel().collect(Collectors.toMap(Catalogo_Detalle::getCatalogoDetalleId, Catalogo_DetalleModel::getDescripcion));
            String nombre;
            for(Agente agente: agentes){
                if(agente.getTipoPersona() == CrmDatabaseKey.PERSONA_FISICA){
                    nombre = agente.getNombre() + " " + agente.getApellidoP() + " " + agente.getApellidoM();
                }else{
                    nombre = agente.getNombre() + " " + mapaSociedad.get(agente.getTipoSociedad());
                }
                mapaAgente.put(agente.getAgenteId(),nombre);
            }
            actionRequest.setAttribute("mapaAgentes",mapaAgente);
            actionRequest.setAttribute("cotizacion",cotizacion);
            actionRequest.setAttribute("cotizacionesPrevias",cotizacionesPrevias);
            ListaRegistro listaRegistro = _CrmGenericoService.getCatalogo(0,"","CATEJECUTIVOS",0,cotizacion.getCanal(),usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            actionRequest.setAttribute("listEjecutivos",listaRegistro.getLista());
            listaRegistro = _CrmGenericoService.getCatalogo(0,"","CATPRODUCTO",0,cotizacion.getProducto(),usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            actionRequest.setAttribute("productos",listaRegistro.getLista());
            listaRegistro = _CrmGenericoService.getCatalogo(0,"","CATRIESGO",0,"",usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
            Map<Long,String> mapaSemaforo = new HashMap<>();
            for(Registro registrol: listaRegistro.getLista()){
                mapaSemaforo.put((long)registrol.getId(),registrol.getDescripcion());
            }
            actionRequest.setAttribute("mapaSemaforo",mapaSemaforo);
            actionRequest.setAttribute("soloLectura",1);
        }catch (Exception e){
            _log.error("[RegistraCotizacionActionCommnad]" + Arrays.toString(e.getStackTrace()));
        }
    }
}
