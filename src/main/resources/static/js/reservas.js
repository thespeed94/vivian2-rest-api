$(document).ready(function(){

    // VARIABLES DE LOS TODOS LOS INPUTS Y COMBOBOXES DEL FORMULARIO. ADEMAS DE LA "X" PARA CERRAR EL MODAL
    const XModal = $(".btn-close-modal");
    const iId = $("#txtId");
    const iDniUsuario = $("#txtDniUsuario");
    const iNMesa = $("#txtnMesa");
    const iFechaReservacion = $("#txtFechaReservacion");
    const cboTurno = $("#txtTurno");
    const iTituloForm= $("#tituloForm");

    // METODO PARA MOSTRAR EL MENSAJE GUARDADO EN EL LOCALSTORAGE AL RECARGAR LA PAGINA LUEGO DEL AJAX
    if (localStorage.getItem("Success")) {
        toastr.success(localStorage.getItem("Success"));
        localStorage.clear();
    } else if (localStorage.getItem("Error")){
        toastr.success(localStorage.getItem("Error"));
        localStorage.clear();
    }


    // AL INCIAR LA APLICACION EL INPUT Y LABEL DEL ID DE USUARIO NO SE MOSTRARAN EN EL MODAL/FORMULARIO
    iId.hide();
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
    let tabla = $('#tReservas').DataTable({
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
    $("#agregarReserva").click(function (){
    iTituloForm.html("Agregar Reserva");
        $('#btnGuardarReserva').val("agregar");
    });

    // METODO PARA LIMPIAR EL FORMULARIO(inputs y mensajes de errores) AL DARLE CLICK A LA "X" DEL MODAL (cerrar modal)
    XModal.click(function (){
        iDniUsuario.val("");
        iNMesa.val(0);
        iFechaReservacion.val("");
        cboTurno.val(0);
        
        $("label.error").remove();
    });

    // METODO PARA LLENAR EL FORMULARIO CON LOS DATOS DEL REGISTRO A EDITAR... PARA ABRIR EL MODAL Y PARA
    // SETEAR EL VALUE "editar" AL BOTON GUARDAR PARA POSTERIORMENTE USARLO EN AJAX
    $('#tReservas tbody').on('click', '.editar', function () {
		var fila=$($($(this)[0]).parent()).parent();
		
		let eId=($($(fila).children()[0]).html());
		let eDni=($($(fila).children()[1]).html());
		let eMesa=($($(fila).children()[3]).html());
		let eFechaReserva=($($(fila).children()[5]).html());
		let eTurno=($($(fila).children()[6]).attr("idturno"));
		
		let eFechaReservaFormateada=eFechaReserva.substring(6,10)+'-'+eFechaReserva.substring(3,5)+'-'+eFechaReserva.substring(0,2)
		iTituloForm.html("Editar Reserva Nº " + eId);

        $("label.error").remove();
		iId.val(eId);
        iDniUsuario.val(eDni);
        iNMesa.val(eMesa);
        iFechaReservacion.val(eFechaReservaFormateada);
        cboTurno.val(eTurno);
        
        openModal();
        $('#btnGuardarReserva').val("editar");
    });

    // METODO PARA MOSTRAR UNA ALERTA PARA POSTERIORMENTE ELIMINAR UN REGISTRO
    $('#tReservas tbody').on('click', '.eliminar', function () {       
        var fila=$($($(this)[0]).parent()).parent();	
		let delId=($($(fila).children()[0]).html());
		
        if(!confirm('Desea Eliminar?'))return false;
        $.ajax({
            type: 'DELETE',
            url: '/reservaciones',
            data: {
                "id":delId,
            },success: function (data){
                if (data.estado === 1){
                    localStorage.setItem("Success",data.mensaje);
                    parent.location.href="/reservaciones";
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
    // METODO PARA VALIDAR FECHAS
    $.validator.addMethod("fechaFormato", function(value, element) {
        // put your own logic here, this is just a (crappy) example
        return value.match(/^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/);
    },
    "Ingrese la fecha en formato  yyyy-mm-dd."
);
    

    // METODO PARA LAS VALIDACIONES Y SUS RESPECTIVOS MENSAJES
    $("#formReservaciones").validate({
        rules:{
            'usuario.dni': { required: true, minlength:8, maxlength:8, digits: true},
            nMesa: { required: true, digits: true},
            fechaReservacion: { required: true,fechaFormato:true},
            txtTurno: { required: true}
        },
        messages:{
            'usuario.dni': { required:"El campo es requerido", minlength:'Mínimo 8 caracteres', maxlength:'Máximo 8 caracteres', digits: "Solo números"},
            nMesa: { required:"El campo es requerido", digits: "Solo números"},
            fechaReservacion: { required:"El campo es requerido"},
            txtTurno: { required:"El campo es requerido"}
        }
    });
    //---------------------------------------------------------------------

    // METODO PARA: SI EL FORMULARIO ES VALIDO, GUARDAR O EDITAR A TRAVES DE AJAX DEPENDIENDO DEL VALUE("agregar" o "editar") DEL BOTON GUARDAR
    $('#btnGuardarReserva').on('click',function(){
        if($('#formReservaciones').valid() == false){
            return false;
        }else{
                    let varPiso=$("#txtnMesa option:selected").attr("piso");
            // SI EL VALUE ES agregar INGRESA UN NUEVO USUARIO (POST)
            if ($('#btnGuardarReserva').val() === "agregar"){

            var obj={
            	idReserva:$("#txtIdReserva").val(),
            	usuario:{dni:$("#txtDniUsuario").val()},
            	nMesa:$("#txtnMesa").val(),
            	piso:varPiso,
            	turno:{
            		id:$("#txtTurno").val()
            		},
            	fechaReservacion:$("#txtFechaReservacion").val()           		
            };
            
            var json = JSON.stringify(obj);
            
                $.ajax({
                    type: 'POST',
                    url: '/reservaciones/',
                    data:{"json":json},
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/reservaciones";
                        } else{
                            toastr.warning(data.mensaje)
                        }
                    },
                    error: function (data){
                        toastr.error(data.responseJSON.mensaje);
                    }
                })
                // SI EL VALUE ES editar ACTUALIZA UN USUARIO YA EXISTENTE
            } else if ($('#btnGuardarReserva').val() === "editar"){
                var obj={
	            	idReserva:$("#txtId").val(),
	            	usuario:{dni:$("#txtDniUsuario").val()},
	            	nMesa:$("#txtnMesa").val(),
	            	piso:varPiso,
	            	turno:{
	            		id:$("#txtTurno").val()
	            		},
	            	fechaReservacion:$("#txtFechaReservacion").val()           		
           		};
           		var json = JSON.stringify(obj);
            
                $.ajax({
                    type: 'PUT',
                    url: '/reservaciones/',
                    data: {"json":json},
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/reservaciones";
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