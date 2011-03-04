<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h2>${user.userDetails.firstName} ${user.userDetails.lastName}</h2>


        <c:choose>
            <c:when test="${fn:length(portfolios) > 0}">
                <h1>Portfolio</h1>
                <table>
                <c:forEach var="anchorElo" items="${anchorElos}">
                    <tr class="${oddEven.oddEven}">
                        <td>
                            
                        </td>
                    </tr>
                </c:forEach>
                </table>
            </c:when>
        </c:choose>


    </tiles:putAttribute>
</tiles:insertDefinition>