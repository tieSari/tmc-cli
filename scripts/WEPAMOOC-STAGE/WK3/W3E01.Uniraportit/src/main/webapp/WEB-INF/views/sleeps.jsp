<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sleep measurements</title>
    </head>
    <body>

        <h1>Sleeps</h1>


        <h2>New sleep</h2>

        <p>Example of date format: 1.9.2014 21.30</p>

        <form:form commandName="sleep" action="/sleeps" method="POST">
            <label>Start: </label><form:input id="start" path="start" /> <form:errors path="start" /><br/>
            <label>End: </label><form:input id="end" path="end" /> <form:errors path="end" /><br/>
            <label>Feeling: </label><form:input id="feeling" path="feeling" /> <form:errors path="feeling" /><br/>
            <input type="submit" value="RESTed">
        </form:form>

        <h2>List</h2>

        <ul>
            <c:forEach var="sleep" items="${sleeps}">
                <li><a href="/sleeps/${sleep.id}">Sleep with ID ${sleep.id}</a></li>
            </c:forEach>
        </ul>

    </body>
</html>