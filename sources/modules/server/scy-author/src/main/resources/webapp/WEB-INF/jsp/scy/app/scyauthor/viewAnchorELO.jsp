<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Anchor ELO: ${model.name}</h1>

        <h2>Portfolio</h2>
        <table width="100%">
            <tr class="tablerow-odd">
                <td>
                    Can be included in portfolio
                </td>
                <td>
                    <s:ajaxCheckBox model="${model}" property="includedInPortfolio"/>
                </td>
            </tr>
            <tr class="tablerow-even">
                <td>
                    Obligatory in portfolio
                </td>
                <td>
                    <s:ajaxCheckBox model="${model}" property="obligatoryInPortfolio"/>
                </td>
            </tr>

        </table>


        

    </tiles:putAttribute>
</tiles:insertDefinition>