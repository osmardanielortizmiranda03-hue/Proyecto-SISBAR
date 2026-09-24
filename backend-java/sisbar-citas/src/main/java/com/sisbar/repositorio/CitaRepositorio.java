package com.sisbar.repositorio;

import com.sisbar.modelo.Cita;
import com.sisbar.modelo.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio de citas. Combina métodos "derivados" (Spring genera el SQL
 * a partir del nombre) y una consulta JPQL escrita con {@link Query}.
 */
public interface CitaRepositorio extends JpaRepository<Cita, Integer> {

    /** Citas de un cliente, de la más reciente a la más antigua. */
    List<Cita> findByClienteIdOrderByFechaCitaDesc(Integer clienteId);

    /** Citas de un barbero dentro de un rango de fechas que NO estén en el estado indicado. */
    List<Cita> findByBarberoIdAndFechaCitaBetweenAndEstadoNot(
            Integer barberoId, LocalDateTime desde, LocalDateTime hasta, EstadoCita estadoExcluido);

    /**
     * Listado para el administrador con filtros opcionales.
     * Si un parámetro llega en null, ese filtro no se aplica.
     */
    @Query("""
            SELECT c FROM Cita c
            WHERE (:estado IS NULL OR c.estado = :estado)
              AND (:desde IS NULL OR c.fechaCita >= :desde)
              AND (:hasta IS NULL OR c.fechaCita < :hasta)
            ORDER BY c.fechaCita DESC
            """)
    List<Cita> buscarConFiltros(@Param("estado") EstadoCita estado,
                                @Param("desde") LocalDateTime desde,
                                @Param("hasta") LocalDateTime hasta);

    /** Cantidad de citas por estado (para las tarjetas de resumen del administrador). */
    long countByEstado(EstadoCita estado);
}
