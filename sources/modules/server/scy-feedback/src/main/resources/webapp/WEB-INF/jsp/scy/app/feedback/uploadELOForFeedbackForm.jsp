<%@ include file="common-taglibs.jsp" %>
<!--form enctype="multipart/form-data" accept-charset="UTF-8" action="/webapp/app/feedback/uploadELOForFeedbackForm.html" onsubmit="postForm(this, this.parentNode);return false;"-->
<form method="POST" enctype="multipart/form-data" accept-charset="UTF-8" action="/webapp/app/feedback/uploadELOForFeedbackForm.html">
    <table>
        <!--tr width="100%">
            <td align="left" width="20%"><strong>Mission</strong></td>
            <td align="left">
                <c:choose>
                    <c:when test="${fn:length(missions) > 0}">
                        <select name="mission">
                            <c:forEach var="mission" items="${missions}">
                                <option value="${mission.id}">${mission.name}</option>
                            </c:forEach>
                        </select>
                    </c:when>
                </c:choose>
            </td>
        </tr-->
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
            <td align="left">
                <input type="name" name="productName">
            </td>
        </tr>
        <!--tr>
            <td align="left"><strong>Product statusssss</strong></td>
            <td align="left">
                <c:choose>
                    <c:when test="${fn:length(statuses) > 0}">
                        <select name="status">
                            <c:forEach var="status" items="${statuses}">
                                <option value="${status}">${status}</option>
                            </c:forEach>
                        </select>
                    </c:when>
                </c:choose>

            </td>
        </tr-->
        <tr>
            <td align="left">
                <strong>
                    Authors
                </strong>
            </td>
            <td align="left">
                ${currentUser.userDetails.firstname}&nbsp;${currentUser.userDetails.lastname}
            </td>
        </tr>
        <tr>
            <td align="left">
                <strong>
                    ELO Type
                </strong>
            </td>
            <td align="left">
                <input type="text" name="productType">
            </td>
        </tr>
        <!--tr>
            <td align="left"><strong>Product</strong></td>
            <td align="left"></td>
        </tr-->
        <!--tr>
            <td align="left"><strong>Tool used</strong></td>
            <td align="left">
                <input type="text" name="tool" value="{currentTool}">
            </td>
        </tr-->
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

