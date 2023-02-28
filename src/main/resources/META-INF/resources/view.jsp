<%@ include file="./init.jsp" %>
<portlet:actionURL var="altaCotizacion" name="/crm/action/cotizacion/registraCotizacion" />
<portlet:actionURL var="buscaCotizacionFolio" name="/crm/action/cotizacion/registraCotizacion" />
<portlet:actionURL var="cargaDatosCotizacion" name="/crm/action/cotizacion/cargaDatosCotizacion" />
<portlet:resourceURL id="/crm/resource/cotizacion/buscaClientesAutoComplete" var="buscaClientesAutoCompleteURL" cacheability="FULL" />
<portlet:resourceURL id="/crm/resource/cotizacion/buscaCotizacionesCliente" var="buscaCotizacionesClienteURL" cacheability="FULL" />
<portlet:resourceURL id="/crm/resource/cotizacion/BuscaCotizacionFolio" var="buscaCotizacionFolioURL" cacheability="FULL" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css?v=${version}">

<div id="customAlertJS"></div>
<section class="REGISTROCOTIZACIONESCRMPORTLET73Portlet upper-case-all">
    <div class="section-heading">
        <div class="container-fluid">
            <h4 class="title text-left">
                <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.title" />
            </h4>
            <div class="section-nav-wrapper"></div>
            <div class="form-wrapper">
                <div class="row">
                    <div class="col">
                        <div class="row">
                            <div class="col-3">
                                <div class="md-form form-group">
                                    <input id="filtroRfc" type="text" maxlength="13" name="<portlet:namespace/>filtroRfc" class="form-control"/>
                                    <label for="filtroRfc">
                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.filtro.rfc"/>
                                    </label>
                                </div>
                            </div>
                            <div class="col-5">
                                <div class="md-form form-group">
                                    <input id="filtroNombreCompleto" type="text" name="<portlet:namespace/>filtroNombreCompleto" class="form-control"/>
                                    <label for="filtroNombreCompleto">
                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.filtro.nombre" />
                                    </label>
                                </div>

                            </div>
                            <div class="col-3">
                                <div class="md-form form-group">
                                    <input id="filtroCodigoCliente" type="text" name="<portlet:namespace/>filtroCodigoCliente" class="form-control" readonly disabled>
                                    <label for="filtroCodigoCliente">
                                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.filtro.codigo" />
                                    </label>
                                </div>
                            </div>
                            <div class="col-1"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" id="rfcNoExiste" >
                <div class="col-md-12">
                    <div id="darAltaCliente" class="btn btn-blue pull-center">
                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.filtro.darDeAlta"/>
                    </div>
                    <div id="cancelar" class="btn btn-blue pull-center">
                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.filtro.cancelar"/>
                    </div>
                </div>
            </div>
            <div class="row" id="rfcExiste" style="display: none">
                <div class="col-md-12">
                    <div id="buscar" class="btn btn-blue pull-center">
                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.filtro.buscar"/>
                    </div>
                    <div id="limpiar" class="btn btn-blue pull-center">
                        <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.filtro.limpiar"/>
                    </div>
                </div>
            </div>
            <c:if test="${manager != 1}">
                <div class="row" id="altaCotizacion" style="display: none">
                    <div class="col-sm-12 text-right">
                        <form class="mb-4" action="${altaCotizacion}" method="post" id="altaCotizacionForm">
                            <%--<input type="hidden" name="rfcCliente"  id = "rfcCliente" value="${agente.agenteId}"/>--%>
                            <input type="hidden" name="rfcCliente"  id = "rfcCliente"/>
                            <input type="hidden" name="nombre" id = "nombre"/>
                            <input type="hidden" name="codigo" id = "codigo"/>
                            <input type="hidden" name="folio" id = "folio"/>
                            <div id="cotizacion" class="btn btn-blue pull-right">
                                <liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.registro.form.cliente.button.alta" />
                            </div>
                        </form>
                    </div>
                    <div class="col-md-12"></div>
                </div>
            </c:if>
            <%--<div class="table-wrapper sin-filtro" id="tableCotizacionesDiv">
                <table class="table data-table-test table-bordered" id="tableCotizaciones">
                    <thead class="btn-blue" style="color: #FFFFFF; background-color: #43aee9">
                    <tr>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.folio" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.carpeta" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.cliente" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.codigo" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.canal" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.area" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.producto" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.claveAgente" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.nombreAgente" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.ejecutivo" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.oficina" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.fechaUltimaModificacion" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.poliza" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.vigencia" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.cotizacionProceso" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.estatus" /></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${cotizacionLista}" var="cotizacion">
                        <tr>
                            <td>
                                <form class="mb-4" action="${buscaCotizacionFolio}" method="post"
                                      id="verCotizacion${cotizacion.folio}}">
                                    <div>
                                        <input type="hidden" name="folio" value="${cotizacion.folio}">
                                        <a href="#" onclick="clickCotizacion(this)">${cotizacion.folio}</a>
                                    </div>
                                </form>
                            </td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>${mapaCanal[cotizacion.canal]}</td>
                            <td>${mapaArea[cotizacion.canal]}</td>
                            <td>${mapaProducto[cotizacion.producto]}</td>
                            <td>${mapaAgentesClave[cotizacion.id_agente]}</td>
                            <td>${mapaAgentes[cotizacion.id_agente]}</td>
                            <td>${cotizacion.ejecutivo}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${mapaSemaforo[cotizacion.id_semaforo] == 'VERDE'}">
                                        <img src="<%=request.getContextPath()%>/img/s-green.png" width="25" height="25">
                                    </c:when>
                                    <c:when test="${mapaSemaforo[cotizacion.id_semaforo] == 'AMARILLO'}">
                                        <img src="<%=request.getContextPath()%>/img/s-yellow.png" width="25" height="25">
                                    </c:when>
                                    <c:when test="${mapaSemaforo[cotizacion.id_semaforo] == 'ROJO'}">
                                        <img src="<%=request.getContextPath()%>/img/s-red.png" width="25" height="25">
                                    </c:when>
                                    <c:when test="${mapaSemaforo[cotizacion.id_semaforo] == 'NEGRO'}">
                                        <img src="<%=request.getContextPath()%>/img/s-yellow.png" width="25" height="25">
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>${mapaAgenteOficina[cotizacion.id_agente]}</td>
                            <td>${cotizacion.fecha_modificacion}</td>
                            <td></td>
                            <td>${cotizacion.fecha_inicio} - ${cotizacion.fecha_fin}</td>
                            <td>${mapaEstatus[cotizacion.estatus_cotizacion]}</td>
                            <td>${mapaEstatus[cotizacion.estatus_cotizacion]}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>--%>

            <div class="table-wrapper" id="tableCotizacionesDiv">
                <table class="table data-table-test table-bordered" id="tableCotizaciones">
                    <thead class="btn-blue" style="color: #FFFFFF; background-color: #43aee9">
                    <tr>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.folio" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.carpeta" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.cliente" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.codigo" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.canal" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.area" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.producto" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.claveAgente" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.nombreAgente" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.ejecutivo" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.dic_semaforo" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.oficina" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.fechaUltimaModificacion" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.poliza" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.vigencia" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.cotizacionProceso" /></th>
                        <th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.estatus" /></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${cotizacionLista}" var="cotizacion">
                        <tr>
                            <td>
                                <form class="mb-4" action="${buscaCotizacionFolio}" method="post"
                                      id="verCotizacion${cotizacion.folio}}">
                                    <div>
                                        <input type="hidden" name="folio" value="${cotizacion.folio}">
                                        <a href="#" onclick="clickCotizacion(this)">${cotizacion.folio}</a>
                                    </div>
                                </form>
                            </td>
                            <td></td>
                                <%--<td>${cotizacion.cliente}</td>
                                <td>${cotizacion.codigo}</td>--%>
                            <td></td>
                            <td></td>
                            <td>${mapaCanal[cotizacion.canal]}</td>
                            <td>${mapaArea[cotizacion.canal]}</td>
                            <td>${mapaProducto[cotizacion.producto]}</td>
                            <td>${mapaAgentesClave[cotizacion.id_agente]}</td>
                            <td>${mapaAgentes[cotizacion.id_agente]}</td>
                            <td>${cotizacion.ejecutivo}</td>
                            <td>${mapaSemaforo[cotizacion.id_semaforo]}</td>
                            <td>${mapaAgenteOficina[cotizacion.id_agente]}</td>
                            <td>${cotizacion.fecha_modificacion}</td>
                            <td></td>
                            <td>${cotizacion.fecha_inicio} - ${cotizacion.fecha_fin}</td>
                            <td>${mapaEstatus[cotizacion.estatus_cotizacion]}</td>
                            <td>${mapaEstatus[cotizacion.estatus_cotizacion]}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row">
                <br/>
                <br/>
                <br/>
            </div>
        </div>
    </div>
</section>
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/main.js?v=${version}"></script>
<script>
    const buscaClientesAutoCompleteURL = '${buscaClientesAutoCompleteURL}';
    const buscaCotizacionesClienteURL = '${buscaCotizacionesClienteURL}';
    const buscaCotizacionFolio = "${buscaCotizacionFolio}";
    const buscaCotizacionFolioURL = "${buscaCotizacionFolioURL}";
    const spanishJson = "<%=request.getContextPath()%>" + "/js/dataTables.spanish.json";
</script>