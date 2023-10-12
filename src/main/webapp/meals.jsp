<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
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
        <tr <c:if test="${meal.excess}">style="color:red;"</c:if>
            <c:if test="${!meal.excess}">style="color:green;"</c:if>>
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