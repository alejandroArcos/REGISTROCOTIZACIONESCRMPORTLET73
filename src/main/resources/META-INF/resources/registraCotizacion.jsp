<%--
  Created by IntelliJ IDEA.
  User: erickalvarez
  Date: 28/09/21
  Time: 11:44
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="./init.jsp"%>
<jsp:useBean id="current" class="java.util.Date" />

<portlet:resourceURL id="/crm/resource/cotizacion/buscaCatalogo" var="buscaCatalogoURL" cacheability="FULL"/>
<portlet:resourceURL id="/crm/resource/cotizacion/validaCotizacion" var="validaCotizacionURL" cacheability="FULL"/>
<portlet:resourceURL id="/crm/resource/cotizacion/buscaParticipanteAsegurador" var="buscaParticipanteAseguradorURL" cacheability="FULL"/>
<portlet:resourceURL id="/crm/resource/cotizacion/buscaRenovacionCotizacion" var="buscaRenovacionCotizacionURL" cacheability="FULL"/>
<portlet:resourceURL id="/crm/resource/cotizacion/guardarCotizacion" var="guardarCotizacionURL" cacheability="FULL"/>
<portlet:resourceURL id="/crm/resource/cotizacion/guardarCotizacionManager" var="guardarCotizacionManagerURL" cacheability="FULL"/>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css?v=${version}">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/fontawesome-all.css">

