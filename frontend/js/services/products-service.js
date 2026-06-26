let productService;

class ProductService {

    photos = [];

    filter = {
        cat: undefined,
        minPrice: undefined,
        maxPrice: undefined,
        subCategory: undefined,
        queryString: () => {
            let qs = "";
            if(this.filter.cat){ qs = `cat=${this.filter.cat}`; }
            if(this.filter.minPrice)
            {
                const minP = `minPrice=${this.filter.minPrice}`;
                if(qs.length>0) {   qs += `&${minP}`; }
                else { qs = minP; }
            }
            if(this.filter.maxPrice)
            {
                const maxP = `maxPrice=${this.filter.maxPrice}`;
                if(qs.length>0) {   qs += `&${maxP}`; }
                else { qs = maxP; }
            }
            if(this.filter.subCategory)
            {
                const sub = `subCategory=${this.filter.subCategory}`;
                if(qs.length>0) {   qs += `&${sub}`; }
                else { qs = sub; }
            }

            return qs.length > 0 ? `?${qs}` : "";
        }
    }

    constructor() {

        //load list of photos into memory

        axios.get("./images/products/photos.json")
            .then(response => {
                this.photos = response.data;
            });
    }

    hasPhoto(photo){
        return this.photos.filter(p => p == photo).length > 0;
    }

    addCategoryFilter(cat)
    {
        if(cat == 0) this.clearCategoryFilter();
        else this.filter.cat = cat;
    }
    addMinPriceFilter(price)
    {
        if(price == 0 || price == "") this.clearMinPriceFilter();
        else this.filter.minPrice = price;
    }
    addMaxPriceFilter(price)
    {
        if(price == 0 || price == "") this.clearMaxPriceFilter();
        else this.filter.maxPrice = price;
    }
    addSubcategoryFilter(subCategory)
    {
        if(subCategory == "") this.clearSubcategoryFilter();
        else this.filter.subCategory = subCategory;
    }

    clearCategoryFilter()
    {
        this.filter.cat = undefined;
    }
    clearMinPriceFilter()
    {
        this.filter.minPrice = undefined;
    }
    clearMaxPriceFilter()
    {
        this.filter.maxPrice = undefined;
    }
    clearSubcategoryFilter()
    {
        this.filter.subCategory = undefined;
    }

