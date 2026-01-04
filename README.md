# FirstCRUD - Spring Boot CRUD приложение с аутентификацией

## Описание проекта

**FirstCRUD** — это веб-приложение на Spring Boot, реализующее полнофункциональную систему управления пользователями (CRUD) с ролевой моделью доступа и аутентификацией через Spring Security.

## Основные возможности

- ✅ **CRUD операции** для управления пользователями
- ✅ **Аутентификация и авторизация** через Spring Security
- ✅ **Ролевая модель доступа** (ADMIN, USER)
- ✅ **Хеширование паролей** с использованием BCrypt
- ✅ **Интеграция с MySQL** через Hibernate/JPA
- ✅ **Thymeleaf шаблоны** для веб-интерфейса
- ✅ **Современный адаптивный UI** с градиентами и анимациями
- ✅ **DTO паттерн** для передачи данных между слоями
- ✅ **Поддержка переменных окружения** через .env файл
- ✅ **Автоматическая инициализация данных** при запуске

## Технологический стек

### Backend
- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security** — аутентификация и авторизация
- **Spring Data JPA** — работа с базой данных
- **Hibernate** — ORM фреймворк
- **MySQL** — реляционная база данных

### Frontend
- **Thymeleaf** — шаблонизатор
- **HTML5/CSS3** — современный адаптивный интерфейс
- **Градиенты и анимации** — улучшенный UX
- **Адаптивный дизайн** — поддержка мобильных устройств

### Инструменты
- **Maven** — управление зависимостями
- **Dotenv Java** — поддержка переменных окружения

## Структура проекта

```
FirstCRUD/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ru/lunathor/crud/
│   │   │       ├── Application.java              # Главный класс приложения
│   │   │       ├── config/                       # Конфигурационные классы
│   │   │       │   ├── AppConfig.java
│   │   │       │   ├── DataInitializer.java      # Инициализация начальных данных
│   │   │       │   ├── LoginSuccessHandler.java  # Обработчик успешного входа
│   │   │       │   └── WebSecurityConfig.java    # Конфигурация безопасности
│   │   │       ├── controller/                   # Контроллеры
│   │   │       │   ├── IndexController.java
│   │   │       │   ├── LoginController.java
│   │   │       │   └── UserController.java       # CRUD операции для пользователей
│   │   │       ├── dao/                          # Data Access Object
│   │   │       │   ├── RoleDao.java
│   │   │       │   ├── RoleDaoImpl.java
│   │   │       │   ├── UserDao.java
│   │   │       │   └── UserDaoImpl.java
│   │   │       ├── dto/                          # Data Transfer Objects
│   │   │       │   └── UserFormDto.java          # DTO для формы пользователя
│   │   │       ├── model/                        # Модели данных
│   │   │       │   ├── Role.java                 # Роль пользователя
│   │   │       │   └── User.java                 # Пользователь
│   │   │       └── service/                      # Бизнес-логика
│   │   │           ├── RoleService.java
│   │   │           ├── RoleServiceImpl.java
│   │   │           ├── UserService.java
│   │   │           └── UserServiceImpl.java      # Реализует UserService и UserDetailsService
│   │   ├── resources/
│   │   │   ├── application.properties            # Конфигурация приложения
│   │   │   └── templates/                       # Thymeleaf шаблоны
│   │   │       ├── login.html
│   │   │       └── users/
│   │   │           ├── form.html                 # Форма создания/редактирования
│   │   │           ├── list.html                  # Список пользователей
│   │   │           └── user.html                 # Профиль пользователя
│   │   └── webapp/
│   │       └── resources/
│   │           └── css/
│   │               └── style.css
└── pom.xml                                        # Maven конфигурация
```

## Архитектура

Проект следует классической многослойной архитектуре:

1. **Controller Layer** — обработка HTTP запросов и ответов, делегирование в сервисный слой
2. **Service Layer** — бизнес-логика приложения, работа с DTO
3. **DAO Layer** — абстракция доступа к данным, работа с Entity
4. **Model Layer** — сущности базы данных (JPA Entity)
5. **DTO Layer** — объекты передачи данных между слоями

### Особенности реализации:

