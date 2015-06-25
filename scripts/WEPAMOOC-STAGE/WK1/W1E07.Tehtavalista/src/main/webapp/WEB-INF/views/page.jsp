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

        <p><strong>Lisää tehtävä</strong></p>

        <form action="/tasks" method="POST">
            <p>Nimi:</p>
            <input type="text" name="name"/>
            <input type="hidden" name="done" value="false"/>
            <input type="submit" value="Lisää"/>
        </form>

        <p><strong>Tehtävälista</strong></p>

        <ol>
            <c:forEach var="task" items="${tasks}">
                <li>${task.name} 
                    <c:choose>
                        <c:when test="${task.done}">
                            <em>done</em>
                        </c:when>
                        <c:otherwise>
                            <form method="POST" action="/tasks/${task.id}/done"><input type="submit" value="aseta tehdyksi"/></form>
                        </c:otherwise>
                    </c:choose>

                    <form method="POST" action="/tasks/${task.id}/delete"><input type="submit" value="poista"/></form>
                </li>
            </c:forEach>
        </ol>

    </body>
</html>
