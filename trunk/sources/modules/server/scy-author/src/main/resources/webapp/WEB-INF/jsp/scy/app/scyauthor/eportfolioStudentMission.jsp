<%@ include file="common-taglibs.jsp" %>
<div id="mission-student">
    <h2><spring:message code="REFLECTION_QUESTIONS_ON_MISSION"/></h2>
    <p>
        <spring:message code="STUDENT_MISSION_HELP"/>
    </p>
    <c:choose>
        <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.reflectionTabs) > 0}">
            <table>
                <tr>
                    <th width="20%">Title</th>
                    <th>Question</th>
                    <th>Type</th>
                    <th></th>
                </tr>
                <c:forEach var="reflectionTab" items="${pedagogicalPlan.assessmentSetup.reflectionTabs}">
                    <tr  class="${oddEven.oddEven}">
                        <td>
                            <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionTab}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionTab.id}" property="title"/>
                        </td>
                        <td>
                            <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionTab}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionTab.id}" property="question"/>
                        </td>
                        <td>
                            ${reflectionTab.type}
                        </td>
                        <td>
                            <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
    </c:choose>
    <br/>
    <a href="javascript:openPage('mission-student', 'eportfolioStudentMission.html?action=addReflectionQuestionOnMission&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">Add reflection question</a>        <br/>
    <br/>
</div>

