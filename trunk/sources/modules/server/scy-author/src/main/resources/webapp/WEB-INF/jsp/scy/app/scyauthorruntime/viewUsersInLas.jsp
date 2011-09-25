<%@ include file="common-taglibs.jsp" %>
<c:choose>
    <c:when test="${fn:length(lasActivityList) > 0}">
        <c:forEach var="lasActivityInfo" items="${lasActivityList}">
                    <table>
                    <tr>
                        <th colspan="3">
                            ${lasActivityInfo.humanReadableName}
                        </th>
                        <c:choose>
                            <c:when test="${fn:length(lasActivityInfo.activeUsers) > 0}">
                                <c:forEach var="activeUser" items="${lasActivityInfo.activeUsers}">
                                    <tr>
                                        <td>
                                            <img alt="${activeUser}" src="/webapp/common/filestreamer.html?username=${activeUser.userDetails.username}&showIcon"/>
                                        </td>
                                        <td>
                                            ${activeUser.userDetails.firstName}
                                        </td>
                                        <td>
                                            ${activeUser.userDetails.lastName}
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                </c:forEach>
        </table>
    </c:when>
    <c:when test="${fn:length(lasActivityList) == 0}">
        <spring:message code="NO_STUDENTS_CURRENTLY_LOGGED_ON"/>
    </c:when>
</c:choose>

