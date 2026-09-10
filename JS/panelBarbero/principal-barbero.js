const btnConfirmar = document.querySelectorAll(".btn-confirm");

btnConfirmar.forEach(i => {

    i.addEventListener("click", (e) =>{
        const fila = i.closest(".appointment-row");
        const badge = fila.querySelector(".badge");

    
            badge.classList.remove("programada")
            badge.textContent = "Completada"
            badge.classList.add("completada");
            
            i.style.display = "none";

    })

})




