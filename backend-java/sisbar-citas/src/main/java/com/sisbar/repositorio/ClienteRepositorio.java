package com.sisbar.repositorio;

import com.sisbar.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio de la tabla cliente. */
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {
}
