<%@ include file="common-taglibs.jsp" %>

 <table style="border:none;">
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
              <div id="feedback_on_feedback_${feedbackItem.id}" ></div>
                <a href="javascript:showBlockLevelElement('feedbackOnFeedbackForm${feedbackItem.id}');">Give feedback</a>
                <form action="/webapp/app/feedback/webversion/AddReplyToFeedback.html" style="display:none;" id="feedbackOnFeedbackForm${feedbackItem.id}" method="POST" accept-charset="UTF-8" onsubmit="postFeedback(this, document.getElementById('feedback_on_feedback_${feedbackItem.id}'), true, 'after');document.getElementById('feedbackItem${feedbackItem.id}').value='';document.getElementById('feedbackOnFeedbackForm${feedbackItem.id}').style.display = 'none';return false;">

                    <input type="hidden" name="feedbackId" value="${feedbackItem.id}"/>
                    <input type="hidden" name="feedbackEloURI" value="${feedbackEloUri}"/>
                    <input type="textarea" name="reply" id="feedbackItem${feedbackItem.id}" style="width:100%;height:50px;border:2px solid #03a5be;"/>
                    <input type="submit">
                </form>
         </td>
     </tr>



<!--div style="clear:both;border-top:1px dashed #666666;" dojoType="dojox.layout.ContentPane" parseOnLoad="true" executeScripts="true">
    <fieldset style="clear:both;margin:5px;">
        <legend style="font-weight:bold;padding:2px;">${feedbackItem.calendarDate}</legend>
        <div style="float:left;width:10%;" class="greenBackgrounds greenBorders" dojoType="dojox.layout.ContentPane"
             parseOnLoad="true" executeScripts="true">

            <div style="height:70px;width:70px;background-color:#ffffff;padding:2px;margin:3px;text-align:center;vertical-align:middle;"
                 dojoType="dojox.layout.ContentPane" parseOnLoad="true" executeScripts="true">
                <img src="/webapp/common/filestreamer.html?username=${feedbackItem.createdBy}&showIcon"/>
            </div>
        </div>
        <div style="float:left;width:45%;padding:5px;" dojoType="dojox.layout.ContentPane" parseOnLoad="true"
             executeScripts="true">
            ${feedbackItem.calendarTime}: ${feedbackItem.createdBy} wrote:
            <p>${feedbackItem.comment}</p>

        </div>
        <div style="float:left;width:40%;" dojoType="dojox.layout.ContentPane" parseOnLoad="true" executeScripts="true">
            Quality score: <img src="/webapp/themes/scy/default/images/smiley_${feedbackItem.evalu}.png" alt=""/><br/>
            <div id="feedback_on_feedback_${feedbackItem.id}" ></div>
            <form action="/webapp/app/feedback/webversion/AddReplyToFeedback.html" method="POST" accept-charset="UTF-8"
                  onsubmit="postFeedback(this, 'feedback_on_feedback_${feedbackItem.id}', true, 'after');document.getElementById('feedbackReplytext${feedbackItem.id}').value='';return false;">

                <input type="hidden" name="feedbackId" value="${feedbackItem.id}"/>
                <input type="hidden" name="feedbackEloURI" value="${feedbackElo.uri}"/>
                <input type="textarea" id="feedbackReplytext${feedbackItem.id}" name="reply" style="width:100%;height:50px;"/>
                <input type="submit">
            </form>

            <c:choose>
                <c:when test="${fn:length(feedbackItem.replies) > 0}">
                    <div dojoType="dojox.layout.ContentPane" id="feedback_on_feedback_${feedbackItem.id}"
                         parseOnLoad="true" executeScripts="true"></div>
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


        </div>
        <div style="clear:both;"></div>
    </fieldset>

</div-->