- **UserServiceImpl** реализует одновременно `UserService` и `UserDetailsService` для интеграции с Spring Security
- Использование **DTO (Data Transfer Objects)** для изоляции слоев и упрощения работы с формами
- **LAZY загрузка** ролей с использованием `LEFT JOIN FETCH` в запросах для оптимизации производительности
- Вся бизнес-логика вынесена в сервисный слой, контроллеры только делегируют вызовы

## Модели данных

### User (Пользователь)

```java
@Entity
@Table(name="users")
public class User implements UserDetails {
    private Long id;
    private String username;      // Уникальное имя пользователя
    private String password;      // Хешированный пароль
    private String name;          // Имя
    private String surname;       // Фамилия
    private String email;         // Email
    private Integer age;          // Возраст
    private Set<Role> roles;      // Роли пользователя (Many-to-Many)
}
```

### Role (Роль)

```java
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    private Long id;
    private String name;          // Название роли (ADMIN, USER)
    private Set<User> users;      // Пользователи с этой ролью
}
```

**Связь:** Many-to-Many между User и Role через промежуточную таблицу `users_roles`.

**Особенности:**
- Используется `LAZY` загрузка для оптимизации производительности
- Роли загружаются через `LEFT JOIN FETCH` в запросах, где это необходимо
- `User` реализует `UserDetails` для интеграции с Spring Security
- `Role` реализует `GrantedAuthority` для работы с правами доступа

### UserFormDto (DTO для формы)

```java
public class UserFormDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private Integer age;
    private List<Long> roleIds;  // Список ID ролей
}
```

**Назначение:** Используется для передачи данных из формы в сервисный слой, изолируя Entity от представления.

## Конфигурация

### База данных

