<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Anchor elos</h1>

        <p>Decide which elos should be obligatory part of the student portfolios. When a student adds elos to the portfolio, only the elo types that are defined as obligatory will be possible to add.</p>

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
                            ${anchorElo.elo.title}
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${anchorElo.elo.obligatoryInPortfolio == null || anchorElo.elo.obligatoryInPortfolio == false}">
                                    <a href="MissionHighLevelOverview.html?eloURI=${eloWrapper.uri}&action=flipObligatoryInPortfolio&anchorElo=${anchorElo.uri}">
                                        NO
                                    </a>

                                </c:when>
                                <c:when test="${anchorElo.elo.obligatoryInPortfolio}">
                                    <a href="MissionHighLevelOverview.html?eloURI=${eloWrapper.uri}&action=flipObligatoryInPortfolio&anchorElo=${anchorElo.uri}">
                                        YES
                                    </a>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>

                </c:forEach>
                </table>
            </c:when>
        </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>