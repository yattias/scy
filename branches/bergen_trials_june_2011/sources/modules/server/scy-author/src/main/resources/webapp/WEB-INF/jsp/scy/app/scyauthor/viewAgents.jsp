<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Specify agents/scaffolds</h1>

        <div>
            <table>
                <tr>
                    <th>Agent / agent group properties</th>
                    <th>Values</th>
                    <th></th>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Collaboration agent</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}" defaultValue="2"/></td>
                    <td><s:helpLink helpId="HELP_PAGE_1_COLLABORATION_AGENT_DESCRIPTION"/></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Votat agent</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}"/></td>
                    <td><s:helpLink helpId="HELP_PAGE_1_COLLABORATION_VOTAT_AGENT_DESCRIPTION"/></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Agent group 1</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}"/></td>
                    <td><s:helpLink helpId="HELP_PAGE_1_COLLABORATION_AGENT_GROUP_1_DESCRIPTION"/></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Agent group 2</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}" defaultValue="1"/></td>
                    <td><s:helpLink helpId="HELP_PAGE_1_COLLABORATION_AGENT_GROUP_2_DESCRIPTION"/></td>
                </tr>
            </table>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>