# SISBAR – Módulo web con Servlets y JSP

**Evidencia:** GA7-220501096-AA2-EV02 – Módulos de software codificados y probados
**Aprendiz:** Osmar Daniel Ortiz Miranda – Tecnología en Análisis y Desarrollo de Software (SENA)
**Repositorio:** https://github.com/osmardanielortizmiranda03-hue/Proyecto-SISBAR
**Carpeta del módulo en el repositorio:** `backend-java/sisbar-web`

SISBAR es un sistema web para la gestión de una barbería. Este módulo implementa, con **Java Servlets + JSP + JDBC (MySQL)**:

1. **Registro de clientes** (formulario "Crear Cuenta").
2. **Inicio y cierre de sesión** con roles (ADMIN / CLIENTE), usando `HttpSession`.
3. **Gestión de Servicios** del panel administrador: crear, listar, editar y eliminar (CRUD).
4. **Panel del cliente** con el catálogo de servicios.

Las vistas reutilizan el diseño ya construido en el proyecto (HTML + CSS + Materialize).

---

## 1. Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Web | Jakarta Servlet 5.0, JSP 3.0, JSTL 2.0, EL |
| Servidor | Apache Tomcat 10.1 (o `mvn jetty:run`) |
| Base de datos | MySQL 8 con JDBC (`mysql-connector-j`) |
| Construcción | Maven (empaquetado WAR) |
| Pruebas | JUnit 5 + pruebas funcionales de los formularios |
| Front-end | HTML5, CSS3, Materialize 1.0, Material Icons |
| Versionamiento | Git + GitHub |

## 2. Arquitectura (MVC)

```
Navegador ──(formulario HTML GET/POST)──► Servlet (Controlador)
                                             │  valida datos (Validaciones)
                                             │  usa DAO ──JDBC──► MySQL
                                             ▼
                                   JSP (Vista) con JSTL/EL ──► HTML
```

```
sisbar-web/
├── pom.xml
├── sql/sisbar.sql                         ← script de la base de datos
├── src/main/java/com/sisbar/
│   ├── conexion/ConexionBD.java           ← conexión JDBC (lee db.properties)
│   ├── modelo/Servicio.java, Usuario.java ← JavaBeans
│   ├── dao/ServicioDAO.java               ← CRUD de servicios
│   ├── dao/UsuarioDAO.java                ← registro y login
│   ├── util/Validaciones.java             ← validación de formularios
│   ├── util/Seguridad.java                ← hash SHA-256 de contraseñas
│   └── servlet/
│       ├── RegistroServlet.java           ← /registro
│       ├── LoginServlet.java              ← /login
│       ├── LogoutServlet.java             ← /logout
│       ├── PanelClienteServlet.java       ← /panel
│       └── ServicioServlet.java           ← /admin/servicios
├── src/main/resources/db.properties       ← usuario y contraseña de MySQL
├── src/main/webapp/
│   ├── index.jsp
│   ├── CSS/ , IMAGES/
│   └── WEB-INF/
│       ├── web.xml
│       └── jsp/  registro.jsp, iniciar-sesion.jsp, error.jsp,
│                 admin/servicios.jsp, cliente/panel.jsp,
│                 fragmentos/mensajes.jspf, fragmentos/pie.jsp
├── src/test/java/...                      ← pruebas JUnit
└── docs/capturas/                         ← evidencias de funcionamiento
```

Las JSP están dentro de `WEB-INF`, así el usuario no puede abrirlas directamente: siempre pasan por un Servlet.

## 3. Formularios, métodos GET y POST

| URL | Método | Qué hace |
|---|---|---|
| `/registro` | **GET** | Muestra el formulario de registro |
| `/registro` | **POST** | Valida y guarda el cliente (contraseña cifrada SHA-256) |
| `/login` | **GET** | Muestra el formulario de inicio de sesión |
| `/login` | **POST** | Valida credenciales, crea la sesión y redirige según el rol |
| `/logout` | **GET** | Cierra la sesión |
| `/panel` | **GET** | Panel del cliente con el catálogo |
| `/admin/servicios` | **GET** | Lista los servicios |
| `/admin/servicios?accion=editar&id=N` | **GET** | Abre el formulario con los datos del servicio N |
| `/admin/servicios` (accion=guardar) | **POST** | Crea (sin id) o actualiza (con id) un servicio |
| `/admin/servicios` (accion=eliminar) | **POST** | Elimina un servicio |

Se usa **GET** para consultar y **POST** para todo lo que modifica datos. Después de cada POST exitoso se aplica el patrón **PRG (Post-Redirect-Get)** para evitar que un refresco (F5) reenvíe el formulario.

## 4. Elementos JSP utilizados

