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
                    <spring:message code="TOOL"/>
                </th>
                <th>
                    <spring:message code="NUMBER_OF_ELOS"/>
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
                    ${userActivityInfo.toolName}
                </td>
                <td>
                    ${userActivityInfo.numberOfElosProduced}
                </td>

            </tr>
        </c:forEach>
        </table>
    </c:when>
</c:choose>


