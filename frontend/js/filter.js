


function loadCategories(categories)
{
    const select = document.getElementById('category-select');
    const adminList = document.getElementById('admin-category-list');
    const adminTools = document.getElementById('admin-catalog-tools');

    if (adminTools) {
        adminTools.classList.toggle('hidden', !userService.isAdmin());
    }

    select.innerHTML = "";

    const showAll = document.createElement('option');
    showAll.setAttribute('value', 0);
    showAll.innerText = "Show All";
    select.appendChild(showAll);

    if (adminList) {
        adminList.innerHTML = "";
    }

    categories.forEach(c => {
        const option = document.createElement('option');
        option.setAttribute('value', c.categoryId);
        option.innerText = c.name;
        select.appendChild(option);

        if (adminList && userService.isAdmin()) {
            const item = document.createElement('div');
            item.classList.add('admin-category-row');

            const name = document.createElement('span');
            name.innerText = c.name;
            item.appendChild(name);

            const actions = document.createElement('div');

            const editButton = document.createElement('button');
            editButton.classList.add('btn');
            editButton.classList.add('btn-outline-primary');
            editButton.type = 'button';
            editButton.innerHTML = '<i class="fa-solid fa-pen-to-square"></i>';
            editButton.setAttribute('aria-label', `Edit ${c.name}`);
            editButton.addEventListener('click', () => categoryService.showEditCategoryForm(c.categoryId));
            actions.appendChild(editButton);

            const deleteButton = document.createElement('button');
            deleteButton.classList.add('btn');
            deleteButton.classList.add('btn-outline-danger');
            deleteButton.type = 'button';
            deleteButton.innerHTML = '<i class="fa-solid fa-trash"></i>';
            deleteButton.setAttribute('aria-label', `Delete ${c.name}`);
            deleteButton.addEventListener('click', () => categoryService.deleteCategory(c.categoryId));
            actions.appendChild(deleteButton);

            item.appendChild(actions);
            adminList.appendChild(item);
        }
    })

    if (productService.filter.cat) {
        select.value = productService.filter.cat;
    }
}

document.addEventListener('DOMContentLoaded', () => {
})
