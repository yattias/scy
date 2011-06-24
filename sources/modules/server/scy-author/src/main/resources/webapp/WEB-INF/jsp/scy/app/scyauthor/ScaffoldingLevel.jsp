<%@ include file="common-taglibs.jsp" %>
<fieldset>
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
