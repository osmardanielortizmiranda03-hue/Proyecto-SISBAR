package com.sisbar.repositorio;

import com.sisbar.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de usuarios. Al extender {@link JpaRepository}, Spring Data
 * genera automáticamente los métodos save, findById, findAll, delete, etc.
 */
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    /** Spring arma la consulta a partir del nombre: WHERE correo_usuario = ? */
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
}
