<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- Formato de números en pesos colombianos (25.000) --%>
<fmt:setLocale value="es_CO"/>
<%-- Acción estándar jsp:useBean: crea un objeto Date disponible en la página --%>
<jsp:useBean id="hoy" class="java.util.Date" scope="page"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Panel - SISBAR</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="${ctx}/CSS/panel%20administrador/servicios.css">
    <link rel="stylesheet" href="${ctx}/CSS/mensajes.css">
</head>
<body>
    <div class="stars-background">
        <div id="stars"></div>
        <div id="stars2"></div>
        <div id="stars3"></div>
        <main>
            <section class="header-section">
                <div class="header-title">
                    <h3><fmt:formatDate value="${hoy}" pattern="dd/MM/yyyy"/> · Portal del Cliente</h3>
                    <h1>Hola, <c:out value="${sessionScope.usuario.nombres}"/></h1>
                </div>
                <a class="btn-nuevo" href="${ctx}/logout">
                    <i class="material-icons">logout</i> CERRAR SESIÓN
                </a>
            </section>

            <%@ include file="/WEB-INF/jsp/fragmentos/mensajes.jspf" %>

            <h5 style="color:#d4af37;margin:16px 0;">Nuestros servicios</h5>
            <section class="services-section">
                <c:forEach var="s" items="${servicios}">
                    <div class="service-card">
                        <div class="service-top">
                            <span class="service-name"><c:out value="${s.nombreServicio}"/></span>
                        </div>
                        <div class="service-footer">
                            <span class="service-duration">${s.duracionMinutos} min</span>
                            <span class="service-price">
                                <fmt:formatNumber value="${s.precioServicio}" type="number" maxFractionDigits="0"/> COP
                            </span>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty servicios}">
                    <p class="vacio">Pronto publicaremos nuestros servicios.</p>
                </c:if>
            </section>
        </main>
    </div>
    <jsp:include page="/WEB-INF/jsp/fragmentos/pie.jsp"/>
</body>
</html>
