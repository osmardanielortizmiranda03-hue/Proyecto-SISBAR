<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SISBAR - Estilo y Cuidado</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/CSS/pagina%20de%20inicio/iniciar-sesion.css">
</head>
<body>
    <div class="contenedor-login">
        <div class="logo-sisbar">
            <img src="${ctx}/IMAGES/logo-sisbar.png" alt="Logo SISBAR">
            <h2>SISBAR <span>"Estilo y Cuidado"</span></h2>
            <h2 class="h2-bienvenido">Bienvenido</h2>
            <%-- Si ya hay sesión se saluda al usuario, si no, se muestran los enlaces --%>
            <c:choose>
                <c:when test="${not empty sessionScope.usuario}">
                    <p>Hola, <c:out value="${sessionScope.usuario.nombres}"/>.</p>
                </c:when>
                <c:otherwise>
                    <p>Agenda tu cita y conoce nuestros servicios</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="iniciar-sesion">
            <c:choose>
                <c:when test="${sessionScope.usuario.rol == 'ADMIN'}">
                    <a class="waves-effect waves-light btn-large btn-login" href="${ctx}/admin/servicios">
                        <i class="material-icons">content_cut</i> Gestión de Servicios</a>
                </c:when>
                <c:when test="${not empty sessionScope.usuario}">
                    <a class="waves-effect waves-light btn-large btn-login" href="${ctx}/panel">
                        <i class="material-icons">dashboard</i> Ir a mi panel</a>
                </c:when>
                <c:otherwise>
                    <a class="waves-effect waves-light btn-large btn-login" href="${ctx}/login">
                        <i class="material-icons">login</i> Iniciar Sesión</a>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="iniciar-sesion-link">
            ¿No tienes cuenta? <a href="${ctx}/registro">Regístrate</a>
        </div>
    </div>
    <jsp:include page="/WEB-INF/jsp/fragmentos/pie.jsp"/>
</body>
</html>
