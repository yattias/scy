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


<c:choose>
    <c:when test="${fn:length(lasActivityList) > 0}">
        <table>
            <tr>
                <th>
                    <spring:message code="LAS"/>
                </th>
            </tr>
                <c:forEach var="lasActivityInfo" items="${lasActivityList}">
                    <tr>
                        <td>
                            ${lasActivityInfo.lasName}
                        </td>
                        <c:choose>
                            <c:when test="${fn:length(lasActivityInfo.activeUsers) > 0}">
                                <c:forEach var="activeUser" items="${lasActivityInfo.activeUsers}">
                                    <td>
                                        <img alt="${activeUser}" src="/webapp/common/filestreamer.html?username=${activeUser}&showIcon"/>
                                    </td>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </tr>
                </c:forEach>
        </table>
    </c:when>
</c:choose>

