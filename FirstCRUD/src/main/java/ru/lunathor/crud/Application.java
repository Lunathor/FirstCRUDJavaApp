package ru.lunathor.crud;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // Загружаем переменные окружения из .env файла
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
        
        // Устанавливаем переменные окружения в систему
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
        
        SpringApplication.run(Application.class, args);
    }
}
