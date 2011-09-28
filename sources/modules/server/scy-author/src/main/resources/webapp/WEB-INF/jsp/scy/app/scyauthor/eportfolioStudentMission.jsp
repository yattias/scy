<%@ include file="common-taglibs.jsp" %>
<script type="text/javascript">
    if(dijit.byId('mission-student')){
        dijit.byId('mission-student').destroy();
    }
</script>
<div dojoType="dojox.layout.ContentPane" id="mission-student" executeScripts="true" parseOnLoad="true">

    <h2><spring:message code="REFLECTION_QUESTIONS_ON_MISSION"/></h2>
    <p>
        <spring:message code="STUDENT_MISSION_HELP"/>
    </p>
    <p>
        <spring:message code="STUDENT_MISSION_HELP_2"/>
    </p>
    <c:choose>
        <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.reflectionTabs) > 0}">
            <table>
                <tr>
                    <th width="20%">Title</th>
                    <th>Question</th>
                    <th>Text input</th>
                    <th>Slider input</th>
                    <th></th>
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
                            <a href="javascript:openPage(document.getElementById('mission-student').parentNode.id, 'eportfolioStudentMission.html?action=setInputTypeToText&reflectionTabId=${reflectionTab.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                <c:if test="${reflectionTab.type == 'text'}">
                                        <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                    </c:if>
                                    <c:if test="${reflectionTab.type == 'slider'}">
                                        <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                    </c:if>
                            </a>
                        </td>
                        <td>
                            <a href="javascript:openPage(document.getElementById('mission-student').parentNode.id, 'eportfolioStudentMission.html?action=setInputTypeToSlider&reflectionTabId=${reflectionTab.id}&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">
                                    <c:if test="${reflectionTab.type == 'slider'}">
                                        <img src="/webapp/themes/scy/default/images/checked_radio.png" alt=""  />
                                    </c:if>
                                    <c:if test="${reflectionTab.type == 'text'}">
                                        <img src="/webapp/themes/scy/default/images/unchecked_radio.png" alt=""  />
                                    </c:if>

                            </a>
                        </td>
                        <td>
                            <img src="/webapp/themes/scy/default/images/trash.png" alt="delete"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
    </c:choose>
    <br/>
    <a href="javascript:openPage(document.getElementById('mission-student').parentNode.id, 'eportfolioStudentMission.html?action=addReflectionQuestionOnMission&eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">Add reflection question</a>        <br/>
    <br/>
</div>

