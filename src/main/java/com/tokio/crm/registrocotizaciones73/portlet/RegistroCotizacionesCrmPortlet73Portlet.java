package com.tokio.crm.registrocotizaciones73.portlet;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.crm.crmservices73.Bean.ListaRegistro;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author urielfloresvaldovinos
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=REGISTROCOTIZACIONESCRMPORTLET73",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class RegistroCotizacionesCrmPortlet73Portlet extends MVCPortlet {
	
	@Reference
	CrmGenerico _CrmGenericoService;

	User usuario;
	
	@Reference
	Catalogo_DetalleLocalService _Catalogo_DetalleLocalService;
	
	@Reference
	User_CrmLocalService _User_CrmLocalService;
	
	@Reference
	CotizacionLocalService _CotizacionLocalService;
	
	@Reference
	AgenteLocalService _AgenteLocalService;

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		usuario = (User) renderRequest.getAttribute(WebKeys.USER);
		cargarInformacion(renderRequest);
		super.doView(renderRequest, renderResponse);
	}

	public void cargarInformacion(RenderRequest renderRequest) {
		List<Cotizacion> cotizacionList = new ArrayList<>();
		List<Agente> agentes = new ArrayList<>();
		Map<String,String> mapaCanal = new HashMap<>();
		Map<String,String> mapaArea = new HashMap<>();
		Map<String,String> mapaProducto = new HashMap<>();
		try{
			User_Crm user_crm = _User_CrmLocalService.getUser_Crm(new Long(usuario.getUserId()).intValue());
			switch (user_crm.getPerfilId()){
				case CrmDatabaseKey.ID_PERFIL_MANAGER_VENTAS:
					cotizacionList = _CotizacionLocalService.findByEstatusCotizacion(CrmDatabaseKey.ESTATUS_PENDIENTE_AUTORIZAR);

					break;
				case CrmDatabaseKey.ID_PERFIL_EJECUTIVO_VENTAS:
				case CrmDatabaseKey.ID_PERFIL_ANALISTA_VENTAS:
					break;
			}
			agentes.addAll(_AgenteLocalService.findByTipoNegocio(CrmDatabaseKey.NEGOCIO_M));
			agentes.addAll(_AgenteLocalService.findByTipoNegocio(CrmDatabaseKey.NEGOCIO_J));
			ListaRegistro registro = _CrmGenericoService.getCatalogo(0,"","CATCANAL",0,"",usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
			registro.getLista().forEach(f->{
				mapaCanal.put(f.getCodigo(),f.getDescripcion());
				mapaArea.put(f.getCodigo(),f.getValor());
			});
			registro = _CrmGenericoService.getCatalogo(0,"","CATPRODUCTO",0,usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
			registro.getLista().forEach(f ->{
				mapaProducto.put(f.getCodigo(),f.getDescripcion());
			});
			registro = _CrmGenericoService.getCatalogo(0,"","CATPRODUCTO",0,"0004",usuario.getScreenName(),RegistroCotizacionesCrmPortlet73PortletKeys.REGISTROCOTIZACIONESCRMPORTLET73);
			registro.getLista().forEach(f ->{
				if(!mapaProducto.containsKey(f.getCodigo())){
					mapaProducto.put(f.getCodigo(),f.getDescripcion());
				}
			});

		}catch (Exception e){
			e.printStackTrace();
		}
		List<Catalogo_Detalle> tiposSociedad = _Catalogo_DetalleLocalService.findByCodigo("CATTIPSOC");
		Map<Integer,String> mapaSociedad = tiposSociedad.stream().map(m -> new Mapa((int)m.getCatalogoDetalleId(),m.getDescripcion())).collect(Collectors.toMap(Mapa::getId,Mapa::getDescripcion));
		String nombre;
		Map<Long,String> mapaAgente = new HashMap<>();
		Map<Long,String> mapaAgenteClave = new HashMap<>();
		Map<Long,String> mapaAgenteOficina = new HashMap<>();
		List<Catalogo_Detalle> oficias = _Catalogo_DetalleLocalService.findByCodigo("CATOFICINA");
		Map<Long,String> mapaOficias = oficias.stream().parallel().collect(Collectors.toMap(Catalogo_Detalle::getCatalogoDetalleId, Catalogo_DetalleModel::getDescripcion));
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

		List<Catalogo_Detalle> estatus = _Catalogo_DetalleLocalService.findByCodigo("CATESTATUS");
		Map<Integer,String> mapaEstatus = new HashMap<>();
		estatus.forEach(f->mapaEstatus.put((int)f.getCatalogoDetalleId(),f.getDescripcion()));
		renderRequest.setAttribute("mapaEstatus", mapaEstatus);
		renderRequest.setAttribute("mapaAgentes",mapaAgente);
		renderRequest.setAttribute("mapaAgentesClave",mapaAgenteClave);
		renderRequest.setAttribute("mapaAgenteOficina",mapaAgenteOficina);
		renderRequest.setAttribute("mapaCanal",mapaCanal);
		renderRequest.setAttribute("mapaProducto",mapaProducto);
		renderRequest.setAttribute("mapaArea",mapaArea);
		renderRequest.setAttribute("cotizacionLista",cotizacionList);
	}
}