<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Anchor ELO: ${model.name}</h1>        
        <table width="100%">
            <tr>
                <th>Anchor ELO portfolio properties</th>
                <th>Values</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Can be included in portfolio
                </td>
                <td>
                    <s:ajaxCheckBox model="${model}" property="includedInPortfolio"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
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