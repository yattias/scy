<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>ELO Gallery</h1>

        <c:choose>
            <c:when test="${fn:length(transporters) > 0}">
                <c:forEach var="transporter" items="${transporters}">
                    <div style="width:28%;float:left;height:220px;margin:7px 15px;border:1px solid #222222;">
                        <table style="border:0 !important;width:100%;">
                            <tr>
                                <td>
                                    <a href="studentEloRefViewer.html?uri=${transporter.uri}"><strong>ELO Title</strong></a>
                                </td>
                                <td>
                                    <strong>${transporter.elo.title}</strong>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <strong>ELO Type</strong>
                                </td>
                                <td>
                                    ${transporter.elo.technicalFormat}

                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <strong>Description</strong>
                                </td>
                                <td>
                                    ${transporter.elo.description}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <strong>Date</strong>
                                </td>
                                <td>

                                </td>
                            </tr>
                        </table>
                    </div>
                </c:forEach>

            </c:when>
        </c:choose>


    </tiles:putAttribute>
</tiles:insertDefinition>
    