let aseguradores;

let fechatest;

$(document).ready(function (){
    window.scrollTo(0, 0);
    $('.datepicker').pickadate({
        format : 'yyyy-mm-dd',
        formatSubmit : 'yyyy-mm-dd',
        yearRange: "1970:+nn",
        yearDropdownItemNumber:100,
        scrollableYearDropdown:true
    });

    fechatest = $('#dic_fechaSolicitud').pickadate({
        format : 'yyyy-mm-dd',
        formatSubmit : 'yyyy-mm-dd',
        max: new Date(),
        yearRange: "1970:+nn",
        yearDropdownItemNumber:100,
        scrollableYearDropdown:true
    });

    if($("#dic_fechaAltaFolio").val() !== undefined && $("#dic_fechaAltaFolio").val() === ""){
        let hoy = new Date();
        let fecha = hoy.toISOString().split("T");
        $("#dic_fechaAltaFolio").val(fecha[0]);
    }

    $("#dic_negocio").change(function (e){
        e.preventDefault();
        let negocio = $("#dic_negocio option:selected");
        let objetoSend = {
            valor: "000" + (parseInt(negocio.val())),
            catalogo:"CATPRODUCTO"
        };

        $.when(buscaCatalogos(objetoSend,"dic_producto","")).done(function (){
            aseguradores = new Array();
            if(negocio.val() !== '1'){
                $("#modal-aseguradores").modal("show");
                $("#da_nombre").val("");
                $("#da_participacion").val("");
                $("#da_codigo").val("");
                restartDatatable("tablaAseguradores");
            }
        });
        e.stopImmediatePropagation();
    });

    $("#dic_canal").change(function (e){
        e.preventDefault();
        let objetoSend = {
            valor: $("#dic_canal option:selected").attr("codigo"),
            catalogo:"CATEJECUTIVOS"
        };
        buscaCatalogos(objetoSend,"dic_ejecutivo","");
        validaCotizacion();
        e.stopImmediatePropagation();
    });

    $("#dic_producto").change(function (e){
        e.preventDefault();
        validaCotizacion();
        e.stopImmediatePropagation();
    });

    $("#dic_ejecutivo").change(function (e){
        e.preventDefault();
        validaCotizacion();
        e.stopImmediatePropagation();
    });

    $("#agregaAsegurador").click(function (e){
        e.preventDefault();
        agregaAsegurador();
        e.stopImmediatePropagation();
    });

    $("#dic_fechaInicio").change(function (e){
        e.preventDefault();
        let fechaInicio = $("#dic_fechaInicio").val();
        let fecha = new Date(fechaInicio + 'T00:00:00');
        let fechaFin = new Date(fecha.getFullYear() + 1, fecha.getMonth(), fecha.getDate());
        $("#dic_fechaTermino").val(fechaFin.toISOString().split('T')[0]).siblings('label').addClass('active');
        e.stopImmediatePropagation();
    });

    $("#dic_movimiento").change(function (e){
        e.preventDefault();
        if($("#dic_movimiento option:selected").val() === '2'){
            $("#modal-renovacion").modal('show');
        }
        e.stopImmediatePropagation();
    });

    $( "#da_nombre" ).autocomplete({
        minLength : 3,
        source : function(request, response) {
            $.getJSON( buscaParticipanteAseguradorURL, {
                nombre : request.term,
                participante: aseguradores.length,
                negocio: "000" + (parseInt($("#dic_negocio option:selected").val()))
            }, function(data, status, xhr) {
                sessionExtend();
                if (data.code !== 0) {
                    showMessageError( '.navbar', data.msg, 0 );
                    response( null );
                } else {
                    if(data.lista.length > 0){
                        response(data.lista);
                    }else{
                        showMessageError( '.navbar', "La consulta no trae datos", 0 );
                        response(null);
                    }
                }
            } );
        },
        focus : function(event, ui) {
            $( "#da_nombre" ).val(ui.item.nombre);
            return false;
        },
        select : function(event, ui) {
            $( "#da_nombre" ).val(ui.item.nombre);
            $( "#da_codigo" ).val(ui.item.codigo);
            return false;
        },
        error : function(jqXHR, textStatus, errorThrown) {
            showMessageError( '.navbar', msj.es.errorInformacion, 0 );
            console.error("autocomplete nombre");
        }
    }).autocomplete( "instance" )._renderItem = function(ul, item) {
        return $("<li>").append("<div>" + item.nombre +  "</div>").appendTo(ul);
    };

    $("#buscarRenovacion").click(function (e){
        showLoader();
        let objetoSend = {
            folio: $("#da_folioRenovacio").val()
        };
        $.post(buscaRenovacionCotizacionURL,objetoSend).done(function (data){
            let dato = JSON.parse(data);
            console.log(dato);
            if(dato.code === '0' || dato.code === 'OK' ){
                preSeleccionarSelect("dic_agente",dato.agente,dato.agente.startsWith("01")?'clave':'preclave');
                preSeleccionarSelect("dic_negocio",dato.negocio,"value");
                $.when(buscaCatalogos({valor: "000" + (parseInt(dato.negocio)), catalogo:"CATPRODUCTO"},"dic_producto",dato.producto)).done(function (){
                    $.when($('#dic_producto').trigger('change')).done(function () {
                        preSeleccionarSelect("dic_canal", dato.canal, "value");
                        $.when(buscaCatalogos({valor: $("#dic_canal option:selected").attr("codigo"), catalogo:"CATEJECUTIVOS"},"dic_ejecutivo",dato.ejecutivo)).done(function () {
                            preSeleccionarSelect("dic_moneda",dato.moneda,"value");
                            aseguradores = dato.participantes;
                            $("#dic_fechaAltaFolio").val(dato.fec_alta).siblings("label").addClass("active");
                            $("#dic_fechaInicio").val(dato.fec_ini).siblings("label").addClass("active");
                            $("#dic_fechaTermino").val(dato.fec_fin).siblings("label").addClass("active");
                            hideLoader();
                        });
                    });
                });
            }else{
                showMessageError( '.navbar', dato.msg, 0 );
            }
            $("#modal-renovacion").modal("hide");
            hideLoader();
        });
    });

    $("#solicitarAutorizacion").click(function (e){
        e.preventDefault();
        guardarCotizacion(true);
        e.stopImmediatePropagation();
    });

    $("#autorizarContizacion").click(function (e){
        e.preventDefault();
        guardarCotizacion(false);
        e.stopImmediatePropagation();
    });

    if(soloLectura == 1){
        $("#collapseInfoCotizacion input, #collapseInfoCotizacion select").prop("disabled", true);
    }

    $(".cancelar").click(()=>{$('#fin').submit();});
});



