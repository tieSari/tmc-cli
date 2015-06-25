<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Airports</title>
    </head>
    <body>
        <h1>Airports</h1>

        <h2>Add new airport</h2>

        <div>
            Enter information and press submit:<br/>
            <form action="/airports" method="POST">
                Name: <input type="text" name="name" id="name"/><br/>
                Identifier <input type="text" name="identifier" id="identifier"/><br/>
                <input type="submit"/>            
            </form>
        </div>

        <h2>Current airports</h2>

        <ol>
            <c:forEach var="airport" items="${airports}">
                <li>${airport.name} (${airport.identifier})<br/>
                    Aircrafts:
                    <ul>
                        <c:forEach var="aircraft" items="${airport.aircrafts}">
                            <li>${aircraft.name}</li>
                            </c:forEach>
                    </ul>
                </li>
            </c:forEach>
        </ol>

        <p><a href="/aircrafts">Aircrafts</a></p>
    </body>
</html>
