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
        <div dojoType="dojox.layout.ContentPane" executeScripts="true" parseOnLoad="true" style="border:4px solid #cc6600;width:786px;height:95%;padding:4px;" class="greenBorders" parseWidgets="true">
            <div class="feedbackHeader" >ELO Gallery</div>
    <div dojoType="dojox.layout.ContentPane" parseOnLoad="true" executeScripts="true">
        <h2>Give feedback</h2>
    <!--table>
        <tr>
            <td style="width:50%;"-->
        <div dojoType="dojox.layout.ContentPane" style="width:50%;float:left;" executeScripts="true" parseOnLoad="true">
                <a href="javascript:loadDialog('/webapp/components/openEloInScyLabDialog.html?eloURI=${transferElo.uri}', '${transferElo.myname}');">
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
                <form method="POST" accept-charset="UTF-8" action="/webapp/app/feedback/webversion/AddFeedback.html" onsubmit="postFeedback(this, document.getElementById('feedbackReturnContainer'), true, 'before');document.getElementById('feedbacktext').value='';return false;">
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


                                 </div>
                                <div style="clear:both;"></div>
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
                                    <form action="/webapp/app/feedback/webversion/AddReplyToFeedback.html" method="POST" accept-charset="UTF-8" onsubmit="postFeedback(this, document.getElementById('feedback_on_feedback_${feedbackItem.id}'), true, 'after');document.getElementById('feedbackItem${feedbackItem.id}').value='';return false;">

                                        <input type="hidden" name="feedbackId" value="${feedbackItem.id}"/>
                                        <input type="hidden" name="feedbackEloURI" value="${feedbackElo.uri}"/>
                                        <input type="textarea" name="reply" id="feedbackItem${feedbackItem.id}" style="width:100%;height:50px;"/>
                                        <input type="submit">
                                    </form>


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
        </div>
    </div>
</tiles:putAttribute>
</tiles:insertDefinition>