function preSeleccionarSelect(selector,opcion,attr){
    $('#' + selector).material_select('destroy');
    $("#" + selector).find('option').each(function (){
        if(attr === 'value'){
            if($(this).val() === opcion){
                $(this).attr("selected","selected");
                $(this).prop('selected', true);
            }
        }else{
            if($(this).attr(attr) === opcion){
                console.log($(this).attr(attr));
                $(this).prop('selected', true);
            }
        }
    });
    $('#' + selector).material_select();
}

function agregaOpciones(opciones,name,preselect){
    return new Promise((resolve) => {
        let o;
        let select = $("#" + name);
        select.material_select('destroy');
        select
            .find('option')
            .remove().end()
            .append('<option value="" >Seleccionar</option>');
        for(let opcion of opciones){
            if(opcion.id === preselect || opcion.codigo === preselect){
                o = '<option value="' + opcion.id + '" codigo="' + opcion.codigo + '" selected="selected">' + opcion.descripcion + ' </option>';
            }else {
                o = '<option value="' + opcion.id + '" codigo="' + opcion.codigo + '">' + opcion.descripcion + '</option>';
            }
            select.append(o);
        }
        select.material_select();
        resolve(select);
    });
}

function  buscaCatalogos (objetoSend,name,select){
    return new Promise((resolve) => {
        $.post(buscaCatalogoURL, objetoSend).done(function (data) {
            let dato = JSON.parse(data);
            let lista = dato.lista;
            $.when(agregaOpciones(lista,name,select)).done(function (){
                resolve(true);
            });
        });
    });
}

function validaCotizacion (){
    let objetoSend = {
        cliente:$("#dc_codigo_cliente").val(),
        producto:$("#dic_producto option:selected").attr("codigo"),
        canal:$("#dic_canal option:selected").attr("codigo"),
        ejecutivo:$("#dic_ejecutivo option:selected").attr("codigo")
    };
    let contador = 0;
    for(let valor in objetoSend){
        if(objetoSend[valor] === undefined || objetoSend[valor] === ""){
            contador++;
        }
    }
    if(contador > 0){
        return false;
    }else{
        $.post(validaCotizacionURL,objetoSend).done(function (data){
            let datos = JSON.parse(data);
            if(datos.valida){
                $("#solicitarAutorizacion").parent().attr("style","display:none;");
                $("#autorizarContizacion").parent().attr("style","");
            }else{
                $("#autorizarContizacion").parent().attr("style","display:none;");
                $("#solicitarAutorizacion").parent().attr("style","");
            }
        });
    }
}

