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
        <h1>${message}</h1>

        <h2>Past orders sent to ..</h2>

        <ul>
            <c:forEach var="order" items="${orders}">
                <li>${order.userDetails.name}</li>   
                </c:forEach>
        </ul>

        <h2>Order address</h2>

        <form:form commandName="userDetails" action="/orders" method="POST">

            <p>Name: <form:input path="name" /> <form:errors path="name" /></p>
            <p>Address: <form:input path="address" /> <form:errors path="address" /></p>

            <input type="submit" value="Place order"/>
        </form:form>

        <p><a href="/items">Back to items.</a></p>

        <p><a href="/cart">Back to cart.</a></p>

    </body>
</html>
