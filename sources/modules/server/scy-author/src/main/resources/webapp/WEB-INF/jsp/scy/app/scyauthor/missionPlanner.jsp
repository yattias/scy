<%@ include file="common-taglibs.jsp" %>
<c:choose>
    <c:when test="${fn:length(pedagogicalPlan.missionPlan.lasTransfers) > 0}">
        <table>
            <tr>
                <th>
                    <spring:message code="MINUTES"/>
                </th>
                <th>
                    <spring:message code="ANCHOR_ELO"/>
                </th>
                <th>
                    <spring:message code="LAS"/>
                </th>
                <th>
                    <spring:message code="OBLIGATORY_IN_PORTFOLIO"/>
                </th>
            </tr>

            <c:forEach var="las" items="${pedagogicalPlan.missionPlan.lasTransfers}">
                <tr>
                    <td>
                        <s:ajaxTransferObjectNumberField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${las}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${las.id}" property="minutesPlannedUsedInLas"/>

                    </td>
                    <td>
                        ${las.anchorElo.name}
                    </td>
                    <td>
                        ${las.name}
                    </td>
                    <td>
                         <s:ajaxTransferObjectCheckBox transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${las.anchorElo}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${las.anchorElo.id}" property="obligatoryInPortfolio"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>

