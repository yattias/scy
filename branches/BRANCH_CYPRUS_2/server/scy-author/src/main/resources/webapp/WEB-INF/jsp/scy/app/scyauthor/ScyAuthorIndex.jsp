<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="main">
         
        <h1>My pedagogical plans</h1>
		<c:choose>
        <c:when test="${fn:length(pedagogicalPlans) > 0}">
            <table id="pedagogicalPlansTable" width="100%">
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
                        <td>
                            <s:modellink model="${pedagogicalPlan}" href="viewPedagogicalPlan.html">${pedagogicalPlan.name}</s:modellink>
                        </td>
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
        <!--a href="viewCreateNewPedagogicalPlan.html">Create new pedagogical plan</a-->

        <div class="createNewPedPlan">
            <a href="createnewPedplansteps/PedPlanNameController.html">Create new pedagogical plan</a>
        </div>


        

	</tiles:putAttribute>
</tiles:insertDefinition>