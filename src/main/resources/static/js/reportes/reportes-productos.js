$(document).ready(function (){
    primerCanvas();
    segundoCanvas();
    tercerCanvas(1);
    $("#cboMes").change(function (){
        tercerCanvas($("#cboMes").val());
    });
    cuartoCanvas(1);
    $("#cboMes2").change(function (){
        cuartoCanvas($("#cboMes2").val());
    });
});
function primerCanvas(){
    $.ajax({
        type: 'GET',
        url: '/producto/reportes/cantidad',
        data: {

        },success: function (data){
            const datos = [];
            for (item of data){
                datos.push(item);
            }
            const ctx = document.getElementById('myChart').getContext('2d');
            const myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Setiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                    datasets: [{
                        label: 'Meses',
                        data: datos,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                            display: false
                        },
                        title: {
                            display: true,
                            text: 'Cantidad total de productos vendidos por mes',
                            font: {
                                size: 15
                            }
                        }
                    }
                }
            });
        }
    });
}

function segundoCanvas(){
    $.ajax({
        type: 'GET',
        url: '/producto/reportes/ingresos',
        data: {

        },success: function (data){
            let datosProductos = [];
            let datosMontos = [];
            for (array of data){
                datosProductos.push(array[0]);
                datosMontos.push(array[1]);
            }
            const ctx2 = document.getElementById('myChart2').getContext('2d');
            const myChart2 = new Chart(ctx2, {
                type: 'doughnut',
                data: {
                    labels: datosProductos,
                    datasets: [{
                        label: 'Top 5 Productos con más ingresos en $',
                        data: datosMontos,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)'
                        ],
                        hoverOffset: 4
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'right',
                        },
                        title: {
                            display: true,
                            text: 'Top 5 Productos con más ingresos',
                            font: {
                                size: 15
                            }
                        }
                    }
                }
            });
        }
    })
}

function tercerCanvas(mes){
    $.ajax({
        type: 'GET',
        url: '/producto/reportes/ingresosxmes',
        data: {
            "mes":mes
        },success: function (data){
            let datosProductos2 = [];
            let datosMontos2 = [];
            for (array of data){
                datosProductos2.push(array[0]);
                datosMontos2.push(array[1]);
            }
            if(window.myChart3 instanceof Chart)
            {
                window.myChart3.destroy();
            }
            const ctx3 = document.getElementById('myChart3').getContext('2d');
            myChart3 = new Chart(ctx3, {
                type: 'bar',
                data: {
                    labels: datosProductos2,
                    datasets: [{
                        label: 'Productos',
                        data: datosMontos2,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                            display: false
                        },
                        title: {
                            display: true,
                            text: 'Cantidades de Productos vendidos por mes',
                            font: {
                                size: 15
                            }
                        }
                    }
                }
            });
        }
    })
}


function cuartoCanvas(mes){
    $.ajax({
        type: 'GET',
        url: '/producto/reportes/categoriasxmes',
        data: {
            "mes":mes
        },success: function (data){
            let datosCategorias = [];
            let datosCantidad = [];
            for (array of data){
                datosCategorias.push(array[0]);
                datosCantidad.push(array[1]);
            }
            if(window.myChart4 instanceof Chart)
            {
                window.myChart4.destroy();
            }
            const ctx4 = document.getElementById('myChart4').getContext('2d');
            myChart4 = new Chart(ctx4, {
                type: 'pie',
                data: {
                    labels: datosCategorias,
                    datasets: [{
                        label: 'Cantidad total de productos vendidos por mes',
                        data: datosCantidad,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)',
                            'rgba(167,253,120,0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)',
                            'rgba(120,255,56,0.76)'
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'right',
                        },
                        title: {
                            display: true,
                            text: 'Cantidades de Categorias vendidas por mes',
                            font: {
                                size: 15
                            }
                        }
                    }
                }
            });
        }
    })

}




