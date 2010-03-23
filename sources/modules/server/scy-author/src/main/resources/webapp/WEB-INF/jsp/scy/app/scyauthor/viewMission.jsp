<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Mission: ${model.name}</h1>
        <table>
            <tr>
                <th>Target group:</th>
                <td>${model.targetGroup}</td>
            </tr>
            <tr>
                <th>Mission outline:</th>
                <td>${model.missionOutline}</td>
            </tr>
        </table>

        <br/>

        <h2>Learning materials</h2>
        <c:choose>
            <c:when test="${fn:length(learningMaterials) > 0}">
                <table id="learningMaterialsTable" border="2"  width="100%">
                    <tr>
                        <th>Name</th>
                        <th>Description</th>

                    </tr>
                    <c:forEach var="lm" items="${learningMaterials}">
                        <tr>
                            <td>${lm.name}</td>
                            <td>${lm.description}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>

        <br/>

        <h2>Learning goals</h2>
        <c:choose>
            <c:when test="${fn:length(learningGoals) > 0}">
                <table id="learningGoalsTable" border="2"  width="100%">
                    <tr>
                        <th>Name</th>
                        <th>Description</th>

                    </tr>
                    <c:forEach var="lg" items="${learningGoals}">
                        <tr>
                            <td>${lg.name}</td>
                            <td>${lg.description}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br/>
            </c:when>
        </c:choose>
        <td><a href="viewMission.html?missionId=${model.id}&newObject=${'NEW_LEARNING_GOAL'}">+</a></td>
        <br/>


    </tiles:putAttribute>
</tiles:insertDefinition>