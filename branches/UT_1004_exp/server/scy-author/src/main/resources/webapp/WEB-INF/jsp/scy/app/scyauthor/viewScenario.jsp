<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Scenario: ${model.name}</h1>
        <table>
            <tr>
                <th>
                    Name
                </th>
                <td>
                     <s:ajaxTextField property="name" model="${model}"/>
                </td>
            </tr>
        </table>
        <br/>
        <br/>

        <c:choose>
            <c:when test="${fn:length(learningActivitySpaces) > 0}">
                <table id="pedagogicalPlansTable" border="2" width="100%">
                    <tr>
                        <th>Learning activity space</th>
                        <th>
                            <table width="100%">
                                <th width="40%">Activity</th>
                                <th width="40%">Anchor ELO</th>
                                <th width="20%">Audo add to SP</th>
                            </table>
                        </th>
                    </tr>
                    <c:forEach var="las" items="${learningActivitySpaces}">
                        <tr class="${oddEven.oddEven}">
                            <td><a href="viewLAS.html?id=${las.id}">${las.name}</a></td>
                            <td>
                                <table width="100%">
                                    <c:forEach var="activity" items="${las.activities}">
                                        <tr>
                                            <td width="40%"><a href="viewActivity.html?activityId=${activity.id}">${activity.name}</a></td>
                                            <td width="40%"><a href="viewAnchorELO.html?anchorELOId=${activity.anchorELO.id}">${activity.anchorELO.name}</a></td>
                                            <td width="20%"><!--s:ajaxCheckBox model="${activity}" property="autoAddToStudentPlan"/--><input type="checkbox"/></td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br/>
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