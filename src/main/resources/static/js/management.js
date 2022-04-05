//prevent the user see the code
/*onkeydown = e => {
	let tecla = e.which || e.keyCode;
	
	if ( e.ctrlKey ) {
		if ( tecla === 85 || tecla === 83 || tecla === 16 || tecla === 73 || tecla === 63 || tecla === 67 || tecla === 123) {
			e.preventDefault();
			e.stopPropagation();	
		}
	}
} */

// NO CAMBIAR NUNCA
function mostrar(id, archivo) {
	$(id).on('click', function() {
		$('body, html').animate({
			scrollTop : '0px'
		}, 50);

		$("#fake-container-body").load(archivo).hide().fadeIn();
		return false;
	});
}

/* $(document).ready(function() {

	mostrar('#productos', 'products.html');
	mostrar('#productos2', 'products.html');

	mostrar('#delivery', '../delivery.html');

	mostrar('#datos-vivian', 'datos-vivian.html');
}); */
