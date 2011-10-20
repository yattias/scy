<%@ include file="common-taglibs.jsp" %>
<div id="studentsForPedagogicalPlan">
        <p>
            Currently ${fn:length(users)} student(s) are assigned to this Mission. Click link below to assign additional students.
            <br/>
        </p>

            

        <s:dialog url="selectStudentsForPedagogicalPlan.html" title="Assign existing student" dialogHeader="Select students" extraParameters="eloURI=${eloURI}"/> |
        <s:dialog url="addNewStudent.html" title="Create new student" dialogHeader="Add student" extraParameters="eloURI=${eloURI}"/> |
        <a href="/webapp/teacher/studentList.html">User admin</a>

        <c:choose>
            <c:when test="${fn:length(users) > 0}">
                <table id="teachersTable" width="100%">
                    <tr>
                        <th></th>
                        <th></th>
                        <th>User name</th>
                        <th>First name</th>
                        <th>Last name</th>
                    </tr>
                    <c:forEach var="user" items="${users}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <s:deleteLink href="viewStudentsForPedagogicalPlan.html?id=${pedagogicalPlan.id}&action=removeStudent&username=${user.userDetails.username}" title="-" confirmText="Do you really want to remove ${user.userDetails.username}?" />
                            </td>
                            <td><img src="/webapp/common/filestreamer.html?username=${user.userDetails.username}&showIcon"/>
                            </td>
                            <td>
                                <a href="javascript:openPage('viewStudents', '/webapp/useradmin/manageAssignedStudent.html?eloURI=' + encodeURIComponent('${eloURI}') + '&username=${user.userDetails.username}');">
                                    ${user.userDetails.username}
                                </a>
                            </td>
                            <td>${user.userDetails.firstName} </td>
                            <td>${user.userDetails.lastName}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>
</div>