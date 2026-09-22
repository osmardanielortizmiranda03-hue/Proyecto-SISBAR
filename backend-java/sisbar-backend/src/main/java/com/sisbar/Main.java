package com.sisbar;

import com.sisbar.dao.ServicioDAO;
import com.sisbar.modelo.Servicio;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        ServicioDAO servicioDAO = new ServicioDAO();

        // 1. INSERTAR un servicio nuevo
        System.out.println("--- Insertando servicio ---");
        Servicio nuevoServicio = new Servicio(0, "Corte Clásico", 25000.0, "00:30:00");
        servicioDAO.insertar(nuevoServicio);

        // 2. CONSULTAR todos los servicios
        System.out.println("\n--- Listado de servicios ---");
        List<Servicio> servicios = servicioDAO.consultarTodos();
        for (Servicio s : servicios) {
            System.out.println(s);
        }

        // 3. ACTUALIZAR el primer servicio de la lista (si existe)
        if (!servicios.isEmpty()) {
            System.out.println("\n--- Actualizando servicio ---");
            Servicio servicioAActualizar = servicios.get(0);
            servicioAActualizar.setPrecioServicio(30000.0);
            servicioDAO.actualizar(servicioAActualizar);
        }

        // 4. ELIMINAR un servicio por id (cambia el número por uno real que exista)
            System.out.println("\n--- Eliminando servicio ---");
            servicioDAO.eliminar(1);

        // 5. CONSULTAR de nuevo para ver los cambios
        System.out.println("\n--- Listado final de servicios ---");
        List<Servicio> serviciosFinal = servicioDAO.consultarTodos();
        for (Servicio s : serviciosFinal) {
            System.out.println(s);
        }
    }
}