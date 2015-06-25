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

        <h2>Shopping cart</h2>

        <c:set var="total" value="${0}"/>
        
        <ul>
            <c:forEach var="entry" items="${items}">
                <li>${entry.key.name} - ${entry.value} kpl
                    <form:form action="/cart/items/${entry.key.id}" method="DELETE"><input type="submit" value="-"/></form:form>
                    <form:form action="/cart/items/${entry.key.id}" method="POST"><input type="submit" value="+"/></form:form>
                    </li>

                <c:set var="total" value="${total + (entry.key.price * entry.value)}"/>
            </c:forEach>
        </ul>
        
        <p><strong>Total <c:out value="${total}"/> $$$</strong></p>
        
        <c:if test="${total > 100}">
            <img src="/img/cart.png"/>            
        </c:if>


        <p><a href="/orders">Proceed to ordering.</a></p>
        <p><a href="/items">Back to items.</a></p>
    </body>
</html>
