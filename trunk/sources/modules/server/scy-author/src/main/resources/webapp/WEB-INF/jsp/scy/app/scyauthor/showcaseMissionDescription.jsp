<%@ include file="common-taglibs.jsp" %>
<div id="showcaseMissionDescription">

    <p>
        <spring:message code="SHOW_CASE_DESCRIPTION_INSTRUCTIONS"/>
    </p>

    <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${pedagogicalPlanTransfer}" transferEloURI="${pedagogicalPlanTransfer.pedagogicalPlanURI}" id="${pedagogicalPlanTransfer.id}" property="missionShowcaseDescription"/>

</div>
