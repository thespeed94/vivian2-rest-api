$(document).ready(function (){

    $("#productos2").click(function (){
       parent.location.href = 'producto/reportes'
    });

    $.ajax({
        type: 'GET',
        url: '/producto/reportes/gs',
        data: {

        },success: function (data){
            $("#productoGS").html(data[0][0])
            $("#precioGS").html("S/ "+data[0][1])
            $("#gananciaGS").html(data[0][2])
            $("#uventaGS").html(data[0][3])

            $("#producto2GS").html(data[1][0])
            $("#precio2GS").html("S/ "+data[1][1])
            $("#ganancia2GS").html(data[1][2])
            $("#uventa2GS").html(data[1][3])

        }
    })

});