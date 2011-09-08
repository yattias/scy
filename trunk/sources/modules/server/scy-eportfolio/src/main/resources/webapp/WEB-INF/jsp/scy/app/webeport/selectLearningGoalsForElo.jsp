<%@ include file="common-taglibs.jsp" %>
<h2><spring:message code="SELECT_LEARNING_GOAL"/></h2>
<c:choose>
    <c:when test="${fn:length(learningGoals) > 0}">
        <ul>
            <c:forEach var="learningGoal" items="${learningGoals}">
                <strong><li>${learningGoal.goal}</li></strong>
                    <c:choose>
                        <c:when test="${fn:length(learningGoal.learningGoalCriterias) > 0}">
                            <table width="100%">
                                <c:forEach var="criteria" items="${learningGoal.learningGoalCriterias}">
                                    <tr align="left"  class="${oddEven.oddEven}">
                                        <td align="left">
                                            ${criteria.criterium}
                                        </td>
                                        <td align="left">
                                            <spring:message code="${criteria.level}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <br/>
                        </c:when>
                    </c:choose>
                

                    <!--/td>
                    <td align="center">
                        <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&lgType=${learningGoalType}&action=addLearningGoal&learningGoalId=${learningGoal.id}&value=LOW><spring:message code="LOW"/> </a>
                    </td>
                    <td align="center">
                        <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&lgType=${learningGoalType}&action=addLearningGoal&learningGoalId=${learningGoal.id}&value=MEDIUM><spring:message code="MEDIUM"/> </a>
                    </td>
                    <td align="center">
                        <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&lgType=${learningGoalType}&action=addLearningGoal&learningGoalId=${learningGoal.id}&value=HIGH><spring:message code="HIGH"/> </a>
                    </td>
                </tr-->
            </c:forEach>
        </ul>
    </c:when>
</c:choose>
