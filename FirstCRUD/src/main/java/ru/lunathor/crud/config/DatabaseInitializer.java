package ru.lunathor.crud.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @org.springframework.beans.factory.annotation.Autowired
    private DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            Connection connection = dataSource.getConnection();
            try {
                Statement stmt = connection.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS users (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(255) NOT NULL, " +
                        "surname VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(255) NOT NULL, " +
                        "age INT NOT NULL" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
                stmt.execute(sql);
                stmt.close();
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Table initialization error: " + e.getMessage());
        }

        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getMetamodel();
        } finally {
            em.close();
        }
    }
}

