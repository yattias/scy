<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="dialog-page">
    <tiles:putAttribute name="main">


        <c:choose>
            <c:when test="${fn:length(elosToBeAssessed) > 0}">
                <table id="anchorElosTable" width="100%">
                    <tr>
                        <th>AnchorELO</th>
                        <th>ID</th>
                    </tr>
                    <c:forEach var="anchorElo" items="${elosToBeAssessed}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <a href="selectAnchorELOsToBePeerAssessed.html?anchorEloId=${anchorElo.id}&action=addToPedagogicalPlan&id=${pedagogicalPlan.id}">${anchorElo.humanReadableName}</a>
                            </td>
                            <td>
                                <a href="selectAnchorELOsToBePeerAssessed.html?anchorEloId=${anchorElo.id}&action=addToPedagogicalPlan&id=${pedagogicalPlan.id}">${anchorElo.name}</a>
                            </td>
                        </tr>

                    </c:forEach>
                </table>
            </c:when>
        </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>