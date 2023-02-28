$(document).ready(function (){
    let url = window.location.href;
    url = new URL(url);
    let codigo = url.searchParams.get("codigo");
    restartDatatable();
    if(codigo !== null && codigo !== "" ){
        $.getJSON(buscaClientesAutoCompleteURL,{term:codigo,tipo:3},function (data,status,xhr){
            let datosCliente = data.lista[0];
            $("#filtroRfc").val(datosCliente.rfc).siblings("label").addClass("active");
            $("#filtroNombreCompleto").val(datosCliente.nombre).siblings("label").addClass("active");
            $("#filtroCodigoCliente").val(datosCliente.codigo).siblings("label").addClass("active");
            $( "#rfcCliente" ).val(datosCliente.rfc);
            $( "#nombre" ).val(datosCliente.nombre);
            $( "#codigo" ).val( datosCliente.codigo );
            $("#altaCotizacion").attr("style","");
            $("#rfcNoExiste").attr("style","display:none;");
            $("#rfcExiste").attr("style","");

        });
    }
    $
});

$("#filtroNombreCompleto").autocomplete({
    minLength : 3,
    source : function(request, response) {
        $.getJSON( buscaClientesAutoCompleteURL, {
            term : request.term,
            tipo : 2,
        }, function(data, status, xhr) {
            sessionExtend();
            if (data.code !== 0) {
                showMessageError( '.navbar', data.msg, 0 );
                response( null );
            } else {
                if(data.lista.length > 0){
                    response( data.lista );
                }else{
                    response(null);
                }
            }
        } );
    },
    focus : function(event, ui) {
        $( "#filtroNombreCompleto" ).val(ui.item.nombre);
        return false;
    },
    select : function(event, ui) {
        $( "#filtroNombreCompleto" ).val( ui.item.nombre).siblings("label").addClass("active");
        $( "#filtroRfc" ).val( ui.item.rfc).siblings("label").addClass("active");
        $( "#filtroCodigoCliente" ).val( ui.item.codigo).siblings("label").addClass("active");
        $( "#rfcCliente" ).val( ui.item.rfc);
        $( "#nombre" ).val( ui.item.nombre);
        $( "#codigo" ).val( ui.item.codigo );
        $("#altaCotizacion").attr("style","");
        $("#rfcNoExiste").attr("style","display:none;")
        $("#rfcExiste").attr("style","");
        return false;
    },
    error : function(jqXHR, textStatus, errorThrown) {
        $("#rfcExiste").attr("style","display:none;");
        $("#altaCotizacion").attr("style","display:none;");
        $("#rfcNoExiste").attr("style","");
        showMessageError( '.navbar', msj.es.errorInformacion, 0 );
        console.error("autocomplete nombre");
    }
}).autocomplete( "instance" )._renderItem = function(ul, item) {
    return $("<li>").append("<div>" + item.nombre +  "</div>").appendTo(ul);
};

/* autocomplite por rfc */
$("#filtroRfc").autocomplete( {
    minLength : 3,
    source : function(request, response) {
        $.getJSON( buscaClientesAutoCompleteURL, {
            term : request.term,
            tipo : 1,
            /*pantalla : infCotizacion.pantalla*/
        }, function(data, status, xhr) {
            sessionExtend();
            if (data.code !== 0) {
                showMessageError( '.navbar', data.msg, 0 );
                console.error("autocomplete rfc");
                response( null );
            } else {
                if(data.lista.length > 0){
                    response( data.lista );
                }else{
                    response(null);
                }
            }
        } );
    },
    focus : function(event, ui) {
        $( "#filtroRfc" ).val( ui.item.rfc );
        return false;
    },
    select : function(event, ui) {
        $( "#filtroNombreCompleto" ).val( ui.item.nombre).siblings("label").addClass("active");
        $( "#filtroRfc" ).val( ui.item.rfc).siblings("label").addClass("active");
        $( "#filtroCodigoCliente" ).val( ui.item.codigo).siblings("label").addClass("active");
        $( "#rfcCliente" ).val( ui.item.rfc);
        $( "#nombre" ).val( ui.item.nombre);
        $( "#codigo" ).val( ui.item.codigo );
        $("#rfcNoExiste").attr("style","display:none;");
        $("#altaCotizacion").attr("style","");
        $("#rfcExiste").attr("style","");
        return false;
    },
    error : function(jqXHR, textStatus, errorThrown) {
        $("#rfcExiste").attr("style","display:none;")
        $("#altaCotizacion").attr("style","display:none;");
        $("#rfcNoExiste").attr("style","");
        showMessageError( '.navbar', msj.es.errorInformacion, 0 );
        console.error("autocomplete rfc");
    }
}).autocomplete( "instance" )._renderItem = function(ul, item) {
    return $("<li>").append("<div>" + item.nombre +  "</div>").appendTo(ul);
};