    search()
    {
        const url = `${config.baseUrl}/products${this.filter.queryString()}`;

        axios.get(url)
             .then(response => {
                 let data = {};
                 data.products = response.data;

                 data.products.forEach(product => {
                     product.isAdmin = userService.isAdmin();
                     product.displayImageUrl = product.imageUrl;

                     if(!this.hasPhoto(product.imageUrl))
                     {
                         product.displayImageUrl = "no-image.jpg";
                     }
                 })

                 templateBuilder.build('product', data, 'content', this.enableButtons);

             })
            .catch(error => {

                const data = {
                    error: "Searching products failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    enableButtons()
    {
        const buttons = [...document.querySelectorAll(".add-button")];

        if(userService.isLoggedIn())
        {
            buttons.forEach(button => {
                button.classList.remove("invisible")
            });
        }
        else
        {
            buttons.forEach(button => {
                button.classList.add("invisible")
            });
        }
    }

    showAddProductForm()
    {
        this.showProductForm();
    }

    showEditProductForm(productId)
    {
        const url = `${config.baseUrl}/products/${productId}`;

        axios.get(url)
            .then(response => {
                this.showProductForm(response.data);
            })
            .catch(error => {
                const data = {
                    error: "Loading product failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    showProductForm(product = {})
    {
        const modal = document.getElementById("login");
        modal.innerHTML = "";

        const isEdit = product.productId !== undefined;
        const form = document.createElement("div");
        form.classList.add("modal");
        form.innerHTML = `
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${isEdit ? "Edit Product" : "Add Product"}</h5>
                        <button type="button" class="btn-close" onclick="hideModalForm()" aria-label="Close"></button>
                    </div>
                    <div class="modal-body admin-form-grid">
                        <input type="hidden" id="product-id" value="${product.productId || ""}">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="product-name" placeholder="Name" value="${this.escapeAttribute(product.name)}">
                            <label for="product-name">Name</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="number" step="0.01" class="form-control" id="product-price" placeholder="Price" value="${product.price || ""}">
                            <label for="product-price">Price</label>
                        </div>
                        <div class="form-floating mb-3">
                            <select class="form-select" id="product-category"></select>
                            <label for="product-category">Category</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="product-subcategory" placeholder="Type" value="${this.escapeAttribute(product.subCategory)}">
                            <label for="product-subcategory">Type</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="number" class="form-control" id="product-stock" placeholder="Stock" value="${product.stock || 0}">
                            <label for="product-stock">Stock</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="product-image" placeholder="Image file" value="${this.escapeAttribute(product.imageUrl)}">
                            <label for="product-image">Image File</label>
                        </div>
                        <div class="form-check admin-featured-control">
                            <input class="form-check-input" type="checkbox" id="product-featured" ${product.featured ? "checked" : ""}>
                            <label class="form-check-label" for="product-featured">Featured</label>
                        </div>
                        <div class="form-floating mb-3 admin-form-full">
                            <textarea class="form-control" id="product-description" placeholder="Description">${this.escapeText(product.description)}</textarea>
                            <label for="product-description">Description</label>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" onclick="hideModalForm()">Cancel</button>
                        <button type="button" class="btn btn-primary" onclick="productService.saveProduct()">Save Product</button>
                    </div>
                </div>
            </div>
        `;

        modal.appendChild(form);
        this.loadProductCategoryOptions(product.categoryId);
    }

    loadProductCategoryOptions(selectedCategoryId)
    {
        const select = document.getElementById("product-category");

        if (!select) {
            return;
        }

        select.innerHTML = "";

        categoryService.getAllCategories(categories => {
            categories.forEach(category => {
                const option = document.createElement("option");
                option.value = category.categoryId;
                option.innerText = category.name;

                if (Number(selectedCategoryId) === Number(category.categoryId)) {
                    option.selected = true;
                }

                select.appendChild(option);
            })
        });
    }

    saveProduct()
    {
        const productId = document.getElementById("product-id").value;
        const product = {
            name: document.getElementById("product-name").value,
            price: Number(document.getElementById("product-price").value),
            categoryId: Number(document.getElementById("product-category").value),
            description: document.getElementById("product-description").value,
            subCategory: document.getElementById("product-subcategory").value,
            stock: Number(document.getElementById("product-stock").value),
            featured: document.getElementById("product-featured").checked,
            imageUrl: document.getElementById("product-image").value
        };

        const request = productId
            ? axios.put(`${config.baseUrl}/products/${productId}`, product)
            : axios.post(`${config.baseUrl}/products`, product);

        request
            .then(response => {
                hideModalForm();
                this.search();

                const data = {
                    message: productId ? "Product updated." : "Product added."
                };

                templateBuilder.append("message", data, "errors")
            })
            .catch(error => {
                const data = {
                    error: "Saving product failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    deleteProduct(productId)
    {
        if (!confirm("Delete this product?")) {
            return;
        }

        axios.delete(`${config.baseUrl}/products/${productId}`)
            .then(response => {
                this.search();

                const data = {
                    message: "Product deleted."
                };

                templateBuilder.append("message", data, "errors")
            })
            .catch(error => {
                const data = {
                    error: "Deleting product failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    escapeAttribute(value)
    {
        return String(value || "")
            .replace(/&/g, "&amp;")
            .replace(/"/g, "&quot;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }

    escapeText(value)
    {
        return String(value || "")
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }

}





document.addEventListener('DOMContentLoaded', () => {
    productService = new ProductService();

});
