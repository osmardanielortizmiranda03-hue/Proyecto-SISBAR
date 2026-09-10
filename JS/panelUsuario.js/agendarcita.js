document.addEventListener("DOMContentLoaded", () => {
    M.AutoInit();
    inicializarFormulario();
});

const reserva = {
    servicio: null,
    barbero: null,
    fecha: null,
    hora: null
};

document.querySelectorAll(".info-general-servicios-cortes").forEach(card =>{

    card.addEventListener("click", (e) => {

        document.querySelectorAll(".info-general-servicios-cortes").forEach(i => {
            i.classList.remove("activo");

        })

        card.classList.add("activo");
        

        const nombre = card.querySelector(".name-servicio h5").textContent;
        const precio = card.querySelectorAll(".footer-principal-cortes span")[1].textContent;
        reserva.servicio = {nombre, precio}

        actualizarPasos()
        actualizarResumen()
    })

});

document.querySelectorAll(".info-general-barbero").forEach(card =>{

    card.addEventListener("click", (e) => {

        document.querySelectorAll(".info-general-barbero").forEach(i => {
            i.classList.remove("activo");
        })

        card.classList.add("activo");

        const nombreBarbero  = card.querySelector(".nombre-barbero h5").textContent;
        reserva.barbero = nombreBarbero;

        actualizarPasos()
        actualizarResumen()
    })
})

document.querySelectorAll(".dias-mes span").forEach(dia =>{

    if(dia.classList.contains("deshabilitado")){
        return
    }

    dia.addEventListener("click", (e) =>{

        document.querySelectorAll(".dias-mes span").forEach(i =>{
            i.classList.remove("activo");
        })

        dia.classList.add("activo")

        reserva.fecha = dia.textContent;

        actualizarPasos()
        actualizarResumen()
    })
} );

document.querySelectorAll(".contenedor-horarios .hora").forEach(tiempo =>{

    if(tiempo.classList.contains("deshabilitada")){
        return
    }

    tiempo.addEventListener("click", (e) =>{

        document.querySelectorAll(".contenedor-horarios .hora").forEach(i => {
            i.classList.remove("activo");
        })

        tiempo.classList.add("activo");

        reserva.hora = tiempo.textContent

        actualizarPasos()
        actualizarResumen()
    })

})

function actualizarPasos(){
    document.querySelector(".caja-barberos").classList.toggle("paso-bloqueado", !reserva.servicio);
    document.querySelector(".caja-fecha").classList.toggle("paso-bloqueado", !reserva.barbero);
    document.querySelector(".caja-horario").classList.toggle("paso-bloqueado", !reserva.fecha);
}

const btnConfirmar = document.querySelector('.btn-confirmar');

function actualizarResumen() {
    document.querySelector(".valor-servicio").textContent = reserva.servicio ? reserva.servicio.nombre : "-";
    document.querySelector(".valor-barbero").textContent = reserva.barbero || "-";
    document.querySelector(".valor-fecha").textContent = reserva.fecha || "-";
    document.querySelector(".valor-hora").textContent = reserva.hora || "-";
    document.querySelector(".valor-precio").textContent = reserva.servicio ? reserva.servicio.precio : "-";

    const completo = reserva.servicio && reserva.barbero && reserva.fecha && reserva.hora;
    btnConfirmar.disabled = !completo;
}

btnConfirmar.addEventListener("click", (e) => {
    window.location.href = "confirmar-compra.html";
});


