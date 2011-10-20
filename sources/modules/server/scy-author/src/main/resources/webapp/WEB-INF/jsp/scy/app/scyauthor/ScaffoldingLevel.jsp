<%@ include file="common-taglibs.jsp" %>
<fieldset>
    <h2><spring:message code="USE_THE_SLIDE_BAR_TO_SET_THE_AGENT_SCAFFOLDING_LEVEL"/> </h2>
    <table>
        <tr>
            <td>
                <strong><spring:message code="SCAFFOLDING_LEVEL"/></strong>
            </td>
            <td>
                <s:ajaxELOSlider sliderValues="${agentLevels}" defaultValue="${scaffoldingLevel}" eloURI="${missionSpecificationTransporter.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/>
            </td>
        </tr>
    </table>
</fieldset>
