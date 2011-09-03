<%@ include file="common-taglibs.jsp" %>

<div id="addReflectionQuestionOnMission">

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
            <c:when test="${fn:length(anchorElos) > 0}">
                <table>
                    <tr>
                        <th>Anchor elo name</th>
                        <th>Question title</th>
                    </tr>
                    <c:forEach var="transporter" items="${anchorElos}">
                        <tr  class="${oddEven.oddEven}">
                            <td valign="top">
                                ${transporter.anchorElo.myname}
                            </td>
                            <td valign="top">
                             <c:choose>
                                 <c:when test="${fn:length(transporter.reflectionQuestions) > 0}">
                                    <table>
                                        <c:forEach var="reflectionQuestion" items="${transporter.reflectionQuestions}">
                                            <tr>
                                                <td width="3%">
                                                    <a href="javascript:openPage('addReflectionQuestionOnMission', 'ConfigureAssessment.html?action=removeReflectionQuestion&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}') + '&amp;reflectionQuestionId=${reflectionQuestion.id}');">
                                                        Delete
                                                    </a>
                                                </td>
                                                <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="reflectionQuestionTitle"/></td>
                                                <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="reflectionQuestion"/></td>
                                                <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="type"/></td>
                                            </tr>
                                        </c:forEach>
                                        <tr>
                                            <td align="right" colspan="2">
                                                <a href="javascript:openPage('addReflectionQuestionOnMission', 'ConfigureAssessment.html?action=addReflectionQuestion&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                                                    <strong>[Add question to ${transporter.anchorElo.myname}]</strong>
                                                </a>
                                            </td>
                                        </tr>
                                    </table>
                                </c:when>
                             </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
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
        <a href="javascript:openPage('addReflectionQuestionOnMission', 'ConfigureAssessment.html?action=addReflectionQuestionOnMission&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">Add reflection question</a>        <br/>
        <br/>


</div>
