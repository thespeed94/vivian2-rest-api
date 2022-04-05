$(document).ready(function (){

    $("#volverInicio").click(function (e) {
        parent.location.href="/principal";
    });

    // ----------------------- VALIDACION --------------------------------
    // METODO PARA LAS VALIDACIONES Y SUS RESPECTIVOS MENSAJES
    $("#formForgotPassword").validate({
        rules:{
            username: { required: true, email: true},
        },
        messages:{
            username: { required:"El campo es requerido", email: 'Formato de email incorrecto'},
        }
    });
    //---------------------------------------------------------------------

    $("#enviarUsuario").click(function (){
        if($('#formForgotPassword').valid() == false){
            return false;
        }else{
            $("#divSpinner").removeClass('spinhidden');
            $("#divSpinner").addClass('spinappear');
            $.ajax({
                type: 'POST',
                url: '/recuperation',
                data: {
                    "username": $("#username").val(),
                },success: function (data){
                    $("#divSpinner").removeClass('spinappear');
                    $("#divSpinner").addClass('spinhidden');
                    if (data.estado === 1){
                        Swal.fire(
                            'Revisa tu correo y sigue las instrucciones',
                            'Te hemos enviado un correo a '+$("#username").val()+' con las instrucciones para cambiar tu contraseña. \n*Si no logras encontrarlo, revisa tu bandeja de spam.',
                            'success'
                        ).then((result) => {
                            parent.location.href = "/principal";
                        })
                    }else{
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'No hemos podido enviar el correo. Intentalo nuevamente más tarde!',
                        })
                    }
                }
            });
        }

    });

});
