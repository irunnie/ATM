<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Withdraw</title>
</head>
<body>
<h2 align="center">Please, enter the required amount</h2>
<form method="post" action="withdraw">
    <p align="center">
        <input name="withdrawAmount" type="number"/><br>
        <br>
        <input type="submit" value="Submit"/>
        <input type="reset" value="Reset"/>
        <br>
        <br>
        <a href="../index.jsp">
        <button>
            <b>Main page</b>
        </button>
            </a>
    </p>
</form>
</body>
</html>
