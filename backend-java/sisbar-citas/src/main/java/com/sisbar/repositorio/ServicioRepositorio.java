package com.sisbar.repositorio;

import com.sisbar.modelo.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Repositorio de la tabla servicio. */
public interface ServicioRepositorio extends JpaRepository<Servicio, Integer> {

    /** Todos los servicios ordenados por precio (del más económico al más costoso). */
    List<Servicio> findAllByOrderByPrecioAsc();
}
