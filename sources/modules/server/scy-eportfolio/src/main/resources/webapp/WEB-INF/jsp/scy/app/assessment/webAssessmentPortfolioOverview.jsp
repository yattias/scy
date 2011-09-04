<%@ include file="common-taglibs.jsp" %>


Number of portfolios ready to be assessed: ${fn:length(portfolios)}


<c:choose>
    <c:when test="${fn:length(portfolios) > 0}">
        <table>
        <c:forEach var="portfolio" items="${portfolios}">
            <tr>
                <td>
                    ${portfolio.owner}
                </td>
                <td>
                    <spring:message code="${portfolio.portfolioStatus}"/>
                </td>
            </tr>
        </c:forEach>
        </table>
    </c:when>
</c:choose>

