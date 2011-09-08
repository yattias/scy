<%@ include file="common-taglibs.jsp" %>
<h2><spring:message code="GENERAL_LEARNING_GOALS"/></h2>

<c:choose>


    <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.generalLearningGoals) > 0}">
        <table>
            <tr>
                <td>
                    <spring:message code="USE"/>
                </td>
                <td>
                    <spring:message code="LEARNING_GOAL"/>
                </td>
                <td>
                    <spring:message code="CRITERIA"/>
                </td>
            </tr>
            <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.generalLearningGoals}">
                <tr  class="${oddEven.oddEven}">
                    <td width="5%">
                        <s:ajaxTransferObjectCheckBox transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="use"/>
                    </td>
                    <td>
                        <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="goal"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${fn:length(learningGoal.learningGoalCriterias) > 0}">
                                <li>
                                    <c:forEach var="criteria" items="${learningGoal.learningGoalCriterias}">
                                        <ul>
                                            <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${criteria}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${criteria.id}" property="criterium"/>
                                            --- ${criteria.criterium}
                                        </ul>
                                    </c:forEach>
                                </li>
                            </c:when>
                        </c:choose>
                        <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addCriteriaToGeneralLearningGoal&learningGoalId=${learningGoal.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));"><spring:message code="ADD_CRITERIA"/> </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>
<a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addGeneralLearningGoal&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));"><spring:message code="ADD_GENERAL_LEARNING_GOAL"/> </a>        <br/>
<br/>
<h2><spring:message code="SPECIFIC_LEARNING_GOALS"/> </h2>

<c:choose>


    <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.specificLearningGoals) > 0}">
        <table>
            <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.specificLearningGoals}">
                <tr  class="${oddEven.oddEven}">
                    <td width="5%">
                        <s:ajaxTransferObjectCheckBox transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="use"/>
                    </td>
                    <td>
                        <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="goal"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>
<a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addSpecificLearningGoal&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));"><spring:message code="ADD_SPECIFIC_LEARNING_GOAL"/></a>

