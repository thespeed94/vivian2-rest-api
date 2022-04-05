$(document).ready(function() {
	
    $('#cerrarsesion').click(function(){
        Swal.fire({
            title: '¿Cerrar Sesión?',
            showCancelButton: true,
            cancelButtonText: 'Cancelar',
            confirmButtonText: 'Sí'
        }).then((result) => {
            if (result.isConfirmed) {
                parent.location.href="/logout";
            }
        })
    });

    $('#toggleMenu').click(function (){
      if ($('#fake-aside').hasClass('active')){
          $('#fake-aside').removeClass('active');
      }else{
          $('#fake-aside').addClass('active');
      }
    });

    $('#mantenimientos').click(function (){
        if ($('.fake-combo.reportes').hasClass('active-combo')){
            $('.fake-combo.reportes').removeClass('active-combo');
        }
        if ($('.fake-combo.mantenimientos').hasClass('active-combo')){
            $('.fake-combo.mantenimientos').removeClass('active-combo');
        }else{
            $('.fake-combo.mantenimientos').addClass('active-combo');
        }

        document.getElementById(this.id).classList.toggle('link-active');
    });

    $('#reportes-detalles').click(function (){
        if ($('.fake-combo.mantenimientos').hasClass('active-combo')){
            $('.fake-combo.mantenimientos').removeClass('active-combo');
        }
        if ($('.fake-combo.reportes').hasClass('active-combo')){
            $('.fake-combo.reportes').removeClass('active-combo');
        }else{
            $('.fake-combo.reportes').addClass('active-combo');
        }

        document.getElementById(this.id).classList.toggle('link-active');
    });
});