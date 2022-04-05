document.addEventListener("DOMContentLoaded", function() {
    showErrorMsg();
});

function showErrorMsg(){
    let msgError = document.querySelector("#errorMsg");
    if (msgError){
        setTimeout(function(){
            msgError.remove();
        }, 2500);
    }
}
