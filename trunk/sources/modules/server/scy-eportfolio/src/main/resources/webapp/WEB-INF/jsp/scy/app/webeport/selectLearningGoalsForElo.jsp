<%@ include file="common-taglibs.jsp" %>
<h2><spring:message code="SELECT_LEARNING_GOAL"/></h2>
<c:choose>
    <c:when test="${fn:length(learningGoals) > 0}">
        <table>
            <c:forEach var="learningGoal" items="${learningGoals}">
                <tr>
                    <td>
                        <a href="editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&lgType=learningGoalType&action=addLearningGoal&learningGoalId=${learningGoal.id}">${learningGoal.goal}</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>
