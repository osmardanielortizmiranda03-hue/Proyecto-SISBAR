/* =====================================================================
 *  SISBAR - Agendar Cita
 *  Basado en el script original agendarcita.js del prototipo.
 *
 *  Flujo:  1. servicio → 2. barbero → 3. fecha → 4. hora → Confirmar
 *  - Cada selección se guarda en el objeto "reserva" y en un campo oculto
 *    del formulario para que el servidor la reciba.
 *  - Los horarios libres se piden al servidor con fetch() (AJAX).
 * ===================================================================== */

document.addEventListener("DOMContentLoaded", () => {
    M.AutoInit();
    inicializarCalendario();
    restaurarSeleccion();
});

/* Estado actual de la reserva */
const reserva = {
    servicio: null,  // { id, nombre, precio, duracion }
    barbero: null,   // { id, nombre }
    fecha: null,     // "2026-09-25"
    hora: null       // "10:30"
};

const NOMBRES_MESES = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
    "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

const formatoPesos = new Intl.NumberFormat("es-CO", { maximumFractionDigits: 0 });

/* ---------------------------------------------------------------------
 *  PASO 1: selección de servicio
 * --------------------------------------------------------------------- */
document.querySelectorAll(".info-general-servicios-cortes").forEach(card => {
    card.addEventListener("click", () => seleccionarServicio(card));
});

function seleccionarServicio(card) {
    marcarActivo(".info-general-servicios-cortes", card);
    reserva.servicio = {
        id: card.dataset.id,
        nombre: card.dataset.nombre,
        precio: Number(card.dataset.precio),
        duracion: Number(card.dataset.duracion)
    };
    document.getElementById("servicioId").value = reserva.servicio.id;

    // Cambiar el servicio cambia la duración: hay que recalcular los horarios
    limpiarHora();
    cargarHorarios();
    actualizarPasos();
    actualizarResumen();
}

/* ---------------------------------------------------------------------
 *  PASO 2: selección de barbero
 * --------------------------------------------------------------------- */
document.querySelectorAll(".info-general-barbero").forEach(card => {
    card.addEventListener("click", () => seleccionarBarbero(card));
});

function seleccionarBarbero(card) {
    marcarActivo(".info-general-barbero", card);
    reserva.barbero = { id: card.dataset.id, nombre: card.dataset.nombre };
    document.getElementById("barberoId").value = reserva.barbero.id;

    limpiarHora();
    cargarHorarios();
    actualizarPasos();
    actualizarResumen();
}

/* ---------------------------------------------------------------------
 *  PASO 3: calendario (se dibuja con JavaScript)
 * --------------------------------------------------------------------- */
const hoy = new Date(CONFIG_AGENDA.hoy + "T00:00:00");
const fechaMaxima = new Date(hoy);
fechaMaxima.setDate(fechaMaxima.getDate() + CONFIG_AGENDA.diasMaximos);

let mesVisible = new Date(hoy.getFullYear(), hoy.getMonth(), 1);

function inicializarCalendario() {
    document.getElementById("mesAnterior").addEventListener("click", () => cambiarMes(-1));
    document.getElementById("mesSiguiente").addEventListener("click", () => cambiarMes(1));
    dibujarCalendario();
}

function cambiarMes(delta) {
    const nuevo = new Date(mesVisible.getFullYear(), mesVisible.getMonth() + delta, 1);
    // No se permite ir antes del mes actual ni después del mes de la fecha máxima
    const primerMes = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
    const ultimoMes = new Date(fechaMaxima.getFullYear(), fechaMaxima.getMonth(), 1);
    if (nuevo < primerMes || nuevo > ultimoMes) {
        return;
    }
    mesVisible = nuevo;
    dibujarCalendario();
}

function dibujarCalendario() {
    const contenedor = document.getElementById("diasMes");
    contenedor.innerHTML = "";
    document.getElementById("tituloMes").textContent =
        NOMBRES_MESES[mesVisible.getMonth()] + " " + mesVisible.getFullYear();

    // Espacios vacíos antes del día 1 (para que caiga en el día de la semana correcto)
    const primerDiaSemana = mesVisible.getDay();
    for (let i = 0; i < primerDiaSemana; i++) {
        const vacio = document.createElement("span");
        vacio.className = "vacio";
        contenedor.appendChild(vacio);
    }

    const diasDelMes = new Date(mesVisible.getFullYear(), mesVisible.getMonth() + 1, 0).getDate();
    for (let dia = 1; dia <= diasDelMes; dia++) {
        const fecha = new Date(mesVisible.getFullYear(), mesVisible.getMonth(), dia);
        const iso = aIso(fecha);
        const span = document.createElement("span");
        span.textContent = dia;
        span.dataset.fecha = iso;

        // Días no disponibles: pasados, domingos o más allá del límite
        const noDisponible = fecha < hoy || fecha > fechaMaxima || fecha.getDay() === 0;
        if (noDisponible) {
            span.classList.add("deshabilitado");
        } else {
            span.addEventListener("click", () => seleccionarFecha(span));
        }
        if (iso === reserva.fecha) {
            span.classList.add("activo");
        }
        contenedor.appendChild(span);
    }
}

function seleccionarFecha(span) {
    marcarActivo(".dias-mes span", span);
    reserva.fecha = span.dataset.fecha;
    document.getElementById("fecha").value = reserva.fecha;

    limpiarHora();
    cargarHorarios();
    actualizarPasos();
    actualizarResumen();
}

/* ---------------------------------------------------------------------
 *  PASO 4: horarios (consulta al servidor)
 * --------------------------------------------------------------------- */
