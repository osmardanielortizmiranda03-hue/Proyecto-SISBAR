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
                                <button class="icon-btn editar" data-target="modalCambiarUsuario"><i class="material-icons">edit</i></button>
                                <button class="icon-btn eliminar"><i class="material-icons">delete</i></button>
                                </div>
                            </td>
    `
    tbody.append(nuevaFila);


})


