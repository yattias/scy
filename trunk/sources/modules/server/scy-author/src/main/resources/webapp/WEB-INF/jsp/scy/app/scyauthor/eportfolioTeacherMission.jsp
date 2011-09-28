<%@ include file="common-taglibs.jsp" %>
<script type="text/javascript">
    if(dijit.byId('teacherQuestionsForMission')){
        dijit.byId('teacherQuestionsForMission').destroy();
    }
</script>
<div dojoType="dojox.layout.ContentPane" id="teacherQuestionsForMission" executeScripts="true" parseOnLoad="true">

<p>
    <spring:message code="EPORTFOLIO_TEACHER_HELPX"/>
   </p>

    <br/>
    <h2><spring:message code="QUESTIONS_FOR_MISSION"/> </h2>
    <c:choose>
        <c:when test="${fn:length(teacherQuestionsToMission) > 0}">
            <table>
                <tr>
                    <th width="40%">
                        <spring:message code="QUESTION_TITLE"/>
                    </th>
                    <th width="40%">
                        <spring:message code="QUESTION"/>
                    </th>
                    <th>
                        Text
                    </th>
                    <th>
                        Slider
                    </th>
                </tr>
                <c:forEach var="teacherQuestion" items="${teacherQuestionsToMission}">
                    <tr>
                        <td>
                            <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${teacherQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${teacherQuestion.id}" property="questionTitle"/>
                        </td>
                        <td>
                            <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${teacherQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${teacherQuestion.id}" property="question"/>
                        </td>
                        <td>
                            <a href="javascript:openPage(document.getElementById('teacherQuestionsForMission').parentNode.id, 'eportfolioTeacherMission.html?action=setTeacherQuestionTypeToText&teacherQuestion=${teacherQuestion.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                <c:if test="${teacherQuestion.questionType == 'text'}">
                                    <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                </c:if>
                                <c:if test="${teacherQuestion.questionType == 'slider'}">
                                    <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                </c:if>

                            </a>
                        </td>
                        <td>
                            <a href="javascript:openPage(document.getElementById('teacherQuestionsForMission').parentNode.id, 'eportfolioTeacherMission.html?action=setTeacherQuestionTypeToSlider&teacherQuestion=${teacherQuestion.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                <c:if test="${teacherQuestion.questionType == 'slider'}">
                                    <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                </c:if>
                                <c:if test="${teacherQuestion.questionType == 'text'}">
                                    <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                </c:if>

                            </a>
                        </td>
                    </tr>
               </c:forEach>
            </table>
        </c:when>
    </c:choose>

    <a href="javascript:openPage(document.getElementById('teacherQuestionsForMission').parentNode.id, 'eportfolioTeacherMission.html?action=addTeacherQuestion&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
        <spring:message code="ADD_QUESTION"/>
    </a>


    <c:choose>
        <c:when test="${fn:length(teacherRubricsForMission) > 0}">
            <br/><br/>
            <h2><spring:message code="RUBRIC_FOR_MISSION"/></h2>
               <c:forEach var="rubric" items="${teacherRubricsForMission}">
                   <table>
                       <tr>
                           <th></th>
                           <th>Assessment Criteria</th>
                           <th>Poor</th>
                           <th>Fair</th>
                           <th>Good</th>
                           <th>Exellent</th>
                       </tr>
                   <c:choose>
                       <c:when test="${fn:length(rubric.rubricCategories) > 0}">
                              <c:forEach var="rubricCategory" items="${rubric.rubricCategories}">
                                  <tr class="${oddEven.oddEven}">
                                      <td>
                                          <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${rubricCategory}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${rubricCategory.id}" property="name"/>
                                      </td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                      <td></td>
                                  </tr>
                                  <c:choose>
                                       <c:when test="${fn:length(rubric.rubricCategories) > 0}">
                                           <c:forEach var="criteria" items="${rubricCategory.rubricAssessmentCriterias}">
                                               <tr class="${oddEven.oddEven}">
                                                      <td>

                                                      </td>
                                                      <td>
                                                          <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${criteria}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${criteria.id}" property="assessmentCriteria"/>
                                                      </td>
                                                      <td></td>
                                                      <td></td>
                                                      <td></td>
                                                      <td></td>
                                                  </tr>
                                           </c:forEach>
                                           <tr>
                                               <td colspan="6" align="right">
                                                   <a href="javascript:openPage('addReflectionQuestionOnEloForTeacher', 'eportfolioTeacherElo.html?action=addCriteria&categoryId=${rubricCategory.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                                                       <spring:message code="ADD_CRITERIA"/>
                                                   </a>
                                               </td>
                                           </tr>
                                       </c:when>
                                  </c:choose>

                              </c:forEach>
                               <tr>
                                   <td colspan="6" align="right">
                                       <a href="javascript:openPage('addReflectionQuestionOnEloForTeacher', 'eportfolioTeacherElo.html?action=addRubricCategory&rubricId=${rubric.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                                           <spring:message code="ADD_CATEGORY"/>
                                       </a>
                                   </td>
                               </tr>
                       </c:when>
                   </c:choose>

                   </table>
                   </c:forEach>
        </c:when>
     </c:choose>


            <a href="javascript:openPage('teacherQuestionsForMission', 'eportfolioTeacherMission.html?action=addRubricToMission&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                <spring:message code="ADD_RUBRIC"/>
            </a>
</div>