package com.sisbar.servlet;

import com.sisbar.dao.ServicioDAO;
import com.sisbar.modelo.Servicio;
import com.sisbar.modelo.Usuario;
import com.sisbar.util.Validaciones;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Módulo "Gestión de Servicios" del panel administrador (CRUD completo).
 *
 *   GET  /admin/servicios                        -> listar servicios
 *   GET  /admin/servicios?accion=editar&id=3      -> abrir formulario con los datos del servicio 3
 *   POST /admin/servicios  accion=guardar         -> crear (sin id) o actualizar (con id)
 *   POST /admin/servicios  accion=eliminar&id=3   -> eliminar el servicio 3
 *
 * Las consultas usan GET (no cambian datos) y las modificaciones usan POST.
 */
@WebServlet(name = "ServicioServlet", urlPatterns = "/admin/servicios")
public class ServicioServlet extends HttpServlet {

    private static final String VISTA = "/WEB-INF/jsp/admin/servicios.jsp";
    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdministrador(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        try {
            if ("editar".equals(accion)) {
                int id = Validaciones.aEnteroPositivo(request.getParameter("id"));
                Servicio servicio = servicioDAO.consultarPorId(id);
                if (servicio == null) {
                    request.setAttribute("error", "El servicio que intentas editar no existe.");
                } else {
                    request.setAttribute("servicioEditar", servicio);
                }
            }
            request.setAttribute("servicios", servicioDAO.consultarTodos());
        } catch (SQLException e) {
            log("Error al consultar servicios", e);
            request.setAttribute("error", "No fue posible conectar con la base de datos.");
        }
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdministrador(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        try {
            if ("eliminar".equals(accion)) {
                int id = Validaciones.aEnteroPositivo(request.getParameter("id"));
                boolean ok = id > 0 && servicioDAO.eliminar(id);
                redirigir(request, response, ok ? "eliminado" : "noexiste");
                return;
            }

            if ("guardar".equals(accion)) {
                guardar(request, response);
                return;
            }

            redirigir(request, response, null);

        } catch (SQLException e) {
            log("Error en la gestión de servicios", e);
            request.setAttribute("error", "No fue posible guardar los cambios: " + e.getMessage());
            doGet(request, response);
        }
    }

    /** Crea o actualiza un servicio a partir del formulario del modal */
    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String idTexto = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        int duracion = Validaciones.aEnteroPositivo(request.getParameter("duracion"));
        double precio = Validaciones.aDecimalPositivo(request.getParameter("precio"));

        List<String> errores = new ArrayList<>();
        if (Validaciones.estaVacio(nombre) || nombre.trim().length() > 100) {
            errores.add("El nombre es obligatorio (máximo 100 caracteres).");
        }
        if (duracion <= 0 || duracion > 600) {
            errores.add("La duración debe ser un número de minutos entre 1 y 600.");
        }
        if (precio <= 0) {
            errores.add("El precio debe ser un número mayor que cero.");
        }

        boolean esNuevo = Validaciones.estaVacio(idTexto);
        Servicio servicio = new Servicio(
                esNuevo ? 0 : Validaciones.aEnteroPositivo(idTexto),
                nombre == null ? "" : nombre.trim(),
                Math.max(precio, 0),
                Validaciones.minutosAHora(Math.max(duracion, 0)));

        if (!errores.isEmpty()) {
            // Se vuelve a abrir el modal con lo que el usuario escribió
            request.setAttribute("errores", errores);
            request.setAttribute("servicioEditar", servicio);
            request.setAttribute("servicios", servicioDAO.consultarTodos());
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        if (esNuevo) {
            servicioDAO.insertar(servicio);
            redirigir(request, response, "creado");
        } else {
            boolean ok = servicioDAO.actualizar(servicio);
            redirigir(request, response, ok ? "actualizado" : "noexiste");
        }
    }

    private void redirigir(HttpServletRequest request, HttpServletResponse response, String msg)
            throws IOException {
        String url = request.getContextPath() + "/admin/servicios";
        response.sendRedirect(msg == null ? url : url + "?msg=" + msg);
    }

    /** Solo un usuario con rol ADMIN puede entrar a este módulo */
    private boolean esAdministrador(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion == null) {
            return false;
        }
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        return usuario != null && "ADMIN".equals(usuario.getRol());
    }
}
