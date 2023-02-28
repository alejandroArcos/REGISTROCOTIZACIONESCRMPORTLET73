<%@ include file="./init.jsp"%>
<section>
	<div class="row">		
		<div class="col">
			<h5 class="title text-center">
				<liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.title.fl_observaciones" />
			</h5>
		</div>		
	</div>
	<div class="row">
		<div class="col-3"></div>
		<div class="col-6">
			<div class="table-wrapper sin-filtro sin-registros">
				<table class="table data-table table-striped table-bordered"
					style="width: 100%;" id="tableAgentesCRM">
					<!--  tablaAgente -->
					<thead class="btn-blue">
						<tr>
							<th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.folio" /></th>
							<th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.ejecutivo" /></th>
							<!--<th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.oficina" /></th>-->
							<th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.nombreAgente" /></th>
							<th><liferay-ui:message key="REGISTROCOTIZACIONESCRMPORTLET73.table.title.acciones" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${cotizacionesPrevias}" var="cotizacionPrevia">
							<tr>
								<td>${cotizacionPrevia.folio}</td>
								<td>${cotizacionPrevia.ejecutivo}</td>
								<%--<td>${mapaOficinas[cotizacionPrevia.oficinaId]}</td>--%>
								<td>${mapaAgentes[cotizacionPrevia.id_agente]}</td>
								<td>
									<a onclick="autorizaCotizacion(${cotizacionPrevia.id_cotizacion});" class="btn-floating btn-green btn-sm">
										<i class="fas fa-check-circle"></i>
									</a>
									<a onclick="rechazaCotizacion(${cotizacionPrevia.id_cotizacion});" class="btn-floating btn-red btn-sm">
										<i class="fas fa-times-circle"></i>
									</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="col-3"></div>
	</div>
</section>