<%@ include file="common-taglibs.jsp" %>

<p>
    <spring:message code="HELP_TEXT_LEARNING_GOALS"/>
</p>

<br/>
<p>
    <spring:message code="HELP_TEXT_LEARNING_GOALS_2"/>
</p>

.

<table>
    <tr>
        <td>
            <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addScorableLearningGoals&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                <c:if test="${pedagogicalPlan.assessmentSetup.useScorableLearningGoals}">
                        <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                    </c:if>
                    <c:if test="${!pedagogicalPlan.assessmentSetup.useScorableLearningGoals}">
                        <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                    </c:if>
                </a>
        </td>
        <td>
            Have student reflect over whether they have low, medium or high achievement of a goal
        </td>
    </tr>
    <tr>
        <td>
            <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addLearningGoalsWithCriteria&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
            <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
                    <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                </c:if>
                <c:if test="${!pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
                    <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                </c:if>
            </a>
        </td>
        <td>
            Have the student chose the achievement criteria for a goal
        </td>
    </tr>
</table>

<br/>
<h2><spring:message code="GENERAL_LEARNING_GOALS"/> </h2>

<c:choose>
    <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.generalLearningGoals) == 0}">
        <i><spring:message code="NO_GENERAL_LEARNING_GOALS_ADDED"/></i> <br>
    </c:when>
    <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.generalLearningGoals) > 0}">
            <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.generalLearningGoals}">
                <table>
                    <tr>
                        <td width="15%">
                            <strong><spring:message code="LEARNING_GOAL"/>:</strong>
                        </td>
                        <td>
                            <i><strong><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="goal"/></strong></i>
                        </td>
                        <td align="left" width="3%">
                            <s:ajaxTransferObjectCheckBox transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="use"/>
                        </td>
                        <td width="3%">
                            <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=deleteLearningGoal&learningGoalId=${learningGoal.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                            </a>
                        </td>
                    </tr>
                </table>
                <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
                        <c:choose>
                            <c:when test="${fn:length(learningGoal.learningGoalCriterias) > 0}">
                                <table>
                                    <tr  class="${oddEven.oddEven}">
                                        <td><strong><spring:message code="CRITERIUM"/></strong> </td>
                                        <td width="7%"><strong><spring:message code="LOW"/></strong></td>
                                        <td width="7%"><strong><spring:message code="MEDIUM"/></strong></td>
                                        <td width="7%"><strong><spring:message code="HIGH"/></strong></td>
                                        <td width="3%"></td>
                                    </tr>
                                    <c:forEach var="criteria" items="${learningGoal.learningGoalCriterias}">
                                        <tr  class="${oddEven.oddEven}">
                                            <td>
                                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${criteria}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${criteria.id}" property="criterium"/>
                                            </td>
                                            <td>
                                                <center>
                                                    <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=setLevelOnCriteria&criteriaId=${criteria.id}&learningGoalId=${learningGoal.id}&level=LOW&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                                        <c:if test="${criteria.level == 'LOW'}">
                                                            <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                        </c:if>
                                                        <c:if test="${criteria.level != 'LOW'}">
                                                            <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                        </c:if>
                                                    </a>
                                                </center>
                                            </td>
                                            <td>
                                                <center>
                                                    <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=setLevelOnCriteria&criteriaId=${criteria.id}&learningGoalId=${learningGoal.id}&level=MEDIUM&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                                        <c:if test="${criteria.level == 'MEDIUM'}">
                                                            <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                        </c:if>
                                                        <c:if test="${criteria.level != 'MEDIUM'}">
                                                            <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                        </c:if>
                                                    </a>
                                                </center>
                                            </td>
                                            <td>
                                                <center>
                                                    <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=setLevelOnCriteria&criteriaId=${criteria.id}&learningGoalId=${learningGoal.id}&level=HIGH&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                                        <c:if test="${criteria.level == 'HIGH'}">
                                                            <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                        </c:if>
                                                        <c:if test="${criteria.level != 'HIGH'}">
                                                            <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                        </c:if>
                                                    </a>
                                                </center>
                                            </td>
                                            <td>
                                                <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=deleteCriteria&criteriaId=${criteria.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                                    <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                                                </a>

                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:when>
                        </c:choose>
                    </c:if>
                <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
                    <table width="100%">
                        <tr>
                            <td align="right">
                                <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addCriteriaToGeneralLearningGoal&learningGoalId=${learningGoal.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));"><spring:message code="ADD_CRITERIA"/> </a>
                            </td>
                        </tr>
                    </table>
                </c:if>
                <br/>
            </c:forEach>

    </c:when>
</c:choose>
<a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addGeneralLearningGoal&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));"><spring:message code="ADD_GENERAL_LEARNING_GOAL"/> </a>        <br/>
<br/>
<h2><spring:message code="SPECIFIC_LEARNING_GOALS"/> </h2>



