<%@ include file="common-taglibs.jsp" %>
<div id="addReflectionQuestionOnMission">
<h2><spring:message code="REFLECTION_QUESTIONS_ON_ELOS"/></h2>

    <p>
        <spring:message code="REFLECTION_QUESTIONS_HELP"/>
    </p>
    <br/>

<c:choose>
    <c:when test="${fn:length(anchorElos) > 0}">
            <c:forEach var="transporter" items="${anchorElos}">

                    <h2>${transporter.anchorElo.myname}</h2>
                     <c:choose>                                                                       
                         <c:when test="${fn:length(transporter.reflectionQuestions) > 0}">
                              <table>
                                <tr>
                                    <th><spring:message code="QUESTION_TITLE"/> </th>
                                    <th><spring:message code="QUESTION"/> </th>
                                    <th width="7%"><spring:message code="INPUT_TYPE"/> </th>
                                    <th></th>
                                </tr>
                                <c:forEach var="reflectionQuestion" items="${transporter.reflectionQuestions}">
                                    <tr class="${oddEven.oddEven}">
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="reflectionQuestionTitle"/></td>
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="reflectionQuestion"/></td>
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="type"/></td>
                                        <td width="3%">
                                            <a href="javascript:openPage('addReflectionQuestionOnMission', 'eportfolioStudentElo.html?action=removeReflectionQuestion&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}') + '&amp;reflectionQuestionId=${reflectionQuestion.id}');">
                                                <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                                            </a>
                                        </td>
                                        
                                    </tr>
                                </c:forEach>
                                  </table>
                            </c:when>

                     </c:choose>
                           <a href="javascript:openPage('addReflectionQuestionOnMission', 'eportfolioStudentElo.html?action=addReflectionQuestion&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                               <spring:message code="ADD_QUESTION"/>
                           </a>
            </c:forEach>


    </c:when>
</c:choose>
<br/><br/>


</div>