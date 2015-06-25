<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order information</title>
    </head>
    <body>
        <h1>Hello ${order.name}! ${message}</h1>

        <p>Your order will be posted to ${order.address}</p>

        <p>Ordered items:</p>

        <ol>
            <c:forEach var="item" items="${order.items}">
                <li>${item}</li>
            </c:forEach>
        </ol>
    </body>
</html>
