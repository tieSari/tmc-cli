<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Actors</title>
    </head>
    <body>
        <h1>Actors</h1>

        <h2>Add new actor</h2>

        <div>
            Enter information and press submit:<br/>
            <form action="/actors" method="POST">
                Name: <input type="text" name="name" id="name"/><br/>
                <input type="submit"/>
            </form>
        </div>

        <h2>Existing actors</h2>

        <ol>
            <c:forEach var="actor" items="${actors}">
                <li><a href="/actors/${actor.id}">${actor.name}</a>
                    <form:form action="/actors/${actor.id}" method="DELETE">
                        <input type="submit" value="Remove" id="remove-${actor.id}"/>
                    </form:form><br/>

                    Known for:<br/>
                    <ul>
                        <c:forEach var="movie" items="${actor.movies}">
                            <li>${movie.name}</li>
                        </c:forEach>
                    </ul>
                </li>
            </c:forEach>
        </ol>

        <div><a href="/movies">Movies</a></div>
    </body>
</html>
