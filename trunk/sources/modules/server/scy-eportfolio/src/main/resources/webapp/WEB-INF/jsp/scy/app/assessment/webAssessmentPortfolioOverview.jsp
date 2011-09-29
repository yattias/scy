<%@ include file="common-taglibs.jsp" %>

<p>
    <spring:message code="PORTFOLIO_OVERVIEW_HELP_TEXT"/>
</p>

<c:choose>
    <c:when test="${fn:length(transporters) > 0}">
        <table>
        <c:forEach var="transporter" items="${transporters}">
            <tr>
                <td>
                    <a href="/webapp/app/assessment/webAssessmentIndex.html?missionRuntimeURI=${transporter.encodedMissionURI}">${transporter.user.userDetails.firstName}</a>
                </td>
                <td>
                    ${transporter.user.userDetails.lastName}
                </td>
                <td>
                    <spring:message code="${transporter.portfolio.portfolioStatus}"/>
                </td>
            </tr>
        </c:forEach>
        </table>
    </c:when>
</c:choose>

