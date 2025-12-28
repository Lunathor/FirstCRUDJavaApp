<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% 
    String contextPath = request.getContextPath();
    response.sendRedirect(contextPath + "/users/list"); 
%>

