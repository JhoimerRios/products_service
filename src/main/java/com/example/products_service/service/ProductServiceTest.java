package com.example.products_service.service;

import com.example.products_service.dto.ProductDto;
import com.example.products_service.model.Product;
import com.example.products_service.repository.ProductRepository;
import org.testng.annotations.Test; // <-- Importación corregida para @Test
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock // Crea un objeto simulado (mock) de ProductRepository
    private ProductRepository productRepository;

    @InjectMocks // Inyecta los mocks en el ProductService
    private ProductService productService;

    @Test
    void whenCreateProduct_thenProductIsSaved() {
        // Arrange: Prepara los datos y el comportamiento simulado
        ProductDto dto = new ProductDto();
        dto.setName("Laptop");
        dto.setPrice(1200.0);
        dto.setCategory("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(1200.0);
        product.setCategory("Electronics");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act: Llama al método que estamos probando
        Product savedProduct = productService.createProduct(dto);

        // Assert: Verifica que el resultado sea el esperado
        assertNotNull(savedProduct.getId());
        assertEquals("Laptop", savedProduct.getName());
    }

    @Test
    void whenGetProductById_thenProductIsReturned() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Tablet");
        product.setPrice(500.0);
        product.setCategory("Electronics");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Optional<Product> foundProduct = productService.getProductById(productId);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals("Tablet", foundProduct.get().getName());
    }

    @Test
    void whenGetProductById_thenNotFound() {
        // Arrange
        Long productId = 2L; // Un ID que no existe
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> foundProduct = productService.getProductById(productId);

        // Assert
        assertFalse(foundProduct.isPresent());
    }

    @Test
    void whenUpdateProduct_thenProductIsUpdated() {
        // Arrange
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Viejo Producto");
        existingProduct.setPrice(100.0);
        existingProduct.setCategory("Categoría Vieja");

        ProductDto updateDto = new ProductDto();
        updateDto.setName("Nuevo Producto");
        updateDto.setPrice(150.0);
        updateDto.setCategory("Nueva Categoría");

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Nuevo Producto");
        updatedProduct.setPrice(150.0);
        updatedProduct.setCategory("Nueva Categoría");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Optional<Product> result = productService.updateProduct(productId, updateDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Nuevo Producto", result.get().getName());
        assertEquals(150.0, result.get().getPrice());
        assertEquals("Nueva Categoría", result.get().getCategory());
    }

    @Test
    void whenUpdateProductWithPartialData_thenOnlyProvidedFieldsAreUpdated() {
        // Arrange
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Producto Original");
        existingProduct.setPrice(200.0);
        existingProduct.setCategory("Electrónica");

        ProductDto partialUpdateDto = new ProductDto();
        partialUpdateDto.setPrice(250.0); // Solo actualizamos el precio

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Producto Original"); // Nombre no cambia
        updatedProduct.setPrice(250.0); // Precio actualizado
        updatedProduct.setCategory("Electrónica"); // Categoría no cambia

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Optional<Product> result = productService.updateProduct(productId, partialUpdateDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Producto Original", result.get().getName()); // Verifica que el nombre no cambió
        assertEquals(250.0, result.get().getPrice());
        assertEquals("Electrónica", result.get().getCategory()); // Verifica que la categoría no cambió
    }


    @Test
    void whenUpdateProduct_thenNotFound() {
        // Arrange
        Long productId = 99L; // ID que no existe
        ProductDto updateDto = new ProductDto();
        updateDto.setName("Producto Inexistente");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.updateProduct(productId, updateDto);

        // Assert
        assertFalse(result.isPresent());
    }
}
