<%@ include file="common-taglibs.jsp" %>
<h2>General Learning goals</h2>

<c:choose>


    <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.generalLearningGoals) > 0}">
        <table>
            <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.generalLearningGoals}">
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
<a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addGeneralLearningGoal&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">Add general learning goal</a>        <br/>
<br/>
<h2>Specific Learning goals</h2>

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
<a href="javascript:openPage('learningGoalsConfiguration', 'LearningGoals.html?action=addSpecificLearningGoal&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">Add specific learning goal</a>

