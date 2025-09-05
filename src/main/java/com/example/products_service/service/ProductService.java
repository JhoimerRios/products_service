package com.example.products_service.service;

import com.example.products_service.dto.ProductDto;
import com.example.products_service.model.Product;
import com.example.products_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> updateProduct(Long id, ProductDto productDto) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            if (productDto.getName() != null) {
                product.setName(productDto.getName());
            }
            if (productDto.getPrice() > 0) {
                product.setPrice(productDto.getPrice());
            }
            if (productDto.getCategory() != null) {
                product.setCategory(productDto.getCategory());
            }
            return Optional.of(productRepository.save(product));
        }
        return Optional.empty();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
