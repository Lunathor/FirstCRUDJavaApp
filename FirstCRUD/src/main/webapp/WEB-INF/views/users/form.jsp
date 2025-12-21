<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>${user.id == null ? 'Добавить' : 'Редактировать'} пользователя</title>
        <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
    </head>
    <body>
        <h1>${user.id == null ? 'Добавить' : 'Редактировать'} пользователя</h1>
        <form method="post" action="${user.id == null ? '/users/save' : '/users/update'}">
            <c:if test="${user.id != null}">
                <input type="hidden" name="id" value="${user.id}"/>
            </c:if>
            <label for="name">Имя:</label>
            <input type="text" id="name" name="name" value="${user.name}" required/>

            <label for="surname">Фамилия:</label>
            <input type="text" id="surname" name="surname" value="${user.surname}" required/>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="${user.email}" required/>

            <label for="age">Возраст:</label>
            <input type="number" id="age" name="age" value="${user.age}" required/>

            <div class="actions">
                <button type="submit" class="button">Сохранить</button>
                <a href="/users/list" class="button button-cancel">Отмена</a>
            </div>
        </form>
        <script src="/resources/js/script.js"></script>
    </body>
</html>