<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Specify agents/scaffolds</h1>

        <table>
            <tr>
                <td rowspan="3">Overall level SCYLab</td>
                <td>Low</td>
                <td><input type="radio"/></td>
            </tr>
            <tr>
                <td>Medium</td>
                <td><input type="radio"/></td>
            </tr>
            <tr>
                <td>High</td>
                <td><input type="radio"/></td>
            </tr>
            <tr>
                <td rowspan="3">Overall level Mission content</td>
                <td>Low</td>
                <td><input type="radio"/></td>
            </tr>
            <tr>
                <td>Medium</td>
                <td><input type="radio"/></td>
            </tr>
            <tr>
                <td>High</td>
                <td><input type="radio"/></td>
            </tr>
        </table>


    </tiles:putAttribute>
</tiles:insertDefinition>