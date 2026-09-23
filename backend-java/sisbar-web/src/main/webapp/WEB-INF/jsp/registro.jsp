<%-- ===== Directivas JSP ===== --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulario de Registro - SISBAR</title>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="${ctx}/CSS/pagina%20de%20inicio/regristro.css">
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
            <h2 class="h2-bienvenido">Crear Cuenta</h2>
            <p>Regístrate para acceder a tu cuenta</p>
        </div>

        <%-- Errores de validación enviados por RegistroServlet --%>
        <%@ include file="/WEB-INF/jsp/fragmentos/mensajes.jspf" %>

        <%-- Formulario HTML enviado por POST al servlet /registro --%>
        <form class="login-form" action="${ctx}/registro" method="post">

            <div class="primer-bloque">
                <div class="input-group">
                    <label for="nombres">Nombres</label>
                    <input type="text" id="nombres" name="nombres" placeholder="Osmar Daniel"
                           value="${fn:escapeXml(param.nombres)}" required>
                </div>
                <div class="input-group">
                    <label for="apellidos">Apellidos</label>
                    <input type="text" id="apellidos" name="apellidos" placeholder="Ortiz Miranda"
                           value="${fn:escapeXml(param.apellidos)}" required>
                </div>
            </div>

            <div class="segundo-bloque">
                <div class="input-group">
                    <label for="numero-identidad">Número de Identidad</label>
                    <input type="number" id="numero-identidad" name="numero-identidad" placeholder="1234567801"
                           value="${fn:escapeXml(param['numero-identidad'])}" required>
                </div>
                <div class="input-group">
                    <label for="numero-cel">Número de Celular</label>
                    <input type="number" id="numero-cel" name="numero-cel" placeholder="3001234567"
                           value="${fn:escapeXml(param['numero-cel'])}" required>
                </div>
            </div>

            <div class="input-group-full">
                <label for="email">Correo Electrónico</label>
                <input type="email" id="email" name="email" placeholder="carlos@ejemplo.com"
                       value="${fn:escapeXml(param.email)}" required>
            </div>

            <div class="input-group-full">
                <label for="fecha-nacimiento">Fecha de Nacimiento</label>
                <input type="date" id="fecha-nacimiento" name="fecha-nacimiento"
                       value="${fn:escapeXml(param['fecha-nacimiento'])}" required>
            </div>

            <div class="input-group-full">
                <label for="nacionalidad">Nacionalidad</label>
                <input type="text" id="nacionalidad" name="nacionalidad" placeholder="Colombiana"
                       value="${fn:escapeXml(param.nacionalidad)}" required>
            </div>

            <div class="input-group-full contenedor-password">
                <label for="password">Contraseña</label>
                <div class="password-wrapper">
                    <input type="password" id="password" name="password" placeholder="Mínimo 8 caracteres" required>
                    <i class="material-icons unsee-icon">visibility</i>
                </div>
            </div>

            <div class="input-group-full contenedor-password">
                <label for="confirm-password">Confirmar Contraseña</label>
                <div class="password-wrapper">
                    <input type="password" id="confirm-password" name="confirm-password" placeholder="Repite tu contraseña" required>
                    <i class="material-icons unsee-icon">visibility</i>
                </div>
            </div>

            <div class="terminos-condiciones">
                <label>
                    <input type="checkbox" required />
                    <span>Acepto los <a href="#">Términos y Condiciones</a> y la <a href="#">Política de Privacidad</a> de SISBAR.</span>
                </label>
            </div>

            <button type="submit" class="btn-registrar">
                <i class="material-icons">person_add</i> REGISTRARSE
            </button>

            <div class="iniciar-sesion-link">
                ¿Ya tienes cuenta? <a href="${ctx}/login">Inicia sesión</a>
            </div>
        </form>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script>
        // Mostrar / ocultar contraseña con el ícono del ojo
        document.querySelectorAll('.unsee-icon').forEach(function (icono) {
            icono.style.cursor = 'pointer';
            icono.addEventListener('click', function () {
                var input = icono.parentElement.querySelector('input');
                var oculto = input.type === 'password';
                input.type = oculto ? 'text' : 'password';
                icono.textContent = oculto ? 'visibility_off' : 'visibility';
            });
        });
    </script>
</body>
</html>
