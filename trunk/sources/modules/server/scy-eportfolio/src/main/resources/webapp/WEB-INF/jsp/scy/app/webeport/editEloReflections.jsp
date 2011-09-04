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
                    <!--img src="/webapp/themes/scy/default/images/feedback_header.png" alt="" class="greenBackgrounds" /-->
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

        <form action="/webapp/app/webeport/StoreEloReflections.html">
            <table>
                <tr>
                    <td>
                        <strong>General learning goals</strong>
                    </td>
                    <td>
                        <div id="generalLearningGoals">
                            <table>
                                <c:choose>
                                    <c:when test="${fn:length(selectedGeneralLearningGoalWithScores) > 0}">
                                        <c:forEach var="generalLearningGoalWithScore" items="${selectedGeneralLearningGoalWithScores}">
                                            <tr>
                                                <td width="5%">
                                                    ${generalLearningGoalWithScore.learningGoalText}
                                                </td>
                                            </tr>

                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                                <tr>
                                    <td>

                                        <a href="javascript:loadDialog('selectLearningGoalsForElo.html?eloURI=' + encodeURIComponent('${elo.uri}') + '&lgType=general&missionRuntimeURI=' + encodeURIComponent('${missionRuntimeURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${anchorEloURI}'), '<spring:message code="SELECT_LEARNING_GOAL"></spring:message>');"><spring:message code="SELECT_LEARNING_GOAL"></spring:message> </a>

                                    </td>
                                </tr>

                            </table>
                        </div>

                    </td>
                </tr>
                <tr>
                    <td>
                        <strong>Specific learning gooals</strong>
                    </td>
                    <td>
                        <div id="generalLearningGoals">
                            <table>
                                <c:choose>
                                    <c:when test="${fn:length(selectedSpecificLearningGoalWithScores) > 0}">
                                        <c:forEach var="specificLearningGoalWithScore" items="${selectedSpecificLearningGoalWithScores}">
                                            <tr>
                                                <td width="5%">
                                                    ${specificLearningGoalWithScore.learningGoalText}
                                                </td>
                                            </tr>

                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                                <tr>
                                    <td>
                                        <!--a href="javascript:loadDialog('selectLearningGoalsForElo.html?eloURI=${elo.uri}&lgType=specific&missionRuntimeURI=${missionRuntimeURI}&anchorEloURI=${anchorEloURI}', '<spring:message code="SELECT_LEARNING_GOAL"></spring:message>');"><spring:message code="SELECT_LEARNING_GOAL"></spring:message> </a-->
                                        <a href="javascript:loadDialog('selectLearningGoalsForElo.html?eloURI=' + encodeURIComponent('${elo.uri}') + '&lgType=general&missionRuntimeURI=' + encodeURIComponent('${missionRuntimeURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${anchorEloURI}'), '<spring:message code="SELECT_LEARNING_GOAL"></spring:message>');"><spring:message code="SELECT_LEARNING_GOAL"></spring:message> </a>
                                    </td>
                                </tr>


                            </table>
                        </div>

                    </td>
                </tr>

                <c:choose>
                    <c:when test="${fn:length(reflectionQuestions) > 0}">
                        <c:forEach var="reflectionQuestion" items="${reflectionQuestions}">
                            <tr>
                                <td>
                                    <strong>${reflectionQuestion.reflectionQuestionTitle}</strong>
                                </td>
                                <td>
                                    <strong>${reflectionQuestion.reflectionQuestion}</strong>
                                    <br/>

                                    <c:if test="${fn:contains(reflectionQuestion.type, 'text')}">
                                        <textarea rows="4" cols="30" name="${reflectionQuestion.id}"></textarea>
                                    </c:if>
                                    <c:if test="${fn:contains(reflectionQuestion.type, 'slider')}">
                                        <input name="${reflectionQuestion.id}" id="${reflectionQuestion.id}" type="text"/>
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
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                </c:choose>
                <input type="hidden" name="missionRuntimeURI" value="${missionRuntimeURI}"/>
                <input type="hidden" name="eloURI" value="${eloURI}"/>
                <input type="hidden" name="anchorEloURI" value="${anchorEloURI}"/>
            </table>
            <input type="submit" value="<spring:message code="ADD_TO_PORTFOLIO"/>">
        </form>

       </div>
        </div>
       
    </tiles:putAttribute>
</tiles:insertDefinition>