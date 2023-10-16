<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <style>
        .normal {
          color: green;
        }

        .excess {
           color: red;
        }
    </style>
</head>
<body>

<h3><a href="index.html">Home</a></h3>

<h2>All meals</h2>
<br>
<p><a href="meals?action=insert">Add Meal</a></p>


<table border="1" cellpadding="8" cellspacing="0">
    <thread>
        <tr>
            <th>Date</th>
            <th>Time</th>
            <th>Description</th>
            <th>Calories</th>
            <th colspan=2>Action</th>
        </tr>
    </thread>

    <c:forEach var="meal" items="${meals}">
        <tr class="${meal.excess ? 'excess' : 'normal'}">
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