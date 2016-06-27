<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Withdraw</title>
</head>
<body>
<h2 align="center">Please, enter the required amount</h2>
<form method="post" action="withdraw">
    <p align="center">
        <script>
            function check(event) {
                if (event.keyCode==32){
                    return false;
                }
            }
        </script>
        <input name="withdrawAmount" required pattern="^[0-9]+$" style="width: 60px;height: 30px" onkeypress="return check(event);"/><br>
        <br>
        <input type="submit" value="Submit"/>
        <input type="reset" value="Reset"/>
        <br>
        <br>
        <button type="button" onclick="history.back()">
            <b>Previous page</b>
        </button>
    </p>
</form>
</body>
</html>
