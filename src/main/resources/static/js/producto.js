$(document).ready(function(){

    // VARIABLES DE LOS TODOS LOS INPUTS Y COMBOBOXES DEL FORMULARIO. ADEMAS DE LA "X" PARA CERRAR EL MODAL
    const XModal = $(".btn-close-modal");
    const inombreproducto = $("#txtNombreProducto");
    const iprecio = $("#txtPrecio");
    const istock = $("#txtStock");
    const ireparto = $("#txtReparto");
    const iestado = $("#txtEstado");
    const cboEstado = $("#txtEstado");
    const cboCategoriaProducto = $("#cboCategoria");
    const iid = $("#txtId");

    // METODO PARA MOSTRAR EL MENSAJE GUARDADO EN EL LOCALSTORAGE AL RECARGAR LA PAGINA LUEGO DEL AJAX
    if (localStorage.getItem("Success")) {
        toastr.success(localStorage.getItem("Success"));
        localStorage.clear();
    } else if (localStorage.getItem("Error")){
        toastr.success(localStorage.getItem("Error"));
        localStorage.clear();
    }

    // AL INCIAR LA APLICACION EL INPUT Y LABEL DEL ID DE USUARIO NO SE MOSTRARAN EN EL MODAL/FORMULARIO
    iid.hide();
    $("#labelId").hide();

    // OBJETO PARA PONER EL DATA TABLE EN ESPANIOL
    let idioma_espanol={
        "sProcessing":     "Procesando...",
        "sLengthMenu":     "Mostrar _MENU_ registros",
        "sZeroRecords":    "No se encontraron resultados",
        "sEmptyTable":     "Ningún dato disponible en esta tabla",
        "sInfo":           "Mostrando registros del _START_ al _END_ de _TOTAL_ registros",
        "sInfoEmpty":      "Mostrando registros del 0 al 0 de un total de 0 registros",
        "sInfoFiltered":   "(filtrado de un total de MAX registros)",
        "sSearch":         "Buscar:",
        "sInfoThousands":  ",",
        "sLoadingRecords": "Cargando...",
        "oPaginate": {
            "sFirst":    "Primero",
            "sLast":     "Último",
            "sNext":     "Siguiente",
            "sPrevious": "Anterior"
        },
        "oAria": {
            "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
            "sSortDescending": ": Activar para ordenar la columna de manera descendente"
        },
        "buttons": {
            "copy": "Copiar",
            "colvis": "Visibilidad"
        }
    }

    // CREA EL DATA TABLE CON BOTONES EDITAR Y ELIMINAR A TRAVES DEL ID DE LA TABLA EN EL HTML
    let tabla = $('#tProducto').DataTable({
        columnDefs: [
            {  targets: 7,
                render: function (data, type, row, meta) {
                    return '<button style="margin-right:5px;" type="button" class="editar" id=n-"' + meta.row + '">Editar</button><button type="button" class="eliminar" id=s-"' + meta.row + '">Eliminar</button>';
                }
            }
        ],
        language: idioma_espanol
    });

    // METODO PARA SETEAR EL VALUE "AGREGAR" AL BOTON GUARDAR AL DARLE CLICK EN "Agregar Nuevo" PARA LUEGO UTILIZAR ESTE VALUE EN EL AJAX
    $("#agregarProducto").click(function (){
        $('#btnGuardarProducto').val("agregar");
        $("#tituloForm").html("Nuevo Producto");
    });

    // METODO PARA LIMPIAR EL FORMULARIO(inputs y mensajes de errores) AL DARLE CLICK A LA "X" DEL MODAL (cerrar modal)
    XModal.click(function (){
        inombreproducto.val("");
        iprecio.val("");
        istock.val("");
        ireparto.val("");
        iestado.val("");
        cboEstado.val(1);
        cboCategoriaProducto.val("");
        $("label.error").remove();
    });

    // METODO PARA LLENAR EL FORMULARIO CON LOS DATOS DEL REGISTRO A EDITAR... PARA ABRIR EL MODAL Y PARA
    // SETEAR EL VALUE "editar" AL BOTON GUARDAR PARA POSTERIORMENTE USARLO EN AJAX
    $('#tProducto tbody').on('click', '.editar', function () {
        $("#tituloForm").html("Editar Producto");
        $("label.error").remove();
        var id = $(this).attr("id").match(/\d+/)[0];
        var data = $('#tProducto').DataTable().row( id ).data();
        var idCat =$(this).parents('tr').find('.idCat').val();

        iid.val(data[0]);
        inombreproducto.val(data[1]);
        iprecio.val(data[2]);
        cboCategoriaProducto.val(idCat);
        istock.val(data[4]);
        ireparto.val(data[5]);
        switch (data[6])
        {
            case "Activo":  cboEstado.val(1); break;
            default:  cboEstado.val(0);
        }
        openModal();
        $('#btnGuardarProducto').val("editar");
    });

    // METODO PARA MOSTRAR UNA ALERTA PARA POSTERIORMENTE ELIMINAR UN REGISTRO
    $('#tProducto tbody').on('click', '.eliminar', function () {
        var id = $(this).attr("id").match(/\d+/)[0];
        var data = $('#tProducto').DataTable().row( id ).data();
        if(!confirm('Desea Eliminar?'))return false;
        $.ajax({
            type: 'DELETE',
            url: '/producto',
            data: {
                "id":data[0],
            },success: function (data){
                if (data.estado === 1){
                    localStorage.setItem("Success",data.mensaje);
                    parent.location.href="/producto";
                }else{
                    toastr.error(data.mensaje);
                }
            }
        })
    });

    // ----------------------- VALIDACION --------------------------------
    // METODO PARA VALIDAR SOLO LETRAS
    $.validator.addMethod("lettersonly", function(value, element) {
        return this.optional(element) || /^[a-z\s]+$/i.test(value);
    }, "El campo solo permite el ingreso de letras");
    // METODO PARA VALIDAR VALORES IGUALES
    $.validator.addMethod("valueNotEquals", function(value, element, param) {
        return this.optional(element) || value != param;
    }, "Seleccione un valor diferente");
    // METODO PARA VALIDAR SOLO DOS DECIMALES
    $.validator.addMethod("onlyTwoDecimals", function(value, element) {
        return this.optional(element) || /^\d+\.?\d{0,2}/i.test(value);
    }, "Solo dos decimales");

    // METODO PARA LAS VALIDACIONES Y SUS RESPECTIVOS MENSAJES
    $("#formProducto").validate({
        rules:{
            nombreProducto: { required: true, minlength:3, maxlength:35},
            precio: { required: true, min: 0.01, onlyTwoDecimals: true},
            idCategoria: { required: true, valueNotEquals: ""},
            stock: { required: true, min: 1, digits: true},
            reparto: { required: true, min: 1, digits: true}
        },
        messages:{
            nombreProducto: { required:"El campo es requerido", minlength:'Mínimo 3 caracteres', maxlength:'Máximo 35 caracteres'},
            precio: { required:"El campo es requerido", min:'Mínimo un valor de 0.01', onlyTwoDecimals: "Solo dos decimales"},
            idCategoria: { required:"El campo es requerido", valueNotEquals:'Seleccione Categoría'},
            stock: { required:"El campo es requerido", min: 'Mínimo un valor de 1', digits: "Solo números"},
            reparto: { required:"El campo es requerido", min: 'Mínimo un valor de 1', digits: "Solo números"}
        }
    });
    $("#btnGuardarProducto").submit();
    // //---------------------------------------------------------------------
    //
    // METODO PARA: SI EL FORMULARIO ES VALIDO, GUARDAR O EDITAR A TRAVES DE AJAX DEPENDIENDO DEL VALUE("agregar" o "editar") DEL BOTON GUARDAR
    $('#btnGuardarProducto').on('click',function(){
        if($('#formProducto').valid() == false){
            return false;
        }else{
            // SI EL VALUE ES agregar INGRESA UN NUEVO USUARIO (POST)
            if ($('#btnGuardarProducto').val() === "agregar"){
                $.ajax({
                    type: 'POST',
                    url: '/producto',
                    data: {
                        "nombreProducto": $("#txtNombreProducto").val(),
                        "precio": $("#txtPrecio").val(),
                        "idCategoria": $("#cboCategoria").val(),
                        "stock": $("#txtStock").val(),
                        "reparto": $("#txtReparto").val(),
                        "estado":$("#txtEstado").val()
                    },
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/producto";
                        } else{
                            toastr.warning(data.mensaje)
                        }
                    },
                    error: function (data){
                        toastr.error(data.responseJSON.mensaje);
                    }
                })
                // SI EL VALUE ES editar ACTUALIZA UN USUARIO YA EXISTENTE
            } else if ($('#btnGuardarProducto').val() === "editar"){
                $.ajax({
                    type: 'PUT',
                    url: '/producto',
                    data: {
                        "id":$("#txtId").val(),
                        "nombreProducto": $("#txtNombreProducto").val(),
                        "precio": $("#txtPrecio").val(),
                        "idCategoria": $("#cboCategoria").val(),
                        "stock": $("#txtStock").val(),
                        "reparto": $("#txtReparto").val(),
                        "estado":$("#txtEstado").val()
                    },
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/producto";
                        } else {
                            toastr.warning(data.mensaje);
                        }
                    },
                    error: function (data){
                        toastr.error(data.responseJSON.mensaje);
                    }
                })
            }
        }
    });

    $('#btnLimpiarProductoForm').click(function (){
        inombreproducto.val("");
        iprecio.val("");
        istock.val("");
        ireparto.val("");
        cboEstado.val(1);
        cboCategoriaProducto.val(0);
        $("label.error").remove();
    })

});