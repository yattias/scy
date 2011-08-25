<%@ include file="common-taglibs.jsp" %>
<c:choose>
    <c:when test="${fn:length(pedagogicalPlan.missionPlan.lasTransfers) > 0}">
        <table>
            <tr>
                <th>
                    <spring:message code="ANCHOR_ELO"/>
                </th>
                <th>
                    <spring:message code="LAS"/>
                </th>
                <th>
                    <spring:message code="OBLIGATORY_IN_PORTFOLIO"/>
                </th>
                <th>
                    <spring:message code="OWN_COMMENTS"/>
                </th>
            </tr>

            <c:forEach var="las" items="${pedagogicalPlan.missionPlan.lasTransfers}">
                <tr>
                    <td>
                        <div id="external${las.id}" dojoType="dijit.Dialog" title="${las.anchorElo.name}" href="${las.instructions}" style="overflow:auto; width: 400px; height: 400px;"> </div>
                        <a href="#" onClick="dijit.byId('external${las.id}').show();">${las.anchorElo.name}</a>
                    </td>
                    <td>
                        ${las.lasType}
                    </td>
                    <td>
                         <s:ajaxTransferObjectCheckBox transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${las.anchorElo}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${las.anchorElo.id}" property="obligatoryInPortfolio"/>
                    </td>
                    <td>
                        <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${las}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${las.id}" property="comments"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>

<a href="missionPlanner.html?eloURI=${eloURI}&action=reinitializePedagogicalPlan">Reinitialize plan</a>


