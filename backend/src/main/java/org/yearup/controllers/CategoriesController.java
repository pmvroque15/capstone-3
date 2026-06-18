package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.List;

// add the annotations to make this a REST controller
@RestController
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoriesController
{
    private CategoryService categoryService;
    private ProductService productService;

    // create an Autowired constructor to inject the categoryService and productService
    @Autowired
    public CategoriesController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    // add the appropriate annotation for a get action
    @GetMapping
    public ResponseEntity<List<Category>> getAll()
    {
        List<Category> categories = categoryService.getAllCategories();
        // find and return all categories
        return ResponseEntity.ok(categories);
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id)
    {
        Category category = categoryService.getById(id);
        if(category == null) {
            return ResponseEntity.notFound().build();
        }
        // get the category by id
        return ResponseEntity.ok(category);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsById(@PathVariable int categoryId)
    {
        List<Product> products = productService.listByCategoryId(categoryId);
        // get a list of product by categoryId
        return ResponseEntity.ok(products);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        // insert the category and return it with status 201 Created
        Category newCategory = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public Category updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id and return the updated category (200 OK)
        return null;
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        // delete the category by id and return status 204 No Content
        return null;
    }
}
