<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Anchor elos</h1>
        <c:choose>
            <c:when test="${fn:length(anchorElos) > 0}">
                <table>

                    <tr>
                        <th>
                            Name
                        </th>
                        <th>
                            Obligatory in portfolio
                        </th>
                    </tr>

                <c:forEach var="anchorElo" items="${anchorElos}">
                    <tr class="${oddEven.oddEven}">
                        <td>
                            ${anchorElo.scyElo.title}
                        </td>
                        <td>
                            ${anchorElo.obligatoryInPortfolio}
                            <s:ajaxELOTextField property="title" eloURI="${missionSpecificationTransporter.uri}" rooloServices="${rooloServices}"/>
                        </td>
                    </tr>

                </c:forEach>
                </table>
            </c:when>
        </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>