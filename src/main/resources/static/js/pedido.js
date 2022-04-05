$(document).ready(function(){

    // METODO PARA MOSTRAR EL MENSAJE GUARDADO EN EL LOCALSTORAGE AL RECARGAR LA PAGINA LUEGO DEL AJAX
    if (localStorage.getItem("Success")) {
        toastr.success(localStorage.getItem("Success"));
        localStorage.clear();
    } else if (localStorage.getItem("Error")){
        toastr.success(localStorage.getItem("Error"));
        localStorage.clear();
    }


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
    let tabla = $('#tPedido').DataTable({
        columnDefs: [
            {  targets: 6,
                render: function (data, type, row, meta) {
                    return '<button style="margin-right:5px;" type="button" class="editar" id=n-"' + meta.row + '">Ver Detalle</button><button type="button" class="eliminar" id=s-"' + meta.row + '">Ver PDF</button>';
                }
            }
        ],
        language: idioma_espanol
    });


    // METODO PARA LLENAR EL FORMULARIO CON LOS DATOS DEL REGISTRO A EDITAR... PARA ABRIR EL MODAL Y PARA
    // SETEAR EL VALUE "editar" AL BOTON GUARDAR PARA POSTERIORMENTE USARLO EN AJAX
    $('#tPedido tbody').on('click', '.editar', function () {
        var id = $(this).attr("id").match(/\d+/)[0];
        var data = $('#tPedido').DataTable().row( id ).data();

        parent.location.href = '/pedido/'+data[0];

    });

    // METODO PARA MOSTRAR UNA ALERTA PARA POSTERIORMENTE ELIMINAR UN REGISTRO
    $('#tPedido tbody').on('click', '.eliminar', function () {
        var id = $(this).attr("id").match(/\d+/)[0];
        var data = $('#tPedido').DataTable().row( id ).data();
        if(!confirm('Desea Generar el PDF?'))return false;

        window.open("/pedido/pdf/"+data[0],"_blank")
    });

});