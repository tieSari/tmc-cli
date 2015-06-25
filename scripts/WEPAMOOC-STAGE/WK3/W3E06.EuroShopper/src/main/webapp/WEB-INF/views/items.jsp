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
        

        <h1>Items</h1>

        <ul>
            <c:forEach var="item" items="${items}">
                <li>${item.name} (${item.price}$)
                    <form action="/cart/items/${item.id}" method="POST">
                        <input type="submit" value="add to cart">
                    </form>
                </li>
            </c:forEach>
        </ul>

        <p>See <a href="/cart">cart</a></p>
    </body>
</html>
