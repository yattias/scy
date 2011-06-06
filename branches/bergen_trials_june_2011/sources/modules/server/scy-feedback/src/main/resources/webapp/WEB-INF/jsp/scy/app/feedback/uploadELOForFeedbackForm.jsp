<%@ include file="common-taglibs.jsp" %>
<!--form enctype="multipart/form-data" accept-charset="UTF-8" action="/webapp/app/feedback/uploadELOForFeedbackForm.html" onsubmit="postForm(this, this.parentNode);return false;"-->
<form method="POST" enctype="multipart/form-data" accept-charset="UTF-8" action="/webapp/app/feedback/uploadELOForFeedbackForm.html">
    <table>
        <tr width="100%">
            <td align="left">
                <strong>File</strong>
            </td>
            <td align="left">
                <input type="file" name="file"/>
            </td>
        </tr>

        <tr>
            <td align="left"><strong>ELO Title</strong></td>
            </td>
            <td align="left" >
                <input type="text" name="name">
            </td>
        </tr>

        <tr>
            <td align="left">
                <strong>
                    Authors
                </strong>
            </td>
            <td align="left">
                ${currentUser.userDetails.firstName}&nbsp;${currentUser.userDetails.lastName}
            </td>
        </tr>
        <tr>
            <td align="left">
                <strong>
                    ELO Type
                </strong>
            </td>
            <td align="left">
                 <c:choose>
                    <c:when test="${fn:length(elosToBeAssessed) > 0}">
                        <select name="anchorEloId">
                            <c:forEach var="eloToBeAssessed" items="${elosToBeAssessed}">
                                <option value="${eloToBeAssessed.id}">${eloToBeAssessed.humanReadableName}</option>
                            </c:forEach>
                        </select>
                    </c:when>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td align="left">
                <strong>
                    Comment/Question
                </strong>
            </td>
            <td align="left">
                <textarea rows="4" cols="40" name="comment"></textarea> 

            </td>
        </tr>
        <input type="hidden" name="action" value="addNewEloRef"/>
        <input type="hidden" name="username" value="${currentUser.userDetails.username}"/>
        <tr>
            <td colspan="2" align="left">
                <input type="submit" title="Submit ELO"/>
            </td>
        </tr>

    </table>
</form>

