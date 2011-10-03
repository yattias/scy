<%@ include file="common-taglibs.jsp" %>
<script type="text/javascript">
    if(dijit.byId('addReflectionQuestionOnMission')){
        dijit.byId('addReflectionQuestionOnMission').destroy();
    }
</script>
<div dojoType="dojox.layout.ContentPane" id="addReflectionQuestionOnMission" executeScripts="true" parseOnLoad="true">
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
                                    <th></th>
                                    <th width="7%">Text</th>
                                    <th width="7%">Slider</th>
                                    <th></th>
                                </tr>
                                <c:forEach var="reflectionQuestion" items="${transporter.reflectionQuestions}">
                                    <tr class="${oddEven.oddEven}">
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="reflectionQuestionTitle"/></td>
                                        <td><s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${reflectionQuestion}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${reflectionQuestion.id}" property="reflectionQuestion"/></td>
                                        <td>${reflectionQuestion.anchorEloURI}</td>
                                        <td>
                                            <a href="javascript:openPage(document.getElementById('addReflectionQuestionOnMission').parentNode.id, 'eportfolioStudentElo.html?action=setReflectionQuestionToText&reflectionQuestion=${reflectionQuestion.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                                               <c:if test="${reflectionQuestion.type == 'text'}">
                                                    <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                </c:if>
                                                <c:if test="${reflectionQuestion.type == 'slider'}">
                                                    <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                </c:if>
                                           </a>
                                        </td>
                                        <td>
                                            <a href="javascript:openPage(document.getElementById('addReflectionQuestionOnMission').parentNode.id, 'eportfolioStudentElo.html?action=setReflectionQuestionToSlider&reflectionQuestion=${reflectionQuestion.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                                               <c:if test="${reflectionQuestion.type == 'slider'}">
                                                    <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                                </c:if>
                                                <c:if test="${reflectionQuestion.type == 'text'}">
                                                    <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                                </c:if>
                                           </a>
                                        </td>
                                        <td width="3%">
                                            <a href="javascript:openPage(document.getElementById('addReflectionQuestionOnMission').parentNode.id, 'eportfolioStudentElo.html?action=removeReflectionQuestion&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}') + '&amp;reflectionQuestionId=${reflectionQuestion.id}');">
                                                <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                                            </a>
                                        </td>
                                        
                                    </tr>
                                </c:forEach>
                                  </table>
                            </c:when>

                     </c:choose>
                           <a href="javascript:openPage(document.getElementById('addReflectionQuestionOnMission').parentNode.id, 'eportfolioStudentElo.html?action=addReflectionQuestion&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}') + '&amp;anchorEloURI=' + encodeURIComponent('${transporter.anchorElo.uri}'));">
                               <spring:message code="ADD_QUESTION"/>
                           </a>
            </c:forEach>

            <br/><br/>
        <a href="javascript:openPage(document.getElementById('addReflectionQuestionOnMission').parentNode.id, 'eportfolioStudentElo.html?action=clearReflectionQuestions&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
            Resync questions
        </a>


    </c:when>
</c:choose>
<br/><br/>


</div>