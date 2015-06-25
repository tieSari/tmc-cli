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

        <h1>Sydän.</h1>

        <form:form commandName="pair" action="/loves" method="POST">
            <p>Sinun nimesi: <form:input path="nameOne" /><form:errors path="nameOne" /></p>
            <p>Ihastuksesi nimi: <form:input path="nameTwo" /><form:errors path="nameTwo" /></p>
            <input type="submit" value="Lisää">
        </form:form>

        <c:if test="${not empty match}">
            <p>Oijoi! Kolahtaa noin ${match} prosentin varmuudella!</p>
            
            <c:if test="${match >= 99}">
                <iframe width="560" height="315" src="//www.youtube-nocookie.com/embed/Xk_XaJ7gE4Q?rel=0&autoplay=1" frameborder="0" allowfullscreen></iframe>
            </c:if>
        </c:if>
    </body>
</html>
