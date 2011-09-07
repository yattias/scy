<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <style type="text/css">

        .assessmentHeader{
                background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                background-repeat:no-repeat;
                color:#ffffff;
                height:50px;
                background-color:#686808 !important;
                font-weight:bold;
                font-size:25px;
                text-align:center;
                padding-top:20px;
            }

        </style>
        <div style="border:4px solid #686808;width:786px;height:95%;padding:4px;" class="brownBorders">
                    <div class="assessmentHeader" >Assess ${elo.myname}</div>

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





                    <table>
                        <tr>
                            <td width="50%" valign="top">
                                <c:choose>
                                    <c:when test="${fn:length(generalLearningGoals) > 0}">
                                        <table>
                                            <tr>
                                                <th width="80%"><spring:message code="LEARNING_GOALS"/></th>
                                                <th><spring:message code="SCORE"/>  </th>
                                            </tr>
                                            <c:forEach var="generalLearningGoalWithScore" items="${generalLearningGoals}">
                                                <tr>
                                                    <td width="5%">
                                                        ${generalLearningGoalWithScore.learningGoalText}
                                                    </td>
                                                    <td>
                                                     ${generalLearningGoalWithScore.score}
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </c:when>
                                </c:choose>

                                <c:choose>
                                    <c:when test="${fn:length(specificLearningGoals) > 0}">
                                        <table>
                                            <tr>
                                                <th width="80%"><spring:message code="LEARNING_GOALS"/></th>
                                                <th><spring:message code="SCORE"/>  </th>
                                            </tr>

                                            <c:forEach var="learningGoalWithScore" items="${specificLearningGoals}">
                                                <tr>
                                                    <td width="5%">
                                                        ${learningGoalWithScore.learningGoalText}
                                                    </td>
                                                    <td>
                                                        ${generalLearningGoalWithScore.score}
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </c:when>
                                </c:choose>




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

                            </td>
                            <td valign="top">
                                <form action="storeEloAssessment.html">
                                    <table>
                                        <tr>
                                            <th>Assessment of ${elo.myname}</th>
                                        </tr>
                                        <tr>
                                            <td>
                                                <textarea rows="3" cols="30" name="assessmentOfElo">${assessmentOfElo}</textarea>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>
                                                Assessment of reflection
                                            </th>
                                        </tr>
                                        <tr>
                                            <td>
                                                <textarea rows="3" cols="30" name="assessmentOfReflection">${assessmentOfReflection}</textarea>
                                                <input type="hidden" value="${elo.uri}" name="eloURI"/>
                                                <input type="hidden" value="${missionRuntimeURI}" name="missionRuntimeURI"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="left">
                                                <input type="submit" value="Save Assessment"/>
                                            </td>
                                        </tr>
                                    </table>
                                </form>







                            </td>
                        </tr>
                    </table>




        </div>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>