<%@ include file="common-taglibs.jsp" %>
<h2><spring:message code="SELECT_LEARNING_GOAL"/></h2>
<c:choose>
    <c:when test="${fn:length(learningGoals) > 0}">
        <table>
            <tr>
                <th></th>
                <th><spring:message code="LOW"/></th>
                <th><spring:message code="MEDIUM"/></th>
                <th><spring:message code="HIGH"/></th>

            </tr>
            <c:forEach var="learningGoal" items="${learningGoals}">
                <tr>
                    <td align="left">
                        ${learningGoal.goal}
                    </td>
                    <td align="center">
                        <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&lgType=${learningGoalType}&action=addLearningGoal&learningGoalId=${learningGoal.id}&value=LOW><spring:message code="LOW"/> </a>
                    </td>
                    <td align="center">
                        <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&lgType=${learningGoalType}&action=addLearningGoal&learningGoalId=${learningGoal.id}&value=MEDIUM><spring:message code="MEDIUM"/> </a>
                    </td>
                    <td align="center">
                        <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&lgType=${learningGoalType}&action=addLearningGoal&learningGoalId=${learningGoal.id}&value=HIGH><spring:message code="HIGH"/> </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>
