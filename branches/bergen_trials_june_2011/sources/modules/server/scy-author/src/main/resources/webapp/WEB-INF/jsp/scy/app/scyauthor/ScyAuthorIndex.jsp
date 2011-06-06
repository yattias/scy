<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="main">

        <h1>My pedagogical plans</h1>
		<c:choose>
        <c:when test="${fn:length(missionTransporters) > 0}">
            <table id="pedagogicalPlansTable" width="100%">
                <tr>
                    <th>
                        Name
                    </th>
                </tr>
                <c:forEach var="missionTransporter" items="${missionTransporters}">
                    <tr class="${oddEven.oddEven}">
                        <td>
                            <a href="viewPedagogicalPlan.html?uri=${missionTransporter.uri}">${missionTransporter.elo.title}</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <br/>
            <!--div class="createNewPedPlan">
                <a href="createnewPedplansteps/PedPlanNameController.html">Create new pedagogical plan</a>
            </div-->

            

            <br>
        </c:when>
    </c:choose>

<br/>
<br/>
        <!--a href="viewCreateNewPedagogicalPlan.html">Create new pedagogical plan</a-->



        

	</tiles:putAttribute>
</tiles:insertDefinition>