<%@ include file="../common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <div>
            <table>
                <tr>
                    <td colspan="2">
                        <h2>Step 2: Select Scenario for  ${model.name}</h2>
                    </td>
                </tr>

                <c:choose>
                    <c:when test="${fn:length(scenarios) > 0}">
                       <c:forEach var="scenario" items="${scenarios}">
                           <tr class="${oddEven.oddEven}">
                               <td>
                                   <a href="selectScenario.html?pedPlanId=${model.id}&action=addScenario&scenario=${scenario.id}">${scenario.name}
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