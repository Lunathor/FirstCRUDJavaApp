<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Список пользователей</title>
        <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
    </head>
    <body>
        <h1>Список пользователей</h1>
        <a href="/users/new" class="button">Добавить пользователя</a>
        <br>
        <c:choose>
            <c:when test="${empty users}">
                <p>Список пользователей пуст.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Имя</th>
                        <th>Фамилия</th>
                        <th>Email</th>
                        <th>Возраст</th>
                        <th>Действия</th>
                    </tr>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.surname}</td>
                            <td>${user.email}</td>
                            <td>${user.age}</td>
                            <td>
                                <a href="/users/edit?id=${user.id}" class="button button-edit">Редактировать</a>
                                <form method="post" action="/users/delete" style="display: inline;" onsubmit="return confirmDelete();">
                                    <input type="hidden" name="id" value="${user.id}" />
                                    <button type="submit" class="button button-delete">Удалить</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
        <script src="/resources/js/script.js"></script>
    </body>
</html>