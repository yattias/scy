<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h1>Learning Activity Space</h1>
        <table width="100%" border="2">
            <tr class="tablerow-odd">
                <td><strong>Name</strong></td>
                <td><s:ajaxTextField property="name" model="${model}"/></td>
            </tr>
            <tr class="tablerow-even">
                <td><strong>Description</strong></td>
                <td><s:ajaxTextField property="description" model="${model}"/></td>
            </tr>
        </table>
        <br/>

        <div>
            <c:choose>
                <c:when test="${fn:length(model.activities) > 0}">
                    <table id="activityTable" border="2"  width="100%">
                        <h2>Activities</h2>
                        <tr>
                            <th>Activity</th>
                            <th>ELO</th>
                            <th>Tools</th>
                            <th>Auto add to student plan</th>
                        </tr>
                        <c:forEach var="activity" items="${model.activities}">
                            <tr>
                                <td><a href="viewActivity.html?activityId=${activity.id}">${activity.name}</a></td>
                                <td>${activity.anchorELO.name}</td>
                                <td>
                                    <s:dialog url="selectToolForActivity.html?id=${activity.id}&clazz=${activity.class.name}" dialogHeader="Tools" title="+"/>
                                </td>
                                <td><!--s:ajaxCheckBox model="${activity}" property="autoAddToStudentPlan"/--><input type="checkbox"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br>
                </c:when>
            </c:choose>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>