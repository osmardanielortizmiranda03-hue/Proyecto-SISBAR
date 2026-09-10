// ======================================================
// CITAS SIMULADAS
// ======================================================

const citasSimuladas = {
    "6": [
        {
            hora: "10:00",
            nombre: "Carlos Ramírez",
            servicio: "Corte Clásico",
            estado: "Programada"
        }
    ],

    "12": [
        {
            hora: "15:30",
            nombre: "Diego Soto",
            servicio: "Barba + Corte",
            estado: "Programada"
        }
    ],
    "27": [
        {
            hora: "15:30",
            nombre: "Diego Soto",
            servicio: "Barba + Corte",
            estado: "Programada"
        }
    ],
    "20": [
        {
            hora: "15:30",
            nombre: "Diego Soto",
            servicio: "Barba + Corte",
            estado: "Programada"
        }
    ]
};


// ======================================================
// FECHA ACTUAL
// ======================================================

let fechaActual = new Date();


// ======================================================
// NOMBRES DE LOS MESES
// ======================================================

const nombresMeses = [
    "Enero",
    "Febrero",
    "Marzo",
    "Abril",
    "Mayo",
    "Junio",
    "Julio",
    "Agosto",
    "Septiembre",
    "Octubre",
    "Noviembre",
    "Diciembre"
];


// ======================================================
// CUANDO CARGUE COMPLETAMENTE LA PÁGINA
// ======================================================

document.addEventListener("DOMContentLoaded", () => {

    generarCalendario();

});


// ======================================================
// GENERAR CALENDARIO
// ======================================================

function generarCalendario() {

    // Obtenemos el año actual
    const año = fechaActual.getFullYear();

    // Obtenemos el mes actual
    const mes = fechaActual.getMonth();


    // ==================================================
    // CAMBIAR TÍTULO DEL CALENDARIO
    // ==================================================

    const tituloMes = document.querySelector(
        ".calendar-month-header h2"
    );

    tituloMes.textContent =
        `${nombresMeses[mes]} ${año}`;


    // ==================================================
    // SABER QUÉ DÍA DE LA SEMANA EMPIEZA EL MES
    // ==================================================

    const primerDiaSemana =
        new Date(año, mes, 1).getDay();


    // ==================================================
    // SABER CUÁNTOS DÍAS TIENE EL MES
    // ==================================================

    const diasEnElMes =
        new Date(año, mes + 1, 0).getDate();


    // ==================================================
    // OBTENER EL GRID DEL CALENDARIO
    // ==================================================

    const grid =
        document.querySelector(".calendar-grid");


    // ==================================================
    // ELIMINAR LOS DÍAS DEL CALENDARIO ANTERIOR
    // ==================================================

    const celdasAnteriores =
        document.querySelectorAll(".day-cell");

    celdasAnteriores.forEach(celda => {
        celda.remove();
    });


    // ==================================================
    // CREAR CELDAS VACÍAS
    // ==================================================

    const fragmentoVacios =
        document.createDocumentFragment();


    for (
        let i = 0;
        i < primerDiaSemana;
        i++
    ) {

        const dayDisabled =
            document.createElement("div");

        dayDisabled.classList.add(
            "day-cell",
            "empty"
        );

        fragmentoVacios.appendChild(
            dayDisabled
        );
    }


    grid.appendChild(fragmentoVacios);


    // ==================================================
    // CREAR LOS DÍAS DEL MES
    // ==================================================

    const fragmentoDias =
        document.createDocumentFragment();


    for (
        let i = 0;
        i < diasEnElMes;
        i++
    ) {

        const newDay = document.createElement("div");


        // El día comienza en 1
        newDay.textContent = i + 1;


        newDay.classList.add("day-cell");

        if(citasSimuladas[i + 1]){

            

            const puntoBlanco = document.createElement("DIV");
            newDay.classList.add("punto")
            puntoBlanco.classList.add("punto-blanco")

            newDay.append(puntoBlanco)


        }

        


        // ==============================================
        // EVENTO CLICK
        // ==============================================

        newDay.addEventListener("click", () => {

            // Quitar "activo" de todos los días
            const dias =
                document.querySelectorAll(
                    ".day-cell"
                );

            dias.forEach(dia => {
                dia.classList.remove("activo");
                
            });


            // Marcar el día seleccionado
            newDay.classList.add("activo");

            // Obtener el número del día
            const diaSeleccionado =
                newDay.textContent.trim();


            // Actualizar título de citas
            actualizarTituloCitas(
                diaSeleccionado
            );


            // Mostrar las citas
            mostrarCitas(
                diaSeleccionado
            );

        });


        fragmentoDias.appendChild(
            newDay
        );
    }


    grid.appendChild(fragmentoDias);

}


// ======================================================
// CAMBIAR MES
// ======================================================

function cambiarMes(direccion) {

    /*
        direccion = -1
        significa mes anterior

        direccion = 1
        significa mes siguiente
    */


    fechaActual.setMonth(
        fechaActual.getMonth() + direccion
    );


    // Volvemos a generar el calendario
    generarCalendario();

}


// ======================================================
// BOTONES PARA CAMBIAR DE MES
// ======================================================

document
    .querySelectorAll(".month-nav-btn")
    .forEach((boton, index) => {


        /*
            Primer botón:
            index = 0
            dirección = -1

            Segundo botón:
            index = 1
            dirección = 1
        */


        const direccion =
            index === 0 ? -1 : 1;


        boton.addEventListener(
            "click",
            () => {

                cambiarMes(direccion);

            }
        );

    });


// ======================================================
// ACTUALIZAR TÍTULO DE LAS CITAS
// ======================================================

function actualizarTituloCitas(diaCita) {

    const diaActualizar =
        document.querySelector(
            ".appointments-panel-header h2"
        );


    const mesActual =
        nombresMeses[
            fechaActual.getMonth()
        ];


    diaActualizar.textContent =
        `Citas del ${diaCita} de ${mesActual}`;

}


// ======================================================
// MOSTRAR CITAS
// ======================================================

function mostrarCitas(diaCita) {

    const contenedor =
        document.querySelector(
            ".appointment-list"
        );


    // Limpiar citas anteriores
    contenedor.innerHTML = "";


    // Buscar las citas del día
    const detallesDiaSeleccionado =
        citasSimuladas[diaCita];


    // ==================================================
    // SI EXISTEN CITAS
    // ==================================================

    if (detallesDiaSeleccionado) {


        detallesDiaSeleccionado.forEach(
            cita => {


                // Obtener primera letra del nombre
                const inicial =
                    cita.nombre.charAt(0);


                // Crear tarjeta
                const card =
                    document.createElement("div");


                card.classList.add(
                    "appointment-card"
                );


                // Contenido de la tarjeta
                card.innerHTML = `

                    <div class="appointment-time">

                        <span class="hour">
                            ${cita.hora}
                        </span>

                    </div>


                    <div class="appointment-avatar">
                        ${inicial}
                    </div>


                    <div class="appointment-details">

                        <div class="name">
                            ${cita.nombre}
                        </div>


                        <div class="service">
                            ${cita.servicio}
                        </div>


                        <span class="appointment-badge">
                            ${cita.estado}
                        </span>

                    </div>

                `;


                // Agregar tarjeta al contenedor
                contenedor.appendChild(
                    card
                );

            }
        );


    // ==================================================
    // SI NO EXISTEN CITAS
    // ==================================================

    } else {

        contenedor.innerHTML = `

            <div class="sin-citas">

                <i class="material-icons">
                    event_busy
                </i>

                <p>
                    No hay citas para este día
                </p>

            </div>

        `;
    }

}