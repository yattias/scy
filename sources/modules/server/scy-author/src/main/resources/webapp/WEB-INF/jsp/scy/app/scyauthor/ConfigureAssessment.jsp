<%@ include file="common-taglibs.jsp" %>

        <h2><spring:message code="REFLECTION_QUESTIONS_ON_ELOS"/></h2>

        <div id="reflectionQuestionHelp" dojoType="dijit.Dialog" title="Reflection questions">
            <p id="reflectionQuestionHelp">Reflection questions are used in the eportfolio tool. Students will be asked these questions for each ELO type they have added to their eportfolio.</p>
        </div>
        <button id="buttonOne" dojoType="dijit.form.Button" type="button">
            Help
            <script type="dojo/method" event="onClick" args="evt">
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

        <h2><spring:message code="REFLECTION_QUESTIONS_ON_MISSION"/></h2>
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
                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionTab}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionTab.id}" property="title"/>
                            </td>
                            <td>
                                <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionTab}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionTab.id}" property="question"/>
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

<h2><spring:message code="TECHNICAL_SETUP_EPORTFOLIO"/></h2>
<table>
    <tr>
        <td width="50%">
            <strong><spring:message code="TRIM_SEARCH_RESULTS_IN_EPORTFOLIO_TO_TECHNICAL_FORMAT_ONLY"/></strong>
        </td>
        <td>
            <s:ajaxTransferObjectCheckBox transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${pedagogicalPlan}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${pedagogicalPlan.id}" property="trimSearchResultsInEportfolioToContainElosWithEqualTechnicalFormat"/>
        </td>

    </tr>
</table>



