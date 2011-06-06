<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Activity centric view scenario: ${model.name}</h1>
        <table>
            <tr>
                <th width="35%">Scenario properties</th>
                <th width="65%">Values</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Name
                </td>
                <td>
                     <s:ajaxTextField property="name" model="${model}"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Description
                </td>
                <td>
                     <s:ajaxTextField property="description" model="${model}" isMultiLine="true"/>
                </td>
            </tr>
        </table>
        <br/>
        <br/>

        <c:choose>
            <c:when test="${fn:length(learningActivitySpaces) > 0}">
                <table id="pedagogicalPlansTable" width="100%">
                    <tr>
                        <th width="20%">Learning activity space</th>
                        <th width="80%">
                            <table width="100%">
                                <th width="25%">Activity</th>
                                <th width="25%">Work arrangement</th>
                                <th width="25%">Teacher role</th>
                                <th width="25%">Expected duration</th>
                            </table>
                        </th>
                    </tr>
                    <c:forEach var="las" items="${learningActivitySpaces}">
                        <tr class="${oddEven.oddEven}">
                            <td width="20%"><a href="viewLAS.html?id=${las.id}">${las.name}</a></td>
                            <td width="80%">
                                <table width="100%">
                                    <c:forEach var="activity" items="${las.activities}">
                                        <tr>
                                            <td width="25%"><a href="viewActivity.html?activityId=${activity.id}">${activity.name}</a></td>
                                            <td width="25%"><s:ajaxCombobox property="workArrangementType" model="${activity}" comboBoxValues="${workArrangement}"/></td>
                                            <td width="25%"><s:ajaxCombobox property="teacherRoleType" model="${activity}" comboBoxValues="${teacherRoles}"/></td>
                                            <td width="25%"><s:ajaxNumberField model="${activity}" property="expectedDurationInMinutes"/></td>
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
                <table id="learningGoalsTable" width="100%">
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

        <br/>
        <br/>
        <a href="viewScenario.html?pedagogicalPlanId=${pedagogicalPlan.id}">Normal scenario view</a>
        <a href="viewScenarioGraphically.html?pedagogicalPlanId=${pedagogicalPlan.id}">Graphical scenario view</a>

    </tiles:putAttribute>
</tiles:insertDefinition>