document.addEventListener("DOMContentLoaded", () => {
        M.AutoInit();
    });

    const botones = [
    { selector: ".agendar-cita", destino: "agendarcita.html" },
    { selector: ".mis-citas",    destino: "miscitas.html" },
    { selector: ".productos",    destino: "productos.html" },
    { selector: ".mi-perfil",    destino: "miperfil.html" }
];

    botones.forEach(({selector, destino}) =>{
        const boton = document.querySelector(selector);

        if(boton){
            boton.addEventListener("click", (e) =>{
                window.location.href = destino;
            })
        }
    })