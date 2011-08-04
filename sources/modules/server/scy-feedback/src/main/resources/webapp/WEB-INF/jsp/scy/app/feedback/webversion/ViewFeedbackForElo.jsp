<%@ include file="common-taglibs.jsp" %>
<p>Give feedback</p>
<table>
    <tr>
        <td style="width:30%;">
            <a href="javascript:openDialog('${transferElo.myname}');">
                <img src="${transferElo.thumbnail}" alt="" style="border:0;"/>
            </a>
            <br/>
            <strong>${transferElo.myname}</strong><br/>
            By: ${transferElo.createdBy}<br/>
            Entered: ${transferElo.createdDate} <br/>
            Catname: ${transferElo.catname}<br/>
            Viewed: ${feedbackElo.shown}<br/>
            Quality Score Average: ${elo.grade}
        </td>
        <td style="width:70%;">
            <form method="POST" accept-charset="UTF-8" action="/webapp/app/feedback/webversion/AddFeedback.html" onsubmit="postFeedback(this, 'feedbackReturnContainer');return false;">
                <textarea id="feedbacktext" name="feedbacktext"style="width:100%;height:50px;"></textarea><br/>
                <input type="hidden" name="feedbackEloURI" id="feedbackEloURI" value="${feedbackElo.uri}"/>
                <!--s:ajaxELOSlider sliderValues="${feedbackLevels}" defaultValue="${scaffoldingLevel}" eloURI="${transferElo.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/-->
                <input type="text" name="score" id="score"/>
                Quality: (Slider here) |----------------------|-----------------------|--------------------|
                <input type="Submit" value="GIVE/GET FEEDBACK" />
            </form>





        </td>
    </tr>
</table>
<div style="width:100%;" class="lightGreenBackgrounds">
    <div id="feedbackReturnContainer"></div>
<!--table-->
    <c:choose>
        <c:when test="${fn:length(feedbackElo.feedbacks) > 0}">
                <c:forEach var="feedbackItem" items="${feedbackElo.feedbacks}">
                    <div style="clear:both;">
                        <fieldset style="clear:both;border:1px solid #000000;margin:5px;" >
                            <legend style="font-weight:bold;border:1px solid #000000;padding:2px;">DATE ...</legend>
                            <div style="float:left;width:10%;" class="greenBackgrounds greenBorders">

                                <div style="height:40px;width:35px;background-color:#ffffff;padding:2px;margin:3px;">
                                ${feedbackItem.createdByPicture}
                                </div>
                            </div>
                            <div style="float:left;width:55%;padding:5px;">
                                 Time: ${feedbackItem.createdBy} wrote:
                                 <p>${feedbackItem.comment}</p>
                                <p><a href="#" style="color:#ffffff;">Reply on feeback</a></p>
                            </div>
                            <div style="float:left;width:30%;">
                                 Quality score:<br/>

                                <img src="/webapp/themes/scy/default/images/smiley_${feedbackItem.evalu}.png" alt=""  />
                            </div>
                            <div style="clear:both;"></div>
                        </fieldset>    

                    </div>

                    <!--tr>
                        <td>
                            ${feedbackItem.comment}
                        </td>
                        <td>
                            ${feedbackItem.evalu}
                        </td>
                        <td>
                            ${feedbackItem.createdBy}
                        </td>
                        <td>
                            ${feedbackItem.createdByPicture}
                        </td>
                    </tr-->

                </c:forEach>
        </c:when>
    </c:choose>
 </div>
<!--/table-->

<!--table>
    <tr>
        <td style="width:100%;height:200px;">
            <div dojoType="dojox.layout.ContentPane" href="${transferElo.feedbackEloUrl}">
                
            </div>
        </td>
    </tr>
</table-->
<!--a href="javascript:loadAccordionContent('newestElosContainer', '/webapp/app/feedback/webversion/NewestElosList.html?eloURI=${missionURI}');">Cancel</a-->


<!--Her maa du see paa TransferElo for aa se hvilke properties du kan hente ut-->