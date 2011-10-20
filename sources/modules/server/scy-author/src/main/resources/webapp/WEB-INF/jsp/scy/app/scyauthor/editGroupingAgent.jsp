<%@ include file="common-taglibs.jsp" %>

<h2><spring:message code="SET_THE_GROUPING_PARAMETERS"/> </h2>

<table>
    <tr  class="${oddEven.oddEven}">
        <td width="50%">
            <strong><spring:message code="GROUPING_AGENT_PERCENT"/> </strong>
        </td>
        <td>
            <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${pedagogicalPlanTransfer}" transferEloURI="${pedagogicalPlanTransfer.pedagogicalPlanURI}" id="${pedagogicalPlanTransfer.id}" property="groupingAgentMinimumUsers"/>
        </td>
    </tr>
    <tr  class="${oddEven.oddEven}">
        <td>
            <strong><spring:message code="MAX_NUMBER_OF_USERS_IN_GROUP"/> </strong>
        </td>
        <td>
            <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${pedagogicalPlanTransfer}" transferEloURI="${pedagogicalPlanTransfer.pedagogicalPlanURI}" id="${pedagogicalPlanTransfer.id}" property="groupingAgentMaximumUsers"/>
        </td>
    </tr>
    <tr  class="${oddEven.oddEven}">
        <td>
            <strong><spring:message code="MIN_NUMBER_OF_USERS_IN_GROUP"/> </strong>
        </td>
        <td>
            <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${pedagogicalPlanTransfer}" transferEloURI="${pedagogicalPlanTransfer.pedagogicalPlanURI}" id="${pedagogicalPlanTransfer.id}" property="groupingAgentPercent"/>
        </td>
    </tr>
</table>

