<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <c:choose>
            <c:when test="${fn:length(transporter.files) > 0}">
                <c:forEach var="refFile" items="${transporter.files}">
                    <img style="background-color:#cccccc;padding:15px;" src="/webapp/components/resourceservice.html?id=${refFile.id}"/>
                </c:forEach>
            </c:when>
        </c:choose>


        <table>
            <tr>
                <td><strong>Tool used</strong></td>
                <td></td>
                <td><strong>Date</strong></td>
                <td>${model.formattedDate}</td>
            </tr>
            <tr>
                <td><strong>Mission</strong></td>
                <td>${model.mission}</td>
                <td><strong>Time</strong></td>
                <td></td>
            </tr>
            <tr>
                <td><strong>Viewings</strong></td>
                <td>${model.viewings}</td>
                <td><strong>Evaluation</strong></td>
                <td>${transporter.totalScore}</td>
            </tr>
        </table>

        <hr/>

        <p>
            <strong>${model.comment}</strong>
        </p>

        <fieldset>
            <label>Peer Evaluation</label>



            <c:choose>
                <c:when test="${fn:length(transporter.assessments) > 0}">
                    <table>
                        <c:forEach var="assessment" items="${transporter.assessments}">
                            <tr class="${oddEven.oddEven}">
                                <td with="100%">
                                    <table>
                                        <tr>
                                            <td>
                                                ${assessment.date} by ${assessment.reviewer.userDetails.username}
                                            </td>
                                            <td>
                                                ${assessment.score}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                <strong>${assessment.comment}</strong>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:when>
            </c:choose>

            <s:dialog url="studentFeedbackForm.html?modelId=${model.id}" title="Add comment"/>
        </fieldset>


    </tiles:putAttribute>
</tiles:insertDefinition>