<%@ include file="common-taglibs.jsp" %>
<div id="addReflectionQuestionOnEloForTeacher">
<p>
    <spring:message code="EPORTFOLIO_TEACHER_HELP"/>
</p>


<div style="width: 800px; height: 600px">
    <div dojoType="dijit.layout.TabContainer" style="width: 100%; height: 100%;">

<c:choose>
    <c:when test="${fn:length(anchorEloReflectionQuestionForTeacherTransporters) > 0}">
            <c:forEach var="transporter" items="${anchorEloReflectionQuestionForTeacherTransporters}">

                <div dojoType="dijit.layout.ContentPane" title="${transporter.anchorElo.myname}" selected="true">
                     <c:choose>
                         <c:when test="${fn:length(transporter.teacherQuestionToElos) > 0}">
                              <table>
                                <tr>
                                    <th width="40%"><spring:message code="QUESTION_TITLE"/> </th>
                                    <th width="40%"><spring:message code="QUESTION"/> </th>
                                    <th>Text</th>
                                    <th>Slider</th>
                                </tr>
                                <c:forEach var="teacherQuestionToElo" items="${transporter.teacherQuestionToElos}">
                                    <tr class="${oddEven.oddEven}">
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${teacherQuestionToElo}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${teacherQuestionToElo.id}" property="questionTitle"/></td>
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${teacherQuestionToElo}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${teacherQuestionToElo.id}" property="question"/></td>
                                        <td>
                                            <a href="javascript:openPage('addReflectionQuestionOnEloForTeacher', 'eportfolioTeacherElo.html?action=setTeacherQuestionToEloToText&teacherQuestionToElo=${teacherQuestionToElo.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                                                <c:if test="${teacherQuestionToElo.questionType == 'text'}">
                                                     <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                 </c:if>
                                                 <c:if test="${teacherQuestionToElo.questionType == 'slider'}">
                                                     <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                 </c:if>

                                            </a>
                                        </td>
                                        <td>
                                            <a href="javascript:openPage('addReflectionQuestionOnEloForTeacher', 'eportfolioTeacherElo.html?action=setTeacherQuestionToEloToSlider&teacherQuestionToElo=${teacherQuestionToElo.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                                                <c:if test="${teacherQuestionToElo.questionType == 'slider'}">
                                                     <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                 </c:if>
                                                 <c:if test="${teacherQuestionToElo.questionType == 'text'}">
                                                     <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                 </c:if>

                                            </a>
                                        </td>
                                        <td width="3%">
                                            <!--a href="javascript:openPage('addReflectionQuestionOnMission', 'eportfolioStudentElo.html?action=removeReflectionQuestion&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}') + '&amp;reflectionQuestionId=${reflectionQuestion.id}');">
                                                <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                                            </a-->
                                        </td>

                                    </tr>
                                </c:forEach>
                                  </table>
                            </c:when>

                     </c:choose>
                           <a href="javascript:openPage('addReflectionQuestionOnEloForTeacher', 'eportfolioTeacherElo.html?action=addTeachersQuestionToElo&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                               <spring:message code="ADD_QUESTION"/>
                           </a>
                    <br/>


                     <c:choose>
                         <c:when test="${fn:length(transporter.rubricForElos) > 0}">
                                <c:forEach var="rubric" items="${transporter.rubricForElos}">
                                    <h2><spring:message code="RUBRIC_FOR"/> ${transporter.anchorElo.myname}</h2>
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



                            <a href="javascript:openPage('addReflectionQuestionOnEloForTeacher', 'eportfolioTeacherElo.html?action=addRubricToElo&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                               <spring:message code="ADD_RUBRIC"/>
                           </a>
                <br/>

                    </div>
            </c:forEach>


    </c:when>
</c:choose>





    </div>
</div>



</div>

