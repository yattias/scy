<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h1>${model.name}</h1>

        <p>${model.description}</p>

        Activities
        <div>
            <c:choose>
                <c:when test="${fn:length(model.activities) > 0}">
                    <table id="activityTable" border="2">
                        <h5>Activities</h5>
                        <tr>
                            <th>Activity</th>
                            <th>ELO</th>
                        </tr>
                        <c:forEach var="activity" items="${model.activities}">
                            <tr>
                                <td>${activity.name}</td>
                                <td>${activity.anchorELO.name}</td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br>
                </c:when>
            </c:choose>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>
