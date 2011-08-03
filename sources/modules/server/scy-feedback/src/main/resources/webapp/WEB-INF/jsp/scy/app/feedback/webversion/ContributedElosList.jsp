<%@ include file="common-taglibs.jsp" %>
<c:choose>
    <c:when test="${fn:length(elos) > 0}">
            <c:forEach var="elo" items="${elos}">
                <div dojoType="dojox.layout.ContentPane" class="feedbackEloContainer" style="width:30%;height:246px;float:left;">
                ${elo.myname}
                ${elo.uri}
                        ${elo.technicalFormat}
                </div>
            </c:forEach>

    </c:when>
</c:choose>
