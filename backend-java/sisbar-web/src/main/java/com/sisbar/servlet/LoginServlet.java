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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet de inicio de sesión.
 *   GET  /login -> muestra el formulario (iniciar-sesion.jsp)
 *   POST /login -> valida correo y contraseña, crea la sesión y redirige según el rol
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/iniciar-sesion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!Validaciones.esEmailValido(email) || Validaciones.estaVacio(password)) {
            mostrarError(request, response, "Escribe un correo y una contraseña válidos.");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(email.trim().toLowerCase());

            if (usuario == null || !Seguridad.coincide(password, usuario.getPasswordHash())) {
                mostrarError(request, response, "Correo o contraseña incorrectos.");
                return;
            }

            // El perfil elegido en las tarjetas (Usuario, Barbero, Administrador)
            // debe coincidir con el rol guardado en la base de datos
            String perfil = request.getParameter("perfil");
            String rolEsperado = "USUARIO".equals(perfil) ? "CLIENTE" : perfil;
            if (rolEsperado != null && !rolEsperado.equals(usuario.getRol())) {
                String nombrePerfil = "ADMIN".equals(perfil) ? "Administrador"
                        : "BARBERO".equals(perfil) ? "Barbero" : "Usuario";
                mostrarError(request, response,
                        "Esta cuenta no tiene el perfil de " + nombrePerfil + ". Selecciona el perfil correcto.");
                return;
            }

            // Evitamos guardar el hash en la sesión
            usuario.setPasswordHash(null);

            // Se crea la sesión HTTP y se guarda el usuario autenticado
            HttpSession sesion = request.getSession(true);
            sesion.setAttribute("usuario", usuario);
            sesion.setMaxInactiveInterval(30 * 60); // 30 minutos

            if ("ADMIN".equals(usuario.getRol())) {
                response.sendRedirect(request.getContextPath() + "/admin/servicios");
            } else {
                response.sendRedirect(request.getContextPath() + "/panel");
            }

        } catch (SQLException e) {
            log("Error en el login", e);
            mostrarError(request, response, "No fue posible conectar con la base de datos.");
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/WEB-INF/jsp/iniciar-sesion.jsp").forward(request, response);
    }
}
