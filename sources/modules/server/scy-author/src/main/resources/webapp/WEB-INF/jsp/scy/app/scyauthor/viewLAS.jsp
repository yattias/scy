<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h1>Learning Activity Space</h1>
        <table width="100%" border="2">
            <tr>
                <td><strong>Name</strong></td>
                <td>${model.name}</td>
            </tr>
            <tr>
                <td><strong>Description</strong></td>
                <td>${model.description}</td>
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
                        </tr>
                        <c:forEach var="activity" items="${model.activities}">
                            <tr>
                                <td><a href="viewActivity.html?activityId=${activity.id}">${activity.name}</a></td>
                                <td>${activity.anchorELO.name}</td>
                                <td><a href="selectToolForActivity.html?id=${activity.id}&clazz=${activity.class.name}">+</a></td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br>
                </c:when>
            </c:choose>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>
