<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>WELCOME TO SCYAuthor Runtime!</h1>

        <c:choose>
        <c:when test="${fn:length(pedagogicalPlans) > 0}">
            <table id="pedagogicalPlansTable" border="2">
                <tr>
                    <th>Pedagogical Plan</th>
                    <th>Active</th>
                    <th>Number of online students</th>
                </tr>
                <c:forEach var="pedagogicalPlan" items="${pedagogicalPlans}">
                    <tr>
                        <td><a href="viewActivePedagogicalPlan.html?id=${pedagogicalPlan.id}">${pedagogicalPlan.name}</a></td>
                        <td>${pedagogicalPlan.published}</td>
                        <td>6</td>
                    </tr>
                </c:forEach>
            </table>
            <br>
        </c:when>
    </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>