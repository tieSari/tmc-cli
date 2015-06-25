<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Reservations</h1>

        <ul>
            <c:forEach var="reservation" items="${reservations}">
                <li>${reservation.apartment} (<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${reservation.reservationStart}" /> - <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${reservation.reservationEnd}" />) - ${reservation.paymentStatus}</li>
            </c:forEach>
        </ul>

        <h2>Add a new reservation</h2>

        <form action="/reservations" method="POST">
            <p>Apartment:</p>
            <select name="apartmentId">
                <c:forEach var="apartment" items="${apartments}">
                    <option value="${apartment.id}">${apartment.name}</option>
                </c:forEach>
            </select>

            <p>From (yyyy-MM-dd HH:mm):</p>
            <input type="text" name="reservationStart"/>

            <p>To (yyyy-MM-dd HH:mm):</p>
            <input type="text" name="reservationEnd"/>

            <input type="submit" value="Reserve"/>            
        </form>
    </body>
</html>
