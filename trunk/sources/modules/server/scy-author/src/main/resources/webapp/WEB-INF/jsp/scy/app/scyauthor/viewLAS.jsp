<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h1>Learning Activity Space</h1>
        <table width="100%">
            <tr>
                <th width="35%">Learning Activity Space properties</th>
                <th width="65%">Values</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td><strong>Name</strong></td>
                <td><s:ajaxTextField property="name" model="${model}"/></td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td><strong>Description</strong></td>
                <td><s:ajaxTextField property="description" model="${model}" isMultiLine="true"/></td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td><strong>Assessment</strong></td>
                <td><s:ajaxTextField property="name" model="${model.assessment}"/></td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td><strong>Assessment strategy</strong></td>
                <td><s:ajaxCombobox property="assessmentStrategyType" model="${model.assessment}" comboBoxValues="${assessmentStrategies}"/></td>
            </tr>
        </table>
        <br/>

        <div>
            <c:choose>
                <c:when test="${fn:length(model.activities) > 0}">
                    <table id="activityTable" width="100%">
                        <h2>Activities</h2>
                        <tr>
                            <th>Activity</th>
                            <th>ELO</th>
                            <th>Tools</th>
                            <th>Auto add to student plan</th>
                        </tr>
                        <c:forEach var="activity" items="${model.activities}">
                            <tr class="${oddEven.oddEven}">
                                <td><a href="viewActivity.html?activityId=${activity.id}">${activity.name}</a></td>
                                <td>${activity.anchorELO.name}</td>
                                <td>
                                    <s:dialog url="selectToolForActivity.html?id=${activity.id}&clazz=${activity.class.name}" dialogHeader="Tools" title="+"/>
                                </td>
                                <td><s:ajaxCheckBox model="${activity}" property="autoaddToStudentPlan"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br>
                </c:when>
            </c:choose>
        </div>        
    </tiles:putAttribute>
</tiles:insertDefinition>
