<%@ include file="common-taglibs.jsp" %>
<form action="uploadELOForFeedbackForm.html">
    <table>
        <tr>
            <td><strong>Mission</strong></td>
            <td>
                <c:choose>
                    <c:when test="${fn:length(missions) > 0}">
                        <select>
                            <c:forEach var="mission" items="${missions}">
                                <option value="${mission.id}">${mission.name}</option>
                            </c:forEach>
                        </select>
                    </c:when>
                </c:choose>


                <input type="text" name="mission" value="${currentLas}">
            </td>
        </tr>
        <tr>
            <td><strong>Product name</strong></td>
            <td>
                <input type="name" name="productName" value="${currentELO}">
            </td>
        </tr>
        <tr>
            <td><strong>Product status</strong></td>
            <td>

            </td>
        </tr>
        <tr>
            <td>
                <strong>
                    Authors
                </strong>
            </td>
            <td>
                ${currentUser.userDetails.firstname}&nbsp;${currentUser.userDetails.lastname}
            </td>
        </tr>
        <tr>
            <td>
                <strong>
                    Product type
                </strong>
            </td>
            <td>
                <input type="text" name="productType">
            </td>
        </tr>
        <tr>
            <td><strong>Product</strong></td>
            <td></td>
        </tr>
        <tr>
            <td><strong>Tool used</strong></td>
            <td>
                <input type="text" name="tool" value="${currentTool}">
            </td>
        </tr>
        <tr>
            <td>
                <strong>
                    Comment/Question
                </strong>
            </td>
            <td>

            </td>
        </tr>


    </table>
</form>