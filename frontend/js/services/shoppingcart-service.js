let cartService;

class ShoppingCartService {

    cart = {
        items:[],
        total:0
    };

    addToCart(productId)
    {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        // const headers = userService.getHeaders();

        return axios.post(url, {})// ,{headers})
            .then(response => {
                this.setCart(response.data)

                this.updateCartDisplay()

            })
            .catch(error => {

                const data = {
                    error: "Add to cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    getProductQuantity(productId)
    {
        const quantityControl = document.getElementById(`quantity-${productId}`);
        const quantity = Number(quantityControl?.value);

        if (!Number.isInteger(quantity) || quantity < 1) {
            return 1;
        }

        return Math.min(quantity, 99);
    }

    setProductQuantity(productId, quantity)
    {
        const quantityControl = document.getElementById(`quantity-${productId}`);

        if (!quantityControl) {
            return;
        }

        quantityControl.value = Math.min(Math.max(quantity, 1), 99);
    }

    increaseProductQuantity(productId)
    {
        const quantity = this.getProductQuantity(productId);
        this.setProductQuantity(productId, quantity + 1);
    }

    decreaseProductQuantity(productId)
    {
        const quantity = this.getProductQuantity(productId);
        this.setProductQuantity(productId, quantity - 1);
    }

    addQuantityToCart(productId)
    {
        const quantity = this.getProductQuantity(productId);
        this.setProductQuantity(productId, quantity);

        let request = Promise.resolve();

        for (let count = 0; count < quantity; count++) {
            request = request.then(() => this.addToCart(productId));
        }

        request.then(() => this.setProductQuantity(productId, 1));
    }

    setCart(data)
    {
        this.cart = {
            items: [],
            total: 0
        }

        this.cart.total = data.total;

        for (const [key, value] of Object.entries(data.items)) {
            this.cart.items.push(value);
        }
    }

    loadCart()
    {

        const url = `${config.baseUrl}/cart`;

        axios.get(url)
            .then(response => {
                this.setCart(response.data)

                this.updateCartDisplay()

            })
            .catch(error => {

                const data = {
                    error: "Load cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })

    }

    loadCartPage()
    {
        // templateBuilder.build("cart", this.cart, "main");

        const main = document.getElementById("main")
        main.innerHTML = "";

        let div = document.createElement("div");
        div.classList="filter-box";
        main.appendChild(div);

        const contentDiv = document.createElement("div")
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");
        contentDiv.classList.add("checkout-panel");

        const cartHeader = document.createElement("div")
        cartHeader.classList.add("cart-header")

        const titleDiv = document.createElement("div");

        const h1 = document.createElement("h1")
        h1.innerText = "Checkout";
        titleDiv.appendChild(h1);

        const subtitle = document.createElement("p");
        subtitle.innerText = "Review your cart before placing the order.";
        titleDiv.appendChild(subtitle);

        cartHeader.appendChild(titleDiv);

        const headerActions = document.createElement("div");
        headerActions.classList.add("cart-actions");

        const continueButton = document.createElement("button");
        continueButton.classList.add("btn")
        continueButton.classList.add("btn-outline-primary")
        continueButton.innerHTML = `<i class="fa-solid fa-arrow-left"></i> Continue Shopping`;
        continueButton.addEventListener("click", () => loadHome());
        headerActions.appendChild(continueButton);

        const button = document.createElement("button");
        button.classList.add("btn")
        button.classList.add("btn-outline-danger")
        button.innerText = "Clear";
        button.addEventListener("click", () => this.clearCart());
        button.disabled = this.cart.items.length === 0;
        headerActions.appendChild(button)

        cartHeader.appendChild(headerActions)

        contentDiv.appendChild(cartHeader)
        main.appendChild(contentDiv);

        if (this.cart.items.length === 0) {
            this.buildEmptyCart(contentDiv);
            return;
        }

        const checkoutBody = document.createElement("div");
        checkoutBody.classList.add("checkout-body");

        const itemList = document.createElement("div");
        itemList.classList.add("cart-item-list");

        this.cart.items.forEach(item => {
            this.buildItem(item, itemList)
        });

        checkoutBody.appendChild(itemList);
        checkoutBody.appendChild(this.buildSummary());
        contentDiv.appendChild(checkoutBody);
    }

    buildItem(item, parent)
    {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");

        let img = document.createElement("img");
        img.classList.add("cart-item-image");
        img.src = `./images/products/${item.product.imageUrl}`
        img.alt = item.product.name;
        img.addEventListener("click", () => {
            showImageDetailForm(item.product.name, img.src)
        })
        outerDiv.appendChild(img);

        let detailsDiv = document.createElement("div");
        detailsDiv.classList.add("cart-item-details");

        let h4 = document.createElement("h4")
        h4.innerText = item.product.name;
        detailsDiv.appendChild(h4);

        let descriptionDiv = document.createElement("div");
        descriptionDiv.classList.add("cart-item-description");
        descriptionDiv.innerText = item.product.description;
        detailsDiv.appendChild(descriptionDiv);

        const itemMeta = document.createElement("div");
        itemMeta.classList.add("cart-item-meta");

        let quantityDiv = document.createElement("span")
        quantityDiv.innerText = `Qty ${item.quantity}`;
        itemMeta.appendChild(quantityDiv)

        let priceDiv = document.createElement("span");
        priceDiv.innerText = `${this.formatCurrency(item.product.price)} each`;
        itemMeta.appendChild(priceDiv);

        detailsDiv.appendChild(itemMeta);
        outerDiv.appendChild(detailsDiv);

        const itemTotal = document.createElement("div");
        itemTotal.classList.add("cart-item-total");
        itemTotal.innerText = this.formatCurrency(item.product.price * item.quantity);
        outerDiv.appendChild(itemTotal);


        parent.appendChild(outerDiv);
    }

    buildSummary()
    {
        const summary = document.createElement("aside");
        summary.classList.add("checkout-summary");

        const h2 = document.createElement("h2");
        h2.innerText = "Order Summary";
        summary.appendChild(h2);

        const itemCount = this.getItemCount();

        const itemRow = document.createElement("div");
        itemRow.classList.add("summary-row");
        itemRow.innerHTML = `<span>Items</span><strong>${itemCount}</strong>`;
        summary.appendChild(itemRow);

        const shippingRow = document.createElement("div");
        shippingRow.classList.add("summary-row");
        shippingRow.innerHTML = `<span>Shipping</span><strong>$0.00</strong>`;
        summary.appendChild(shippingRow);

        const totalRow = document.createElement("div");
        totalRow.classList.add("summary-row");
        totalRow.classList.add("summary-total");
        totalRow.innerHTML = `<span>Total</span><strong>${this.formatCurrency(this.cart.total)}</strong>`;
        summary.appendChild(totalRow);

        const checkoutButton = document.createElement("button");
        checkoutButton.classList.add("btn");
        checkoutButton.classList.add("btn-success");
        checkoutButton.classList.add("checkout-button");
        checkoutButton.innerHTML = `<i class="fa-solid fa-lock"></i> Place Order`;
        checkoutButton.addEventListener("click", () => this.checkout());
        summary.appendChild(checkoutButton);

        return summary;
    }

    buildEmptyCart(parent)
    {
        const emptyDiv = document.createElement("div");
        emptyDiv.classList.add("empty-cart");

        const icon = document.createElement("i");
        icon.classList.add("fa-solid");
        icon.classList.add("fa-basket-shopping");
        emptyDiv.appendChild(icon);

        const h2 = document.createElement("h2");
        h2.innerText = "Your cart is empty";
        emptyDiv.appendChild(h2);

        const p = document.createElement("p");
        p.innerText = "Add a few products and they will appear here.";
        emptyDiv.appendChild(p);

        const button = document.createElement("button");
        button.classList.add("btn");
        button.classList.add("btn-success");
        button.innerText = "Shop Products";
        button.addEventListener("click", () => loadHome());
        emptyDiv.appendChild(button);

        parent.appendChild(emptyDiv);
    }

    checkout()
    {
        if (this.cart.items.length === 0) {
            return;
        }

        const url = `${config.baseUrl}/orders`;

        axios.post(url, {})
             .then(response => {
                 this.cart = {
                     items: [],
                     total: 0
                 }

                 this.updateCartDisplay()
                 this.loadCartPage()

                 const data = {
                     message: "Order placed successfully."
                 };

                 templateBuilder.append("message", data, "errors")
             })
             .catch(error => {

                 const data = {
                     error: "Checkout failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }

    clearCart()
    {

        const url = `${config.baseUrl}/cart`;

        axios.delete(url)
             .then(response => {
                 this.cart = {
                     items: [],
                     total: 0
                 }

                 this.cart.total = response.data.total;

                 for (const [key, value] of Object.entries(response.data.items)) {
                     this.cart.items.push(value);
                 }

                 this.updateCartDisplay()
                 this.loadCartPage()

             })
             .catch(error => {

                 const data = {
                     error: "Empty cart failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }

    updateCartDisplay()
    {
        try {
            const itemCount = this.getItemCount();
            const cartControl = document.getElementById("cart-items")

            cartControl.innerText = itemCount;
        }
        catch (e) {

        }
    }

    getItemCount()
    {
        return this.cart.items.reduce((total, item) => total + (Number(item.quantity) || 0), 0);
    }

    formatCurrency(value)
    {
        const amount = Number(value) || 0;

        return amount.toLocaleString("en-US", {
            style: "currency",
            currency: "USD"
        });
    }
}





document.addEventListener('DOMContentLoaded', () => {
    cartService = new ShoppingCartService();

    if(userService.isLoggedIn())
    {
        cartService.loadCart();
    }

});
