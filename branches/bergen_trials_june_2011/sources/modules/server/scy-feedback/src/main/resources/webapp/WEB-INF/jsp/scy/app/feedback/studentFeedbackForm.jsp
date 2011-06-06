<%@ include file="common-taglibs.jsp" %>

<form method="POST" accept-charset="UTF-8" action="/webapp/app/feedback/studentFeedbackForm.html">'

    <table>
        <input type="hidden" name="action" value="addNewComment"/>
        <input type="hidden" name="modelId" value="${modelId}"/>
        <tr>
            <td><strong>Comment</strong></td>
            <td>
                <textarea name="comment" cols="40" rows="4"></textarea>
            </td>
        </tr>
        <tr>
            <td>
                <strong>Evaluation score</strong>
            </td>
            <td>
                <input type="radio" name="score" value="1">1<br>
                <input type="radio" name="score" value="2">2<br>
                <input type="radio" name="score" value="3">3<br>
                <input type="radio" name="score" value="4">4<br>
                <input type="radio" name="score" value="5">5<br>
                <input type="radio" name="score" value="6">6<br>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="right">
                <input type="submit"/>
            </td>
        </tr>
    </table>

</form>
