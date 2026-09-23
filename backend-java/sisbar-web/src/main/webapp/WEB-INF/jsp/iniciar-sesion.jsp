<%-- Inicio de sesión (diseño original de Pagina de Inicio/iniciar-sesion.html) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- Perfil elegido (se conserva si hubo un error). Por defecto: USUARIO --%>
<c:set var="perfil" value="${empty param.perfil ? 'USUARIO' : param.perfil}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SISBAR - Iniciar Sesión</title>

    <!-- Iconos de Google Material Design -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

    <!-- Framework Materialize CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">

    <!-- Tipografía Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700;800;900&display=swap" rel="stylesheet">

    <!-- Estilos Personalizados -->
    <link rel="stylesheet" href="${ctx}/CSS/pagina%20de%20inicio/iniciar-sesion.css">
    <link rel="stylesheet" href="${ctx}/CSS/mensajes.css">
    <link rel="stylesheet" href="${ctx}/CSS/login-mejoras.css">
</head>
<body>

    <!-- Contenedor Principal de la Tarjeta de Login -->
    <div class="contenedor-login">

        <!-- Botón Superior para Regresar -->
        <div class="regresar-inicio">
            <a href="${ctx}/">VOLVER AL INICIO</a>
        </div>

        <!-- Encabezado: Logo y Títulos de Bienvenida -->
        <div class="logo-sisbar">
            <img src="${ctx}/IMAGES/logo-sisbar.png" alt="Logo SISBAR">
            <h2>SISBAR
                <span>"Estilo y Cuidado"</span>
            </h2>
            <h2 class="h2-bienvenido">Bienvenidos a SISBAR</h2>
            <p>Selecciona tu perfil e ingresa tus credenciales</p>
        </div>
        
        <!-- Sección de Selección de Perfil (Usuario, Barbero, Administrador) -->
        <div class="login-general">
            <div class="login-perfil login-usuario">
                <button type="button" data-perfil="USUARIO" class="waves-effect waves-light ${perfil == 'USUARIO' ? 'seleccionado' : ''}">
                    <i class="material-icons">person_outline</i>
                    <h3>Usuario</h3>
                    <p>Agenda y gestiona tus citas</p>
                </button>
            </div>
            <div class="login-perfil login-barbero">
                <button type="button" data-perfil="BARBERO" class="waves-effect waves-light ${perfil == 'BARBERO' ? 'seleccionado' : ''}">
                    <i class="material-icons">content_cut</i>
                    <h3>Barbero</h3>
                    <p>Gestiona tu agenda de trabajo</p>
                </button>
            </div>
            <div class="login-perfil login-administrador">
                <button type="button" data-perfil="ADMIN" class="waves-effect waves-light ${perfil == 'ADMIN' ? 'seleccionado' : ''}">
                    <i class="material-icons">settings</i>
                    <h3>Administrador</h3>
                    <p>Control total del sistema</p>
                </button>
            </div>
        </div>

        <!-- Formulario de Credenciales -->
        <%-- Mensajes (registro exitoso, sesión cerrada o errores del LoginServlet) --%>
        <div class="mensajes-login">
            <c:if test="${param.registro == 'ok'}">
                <div class="alerta alerta-exito">¡Cuenta creada con éxito! Ya puedes iniciar sesión.</div>
            </c:if>
            <c:if test="${param.salida == 'ok'}">
                <div class="alerta alerta-exito">Cerraste sesión correctamente.</div>
            </c:if>
            <%@ include file="/WEB-INF/jsp/fragmentos/mensajes.jspf" %>
        </div>

        <%-- Formulario HTML enviado por POST al servlet /login --%>
        <form class="login-form" id="formLogin" action="${ctx}/login" method="post">
            <%-- Perfil seleccionado arriba (Usuario, Barbero o Administrador) --%>
            <input type="hidden" name="perfil" id="perfil" value="${fn:escapeXml(perfil)}">

            <!-- Campo: Correo Electrónico -->
            <div class="input-container email">
                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email" placeholder="example@gmail.com" value="${fn:escapeXml(param.email)}" required>
                <i class="material-icons field-icon">email</i>
            </div>

            <!-- Campo: Contraseña -->
            <div class="input-container password">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="••••••••" required>
                <i class="material-icons field-icon ver-password" title="Mostrar contraseña">visibility</i>
            </div>

            <!-- Fila Inferior: Checkbox y Recuperación de Contraseña -->
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

            <!-- Botón de Envío del Formulario -->
            <div class="iniciar-sesion">
                <button type="submit" id="btnLogin" class="waves-effect waves-light btn-large btn-login">
                    <i class="material-icons">login</i> Iniciar Sesión
                </button>
            </div>

            <!-- Enlace alternativo para crear cuenta -->
            <div class="iniciar-sesion-link">
                ¿No tienes cuenta? <a href="${ctx}/registro">Regístrate</a>
            </div>

        </form>
    </div>

    <!-- Scripts de Materialize JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script>
        // 1. Selección de perfil: se marca la tarjeta y se guarda en el campo oculto "perfil"
        var botonesPerfil = document.querySelectorAll('.login-general button');
        botonesPerfil.forEach(function (boton) {
            boton.addEventListener('click', function () {
                botonesPerfil.forEach(function (b) { b.classList.remove('seleccionado'); });
                boton.classList.add('seleccionado');
                document.getElementById('perfil').value = boton.dataset.perfil;
            });
        });

        // 2. Ojo para mostrar / ocultar la contraseña
        var ojo = document.querySelector('.ver-password');
        ojo.addEventListener('click', function () {
            var input = document.getElementById('password');
            var oculta = input.type === 'password';
            input.type = oculta ? 'text' : 'password';
            ojo.textContent = oculta ? 'visibility_off' : 'visibility';
        });

        // 3. El botón muestra "Ingresando..." mientras se envía el formulario
        document.getElementById('formLogin').addEventListener('submit', function () {
            var btn = document.getElementById('btnLogin');
            btn.classList.add('cargando');
            btn.innerHTML = '<i class="material-icons">hourglass_top</i> Ingresando...';
        });
    </script>
</body>
</html>