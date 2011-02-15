<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Configure assessment for ${pedagogicalPlan.name}</h1>

        <table>
            <tr>
                <td>
                    Name
                </td>
                <td>
                    <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${pedagogicalPlan}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${pedagogicalPlan.id}" property="name"/>
                </td>
            </tr>
        </table>
        <br/>
        <br/>
        <h2>General Learning goals</h2>

        <c:choose>


            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.generalLearningGoals) > 0}">
                <table>
                    <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.generalLearningGoals}">
                        <tr>
                            <td>
                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="goal"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
        <a href="ConfigureAssessment.html?action=addGeneralLearningGoal&eloURI=${missionSpecificationEloURI}">Add general learning goal</a>
        <br/>
        <br/>
        <h2>Specific Learning goals</h2>

        <c:choose>


            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.specificLearningGoals) > 0}">
                <table>
                    <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.specificLearningGoals}">
                        <tr>
                            <td>
                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="goal"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
        <a href="ConfigureAssessment.html?action=addSpecificLearningGoal&eloURI=${missionSpecificationEloURI}">Add specific learning goal</a>




    </tiles:putAttribute>
</tiles:insertDefinition>