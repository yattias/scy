<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        REFLECTION ON PORTFOLIO ${portfolio.portfolioStatus}

        NUMBER OF ANSWERS: ${fn:length(portfolio.missionReflectionQuestionAnswers)}

        <c:choose>
            <c:when test="${fn:length(portfolio.missionReflectionQuestionAnswers) > 0}">
                <table>
                <c:forEach var="missionReflectionQuestionAnswer" items="${portfolio.missionReflectionQuestionAnswers}">
                    <tr>
                        <td>
                            ${missionReflectionQuestionAnswer.tab.question}
                        </td>
                        <td>
                            ${missionReflectionQuestionAnswer.answer}
                        </td>
                    </tr>

                </c:forEach>
                </table>
            </c:when>
        </c:choose>




        <c:choose>
            <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.reflectionTabs) > 0}">
                <form action="storeReflectionsOnMission.html" style="display:block;height:50%;">
                <div dojoType="dijit.layout.TabContainer" style="height:100%;">

                        <c:forEach var="tab" items="${pedagogicalPlan.assessmentSetup.reflectionTabs}">
                            <div dojoType="dojox.layout.ContentPane" title="${tab.title}">
                                <table>
                                    <tr>
                                <td width="20%">
                                    <strong>${tab.question}</strong>
                                </td>
                                <td>
                                    <c:if test="${fn:contains(tab.type, 'text')}">
                                        <textarea rows="4" cols="30" name="${tab.id}"></textarea>
                                    </c:if>
                                    <c:if test="${fn:contains(tab.type, 'slider')}">
                                        <input name="${tab.id}" type="text"/>
                                    </c:if>
                                </td>
                            </tr>
                                    </table>
                                </div>
                        </c:forEach>
                   <div dojoType="dojox.layout.ContentPane" title="SUBMIT">
                       <table>
                    <tr>
                        <td>
                            <input type="hidden" value="${missionRuntimeURI}" name="missionRuntimeURI"/>
                            <input type="submit" value="SUBMIT_PORTFOLIO">SUBMIT</input>        
                        </td>
                    </tr>
                    </table>
                       </div>

                    </div>

                   </form>

            </c:when>
        </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>