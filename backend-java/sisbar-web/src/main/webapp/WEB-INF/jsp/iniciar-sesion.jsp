<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SISBAR - Iniciar Sesión</title>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/CSS/pagina%20de%20inicio/iniciar-sesion.css">
    <link rel="stylesheet" href="${ctx}/CSS/mensajes.css">
</head>
<body>

    <div class="contenedor-login">

        <div class="regresar-inicio">
            <a href="${ctx}/">VOLVER AL INICIO</a>
        </div>

        <div class="logo-sisbar">
            <img src="${ctx}/IMAGES/logo-sisbar.png" alt="Logo SISBAR">
            <h2>SISBAR
                <span>"Estilo y Cuidado"</span>
            </h2>
            <h2 class="h2-bienvenido">Bienvenidos a SISBAR</h2>
            <p>Ingresa tus credenciales</p>
        </div>

        <%-- Mensajes que llegan por parámetro GET después de una redirección --%>
        <c:choose>
            <c:when test="${param.registro == 'ok'}">
                <div class="alerta alerta-exito">¡Cuenta creada con éxito! Ya puedes iniciar sesión.</div>
            </c:when>
            <c:when test="${param.salida == 'ok'}">
                <div class="alerta alerta-exito">Cerraste sesión correctamente.</div>
            </c:when>
        </c:choose>

        <%@ include file="/WEB-INF/jsp/fragmentos/mensajes.jspf" %>

        <%-- Formulario HTML enviado por POST al servlet /login --%>
        <form class="login-form" action="${ctx}/login" method="post">

            <div class="input-container email">
                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email" placeholder="example@gmail.com"
                       value="${fn:escapeXml(param.email)}" required>
                <i class="material-icons field-icon">email</i>
            </div>

            <div class="input-container password">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="••••••••" required>
                <i class="material-icons field-icon">visibility</i>
            </div>

            <div class="enlaces-checkbox">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" class="filled-in" name="recordarme" />
                        <span>Recordarme</span>
                    </label>
                </div>
                <div class="contraseña-forget">
                    <a href="#">¿Olvidaste Tu Contraseña?</a>
                </div>
            </div>

            <div class="iniciar-sesion">
                <button type="submit" class="waves-effect waves-light btn-large btn-login">
                    <i class="material-icons">login</i> Iniciar Sesión
                </button>
            </div>

            <div class="iniciar-sesion-link">
                ¿No tienes cuenta? <a href="${ctx}/registro">Regístrate</a>
            </div>
        </form>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>
