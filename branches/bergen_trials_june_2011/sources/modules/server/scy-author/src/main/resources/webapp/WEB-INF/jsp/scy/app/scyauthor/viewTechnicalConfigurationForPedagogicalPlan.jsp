<%@ include file="common-taglibs.jsp" %>

<spring:message code="JNLP_PROPERTIES"/>
<c:choose>
    <c:when test="${fn:length(pedagogicalPlan.technicalInfo.jnlpProperties) > 0}">
        <table>
            <tr>
                <th>
                    <spring:message code="PROPERTY_NAME"/>
                </th>
                <th>
                    <spring:message code="PROPERTY_VALUE"/>
                </th>
            </tr>

            <c:forEach var="jnlpProperty" items="${pedagogicalPlan.technicalInfo.jnlpProperties}">
                <tr>
                    <td>
                        <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${jnlpProperty}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${jnlpProperty.id}" property="name"/>
                    </td>
                    <td>
                        <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${jnlpProperty}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${jnlpProperty.id}" property="value"/>
                    </td>
                </tr>
            </c:forEach>
        </c:when>
    </c:choose>

    <a href="viewTechnicalConfigurationForPedagogicalPlan.html?action=addProperty&eloURI=${missionSpecificationEloURI}">[ADD PROPERTY]</a> |
    <a href="viewTechnicalConfigurationForPedagogicalPlan.html?action=clearProperties&eloURI=${missionSpecificationEloURI}">[CLEAR PROPERTIES]</a>
