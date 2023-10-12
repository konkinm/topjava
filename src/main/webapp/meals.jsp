<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <style>
    table, td, th {
      border: 1px solid;
    }

    table {
      border-collapse: collapse;
    }

    th, td {
      padding: 10px;
      text-align: left;
    }
    </style>
</head>
<body>

<h2>All meals</h2>
<br>

<table>
    <tr>
        <th>Date</th>
        <th>Time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>

    <c:forEach var="meal" items="${allMeals}">
        <tr style="${meal.excess ? 'color:red' : 'color:green'}">
            <td>${meal.date}</td>
            <td>${meal.time}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>

</table>

<br>

</body>
</html>