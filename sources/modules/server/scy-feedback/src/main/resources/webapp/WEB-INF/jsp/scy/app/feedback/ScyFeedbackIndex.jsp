<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>SCYFeedback</h1>

        <c:choose>
            <c:when test="${fn:length(eloRefs) > 0}">
                <c:forEach var="eloRef" items="${eloRefs}">
                    <div>
                        <table>
                            <tr>
                                <td colspan="2">
                                    <strong>${eloRef.name}</strong>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <strong>By</strong>
                                </td>
                                <td>
                                    ${eloRef.author.userDetails.firstname}&nbsp;${eloRef.author.userDetails.lastname}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <strong>Date</strong>
                                </td>
                                <td>

                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <strong>Shown</strong>
                                </td>
                            </tr>
                        </table>
                    </div>
                </c:forEach>

            </c:when>
        </c:choose>


    </tiles:putAttribute>
</tiles:insertDefinition>
    