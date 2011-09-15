<%@ include file="common-taglibs.jsp" %>
<div id="addReflectionQuestionOnEloForTeacher">
<p>
    <spring:message code="EPORTFOLIO_TEACHER_HELP"/>
</p>


<c:choose>
    <c:when test="${fn:length(anchorEloReflectionQuestionForTeacherTransporters) > 0}">
            <c:forEach var="transporter" items="${anchorEloReflectionQuestionForTeacherTransporters}">

                    <h2>${transporter.anchorElo.myname}</h2>
                     <c:choose>
                         <c:when test="${fn:length(transporter.teacherQuestionToElos) > 0}">
                              <table>
                                <tr>
                                    <th><spring:message code="QUESTION_TITLE"/> </th>
                                    <th><spring:message code="QUESTION"/> </th>
                                    <th width="7%"><spring:message code="INPUT_TYPE"/> </th>
                                    <th></th>
                                </tr>
                                <c:forEach var="teacherQuestionToElo" items="${transporter.teacherQuestionToElos}">
                                    <tr class="${oddEven.oddEven}">
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${teacherQuestionToElo}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${teacherQuestionToElo.id}" property="questionTitle"/></td>
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${teacherQuestionToElo}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${teacherQuestionToElo.id}" property="question"/></td>
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${teacherQuestionToElo}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${teacherQuestionToElo.id}" property="questionType"/></td>
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
            </c:forEach>


    </c:when>
</c:choose>

</div>

