<%@ include file="../common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <div>
            <table>
                <tr>
                    <td colspan="2">
                        <h2>Step 2: Select which mission to base the new mission on
                    </td>
                </tr>

                <c:choose>
                    <c:when test="${fn:length(transporters) > 0}">
                       <c:forEach var="transporter" items="${transporters}">
                           <tr class="${oddEven.oddEven}">
                               <td>
                                   <a href="selectScenario.html?action=add&uri=${transporter.uri}">${transporter.elo.title}</a>
                               </td>
                           </tr>
                       </c:forEach>
                    </c:when>
                </c:choose>



                <tr>
                    <td>

                    </td>
                    <td>

                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="right">
                        Step 3>>
                    </td>
                </tr>
            </table>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>