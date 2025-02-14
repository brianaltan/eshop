package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product){
        productData.add(product);
        return product;
    }
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String productId) {
        return productData.stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product update(Product updatedProduct) {
        Product existingProduct = findById(updatedProduct.getProductId());
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setProductQuantity(updatedProduct.getProductQuantity());
        return existingProduct;
    }

    public void delete(String productId) {
        productData.removeIf(product -> product.getProductId().equals(productId));
    }
}