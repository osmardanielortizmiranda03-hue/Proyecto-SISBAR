package com.sisbar.config;

import com.sisbar.dto.UsuarioSesion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor de seguridad: se ejecuta ANTES de cada controlador protegido.
 *
 * <ul>
 *   <li>/cliente/** solo para usuarios con rol CLIENTE</li>
 *   <li>/admin/**   solo para usuarios con rol ADMIN</li>
 * </ul>
 * Si no hay sesión o el rol no corresponde, redirige al login.
 */
@Component
public class SesionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession sesion = request.getSession(false);
        UsuarioSesion usuario = sesion == null ? null
                : (UsuarioSesion) sesion.getAttribute(UsuarioSesion.ATRIBUTO);

        String ruta = request.getRequestURI().substring(request.getContextPath().length());
        boolean permitido = usuario != null
                && (ruta.startsWith("/cliente") ? usuario.esCliente() : usuario.esAdmin());

        if (!permitido) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
