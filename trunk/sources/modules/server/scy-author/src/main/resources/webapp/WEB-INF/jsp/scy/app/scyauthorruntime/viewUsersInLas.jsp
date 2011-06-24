<%@ include file="common-taglibs.jsp" %>
<c:choose>
    <c:when test="${fn:length(lasActivityList) > 0}">
        <table>
            <tr>
                <th>
                    <spring:message code="LAS"/>
                </th>
                <c:forEach var="j" begin="1" end="${highestNumberOfActiveUsers}">
                    <th></th>
                </c:forEach>
            </tr>
                <c:forEach var="lasActivityInfo" items="${lasActivityList}">
                    <tr>
                        <td>
                            ${lasActivityInfo.humanReadableName}
                        </td>
                        <c:choose>
                            <c:when test="${fn:length(lasActivityInfo.activeUsers) > 0}">
                                <c:forEach var="activeUser" items="${lasActivityInfo.activeUsers}">
                                    <td>
                                        <table width="100%">
                                            <tr>
                                                <td colspan="2">
                                                    <img alt="${activeUser}" src="/webapp/common/filestreamer.html?username=${activeUser}&showIcon"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    ${activeUser}
                                                </td>
                                                <td>
                                                    ${activeUser}
                                                </td>
                                            </tr>

                                        </table>
                                    </td>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </tr>
                </c:forEach>
        </table>
    </c:when>
</c:choose>

