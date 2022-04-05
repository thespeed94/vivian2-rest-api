$(document).ready(function (){

    $("#bug").click(function (){
        Swal.fire({
            input: 'textarea',
            inputLabel: 'Enviar fallo a desarrolladores',
            inputPlaceholder: 'Escribe los detalles del fallo...',
            inputAttributes: {
                'aria-label': 'Type your message here'
            },
            showCancelButton: true,
            cancelButtonText: 'Cancelar',
            confirmButtonText: 'Enviar'
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    icon: 'success',
                    title: 'Error enviado',
                    text: 'Acaba de ser enviado el fallo que has encontrado! Espera a que los desarrolladores atiendan la solicitud.'
                })
            }
        })
    });

});