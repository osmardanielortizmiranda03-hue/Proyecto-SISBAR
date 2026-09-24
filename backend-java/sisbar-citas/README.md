# SISBAR – Módulo de Citas con Spring Boot

**Evidencia:** GA7-220501096-AA3-EV01 – Codificación de módulos del software stand-alone, web y móvil
**Aprendiz:** Osmar Daniel Ortiz Miranda – Tecnología en Análisis y Desarrollo de Software (SENA)
**Repositorio:** https://github.com/osmardanielortizmiranda03-hue/Proyecto-SISBAR
**Carpeta del módulo:** `backend-java/sisbar-citas`

Módulo web del sistema SISBAR (barbería "Estilo y Cuidado") que permite:

| Actor | Funcionalidad |
|---|---|
| Cliente | Agendar una cita en 5 pasos: servicio → barbero → fecha → hora → observaciones |
| Cliente | Ver el historial de sus citas y cancelar las que aún no han pasado |
| Administrador | Ver todas las citas, filtrarlas por estado o fecha y cambiar su estado |

Las vistas conservan el diseño de los prototipos del proyecto (`agendarcita.html`, `miscitas.html`, `citas.html` e `iniciar-sesion.html`).

---

## 1. Framework y tecnologías

| Tecnología | Uso en el módulo |
|---|---|
| **Spring Boot 3.5** | Framework principal: configuración automática y servidor Tomcat incluido |
| **Spring MVC** | Controladores que atienden las peticiones GET/POST |
| **Spring Data JPA + Hibernate** | Acceso a MySQL con entidades y repositorios (sin escribir SQL a mano) |
| **Thymeleaf** | Plantillas HTML con datos dinámicos |
| **Bean Validation** | Validación del formulario con anotaciones (`@NotNull`, `@Size`) |
| **JUnit 5 + Mockito** | Pruebas unitarias de las reglas de negocio |
| MySQL 8 | Base de datos `sisbar` (la misma del proyecto) |
| Maven | Gestión de dependencias y construcción |
| Git + GitHub | Control de versiones |

## 2. Arquitectura por capas

```
Navegador
   │  (GET / POST)
   ▼
controlador/   ← recibe la petición, valida el formulario y elige la vista
   │
   ▼
servicio/      ← reglas del negocio (horarios libres, cancelaciones, estados)
   │
   ▼
repositorio/   ← Spring Data JPA genera las consultas
   │
   ▼
modelo/        ← entidades JPA = tablas de MySQL (usuario, cliente, barbero, servicio, citas)
```

```
sisbar-citas/
├── pom.xml
├── sql/modulo_citas.sql                    ← script de base de datos del módulo
└── src/
    ├── main/java/com/sisbar/
    │   ├── SisbarCitasApplication.java     ← clase principal (arranca la app)
    │   ├── config/                         ← interceptor de sesión, reloj, configuración MVC
    │   ├── controlador/                    ← LoginControlador, CitaClienteControlador, CitaAdminControlador
    │   ├── dto/                            ← CitaForm (formulario), UsuarioSesion
    │   ├── modelo/                         ← Usuario, Cliente, Barbero, Servicio, Cita, EstadoCita
    │   ├── repositorio/                    ← interfaces JpaRepository
    │   ├── servicio/                       ← CitaServicio, AutenticacionServicio, HorarioBarberia
    │   └── util/Seguridad.java             ← hash SHA-256 de contraseñas
    ├── main/resources/
    │   ├── application.properties          ← conexión a MySQL, puerto, etc.
    │   ├── templates/                      ← vistas Thymeleaf
    │   └── static/                         ← css, js, imágenes
    └── test/java/…/CitaServicioTest.java   ← pruebas unitarias
```

## 3. Relación con los artefactos del proyecto

- **Diagrama de clases / modelo de datos:** las entidades JPA corresponden a las tablas `usuario`, `cliente`, `barbero`, `servicio` y `citas`, respetando sus llaves foráneas (`BARBERO_idUSUARIO`, `CLIENTE_idUSUARIO`).
- **Casos de uso:** *Agendar cita*, *Consultar mis citas*, *Cancelar cita* (cliente) y *Gestionar citas* (administrador).
- **Historias de usuario:** "Como cliente quiero reservar una cita eligiendo servicio, barbero, fecha y hora para asegurar mi turno".
- **Prototipos:** se reutilizaron los HTML/CSS de `panel usuario` y `panel administrador`.

