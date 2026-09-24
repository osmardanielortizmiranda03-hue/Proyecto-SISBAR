package com.sisbar.servicio;

import com.sisbar.dto.UsuarioSesion;
import com.sisbar.repositorio.UsuarioRepositorio;
import com.sisbar.util.Seguridad;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio de inicio de sesión. Verifica correo, contraseña y perfil
 * contra la tabla {@code usuario}.
 */
@Service
public class AutenticacionServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    public AutenticacionServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    /**
     * Intenta autenticar al usuario.
     *
     * @param correo     correo escrito en el formulario
     * @param contrasena contraseña escrita en el formulario
     * @param perfil     perfil elegido en las tarjetas (USUARIO, BARBERO o ADMIN)
     * @return los datos de sesión si todo coincide; vacío si no
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioSesion> autenticar(String correo, String contrasena, String perfil) {
        if (correo == null || contrasena == null) {
            return Optional.empty();
        }
        String rolEsperado = "USUARIO".equals(perfil) ? "CLIENTE" : perfil;

        return usuarioRepositorio.findByCorreoIgnoreCase(correo.trim())
                .filter(u -> Seguridad.coincide(contrasena, u.getContrasenaHash()))
                .filter(u -> rolEsperado == null || rolEsperado.equals(u.getRol()))
                .map(u -> new UsuarioSesion(u.getId(), u.getNombre(), u.getApellido(), u.getRol()));
    }
}
