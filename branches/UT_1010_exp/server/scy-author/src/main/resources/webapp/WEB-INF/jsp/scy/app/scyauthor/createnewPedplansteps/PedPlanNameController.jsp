<%@ include file="../common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <div>
            <table>
                <tr>
                    <td colspan="2">
                        <h2>Step 1: Pedagogical plan</h2>
                    </td>
                </tr>
                <tr>
                    <td>
                        Enter pedagogical plan name
                    </td>
                    <td>
                        <s:ajaxTextField property="name" model="${model}"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="right">
                        <a href="selectScenario.html?pedPlanId=${model.id}">Step 2>></a>
                    </td>
                </tr>
            </table>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>