<div id="customAlertJS"></div>
<div class="REGISTROCOTIZACIONESCRMPORTLET73Portlet upper-case-all">
    <section class="">
        <!-- "upper-case-all">-->
        <div class="section-heading">
            <div class="container-fluid">
                <h4 class="title text-left">
                    <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.title" />
                </h4>
                <div class="section-nav-wrapper"></div>
                <div class="form-wrapper">
                    <section id="formCotizacion" class="bodyFields">
                        <div class="accordion md-accordion" id="accordionEx" role="tablist" aria-multiselectable="true">
                            <!-- Accordion card -->
                            <div class="card ">
                                <!-- Card header -->
                                <div class="card-header btn-blue modificado" role="tab" id="headingDatosCliente">
                                    <a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#collapseDatosCliente" aria-expanded="false" aria-controls="collapseDatosCliente">
                                        <h5 class="mb-0">
                                            <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.accordion.cliente.general"/>
                                        </h5>
                                        <i class="fas fa-angle-down rotate-icon"></i>
                                    </a>
                                </div>
                                <div id="collapseDatosCliente" class="collapse in" role="tabpanel" aria-labelledby="headingDatosCliente" data-parent="#accordionEx">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <input type="text" maxlength="13" name="dc_rfc" id="dc_rfc" value="${cliente.rfc}" class="form-control numerosLetras" readonly disabled>
                                                    <label for="dc_rfc">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dc_rfc" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <input type="text" name="dc_nombre_cliente" id="dc_nombre_cliente" value="${cliente.nombre}" class="form-control" readonly disabled>
                                                    <label for="dc_nombre_cliente">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dc_nombre_cliente" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <input type="text" name="dc_codigo_cliente" id="dc_codigo_cliente" value="${cliente.codigo}" class="form-control" readonly disabled>
                                                    <label for="dc_codigo_cliente">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dc_codigo_cliente" />
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card ">
                                <!-- Card header -->
                                <div class="card-header btn-blue modificado" role="tab" id="headingDatosDireccion">
                                    <a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#collapseDatosDireccion" aria-expanded="false" aria-controls="collapseDatosDireccion">
                                        <h5 class="mb-0">
                                            <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.accordion.cliente.direccion"/>
                                        </h5>
                                        <i class="fas fa-angle-down rotate-icon"></i>
                                    </a>
                                </div>
                                <div id="collapseDatosDireccion" class="collapse in" role="tabpanel" aria-labelledby="headingDatosDireccion" data-parent="#accordionEx">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-3">
                                                <div class="md-form form-group">
                                                    <input type="text" name="dr_cp" id="dr_cp" value="${cliente.cp}" maxlength="5" class="form-control cpValido" readonly disabled>
                                                    <label for="dr_cp">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dr_cp" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-6">
                                                <div class="md-form form-group">
                                                    <input type="text" name="dr_calle" id="dr_calle" value="${cliente.calle}" class="form-control" readonly disabled>
                                                    <label for="dr_calle">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dr_calle" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col">
                                                <div class="md-form form-group">
                                                    <input type="text" name="dr_numero" id="dr_numero" value="${cliente.num_ext}" class="form-control" readonly disabled>
                                                    <label for="dr_numero">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dr_numero" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col">
                                                <div class="md-form form-group">
                                                    <input type="text" name="dr_numeroi" id="dr_numeroi" value="${cliente.num_int}" class="form-control" readonly disabled>
                                                    <label for="dr_numeroi">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dr_numeroi" />
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col">
                                                <div class="md-form form-group">
                                                    <select name="dr_colonia" id="dr_colonia" class="mdb-select" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar"/>' disabled>
                                                        <option value="-1">Seleccionar</option>
                                                        <c:forEach items="${cpData}" var="cpItem">
                                                            <option value="${cpItem.id}" ${cpItem.id==cliente.id_colonia?'selected':''}>${cpItem.colonia}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <label for="dr_colonia">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dr_colonia" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col">
                                                <div class="md-form form-group">
                                                    <input type="text" name="dr_municipio" id="dr_municipio" value="${cliente.municipio}" class="form-control" readonly disabled>
                                                    <label for="dr_municipio">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dr_municipio" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col">
                                                <div class="md-form form-group">
                                                    <input type="text" name="dr_estado" id="dr_estado" value="${cliente.estado}" class="form-control" readonly disabled>
                                                    <label for="dr_estado">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dr_estado" />
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card ">
                                <!-- Card header -->
                                <div class="card-header btn-blue modificado" role="tab" id="headingInfoCotizacion">
                                    <a class="collapsed" data-toggle="collapse" data-parent="#accordionEx" href="#collapseInfoCotizacion" aria-expanded="false" aria-controls="collapseInfoCotizacion">
                                        <h5 class="mb-0">
                                            <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.accordion.cliente.infoCotizacion"/>
                                        </h5>
                                        <i class="fas fa-angle-down rotate-icon"></i>
                                    </a>
                                </div>
                                <div id="collapseInfoCotizacion" class="collapse in" role="tabpanel" aria-labelledby="headingInfoCotizacion" data-parent="#accordionEx">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_agente" id="dic_agente" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar"/>'>
                                                        <option value="-1" disabled selected>Seleccionar</option>
                                                        <c:forEach items="${agentes}" var="agente">
                                                            <option value="${agente.agenteId}" ${agente.agenteId==cotizacion.id_agente?'selected':''} clave="${agente.clave}" preclave="${agente.preclave}">
                                                                <c:if test="${agente.tipoPersona == '1'}">
                                                                    ${agente.nombre} ${agente.apellidoP} ${agente.apellidoM}
                                                                </c:if>
                                                                <c:if test="${agente.tipoPersona == '2'}">
                                                                    ${agente.nombre} ${mapaSociedad[agente.tipoSociedad]}
                                                                </c:if>
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                    <label for="dic_agente">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_agente" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_negocio" id="dic_negocio" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar"/>'>
                                                        <option value="-1" selected disabled>Seleccionar</option>
                                                        <option value="1" ${cotizacion.tipo_negocio== 1?'selected':''}>DIRECTO</option>
                                                        <option value="2" ${cotizacion.tipo_negocio== 2?'selected':''}>COASEGURO L&Iacute;DER (CEDIDO)</option>
                                                        <option value="3" ${cotizacion.tipo_negocio== 3?'selected':''}>COASEGURO SEGUIDOR (ACEPTADO)</option>
                                                        <option value="4" ${cotizacion.tipo_negocio== 4?'selected':''}>REASEGURO L&Iacute;DER (FACULTATIVO)</option>
                                                        <option value="5" ${cotizacion.tipo_negocio== 5?'selected':''}>REASEGURO SEGUIDOR (TOMADO)</option>
                                                    </select>
                                                    <label for="dic_negocio">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_negocio" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_producto" id="dic_producto" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar"/>'>
                                                        <option value="-1">Seleccionar</option>
                                                        <c:forEach items="${productos}" var="producto">
                                                            <option value="${producto.id}" codigo="${producto.codigo}" ${producto.codigo==cotizacion.producto?'selected':''}>
                                                                    ${producto.descripcion}
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                    <label for="dic_producto">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_producto" />
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_moneda" id="dic_moneda" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar" />'>
                                                        <option value="-1" selected disabled>Seleccionar</option>
                                                        <c:forEach items="${listMoneda}" var="option">
                                                            <option value="${option.id}" ${option.id==cotizacion.id_moneda?'selected':''}>${option.valor}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <label for="dic_moneda">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_moneda" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <input type="number" maxlength="9" step="0.01" name="dic_prima" id="dic_prima" value="${cotizacion.prima}" class="numeros form-control" >
                                                    <label for="dic_prima">
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_prima"/>
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_movimiento" id="dic_movimiento" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar"/>'>
                                                        <option value="-1">Seleccionar</option>
                                                        <option value="1" ${cotizacion.tipo_movimiento== 1?'selected':''}>Nuevo</option>
                                                        <option value="2" ${cotizacion.tipo_movimiento== 2?'selected':''}>Renovaci&oacute;n</option>
                                                    </select>
                                                    <label for="dic_movimiento">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_movimiento" />
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-3">
                                                <div class="md-form form-group">
                                                    <input placeholder="" type="date" id="dic_fechaInicio" name="dic_fechaInicio" class="form-control datepicker requerido" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${cotizacion.fecha_inicio}"/>"/>
                                                    <label for="dic_fechaInicio">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_fechaInicio" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-3">
                                                <div class="md-form form-group">
                                                    <input placeholder="" type="date" id="dic_fechaTermino" name="dic_fechaTermino" class="form-control datepicker requerido" value='<fmt:formatDate pattern = "yyyy-MM-dd" value = "${cotizacion.fecha_fin}"/>'>
                                                    <label for="dic_fechaTermino">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_fechaTermino" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-3">
                                                <div class="md-form form-group">
                                                    <input placeholder="" type="date" id="dic_fechaSolicitud" name="dic_fechaSolicitud" class="form-control requerido" value='<fmt:formatDate pattern = "yyyy-MM-dd" value = "${cotizacion.fecha_solicitud}" />'>
                                                    <label for="dic_fechaSolicitud">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_fechaSolicitud" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-3">
                                                <div class="md-form form-group">
                                                    <input placeholder="" type="date" id="dic_fechaAltaFolio" name="dic_fechaAltaFolio" class="form-control datepicker requerido" value='<fmt:formatDate pattern = "yyyy-MM-dd" value = "${cotizacion.fecha_alta}" />' disabled>
                                                    <label for="dic_fechaInicio">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_fechaAltaFolio" />
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_controlSub" id="dic_controlSub" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar"/>'>
                                                        <option value="-1">Seleccionar</option>
                                                        <option value="1" ${cotizacion.control_subscripcion == 1?'selected':''}>Respuesta Cliente</option>
                                                        <option value="2" ${cotizacion.control_subscripcion == 2?'selected':''}>Cotizaci&oacute;n</option>
                                                        <c:if test="${autorizacion == false}">
                                                            <option value="3" ${cotizacion.control_subscripcion == 3?'selected':''} >Requiere Autorizaci&oacute;n</option>
                                                        </c:if>
                                                    </select>
                                                    <label for="dic_controlSub">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_controlSub" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <input type="email" name="dic_email" id="dic_email" value="${cotizacion.email}" class="form-control requerido">
                                                    <label for="dic_email" >
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_email" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="custom-control custom-checkbox">
                                                    <input type="checkbox" class="custom-control-input" id="dic_gpFlag" name="dic_gpFlag" ${cotizacion.gp_flag == 1 ? 'checked':''}>
                                                    <label for="dic_gpFlag" >
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_gpFlag" />
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_canal" id="dic_canal" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar" />'>
                                                        <option value="-1" selected disabled>Seleccionar</option>
                                                        <c:forEach items="${listCanalNegocio}" var="option">
                                                            <option value="${option.id}" ${option.codigo==cotizacion.canal?'selected':''} codigo="${option.codigo}" >${option.descripcion}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <label for="dic_canal">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_canal" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_ejecutivo" id="dic_ejecutivo" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar" />'>
                                                        <option value="-1" selected disabled>Seleccionar</option>
                                                        <c:forEach items="${listEjecutivos}" var="option">
                                                            <option value="${option.id}" ${option.id==cotizacion.id_ejecutivo?'selected':''}>${option.valor}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <label for="dic_ejecutivo">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_ejecutivo" />
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="md-form form-group">
                                                    <select name="dic_semaforo" id="dic_semaforo" class="mdb-select requerido" searchable='<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73Portlet.buscar" />'>
                                                        <option value="-1" selected disabled>Seleccionar</option>
                                                        <c:forEach items="${listaSemaforo}" var="option">
                                                            <option value="${option.id}" ${option.id==cotizacion.id_semaforo?'selected':''}>${option.valor}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <label for="dic_semaforo">
                                                        <i class="fa fa-star red-text fa-xs" aria-hidden="true"></i>
                                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.dic_semaforo" />
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                    <c:if test="${manager == 1}">
                        <%@ include file="./tablaCotizacionesPrevias.jsp"%>
                    </c:if>
                    <c:choose>
                        <c:when test="${manager == 1}">
                            <section class="manager">
                                <div class="row text-center">
                                    <div class="col-md"></div>
                                    <div class="col-md-1.5">
                                        <button class="btn btn-blue" id="regresarManager">
                                            <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.button.return"/>
                                        </button>
                                    </div>
                                    <div class="col-md-1.5">
                                        <button class="btn btn-pink cancelar" id="cancelar">
                                            <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.button.cancel"/>
                                        </button>
                                    </div>
                                    <div class="col-md"></div>
                                </div>
                            </section>
                        </c:when>
                        <c:when test="${manager == 0}">
                            <section>
                                <div class="row text-center">
                                    <div class="col-md"></div>
                                    <div class="col-md-1.5" style="display: none">
                                        <button class="btn btn-blue" id="solicitarAutorizacion"><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.button.autorizacionManager"/></button>
                                    </div>
                                    <div class="col-md-1.5" style="display: none">
                                        <button class="btn btn-blue" id="autorizarContizacion"><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.button.submit"/></button>
                                    </div>
                                    <div class="col-md-1.5">
                                        <button class="btn btn-pink cancelar"><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.button.cancel"/></button>
                                    </div>
                                    <div class="col-md"></div>
                                </div>
                            </section>
                        </c:when>
                    </c:choose>
                    <form action="" id="fin"></form>
                </div>
            </div>
        </div>
    </section>
    <div class="modal" id="modal-aseguradores" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable" role="document">
            <div class="modal-content">
                <div class="modal-header  blue-gradient text-white">
                    <h5 class="modal-title" id="modal-aseguradores-title"><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.modal.clave.title" /></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body ui-front">
                    <div class="form-wrapper">
                        <div class="row justify-content-center">
                            <div class="col-4">
                                <div class="md-form form-group">
                                    <input type="text"  name="da_nombre" id="da_nombre" class="form-control">
                                    <label for="da_nombre">
                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.modal.form.asegurador" />
                                    </label>
                                </div>
                            </div>
                            <div class="col-2">
                                <div class="md-form form-group">
                                    <input type="number"  step="1" id="da_participacion" name="da_participacion" class="form-control">
                                    <label for="da_participacion">
                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.modal.form.porcentaje" />
                                    </label>
                                </div>
                            </div>
                            <div class="col-2">
                                <div class="md-form form-group">
                                    <a class="btn-floating btn-sm teal" id="agregaAsegurador">
                                        <i class="fas fa-plus"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="col-2">
                                <div class="md-form form-group">
                                    <input type="hidden" id="da_codigo" name="da_codigo" class="form-control">
                                    <label for="da_codigo">
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="row justify-content-center">
                            <div id="tablaAseguradoresDiv">
                                <table class="table data-table table-bordered" style="width: 100%;" id="tablaAseguradores">
                                    <thead class="btn-blue" style="color: #FFFFFF; background-color: #43aee9">
                                        <th></th>
                                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.modal.title.codigo" /></th>
                                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.modal.title.participante" /></th>
                                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.modal.title.participacion" /></th>
                                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.modal.title.accion" /></th>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" id="continuarCoaseguradores" style="display:none;">
                    <button type="button" class="btn btn-blue" data-dismiss="modal" aria-label="Close">
                        Continuar
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal" id="modal-renovacion" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable" role="document">
            <div class="modal-content">
                <div class="modal-header  blue-gradient text-white">
                    <h5 class="modal-title"><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.modal.form.renovacion" /></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body ui-front">
                    <div class="form-wrapper">
                        <div class="row justify-content-center">
                            <div class="col-4">
                                <div class="md-form form-group">
                                    <input type="text"  name="da_folioRenovacio" id="da_folioRenovacio" class="form-control">
                                    <label for="da_folioRenovacio">
                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.modal.form.poliza.renovacion" />
                                    </label>
                                </div>
                            </div>
                            <div class="col-2">
                                <div class="md-form form-group">
                                    <button class="btn btn-blue" id="buscarRenovacion"><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.filtro.buscar"/></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/registraCotizacion.js?v=${version}"></script>

<script type="text/javascript">
    const buscaCatalogoURL = "${buscaCatalogoURL}";
    const validaCotizacionURL = "${validaCotizacionURL}";
    const buscaParticipanteAseguradorURL = "${buscaParticipanteAseguradorURL}";
    const buscaRenovacionCotizacionURL = "${buscaRenovacionCotizacionURL}";
    const guardarCotizacionURL = "${guardarCotizacionURL}";
    const guardarCotizacionManagerURL = "${guardarCotizacionManagerURL}";
    const manager = "${manager}";
    const soloLectura = "${soloLectura}";
    const spanishJson = "<%=request.getContextPath()%>" + "/js/dataTables.spanish.json";
</script>