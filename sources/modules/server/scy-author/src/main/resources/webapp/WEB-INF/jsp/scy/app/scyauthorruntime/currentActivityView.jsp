<%@ include file="common-taglibs.jsp" %>


<c:choose>
    <c:when test="${fn:length(userActivityList) > 0}">
        <table>

            <tr>
                <th>
                    <spring:message code="STUDENT"/>
                </th>
                <th>
                    <spring:message code="LAS"/>
                </th>
                <th>
                    Number of elos produced
                </th>
                <th>
                    Portfolio status
                </th>
            </tr>

        <c:forEach var="userActivityInfo" items="${userActivityList}">
            <tr>
                <td>
                    <img src="/webapp/common/filestreamer.html?username=${userActivityInfo.parsedUserName}&showIcon"/>
                    ${userActivityInfo.parsedUserName}
                </td>
                <td>
                    ${userActivityInfo.lasName}
                </td>
                <td>
                    ${userActivityInfo.numberOfElosProduced}
                </td>
                <td>
                    ${userActivityInfo.numberOfElosInPorfolio}/${fn:length(obligatoryElos)}
                </td>

            </tr>
        </c:forEach>
        </table>
    </c:when>
    <c:when test="${fn:length(userActivityList) == 0}">
        <spring:message code="NO_STUDENTS_CURRENTLY_LOGGED_ON"/>        
    </c:when>
</c:choose>


