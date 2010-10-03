<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>${studentPlan.pedagogicalPlan.name}</h1>
        <div id="user_details">
            <img src="/webapp/common/filestreamer.html?username=${studentPlan.user.userDetails.username}&showIcon"/>&nbsp;<strong>${studentPlan.user.userDetails.firstName}&nbsp;${studentPlan.user.userDetails.lastName}</strong>
        </div>

        <c:choose>
            <c:when test="${fn:length(studentPlan.studentPlannedActivities) > 0}">
                <table id="studentPlanTable">
                    <tr>
                        <th>Planned Activity</th>
                        <th>Start date</th>
                        <th>End date</th>
                        <th>Note</th>
                        <th>Associated ELO</th>
                        <th>Activity</th>
                        <th>Input to LAS</th>
                        <th>Collaborators</th>
                    </tr>
                    <c:forEach var="studentPlannedActivity" items="${studentPlan.studentPlannedActivities}">
                        <tr class="${oddEven.oddEven}">
                            <td>${studentPlannedActivity.name}</td>
                            <td>${studentPlannedActivity.startDate}</td>
                            <td>${studentPlannedActivity.endDate}</td>
                            <td>${studentPlannedActivity.note}</td>
                            <td>${studentPlannedActivity.assoicatedELO.name}</td>
                            <td>${studentPlannedActivity.assoicatedELO.producedBy.name}</td>
                            <td>${studentPlannedActivity.assoicatedELO.inputTo.name}</td>

                            <td>

                                <c:forEach var="member" items="${studentPlannedActivity.members}">
                                    <table>
                                        <tr><td>
                                            <img src="/webapp/common/filestreamer.html?username=${member.userDetails.username}&showIcon"/>
                                            ${member.userDetails.username}
                                        </td></tr>
                                    </table>
                                </c:forEach>
                            </td>

                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>