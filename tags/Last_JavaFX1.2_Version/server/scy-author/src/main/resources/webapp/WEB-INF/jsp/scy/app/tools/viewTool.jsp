<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>${model.name}</h1>

        <table width="100%">
            <tr>
                <td width="15%">Name</td>
                <td>
                    <s:ajaxTextField model="${model}" property="name"/>
                </td>
            </tr>
            <tr>
                <td>
                    Tool ID
                </td>
                <td>
                    <s:ajaxTextField model="${model}" property="toolId"/>
                </td>
            </tr>
            <tr>
                <td>
                    Tool popularity
                </td>
                <td>
                    ${toolUsage}
                </td>
            </tr>
        </table>

    </tiles:putAttribute>
</tiles:insertDefinition>