<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <style type="text/css">

        .assessmentHeader{
                background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                background-repeat:no-repeat;
                color:#ffffff;
                height:50px;
                background-color:#686808 !important;
                font-weight:bold;
                font-size:25px;
                text-align:center;
                padding-top:20px;
            }

        </style>




        <div style="border:4px solid #686808;width:786px;height:95%;padding:4px;" class="brownBorders">
                    <!--img src="/webapp/themes/scy/default/images/feedback_header.png" alt="" class="greenBackgrounds" /-->
                    <div class="assessmentHeader" >Assess ELO</div>
                    
                <div dojoType="dojox.layout.ContentPane" style="width:100%;height:90%;" id="eportfolioPane" parseOnLoad="true" executeScripts="true">

                    <c:choose>
                        <c:when test="${fn:length(elos) > 0}">
                            <table>
                                <c:forEach var="elo" items="${elos}">
                                    <tr>
                                        <td>
                                            <img src="${elo.thumbnail}"/>
                                        </td>
                                        <td>
                                            <a href="assessElo.html?eloURI=${elo.uri}&missionRuntimeURI=${missionRuntimeURI}">${elo.myname}</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </table>
                        </c:when>
                    </c:choose>

        </div>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>