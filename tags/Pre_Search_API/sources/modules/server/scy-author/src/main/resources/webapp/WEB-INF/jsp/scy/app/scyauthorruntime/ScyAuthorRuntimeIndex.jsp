<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <h1>WELCOME TO SCYAuthor Runtime!</h1>

        <c:choose>
            <c:when test="${fn:length(missionTransporters) > 0}">
                <table id="pedagogicalPlansTable" width="100%">
                    <tr>
                        <th>
                            Name
                        </th>
                    </tr>
                    <c:forEach var="missionTransporter" items="${missionTransporters}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <a href="viewActivePedagogicalPlan.html?eloURI=${missionTransporter.uri}">${missionTransporter.elo.title}</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br/>

            </c:when>
        </c:choose>


        <c:choose>
            <c:when test="${fn:length(pedagogicalPlans) > 0}">
                <table id="pedagogicalPlansTable">
                    <tr>
                        <th>Pedagogical Plan</th>
                        <th>Active</th>
                        <th>Number of online students</th>
                    </tr>
                    <c:forEach var="pedagogicalPlan" items="${pedagogicalPlans}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <a href="viewActivePedagogicalPlan.html?id=${pedagogicalPlan.id}">${pedagogicalPlan.name}</a>
                            </td>
                            <td><s:ajaxCheckBox model="${pedagogicalPlan}" property="published"/></td>
                            <td>?</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>

        <!--a href="ScyAuthorRuntimeGraphicalView.html">Graphical runtime view</a-->
    </tiles:putAttribute>
</tiles:insertDefinition>