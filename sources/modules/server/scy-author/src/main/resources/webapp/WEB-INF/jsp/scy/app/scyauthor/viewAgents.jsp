<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Specify agents/scaffolds</h1>


        <div>
            <table>
                <tr>
                    <th>Agent / agent group properties</th>
                    <th>Values</th>
                    <th>Description</th>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Collaboration agent</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}" defaultValue="2"/></td>
                    <td></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Votat agent</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}"/></td>
                    <td></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Agent group 1</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}"/></td>
                    <td>Agent group 1 consists of agents related to ...</td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Agent group 2</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}" defaultValue="1"/></td>
                    <td>Agent group 2 consists of agents related to and so on ...</td>
                </tr>
            </table>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>