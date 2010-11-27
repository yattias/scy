<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
        <c:when test="${author}">

            <h1>Mission name: ${missionSpecificationTransporter.elo.title}</h1>


        </c:when>
        <c:otherwise>
            <h1>Mission name: ${missionSpecificationTransporter.elo.title}</h1>
            <table>
                <tr class="${oddEven.oddEven}">
                    <td>
                        <strong>Mission name</strong>
                    </td>                   
                    <td>
                        <s:ajaxELOTextField property="title" eloURI="${missionSpecificationTransporter.uri}" rooloServices="${rooloServices}"/>
                    </td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>
                        <strong>Assigned students</strong>
                    </td>
                    <td>
                        <a href="viewStudentsForPedagogicalPlan.html?eloURI=${missionSpecificationTransporter.uri}" >click</a>
                    </td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>
                        <strong>Scaffolding level</strong>
                    </td>
                    <td>
                        <s:ajaxELOSlider sliderValues="${agentLevels}" defaultValue="${scaffoldingLevel}" eloURI="${missionSpecificationTransporter.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/>
                    </td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td colspan="2">
                        <a href="MissionHighLevelOverview.html?missionSpecificationUri=${missionSpecificationTransporter.uri}">More details...</a>

                    </td>
                </tr>
            </table>

        </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>