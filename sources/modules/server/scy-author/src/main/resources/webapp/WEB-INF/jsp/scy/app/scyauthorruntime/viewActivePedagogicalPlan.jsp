<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>${model.name}</h1>

        <c:choose>
            <c:when test="${fn:length(assignedPedagogicalPlans) > 0}">

                <h2>Current activity summary</h2>

                <table id="teachersTable" width="100%">
                    <tr>
                        <th></th>
                        <th>User name</th>
                        <th>First name</th>
                        <th>Last name</th>
                        <th>Current activity</th>
                        <th>Student plans</th>
                    </tr>
                    <c:forEach var="assignedPedagogicalPlan" items="${assignedPedagogicalPlans}">
                        <tr class="${oddEven.oddEven}">
                            <td><img src="/webapp/common/filestreamer.html?username=${assignedPedagogicalPlan.user.userDetails.username}&showIcon"/>
                            </td>
                            <td>
                                ${assignedPedagogicalPlan.user.userDetails.username}
                            </td>
                            <td>${assignedPedagogicalPlan.user.userDetails.firstname} </td>
                            <td>${assignedPedagogicalPlan.user.userDetails.lastname}</td>
                            <td><s:currentStudentActivity/></td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>




    </tiles:putAttribute>
</tiles:insertDefinition>