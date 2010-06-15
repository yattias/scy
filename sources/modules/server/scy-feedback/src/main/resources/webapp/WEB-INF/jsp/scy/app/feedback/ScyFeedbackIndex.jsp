<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>SCYFeedback</h1>

        <c:choose>
            <c:when test="${fn:length(eloRefTransporters) > 0}">
                <c:forEach var="transporter" items="${eloRefTransporters}">
                    <div>
                        <table>
                            <tr>
                                <td align="center">
                                    <c:choose>
                                         <c:when test="${fn:length(transporter.files) > 0}">
                                            <c:forEach var="refFile" items="${transporter.files}">
                                                file here!! ${refFile.fileData.name} ${refFile.fileData.contentType}
                                                <img src="/webapp/components/resourceservice.html?id=${refFile.id}"/>
                                            </c:forEach>
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <strong>${transporter.eloRef.name}</strong>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <strong>By</strong>
                                </td>
                                <td>
                                    ${transporter.eloRef.author.userDetails.firstname}&nbsp;${transporter.eloRef.author.userDetails.lastname}

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
    