<%@ include file="common-taglibs.jsp" %>
<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
   <style type="text/css">
        .feedbackHeader{
                /*background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                background-repeat:no-repeat;*/
                color:#ffffff;
                height:100px;
                background-color:#333333 !important;
                font-weight:bold;
                /*font-size:25px;
                text-align:center;*/
                padding:10px;
                line-height:1.5;
            }

            .tablerow-odd_eport{
                background-color:#cccccc;
            }
            .tablerow-even_eport{
                background-color:#ebebeb;
            }
            table{
                border:none !important;
            }
    </style>
        <div style="border:4px solid #333333;border-bottom-left-radius:40px;width:786px;height:95%;padding:4px;background-color:#efefef;">
                    <div class="feedbackHeader" >


                            <table width="100%">
                                <tr>
                                    <td align="left" style="width:150px;">

                                       <div style="width:80px;height:90px;float:left;">
                                            <a href="javascript:loadDialog('/webapp/components/openEloInScyLabDialog.html?eloURI=${elo.uri}', '${eno.myname}');">
                                                <img src="${elo.thumbnail}" style="background-color:#cccccc;padding:2px;border:1px solid #cccccc;border-radius:3px;"/>
                                            </a>
                                        </div>
                                        
                                    </td>
                                    <td align="left">
                                        <spring:message code="ELO"/>: ${elo.myname}<br/>

                                        <spring:message code="CREATED_BY"/>:

                                        ${elo.createdBy}<br/>

                                        <spring:message code="DATE"/>:

                                        ${elo.lastModified}
                                    </td>
                                </tr>
                            </table>

                    </div>

                <div dojoType="dojox.layout.ContentPane" style="width:100%;height:90%;" id="eportfolioPane" parseOnLoad="true" executeScripts="true">


        <br/>
        <table style="width:100%;">
        <c:if test="${showWarning}">
            <tr>
                <td width="100%" colspan="2">
                    <font color="red">
                        <center><strong>${warningText}</strong></center>
                    </font>
                </td>
            </tr>
        </c:if>
        <c:if test="${showWarningNoSpecificLearningGoalsAdded}">

                <tr>
                    <td width="100%" colspan="2">
                        <font color="red">
                            <center><strong><spring:message code="NO_LEARNING_GOALS_HAVE_BEEN_SELECTED_WARNING"/></strong></center>
                        </font>
                    </td>
                </tr>

        </c:if>

              <tr>
                   <td style="text-align:right;vertical-align:top;width:40%;">
                        <strong><spring:message code="GENERAL_LEARNING_GOALS"/>:</strong>

                       </td>
                       <td>
                           <span id="generalLEarningGoalsLabel">${fn:length(selectedGeneralLearningGoalWithScores)} <spring:message code="SELECTED_GENERAL_LEARNING_GOALS"/></span>
                           <c:if test="${portfolioLocked == false}">
                               <input type="button" id="generalLearningGoalsSelectorButton" onclick="loadDialog('/webapp/app/webeport/selectLearningGoalsForElo.html?eloURI=' + encodeURIComponent('${elo.uri}') + '&lgType=general&missionRuntimeURI=' + encodeURIComponent('${missionRuntimeURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${anchorEloURI}'), '<spring:message code="SELECT_ACHIVEMENT_LEVEL"></spring:message>');" value="<spring:message code="SELECT_LEARNING_GOAL"></spring:message>">
                            </c:if>

                        <div id="generalLearningGoalsToolTip" dojoType="dijit.Tooltip" connectId="generalLEarningGoalsLabel,generalLearningGoalsSelectorButton" position="below">

                            <c:choose>
                                <c:when test="${fn:length(selectedGeneralLearningGoalWithScores) > 0}">
                                    <table>
                                        <tr>
                                            <c:if test="${pedagogicalPlan.assessmentSetup.useOnlyLearningGoals}">
                                                <td width="97%" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="SELECTED_LEARNING_GOALS"/></strong>
                                                </td>
                                            </c:if>
                                            <c:if test="${pedagogicalPlan.assessmentSetup.useScorableLearningGoals}">
                                                <td width="87%" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="SELECTED_LEARNING_GOALS"/></strong>
                                                </td>
                                                 <td width="10" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="LEVEL"/></strong>
                                                </td>
                                            </c:if>
                                            <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
                                                <td width="42%" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="SELECTED_LEARNING_GOALS"/></strong>
                                                </td>
                                                <td width="45%" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="SELECTED_CRITERIA"/></strong>
                                                </td>
                                                <td style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="LEVEL"/></strong>
                                                </td>
                                            </c:if>
                                            <td width="3%" style="background-color:#333333;color:#ffffff;">&nbsp;</td>
                                        </tr>

                                        <c:forEach var="generalLearningGoalWithScore" items="${selectedGeneralLearningGoalWithScores}">
                                            <tr class="${oddEven.oddEven}_eport">
                                                <c:if test="${pedagogicalPlan.assessmentSetup.useOnlyLearningGoals}">
                                                    <td>
                                                        ${generalLearningGoalWithScore.learningGoalText}
                                                    </td>
                                                </c:if>
                                                <c:if test="${pedagogicalPlan.assessmentSetup.useScorableLearningGoals}">
                                                    <td>
                                                        ${generalLearningGoalWithScore.learningGoalText}
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
                                                </c:if>
                                                <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
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
                                                </c:if>
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



             </td>
        </tr>

        <tr>
          <td style="text-align:right;vertical-align:top;width:40%;">

                        <strong><spring:message code="SPECIFIC_LEARNING_GOALS"/>:</strong>

                        </td>
                        <td>
                <c:if test="${portfolioLocked == false}">

                    <span id="specialLEarningGoalsLabel">${fn:length(selectedSpecificLearningGoalWithScores)} <spring:message code="SELECTED_SPECIFIC_LEARNING_GOALS"/></span>
                    <input type="button" id="specialLearningGoalsSelectorButton" onclick="loadDialog('/webapp/app/webeport/selectLearningGoalsForElo.html?eloURI=' + encodeURIComponent('${elo.uri}') + '&lgType=specific&missionRuntimeURI=' + encodeURIComponent('${missionRuntimeURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${anchorEloURI}'), '<spring:message code="SELECT_ACHIVEMENT_LEVEL"></spring:message>');" value="<spring:message code="SELECT_LEARNING_GOAL"></spring:message>">
                        </c:if>
                        <div id="specificLearningGoalsToolTip" dojoType="dijit.Tooltip" connectId="specialLEarningGoalsLabel,specialLearningGoalsSelectorButton" position="below">

                            <c:choose>
                                <c:when test="${fn:length(selectedSpecificLearningGoalWithScores) > 0}">

                                    <table>
                                        <tr>
                                            <c:if test="${pedagogicalPlan.assessmentSetup.useOnlyLearningGoals}">
                                                <td width="97%" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="SELECTED_LEARNING_GOALS"/></strong>
                                                </td>
                                            </c:if>
                                            <c:if test="${pedagogicalPlan.assessmentSetup.useScorableLearningGoals}">
                                                <td width="87%" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="SELECTED_LEARNING_GOALS"/></strong>
                                                </td>
                                                 <td width="10" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="LEVEL"/></strong>
                                                </td>
                                            </c:if>
                                            <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
                                                <td width="42%" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="SELECTED_LEARNING_GOALS"/></strong>
                                                </td>
                                                <td width="45%" style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="SELECTED_CRITERIA"/></strong>
                                                </td>
                                                <td style="background-color:#333333;color:#ffffff;">
                                                    <strong><spring:message code="LEVEL"/></strong>
                                                </td>
                                            </c:if>
                                            <td width="3%" style="background-color:#333333;color:#ffffff;"></td>
                                        </tr>

                                        <c:forEach var="generalLearningGoalWithScore" items="${selectedSpecificLearningGoalWithScores}">
                                            <tr class="${oddEven.oddEven}_eport">
                                                <c:if test="${pedagogicalPlan.assessmentSetup.useOnlyLearningGoals}">
                                                    <td>
                                                        ${generalLearningGoalWithScore.learningGoalText}
                                                    </td>
                                                </c:if>
                                                <c:if test="${pedagogicalPlan.assessmentSetup.useScorableLearningGoals}">
                                                    <td>
                                                        ${generalLearningGoalWithScore.learningGoalText}
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
                                                </c:if>
                                                <c:if test="${pedagogicalPlan.assessmentSetup.useLearningGoalsWithCriteria}">
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
                                                </c:if>
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
        </td>
        </tr>

            
                <tr>
                <form action="/webapp/app/webeport/StoreEloReflections.html" >
                <c:choose>
                    <c:when test="${fn:length(reflectionQuestions) > 0}">
                        <c:forEach var="reflectionQuestion" items="${reflectionQuestions}">
                            <tr>
                                <td style="text-align:right;vertical-align:top;">
                                    <strong>${reflectionQuestion.reflectionQuestionTitle}</strong>
                                </td>
                                <td>
                                    <strong>${reflectionQuestion.reflectionQuestion}</strong>
                                    <br/>
                                    <c:if test="${portfolioLocked == false}">
                                        <c:if test="${fn:contains(reflectionQuestion.type, 'text')}">
                                            <textarea rows="4" cols="30" style="width:100%;" name="reflection-${reflectionQuestion.id}"></textarea>
                                        </c:if>
                                        <c:if test="${fn:contains(reflectionQuestion.type, 'slider')}">
                                            <input name="reflection-${reflectionQuestion.id}" id="${reflectionQuestion.id}" type="text" value="1" style="display:none"/>
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
                            <c:if test="${eloCanBeAddedToPortfolio}">
                                <td colspan="4" style="text-align:center;height:50px;vertical-align:bottom;">
                                    <input type="submit" value="<spring:message code="ADD_TO_PORTFOLIO"/>" name="submitToPortfolio" />
                                </td>
                            </c:if>
                        </tr>

            </c:if>
                </form>
            </table>


       </div>
        </div>
       
    </tiles:putAttribute>
</tiles:insertDefinition>