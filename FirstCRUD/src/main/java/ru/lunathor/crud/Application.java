package ru.lunathor.crud;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // Загружаем переменные окружения из .env файла (если существует)
        // Переменные из .env файла не перезаписывают существующие системные переменные окружения
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // Игнорируем, если файл .env отсутствует
                .ignoreIfMalformed() // Игнорируем невалидные строки
                .load();
        
        // Устанавливаем переменные из .env файла в системные свойства
        // только если они еще не установлены (приоритет у системных переменных окружения)
        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();
            // Проверяем, не установлена ли уже переменная окружения или системное свойство
            if (System.getenv(key) == null && System.getProperty(key) == null) {
                System.setProperty(key, entry.getValue());
            }
        });
        
        SpringApplication.run(Application.class, args);
    }
}
