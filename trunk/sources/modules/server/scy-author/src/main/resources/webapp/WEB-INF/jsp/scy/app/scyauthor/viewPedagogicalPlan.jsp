<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
        <c:when test="${author}">

            AUTHOR
            <h1>Pedagogical Plan: ${missionSpecificationTransporter.elo.title}</h1>


        </c:when>
        <c:otherwise>

            TEACHER
            <h1>Pedagogical Plan: ${missionSpecificationTransporter.elo.title}</h1>
            <table>
                <tr class="${oddEven.oddEven}">
                    <td>
                        <strong>Mission name</strong>
                    </td>                   '
                    <td>
                        <s:ajaxELOTextField property="globalMissionScaffoldingLevel" eloURI="${missionSpecificationTransporter.uri}"/>
                    </td>
                </tr> <tr class="${oddEven.oddEven}">
                    <td>
                        <strong>Scaffolding level</strong>
                    </td>                   '
                    <td>
                        ${missionGlobalScaffoldingLevel}  <a href="viewPedagogicalPlan.html?uri=${missionSpecificationTransporter.uri}&action=increaseScaffoldingLevel">+</a>
                    </td>
                </tr>
            </table>

        </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>