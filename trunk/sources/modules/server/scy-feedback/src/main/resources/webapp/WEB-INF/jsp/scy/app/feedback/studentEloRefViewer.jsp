<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>${model.name}${model.id}</h1>
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
            </tr>
        </table>

        <hr/>

        <p>
            ${model.comment}
        </p>

        <fieldset>
            <label>Peer Evaluation</label>
        </fieldset>

    </tiles:putAttribute>
</tiles:insertDefinition>