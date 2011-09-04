<%@ include file="common-taglibs.jsp" %>
<table style="border:none;">
    <tr class="extraLightGreenBackgrounds">
        <td style="width:30%;">
            <strong>${feedbackReply.calendarDate}</strong><br/>
            ${feedbackReply.calendarTime} ${feedbackReply.createdBy} wrote:
        </td>
        <td style="width:70%;">${feedbackReply.comment}</td>
    </tr>


</table>