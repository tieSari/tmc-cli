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

        <p><strong>Lisää albumi</strong></p>

        <form action="/albums" method="POST">
            <p>Nimi:</p>
            <input type="text" name="name"/>
            <input type="submit" value="Lisää"/>
        </form>

        <p><strong>Albumilista</strong></p>

        <c:forEach var="album" items="${albums}">
            <p>Albumi <em>${album.name}</em></p>
            <ul>
                <c:forEach var="track" items="${album.tracks}">
                    <li>${track.name} <form action="/albums/${album.id}/tracks/${track.id}/delete" method="POST"><input type="submit" value="poista"/></form></li>
                </c:forEach>
            </ul>

            <p>Lisää kappale albumille ${album.name}</p>

            <form action="/albums/${album.id}/tracks" method="POST">
                <p>Kappale: <input type="text" name="name"/><input type="submit" value="Lisää"/></p>
            </form>

        </c:forEach>

    </body>
</html>