Настройки базы данных находятся в `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_hiber?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**Примечание:** Драйвер и диалект настраиваются автоматически Spring Boot на основе зависимостей.

### Переменные окружения

Приложение поддерживает использование переменных окружения через `.env` файл или системные переменные. Приоритет:
1. Системные переменные окружения
2. Переменные из `.env` файла
3. Значения по умолчанию из `application.properties`

**Пример `.env` файла:**

```env
DB_URL=jdbc:mysql://localhost:3306/spring_hiber?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
DB_USERNAME=root
DB_PASSWORD=your_password
DB_DRIVER=com.mysql.cj.jdbc.Driver
SERVER_PORT=8080
```

### Безопасность

Конфигурация безопасности в `WebSecurityConfig`:

- **Публичные эндпоинты:**
  - `/login` — страница входа
  - `/resources/**` — статические ресурсы

- **Защищенные эндпоинты:**
  - `/admin/**` — только для роли ADMIN
  - `/user` — для ролей USER и ADMIN
  - Все остальные — требуют аутентификации

## Установка и запуск

### Требования

- **Java 17** или выше
- **Maven 3.6+**
- **MySQL 8.0+** (или совместимая версия)

### Шаги установки

1. **Клонируйте репозиторий** (если применимо):
   ```bash
   git clone <repository-url>
   cd FirstCRUDJavaApp/FirstCRUD
   ```

2. **Создайте базу данных MySQL:**
   ```sql
   CREATE DATABASE spring_hiber;
   ```
   Или база будет создана автоматически при первом запуске (если указан параметр `createDatabaseIfNotExist=true`).

3. **Настройте подключение к базе данных:**
   
   **Вариант 1:** Создайте файл `.env` в корне проекта:
   ```env
   DB_URL=jdbc:mysql://localhost:3306/spring_hiber?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
   DB_USERNAME=root
   DB_PASSWORD=your_password
   ```
   
   **Вариант 2:** Отредактируйте `src/main/resources/application.properties`

4. **Соберите проект:**
   ```bash
   mvn clean install
   ```

5. **Запустите приложение:**
   ```bash
   mvn spring-boot:run
   ```
   
   Или запустите класс `Application.java` из IDE.

6. **Откройте браузер:**
   ```
   http://localhost:8080
   ```

## Использование

### Первый запуск

При первом запуске приложение автоматически:
- Создаст необходимые таблицы в базе данных
- Инициализирует роли ADMIN и USER
- Назначит роль ADMIN всем существующим пользователям без ролей
- Установит пароль "admin" для пользователей без пароля

### Вход в систему

1. Перейдите на страницу входа: `http://localhost:8080/login`
2. Используйте учетные данные существующего пользователя
3. После входа вы будете перенаправлены:
   - **ADMIN** → `/admin/list` (список всех пользователей)
   - **USER** → `/user` (личный профиль)

### Функционал для администратора

Доступен по адресу `/admin/**`:

- **Просмотр списка пользователей** (`/admin/list`)
- **Создание нового пользователя** (`/admin/new`)
- **Редактирование пользователя** (`/admin/edit?id={id}`)
- **Удаление пользователя** (`/admin/delete`)
- **Назначение ролей** при создании/редактировании

### Функционал для обычного пользователя

Доступен по адресу `/user`:

- **Просмотр личного профиля**
- **Просмотр назначенных ролей**

## API Endpoints

### Публичные

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/login` | Страница входа |
| POST | `/login` | Обработка формы входа |
| POST | `/logout` | Выход из системы |

### Для аутентифицированных пользователей

| Метод | URL | Роль | Описание |
|-------|-----|------|----------|
| GET | `/user` | USER, ADMIN | Просмотр личного профиля |

### Для администраторов

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/admin/` | Редирект на `/admin/list` |
| GET | `/admin/list` | Список всех пользователей |
| GET | `/admin/new` | Форма создания пользователя |
| POST | `/admin/save` | Сохранение нового пользователя |
| GET | `/admin/edit` | Форма редактирования (требует `?id={id}`) |
| POST | `/admin/update` | Обновление пользователя |
| POST | `/admin/delete` | Удаление пользователя (требует `?id={id}`) |

## Безопасность

### Хеширование паролей

Все пароли хешируются с использованием **BCrypt** перед сохранением в базу данных. Это обеспечивает безопасное хранение паролей.

### Роли и права доступа

- **ADMIN** — полный доступ ко всем функциям управления пользователями
- **USER** — доступ только к просмотру собственного профиля

### Защита от CSRF

Spring Security автоматически защищает от CSRF атак для всех POST запросов.

## Разработка

### Добавление новых ролей

1. Создайте новую роль в базе данных или через `DataInitializer`
2. Обновите `WebSecurityConfig` для настройки доступа по новой роли

### Расширение функционала

Проект следует стандартным паттернам Spring Boot:
- Добавьте новые методы в `UserService` для бизнес-логики
- Создайте новые контроллеры для новых сущностей
- Используйте `@Service`, `@Repository`, `@Controller` аннотации
- Создавайте DTO классы для передачи данных между слоями
- Вся бизнес-логика должна находиться в сервисном слое

### UI/UX улучшения

Приложение использует современный дизайн:
- Градиентные фоны и кнопки
- Плавные анимации и переходы
- Адаптивная верстка для всех устройств
- Улучшенная типографика и отступы
- Интуитивно понятный интерфейс

## Устранение неполадок

### Проблемы с подключением к базе данных

1. Проверьте, что MySQL запущен
2. Убедитесь, что учетные данные в `.env` или `application.properties` корректны
3. Проверьте, что база данных существует или параметр `createDatabaseIfNotExist=true` установлен

### Проблемы с аутентификацией

1. Убедитесь, что пользователь существует в базе данных
2. Проверьте, что пароль захеширован с помощью BCrypt
3. Убедитесь, что пользователю назначена хотя бы одна роль

### Проблемы с портом

Если порт 8080 занят, измените его в `.env`:
```env
SERVER_PORT=8081
```

## Лицензия

Этот проект создан в образовательных целях.

## Автор

Lunathor

---

**Версия:** 1.0-SNAPSHOT  
**Последнее обновление:** Январь 2026

---

## История изменений

### Версия 1.0-SNAPSHOT (Январь 2025)
- ✅ Рефакторинг: объединение `UserServiceImpl` и `UserDetailsServiceImpl`
- ✅ Внедрение DTO паттерна (`UserFormDto`)
- ✅ Перенос бизнес-логики из контроллеров в сервисный слой
- ✅ Замена `EAGER` на `LAZY` загрузку с оптимизацией запросов
- ✅ Полный редизайн UI с современным адаптивным интерфейсом
- ✅ Улучшение производительности и архитектуры приложения

