package com.sisbar.servicio;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Reglas de horario de la barbería "Estilo y Cuidado".
 * Tomadas del prototipo agendarcita.html (turnos de 9:00 a 12:30 y de 14:00 a 17:30).
 */
public final class HorarioBarberia {

    /** Minutos entre cada turno que se ofrece al cliente. */
    public static final int INTERVALO_MINUTOS = 30;

    /** Máximo de días hacia adelante en que se puede reservar. */
    public static final int DIAS_MAXIMOS_RESERVA = 30;

    /** Jornada de la mañana y de la tarde (hora de apertura, hora de cierre). */
    public static final List<LocalTime[]> JORNADAS = List.of(
            new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(13, 0)},
            new LocalTime[]{LocalTime.of(14, 0), LocalTime.of(18, 0)}
    );

    private HorarioBarberia() {
        // Clase de constantes: no se instancia
    }

    /** La barbería atiende de lunes a sábado. */
    public static boolean esDiaLaboral(LocalDate fecha) {
        return fecha.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    /**
     * Indica si una cita que empieza en {@code inicio} y dura {@code minutos}
     * cabe completa dentro de alguna jornada.
     */
    public static boolean cabeEnJornada(LocalTime inicio, int minutos) {
        LocalTime fin = inicio.plusMinutes(minutos);
        for (LocalTime[] jornada : JORNADAS) {
            boolean empiezaDentro = !inicio.isBefore(jornada[0]);
            boolean terminaDentro = !fin.isAfter(jornada[1]) && fin.isAfter(inicio);
            if (empiezaDentro && terminaDentro) {
                return true;
            }
        }
        return false;
    }
}
