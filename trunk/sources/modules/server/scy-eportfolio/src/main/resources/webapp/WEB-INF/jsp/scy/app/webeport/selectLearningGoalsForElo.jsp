<%@ include file="common-taglibs.jsp" %>
<div id="selectLearningGoal">
<c:choose>
    <c:when test="${fn:length(learningGoals) > 0}">
        <c:if test="${pedagogicalPlan.assessmentSetup.useOnlyLearningGoals}">
            <p>
                <spring:message code="HELP_TEXT_CLICK_ON_LEARNING_GOAL"/> 

            </p>
            <table>

            </table>
            <c:forEach var="learningGoal" items="${learningGoals}">
                <tr  class="${oddEven.oddEven}_eport">
                    <td align="left">
                        <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&action=addLearningGoal&learningGoalId=${learningGoal.id}&lgType=${learningGoalType}>
                            <img src="/webapp/themes/scy/default/images/green.png" alt="<spring:message code="HIGH"/>"/> ${learningGoal.goal}
                        </a>

                    </td>
                </tr>

            </c:forEach>

        </c:if>
        <c:if test="${pedagogicalPlan.assessmentSetup.useScorableLearningGoals}">
            <p>
                <spring:message code="HELP_TEXT_SELECT_SCORABLE_LEARNING_GOAL"/>
            </p>
            <table>
                <tr>
                    <td></td>
                    <td><strong><spring:message code="LOW"/></strong></td>
                    <td><strong><spring:message code="MEDIUM"/></strong></td>
                    <td><strong><spring:message code="HIGH"/></strong></td>
                </tr>
                <c:forEach var="learningGoal" items="${learningGoals}">
                    <tr>
                        <td align="left">
                            <a href="javascript:openPage('selectLearningGoal', 'selectLearningGoalsForElo.html?action=selectLearningGoal&learningGoalId=${learningGoal.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                ${learningGoal.goal}
                            </a>

                        </td>
                        <td>
                            <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&action=addLearningGoal&learningGoalId=${learningGoal.id}&level=LOW&lgType=${learningGoalType}>
                                <img src="/webapp/themes/scy/default/images/red.png" alt="<spring:message code="LOW"/>"  />
                            </a>
                        </td>
                        <td>
                            <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&action=addLearningGoal&learningGoalId=${learningGoal.id}&level=MEDIUM&lgType=${learningGoalType}>
                                <img src="/webapp/themes/scy/default/images/yellow.png" alt="<spring:message code="MEDIUM"/>"  />
                            </a>
                        </td>
                        <td>
                            <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&action=addLearningGoal&learningGoalId=${learningGoal.id}&level=HIGH&lgType=${learningGoalType}>
                                <img src="/webapp/themes/scy/default/images/green.png" alt="<spring:message code="HIGH"/>"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>

        </c:if>





        <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
            <p>
                <spring:message code="HELP_TEXT_CLICK_ON_CRITERIA_TO_SELECT"/>
            </p>
            <ul>
                <c:forEach var="learningGoal" items="${learningGoals}">
                    <a href="javascript:openPage('selectLearningGoal', 'selectLearningGoalsForElo.html?action=selectLearningGoal&learningGoalId=${learningGoal.id}&eloURI=' + encodeURIComponent('${eloURI}'));">
                        <strong><li>${learningGoal.goal}</li></strong>
                    </a>
                        <c:choose>
                            <c:when test="${fn:length(learningGoal.learningGoalCriterias) > 0}">
                                <table width="100%">
                                    <c:forEach var="criteria" items="${learningGoal.learningGoalCriterias}">
                                        <tr align="left"  class="${oddEven.oddEven}">
                                            <td align="left" width="3%">

                                                <c:if test="${criteria.level =='LOW'}">
                                                    <img src="/webapp/themes/scy/default/images/red.png" alt="<spring:message code="LOW"/>"  />
                                                </c:if>
                                                <c:if test="${criteria.level =='MEDIUM'}">
                                                    <img src="/webapp/themes/scy/default/images/yellow.png" alt="<spring:message code="MEDIUM"/>"  />
                                                </c:if>
                                                <c:if test="${criteria.level =='HIGH'}">
                                                    <img src="/webapp/themes/scy/default/images/green.png" alt="<spring:message code="HIGH"/>"/>
                                                </c:if>
                                            </td>
                                            <td width="20%">
                                                <spring:message code="${criteria.level}"/>
                                            </td>
                                            <td align="left">
                                                <a href=/webapp/app/webeport/editEloReflections.html?eloURI=${eloURI}&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}&action=addCriteria&learningGoalId=${learningGoal.id}&criteriaId=${criteria.id}&lgType=${learningGoalType}>
                                                    ${criteria.criterium}
                                                </a>

                                            </td>

                                        </tr>
                                    </c:forEach>
                                </table>
                                <br/>
                            </c:when>
                        </c:choose>
                </c:forEach>
            </ul>

        </c:if>

    </c:when>
</c:choose>
</div>