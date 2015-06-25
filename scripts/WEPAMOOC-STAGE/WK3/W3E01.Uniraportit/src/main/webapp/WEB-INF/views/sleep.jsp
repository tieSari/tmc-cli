<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View sleep entry</title>
    </head>
    <body>

        <a href="${pageContext.request.contextPath}/sleeps">Back to sleeps</a>

        <h1>Sleep with ID ${sleep.id}</h1>

        <strong>Start: </strong> <fmt:formatDate value="${sleep.start}" pattern="dd.MM.yyyy HH.mm" /><br/>
        <strong>End: </strong> <fmt:formatDate value="${sleep.end}" pattern="dd.MM.yyyy HH.mm" /><br/>
        <strong>Feeling: </strong> ${sleep.feeling}

        <h2>Delete</h2>

        <!-- Lisää lomake Sleepin poistamiseen tähän -->

        <form:form action="/sleeps/${sleep.id}" method="DELETE">
            <input type="submit" value="Delete">
        </form:form>
    </body>
</html>