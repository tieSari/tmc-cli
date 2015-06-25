<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>A Page</title>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        
        <p>The last time you sent data, i got ${data}</p>
        
        <form action="/submit">
            <input type="text" name="data"/>
            <input type="submit"/>
        </form>
    </body>
</html>
