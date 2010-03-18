<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

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

    </tiles:putAttribute>
</tiles:insertDefinition>