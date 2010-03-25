<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="main">

        <h1>My pedagogical plans</h1>
		<c:choose>
        <c:when test="${fn:length(pedagogicalPlans) > 0}">
            <table id="pedagogicalPlansTable" border="2" width="100%">
                <tr>
                    <th>
                        Name
                    </th>
                    <th>
                        Description
                    </th>
                    <th>
                        Published
                    </th>
                </tr>
                <c:forEach var="pedagogicalPlan" items="${pedagogicalPlans}">
                    <tr class="${oddEven.oddEven}">
                        <td><a href="viewPedagogicalPlan.html?id=${pedagogicalPlan.id}">${pedagogicalPlan.name}</a></td>
                        <td>${pedagogicalPlan.description}</td>
                        <td><s:ajaxCheckBox model="${pedagogicalPlan}" property="published"/></td>
                    </tr>
                </c:forEach>
            </table>
            <br>
        </c:when>
    </c:choose>

<br/>
<br/>
        <a href="viewCreateNewPedagogicalPlan.html">Create new pedagogical plan</a>
	</tiles:putAttribute>
</tiles:insertDefinition>