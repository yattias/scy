<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Scenario: ${model.name}</h1>
        <table>
            <tr>
                <th width="35%">Scenario properties</th>
                <th width="65%">Values</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Name
                </td>
                <td>
                     <s:ajaxTextField property="name" model="${model}"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Description
                </td>
                <td>
                     <s:ajaxTextField property="description" model="${model}" isMultiLine="true"/>
                </td>
            </tr>
        </table>

       

        <s:scenariodiagram scenario="${model}" pedagogicalPlan="${pedagogicalPlan}" lasLink="${myLasLink}" includeRuntimeInfo="${false}"/>
        

    </tiles:putAttribute>
</tiles:insertDefinition>