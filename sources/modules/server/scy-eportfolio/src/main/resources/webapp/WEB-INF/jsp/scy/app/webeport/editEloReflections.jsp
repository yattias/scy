<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
   <style type="text/css">
        .feedbackHeader{
                background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                background-repeat:no-repeat;
                color:#ffffff;
                height:50px;
                background-color:#333333 !important;
                font-weight:bold;
                font-size:25px;
                text-align:center;
                padding-top:20px;
            }
    </style>
        <div style="border:4px solid #cc6600;width:786px;height:95%;padding:4px;" class="greenBorders">
                    <div class="feedbackHeader" >My ePortfolio</div>

                <div dojoType="dojox.layout.ContentPane" style="width:100%;height:90%;" id="eportfolioPane" parseOnLoad="true" executeScripts="true">

        <table>
            <tr>
                <td width="20%">
                    <a href="javascript:loadDialog('/webapp/components/openEloInScyLabDialog.html?eloURI=${elo.uri}', '${eno.myname}');">
                        <img src="${elo.thumbnail}"/>
                    </a>
                </td>
                <td>
                    <table>
                        <tr>
                            <td>
                                <strong>Name</strong>
                            </td>
                            <td>
                                ${elo.myname}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <strong>Created by</strong>
                            </td>
                            <td>
                                ${createdBy}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <strong>Date</strong>
                            </td>
                            <td>
                                ${lastModified}
                            </td>
                        </tr>
                    </table>

                </td>
            </tr>
        </table>
        <br/>

        <c:if test="${showWarningNoSpecificLearningGoalsAdded}">
            <table>
                <tr>
                    <td width="100%">
                        <font color="red">
                            <center><strong><spring:message code="NO_LEARNING_GOALS_HAVE_BEEN_SELECTED_WARNING"/></strong></center>
                        </font>
                    </td>
                </tr>
            </table>
            <br/>
        </c:if>



                        <h2><spring:message code="GENERAL_LEARNING_GOALS"/> </h2>
                            <c:if test="${portfolioLocked == false}">
                                <a href="javascript:loadDialog('/webapp/app/webeport/selectLearningGoalsForElo.html?eloURI=' + encodeURIComponent('${elo.uri}') + '&lgType=general&missionRuntimeURI=' + encodeURIComponent('${missionRuntimeURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${anchorEloURI}'), '<spring:message code="SELECT_LEARNING_GOAL"></spring:message>');"><spring:message code="SELECT_LEARNING_GOAL"></spring:message> </a>
                            </c:if>
                        <div id="generalLearningGoals">
                            <c:choose>
                                <c:when test="${fn:length(selectedGeneralLearningGoalWithScores) > 0}">
                                    <table>
                                        <tr>
                                            <td width="45%">
                                                <strong><spring:message code="SELECTED_LEARNING_GOALS"/></strong>
                                            </td>
                                            <td width="45%">
                                                <strong><spring:message code="SELECTED_CRITERIA"/></strong>
                                            </td>
                                            <td>
                                                <strong><spring:message code="LEVEL"/></strong>
                                            </td>
                                            <td></td>
                                        </tr>

                                        <c:forEach var="generalLearningGoalWithScore" items="${selectedGeneralLearningGoalWithScores}">
                                            <tr class="${oddEven.oddEven}">
                                                <td>
                                                    ${generalLearningGoalWithScore.learningGoalText}
                                                </td>
                                                <td>
                                                    ${generalLearningGoalWithScore.criteriaText}
                                                </td>
                                                <td>
                                                    <c:if test="${generalLearningGoalWithScore.criteriaLevel =='LOW'}">
                                                        <img src="/webapp/themes/scy/default/images/red.png" alt="<spring:message code="LOW"/>"  />
                                                    </c:if>
                                                    <c:if test="${generalLearningGoalWithScore.criteriaLevel =='MEDIUM'}">
                                                        <img src="/webapp/themes/scy/default/images/yellow.png" alt="<spring:message code="MEDIUM"/>"  />
                                                    </c:if>
                                                    <c:if test="${generalLearningGoalWithScore.criteriaLevel =='HIGH'}">
                                                        <img src="/webapp/themes/scy/default/images/green.png" alt="<spring:message code="HIGH"/>"/>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <center>
                                                        <a href="javascript:if(confirm('Do you really want to delete this?')){ location.href='editEloReflections.html?anchorEloURI='  + encodeURIComponent('${anchorEloURI}') + '&amp;eloURI='  + encodeURIComponent('${eloURI}') + '&amp;missionRuntimeURI=' + encodeURIComponent('${missionRuntimeURI}') + '&amp;generalLearningGoalWithScoreId=${generalLearningGoalWithScore.id}&amp;action=delete';}">
                                                            <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                                                        </a>
                                                    </center>
                                                </td>


                                            </tr>

                                        </c:forEach>
                                    </table>
                                </c:when>
                            </c:choose>


                        </div>

                        <br/>
                        <h2><spring:message code="SPECIFIC_LEARNING_GOALS"/></h2>
                        <c:if test="${portfolioLocked == false}">
                            <a href="javascript:loadDialog('/webapp/app/webeport/selectLearningGoalsForElo.html?eloURI=' + encodeURIComponent('${elo.uri}') + '&lgType=specific&missionRuntimeURI=' + encodeURIComponent('${missionRuntimeURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${anchorEloURI}'), '<spring:message code="SELECT_LEARNING_GOAL"></spring:message>');"><spring:message code="SELECT_LEARNING_GOAL"></spring:message> </a>
                        </c:if>

                        <div id="generalLearningGoals">
                            <c:choose>
                                <c:when test="${fn:length(selectedSpecificLearningGoalWithScores) > 0}">

                                    <table>
                                        <tr>
                                            <td width="45%">
                                                <strong><spring:message code="SELECTED_LEARNING_GOALS"/></strong>
                                            </td>
                                            <td width="44%">
                                                <strong><spring:message code="SELECTED_CRITERIA"/></strong>
                                            </td>
                                            <td>
                                                <strong><spring:message code="LEVEL"/></strong> 
                                            </td>
                                            <td></td>
                                        </tr>

                                        <c:forEach var="generalLearningGoalWithScore" items="${selectedSpecificLearningGoalWithScores}">
                                            <tr class="${oddEven.oddEven}">
                                                <td>
                                                    ${generalLearningGoalWithScore.learningGoalText}
                                                </td>
                                                <td>
                                                    ${generalLearningGoalWithScore.criteriaText}
                                                </td>
                                                <td>
                                                    <c:if test="${generalLearningGoalWithScore.criteriaLevel =='LOW'}">
                                                        <img src="/webapp/themes/scy/default/images/red.png" alt="<spring:message code="LOW"/>"  />
                                                    </c:if>
                                                    <c:if test="${generalLearningGoalWithScore.criteriaLevel =='MEDIUM'}">
                                                        <img src="/webapp/themes/scy/default/images/yellow.png" alt="<spring:message code="MEDIUM"/>"  />
                                                    </c:if>
                                                    <c:if test="${generalLearningGoalWithScore.criteriaLevel =='HIGH'}">
                                                        <img src="/webapp/themes/scy/default/images/green.png" alt="<spring:message code="HIGH"/>"/>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <center>
                                                        <a href="javascript:if(confirm('Do you really want to delete this?')){ location.href='editEloReflections.html?anchorEloURI='  + encodeURIComponent('${anchorEloURI}') + '&amp;eloURI='  + encodeURIComponent('${eloURI}') + '&amp;missionRuntimeURI=' + encodeURIComponent('${missionRuntimeURI}') + '&amp;generalLearningGoalWithScoreId=${generalLearningGoalWithScore.id}&amp;action=delete';}">
                                                            <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                                                        </a>
                                                    </center>
                                                </td>


                                            </tr>

                                        </c:forEach>
                                    </table>
                                </c:when>
                            </c:choose>
                        </div>


            <table>
                <tr>
                <form action="/webapp/app/webeport/StoreEloReflections.html" >
                <c:choose>
                    <c:when test="${fn:length(reflectionQuestions) > 0}">
                        <c:forEach var="reflectionQuestion" items="${reflectionQuestions}">
                            <tr class="${oddEven.oddEven}">
                                <td>
                                    <strong>${reflectionQuestion.reflectionQuestionTitle}</strong>
                                </td>
                                <td>
                                    <strong>${reflectionQuestion.reflectionQuestion}</strong>
                                    <br/>
                                    <c:if test="${portfolioLocked == false}">
                                        <c:if test="${fn:contains(reflectionQuestion.type, 'text')}">
                                            <textarea rows="4" cols="30" name="${reflectionQuestion.id}"></textarea>
                                        </c:if>
                                        <c:if test="${fn:contains(reflectionQuestion.type, 'slider')}">
                                            <input name="${reflectionQuestion.id}" id="${reflectionQuestion.id}" type="text" value="1" style="display:none"/>
                                            <div id="reflectionSlider" dojoType="dijit.form.HorizontalSlider" value="1" minimum="1" maximum="4" discreteValues="1" intermediateChanges="false" showButtons="false" style="width:90%;margin-top:5px;" onChange="document.getElementById('${reflectionQuestion.id}').value = Math.round(this.value);">
                                                <ol dojoType="dijit.form.HorizontalRuleLabels" container="topDecoration" style="height:1.5em;font-size:75%;color:gray;">
                                                    <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_1.png" alt=""  /></li>
                                                    <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_2.png" alt=""  /></li>
                                                    <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_3.png" alt=""  /></li>
                                                    <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_4.png" alt=""  /></li>
                                                </ol>
                                                <div dojoType="dijit.form.HorizontalRule" container="bottomDecoration" count="4" style="height:5px;">
                                                    <ol dojoType="dijit.form.HorizontalRuleLabels" container="bottomDecoration" style="height:1em;font-size:75%;color:gray;"></ol>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                </c:choose>
                <input type="hidden" name="missionRuntimeURI" value="${missionRuntimeURI}"/>
                <input type="hidden" name="eloURI" value="${eloURI}"/>
                <input type="hidden" name="anchorEloURI" value="${anchorEloURI}"/>
                    <c:if test="${portfolioLocked == false}">

                        <tr>
                            <td colspan="2"><input type="submit" value="<spring:message code="ADD_TO_PORTFOLIO"/>" name="submitToPortfolio" /></td>
                        </tr>

            </c:if>
                </form>
            </table>


       </div>
        </div>
       
    </tiles:putAttribute>
</tiles:insertDefinition>