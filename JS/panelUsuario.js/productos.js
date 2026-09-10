document.addEventListener("DOMContentLoaded", () => {
    const storageKey = "sisbar-carrito";
    const productCards = [...document.querySelectorAll(".product-card")];
    const cartPanel = document.getElementById("cart-panel");
    const cartBackdrop = document.getElementById("cart-backdrop");
    const cartItems = document.getElementById("cart-items");
    const cartTotal = document.getElementById("cart-total");
    const checkoutButton = document.getElementById("checkout-button");
    const counters = [
        document.getElementById("cart-count-header"),
        document.getElementById("cart-count-main")
    ];

    let cart = loadCart();

    function loadCart() {
        try {
            const savedCart = JSON.parse(localStorage.getItem(storageKey));
            return Array.isArray(savedCart) ? savedCart : [];
        } catch (error) {
            return [];
        }
    }

    function saveCart() {
        localStorage.setItem(storageKey, JSON.stringify(cart));
    }

    function formatPrice(price) {
        return `$${price.toLocaleString("es-CO")}`;
    }

    function getProductFromCard(card, index) {
        const priceText = card.querySelector(".product-price").textContent;

        return {
            id: card.dataset.productId || `producto-${index + 1}`,
            name: card.querySelector(".product-name").textContent.trim(),
            category: card.querySelector(".product-category").textContent.trim(),
            price: Number(priceText.replace(/[^0-9]/g, ""))
        };
    }

    function updateCounters() {
        const quantity = cart.reduce((total, product) => total + product.quantity, 0);

        counters.forEach((counter) => {
            counter.textContent = quantity;
        });
    }

    function updateProductCards() {
        productCards.forEach((card, index) => {
            const product = getProductFromCard(card, index);
            const itemInCart = cart.find((item) => item.id === product.id);

            card.classList.toggle("in-cart", Boolean(itemInCart));
            card.dataset.cartLabel = itemInCart
                ? `${itemInCart.quantity} en carrito`
                : "";
        });
    }

    function createCartItem(item) {
        const article = document.createElement("article");
        article.className = "cart-item";

        const titleRow = document.createElement("div");
        titleRow.className = "cart-item-title-row";

        const name = document.createElement("span");
        name.className = "cart-item-name";
        name.textContent = item.name;

        const subtotal = document.createElement("strong");
        subtotal.className = "cart-item-subtotal";
        subtotal.textContent = formatPrice(item.price * item.quantity);

        const unitPrice = document.createElement("span");
        unitPrice.className = "cart-item-price";
        unitPrice.textContent = `${formatPrice(item.price)} por unidad`;

        const controls = document.createElement("div");
        controls.className = "cart-item-controls";

        const quantityControl = document.createElement("div");
        quantityControl.className = "cart-item-quantity";

        const decrease = document.createElement("button");
        decrease.type = "button";
        decrease.textContent = "−";
        decrease.dataset.action = "decrease";
        decrease.dataset.id = item.id;
        decrease.setAttribute("aria-label", `Disminuir cantidad de ${item.name}`);

        const quantity = document.createElement("span");
        quantity.textContent = item.quantity;

        const increase = document.createElement("button");
        increase.type = "button";
        increase.textContent = "+";
        increase.dataset.action = "increase";
        increase.dataset.id = item.id;
        increase.setAttribute("aria-label", `Aumentar cantidad de ${item.name}`);

        const remove = document.createElement("button");
        remove.type = "button";
        remove.className = "cart-item-remove";
        remove.textContent = "Eliminar";
        remove.dataset.action = "remove";
        remove.dataset.id = item.id;

        titleRow.append(name, subtotal);
        quantityControl.append(decrease, quantity, increase);
        controls.append(quantityControl, remove);
        article.append(titleRow, unitPrice, controls);

        return article;
    }

    function renderCart() {
        cartItems.replaceChildren();

        if (cart.length === 0) {
            const emptyMessage = document.createElement("p");
            emptyMessage.className = "empty-cart";
            emptyMessage.textContent = "Aún no has agregado productos al carrito.";
            cartItems.append(emptyMessage);
        } else {
            cart.forEach((item) => cartItems.append(createCartItem(item)));
        }

        const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
        cartTotal.textContent = formatPrice(total);
        checkoutButton.disabled = cart.length === 0;

        updateCounters();
        updateProductCards();
        saveCart();
    }

    function addProduct(product) {
        const itemInCart = cart.find((item) => item.id === product.id);

        if (itemInCart) {
            itemInCart.quantity += 1;
        } else {
            cart.push({ ...product, quantity: 1 });
        }

        renderCart();
    }

    function changeQuantity(id, change) {
        const item = cart.find((product) => product.id === id);
        if (!item) return;

        item.quantity += change;
        if (item.quantity <= 0) {
            cart = cart.filter((product) => product.id !== id);
        }

        renderCart();
    }

    function removeProduct(id) {
        cart = cart.filter((product) => product.id !== id);
        renderCart();
    }

    function openCart() {
        cartBackdrop.hidden = false;
        cartPanel.setAttribute("aria-hidden", "false");
        requestAnimationFrame(() => cartPanel.classList.add("is-open"));
    }

    function closeCart() {
        cartPanel.classList.remove("is-open");
        cartPanel.setAttribute("aria-hidden", "true");
        window.setTimeout(() => {
            if (!cartPanel.classList.contains("is-open")) {
                cartBackdrop.hidden = true;
            }
        }, 300);
    }

    productCards.forEach((card, index) => {
        const button = card.querySelector(".add-to-cart-btn");
        button.type = "button";
        button.addEventListener("click", () => {
            addProduct(getProductFromCard(card, index));
            openCart();
        });
    });

    document.getElementById("open-cart-header").addEventListener("click", openCart);
    document.getElementById("open-cart-main").addEventListener("click", openCart);
    document.getElementById("close-cart").addEventListener("click", closeCart);
    cartBackdrop.addEventListener("click", closeCart);
    checkoutButton.addEventListener("click", () => {
        window.location.href = "/panel usuario/confirmar-compra.html";
    });

    cartItems.addEventListener("click", (event) => {
        const button = event.target.closest("button[data-action]");
        if (!button) return;

        const { action, id } = button.dataset;
        if (action === "increase") changeQuantity(id, 1);
        if (action === "decrease") changeQuantity(id, -1);
        if (action === "remove") removeProduct(id);
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && cartPanel.classList.contains("is-open")) {
            closeCart();
        }
    });

    renderCart();
});

