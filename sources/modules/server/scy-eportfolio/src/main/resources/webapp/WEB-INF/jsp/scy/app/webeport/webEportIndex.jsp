<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

    <div>
        <table>
            <tr>
                <td>owner</td>
                <td>${portfolio.owner}</td>
            </tr>
            <tr>
                <td>portfolioStatus</td>
                <td>${portfolio.portfolioStatus}</td>
            </tr>
            <tr>
                <td>missionRuntimeURI</td>
                <td>${portfolio.missionRuntimeURI}</td>
            </tr>
            <tr>
                <td>missionName</td>
                <td>${portfolio.missionName}</td>
            </tr>
            <tr>
                <td>reflectionMission</td>
                <td>${portfolio.reflectionMission}</td>
            </tr>
            <tr>
                <td>reflectionCollaboration</td>
                <td>${portfolio.reflectionCollaboration}</td>
            </tr>
            <tr>
                <td>reflectionInquiry</td>
                <td>${portfolio.reflectionInquiry}</td>
            </tr>
            <tr>
                <td>reflectionEffort</td>
                <td>${portfolio.reflectionEffort}</td>
            </tr>
            <tr>
                <td>assessmentPortfolioComment</td>
                <td>${portfolio.assessmentPortfolioComment}</td>
            </tr>
            <tr>
                <td>assessmentPortfolioRating</td>
                <td>${portfolio.assessmentPortfolioRating}</td>
            </tr>
            <tr>
                <td>assessed</td>
                <td>${portfolio.assessed}</td>
            </tr>
        </table>
    </div>



    <c:choose>
        <c:when test="${fn:length(obligatoryAnchorElos) > 0}">
            <c:forEach var="anchorElo" items="${obligatoryAnchorElos}">
                ${anchorElo.myname}
            </c:forEach>
        </c:when>
    </c:choose>



    </tiles:putAttribute>
</tiles:insertDefinition>