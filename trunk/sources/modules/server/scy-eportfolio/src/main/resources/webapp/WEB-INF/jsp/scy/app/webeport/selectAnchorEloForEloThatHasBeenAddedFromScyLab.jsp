<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <h2><spring:message code="SELECT_ANCHOR_ELO_FOR_ELO"/> ${elo.myname} </h2>

        <c:choose>
            <c:when test="${fn:length(anchorElos) > 0}">
                <c:forEach var="anchorElo" items="${anchorElos}">
                    
                </c:forEach>
            </c:when>
        </c:choose>

    </tiles:putAttribute>
</tiles:insertDefinition>