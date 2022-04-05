$(document).ready(function (){

    // ----------------------- VALIDACION --------------------------------
    // METODO PARA LAS VALIDACIONES Y SUS RESPECTIVOS MENSAJES
    $("#formResetPassword").validate({
        rules:{
            resetpassword: { required: true, minlength:4, maxlength:18},
            repeatresetpassword: { required: true, minlength:4, maxlength:18, equalTo:"#resetpassword"}
        },
        messages:{
            resetpassword: { required:"El campo es requerido", minlength:'Mínimo 4 caracteres', maxlength:'Máximo 18 caracteres'},
            repeatresetpassword: { required:"El campo es requerido", minlength:'Mínimo 4 caracteres', maxlength:'Máximo 18 caracteres', equalTo: 'Las contraseñas no coinciden'}
        }
    });
    //---------------------------------------------------------------------


    $("#cambiarPassword").click(function (){
        if($('#formResetPassword').valid() == false){
            return false;
        }else {
            $.ajax({
                type: 'PUT',
                url: '/resetpassword',
                data: {
                    "password": $("#resetpassword").val(),
                }, success: function (data) {
                    if (data.estado === 1){
                        Swal.fire({
                            icon: 'success',
                            title: data.mensaje,
                        }).then((result) => {
                            parent.location.href = "/principal";
                        })
                    }else{
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: data.mensaje,
                        })
                    }
                }
            })
        }
    });

});