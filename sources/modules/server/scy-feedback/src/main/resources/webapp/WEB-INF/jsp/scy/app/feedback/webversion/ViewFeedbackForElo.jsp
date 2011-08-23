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
                <input type="text" name="score" id="score" value="1" style="display:none;"/>
                Quality:
<div id="horizontalSlider" dojoType="dijit.form.HorizontalSlider" value="1" minimum="1" maximum="4" discreteValues="1" intermediateChanges="false" showButtons="false" style="width:400px;margin-top:5px;" onChange="document.getElementById('score').value = Math.round(this.value);">
    <ol dojoType="dijit.form.HorizontalRuleLabels" container="topDecoration" style="height:1.5em;font-size:75%;color:gray;">
        <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_1.png" alt=""  /></li>
        <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_2.png" alt=""  /></li>
        <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_3.png" alt=""  /></li>
        <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_4.png" alt=""  /></li>
    </ol>
    <div dojoType="dijit.form.HorizontalRule" container="bottomDecoration" count="4" style="height:5px;">
        <ol dojoType="dijit.form.HorizontalRuleLabels" container="bottomDecoration" style="height:1em;font-size:75%;color:gray;"></ol>
    </div>
</div>

<input type="Submit" value="GIVE/GET FEEDBACK" />
</form>





        </td>
    </tr>
</table>
<div id="feedbackReturnContainer"></div>
<div style="width:100%;" class="lightGreenBackgrounds">

<!--table-->
    <c:choose>
        <c:when test="${fn:length(feedbackElo.feedbacks) > 0}">
                <c:forEach var="feedbackItem" items="${feedbackElo.feedbacks}">
                    <div style="clear:both;">
                        <fieldset style="clear:both;border:1px solid #000000;margin:5px;" >
                            <legend style="font-weight:bold;border:1px solid #000000;padding:2px;">${feedbackItem.calendarDate}</legend>
                            <div style="float:left;width:10%;" class="greenBackgrounds greenBorders">

                                <div style="height:70px;width:70px;background-color:#ffffff;padding:2px;margin:3px;text-align:center;vertical-align:middle;">
                                <img src="/webapp/common/filestreamer.html?username=${feedbackItem.createdBy}&showIcon"/>
                                </div>
                            </div>
                            <div style="float:left;width:55%;padding:5px;">
                                 Time: ${feedbackItem.createdBy} wrote:
                                 <p>${feedbackItem.comment}</p>
                                <p><a href="#" style="color:#ffffff;">Reply on feeback</a></p>
                            </div>
                            <div style="float:left;width:30%;">
                                 Quality score:<br/>

                                <form action="/webapp/app/feedback/webversion/AddReplyToFeedback.html" method="POST" accept-charset="UTF-8" >

                                    <input type="hidden" name="feedbackId" value="${feedbackItem.id}"/>
                                    <input type="hidden" name="feedbackEloURI" value="${feedbackElo.uri}"/>
                                    <input type="textarea" name="reply" style="width:100%;height:50px;"/>
                                    <input type="submit">
                                </form>

                                <c:choose>
                                    <c:when test="${fn:length(feedbackItem.replies) > 0}">
                                        <table>
                                            <c:forEach var="reply" items="${feedbackItem.replies}">
                                                <tr>
                                                    <td>
                                                        <strong>${reply.calendarDate}</strong><br/>
                                                        ${reply.calendarTime}
                                                    </td>
                                                    <td>
                                                        ${reply.comment}
                                                    </td>
                                                </tr>

                                            </c:forEach>
                                        </table>
                                    </c:when>
                                </c:choose>


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