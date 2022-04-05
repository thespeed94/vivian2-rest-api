$(document).ready(function(){

    // VARIABLES DE LOS TODOS LOS INPUTS Y COMBOBOXES DEL FORMULARIO. ADEMAS DE LA "X" PARA CERRAR EL MODAL
    const XModal = $(".btn-close-modal");
    
    const iid = $("#txtId");
    const idescripcion = $("#txtDescripcion");
    const ihorario = $("#txtHorario");
   
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

    // OBJETO PARA PONER EL DATA TABLE EN ESPANIOL		NO TOCAR
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

    // CREA EL DATA TABLE CON BOTONES EDITAR Y ELIMINAR A TRAVES DEL ID DE LA TABLA EN EL HTML   NO TOCAR
    let tabla = $('#tTurno').DataTable({
        columnDefs: [
            {  targets: 3,
                render: function (data, type, row, meta) {
                    return '<button style="margin-right:5px;" type="button" class="editar" id=n-"' + meta.row + '">Editar</button><button type="button" class="eliminar" id=s-"' + meta.row + '">Eliminar</button>';
                }
            }
        ],
        language: idioma_espanol
    });

    // METODO PARA SETEAR EL VALUE "AGREGAR" AL BOTON GUARDAR AL DARLE CLICK EN "Agregar Nuevo" PARA LUEGO UTILIZAR ESTE VALUE EN EL AJAX    NO TOCAR
    $("#agregarTurno").click(function (){
        $('#btnGuardarTurno').val("agregar");
    });

    // METODO PARA LIMPIAR EL FORMULARIO(inputs y mensajes de errores) AL DARLE CLICK A LA "X" DEL MODAL (cerrar modal)
    XModal.click(function (){
        iid.val("");
        idescripcion.val("");

        $("label.error").remove();
    });

    // METODO PARA LLENAR EL FORMULARIO CON LOS DATOS DEL REGISTRO A EDITAR... PARA ABRIR EL MODAL Y PARA
    // SETEAR EL VALUE "editar" AL BOTON GUARDAR PARA POSTERIORMENTE USARLO EN AJAX
    $('#tTurno tbody').on('click', '.editar', function () {
        $("label.error").remove();
        
        var id = $(this).attr("id").match(/\d+/)[0];
        var data = $('#tTurno').DataTable().row( id ).data();

        iid.val([data[0]]);
        idescripcion.val(data[1]);
        ihorario.val(data[2]);

        openModal();
        $('#btnGuardarTurno').val("editar");
    });

    // METODO PARA MOSTRAR UNA ALERTA PARA POSTERIORMENTE ELIMINAR UN REGISTRO
    $('#tTurno tbody').on('click', '.eliminar', function () {
        var id = $(this).attr("id").match(/\d+/)[0];
        var data = $('#tTurno').DataTable().row( id ).data();
        
        if(!confirm('Desea Eliminar?'))return false;
        $.ajax({
            type: 'DELETE',
            url: '/turno',
            data: {
                "id":data[0],
            },success: function (data){
                if (data.estado === 1){
                    localStorage.setItem("Success",data.mensaje);
                    parent.location.href="/turno";
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
    $("#formTurno").validate({
        rules:{
            descripcion: { required: true, maxlength:35},
            horario: { required: true, maxlength:35},
            
            },
        messages:{
            descripcion: { required:"El campo es requerido", maxlength:'Máximo 45 caracteres'},
            horario: { required: "El campo es requerido", maxlength:'Máximo 35 caracteres'},
                   
        }
    });
    //---------------------------------------------------------------------

    // METODO PARA: SI EL FORMULARIO ES VALIDO, GUARDAR O EDITAR A TRAVES DE AJAX DEPENDIENDO DEL VALUE("agregar" o "editar") DEL BOTON GUARDAR
    $('#btnGuardarTurno').on('click',function(){
        if($('#formTurno').valid() == false){
            return false;
        }else{
            // SI EL VALUE ES agregar INGRESA UN NUEVO USUARIO (POST)
            if ($('#btnGuardarTurno').val() === "agregar"){
                $.ajax({
                    type: 'POST',
                    url: '/turno',
                    data: {
                        "descripcion": $("#txtDescripcion").val(),
                        "horario":$("#txtHorario").val()
                    },
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/turno";
                        } else{
                            toastr.warning(data.mensaje)
                        }
                    },
                    error: function (data){
                        toastr.error(data.responseJSON.mensaje);
                    }
                })
                // SI EL VALUE ES editar ACTUALIZA UN USUARIO YA EXISTENTE
            } else if ($('#btnGuardarTurno').val() === "editar"){
                $.ajax({
                    type: 'PUT',
                    url: '/turno',
                    data: {
                        "id":$("#txtId").val(),
                        "descripcion": $("#txtDescripcion").val(),
                        "horario":$("#txtHorario").val()
                    },
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/turno";
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