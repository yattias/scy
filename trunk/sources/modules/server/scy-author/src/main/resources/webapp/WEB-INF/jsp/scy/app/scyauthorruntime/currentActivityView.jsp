<%@ include file="common-taglibs.jsp" %>


<c:choose>
    <c:when test="${fn:length(userActivityList) > 0}">
        <table>

            <tr>
                <th>
                    User
                </th>
                <th>
                    Language
                </th>
            </tr>

        <c:forEach var="userActivityInfo" items="${userActivityList}">
            <tr>
                <td>
                    ${userActivityInfo.userName}
                </td>
                <td>
                    ${userActivityInfo.language}
                </td>
            </tr>
        </c:forEach>
        </table>
    </c:when>
</c:choose>

