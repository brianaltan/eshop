package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId("test-id");
        testProduct.setProductName("Test Product");
        testProduct.setProductQuantity(10);
    }

    @Test
    void testCreate() {
        when(productRepository.create(any(Product.class))).thenReturn(testProduct);

        Product created = productService.create(new Product());
        assertNotNull(created.getProductId());
        verify(productRepository).create(any(Product.class));
    }

    @Test
    void testFindAll() {
        List<Product> products = new ArrayList<>();
        products.add(testProduct);
        Iterator<Product> iterator = products.iterator();

        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testProduct.getProductName(), result.get(0).getProductName());
    }

    @Test
    void testFindById() {
        when(productRepository.findById("test-id")).thenReturn(testProduct);

        Product found = productService.findById("test-id");
        assertNotNull(found);
        assertEquals(testProduct.getProductName(), found.getProductName());
    }

    @Test
    void testUpdate() {
        when(productRepository.update(any(Product.class))).thenReturn(testProduct);

        Product updated = productService.update(testProduct);
        assertNotNull(updated);
        assertEquals(testProduct.getProductName(), updated.getProductName());
    }

    @Test
    void testDelete() {
        doNothing().when(productRepository).delete("test-id");

        productService.delete("test-id");
        verify(productRepository).delete("test-id");
    }
}