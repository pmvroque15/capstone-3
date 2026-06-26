let categoryService;

class CategoryService {

    categories = [];

    getAllCategories(callback)
    {
        const url = `${config.baseUrl}/categories`;

        return axios.get(url)
            .then(response => {
                this.categories = response.data;
                callback(response.data);
            })
            .catch(error => {

                const data = {
                    error: "Loading categories failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    getCategory(categoryId)
    {
        return this.categories.find(category => Number(category.categoryId) === Number(categoryId));
    }

    showAddCategoryForm()
    {
        this.showCategoryForm();
    }

    showEditCategoryForm(categoryId)
    {
        const category = this.getCategory(categoryId);

        if (category) {
            this.showCategoryForm(category);
            return;
        }

        axios.get(`${config.baseUrl}/categories/${categoryId}`)
            .then(response => {
                this.showCategoryForm(response.data);
            })
            .catch(error => {
                const data = {
                    error: "Loading category failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    showCategoryForm(category = {})
    {
        const modal = document.getElementById("login");
        modal.innerHTML = "";

        const isEdit = category.categoryId !== undefined;
        const form = document.createElement("div");
        form.classList.add("modal");
        form.innerHTML = `
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${isEdit ? "Edit Category" : "Add Category"}</h5>
                        <button type="button" class="btn-close" onclick="hideModalForm()" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="category-id" value="${category.categoryId || ""}">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="category-name" placeholder="Name" value="${this.escapeAttribute(category.name)}">
                            <label for="category-name">Name</label>
                        </div>
                        <div class="form-floating mb-3">
                            <textarea class="form-control" id="category-description" placeholder="Description">${this.escapeText(category.description)}</textarea>
                            <label for="category-description">Description</label>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" onclick="hideModalForm()">Cancel</button>
                        <button type="button" class="btn btn-primary" onclick="categoryService.saveCategory()">Save Category</button>
                    </div>
                </div>
            </div>
        `;

        modal.appendChild(form);
    }

    saveCategory()
    {
        const categoryId = document.getElementById("category-id").value;
        const category = {
            name: document.getElementById("category-name").value,
            description: document.getElementById("category-description").value
        };

        const request = categoryId
            ? axios.put(`${config.baseUrl}/categories/${categoryId}`, category)
            : axios.post(`${config.baseUrl}/categories`, category);

        request
            .then(response => {
                hideModalForm();
                this.refreshCategoryControls();

                const data = {
                    message: categoryId ? "Category updated." : "Category added."
                };

                templateBuilder.append("message", data, "errors")
            })
            .catch(error => {
                const data = {
                    error: "Saving category failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    deleteCategory(categoryId)
    {
        if (!confirm("Delete this category?")) {
            return;
        }

        axios.delete(`${config.baseUrl}/categories/${categoryId}`)
            .then(response => {
                this.refreshCategoryControls();

                const data = {
                    message: "Category deleted."
                };

                templateBuilder.append("message", data, "errors")
            })
            .catch(error => {
                const data = {
                    error: "Deleting category failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    refreshCategoryControls()
    {
        this.getAllCategories(categories => {
            loadCategories(categories);
            productService.search();
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
    categoryService = new CategoryService();
});
