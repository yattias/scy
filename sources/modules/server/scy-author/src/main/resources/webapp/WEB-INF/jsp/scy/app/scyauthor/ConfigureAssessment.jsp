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

        <h2>Learning goals URI: ${missionSpecificationEloURI}</h2>

        <c:choose>


            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.generalLearningGoals) > 0}">
                <table>
                    <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.generalLearningGoals}">
                        <tr>
                            <td>
                                Learning goal here
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.generalLearningGoals) == 0}">
                <a href="ConfigureAssessment.html?action=addGeneralLearningGoal&eloURI=${missionSpecificationEloURI}">Add general learning goal</a>
            </c:when>

        </c:choose>




    </tiles:putAttribute>
</tiles:insertDefinition>