function clickCotizacion(cotizacionX) {
    console.log(cotizacionX);
    let cotizacion = cotizacionX;
    console.log($(cotizacion));
    console.log($(cotizacion).text());
    showLoader();
    $.post(buscaCotizacionFolioURL,{folio:$(cotizacion).text()}).done(function(data){
        let dato = JSON.parse(data);
        if(dato.code == 0){
            cotizacion.parentElement.parentElement.submit();
        }else{
            showMessageError( '.navbar', dato.msg, 0 );
            hideLoader();
        }
    });
}

$("#cotizacion").on("click",function (e){
    e.preventDefault();
    $("#altaCotizacionForm").submit();
    e.stopImmediatePropagation();
});

$("#buscar").on("click",function (e){
    e.preventDefault();
    showLoader();
    let objetoSend = {
        codigo: $("#filtroCodigoCliente").val()
    };
    $.post(buscaCotizacionesClienteURL,objetoSend).done(function (data){
        let dato = JSON.parse(data);
        let lista = dato.lista;
        if(dato.code === 0){
            if(lista.length > 0){
                let dt = $("#tableCotizaciones").DataTable();
                dt.clear();
                if (lista.length !== 0) {
                    dt
                        .clear()
                        .draw();
                }
                let form;
                let carpeta;
                let cliente = $("#filtroNombreCompleto").val();
                let codigo = $("#filtroCodigoCliente").val();
                $(lista).each(function (key,data){
                    form = "<form class='mb-4' action='" + buscaCotizacionFolio + "' method='post' id='verCotizacion-" + data.folio + "'><div><input type='hidden' name='folio' value='" + data.folio + "'><a href='#' onclick='clickCotizacion(this)'>" + data.folio + "</a></div></form>";
                    carpeta = "<a onclick='copyToClipboard(this);' style='color: #0275d8 !important;'>" + data.carpeta + "</a>";
                    dt.row.add([
                        form,
                        carpeta,
                        cliente,
                        codigo,
                        data.canal==null?"":data.canal,
                        data.area==null?"":data.area,
                        data.producto==null?"":data.producto,
                        data.cve_agente==null?"":data.cve_agente,
                        data.agente==null?"":data.agente,
                        data.ejecutivo==null?"":data.ejecutivo,
                        data.semaforo==null?"VERDE":data.semaforo,
                        data.oficina==null?"":data.oficina,
                        data.fec_mod==null?"":data.fec_mod,
                        data.poliza==null?"":data.poliza,
                        data.vigencia==null?"":data.vigencia,
                        data.cot_proceso==null?"":data.cot_proceso,
                        data.estatus==null?"":data.estatus
                    ]).draw(false);
                });
                $("#tableCotizacionesDiv").attr("style","");
            }else{
                showMessageError( '.navbar', "El cliente no tiene cotizaciones.", 0 );
            }
        }else{
            showMessageError( '.navbar', dato.msg, 0 );
        }
        hideLoader();
    });
    e.stopImmediatePropagation();
});

function restartDatatable(){
    $('.data-table-test').DataTable({
        scrollX:true,
        responsive: false,
        destroy: true,
        dom: 'fBrltip',
        ordering:false,
        ordered:false,
        buttons: [{
            extend:    'excelHtml5',
            text:      '<a class="btn-floating btn-sm teal"><i class="far fa-file-excel"></i></a>',
            titleAttr: 'Exportar XLS',
            className:"btn-unstyled",
        }],
        /*columnDefs: [
            {targets: '_all', className: "py-2" }
        ],*/
        lengthChange: true,
        language: {
            "url": spanishJson,

        },
        lengthMenu: [[5,10,15], [5,10,15]],
        pageLength: 10
    });
}

$("#darAltaCliente").on("click",function (e){
    e.preventDefault();
    showLoader();
    window.location="alta-de-clientes";
    hideLoader();
    e.stopImmediatePropagation();
});

$("#limpiar").on("click",function (e){
    e.preventDefault();
    showLoader();
    $('#tableCotizaciones').DataTable().rows().remove().draw();
    hideLoader();
    e.stopImmediatePropagation();
});

function copyToClipboard(elemento) {
    var $temp = $("<input>")
    $("body").append($temp);
    $temp.val($(elemento).text()).select();
    document.execCommand("copy");
    $temp.remove();
    showMessageSuccess( '.navbar', 'Enlace copiado', 0 );
}