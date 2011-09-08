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
                                            <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&action=addCriteria&learningGoalId=${learningGoal.id}&criteriaId=${criteria.id}&lgType=${learningGoalType}>
                                                ${criteria.criterium}
                                            </a>

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
            </c:forEach>
        </ul>
    </c:when>
</c:choose>
