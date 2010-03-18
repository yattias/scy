<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Pedagogical Plan: ${pedagogicalPlan.name}</h1>


        <table width="100%" border="2">
            <tr>
                <td>
                    Scenario
                </td>
                <td>
                    <a href="viewScenario.html?scenarioId=${pedagogicalPlan.scenario.id}&pedagogicalPlanId=${pedagogicalPlan.id}">${pedagogicalPlan.scenario.name}</a>
                </td>
                <td>
                    ${pedagogicalPlan.scenario.description}
                </td>
            </tr>
            <tr>
                <td>
                    Mission
                </td>
                <td>
                    ${pedagogicalPlan.mission.name}
                </td>
                <td>
                    ${pedagogicalPlan.mission.description}
                </td>
            </tr>
        </table>



       
        <c:choose>
            <c:when test="${!pedagogicalPlan.published}">
                <a href="viewPedagogicalPlan.html?id=${pedagogicalPlan.id}&publish=true">Publish</a>
            </c:when>
            <c:when test="${pedagogicalPlan.published}">
                <a href="viewPedagogicalPlan.html?id=${pedagogicalPlan.id}&publish=false">Unpublish</a>
            </c:when>
        </c:choose>

        <a href="viewAgents.html?pedagogicalPlanId=${pedagogicalPlan.id}">View agents</a>
    </tiles:putAttribute>
</tiles:insertDefinition>