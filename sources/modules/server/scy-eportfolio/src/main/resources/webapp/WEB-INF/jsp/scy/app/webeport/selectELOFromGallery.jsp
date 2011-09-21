<%@ include file="common-taglibs.jsp" %>

<tiles:insertDefinition name="default-page">

    <tiles:putAttribute name="main">
        <style type="text/css">
        .feedbackHeader{
                background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                background-repeat:no-repeat;
                color:#ffffff;
                height:50px;
                background-color:#333333 !important;
                font-weight:bold;
                font-size:25px;
                text-align:center;
                padding-top:20px;
            }
    </style>
        <div style="border:4px solid #333333;border-bottom-left-radius:40px;width:786px;height:95%;padding:4px;">
                    <!--img src="/webapp/themes/scy/default/images/feedback_header.png" alt="" class="greenBackgrounds" /-->
                    <div class="feedbackHeader" >My ePortfolio</div>
                   
                <div dojoType="dojox.layout.ContentPane" style="width:100%;height:90%;" id="eportfolioPane" parseOnLoad="true" executeScripts="true">

            <c:choose>
                <c:when test="${fn:length(elos) > 0}">
                    <c:forEach var="elo" items="${elos}">
                       <a href="/webapp/app/webeport/editEloReflections.html?eloURI=${elo.uri}&anchorEloURI=${encodedAnchorEloURI}&missionRuntimeURI=${missionRuntimeURI}">
                        <div dojoType="dojox.layout.ContentPane" class="feedbackEloContainer" style="width:30%;height:246px;float:left;background-color:#333333;margin:2px;border:3px solid #333333;color:#ffffff;">


                                <div style="background-color:#cccccc;text-align:center;">

                                        <img src="${elo.thumbnail}"/>

                                </div>


                        <table style="border:0 !important;">
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
                            <tr>
                                <td>
                                    <strong>Created by</strong>
                                </td>
                                <td>
                                    ${elo.authors}
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                <c:choose>
                                    <c:when test="${elo.eloFinished}">
                                        FINISHED
                                    </c:when>
                                    <c:when test="${!elo.eloFinished}">
                                        NOT FINISHED                                        
                                    </c:when>
                                </c:choose>
                                </td>
                            </tr>
                        </table>
                          
                   
                </div>
                           </a>




                    </c:forEach>
                </c:when>
            </c:choose>
                   </div>
        </div>
       

        </tiles:putAttribute>
</tiles:insertDefinition>