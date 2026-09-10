document.addEventListener('DOMContentLoaded', () => {

      // ===== Seleccionar método de pago =====
      // (ahorita solo hay una tarjeta, pero esto ya queda listo
      //  para cuando agregues más métodos: tarjeta, transferencia, etc.)
        const metodosPago = document.querySelectorAll('.metodo-pago-card');

        metodosPago.forEach(card => {
            card.addEventListener('click', () => {
            metodosPago.forEach(c => c.classList.remove('seleccionado'));
            card.classList.add('seleccionado');
            });
        });

      // ===== Botón: Confirmar compra =====
        const btnConfirmar = document.getElementById('btnConfirmarCompra');
        btnConfirmar.addEventListener('click', () => {
        // Aquí, en un proyecto real, iría el fetch() que manda
        // el pedido al backend para guardarlo en la base de datos.
        alert('Compra confirmada. En un proyecto real, aquí se enviaría el pedido al servidor.');
        });

      // ===== Botón: Volver al carrito =====
        const btnVolver = document.getElementById('btnVolverCarrito');   // <- esta línea faltaba
        btnVolver.addEventListener('click', () => {
        window.location.href = 'productos.html';
        });

    });