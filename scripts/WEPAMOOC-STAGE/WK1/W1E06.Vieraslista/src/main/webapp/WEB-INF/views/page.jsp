<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>A Page</title>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>

        <p><strong>Ilmoittautuminen</strong></p>
        
        <form action="/guests" method="POST">
            <p>Nimi:</p>
            <input type="text" name="name"/>
            <p>Ruokavalio:</p>
            <input type="radio" name="menu" value="liha" checked="checked"/>Lihaisa<br/>
            <input type="radio" name="menu" value="kala"/>Kalaisa<br/>
            <input type="submit" value="Lisää"/>
        </form>

        <p><strong>Ilmoittautumiset</strong></p>
        
        <ol>
            <c:forEach var="guest" items="${guests}">
                <li>${guest.name} (${guest.menu}) <form action="/guests/${guest.id}/delete" method="POST"><input type="submit" value="poista"/></form></li>
            </c:forEach>
        </ol>

    </body>
</html>
