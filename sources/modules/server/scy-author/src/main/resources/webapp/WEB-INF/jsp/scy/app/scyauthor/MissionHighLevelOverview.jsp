<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
            <c:when test="${fn:length(learningActivitySpaces) > 0}">
                <table>


                <c:forEach var="las" items="${learningActivitySpaces}">
                    <tr class="${oddEven.oddEven}">
                        <td>
                            ${las.toolTip}
                        </td>
                    </tr>

                </c:forEach>
                </table>
            </c:when>
        </c:choose>


        <h1>Anchor elos</h1>
        <c:choose>
            <c:when test="${fn:length(anchorElos) > 0}">
                <table>


                <c:forEach var="anchorElo" items="${anchorElos}">
                    <tr class="${oddEven.oddEven}">
                        <td>
                            ++ ${anchorElo.scyElo.title}
                        </td>
                    </tr>

                </c:forEach>
                </table>
            </c:when>
        </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>