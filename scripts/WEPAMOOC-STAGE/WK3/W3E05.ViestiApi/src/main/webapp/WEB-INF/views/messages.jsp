<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>A Page</title>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>

        <h1>Send message</h1>

        <form:form commandName="message" action="/messages" method="POST">
            <p>Viesti <form:input path="message" /><form:errors path="message" /></p>
            <input type="submit" value="Lähetä">
        </form:form>

        <h1>Messages</h1>

        <ul>
            <c:forEach var="message" items="${messages}">
                <li>${message.message}</li>
                </c:forEach>
        </ul>
    </body>
</html>
