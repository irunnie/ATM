<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Withdraw</title>
    <script>
        function empty_form()
        {
            var amount = document.getElementById('num').value;
            return amount != '';
        }
    </script>
</head>
<body>
<h2 align="center">Please, enter the required amount</h2>
<form method="post" action="withdraw" onsubmit="return empty_form()">
    <p align="center">
        <input name="withdrawAmount" id="num" type="number"/><br>
        <br>
        <input type="submit" value="Submit"/>
        <input type="reset" value="Reset"/>
        <br>
        <br>
    </p>
</form>
<p align="center">
    <a href="../index.jsp">
        <button>
            <b>Main page</b>
        </button>
    </a>
</p>
</body>
</html>
