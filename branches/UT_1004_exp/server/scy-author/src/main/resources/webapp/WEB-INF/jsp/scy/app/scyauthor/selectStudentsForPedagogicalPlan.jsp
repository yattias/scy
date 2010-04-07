<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="dialog-page">
    <tiles:putAttribute name="main">

        <c:choose>
            <c:when test="${fn:length(students) > 0}">
                <table id="teachersTable" border="2" width="100%">
                    <tr>
                        <th></th>
                        <th>User name</th>
                        <th>First name</th>
                        <th>Last name</th>
                    </tr>
                    <c:forEach var="student" items="${students}">
                        <tr class="${oddEven.oddEven}">
                            <td><img src="/webapp/common/filestreamer.html?username=${student.userDetails.username}&showIcon"/>
                            </td>
                            <td>
                                <a href="viewPedagogicalPlan.html?action=addStudent&username=${student.userDetails.username}&id=${id}">${student.userDetails.username}</a>
                            </td>
                            <td>${student.userDetails.firstname} </td>
                            <td>${student.userDetails.lastname}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>