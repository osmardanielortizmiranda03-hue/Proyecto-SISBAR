package com.sisbar.servlet;

import com.sisbar.dao.ServicioDAO;
import com.sisbar.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * GET /panel -> panel del cliente con el catálogo de servicios (solo lectura).
 */
@WebServlet(name = "PanelClienteServlet", urlPatterns = "/panel")
public class PanelClienteServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        Usuario usuario = sesion == null ? null : (Usuario) sesion.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            request.setAttribute("servicios", servicioDAO.consultarTodos());
        } catch (SQLException e) {
            log("Error al consultar servicios", e);
            request.setAttribute("error", "No fue posible cargar los servicios.");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/cliente/panel.jsp").forward(request, response);
    }
}
