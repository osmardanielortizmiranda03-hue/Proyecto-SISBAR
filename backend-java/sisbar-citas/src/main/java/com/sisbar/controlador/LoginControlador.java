package com.sisbar.controlador;

import com.sisbar.dto.UsuarioSesion;
import com.sisbar.servicio.AutenticacionServicio;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador de inicio y cierre de sesión.
 */
@Controller
public class LoginControlador {

    private final AutenticacionServicio autenticacionServicio;

    public LoginControlador(AutenticacionServicio autenticacionServicio) {
        this.autenticacionServicio = autenticacionServicio;
    }

    /** Raíz del módulo: envía a cada usuario a su pantalla principal. */
    @GetMapping("/")
    public String inicio(HttpSession sesion) {
        UsuarioSesion usuario = (UsuarioSesion) sesion.getAttribute(UsuarioSesion.ATRIBUTO);
        if (usuario == null) {
            return "redirect:/login";
        }
        return usuario.esAdmin() ? "redirect:/admin/citas" : "redirect:/cliente/citas/nueva";
    }

    /** GET /login: muestra el formulario. */
    @GetMapping("/login")
    public String formularioLogin() {
        return "login";
    }

    /** POST /login: valida credenciales y crea la sesión. */
    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String email,
                                @RequestParam String password,
                                @RequestParam(defaultValue = "USUARIO") String perfil,
                                HttpServletRequest request,
                                Model model) {

        Optional<UsuarioSesion> usuario = autenticacionServicio.autenticar(email, password, perfil);
        if (usuario.isEmpty()) {
            model.addAttribute("error", "Correo, contraseña o perfil incorrectos.");
            model.addAttribute("email", email);
            model.addAttribute("perfil", perfil);
            return "login";
        }

        // Se cambia el id de sesión al iniciar sesión (buena práctica de seguridad)
        request.getSession(true);
        request.changeSessionId();
        request.getSession().setAttribute(UsuarioSesion.ATRIBUTO, usuario.get());
        return "redirect:/";
    }

    /** GET /logout: cierra la sesión. */
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession sesion, RedirectAttributes flash) {
        sesion.invalidate();
        flash.addFlashAttribute("exito", "Cerraste sesión correctamente.");
        return "redirect:/login";
    }
}
