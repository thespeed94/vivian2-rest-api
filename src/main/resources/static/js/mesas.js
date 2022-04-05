$(document).ready(function(){

    // VARIABLES DE LOS TODOS LOS INPUTS Y COMBOBOXES DEL FORMULARIO. ADEMAS DE LA "X" PARA CERRAR EL MODAL
    const XModal = $(".btn-close-modal");
    const iId = $("#txtId");
    const iPiso = $("#txtpiso");
    const iCapacidadPersonas = $("#txtcapacidadpersonas");
    const iTituloForm=$("#tituloForm");

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
    let tabla = $('#tMesas').DataTable({
        columnDefs: [
            {  targets: 3,
                render: function (data, type, row, meta) {
                    return '<button style="margin-right:5px;" type="button" class="editar" id=n-"' + meta.row + '">Editar</button><button type="button" class="eliminar" id=s-"' + meta.row + '">Eliminar</button>';
                }
            }
        ],
        language: idioma_espanol
    });

    // METODO PARA SETEAR EL VALUE "AGREGAR" AL BOTON GUARDAR AL DARLE CLICK EN "Agregar Nuevo" PARA LUEGO UTILIZAR ESTE VALUE EN EL AJAX
    $("#agregarMesa").click(function (){
    iTituloForm.html("Agregar Mesa");
        $('#btnGuardarMesa').val("agregar");
    });

    // METODO PARA LIMPIAR EL FORMULARIO(inputs y mensajes de errores) AL DARLE CLICK A LA "X" DEL MODAL (cerrar modal)
    XModal.click(function (){
        iPiso.val("");
        iCapacidadPersonas.val(0);
        
        $("label.error").remove();
    });

    // METODO PARA LLENAR EL FORMULARIO CON LOS DATOS DEL REGISTRO A EDITAR... PARA ABRIR EL MODAL Y PARA
    // SETEAR EL VALUE "editar" AL BOTON GUARDAR PARA POSTERIORMENTE USARLO EN AJAX
    $('#tMesas tbody').on('click', '.editar', function () {
		var fila=$($($(this)[0]).parent()).parent();
		
		let eId=($($(fila).children()[0]).html());
		let ePiso=($($(fila).children()[1]).html());
		let eCapacidadPersonas=($($(fila).children()[2]).html());

		iTituloForm.html("Editar Mesa Nº " + eId);

        $("label.error").remove();
		iId.val(eId);
        iPiso.val(ePiso);
        iCapacidadPersonas.val(eCapacidadPersonas);
        
        openModal();
        $('#btnGuardarMesa').val("editar");
    });

    // METODO PARA MOSTRAR UNA ALERTA PARA POSTERIORMENTE ELIMINAR UN REGISTRO
    $('#tMesas tbody').on('click', '.eliminar', function () {       
        var fila=$($($(this)[0]).parent()).parent();	
		let delId=($($(fila).children()[0]).html());
		
        if(!confirm('Desea Eliminar?'))return false;
        $.ajax({
            type: 'DELETE',
            url: '/mesas',
            data: {
                "id":delId,
            },success: function (data){
                if (data.estado === 1){
                    localStorage.setItem("Success",data.mensaje);
                    parent.location.href="/mesas";
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
    $("#formMesas").validate({
        rules:{
            piso: { required: true, maxlength:2, digits: true},
            capacidadPersonas: { required: true,maxlength:2, digits: true}
        },
        messages:{
            piso: { required:"El campo es requerido", maxlength:'Máximo 2 caracteres', digits: "Solo números"},
            capacidadPersonas: { required:"El campo es requerido", maxlength:'Máximo 2 caracteres', digits: "Solo números"}
        }
    });
    //---------------------------------------------------------------------

    // METODO PARA: SI EL FORMULARIO ES VALIDO, GUARDAR O EDITAR A TRAVES DE AJAX DEPENDIENDO DEL VALUE("agregar" o "editar") DEL BOTON GUARDAR
    $('#btnGuardarMesa').on('click',function(){
        if($('#formMesas').valid() == false){
            return false;
        }else{
            // SI EL VALUE ES agregar INGRESA UN NUEVO USUARIO (POST)
            if ($('#btnGuardarMesa').val() === "agregar"){

            var obj={
            	id:$("#txtId").val(),
            	piso:$("#txtpiso").val(),
            	capacidadPersonas:$("#txtcapacidadpersonas").val()            	       		
            };
            
            var json = JSON.stringify(obj);
            
                $.ajax({
                    type: 'POST',
                    url: '/mesas/',
                    data:{"json":json},
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/mesas";
                        } else{
                            toastr.warning(data.mensaje)
                        }
                    },
                    error: function (data){
                        toastr.error(data.responseJSON.mensaje);
                    }
                })
                // SI EL VALUE ES editar ACTUALIZA UN USUARIO YA EXISTENTE
            } else if ($('#btnGuardarMesa').val() === "editar"){
            var obj={
            	id:$("#txtId").val(),
            	piso:$("#txtpiso").val(),
            	capacidadPersonas:$("#txtcapacidadpersonas").val()            	       		
            };
           		var json = JSON.stringify(obj);
            
                $.ajax({
                    type: 'PUT',
                    url: '/mesas/',
                    data: {"json":json},
                    success: function (data){
                        if (data.estado === 1){
                            localStorage.setItem("Success",data.mensaje);
                            parent.location.href = "/mesas";
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