## 4. Reglas de negocio implementadas

1. Horario de atención: lunes a sábado, 9:00 a 13:00 y 14:00 a 18:00, turnos cada 30 minutos.
2. Solo se puede reservar desde hoy hasta 30 días adelante; no se ofrecen horas que ya pasaron.
3. Un horario está libre solo si la cita (según la duración del servicio) **no se cruza** con otra cita activa del mismo barbero.
4. Solo aparecen barberos con estado `ACTIVO`.
5. Toda cita nueva queda en estado **PENDIENTE**.
6. El cliente solo puede cancelar **sus** citas pendientes o confirmadas que aún no han pasado.
7. El administrador no puede modificar citas **completadas** ni **canceladas**.

## 5. Estándares de codificación aplicados

- Convenciones de Java: clases en `PascalCase`, métodos y variables en `camelCase`, constantes en `MAYUSCULAS_CON_GUION`.
- Paquetes por responsabilidad (controlador, servicio, repositorio, modelo, dto, config, util).
- Inyección de dependencias por constructor (sin `@Autowired` en atributos).
- Comentarios Javadoc en clases y métodos públicos, y comentarios de línea en la lógica importante.
- Nombres en español, coherentes con la base de datos y el resto del proyecto.
- Sin SQL concatenado: Spring Data usa consultas parametrizadas (evita inyección SQL).
- Operaciones que modifican datos con **POST**; consultas con **GET**.
- Salida escapada por Thymeleaf (`th:text`) para evitar XSS.

## 6. Cómo ejecutar

1. **Base de datos:** en MySQL Workbench ejecutar `sql/modulo_citas.sql`. Agrega a `citas` las columnas `idservicio` y `observaciones` (solo si no existen) y crea datos de prueba.
2. **Conexión:** revisar usuario/contraseña de MySQL en `src/main/resources/application.properties`.
3. **Ejecutar:**
   ```bash
   cd backend-java/sisbar-citas
   mvn spring-boot:run
   ```
4. Abrir **http://localhost:8081/sisbar-citas/**

| Perfil | Correo | Contraseña |
|---|---|---|
| Usuario (cliente) | cliente@sisbar.com | Cliente123 |
| Administrador | admin@sisbar.com | Admin12345 |

5. **Pruebas unitarias:** `mvn test`

## 7. Rutas del módulo

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/login` | Formulario de inicio de sesión |
| POST | `/login` | Valida credenciales y perfil |
| GET | `/logout` | Cierra la sesión |
| GET | `/cliente/citas/nueva` | Formulario "Agendar Cita" |
| POST | `/cliente/citas/nueva` | Guarda la cita |
| GET | `/cliente/citas/horarios` | Horarios libres en JSON (lo usa JavaScript) |
| GET | `/cliente/citas` | Mis citas |
| POST | `/cliente/citas/{id}/cancelar` | Cancela una cita |
| GET | `/admin/citas` | Listado con filtros (`estado`, `fecha`) |
| POST | `/admin/citas/{id}/estado` | Cambia el estado de una cita |

## 8. Pruebas realizadas

**Pruebas unitarias (JUnit 5 + Mockito): 14 de 14 exitosas.** Verifican los horarios libres, los cruces de citas, los domingos y fechas no válidas, el agendamiento, la creación automática del cliente, la cancelación (solo citas propias) y los cambios de estado del administrador.

**Pruebas funcionales con la aplicación corriendo y la estructura real de la base de datos: 29 de 29 exitosas**, entre ellas:

- El login rechaza contraseñas incorrectas y perfiles que no corresponden.
- Un cliente no puede entrar al panel del administrador.
- El formulario carga los servicios y barberos desde MySQL.
- Al agendar, la cita queda guardada como PENDIENTE, con su servicio y sus observaciones.
- Los horarios que se cruzan con otra cita desaparecen, y un mismo horario no se puede reservar dos veces.
- Al cancelar una cita, su horario vuelve a quedar libre.
- Los filtros del administrador (por estado y por fecha) y el cambio de estado funcionan.
- No se puede modificar una cita cancelada.

Las capturas de pantalla están en `docs/capturas/`.
