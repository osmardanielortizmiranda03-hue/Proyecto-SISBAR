<%-- Página de inicio de SISBAR (diseño original de Pagina de Inicio/index.html) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SISBAR - Sistema de Barbería</title>

    <!-- Materialize Icons & CSS -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Alegreya+Sans:ital,wght@0,100..900;1,100..900&family=Barlow+Condensed:wght@200&family=Karla:ital,wght@0,200..800;1,200..800&family=Oswald:wght@200&family=Playfair+Display+SC:ital,wght@0,400..900;1,400..900&family=Space+Grotesk:wght@300&display=swap" rel="stylesheet">
    
    <!-- CSS Personalizado -->
    <link rel="stylesheet" href="${ctx}/CSS/pagina%20de%20inicio/styles.css">
</head>
<body>

    <!-- ================= NAVBAR (STICKY) ================= -->
    <header>
        <nav class="nav-modification">
            <div class="nav-wrapper">
                <!-- Logo -->
                <a href="#" class="brand-logo">
                    <img src="${ctx}/IMAGES/logo-sisbar.png" alt="Logo SISBAR">
                </a>

                <!-- Hamburguesa Móvil -->
                <a href="#" class="sidenav-trigger" data-target="mobile-demo">
                    <i class="material-icons">menu</i>
                </a>

                <!-- Menú Central (Escritorio) -->
                <ul class="nav-center hide-on-med-and-down">
                    <li><a href="#inicio" class="modification-a">INICIO</a></li>
                    <li><a href="#servicios" class="modification-a">SERVICIOS</a></li>
                    <li><a href="#nosotros" class="modification-a">NOSOTROS</a></li>
                    <li><a href="#ubicacion" class="modification-a">UBICACIÓN</a></li>
                    <li><a href="#contacto" class="modification-a">CONTACTO</a></li>
                </ul>

                <!-- Botones de Acción (Escritorio) -->
                <div class="right hide-on-med-and-down">
                    <%-- Si el usuario ya inició sesión, se muestran otros botones --%>
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuario}">
                            <a href="${ctx}/${sessionScope.usuario.rol == 'ADMIN' ? 'admin/servicios' : 'panel'}" class="btn modification-button">MI PANEL</a>
                            <a href="${ctx}/logout" class="btn modification-button">CERRAR SESIÓN</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${ctx}/login" class="btn modification-button">INICIAR SESIÓN</a>
                            <a href="${ctx}/registro" class="btn modification-button">REGISTRARSE</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </nav>

        <!-- SIDENAV (Menú Desplegable Móvil) -->
        <ul class="sidenav" id="mobile-demo">
            <li><a href="#inicio" class="modification-a">INICIO</a></li>
            <li><a href="#servicios" class="modification-a">SERVICIOS</a></li>
            <li><a href="#nosotros" class="modification-a">NOSOTROS</a></li>
            <li><a href="#ubicacion" class="modification-a">UBICACIÓN</a></li>
            <li><a href="#contacto" class="modification-a">CONTACTO</a></li>
            <li><a href="${ctx}/login" class="modification-a-button">INICIAR SESIÓN</a></li>
            <li><a href="${ctx}/registro" class="modification-a-button-especial">REGISTRARSE</a></li>
        </ul>
    </header>

    <!-- ================= CONTENIDO PRINCIPAL ================= -->
    <main>
        <!-- HERO SECTION -->
        <section class="hero" id="inicio">
            <div class="card-image">
                <img src="${ctx}/IMAGES/fotoSISIBAR.jpg" alt="Barbería Interno">
                
                <div class="contenido-principal">
                    <div class="title-secound">
                        <h2>SISTEMA DE BARBERÍA "ESTILO Y CUIDADO"</h2>
                    </div>

                    <div class="title-principal">
                        <h1>TU ESTILO <span>COMIENZA AQUÍ</span></h1>
                    </div>

                    <div class="separator">
                        <span></span>
                        <i class="material-icons">diamond</i>
                        <span></span>
                    </div>

                    <div class="container-text">
                        <p>Bienvenido a <strong>SISBAR</strong>, la aplicación oficial de <strong>Estilo y Cuidado</strong>. Agenda tu cita de forma rápida y vive una experiencia premium.</p>
                    </div>

                    <div class="container-button">
                        <a href="${ctx}/registro" class="btn-large modification-a">
                            <i class="material-icons left">schedule</i> RESERVAR AHORA
                        </a>
                        <a href="#servicios" class="btn-large modification-a">
                            CONOCER SERVICIOS
                        </a>
                    </div>
                </div>
            </div>
        </section>
        
        <!-- SECCIÓN DE FONDO CON ESTRELLAS Y CONTENIDOS -->
        <div class="stars-background" id="servicios">
            <div id="stars"></div>
            <div id="stars2"></div>
            <div id="stars3"></div>
            
            <!-- SECCIÓN 2: NUESTROS SERVICIOS -->
            <section class="section-servicios">
                <div class="servicios">
                    <h2 class="center">Lo que ofrecemos</h2>
                    <h2 class="titulo-secound center">NUESTROS SERVICIOS</h2>

                    <div class="separator">
                        <span class="separador-02-span"></span>
                        <i class="material-icons">diamond</i>
                        <span class="separador-02-span"></span>
                    </div>

                    <!-- Cajas de Servicios -->
                    <div class="cajas-servicios"> 
                        <!-- Caja 01 -->
                        <div class="caja">
                            <div class="icono">
                                <i class="material-icons">content_cut</i>
                            </div>
                            <div class="titulo">
                                <h3>Corte Tradicional</h3>
                            </div>
                            <div class="parrafo">
                                <p>Técnica clásica con tijera y navaja para un acabado impecable y personalizado</p>
                            </div>
                            <div class="precio-duracion">
                                <div class="tiempo">
                                    <span>45m</span>
                                </div>
                                <div class="precio">
                                    <span>$ 20.000</span>
                                </div>
                            </div>
                        </div>

                        <!-- Caja 02 -->
                        <div class="caja">
                            <div class="icono">
                                <i class="material-icons">face</i>
                            </div>
                            <div class="titulo">
                                <h3>Afeitado Clásico</h3>
                            </div>
                            <div class="parrafo">
                                <p>Experiencia de afeitado con toallas calientes y productos premium</p>
                            </div>
                            <div class="precio-duracion">
                                <div class="tiempo">
                                    <span>30m</span>
                                </div>
                                <div class="precio">
                                    <span>$ 15.000</span>
                                </div>
                            </div>
                        </div>

                        <!-- Caja 03 -->
                        <div class="caja"> 
                            <div class="icono">
                                <i class="material-icons">spa</i>
                            </div>
                            <div class="titulo">
                                <h3 class="modificar-h3">Tratamiento Capilar</h3>
                            </div>
                            <div class="parrafo">
                                <p>Nutrición profunda del cabello con productos especializados</p>
                            </div>
                            <div class="precio-duracion">
                                <div class="tiempo">
                                    <span>60m</span>
                                </div>
                                <div class="precio">
                                    <span>$ 30.000</span>
                                </div>
                            </div>
                        </div>

                        <!-- Caja 04 -->
                        <div class="caja"> 
                            <div class="icono">
                                <i class="material-icons">face_retouching_natural</i>
                            </div>
                            <div class="titulo">
                                <h3>Perfilado de Barba</h3>
                            </div>
                            <div class="parrafo">
                                <p>Definición y estilizado de la barra para un look moderno y cuidado</p>
                            </div>
                            <div class="precio-duracion">
                                <div class="tiempo">
                                    <span>20m</span>
                                </div>
                                <div class="precio">
                                    <span>$ 10.000</span>
                                </div>
                            </div>
                        </div>

                        <!-- Caja 05 -->
                        <div class="caja"> 
                            <div class="icono">
                                <i class="material-icons">color_lens</i>
                            </div>
                            <div class="titulo">
                                <h3>Coloración</h3>
                            </div>
                            <div class="parrafo">
                                <p>Cambio de color o retoque de raíces con productos de alta calidad</p>
                            </div>
                            <div class="precio-duracion">
                                <div class="tiempo">
                                    <span>90m</span>
                                </div>
                                <div class="precio">
                                    <span>$ 50.000</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- ================= SECCIÓN 3: NOSOTROS ================= -->
            <section class="section-nosotros" id="nosotros">
                <div class="nosotros">
                    <div class="imagen-grande">
                        <img src="${ctx}/IMAGES/persona afeitandose.jpg" alt="Barbero afeitando a un cliente">
                    </div>

                    <div class="contenido-nosostros">
                        <div class="title-nosotros">
                            <h3>QUIENES SOMOS</h3>
                            <h2>Barberia <span>ESTILO Y CUIDADO</span></h2>
                        </div>
                        <div class="separator separador-modificador">
                            <span class="separador-02-span modificardor-linea-span"></span>
                            <i class="material-icons">diamond</i>
                            <span class="separador-02-span modificardor-linea-span mas-largo-span"></span>
                        </div>
                        
                        <div class="descripcion-p">
                            <p>Somos una barbería comprometida con la excelencia en el cuidado personal masculino. Nuestro equipo de profesionales está dedicado a ofrecer servicios de alta calidad, combinando técnicas tradicionales con las últimas tendencias en estilo y cuidado del cabello y la barba.</p>
                        </div>

                        <div class="contenedor-tarjetas">
                            <div class="tarjetas">
                                <div>
                                    <h4>Nuestra Mision</h4>
                                </div>
                                <div>
                                    <p>Elevar los estándares de servicio y calidad en el cuidado personal masculino, ofreciendo una experiencia moderna y profesional.</p>
                                </div>
                            </div>

                            <div class="tarjetas">
                                <div>
                                    <h4>Nuestra Vision</h4>
                                </div>
                                <div>
                                    <p>Ser la barbería de referencia en el cuidado personal masculino, reconocida por su excelencia y servicio al cliente.</p>
                                </div>
                            </div>
                        </div>

                        <div class="contenedor-footer-tarjeta">
                            <div class="bloque">
                                <h5>8<span>+</span></h5>
                                <h6>AÑOS DE EXPERIENCIA</h6>
                            </div>
                            <div class="bloque">
                                <h5>2400<span>+</span></h5>
                                <h6>CLIENTES</h6>
                            </div>
                            <div class="bloque">
                                <h5>15<span>+</span></h5>
                                <h6>BARBEROS</h6>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- ================= SECCIÓN 4: UBICACION ================= -->
            <section class="section-ubicacion" id="ubicacion">
                <div class="titulos-ubicacion">
                    <h3>ENCUENTRANOS</h3>
                    <h2>NUESTRA UBICACIÓN</h2>

                    <div class="separator separador-modificador">
                        <span class="separador-02-span modificardor-linea-span"></span>
                        <i class="material-icons">diamond</i>
                        <span class="separador-02-span modificardor-linea-span mas-largo-span"></span>
                    </div>
                </div>

                <div class="ubicacion">
                    <div class="mapa">
                        <iframe
                            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d63092.812180435525!2d-75.90926540350351!3d8.7577468938834!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x8e5a2fe5a57031ad%3A0x92e1cbad2ed7c0a0!2zTW9udGVyw61hLCBDw7NyZG9iYQ!5e0!3m2!1ses-419!2sco!4v1783972699617!5m2!1ses-419!2sco"
                            loading="lazy"
                            allowfullscreen
                            referrerpolicy="strict-origin-when-cross-origin"
                            title="Mapa de ubicación de la barbería">
                        </iframe>
                    </div>
                    
                    <div class="informacion-contacto">
                        <!-- Dirección -->
                        <div class="tarjeta-info">
                            <div class="icono-titulo">
                                <i class="material-icons">location_on</i>
                                <h5>DIRECCIÓN</h5>
                            </div>
                            <div class="contenido">
                                <p>
                                    Av. Reforma 125, Col. Centro <br>
                                    Ciudad de México, CDMX
                                </p>
                            </div>
                        </div>

                        <!-- Teléfono -->
                        <div class="tarjeta-info">
                            <div class="icono-titulo">
                                <i class="material-icons">phone</i>
                                <h5>TELÉFONO</h5>
                            </div>
                            <div class="contenido">
                                <p>+52 55 1234 5678</p>
                            </div>
                        </div>

                        <!-- Correo -->
                        <div class="tarjeta-info">
                            <div class="icono-titulo">
                                <i class="material-icons">email</i>
                                <h5>CORREO</h5>
                            </div>
                            <div class="contenido">
                                <p>contacto@sisbar.mx</p>
                            </div>
                        </div>

                        <!-- Horarios -->
                        <div class="tarjeta-info">
                            <div class="icono-titulo">
                                <i class="material-icons">schedule</i>
                                <h5>HORARIOS</h5>
                            </div>
                            <div class="contenido">
                                <div class="horario">
                                    <span>Lun - Vie</span>
                                    <span>9:00 - 20:00</span>
                                </div>
                                <div class="horario">
                                    <span>Sábado</span>
                                    <span>8:00 - 21:00</span>
                                </div>
                                <div class="horario">
                                    <span>Domingo</span>
                                    <span>10:00 - 17:00</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- ==================== SECCIÓN 5: CONTACTO ==================== -->
            <section class="section-contacto" id="contacto">
                <div class="titulo-contacto">
                    <h3>ESCRÍBENOS</h3>
                    <h2>Contáctanos</h2>

                    <div class="separator">
                        <span class="separador-02-span"></span>
                        <i class="material-icons">diamond</i>
                        <span class="separador-02-span"></span>
                    </div>
                </div>

                <form class="formulario-contacto">
                    <div class="fila">
                        <div class="grupo-input">
                            <label for="form-nombre">Nombre</label>
                            <input type="text" id="form-nombre" placeholder="Juan García" required>
                        </div>

                        <div class="grupo-input">
                            <label for="form-email">Correo Electrónico</label>
                            <input type="email" id="form-email" placeholder="juan@ejemplo.com" required>
                        </div>
                    </div>

                    <div class="grupo-input">
                        <label for="form-tel">Teléfono</label>
                        <input type="tel" id="form-tel" placeholder="+57 300 000 0000">
                    </div>

                    <div class="grupo-input">
                        <label for="form-mensaje">Mensaje</label>
                        <textarea id="form-mensaje" placeholder="¿En qué podemos ayudarte?" required></textarea>
                    </div>

                    <div class="contenedor-boton">
                        <button type="submit">
                            <i class="material-icons">send</i> Enviar Mensaje
                        </button>
                    </div>
                </form>
            </section>
        </div>
    </main>

    <!-- ================= FOOTER ================= -->
    
    <div class="stars-background">
            <div id="stars"></div>
            <div id="stars2"></div>
            <div id="stars3"></div>

            <footer class="page-footer">
                <div class="container">
                    <div class="row">
                        <!-- Info y Redes -->
                        <div class="col l3 m6 s12">
                            <div class="footer-logo">
                                <img src="${ctx}/IMAGES/logo-sisbar.png" alt="Logo Footer">
                                <h5>SISBAR</h5>
                            </div>
                            <p class="footer-description">El sistema definitivo para barberías que buscan la excelencia y un control impecable.</p>
                            <div class="footer-social">
                                <a href="#"><img src="${ctx}/IMAGES/LogoInstagram.png" alt="Síguenos en Instagram"></a>
                                <a href="#"><img src="${ctx}/IMAGES/LogoFacebook.png" alt="Síguenos en Facebook"></a>
                                <a href="#"><img src="${ctx}/IMAGES/LogoWhatsapp.png" alt="Escríbenos por WhatsApp"></a>
                            </div>
                        </div>

                        <!-- Enlaces Rápidos -->
                        <div class="col l3 m6 s12">
                            <h5>Enlaces</h5>
                            <ul class="footer-links">
                                <li><a href="#inicio">Inicio</a></li>
                                <li><a href="#servicios">Servicios</a></li>
                                <li><a href="#nosotros">Nosotros</a></li>
                                <li><a href="#ubicacion">Ubicación</a></li>
                                <li><a href="#contacto">Contacto</a></li>
                            </ul>
                        </div>

                        <!-- Info de Contacto -->
                        <div class="col l3 m6 s12">
                            <h5>Contacto</h5>
                            <ul class="footer-contact">
                                <li>📞 +57 300 000 0000</li>
                                <li>✉ contacto@sisbar.com</li>
                                <li>📍 Cartagena, Colombia</li>
                            </ul>
                        </div>

                        <!-- Horarios de Atención -->
                        <div class="col l3 m6 s12">
                            <h5>Horarios</h5>
                            <ul class="footer-hours">
                                <li>Lun - Vie <span>8:00 AM - 8:00 PM</span></li>
                                <li>Sábado <span>8:00 AM - 9:00 PM</span></li>
                                <li>Domingo <span>10:00 AM - 5:00 PM</span></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="footer-copyright">
                    <div class="container">
                        © 2026 SISBAR | Todos los derechos reservados.
                    </div>
                </div>
            </footer>

    </div>
    

    <!-- JavaScript de Materialize -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", () => {
            M.AutoInit();
        });
    </script>
</body>
</html>
