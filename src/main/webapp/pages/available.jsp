<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
<head>
    <title>Available amount</title>
</head>
<body>

<h2 align="center">ATM can not issue such amount of money</h2>
<h3 align="center">Possible amount = <%= request.getAttribute("availableAmount")%> </h3>

<form action="withdraw" method="post">
<p align="center">
    <input name="withdrawAmount" type="hidden" value="<%= request.getAttribute("availableAmount")%>"/>
<button type="submit">OK</button>
<button type="button" onclick="history.back()">Cancel</button>
    </p>
    </form>
</body>
</html>
