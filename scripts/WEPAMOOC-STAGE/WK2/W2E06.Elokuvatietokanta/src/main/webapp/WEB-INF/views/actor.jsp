<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${actor.name}</title>
    </head>
    <body>
        <h1>Actor ${actor.name}</h1>

        <h2>Movies</h2>

        <div>
            <ul>
                <c:forEach var="movie" items="${actor.movies}">
                    <li>${movie.name}</li>
                    </c:forEach>
            </ul>
        </div>

        <c:if test="${not empty movies}">
            <h2>Assign to movie</h2>

            <div>
                <form method="POST" action="/actors/${actor.id}/movies">
                    <select name="movieId">
                        <c:forEach var="movie" items="${movies}">
                            <option value="${movie.id}">${movie.name}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="Assign" id="add-to-movie"/>
                </form>
            </div>
        </c:if>

        <div><a href="/actors">Actors</a></div>
    </body>
</html>
