<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>ELO Gallery</h1>

        <c:choose>
            <c:when test="${fn:length(eloRefTransporters) > 0}">
                <c:forEach var="transporter" items="${eloRefTransporters}">
                    <s:modellink href="studentEloRefViewer.html" model="${transporter.eloRef}">
                        <div style="width:28%;float:left;height:220px;margin:7px 15px;border:1px solid #222222;">
                            <table style="border:0 !important;width:100%;">
                                <tr>
                                    <td  colspan="2" align="center" style="width:100%;">
                                        <c:choose>
                                             <c:when test="${fn:length(transporter.files) > 0}">
                                                <c:forEach var="refFile" items="${transporter.files}">
                                                    <img src="/webapp/components/resourceservice.html?id=${refFile.id}&showIcon=true" }/>
                                                </c:forEach>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <strong>ELO Title</strong>
                                    </td>
                                    <td>
                                        <strong>${transporter.eloRef.name}</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <strong>ELO Type</strong>
                                    </td>
                                    <td>
                                        ${transporter.eloRef.type}

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
                                        ${transporter.eloRef.formattedDate}
                                    </td>
                                </tr>
                                <!--tr>
                                    <td colspan="2" >
                                        <strong>Shown</strong>${transporter.eloRef.viewings} <strong>Evaluated by</strong>${transporter.totalAssessments} <strong>Score</strong> ${transporter.totalScore}
                                    </td>
                                </tr-->
                            </table>
                        </div>
                    </s:modellink>
                </c:forEach>

            </c:when>
        </c:choose>


    </tiles:putAttribute>
</tiles:insertDefinition>
    