<%@ include file="common-taglibs.jsp" %>
        <p>
            Currently ${fn:length(users)} are assigned to this mission. Click link below to assign additional students.
        </p>

            

        <s:dialog url="selectStudentsForPedagogicalPlan.html" title="Click to assign another student" dialogHeader="Select students" extraParameters="eloURI=${eloURI.uri}"/>

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
                                <a href="/webapp/useradmin/manageAssignedStudent.html?username=${user.userDetails.username}&eloURI=${eloURI.uri}">${user.userDetails.username}</a>
                            </td>
                            <td>${user.userDetails.firstName} </td>
                            <td>${user.userDetails.lastName}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>
