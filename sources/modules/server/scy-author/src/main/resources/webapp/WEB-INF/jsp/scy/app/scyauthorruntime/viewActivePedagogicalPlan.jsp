<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
            <c:when test="${fn:length(users) > 0}">
                <table id="activityTable" width="100%">
                    <tr>
                        <th>Student</th>
                    </tr>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>
                                ${user.userDetails.username}
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>


    </tiles:putAttribute>
</tiles:insertDefinition>