<c:choose>
    <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.specificLearningGoals) == 0}">
        <i><spring:message code="NO_SPECIFIC_LEARNING_GOALS_ADDED"/></i><br/>
    </c:when>


    <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.specificLearningGoals) > 0}">
            <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.specificLearningGoals}">
                <table>
                    <tr>
                        <td width="15%">
                            <strong><spring:message code="LEARNING_GOAL"/>:</strong>
                        </td>
                        <td>
                            <i><strong><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="goal"/></strong></i>
                        </td>
                        <td align="left" width="3%">
                            <s:ajaxTransferObjectCheckBox transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="use"/>
                        </td>
                        <td width="3%">
                            <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=deleteLearningGoal&learningGoalId=${learningGoal.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                            </a>
                        </td>

                    </tr>
                </table>
                    <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
                        <c:choose>
                            <c:when test="${fn:length(learningGoal.learningGoalCriterias) > 0}">
                                <table>
                                    <tr  class="${oddEven.oddEven}">
                                        <td><strong><spring:message code="CRITERIUM"/></strong> </td>
                                        <td width="7%"><strong><spring:message code="LOW"/></strong></td>
                                        <td width="7%"><strong><spring:message code="MEDIUM"/></strong></td>
                                        <td width="7%"><strong><spring:message code="HIGH"/></strong></td>
                                        <td width="3%"></td>
                                    </tr>
                                    <c:forEach var="criteria" items="${learningGoal.learningGoalCriterias}">
                                        <tr  class="${oddEven.oddEven}">
                                            <td>
                                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${criteria}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${criteria.id}" property="criterium"/>
                                            </td>
                                            <td>
                                                <center>
                                                    <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=setLevelOnCriteria&criteriaId=${criteria.id}&learningGoalId=${learningGoal.id}&level=LOW&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                                        <c:if test="${criteria.level == 'LOW'}">
                                                            <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                        </c:if>
                                                        <c:if test="${criteria.level != 'LOW'}">
                                                            <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                        </c:if>
                                                    </a>
                                                </center>
                                            </td>
                                            <td>
                                                <center>
                                                    <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=setLevelOnCriteria&criteriaId=${criteria.id}&learningGoalId=${learningGoal.id}&level=MEDIUM&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                                        <c:if test="${criteria.level == 'MEDIUM'}">
                                                            <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                        </c:if>
                                                        <c:if test="${criteria.level != 'MEDIUM'}">
                                                            <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                        </c:if>
                                                    </a>
                                                </center>
                                            </td>
                                            <td>
                                                <center>
                                                    <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=setLevelOnCriteria&criteriaId=${criteria.id}&learningGoalId=${learningGoal.id}&level=HIGH&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                                        <c:if test="${criteria.level == 'HIGH'}">
                                                            <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                        </c:if>
                                                        <c:if test="${criteria.level != 'HIGH'}">
                                                            <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                        </c:if>
                                                    </a>
                                                </center>
                                            </td>
                                            <td>
                                                <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=deleteCriteria&criteriaId=${criteria.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                                    <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:when>
                        </c:choose>
                    </c:if>
                <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
                    <table width="100%">
                        <tr>
                            <td align="right">
                                <a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addCriteriaToSpecificLearningGoal&learningGoalId=${learningGoal.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));"><spring:message code="ADD_CRITERIA"/> </a>
                            </td>
                        </tr>
                    </table>
                </c:if>
                <br/>
            </c:forEach>

    </c:when>
</c:choose>
<a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addSpecificLearningGoal&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));"><spring:message code="ADD_SPECIFIC_LEARNING_GOAL"/> </a>        <br/>



































