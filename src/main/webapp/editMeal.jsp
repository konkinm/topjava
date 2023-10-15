<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <meta charset="utf-8">
</head>
<body>
<h2>Edit meal</h2>
<br>
<form method="POST" action='meals' name="frmEditMeal">
        <input type="hidden" readonly="readonly" name="id"
            value="<c:out value="${meal.id}" />" />
        DateTime: <input
            type="datetime-local" name="datetime"
            value="<c:out value="${meal.dateTime}" />" /> <br />
        Description: <input
            type="text" name="description"
            value="<c:out value="${meal.description}" />" /> <br />
        Calories: <input
            type="text" name="calories"
            value="<c:out value="${meal.calories}" />" /> <br />
        <input type="submit" value="Save">
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</body>
</html>