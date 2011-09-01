<%@ include file="common-taglibs.jsp" %>
<div dojoType="dojox.layout.ContentPane" parseOnLoad="true" executeScripts="true">
    <p>Give feedback</p>
<!--table>
    <tr>
        <td style="width:50%;"-->
    <div dojoType="dojox.layout.ContentPane" style="width:50%;float:left;" executeScripts="true" parseOnLoad="true">
            <a href="javascript:openDialog('${transferElo.myname}');">
                <img src="${transferElo.thumbnail}" alt="" style="border:0;"/>
            </a>
            <br/>
            <strong>${transferElo.myname}</strong><br/>
            By: ${transferElo.createdBy}<br/>
            Entered: ${transferElo.createdDate} <br/>
            Catname: ${transferElo.catname}<br/>
            Viewed: ${feedbackElo.shown}<br/>
            Quality Score Average: ${elo.grade}<br/>
            Comment: ${elo.comment}

    </div>
    <div dojoType="dojox.layout.ContentPane" style="width:50%;float:left;" executeScripts="true" parseOnLoad="true">
        <!--/td>
        <td style="width:50%;"-->
            <!--form method="POST" accept-charset="UTF-8" action="/webapp/app/feedback/webversion/AddFeedback.html" onsubmit="postFeedback(this, 'feedbackReturnContainer', this.parentNode.parentNode.parentNode.parentNode.parentNode.childNodes[5]);return false;"-->
            <form method="POST" accept-charset="UTF-8" action="/webapp/app/feedback/webversion/AddFeedback.html" onsubmit="postFeedback(this, document.getElementById('feedbackReturnContainer'));return false;">
                <textarea id="feedbacktext" name="feedbacktext"style="width:90%;height:50px;"></textarea><br/>
                <input type="hidden" name="feedbackEloURI" id="feedbackEloURI" value="${feedbackElo.uri}"/>
                <!--s:ajaxELOSlider sliderValues="${feedbackLevels}" defaultValue="${scaffoldingLevel}" eloURI="${transferElo.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/-->
                <input type="text" name="score" id="score" value="1" style="display:none;"/>
                Quality:
<div id="horizontalSlider" dojoType="dijit.form.HorizontalSlider" value="1" minimum="1" maximum="4" discreteValues="1" intermediateChanges="false" showButtons="false" style="width:90%;margin-top:5px;" onChange="document.getElementById('score').value = Math.round(this.value);">
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



   </div>
          <div style="clear:both;"></div>
        <!--/td>
    </tr>
</table-->

<div style="width:100%;" >
  <div dojoType="dojox.layout.ContentPane" id="feedbackReturnContainer"></div>
<!--table-->
    <c:choose>
        <c:when test="${fn:length(feedbackElo.feedbacks) > 0}">
                <c:forEach var="feedbackItem" items="${feedbackElo.feedbacks}">
                    <div style="clear:both;border-top:1px dashed #666666;" >
                        <fieldset style="clear:both;margin:5px;" >
                            <legend style="font-weight:bold;padding:2px;">${feedbackItem.calendarDate}</legend>
                            <div style="float:left;width:10%;" class="greenBackgrounds greenBorders"  >

                                <div style="height:70px;width:70px;background-color:#ffffff;padding:2px;margin:3px;text-align:center;vertical-align:middle;"  >
                                <img src="/webapp/common/filestreamer.html?username=${feedbackItem.createdBy}&showIcon"/>
                                </div>
                            </div>
                            <div style="width:40%;padding:5px;float:left;"  >
                                 ${feedbackItem.calendarTime}: ${feedbackItem.createdBy} wrote:
                                 <p>${feedbackItem.comment}</p>
                                <!--p><a href="#" style="color:#ffffff;">Reply on feeback</a></p-->
                            </div>
                            <div style="float:left;width:40%;" >
                                 Quality score: <img src="/webapp/themes/scy/default/images/smiley_${feedbackItem.evalu}.png" alt=""  /><br/>

                                <form action="/webapp/app/feedback/webversion/AddReplyToFeedback.html" method="POST" accept-charset="UTF-8" onsubmit="postFeedback(this, document.getElementById('feedback_on_feedback_${feedbackItem.id}'));return false;">

                                    <input type="hidden" name="feedbackId" value="${feedbackItem.id}"/>
                                    <input type="hidden" name="feedbackEloURI" value="${feedbackElo.uri}"/>
                                    <input type="textarea" name="reply" style="width:100%;height:50px;"/>
                                    <input type="submit">
                                </form>
                             </div>
                            <div style="clear:both;"></div>
                                <c:choose>
                                    <c:when test="${fn:length(feedbackItem.replies) > 0}">
                                        <div id="feedback_on_feedback_${feedbackItem.id}" ></div>
                                        <table style="border:0 !important;margin:5px;" >
                                            <c:forEach var="reply" items="${feedbackItem.replies}">
                                                <tr class="extraLightGreenBackgrounds">
                                                    <td style="width:30%;">
                                                        <strong>${reply.calendarDate}</strong><br/>
                                                        ${reply.calendarTime} ${reply.createdBy} wrote:
                                                    </td>
                                                    <td style="width:70%;">
                                                        ${reply.comment}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" style="height:7px;background-color:#ffffff;"></td>
                                                </tr>

                                            </c:forEach>
                                        </table>

                                    </c:when>
                                </c:choose>




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
    </div>