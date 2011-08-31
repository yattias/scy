<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


            <c:choose>
                <c:when test="${fn:length(elos) > 0}">
                    <c:forEach var="elo" items="${elos}">

                        <div dojoType="dojox.layout.ContentPane" class="feedbackEloContainer greenBackgrounds greenBorders" style="width:30%;height:246px;float:left;">
                            <div class="thumbContainer lightGreenBackgrounds">
                                <img src="${elo.thumbnail}"/>
                            </div>
                    <div class="eloInfoContainer">
                        <table>
                            <tr>
                                <td>
                                    <strong>Title</strong>
                                </td>
                                <td>
                                    ${elo.title}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <strong>Last modified</strong>
                                </td>
                                <td>
                                    ${elo.date}
                                </td>
                            </tr>
                        </table>
                          
                    </div>
                </div>




                    </c:forEach>
                </c:when>
            </c:choose>
        

        </tiles:putAttribute>
</tiles:insertDefinition>