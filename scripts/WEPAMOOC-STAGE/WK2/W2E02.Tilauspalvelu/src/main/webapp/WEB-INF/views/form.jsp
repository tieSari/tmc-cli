<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Place an order</title>
    </head>
    <body>
        <h1>Select items, and enter your data</h1>

        <div>
            <form:form commandName="order" action="/orders" method="POST" >
                Name: <form:input path="name" /> <form:errors path="name" /><br/>
                Address: <form:input path="address" /> <form:errors path="address" /><br/>
                Items: <form:checkboxes items="${items}" path="items"/> <form:errors path="items" /><br/>
                <input type="submit"/>
            </form:form>
        </div>
    </body>
</html>
