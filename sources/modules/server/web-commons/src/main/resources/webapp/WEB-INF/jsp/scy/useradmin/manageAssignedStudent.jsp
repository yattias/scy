<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h2>${user.userDetails.firstName} ${user.userDetails.lastName}</h2>


        <c:choose>
            <c:when test="${fn:length(portfolios) > 0}">
                <c:forEach var="portfolio" items="${portfolios}">
                    <h1>Portfolio ${portfolio.missionName}</h1>
                    <table>
                        <c:choose>
                            <c:when test="${fn:length(portfolio.elos) > 0}">
                                <c:forEach var="elo" items="${portfolio.elos}">
                                    <tr class="${oddEven.oddEven}">
                                        <td>
                                            ${elo.catname}
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </table>
                    </c:forEach>
                </c:when>
        </c:choose>
        <a href="manageAssignedStudent.html?eloURI=${eloURI}&username=${user.userDetails.username}&action=clearPortfolios">
            Clear portfolios
        </a>


    </tiles:putAttribute>
</tiles:insertDefinition>