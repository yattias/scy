<%@ include file="../authorheader.jsp" %>

<h1>${pedagogicalPlan.name}</h1>
<p>
    ${pedagogicalPlan.description}
</p>

<div id="scenario_part">
    ${pedagogicalPlan.scenario.name}
</div>

   <c:choose>
        <c:when test="${fn:length(learningActivitySpaces) > 0}">
            <table id="pedagogicalPlansTable" border="2">
                <h5>Learning activity spaces</h5>
                <tr>
                    <th></th>
                </tr>
                <c:forEach var="las" items="${learningActivitySpaces}">
                    <tr>
                        <td><a href="viewLAS.html?id=${las.id}">${las.name}</a></td>
                    </tr>
                </c:forEach>
            </table>
            <br>
        </c:when>
    </c:choose>





<%@ include file="../authorfooter.jsp"%>