async function cargarHorarios() {
    const nota = document.getElementById("notaHorarios");
    const botones = document.querySelectorAll("#contenedorHorarios .hora");

    // Primero se deshabilitan todas las horas
    botones.forEach(b => b.classList.add("deshabilitada"));

    if (!reserva.servicio || !reserva.barbero || !reserva.fecha) {
        nota.textContent = "Selecciona una fecha para ver los horarios libres.";
        return;
    }

    nota.textContent = "Consultando horarios disponibles...";
    const url = CONFIG_AGENDA.urlHorarios
        + "?barberoId=" + encodeURIComponent(reserva.barbero.id)
        + "&fecha=" + encodeURIComponent(reserva.fecha)
        + "&servicioId=" + encodeURIComponent(reserva.servicio.id);

    try {
        const respuesta = await fetch(url, { headers: { "Accept": "application/json" } });
        if (!respuesta.ok) {
            throw new Error("HTTP " + respuesta.status);
        }
        const libres = await respuesta.json();   // ej: ["09:00", "09:30", ...]

        botones.forEach(b => b.classList.toggle("deshabilitada", !libres.includes(b.dataset.hora)));
        nota.textContent = libres.length === 0
            ? "No hay horarios libres ese día. Prueba con otra fecha u otro barbero."
            : libres.length + " horarios disponibles.";
    } catch (error) {
        nota.textContent = "No fue posible consultar los horarios. Intenta de nuevo.";
    }
}

document.querySelectorAll("#contenedorHorarios .hora").forEach(boton => {
    boton.addEventListener("click", () => {
        if (boton.classList.contains("deshabilitada")) {
            return;
        }
        marcarActivo("#contenedorHorarios .hora", boton);
        boton.classList.add("activa");
        reserva.hora = boton.dataset.hora;
        document.getElementById("hora").value = reserva.hora;
        actualizarResumen();
    });
});

function limpiarHora() {
    reserva.hora = null;
    document.getElementById("hora").value = "";
    document.querySelectorAll("#contenedorHorarios .hora").forEach(b => b.classList.remove("activo", "activa"));
}

/* ---------------------------------------------------------------------
 *  Resumen y control de pasos (igual que el prototipo)
 * --------------------------------------------------------------------- */
function actualizarPasos() {
    document.querySelector(".caja-barberos").classList.toggle("paso-bloqueado", !reserva.servicio);
    document.querySelector(".caja-fecha").classList.toggle("paso-bloqueado", !reserva.barbero);
    document.querySelector(".caja-horario").classList.toggle("paso-bloqueado", !reserva.fecha);
}

const btnConfirmar = document.querySelector(".btn-confirmar");

function actualizarResumen() {
    document.querySelector(".valor-servicio").textContent = reserva.servicio ? reserva.servicio.nombre : "-";
    document.querySelector(".valor-barbero").textContent = reserva.barbero ? reserva.barbero.nombre : "-";
    document.querySelector(".valor-fecha").textContent = reserva.fecha ? fechaLegible(reserva.fecha) : "-";
    document.querySelector(".valor-hora").textContent = reserva.hora || "-";
    document.querySelector(".valor-precio").textContent =
        reserva.servicio ? "$" + formatoPesos.format(reserva.servicio.precio) : "-";

    const completo = reserva.servicio && reserva.barbero && reserva.fecha && reserva.hora;
    btnConfirmar.disabled = !completo;
}

/* El botón queda deshabilitado mientras se envía, para evitar doble reserva */
document.getElementById("formCita").addEventListener("submit", () => {
    btnConfirmar.disabled = true;
    btnConfirmar.textContent = "Agendando...";
});

/* ---------------------------------------------------------------------
 *  Si el servidor devolvió el formulario con un error, se recupera
 *  lo que el cliente ya había seleccionado.
 * --------------------------------------------------------------------- */
function restaurarSeleccion() {
    const servicio = document.querySelector(".info-general-servicios-cortes.activo");
    const barbero = document.querySelector(".info-general-barbero.activo");
    if (servicio) {
        reserva.servicio = {
            id: servicio.dataset.id, nombre: servicio.dataset.nombre,
            precio: Number(servicio.dataset.precio), duracion: Number(servicio.dataset.duracion)
        };
    }
    if (barbero) {
        reserva.barbero = { id: barbero.dataset.id, nombre: barbero.dataset.nombre };
    }
    const fecha = document.getElementById("fecha").value;
    if (fecha) {
        reserva.fecha = fecha;
        const f = new Date(fecha + "T00:00:00");
        mesVisible = new Date(f.getFullYear(), f.getMonth(), 1);
        dibujarCalendario();
    }
    document.getElementById("hora").value = "";
    cargarHorarios();
    actualizarPasos();
    actualizarResumen();
}

/* ---------------------------------------------------------------------
 *  Utilidades
 * --------------------------------------------------------------------- */
function marcarActivo(selector, elemento) {
    document.querySelectorAll(selector).forEach(e => e.classList.remove("activo", "activa"));
    elemento.classList.add("activo");
}

function aIso(fecha) {
    const mm = String(fecha.getMonth() + 1).padStart(2, "0");
    const dd = String(fecha.getDate()).padStart(2, "0");
    return fecha.getFullYear() + "-" + mm + "-" + dd;
}

function fechaLegible(iso) {
    const f = new Date(iso + "T00:00:00");
    return f.getDate() + " de " + NOMBRES_MESES[f.getMonth()].toLowerCase() + " " + f.getFullYear();
}
