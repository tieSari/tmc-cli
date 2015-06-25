<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Movies</title>
    </head>
    <body>
        <h1>Movies</h1>

        <h2>Add new movie</h2>

        <div>
            Enter name and length and press Submit:<br/>
            <form action="/movies" method="POST">
                Name: <input type="text" name="name" id="name"/><br/>
                Length: <input type="text" name="lengthInMinutes" id="lengthInMinutes"/><br/>
                <input type="submit"/>            
            </form>
        </div>

        <h2>Current movies</h2>

        <ol>
            <c:forEach var="movie" items="${movies}">
                <li>${movie.name} (${movie.lengthInMinutes} min)
                    <form:form action="/movies/${movie.id}" method="DELETE">
                        <input type="submit" value="Remove"/>
                    </form:form><br/>

                    Actors:</br>
                    <ul>
                        <c:forEach var="actor" items="${movie.actors}">
                            <li>${actor.name}</li>
                            </c:forEach>
                    </ul>
                </li>
            </c:forEach>
        </ol>

        <div><a href="/actors">Actors</a></div>
    </body>
</html>
