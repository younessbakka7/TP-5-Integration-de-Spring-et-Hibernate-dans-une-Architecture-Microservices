package com.microservice.product;

import com.microservice.product.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestHibernate {
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   ETAPE 11 : VERIFICATION DE LA CONFIGURATION SPRING ET HIBERNATE");
        System.out.println("================================================================================");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("Contexte Spring initialise avec succes !");
        System.out.println("SessionFactory Bean detecte : " + context.containsBean("sessionFactory"));
        System.out.println("DataSource Bean detecte : " + context.containsBean("dataSource"));
        System.out.println("TransactionManager detecte : " + context.containsBean("transactionManager"));
        context.close();
        System.out.println("================================================================================");
    }
}
