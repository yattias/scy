<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <c:choose>
            <c:when test="${fn:length(transporter.files) > 0}">
                <center>
                    <c:forEach var="refFile" items="${transporter.files}">
                        <a href="/webapp/components/resourceservice.html?id=${refFile.id}" target="_blank">
                            <img style="background-color:#cccccc;padding:15px;" src="/webapp/components/resourceservice.html?id=${refFile.id}&showIcon=true"/>
                        </a>
                    </c:forEach>
                </center>
            </c:when>
        </c:choose>


        <table>
            <tr>
                <td><strong>Tool used</strong></td>
                <td></td>
                <td><strong>Date</strong></td>
                <td>${transporter.eloRef.formattedDate}</td>
            </tr>
            <tr>
                <td><strong>Mission</strong></td>
                <td>${transporter.eloRef.mission}</td>
                <td><strong>Time</strong></td>
                <td></td>
            </tr>
            <tr>
                <td><strong>Viewings</strong></td>
                <td>${transporter.eloRef.viewings}</td>
                <td><strong>Evaluation</strong></td>
                <td>${transporter.totalScore}</td>
            </tr>
        </table>

        <hr/>

        <p>
            <h2>${transporter.eloRef.comment}</h2>
        </p>

        <fieldset>
            <label>Peer Evaluation</label>


            <c:choose>
                <c:when test="${fn:length(transporter.assessments) > 0}">
                    <table>
                        <tr>
                            <th>
                                Date
                            </th>
                            <th>
                                Evaluator
                            </th>
                            <th>
                                Comment
                            </th>
                            <th>
                                Evaluation Score
                            </th>
                        </tr>
                        <c:forEach var="assessment" items="${transporter.assessments}">
                            <tr class="${oddEven.oddEven}">
                                <td>
                                    ${assessment.date}
                                </td>
                                <td>
                                    ${assessment.reviewer.userDetails.username}
                                </td>
                                <td>
                                    <strong>${assessment.comment}</strong>
                                </td>
                                <td>
                                    ${assessment.score}
                                </td>

                            </tr>
                        </c:forEach>
                    </table>
                </c:when>
            </c:choose>

            <br/>
            <br/>

            <table>
                <c:if test="${transporter.useCriteriaBasedAssessment}">
                    <tr>
                        <th>
                            Criteria
                        </th>
                        <th>
                            Score
                        </th>
                        <th>
                            Comment
                        </th>
                    </tr>
                    <c:choose>
                        <c:when test="${fn:length(transporter.criteriaAndExperienceHolders) > 0}">

                                <c:forEach var="criteriaAndExperienceHolder" items="${transporter.criteriaAndExperienceHolders}">
                                    <tr class="${oddEven.oddEven}">
                                        <td>
                                            <s:dialog url="CriteriaBasedEvaluation.html" title="${criteriaAndExperienceHolder.criteriaText}" extraParameters="evaluationCriteriaId=${criteriaAndExperienceHolder.criteria.id}&anchorEloId=${transporter.eloRef.anchorELO.id}&eloRefId=${transporter.eloRef.id}"/>
                                        </td>
                                        <td>
                                            <c:if test="${criteriaAndExperienceHolder.fileRef != null}">
                                                <img style="background-color:#cccccc;padding:15px;"src="/webapp/components/resourceservice.html?id=${criteriaAndExperienceHolder.fileRef.id}&showIcon=true"/>
                                           </c:if>
                                            ${criteriaAndExperienceHolder.score}
                                        </td>
                                        <td>
                                            ${criteriaAndExperienceHolder.comment}
                                        </td>
                                    </tr>
                                </c:forEach>
                        </c:when>
                    </c:choose>
                </c:if>
                <tr class="${oddEven.oddEven}">
                    <td colspan="${transporter.colSpan}">
                        <form method="POST" accept-charset="UTF-8" action="studentEloRefViewer.html">

                            <table>
                                <input type="hidden" name="action" value="addNewComment"/>
                                <input type="hidden" name="modelId" value="${modelId}"/>
                                <input type="hidden" name="username" value="${username}"/>

                                <input type="hidden" name="model" value="${encodedModel}"/>
                                <tr>
                                    <td>
                                        <strong>Comment</strong><br/>
                                        <textarea name="comment" cols="40" rows="4"></textarea>
                                    </td>
                                    <td>
                                        <strong>Evaluation score</strong>
                                        <input type="range" min="1" max="5" value="1" name="score" />
                                        <strong>5</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" align="right">
                                        <input type="submit"/>
                                    </td>
                                </tr>
                            </table>

                        </form>
                    </td>
                </tr>
            </table>


        </fieldset>


    </tiles:putAttribute>
</tiles:insertDefinition>