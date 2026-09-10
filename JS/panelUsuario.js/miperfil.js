// ==========================================================
// INICIALIZACIÓN DE MATERIALIZE
// ==========================================================
document.addEventListener("DOMContentLoaded", () => {
    M.AutoInit();
});

// ==========================================================
// REFERENCIAS PRINCIPALES DEL DOM
// ==========================================================
const boton = document.querySelector(".edit-button");
const inputs = document.querySelectorAll(".field-input");
const divBtn = document.querySelector(".div-button");

// ==========================================================
// 1. AL CARGAR LA PÁGINA: restaurar datos guardados (si existen)
//    Busca en localStorage con la etiqueta "perfilGuardado".
//    Si no hay nada guardado (primera visita), textoGuardado es null
//    y este bloque simplemente no hace nada.
// ==========================================================
const textoGuardado = localStorage.getItem("perfilGuardado");

if (textoGuardado) {
    const datosPerfil = JSON.parse(textoGuardado); // texto → objeto

    inputs.forEach(i => {
        // Solo sobrescribe el input si existe un valor guardado para su id
        if (datosPerfil[i.id]) {
            i.value = datosPerfil[i.id];
        }
    });
}

// ==========================================================
// 2. CLIC EN "EDITAR PERFIL"
// ==========================================================
boton.addEventListener("click", () => {

    // Guarda una copia de los valores actuales de cada input,
    // por si el usuario decide cancelar más adelante
    const valoresGuardado = new Map();

    inputs.forEach(i => {
        valoresGuardado.set(i, i.value); // fotocopia: este input → este valor
        i.disabled = false;              // desbloquea el input para poder editarlo
        i.placeholder = "--";
    });

    boton.style.display = "none";

    // Crea los botones Guardar y Cancelar dinámicamente
    const btnGuardar = document.createElement("button");
    btnGuardar.textContent = "GUARDAR";
    btnGuardar.classList.add("guardar");

    const btnCancelar = document.createElement("button");
    btnCancelar.textContent = "CANCELAR";
    btnCancelar.classList.add("cancelar");

    divBtn.style.display = "flex";
    divBtn.style.gap = "8px";
    divBtn.append(btnGuardar, btnCancelar);

    // ---------- GUARDAR ----------
    btnGuardar.addEventListener("click", () => {

        // Arma un objeto plano { idDelInput: valorEditado } con los 3 campos
        const datosPerfil = {};

        inputs.forEach(i => {
            datosPerfil[i.id] = i.value;
            i.disabled = true; // vuelve a bloquear, ya con el valor nuevo puesto
        });

        // Convierte el objeto a texto y lo guarda de forma permanente
        localStorage.setItem("perfilGuardado", JSON.stringify(datosPerfil));

        btnGuardar.style.display = "none";
        btnCancelar.style.display = "none";
        boton.style.display = "inline-block";
    });

    // ---------- CANCELAR ----------
    btnCancelar.addEventListener("click", () => {

        // Restaura cada input a su valor de ANTES de entrar en modo edición
        inputs.forEach(i => {
            i.value = valoresGuardado.get(i);
            i.disabled = true;
        });

        btnGuardar.style.display = "none";
        btnCancelar.style.display = "none";
        boton.style.display = "inline-block";
    });

});