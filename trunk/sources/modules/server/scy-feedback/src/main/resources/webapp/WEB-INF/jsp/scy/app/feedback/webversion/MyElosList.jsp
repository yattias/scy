<%@ include file="common-taglibs.jsp" %>
MY elos here!


<c:choose>
    <c:when test="${fn:length(elos) > 0}">
        <table id="teachersTable" width="100%">
            <tr>
                <th>Name</th>
                <td>Format</td>
            </tr>
            <c:forEach var="elo" items="${elos}">
                <tr>
                    <td>
                        <a href="ViewFeedbackForElo.html?eloURI=${elo.uri}">${elo.myname}</a>
                    </td>
                    <td>
                        ${elo.technicalFormat}
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>
