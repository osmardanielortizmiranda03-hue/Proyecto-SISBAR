package com.sisbar.controlador;

import com.sisbar.dto.UsuarioSesion;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Agrega a TODAS las vistas el usuario que inició sesión (variable "usuario"),
 * para mostrar su nombre y su inicial en la barra superior.
 */
@ControllerAdvice
public class DatosGlobalesAdvice {

    @ModelAttribute("usuario")
    public UsuarioSesion usuarioActual(HttpSession sesion) {
        return (UsuarioSesion) sesion.getAttribute(UsuarioSesion.ATRIBUTO);
    }
}
