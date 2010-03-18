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
        <br/>

        <h2>Portfolio</h2>

        <table border="2" width="100%">
            <tr>
                <td>
                    Minimum number of AnchorELOs
                </td>
                <td>
                    <input type="text">
                </td>
            </tr>
            <tr>
                <td>
                    Maximum number of AnchorELOs
                </td>
                <td>
                    <input type="text">
                </td>
            </tr>
        </table>
        <br/>

        <h2>Anchor ELOs in portfolio</h2>
        <c:choose>
            <c:when test="${fn:length(anchorElos) > 0}">
                <table id="activityTable" border="2"  width="100%">
                    <tr>
                        <th>Can be included</th>
                        <th>Obligatory</th>
                        <th>Anchor ELO</th>

                    </tr>
                    <c:forEach var="anchorElo" items="${anchorElos}">
                        <tr>
                            <td><input type="checkbox"></td>
                            <td><input type="checkbox"></td>
                            <td>${anchorElo.name}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>








       
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