function agregaAsegurador(){
    if($("#da_nombre").val() != "" && $("#da_participacion").val() != "" && $("#da_codigo").val() != "") {
        let row = new Object();
        if ($("#dic_negocio option:selected").val() === 2) {
            /*TODO validar los codigos de Tokio*/
        }
        switch ($("#dic_negocio").val()) {
            case "2":
                row.texto = "CEDIDO";
                break;
            case "3":
                row.texto = "ACEPTADO";
                break;
            case "4":
                row.texto = "FACULTATIVO";
                break;
            case "5":
                row.texto = "TOMADO";
                break;
        }
        row.codigo = $("#da_codigo").val();
        row.nombre = $("#da_nombre").val();
        row.porcentaje = parseInt($("#da_participacion").val());
        let total = 0.0;
        let repetidos = 0;
        if (aseguradores.length > 0) {
            for (let participacion of aseguradores) {
                if (participacion.codigo === row.codigo) {
                    repetidos++;
                }
                total += participacion.porcentaje;
            }
        }
        if (repetidos > 0) {
            showMessageError('.navbar', "El participante que se desea agregar ya se encuentra en la lista", 0);
            return false;
        }
        if (total <= 100) {
            if ((total + row.porcentaje) <= 100) {
                if (row.porcentaje === 100) {
                    showMessageError('.navbar', "El porcentaje no puede ser del 100%", 0);
                } else {
                    total += row.porcentaje;
                    aseguradores.push(row);
                }
            } else {
                showMessageError('.navbar', "El porcentaje del participante excede a la suma total del 100%", 0);
                return false;
            }
        } else {
            showMessageError('.navbar', "El porcentaje de participaciones ya esta completo", 0);
            return false;
        }
        let dt = $("#tablaAseguradores").DataTable();
        dt.clear().draw();
        let boton;
        for (let participacion of aseguradores) {
            boton = '<a class="btn-floating btn-sm teal" onclick="elimina(\'' + participacion.codigo + '\');"><i class="fas fa-minus"></i></a>'
            dt.row.add([
                participacion.texto,
                participacion.codigo,
                participacion.nombre,
                participacion.porcentaje,
                boton
            ]).draw(false);
        }
        dt.row.add([
            "",
            "",
            "Total",
            total + "%",
            ""
        ]).draw(false);
        if(total == 100){
            $("#continuarCoaseguradores").attr("style","");
        }else{
            $("#continuarCoaseguradores").attr("style","display:none;");
        }
        $("#da_participacion").val("");
        $("#da_nombre").val("");
        $("#da_codigo").val("");
    }else{
        showMessageError('.navbar', "Debe seleccionar una opción de la lista.", 0);
        return false;
    }
}

function elimina(codigo){
    let dtr = $("#tablaAseguradores").DataTable();
    dtr.clear().draw();
    let boton;
    let total = 0;
    let aseguradoresAux = new Array();

    for(let participacion of aseguradores){
        if(participacion.codigo !== codigo){
            aseguradoresAux.push(participacion)
            boton = '<a class="btn-floating btn-sm teal" onclick="elimina(\'' + participacion.codigo + '\');"><i class="fas fa-minus"></i></a>';
            total += participacion.porcentaje;
            dtr.row.add([
                participacion.texto,
                participacion.codigo,
                participacion.nombre,
                participacion.porcentaje,
                boton
            ]).draw(false);
        }
    }

    dtr.row.add([
        "",
        "",
        "Total",
        total + "%",
        ""
    ]).draw(false);

    $("#continuarCoaseguradores").attr("style","display:none;");
    
    aseguradores = aseguradoresAux;
}

