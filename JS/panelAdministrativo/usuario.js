document.addEventListener("DOMContentLoaded", () => {
    M.AutoInit();
});

document.addEventListener('DOMContentLoaded', () => {
    M.Modal.init(document.querySelectorAll('.modal'));
    M.FormSelect.init(document.querySelectorAll('select'));
});


const  nombreUsuario = document.getElementById("nombreUsuario");
const  correoUsuario = document.getElementById("correoUsuario");
const  telefonoUsuario = document.getElementById("telefonoUsuario");
const  rolUsuario = document.getElementById("rolUsuario");
const  modalFooter= document.querySelector(".modal-footer")
const btnGuardar = document.querySelector(".btn-guardar")
const tbody = document.getElementById("tbody")


btnGuardar.addEventListener("click", (e) =>{
    e.preventDefault();

    const nuevoUsuario = {
        nombre: nombreUsuario.value.trim(),
        correo: correoUsuario.value.trim(),
        telefono: telefonoUsuario.value.trim(),
        rol: rolUsuario.value
    };

    
    if(nuevoUsuario.nombre && nuevoUsuario.correo && nuevoUsuario.telefono && nuevoUsuario.rol){
        console.log("Nuevo usuario a registrar");
    } else {
        const div = document.createElement("DIV");
        div.textContent = "Todos los campos son obligatorios.";
        div.style.color = "#ff5252";
        modalFooter.append(div);
        return;
    }
    

    const letraInicial = nuevoUsuario.nombre.charAt(0).toUpperCase();
    const fechaHoy = new Date().toISOString().split('T')[0];
    const rol = nuevoUsuario.rol.toLowerCase();

    const nuevaFila = document.createElement("TR");

    nuevaFila.innerHTML = `           <td>
                                <div class="usuario-cell">
                                <div class="avatar">${letraInicial}</div>
                                <span class="nombre">${nuevoUsuario.nombre}</span>
                                </div>
                            </td>
                            <td class="correo">${nuevoUsuario.correo}</td>
                            <td class="telefono">${nuevoUsuario.telefono}</td>
                            <td><span class="badge ${rol}">${nuevoUsuario.rol}</span></td>
                            <td class="desde">${fechaHoy}</td>
                            <td>
                                <div class="acciones">
                                <button class="icon-btn editar modal-trigger" data-target="modalCambiarUsuario"><i class="material-icons">edit</i></button>
                                <button class="icon-btn eliminar"><i class="material-icons">delete</i></button>
                                </div>
                            </td>
    `
    tbody.append(nuevaFila);

})

const modificar = document.querySelectorAll(".icon-btn.editar");
const guardarModal = document.querySelector(".modalModificar")

let modificarFila = null;

modificar.forEach(i => {

    i.addEventListener("click", (e) => {

        modificarFila = i.closest("tr");

        const nombreActual = modificarFila.querySelector(".nombre").textContent
        const correoActual = modificarFila.querySelector(".correo").textContent
        const telefonoActual = modificarFila.querySelector(".telefono").textContent
        const rolActual = modificarFila.querySelector(".badge").textContent.toLowerCase()

        console.log(modificarFila)

        document.querySelector("#nombreUsuarioModificar").value = nombreActual
        document.querySelector("#correoUsuarioModificar").value = correoActual
        document.querySelector("#telefonoUsuarioModificar").value = telefonoActual
        
        const inputRol = document.querySelector("#rolUsuarioModificar");

        if (inputRol) {
            // Se corrigió selectRol -> inputRol
            inputRol.value = rolActual;
            
            // Re-inicializar el select en Materialize
            M.FormSelect.init(inputRol);
        }
        M.updateTextFields()
    })
})

if(guardarModal) {

    guardarModal.addEventListener("click", (e) => {

        const nombreNuevo = document.querySelector("#nombreUsuarioModificar").value
        const correoNuevo = document.querySelector("#correoUsuarioModificar").value
        const telefonoNuevo = document.querySelector("#telefonoUsuarioModificar").value
        const rolNuevo = document.querySelector("#rolUsuarioModificar").value

        modificarFila.querySelector(".nombre").textContent = nombreNuevo;
        modificarFila.querySelector(".correo").textContent = correoNuevo;
        modificarFila.querySelector(".telefono").textContent = telefonoNuevo;
        modificarFila.querySelector(".badge").textContent = rolNuevo;
        modificarFila.querySelector(".avatar").textContent = nombreNuevo.charAt(0).toUpperCase()

        const badge = modificarFila.querySelector(".badge");

        if (badge) {
            // 2. Formatear texto: primera letra en mayúscula + el resto de la cadena
            badge.textContent = rolNuevo.charAt(0).toUpperCase() + rolNuevo.slice(1);
            
            // 3. Asignación limpia de clases (mantiene la clase 'badge' y agrega el rol)
            badge.className = `badge ${rolNuevo.toLowerCase()}`;
        }
    })
}

const eliminarCelda = document.querySelectorAll(".eliminar");

eliminarCelda.forEach(eliminar =>{

    eliminar.addEventListener("click", (e) => {

        const celdaEliminada = eliminar.closest("tr");

        celdaEliminada.remove()
    })
})






