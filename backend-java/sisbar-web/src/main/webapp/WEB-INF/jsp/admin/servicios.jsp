<%-- ===== Directivas JSP ===== --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.LocalTime" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- ===== Declaración JSP: método disponible en toda la página ===== --%>
<%!
    private String saludo() {
        int hora = LocalTime.now().getHour();
        if (hora < 12) return "Buenos días";
        if (hora < 19) return "Buenas tardes";
        return "Buenas noches";
    }
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- Formato de números en pesos colombianos (25.000) --%>
<fmt:setLocale value="es_CO"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Servicios - Panel Administrador - SISBAR</title>

    <!-- Material Icons & Materialize CSS -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Alegreya+Sans:wght@100..900&family=Barlow+Condensed:wght@200&family=Karla:ital,wght@0,200..800;1,200..800&family=Oswald:wght@200&family=Playfair+Display+SC:wght@400..900&family=Space+Grotesk:wght@300&display=swap" rel="stylesheet">

    <!-- CSS Personalizado -->
    <link rel="stylesheet" href="${ctx}/CSS/panel%20administrador/servicios.css">
    <link rel="stylesheet" href="${ctx}/CSS/mensajes.css">
</head>
<body>

    <!-- LAYOUT PRINCIPAL -->
    <div class="dashboard">

        <!-- SIDEBAR IZQUIERDO -->
        <aside class="sidebar" aria-label="Navegación principal">
            <header class="sidebar-header">
                <div class="fila-superior">
                    <div class="icono" aria-hidden="true">
                        <i class="material-icons">content_cut</i>
                    </div>
                    <h2 class="titulo-app">SISBAR</h2>
                </div>
                <p>Panel Administrativo</p>
            </header>

            <nav class="menu" aria-label="Menú lateral">
                <ul>
                    <li>
                        <a href="#">
                            <i class="material-icons">dashboard</i>
                            <span>Dashboard</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">group</i>
                            <span>Usuarios</span>
                        </a>
                    </li>
                    <li class="activo">
                        <a href="${ctx}/admin/servicios">
                            <i class="material-icons">content_cut</i>
                            <span>Servicios</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">event</i>
                            <span>Citas</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">shopping_bag</i>
                            <span>Productos</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">attach_money</i>
                            <span>Pagos</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">bar_chart</i>
                            <span>Reportes</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">settings</i>
                            <span>Configuración</span>
                        </a>
                    </li>
                    <li>
                        <a href="${ctx}/logout" class="cerrar-sesion">
                            <i class="material-icons">logout</i>
                            <span>Cerrar Sesión</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </aside>

        <!-- CONTENIDO PRINCIPAL -->
        <!-- VISTA DEL FORMULARIO DE RESERVA -->
            <!-- FONDO ANIMADO -->
        <div class="contenido">

            <!-- BARRA SUPERIOR (TOPBAR) -->
            <header class="topbar">
                <nav class="nav-wrapper">
                    <a href="#" class="brand-logo">
                        <img src="${ctx}/IMAGES/logo-sisbar.png" alt="Logo SISBAR">
                    </a>

                    <a href="#" class="sidenav-trigger" data-target="mobile-menu" aria-label="Abrir menú móvil">
                        <i class="material-icons">menu</i>
                    </a>

                    <div class="usuario">
                        <div class="avatar-usuario" aria-hidden="true">${sessionScope.usuario.inicial}</div>
                        <span><c:out value="${sessionScope.usuario.nombreCompleto}"/></span>
                    </div>
                </nav>

                <!-- MENÚ RESPONSIVE -->
                <ul class="sidenav" id="mobile-menu">
                    <li class="sidebar-header">
                        <div class="fila-superior">
                            <div class="icono"><i class="material-icons">content_cut</i></div>
                            <h2>SISBAR</h2>
                        </div>
                        <p>Portal del Cliente</p>
                    </li>
                    <li class="sidebar-header">
                        <div class="fila-superior">
                            <div class="icono"><i class="material-icons">content_cut</i></div>
                            <h2>SISBAR</h2>
                        </div>
                        <p>Portal del Cliente</p>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">dashboard</i>
                            <span>Dashboard</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">group</i>
                            <span>Usuarios</span>
                        </a>
                    </li>
                    <li class="activo">
                        <a href="${ctx}/admin/servicios">
                            <i class="material-icons">content_cut</i>
                            <span>Servicios</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">event</i>
                            <span>Citas</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">shopping_bag</i>
                            <span>Productos</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">attach_money</i>
                            <span>Pagos</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">bar_chart</i>
                            <span>Reportes</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="material-icons">settings</i>
                            <span>Configuración</span>
                        </a>
                    </li>
                    <li>
                        <a href="${ctx}/logout" class="cerrar-sesion">
                            <i class="material-icons">logout</i>
                            <span>Cerrar Sesión</span>
                        </a>
                    </li>
                </ul>
            </header>

            <div class="stars-background">
                <div id="stars"></div>
                <div id="stars2"></div>
                <div id="stars3"></div>
                <main>
                    <!-- SECCIÓN 1: ENCABEZADO -->
                    <section class="header-section">
                        <div class="header-title">
                        <%-- Expresión JSP que llama al método declarado arriba --%>
                        <h3><%= saludo() %>, <c:out value="${sessionScope.usuario.nombres}"/> · Catálogo</h3>
                        <h1>Gestión de Servicios</h1>
                        </div>
                        <button class="btn-nuevo modal-trigger" data-target="modalNuevoServicio">
                        <i class="material-icons">add</i>
                        NUEVO SERVICIO
                        </button>
                    </section>
                    

                    <%-- Mensajes después de crear / actualizar / eliminar (llegan por GET: ?msg=...) --%>
                    <c:choose>
                        <c:when test="${param.msg == 'creado'}">
                            <div class="alerta alerta-exito">Servicio creado correctamente.</div>
                        </c:when>
                        <c:when test="${param.msg == 'actualizado'}">
                            <div class="alerta alerta-exito">Servicio actualizado correctamente.</div>
                        </c:when>
                        <c:when test="${param.msg == 'eliminado'}">
                            <div class="alerta alerta-exito">Servicio eliminado correctamente.</div>
                        </c:when>
                        <c:when test="${param.msg == 'noexiste'}">
                            <div class="alerta alerta-error">El servicio no existe o ya fue eliminado.</div>
                        </c:when>
                    </c:choose>
                    <%@ include file="/WEB-INF/jsp/fragmentos/mensajes.jspf" %>

                    <!-- SECCIÓN 2: GRID DE SERVICIOS (se genera desde la base de datos) -->
                    <section class="services-section">

                        <c:if test="${empty servicios}">
                            <p class="vacio">Aún no hay servicios registrados. Usa el botón "NUEVO SERVICIO".</p>
                        </c:if>

                        <c:forEach var="s" items="${servicios}">
                        <div class="service-card">
                        <div class="service-top">
                            <span class="service-name"><c:out value="${s.nombreServicio}"/></span>
                            <div class="service-actions">
                            <%-- Editar: petición GET con parámetros en la URL --%>
                            <a class="icon-btn editar" title="Editar"
                               href="${ctx}/admin/servicios?accion=editar&amp;id=${s.idServicio}">
                                <i class="material-icons">edit</i>
                            </a>
                            <%-- Eliminar: formulario POST (modifica datos) --%>
                            <form class="form-eliminar" method="post" action="${ctx}/admin/servicios"
                                  onsubmit="return confirm('¿Seguro que deseas eliminar este servicio?');">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="${s.idServicio}">
                                <button type="submit" class="icon-btn eliminar" title="Eliminar">
                                    <i class="material-icons">delete</i>
                                </button>
                            </form>
                            </div>
                        </div>
                        <p class="service-desc">Código del servicio: #${s.idServicio}</p>
                        <div class="service-footer">
                            <span class="service-duration">${s.duracionMinutos} min</span>
                            <span class="service-price">
                                <fmt:formatNumber value="${s.precioServicio}" type="number" maxFractionDigits="0"/> COP
                            </span>
                        </div>
                        </div>
                        </c:forEach>
                    </section>

                    <%-- Scriptlet JSP: código Java incrustado para calcular un dato de la vista --%>
                    <%
                        java.util.List<?> lista = (java.util.List<?>) request.getAttribute("servicios");
                        int total = (lista == null) ? 0 : lista.size();
                    %>
                    <p class="vacio">Total de servicios registrados: <strong><%= total %></strong></p>

                    <!-- MODAL: NUEVO / EDITAR SERVICIO (formulario HTML -> POST al servlet) -->
                    <c:set var="editando" value="${not empty servicioEditar and servicioEditar.idServicio > 0}"/>
                    <div id="modalNuevoServicio" class="modal">
                        <form id="formServicio" method="post" action="${ctx}/admin/servicios">
                        <div class="modal-content">
                        <h4 id="tituloModal">${editando ? 'Editar Servicio' : 'Nuevo Servicio'}</h4>
                        <input type="hidden" name="accion" value="guardar">
                        <input type="hidden" id="idServicio" name="id" value="${editando ? servicioEditar.idServicio : ''}">
                        <div class="row">
                            <div class="input-field col s12">
                            <input id="nombreServicio" name="nombre" type="text" maxlength="100" required
                                   value="${fn:escapeXml(servicioEditar.nombreServicio)}">
                            <label for="nombreServicio" class="${empty servicioEditar ? '' : 'active'}">Nombre del servicio</label>
                            </div>
                            <div class="input-field col s6">
                            <input id="duracionServicio" name="duracion" type="number" min="1" max="600" required
                                   value="${servicioEditar.duracionMinutos > 0 ? servicioEditar.duracionMinutos : ''}">
                            <label for="duracionServicio" class="${empty servicioEditar ? '' : 'active'}">Duración (min)</label>
                            </div>
                            <div class="input-field col s6">
                            <input id="precioServicio" name="precio" type="number" min="1" step="any" required
                                   value="${servicioEditar.precioServicio > 0 ? servicioEditar.precioTexto : ''}">
                            <label for="precioServicio" class="${empty servicioEditar ? '' : 'active'}">Precio ($)</label>
                            </div>
                        </div>
                        </div>
                        <div class="modal-footer">
                        <a href="${ctx}/admin/servicios" class="btn-cancelar">Cancelar</a>
                        <button type="submit" class="btn-guardar">Guardar</button>
                        </div>
                        </form>
                    </div>

                </main>
            </div>
    </div>
    </div> <!-- Cierre correcto del fondo animado -->

    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            M.Sidenav.init(document.querySelectorAll('.sidenav'));
            var modales = M.Modal.init(document.querySelectorAll('.modal'));

            // Botón "NUEVO SERVICIO": limpia el formulario antes de abrir el modal
            var btnNuevo = document.querySelector('.btn-nuevo');
            if (btnNuevo) {
                btnNuevo.addEventListener('click', function () {
                    var form = document.getElementById('formServicio');
                    ['idServicio', 'nombreServicio', 'duracionServicio', 'precioServicio'].forEach(function (id) {
                        form.querySelector('#' + id).value = '';
                    });
                    document.getElementById('tituloModal').textContent = 'Nuevo Servicio';
                    M.updateTextFields();
                });
            }

            // Si el servlet envió un servicio para editar (o hubo errores), el modal se abre solo
            <c:if test="${not empty servicioEditar}">
            M.Modal.getInstance(document.getElementById('modalNuevoServicio')).open();
            </c:if>
        });
    </script>
</body>
</html>
