<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Aircrafts</title>
    </head>
    <body>
        <h1>Aircrafts</h1>

        <h2>Add new aircraft</h2>

        <div>
            Enter information and press Submit:<br/>
            <form action="/aircrafts" method="POST">
                Name: <input type="text" name="name" id="name"/><br/>
                Capacity: <input type="text" name="capacity" id="capacity"/><br/>
                <input type="submit"/>            
            </form>
        </div>

        <h2>Current aircrafts</h2>

        <ol>
            <c:forEach var="aircraft" items="${aircrafts}">
                <li>${aircraft.name}
                    <c:choose>
                        <c:when test="${aircraft.airport == null}">
                            -- Unassigned. Assign to:<br/>
                            <form action="/aircrafts/${aircraft.id}/airports" method="POST">
                                <select name="airportId">
                                    <c:forEach var="airport" items="${airports}">
                                        <option value="${airport.id}">${airport.name}</option>
                                    </c:forEach>
                                </select>
                                <input type="submit" value="Assign"/>
                            </form>
                        </c:when>
                        <c:otherwise>
                            at ${aircraft.airport.name}
                        </c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ol>

        <p><a href="/airports">Airports</a></p>
    </body>
</html>
