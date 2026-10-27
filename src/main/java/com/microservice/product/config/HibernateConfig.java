package com.microservice.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
public class HibernateConfig {

    @Autowired
    private Environment env;

    private boolean isH2Fallback = false;

    @Bean
    public DataSource dataSource() {
        String driver = env.getProperty("jdbc.driver");
        String url = env.getProperty("jdbc.url");
        String user = env.getProperty("jdbc.username");
        String pass = env.getProperty("jdbc.password");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        try {
            Class.forName(driver);
            // Quick connectivity test with short timeout
            java.sql.DriverManager.setLoginTimeout(2);
            java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, pass);
            conn.close();
            
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(pass);
            System.out.println("[INFO] Connexion etablie avec succes a MySQL: " + url);
        } catch (Exception ex) {
            System.out.println("[WARN] Serveur MySQL indisponible (" + ex.getMessage() + "). Utilisation automatique de la base H2 en memoire (mode MySQL).");
            isH2Fallback = true;
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setUrl("jdbc:h2:mem:product_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
        }

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        DataSource ds = dataSource();
        sessionFactory.setDataSource(ds);
        sessionFactory.setPackagesToScan("com.microservice.product.entity");

        Properties props = new Properties();
        if (isH2Fallback) {
            props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        } else {
            props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        }
        props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        sessionFactory.setHibernateProperties(props);

        return sessionFactory;
    }
}
