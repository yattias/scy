<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>${model.name}</h1>

        <c:choose>
            <c:when test="${fn:length(assignedPedagogicalPlans) > 0}">

                <h2>Current activity summary</h2>

                <table id="teachersTable" width="100%">
                    <tr>
                        <th width="16%"></th>
                        <th width="17%">User name</th>
                        <th width="17%">First name</th>
                        <th width="17%">Last name</th>
                        <th width="16%">Current activity</th>
                        <th width="17%">Student plans</th>
                    </tr>
                    <c:forEach var="assignedPedagogicalPlan" items="${assignedPedagogicalPlans}">
                        <tr class="${oddEven.oddEven}">
                            <td><img src="/webapp/common/filestreamer.html?username=${assignedPedagogicalPlan.assignedPedagogicalPlan.user.userDetails.username}&showIcon"/>
                            </td>
                            <td>
                                ${assignedPedagogicalPlan.assignedPedagogicalPlan.user.userDetails.username}
                            </td>
                            <td>${assignedPedagogicalPlan.assignedPedagogicalPlan.user.userDetails.firstname} </td>
                            <td>${assignedPedagogicalPlan.assignedPedagogicalPlan.user.userDetails.lastname}</td>
                            <td><s:currentStudentActivity username="${assignedPedagogicalPlan.assignedPedagogicalPlan.user.userDetails.username}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:length(assignedPedagogicalPlan.studentPlans) > 0}">
                                         <table>
                                             <c:forEach var="sp" items="${assignedPedagogicalPlan.studentPlans}">
                                                 <tr>
                                                     <td>
                                                     <td><a href="viewStudentPlan.html?studentPlanId=${sp.id}">${sp.pedagogicalPlan.name}</a></td>
                                                 </tr>
                                             </c:forEach>
                                         </table>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>