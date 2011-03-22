<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="main">
		<c:choose>
        <c:when test="${fn:length(pedagogicalPlans) > 0}">
            <table id="pedagogicalPlansTable" border="2">
                <h5>Pedagogical plans</h5>
                <tr>
                    <th></th>
                </tr>
                <c:forEach var="pedagogicalPlan" items="${pedagogicalPlans}">
                    <tr>
                        <td><a href="viewPedagogicalPlan.html?id=${pedagogicalPlan.id}">${pedagogicalPlan.name}</a></td>
                    </tr>
                </c:forEach>
            </table>
            <br>
        </c:when>
    </c:choose>
	</tiles:putAttribute>
</tiles:insertDefinition>