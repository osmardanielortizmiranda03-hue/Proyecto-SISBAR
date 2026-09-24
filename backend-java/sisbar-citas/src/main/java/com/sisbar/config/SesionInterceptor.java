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
 *   <li>/barbero/** solo para usuarios con rol BARBERO</li>
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
        boolean permitido = usuario != null && tienePermiso(usuario, ruta);

        if (!permitido) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    /** Cada sección del módulo solo es accesible para su rol. */
    private boolean tienePermiso(UsuarioSesion usuario, String ruta) {
        if (ruta.startsWith("/cliente")) {
            return usuario.esCliente();
        }
        if (ruta.startsWith("/barbero")) {
            return usuario.esBarbero();
        }
        return usuario.esAdmin();
    }
}
