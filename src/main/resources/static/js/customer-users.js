$(document).ready(function(){

    // VARIABLES DE LOS TODOS LOS INPUTS Y COMBOBOXES DEL FORMULARIO. ADEMAS DE LA "X" PARA CERRAR EL MODAL
    const XModal = $(".btn-close-modal");
    const idni = $("#txtDni");
    const inombres = $("#txtNombres");
    const iapellidos = $("#txtApellidos");
    const iusuario = $("#txtEmail");
    const ipassword = $("#txtClave");
    const irepeatpass = $("#txtRepeatPassword");
    const itel = $("#txtTel");
    const cboEstado = $("#txtEstado");
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
    let tabla = $('#tAdminUsuarioCustomer').DataTable({
        columnDefs: [
            {  targets: 8,
                render: function (data, type, row, meta) {
                    return '<button style="margin-right:5px;" type="button" class="editar" id=n-"' + meta.row + '">Editar</button><button type="button" class="eliminar" id=s-"' + meta.row + '">Eliminar</button>';
                }
            }
        ],
        language: idioma_espanol
    });

    // METODO PARA SETEAR EL VALUE "AGREGAR" AL BOTON GUARDAR AL DARLE CLICK EN "Agregar Nuevo" PARA LUEGO UTILIZAR ESTE VALUE EN EL AJAX
    $("#agregarCustomerUser").click(function (){
        $('#btnGuardarCustomerUser').val("agregar");
        $("#lclave").show();
        $("#txtClave").show();
        $("#lrepeatpassword").show();
        $("#txtRepeatPassword").show();
    });

    // METODO PARA LIMPIAR EL FORMULARIO(inputs y mensajes de errores) AL DARLE CLICK A LA "X" DEL MODAL (cerrar modal)
    XModal.click(function (){
        idni.val("");
        inombres.val("");
        iapellidos.val("");
        iusuario.val("");
        ipassword.val("");
        irepeatpass.val("");
        itel.val("");
        cboEstado.val(1);
        $("label.error").remove();
    });

    // METODO PARA LLENAR EL FORMULARIO CON LOS DATOS DEL REGISTRO A EDITAR... PARA ABRIR EL MODAL Y PARA
    // SETEAR EL VALUE "editar" AL BOTON GUARDAR PARA POSTERIORMENTE USARLO EN AJAX
    $('#tAdminUsuarioCustomer tbody').on('click', '.editar', function () {
        $("label.error").remove();
        var id = $(this).attr("id").match(/\d+/)[0];
        var data = $('#tAdminUsuarioCustomer').DataTable().row( id ).data();
        $("#lclave").hide();
        $("#txtClave").hide();
        $("#lrepeatpassword").hide();
        $("#txtRepeatPassword").hide();
        iid.val([data[0]]);
        idni.val(data[1]);
        inombres.val(data[2]);
        iapellidos.val(data[3]);
        iusuario.val(data[4]);
        itel.val(data[5]);
        switch (data[7])
        {
            case "Activo":  cboEstado.val(1); break;
            default:  cboEstado.val(0);
        }
        openModal();
        $('#btnGuardarCustomerUser').val("editar");
    });

    // METODO PARA MOSTRAR UNA ALERTA PARA POSTERIORMENTE ELIMINAR UN REGISTRO
    $('#tAdminUsuarioCustomer tbody').on('click', '.eliminar', function () {
        var id = $(this).attr("id").match(/\d+/)[0];
        var data = $('#tAdminUsuarioCustomer').DataTable().row( id ).data();
        if(!confirm('Desea Eliminar?'))return false;
        $.ajax({
            type: 'DELETE',
            url: '/customerusers',
            data: {
                "id":data[0],
            },success: function (data){
                if (data.estado === 1){
                    localStorage.setItem("Success",data.mensaje);
                    parent.location.href="/customerusers";
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

    // METODO PARA LAS VALIDACIONES Y SUS RESPECTIVOS MENSAJES
    $("#formCustomerUser").validate({
        rules:{
            dni: { required: true, minlength:8, maxlength:8, digits: true},
            nombresUsuario: { required: true, minlength:2, maxlength:30, lettersonly: true},
            apellidosUsuario: { required: true, minlength:4, maxlength:30, lettersonly: true},
            telefono: { required: true, minlength:7, maxlength:9, digits: true},
            email: { required: true, email: true},
            clave: { required:true, minlength:4, maxlength:18},
            txtRepeatPassword: { required:true, equalTo:"#txtClave"}
        },
        messages:{
            dni: { required:"El campo es requerido", minlength:'Mínimo 8 caracteres', maxlength:'Máximo 8 caracteres', digits: "Solo números"},
            nombresUsuario: { required:"El campo es requerido", minlength:'Mínimo 2 caracteres', maxlength:'Máximo 30 caracteres', lettersonly: "Solo letras"},
            apellidosUsuario: { required:"El campo es requerido", minlength:'Mínimo 4 caracteres', maxlength:'Máximo 30 caracteres', lettersonly: "Solo letras"},
            telefono: { required:"El campo es requerido", minlength:'Mínimo 7 caracteres', maxlength:'Máximo 9 caracteres', digits: "Solo números"},
            email: { required:"El campo es requerido", email: "Formato de email incorrecto"},
            clave: { required:"El campo es requerido", minlength:'Mínimo 4 caracteres', maxlength:'Máximo 18 caracteres'},
            txtRepeatPassword: { required: "El campo es requerido", equalTo: "Las contraseñas no coinciden"}
        }
    });
    //---------------------------------------------------------------------

    // METODO PARA: SI EL FORMULARIO ES VALIDO, GUARDAR O EDITAR A TRAVES DE AJAX DEPENDIENDO DEL VALUE("agregar" o "editar") DEL BOTON GUARDAR
    $('#btnGuardarCustomerUser').on('click',function(){
        if($('#formCustomerUser').valid() == false){
            return false;
        }else{
            // SI EL VALUE ES agregar INGRESA UN NUEVO USUARIO (POST)
            if ($('#btnGuardarCustomerUser').val() === "agregar"){
                $.ajax({
                    type: 'POST',
                    url: '/customerusers/',
                    data: {
                        "dni": $("#txtDni").val(),
                        "nombresUsuario": $("#txtNombres").val(),
                        "apellidosUsuario": $("#txtApellidos").val(),
                        "telefono": $("#txtTel").val(),
                        "email": $("#txtEmail").val(),
                        "clave": $("#txtClave").val(),
                        "estado":$("#txtEstado").val()
                    },
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/customerusers";
                        } else{
                            toastr.warning(data.mensaje)
                        }
                    },
                    error: function (data){
                        toastr.error(data.responseJSON.mensaje);
                    }
                })
                // SI EL VALUE ES editar ACTUALIZA UN USUARIO YA EXISTENTE
            } else if ($('#btnGuardarCustomerUser').val() === "editar"){
                $.ajax({
                    type: 'PUT',
                    url: '/customerusers/',
                    data: {
                        "id":$("#txtId").val(),
                        "dni": $("#txtDni").val(),
                        "nombresUsuario": $("#txtNombres").val(),
                        "apellidosUsuario": $("#txtApellidos").val(),
                        "telefono": $("#txtTel").val(),
                        "email": $("#txtEmail").val(),
                        "estado":$("#txtEstado").val()
                    },
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/customerusers";
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

});
