<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Configure assessment for ${pedagogicalPlan.name}</h1>

        <table>
            <tr>
                <td>
                    Name
                </td>
                <td>
                    <s:ajaxTransferObjectTextField transferObjectServiceCollection="${transferObjectServiceCollection}" transferObject="${pedagogicalPlan}" transferEloURI="${pedagogicalPlan.pedagogicalPlanURI}" id="${pedagogicalPlan.id}" property="name"/>
                </td>
            </tr>
        </table>


    </tiles:putAttribute>
</tiles:insertDefinition>