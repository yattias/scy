<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

    <h1>Create new Pedagogical plan</h1>

        <c:choose>
        <c:when test="${fn:length(pedagogicalPlans) > 0}">
            <table id="pedagogicalPlansTable">
                <tr>
                    <th>
                        Name
                    </th>
                    <th>
                        Description
                    </th>
                </tr>
                <c:forEach var="pedagogicalPlan" items="${pedagogicalPlans}">
                    <tr>
                        <td><a href="viewPedagogicalPlan.html?id=${pedagogicalPlan.id}">${pedagogicalPlan.name}</a></td>
                        <td>${pedagogicalPlan.description}</td>
                    </tr>
                </c:forEach>
            </table>
            <br>
        </c:when>
    </c:choose>


    </tiles:putAttribute>
</tiles:insertDefinition>
