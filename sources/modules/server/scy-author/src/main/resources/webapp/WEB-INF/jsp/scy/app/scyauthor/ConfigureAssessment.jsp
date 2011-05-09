<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <h1>Configure assessment for ${pedagogicalPlan.name}</h1>   <a href="ConfigureAssessment.html?action=clearReflectionQuestions&eloURI=${missionSpecificationEloURI}">[clear reflection questions]</a>

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
        <h2>Reflection questions</h2>
        <c:choose>
            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.reflectionQuestions) > 0}">
                <table>
                    <tr>
                        <th width="20%">Anchor ELO name</th>
                        <th>Reflection question</th>
                    </tr>
                    <c:forEach var="reflectionQuestion" items="${pedagogicalPlan.assessmentSetup.reflectionQuestions}">
                        <tr>
                            <td>
                                ${reflectionQuestion.anchorEloName}
                            </td>
                            <td>
                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="reflectionQuestion"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
    <!--a href="ConfigureAssessment.html?action=addReflectionQuestion&eloURI=${missionSpecificationEloURI}&anchorEloURI=BAAAA">Add reflection question</a-->

        <br/><br/>

        <h2>Reflection tabs</h2>
        <c:choose>
            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.reflectionTabs) > 0}">
                <table>
                    <tr>
                        <th width="20%">Title</th>
                        <th>Question</th>
                        <th>Type</th>
                    </tr>
                    <c:forEach var="reflectionTab" items="${pedagogicalPlan.assessmentSetup.reflectionTabs}">
                        <tr>
                            <td>
                                ${reflectionTab.title}
                            </td>
                            <td>
                                ${reflectionTab.question}
                            </td>
                            <td>
                                ${reflectionTab.type}
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>





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