| Elemento | Ejemplo | Archivo |
|---|---|---|
| Directiva `page` | `<%@ page contentType="text/html; charset=UTF-8" %>` | todas |
| Directiva `taglib` | `<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>` | todas |
| Directiva `include` | `<%@ include file="/WEB-INF/jsp/fragmentos/mensajes.jspf" %>` | registro, login, servicios |
| Declaración | `<%! private String saludo() {...} %>` | admin/servicios.jsp |
| Scriptlet | `<% int total = lista.size(); %>` | admin/servicios.jsp |
| Expresión | `<%= saludo() %>` , `<%= total %>` | admin/servicios.jsp, pie.jsp |
| Acción `jsp:include` | `<jsp:include page="/WEB-INF/jsp/fragmentos/pie.jsp"/>` | index.jsp, panel.jsp |
| Acción `jsp:useBean` | `<jsp:useBean id="hoy" class="java.util.Date"/>` | cliente/panel.jsp |
| Lenguaje de expresiones (EL) | `${sessionScope.usuario.nombres}`, `${param.email}` | todas |
| JSTL core | `c:forEach`, `c:if`, `c:choose`, `c:out`, `c:set` | todas |
| JSTL fmt / fn | `fmt:formatNumber`, `fmt:formatDate`, `fn:escapeXml` | servicios, panel, registro |
| Comentario JSP | `<%-- ... --%>` | todas |

## 5. Cómo ejecutar

1. **Base de datos:** ejecutar `sql/sisbar.sql` en MySQL Workbench (crea la BD `sisbar`, las tablas `servicio` y `usuario`, un administrador y 4 servicios de ejemplo).
2. **Conexión:** revisar usuario y contraseña de MySQL en `src/main/resources/db.properties`.
3. **Compilar y probar:**
   ```bash
   mvn clean package
   ```
   Ejecuta las pruebas JUnit y genera `target/sisbar-web.war`.
4. **Desplegar:** copiar `sisbar-web.war` en la carpeta `webapps` de Tomcat 10.1 e iniciar Tomcat.
   (Opción rápida sin Tomcat: `mvn jetty:run`).
5. Abrir **http://localhost:8080/sisbar-web/**

**Usuario administrador de prueba:** `admin@sisbar.com` / `Admin12345`

## 6. Pruebas realizadas

### 6.1 Pruebas unitarias (JUnit 5) – 9 pruebas, 9 exitosas

| Clase | Qué se prueba |
|---|---|
| `ValidacionesTest` | textos vacíos, correo, contraseña segura, documento/celular, conversión de números, minutos ↔ formato TIME |
| `SeguridadTest` | hash SHA-256 de 64 caracteres y comparación de contraseñas |
| `ServicioTest` | cálculo de duración en minutos y precio para la vista |

### 6.2 Pruebas funcionales de los formularios (Tomcat 10 + base de datos) – 28 casos, 28 exitosos

| # | Caso de prueba | Resultado esperado | Estado |
|---|---|---|---|
| 1 | GET /registro | Muestra el formulario con `method="post"` | ✅ |
| 2 | GET /login | Muestra el formulario de inicio de sesión | ✅ |
| 3 | POST /registro con datos inválidos | Muestra errores y conserva lo escrito | ✅ |
| 4 | POST /registro con datos válidos | Guarda y redirige a /login?registro=ok | ✅ |
| 5 | POST /registro con correo repetido | "Ya existe una cuenta…" | ✅ |
| 6 | Mensaje después del registro | "¡Cuenta creada con éxito!" | ✅ |
| 7 | POST /login con contraseña incorrecta | "Correo o contraseña incorrectos" | ✅ |
| 8 | POST /login como cliente | Redirige a /panel | ✅ |
| 9 | GET /panel | Muestra nombre y servicios con precio 25.000 COP | ✅ |
| 10 | Cliente entra a /admin/servicios | Es redirigido al login | ✅ |
| 11 | /panel sin sesión | Es redirigido al login | ✅ |
| 12 | POST /login como administrador | Redirige a /admin/servicios | ✅ |
| 13 | GET listar servicios | Muestra las 4 tarjetas, total y saludo | ✅ |
| 14 | POST crear servicio | Redirige con msg=creado | ✅ |
| 15 | Servicio nuevo en el listado | Aparece con su duración en minutos | ✅ |
| 16 | GET editar | Abre el modal con los datos cargados | ✅ |
| 17 | POST actualizar | Redirige con msg=actualizado | ✅ |
| 18 | Verificación en la BD | Duración 01:30:00 y precio 22000 guardados | ✅ |
| 19 | POST con campos inválidos | Muestra los 3 errores de validación | ✅ |
| 20 | Nombre con código HTML (`<script>`) | Se muestra escapado (protección XSS) | ✅ |
| 21 | POST eliminar | Redirige con msg=eliminado | ✅ |
| 22 | Verificación en la BD | El servicio ya no existe | ✅ |
| 23 | Eliminar id inexistente | Mensaje "no existe" | ✅ |
| 24 | GET /logout | Cierra sesión | ✅ |
| 25 | Acceso después del logout | Redirige al login | ✅ |
| 26 | URL que no existe | Página de error personalizada (404) | ✅ |
| 27 | Abrir una JSP de WEB-INF directamente | Bloqueado (404) | ✅ |
| 28 | Carga de hojas de estilo | 200 OK | ✅ |

Las capturas de pantalla están en `docs/capturas/`.

## 7. Seguridad aplicada

- Contraseñas guardadas como hash SHA-256 (nunca en texto plano).
- Consultas con `PreparedStatement` (evita inyección SQL).
- Salida escapada con `c:out` y `fn:escapeXml` (evita XSS).
- Control de acceso por rol en los servlets.
- JSP protegidas dentro de `WEB-INF`.
