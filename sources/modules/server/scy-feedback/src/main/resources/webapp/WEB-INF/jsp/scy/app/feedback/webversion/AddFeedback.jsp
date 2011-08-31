<%@ include file="common-taglibs.jsp" %>

FEEDBACK ADDED ${feedbacktext}


<div style="clear:both;" dojoType="dojox.layout.ContentPane" parseOnLoad="true" executeScripts="true">
    <fieldset style="clear:both;border:1px solid #000000;margin:5px;">
        <legend style="font-weight:bold;border:1px solid #000000;padding:2px;">${feedbackItem.calendarDate}</legend>
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
            <!--p><a href="#" style="color:#ffffff;">Reply on feeback</a></p-->
        </div>
        <div style="float:left;width:40%;" dojoType="dojox.layout.ContentPane" parseOnLoad="true" executeScripts="true">
            Quality score: <img src="/webapp/themes/scy/default/images/smiley_${feedbackItem.evalu}.png" alt=""/><br/>

            <form action="/webapp/app/feedback/webversion/AddReplyToFeedback.html" method="POST" accept-charset="UTF-8"
                  onsubmit="postFeedback(this, 'feedback_on_feedback_${feedbackItem.id}');return false;">

                <input type="hidden" name="feedbackId" value="${feedbackItem.id}"/>
                <input type="hidden" name="feedbackEloURI" value="${feedbackElo.uri}"/>
                <input type="textarea" name="reply" style="width:100%;height:50px;"/>
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

</div>