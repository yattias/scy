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

                <c:choose>
                    <c:when test="${fn:length(missionReflectionQuestionAnswers) > 0}">
                        <h2><spring:message code="STUDENT_REFLECTIONS"/>  </h2>
                            <table>
                                <c:forEach var="missionReflectionQuestionAnswer" items="${missionReflectionQuestionAnswers}">
                                    <tr>
                                         <td>
                                            ${missionReflectionQuestionAnswer.tab.question}
                                        </td>
                                        <td>
                                            <c:if test="${missionReflectionQuestionAnswer.tab.type == 'text'}">
                                                ${missionReflectionQuestionAnswer.answer}
                                            </c:if>
                                            <c:if test="${missionReflectionQuestionAnswer.tab.type == 'slider'}">
                                                <img src="/webapp/themes/scy/default/images/smiley_${missionReflectionQuestionAnswer.answer}.png" alt=""  />    
                                            </c:if>


                                        </td>
                                    </tr>

                                </c:forEach>
                            </table>
                    </c:when>
                </c:choose>


                <form action="storeMissionReflection.html">
                <c:choose>
                    <c:when test="${fn:length(teacherReflectionOnMisionAnswers) > 0}">
                        <h2><spring:message code="TEACHERS_REFLECTIONS"/>  </h2>
                            <table>
                                <c:forEach var="answer" items="${teacherReflectionOnMisionAnswers}">
                                    <tr>
                                        <td>
                                            ${answer.teacherQuestionToMission} - ${answer.answer}
                                        </td>
                                        <td>
                                            <textarea rows="3" cols="30" name="teacherReflection-${answer.id}">${answer.answer}</textarea>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                    </c:when>
                </c:choose>



            <div>

                    <table>
                        <tr>
                            <td>
                                <h2><spring:message code="COMMENTS_ON_PORTFOLIO"/></h2>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <textarea rows="3" cols="30" name="commentsOnPortfolio"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h2><spring:message code="RATING_ON_PORTFOLIO"/></h2>
                            </td>
                        </tr>
                        <tr>
                            <td>
                               <input type="radio" name="rating" value="1"><spring:message code="POOR"></spring:message><br/>
                               <input type="radio" name="rating" value="2"><spring:message code="FAIR"></spring:message><br/>
                               <input type="radio" name="rating" value="3"><spring:message code="GOOD"></spring:message><br/>
                               <input type="radio" name="rating" value="4"><spring:message code="EXCELLENT"></spring:message><br/>
                                <input type="hidden" value="${missionRuntimeURI}" name="missionRuntimeURI"/>
                            </td>

                        </tr>
                        <tr>
                            <td>
                                <input type="submit" value="<spring:message code="SAVE"/>"/>
                            </td>
                        </tr>
                    </table>

                </div>
            </form>
            </div>


            


        </div>


    </tiles:putAttribute>
</tiles:insertDefinition>