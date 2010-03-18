<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Anchor ELO: ${model.name}</h1>

        <h2>Portfolio</h2>
        <table width="200%" border="2">
            <tr>
                <td>
                    Can be included in portfolio
                </td>
                <td>
                    <input type="checkbox">
                </td>
            </tr>
            <tr>
                <td>
                    Obligatory in portfolio
                </td>
                <td>
                    <input type="checkbox">
                </td>
            </tr>

        </table>


        

    </tiles:putAttribute>
</tiles:insertDefinition>