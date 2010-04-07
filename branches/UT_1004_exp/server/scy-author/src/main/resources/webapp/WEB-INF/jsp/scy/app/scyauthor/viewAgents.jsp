<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Specify agents/scaffolds</h1>


        <div>
            <table>
                <tr>
                    <td>Overall level SCYLab</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}"/></td>
                </tr>
                <tr>
                    <td>Overall level Mission content</td>
                    <td><s:ajaxSlider sliderValues="${contentLevels}"/></td>
                </tr>
            </table>
        </div>

        <!--table>
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
        </table-->


    </tiles:putAttribute>
</tiles:insertDefinition>