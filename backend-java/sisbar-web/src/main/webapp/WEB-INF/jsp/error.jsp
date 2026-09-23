<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error - SISBAR</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
</head>
<body class="grey darken-4 white-text">
    <div class="container center-align" style="padding-top:80px;">
        <h3>Ups, algo salió mal</h3>
        <p>Código: ${requestScope['jakarta.servlet.error.status_code']}</p>
        <p>La página que buscas no existe o ocurrió un error inesperado.</p>
        <a class="btn amber darken-2" href="${pageContext.request.contextPath}/">Volver al inicio</a>
    </div>
</body>
</html>
