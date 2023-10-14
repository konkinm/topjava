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

<h3><a href="index.html">Home</a></h3>

<h2>All meals</h2>
<br>
<p><a href="meals?action=insert">Add Meal</a></p>


<table>
    <tr>
        <th>Date</th>
        <th>Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan=2>Action</th>
    </tr>

    <c:forEach var="meal" items="${meals}">
        <tr style="${meal.excess ? 'color:red' : 'color:green'}">
            <td>${meal.date}</td>
            <td>${meal.time}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>

</table>

<br>

</body>
</html>