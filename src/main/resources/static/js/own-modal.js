let myModal = document.querySelector('.modal-container');
let modalWindow;
let modalWindow2 = document.querySelector('.modal-window-w2');
let modalWindow3 = document.querySelector('.modal-window-w3');
let modalWindow5 = document.querySelector('.modal-window-w5');
let modalWindow7 = document.querySelector('.modal-window-w7');
let btnOpenModal = document.querySelector('.btn-open-modal');
let btnCloseModal = document.querySelector(".btn-close-modal");
if (modalWindow2){
  modalWindow = modalWindow2;
} else if (modalWindow3){
  modalWindow = modalWindow3;
} else if (modalWindow5){
  modalWindow = modalWindow5;
} else {
  modalWindow = modalWindow7;
}
if (!myModal.classList.contains('modal-disable-close')){
  document.addEventListener('click', function( event ) {
    if (btnOpenModal !== event.target && myModal.style.display === 'block' && modalWindow !== event.target && !modalWindow.contains(event.target)) {
      if (modalWindow.classList.contains("bounce-in-modal")){
        modalWindow.classList.remove("bounce-in-modal")
      }
      myModal.style.display = "none";
    }
  });
}
btnCloseModal.addEventListener('click', function( event ) {
  if (modalWindow.classList.contains("bounce-in-modal")){
    modalWindow.classList.remove("bounce-in-modal")
  }
  myModal.style.display = "none";

});

btnOpenModal.addEventListener('click', function( event ) {
  openModal();
});

function openModal(){
  modalWindow.classList.add("bounce-in-modal");
  myModal.style.display = "block";
}