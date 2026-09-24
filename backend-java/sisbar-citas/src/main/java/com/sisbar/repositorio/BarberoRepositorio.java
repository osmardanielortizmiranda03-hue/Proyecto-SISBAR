package com.sisbar.repositorio;

import com.sisbar.modelo.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Repositorio de la tabla barbero. */
public interface BarberoRepositorio extends JpaRepository<Barbero, Integer> {

    /** Barberos con un estado dado (ej: ACTIVO), para mostrarlos al agendar. */
    List<Barbero> findByEstadoIgnoreCase(String estado);
}
