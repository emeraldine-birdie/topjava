<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1">
    <caption>Meal list</caption>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Excess</th>
    </tr>
    <c:forEach items="${mealsTo}" var="meal">
        <tr style="
        <c:if test="${meal.excess eq true}">
                color: red
        </c:if>
        <c:if test="${meal.excess eq false}">
                color: green;
                </c:if>">
            <td>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/>
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>${meal.excess}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