function guardarCotizacion(validacion){
    showLoader();
    if(validaRequeridos()){
        let total = 0;
        if($("#dic_negocio option:selected").val() !== "1" ) {
            for (let participacion of aseguradores) {
                total += participacion.porcentaje;
            }
            if (total < 100) {
                showMessageError('.navbar', "El porcentaje de los participantes no es del 100%", 0);
                return false;
            }
        }
        let objetoSend = {
            codigo:$("#dc_codigo_cliente").val(),
            nombreCliente:$("#dc_nombre_cliente").val(),
            agente:$("#dic_agente option:selected").text(),
            clave:$("#dic_agente option:selected").attr("clave"),
            preclave: $("#dic_agente option:selected").attr("preclave"),
            idAgente:$("#dic_agente option:selected").val(),
            tipoNegocio:$("#dic_negocio option:selected").val(),
            producto: $("#dic_producto option:selected").attr("codigo"),
            moneda: $("#dic_moneda option:selected").val(),
            prima: $("#dic_prima").val(),
            movimiento: $("#dic_movimiento option:selected").val(),
            fechaInicio: $("#dic_fechaInicio").val(),
            fechaTermino: $("#dic_fechaTermino").val(),
            fechaSolicitud: $("#dic_fechaSolicitud").val(),
            fechaAlta: $("#dic_fechaAltaFolio").val(),
            controlSub: $("#dic_controlSub option:selected").val(),
            email: $("#dic_email").val(),
            canal:$("#dic_canal option:selected").attr("codigo"),
            idEjecutivo: $("#dic_ejecutivo option:selected").val(),
            ejecutivo:$("#dic_ejecutivo option:selected").text(),
            codigoEjecutivo:$("#dic_ejecutivo option:selected").attr("codigo"),
            gpFlag:$("#dic_gpFlag").is(":checked")? 1:0,
            idSemaforo: $("#dic_semaforo option:selected").val(),
            participantes: JSON.stringify(aseguradores),
            validacion:validacion
        }
        $.post(guardarCotizacionURL,objetoSend).done(function (data){
            let response = JSON.parse(data);
            if (response.code === 0) {
                $('#fin').submit();
            } else {
                hideLoader();
                showMessageError('.navbar', response.msg, 0);
            }
        });
    }
}

function validaRequeridos(){
    var campos = $(".requerido:visible");
    var completos = true;
    $.each(campos, function(key, campo) {
        if($(campo).hasClass("select-wrapper")){
            var select = $(campo).children('select');
            completos = noSelect($(select)) ? false : completos;
        }else{
            completos = vaciosInpText($(campo)) ? false : completos;
        }
    });
    if($("dic_negocio option:selected").val() > 0 && $("dic_negocio option:selected").val() !== '1' && aseguradores.length < 1){
        showMessageError('.modal-header', "Los participantes deben ser por lo menos 2", 0 );
        hideLoader();
        return false;
    }
    if(!completos){
        showMessageError( '.modal-header', "Falta información requerida", 0 );
        hideLoader();
        return false;
    }
    return completos;
}

function noSelect(campo) {
    var errores = false;
    if ($(campo).val() == "-1") {
        errores = true;
        $(campo).siblings("input").addClass('invalid');
        $(campo).parent().append(
            "<div class=\"alert alert-danger\"> <span class=\"glyphicon glyphicon-ban-circle\"></span> " + " Campo Requerido" +
            "</div>");
    }

    return errores;
}

function vaciosInpText(value) {
    var errores = false;
    if($(value).is(":visible")){
        if (valIsNullOrEmpty($(value).val())) {
            errores = true;
            $(value).addClass('invalid');
            $(value).parent().append(
                "<div class=\"alert alert-danger\" role=\"alert\"> <span class=\"glyphicon glyphicon-ban-circle\"></span>" + " Campo Requerido"
                + "</div>");
        }
    }
    return errores;
}

function autorizaCotizacion(id){
    gurardaCotizacionManager(id,false);
}

function rechazaCotizacion(id){
    gurardaCotizacionManager(id,true);
}

function gurardaCotizacionManager(id,rechazo){
    if(manager == 1){
        showLoader();
        $.post(guardarCotizacionManagerURL,{id:id,rechazo:rechazo}).done(function (data){
            console.log(data);
            let response = JSON.parse(data);
            if (response.code == 0) {
                $('#fin').submit();
            } else {
                hideLoader();
                showMessageError('.navbar', response.msg, 0);
            }
        });
    }
}

function restartDatatable(dataTable){
    $('#' + dataTable).DataTable({
        responsive: true,
        destroy: true,
        dom: 'fBrltip',
        ordering:false,
        ordered:false,
        buttons: [],
        columnDefs: [
            {targets: '_all', className: "py-2" }
        ],
        lengthChange: true,
        language: {
            "url": spanishJson,

        },
        lengthMenu: [[5,10,15], [5,10,15]],
        pageLength: 10
    });
}