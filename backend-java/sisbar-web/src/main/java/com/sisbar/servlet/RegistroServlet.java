package com.sisbar.servlet;

import com.sisbar.dao.UsuarioDAO;
import com.sisbar.modelo.Usuario;
import com.sisbar.util.Seguridad;
import com.sisbar.util.Validaciones;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet del formulario "Crear Cuenta".
 *   GET  /registro  -> muestra el formulario (registro.jsp)
 *   POST /registro  -> recibe los datos, valida y guarda el usuario como CLIENTE
 */
@WebServlet(name = "RegistroServlet", urlPatterns = "/registro")
public class RegistroServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Leer los campos del formulario HTML (atributo name de cada input)
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String numeroIdentidad = request.getParameter("numero-identidad");
        String celular = request.getParameter("numero-cel");
        String email = request.getParameter("email");
        String fechaNacimiento = request.getParameter("fecha-nacimiento");
        String nacionalidad = request.getParameter("nacionalidad");
        String password = request.getParameter("password");
        String confirmar = request.getParameter("confirm-password");

        // 2. Validar
        List<String> errores = new ArrayList<>();
        if (Validaciones.estaVacio(nombres) || Validaciones.estaVacio(apellidos)) {
            errores.add("Los nombres y apellidos son obligatorios.");
        }
        if ((nombres != null && nombres.trim().length() > 45) || (apellidos != null && apellidos.trim().length() > 100)) {
            errores.add("El nombre admite máximo 45 caracteres y el apellido máximo 100.");
        }
        if (!Validaciones.esNumeroDocumentoValido(numeroIdentidad)) {
            errores.add("El número de identidad debe tener solo números (6 a 15 dígitos).");
        }
        if (!Validaciones.esNumeroDocumentoValido(celular)) {
            errores.add("El número de celular no es válido.");
        }
        if (!Validaciones.esEmailValido(email)) {
            errores.add("El correo electrónico no es válido.");
        }
        if (Validaciones.estaVacio(fechaNacimiento)) {
            errores.add("La fecha de nacimiento es obligatoria.");
        }
        if (!Validaciones.esPasswordValida(password)) {
            errores.add("La contraseña debe tener mínimo 8 caracteres, con letras y números.");
        } else if (!password.equals(confirmar)) {
            errores.add("Las contraseñas no coinciden.");
        }

        try {
            if (errores.isEmpty() && usuarioDAO.existe(email.trim(), numeroIdentidad.trim())) {
                errores.add("Ya existe una cuenta con ese correo o número de identidad.");
            }

            if (!errores.isEmpty()) {
                // Volver al formulario mostrando los errores (los datos escritos se conservan con ${param.x})
                request.setAttribute("errores", errores);
                request.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(request, response);
                return;
            }

            // 3. Crear el objeto y guardarlo
            Usuario usuario = new Usuario();
            usuario.setNombres(nombres.trim());
            usuario.setApellidos(apellidos.trim());
            usuario.setNumeroIdentidad(numeroIdentidad.trim());
            usuario.setCelular(celular.trim());
            usuario.setEmail(email.trim().toLowerCase());
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setNacionalidad(nacionalidad == null ? "" : nacionalidad.trim());
            usuario.setPasswordHash(Seguridad.hashSha256(password));
            usuario.setRol("CLIENTE");
            usuarioDAO.registrar(usuario);

            // 4. Patrón PRG (Post-Redirect-Get): redirigimos al login con un mensaje
            response.sendRedirect(request.getContextPath() + "/login?registro=ok");

        } catch (SQLException e) {
            log("Error al registrar usuario", e);
            errores.add("No fue posible conectar con la base de datos. Intenta más tarde.");
            request.setAttribute("errores", errores);
            request.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(request, response);
        }
    }
}
