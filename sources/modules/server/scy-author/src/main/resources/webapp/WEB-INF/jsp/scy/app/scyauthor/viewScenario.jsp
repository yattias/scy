<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Scenario: ${model.name}</h1>

         <c:choose>
            <c:when test="${fn:length(learningActivitySpaces) > 0}">
                <table id="pedagogicalPlansTable" border="2" width="100%">
                    <tr>
                        <th>Learning activity space</th>
                    </tr>
                    <c:forEach var="las" items="${learningActivitySpaces}">
                        <tr class="${oddEven.oddEven}">
                            <td><a href="viewLAS.html?id=${las.id}">${las.name}</a></td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>

         <c:choose>
            <c:when test="${fn:length(learningGoals) > 0}">
                <table id="learningGoalsTable" border="2" width="100%">
                    <tr>
                        <th>Learning goals</th>
                    </tr>
                    <c:forEach var="learningGoal" items="${learningGoals}">
                        <tr class="${oddEven.oddEven}">
                            <td>${learningGoal.name}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>