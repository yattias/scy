<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="main">
        <script type="text/javascript">
            function loadAccordionContent(container, url){

                dijit.byId(container).set('href', url);
            }

            function loadAccordionContentWithEscapedURL(container, url){
               dijit.byId(container).attr('href', escape(url));
            }



            function openDialog(title){
                var dialog = new dijit.Dialog({
                    title:title,
                    content: "<h2>Show the ELO here!</h2>"
                });
                dialog.style.width = "600px";

                dialog.show();
            }

            function showBlockLevelElement(elementId){
                document.getElementById(elementId).style.display = 'block';
            }
        </script>
        <style type="text/css">

             html, body { height: 100%; width: 100%; margin: 0; padding: 0; }

            .feedbackEloContainer{
                border:2px solid #ff9933;
                margin:2px;
                /*background-color:#cc6600;*/
            }

            .feedbackEloContainer .thumbContainer{
                margin:2px;
                padding:2px;
                text-align:center;
                /*background-color:#e5994c;*/
            }

            .feedbackEloContainer .eloInfoContainer{
                padding:2px;
            }

            .soria .dijitAccordionTitle {
               background-color:#6bcfdf !important;
                background-image:url() !important;

            }

            .soria .dijitAccordionTitle-selected{
                background-color:#03a5be !important;
                background-image:url() !important;
                color:#ffffff;
            }

            .greenBackgrounds{
                background-color:#03a5be !important;
            }

            .lightGreenBackgrounds{
                background-color:#6bcfdf !important;
            }
            .extraLightGreenBackgrounds{
                background-color:#e3f5f8 !important;
            }

            .greenBorders{
                border-color:#03a5be !important;
            }

            .feedbackHeader{
                background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                background-repeat:no-repeat;
                color:#ffffff;
                height:50px;
                background-color:#03a5be !important;
                font-weight:bold;
                font-size:25px;
                text-align:center;
                padding-top:20px;
            }

        </style>
        <div dojoType="dojox.layout.ContentPane" executeScripts="true" parseOnLoad="true" style="border:4px solid #cc6600;border-bottom-left-radius:40px;width:786px;height:95%;padding:4px;" class="greenBorders" parseWidgets="true">
            <div class="feedbackHeader" >Give/Get Feedback</div>
    <div dojoType="dojox.layout.ContentPane" parseOnLoad="true" executeScripts="true">



    <!--table>
        <tr>
            <td style="width:50%;"-->
        <div dojoType="dojox.layout.ContentPane" style="width:50%;float:left;" executeScripts="true" parseOnLoad="true">
                <a href="javascript:loadDialog('/webapp/components/openEloInScyLabDialog.html?eloURI=${transferElo.uri}', '${transferElo.myname}');">
                    <img src="${transferElo.thumbnail}" alt="" style="border:0;"/>
                </a>

                <br/>
                <strong>${transferElo.myname}</strong><br/>
                <table style="border:white">
                    <tr>
                        <td width="30%">
                            By:
                        </td>
                        <td>
                            ${transferElo.createdBy}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Entered:
                        </td>
                        <td>
                            ${transferElo.createdDate}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Viewed:
                        </td>
                        <td>
                            ${feedbackElo.shown}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Total Score:
                        </td>
                        <td>
                            ${feedbackElo.score}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Average score:
                        </td>
                        <td>
                            <img src="/webapp/themes/scy/default/images/smiley_${feedbackElo.averageScore}.png" alt=""  />
                        </td>
                    </tr>
                </table>

        </div>
        <div dojoType="dojox.layout.ContentPane" style="width:50%;float:left;" executeScripts="true" parseOnLoad="true">
            <strong><i>${feedbackElo.comment}</i></strong>
            <br/>
            <!--/td>
            <td style="width:50%;"-->
                <!--form method="POST" accept-charset="UTF-8" action="/webapp/app/feedback/webversion/AddFeedback.html" onsubmit="postFeedback(this, 'feedbackReturnContainer', this.parentNode.parentNode.parentNode.parentNode.parentNode.childNodes[5]);return false;"-->
                <form method="POST" accept-charset="UTF-8" action="/webapp/app/feedback/webversion/AddFeedback.html" onsubmit="postFeedback(this, document.getElementById('feedbackReturnContainer'), true, 'after');document.getElementById('feedbacktext').value='';return false;">

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

    <input type="Submit" value="Give Feedback" />
    </form>



       </div>
              <div style="clear:both;"></div>
            <!--/td>
        </tr>
    </table-->

    <div style="width:100%;" >
      <div dojoType="dojox.layout.ContentPane" id="feedbackReturnContainer"></div>

        <c:choose>
            <c:when test="${fn:length(feedbackElo.feedbacks) > 0}">
                <table style="border:none;">


                <c:forEach var="feedbackItem" items="${feedbackElo.feedbacks}">
                    <tr>
                        <td colspan="4">
                             <div style="clear:both;border-top:1px dashed #666666;" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <img src="/webapp/common/filestreamer.html?username=${feedbackItem.createdBy}&showIcon"/>
                        </td>
                        <td>
                             ${feedbackItem.calendarDate} ${feedbackItem.calendarTime}: ${feedbackItem.createdBy} wrote:

                        </td>
                        <td>
                            ${feedbackItem.comment}
                        </td>
                        <td>
                            Quality score: <img src="/webapp/themes/scy/default/images/smiley_${feedbackItem.evalu}.png" alt=""  /><br/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <c:choose>
                                <c:when test="${fn:length(feedbackItem.replies) > 0}">

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
                            <div id="feedback_on_feedback_${feedbackItem.id}" ></div>
                            <a href="javascript:showBlockLevelElement('feedbackOnFeedbackForm${feedbackItem.id}');">Give feedback</a>
                            <form action="/webapp/app/feedback/webversion/AddReplyToFeedback.html" style="display:none;" id="feedbackOnFeedbackForm${feedbackItem.id}" method="POST" accept-charset="UTF-8" onsubmit="postFeedback(this, document.getElementById('feedback_on_feedback_${feedbackItem.id}'), true, 'after');document.getElementById('feedbackItem${feedbackItem.id}').value='';document.getElementById('feedbackOnFeedbackForm${feedbackItem.id}').style.display='none';return false;">

                                <input type="hidden" name="feedbackId" value="${feedbackItem.id}"/>
                                <input type="hidden" name="feedbackEloURI" value="${feedbackElo.uri}"/>
                                <input type="textarea" name="reply" id="feedbackItem${feedbackItem.id}" style="width:100%;height:50px;border:2px solid #03a5be;"/>
                                <input type="submit">
                            </form>
                        </td>
                    </tr>

                            <!--fieldset style="clear:both;margin:5px;" >
                                <legend style="font-weight:bold;padding:2px;"</legend>
                                <div style="float:left;width:10%;" class="greenBackgrounds greenBorders"  >

                                    <div style="height:70px;width:70px;background-color:#ffffff;padding:2px;margin:3px;text-align:center;vertical-align:middle;"  >
                                    <img src="/webapp/common/filestreamer.html?username=${feedbackItem.createdBy}&showIcon"/>
                                    </div>
                                </div>
                                <div style="width:40%;padding:5px;float:left;"  >
                                     ${feedbackItem.calendarTime}: ${feedbackItem.createdBy} wrote:
                                     <p>${feedbackItem.comment}</p>

                                </div>
                                <div style="float:left;width:40%;" >



                                 </div>
                                <div style="clear:both;"></div-->



                            <!--/fieldset-->

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
        </div>
    </div>
</tiles:putAttribute>
</tiles:insertDefinition>
