package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId("test-id-1");
        testProduct.setProductName("Test Product");
        testProduct.setProductQuantity(10);
    }

    @Test
    void testUpdateProduct_Success() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("test-id-1");
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductQuantity(20);

        when(productRepository.update(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.update(updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getProductName());
        assertEquals(20, result.getProductQuantity());
        verify(productRepository, times(1)).update(any(Product.class));
    }

    @Test
    void testFindProductById_Success() {
        when(productRepository.findById("test-id-1")).thenReturn(testProduct);

        Product result = productService.findById("test-id-1");

        assertNotNull(result);
        assertEquals(testProduct.getProductId(), result.getProductId());
        assertEquals(testProduct.getProductName(), result.getProductName());
    }

    @Test
    void testUpdateProduct_NonexistentProduct() {
        Product nonexistentProduct = new Product();
        nonexistentProduct.setProductId("nonexistent-id");
        when(productRepository.update(any(Product.class))).thenReturn(null);

        Product result = productService.update(nonexistentProduct);

        assertNull(result);
        verify(productRepository, times(1)).update(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        doNothing().when(productRepository).delete("test-id-1");

        productService.delete("test-id-1");

        verify(productRepository, times(1)).delete("test-id-1");
    }

    @Test
    void testDeleteProduct_NonexistentProduct() {
        doNothing().when(productRepository).delete("nonexistent-id");

        productService.delete("nonexistent-id");

        verify(productRepository, times(1)).delete("nonexistent-id");
    }

    @Test
    void testDeleteProduct_NullId() {
        productService.delete(null);
        verify(productRepository, times(1)).delete(null);
    }

    @Test
    void testUpdateProduct_NullProduct() {
        when(productRepository.update(null)).thenReturn(null);

        Product result = productService.update(null);

        assertNull(result);
        verify(productRepository, times(1)).update(null);
    }

    @Test
    void testUpdateProduct_EmptyFields() {
        Product emptyProduct = new Product();
        when(productRepository.update(any(Product.class))).thenReturn(emptyProduct);

        Product result = productService.update(emptyProduct);

        assertNotNull(result);
        assertNull(result.getProductName());
        assertEquals(0, result.getProductQuantity());
        verify(productRepository, times(1)).update(any(Product.class));
    }
}