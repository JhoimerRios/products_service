package com.example.products_service.service;

import com.example.products_service.dto.ProductDto;
import com.example.products_service.model.Product;
import com.example.products_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductDto productDto) {
        // Validación de datos
        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        // Conversión DTO a Entidad
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());

        return productRepository.save(product);
    }

    /**
     * Obtiene un producto por su ID.
     * @param id El ID del producto a buscar.
     * @return Un Optional que contiene el producto si se encuentra, o un Optional vacío si no.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Actualiza un producto existente.
     * Los campos nulos o con valores inválidos en el DTO no se actualizarán.
     * @param id El ID del producto a actualizar.
     * @param productDto El DTO con los nuevos datos del producto.
     * @return Un Optional que contiene el producto actualizado si se encuentra, o un Optional vacío si no.
     */
    public Optional<Product> updateProduct(Long id, ProductDto productDto) {
        // Busca el producto por ID
        return productRepository.findById(id).map(existingProduct -> {
            // Actualiza los campos si se proporcionan en el DTO
            if (productDto.getName() != null && !productDto.getName().isEmpty()) {
                existingProduct.setName(productDto.getName());
            }
            if (productDto.getPrice() > 0) { // Solo actualiza si el precio es válido
                existingProduct.setPrice(productDto.getPrice());
            }
            if (productDto.getCategory() != null && !productDto.getCategory().isEmpty()) {
                existingProduct.setCategory(productDto.getCategory());
            }
            return productRepository.save(existingProduct); // Guarda el producto actualizado
        });
    }
}
