<%@ include file="common-taglibs.jsp" %>
<div id="menuBar">
</div>
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


<script type="text/javascript">
    alert("SCRIPTS ARE ENABLED!");
</script>


        <a href="javascript:openPage('viewStudents', '/webapp/useradmin/manageAssignedStudent.html?eloURI=${eloURI}&username=${user.userDetails.username}&action=clearPortfolios');">
            <spring:message code="RESET_PORTFOLIO"/>
        </a><a href="javascript:openPage('viewStudents', '/webapp/app/scyauthor/viewStudentsForPedagogicalPlan.html?eloURI=${eloURI}');">
            <spring:message code="BACK_TO_STUDENT_LIST"/>
        </a>



