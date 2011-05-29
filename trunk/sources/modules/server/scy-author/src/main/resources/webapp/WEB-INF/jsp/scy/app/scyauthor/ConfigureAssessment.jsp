<%@ include file="common-taglibs.jsp" %>

        <h2>Reflection questions</h2>

        <div id="reflectionQuestionHelp" dojoType="dijit.Dialog" title="Reflection questions">
            <p id="reflectionQuestionHelp">Reflection questions are used in the eportfolio tool. Students will be asked these questions for each ELO type they have added to their eportfolio.</p>
        </div>
        <button id="buttonOne" dojoType="dijit.form.Button" type="button">
            Help
            <script type="dojo/method" event="onClick" args="evt">
                // Show the Dialog:
                dijit.byId("reflectionQuestionHelp").show();
            </script>
        </button>


        <c:choose>
            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.reflectionQuestions) > 0}">
                <table>
                    <tr>
                        <th width="20%">Anchor ELO name</th>
                        <th>Reflection question</th>
                    </tr>
                    <c:forEach var="reflectionQuestion" items="${pedagogicalPlan.assessmentSetup.reflectionQuestions}">
                        <tr  class="${oddEven.oddEven}">
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
        <a href="ConfigureAssessment.html?action=clearReflectionQuestions&eloURI=${missionSpecificationEloURI}">[clear reflection questions]</a>

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
                        <tr  class="${oddEven.oddEven}">
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
        <br/>
        <br/>




        <h2>General Learning goals</h2>

        <c:choose>


            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.generalLearningGoals) > 0}">
                <table>
                    <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.generalLearningGoals}">
                        <tr  class="${oddEven.oddEven}">
                            <td>
                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="goal"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
        <a href="javascript:dijit.byId('portfolioConfiguration').attr('href', 'ConfigureAssessment.html?action=addGeneralLearningGoal&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">Add general learning goal</a>        <br/>
        <br/>
        <h2>Specific Learning goals</h2>

        <c:choose>


            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.specificLearningGoals) > 0}">
                <table>
                    <c:forEach var="learningGoal" items="${pedagogicalPlan.assessmentSetup.specificLearningGoals}">
                        <tr  class="${oddEven.oddEven}">
                            <td>
                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${learningGoal}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${learningGoal.id}" property="goal"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
        <a href="ConfigureAssessment.html?action=addSpecificLearningGoal&eloURI=${missionSpecificationEloURI}">Add specific learning goal</a>




