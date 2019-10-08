
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Andrew
  Date: 07.10.2019
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title">Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2 align="center">Meals</h2>


<table border="5" width="900" align="center" bordercolor="black">
    <tr>
        <th width="300">Date</th>
        <th width="300">Desc</th>
        <th width="300">Calories</th>
    </tr>
<c:forEach items="${meals}" var="meal">

       <c:if test="${meal.isExcess()}">
           <tr bgcolor=#dc143c align="center" valign="top">
       </c:if>
           <c:if test="${!meal.isExcess()}">
           <tr bgcolor=#006400 align="center" valign="top">
               </c:if>
    <td>${meal.getDateTime().toString().replaceAll("T", " ")}</td>
    <td>${meal.getDescription()}</td>
    <td>${meal.getCalories()}</td>
   </tr>

</c:forEach>
</table>
</body>
</html>
