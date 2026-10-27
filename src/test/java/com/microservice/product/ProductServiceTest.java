package com.microservice.product;

import com.microservice.product.config.AppConfig;
import com.microservice.product.entity.Product;
import com.microservice.product.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private AnnotationConfigApplicationContext context;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        productService = context.getBean(ProductService.class);
    }

    @AfterEach
    public void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    public void testAddProductSuccess() {
        Product p = new Product("Laptop Dell XPS", 1200.0);
        productService.addProduct(p);
        assertNotNull(p.getId(), "L'identifiant genere ne doit pas etre null");

        Product found = productService.getProductById(p.getId());
        assertNotNull(found, "Le produit insere doit etre retrouve en base");
        assertEquals("Laptop Dell XPS", found.getName());
        assertEquals(1200.0, found.getPrice(), 0.001);
    }

    @Test
    public void testAddProductNegativePriceThrowsException() {
        Product p = new Product("Article Gratuit Frauduleux", -25.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(p);
        });
        assertEquals("Le prix ne peut pas être négatif", exception.getMessage());
    }

    @Test
    public void testGetAllProducts() {
        Product p1 = new Product("Routeur Cisco", 450.0);
        Product p2 = new Product("Switch Netgear", 120.0);
        productService.addProduct(p1);
        productService.addProduct(p2);

        List<Product> list = productService.getAllProducts();
        assertTrue(list.size() >= 2, "La liste doit contenir au moins 2 produits");
    }

    @Test
    public void testUpdateProduct() {
        Product p = new Product("Clavier Mecanique", 80.0);
        productService.addProduct(p);

        p.setPrice(75.0);
        productService.updateProduct(p);

        Product updated = productService.getProductById(p.getId());
        assertEquals(75.0, updated.getPrice(), 0.001);
    }

    @Test
    public void testDeleteProduct() {
        Product p = new Product("Souris Sans Fil", 30.0);
        productService.addProduct(p);
        Long id = p.getId();

        Product found = productService.getProductById(id);
        assertNotNull(found);

        productService.deleteProduct(found);
        Product deleted = productService.getProductById(id);
        assertNull(deleted, "Le produit doit etre supprime de la base");
    }
}
