package com.example.products_service.controller;

import com.example.products_service.dto.ProductDto;
import com.example.products_service.model.Product;
import com.example.products_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Crea un nuevo producto.
     * @param productDto El DTO con los datos del nuevo producto.
     * @return Una respuesta con el producto creado y estado 201 Created.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        Product newProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    /**
     * Obtiene un producto por su ID.
     * @param id El ID del producto a buscar.
     * @return Una respuesta con el producto y estado 200 OK si se encuentra, o 404 Not Found si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Actualiza un producto existente.
     * @param id El ID del producto a actualizar.
     * @param productDto El DTO con los datos actualizados del producto.
     * @return Una respuesta con el producto actualizado y estado 200 OK si se encuentra, o 404 Not Found si no.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto)
                .map(updatedProduct -> new ResponseEntity<>(updatedProduct, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Si no se encuentra el producto
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }
}
