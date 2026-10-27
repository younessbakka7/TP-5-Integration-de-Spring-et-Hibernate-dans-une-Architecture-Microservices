package com.microservice.product;

import com.microservice.product.config.AppConfig;
import com.microservice.product.entity.Product;
import com.microservice.product.service.ProductServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   TP 5 : INTEGRATION DE SPRING ET HIBERNATE DANS UNE ARCHITECTURE MICROSERVICES");
        System.out.println("================================================================================");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("[INFO] Contexte Spring initialise avec succes !\n");

        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);

        System.out.println("[1] Insertion d'un nouveau produit via le Microservice (Etape 12) :");
        Product p1 = new Product("Microservice Gateway", 149.99);
        productService.addProduct(p1);
        System.out.println("-> Produit ajoute avec succes ! Id généré = " + p1.getId());

        System.out.println("\n[2] Insertion de produits complementaires :");
        Product p2 = new Product("Auth Service Token Module", 89.50);
        Product p3 = new Product("Eureka Discovery Client", 199.00);
        productService.addProduct(p2);
        productService.addProduct(p3);
        System.out.println("-> " + p2.getName() + " inséré avec id=" + p2.getId());
        System.out.println("-> " + p3.getName() + " inséré avec id=" + p3.getId());

        System.out.println("\n[3] Consultation de tous les produits (readOnly=true) :");
        List<Product> products = productService.getAllProducts();
        for (Product prod : products) {
            System.out.println("   * [" + prod.getId() + "] " + prod.getName() + " - " + prod.getPrice() + " EUR");
        }
        System.out.println("Total en base : " + products.size());

        System.out.println("\n[4] Test de validation metier (Gestion des exceptions) :");
        try {
            Product invalid = new Product("Produit Invalide", -10.0);
            productService.addProduct(invalid);
        } catch (IllegalArgumentException e) {
            System.out.println("-> [VALIDATION METIER OK] Exception capturee attendue : " + e.getMessage());
        }

        System.out.println("\n[5] Mise a jour et consultation par Id :");
        Product toUpdate = productService.getProductById(p1.getId());
        toUpdate.setPrice(129.99);
        productService.updateProduct(toUpdate);
        System.out.println("-> Produit mis a jour : " + productService.getProductById(p1.getId()));

        System.out.println("\n================================================================================");
        System.out.println("   EXECUTION TERMINEE AVEC SUCCES - ARCHITECTURE MICROSERVICE VALIDE");
        System.out.println("================================================================================");

        context.close();
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable ignored) {}
    }
}
