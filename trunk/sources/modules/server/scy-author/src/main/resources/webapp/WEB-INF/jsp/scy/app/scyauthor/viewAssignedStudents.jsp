<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <c:choose>
            <c:when test="${fn:length(students) > 0}">
                <table id="activityTable" width="100%">
                    <tr>
                        <th>Student</th>
                    </tr>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>
                                <s:studentEditor username="${student.userDetails.username}"/>
                                <!--div id="user_details">
                                    <img src="/webapp/common/filestreamer.html?username=${student.userDetails.username}&showIcon"/>&nbsp;<strong>${student.userDetails.firstName}&nbsp;${student.userDetails.lastName}</strong>
                                </div-->
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>


    </tiles:putAttribute>
</tiles:insertDefinition>