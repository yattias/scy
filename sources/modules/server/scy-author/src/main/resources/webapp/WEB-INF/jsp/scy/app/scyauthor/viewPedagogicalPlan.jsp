<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Pedagogical Plan: ${pedagogicalPlan.name}</h1>

        <table width="100%" border="2">
            <tr>
                <td>
                    Name
                </td>
                <td colspan="2">
                    <s:ajaxTextField model="${pedagogicalPlan}" property="name"/>
                </td>
            </tr>
            <tr>
                <td>
                    Scenario
                </td>
                <td>
                    <a href="viewScenario.html?scenarioId=${pedagogicalPlan.scenario.id}&pedagogicalPlanId=${pedagogicalPlan.id}">${pedagogicalPlan.scenario.name}</a>
                </td>
                <td>
                        ${pedagogicalPlan.scenario.description}
                </td>
            </tr>
            <tr>
                <td>
                    Mission
                </td>
                <td>
                    <a href="viewMission.html?missionId=${pedagogicalPlan.mission.id}">${pedagogicalPlan.mission.name}</a>
                </td>
                <td>
                        ${pedagogicalPlan.mission.description}
                </td>
            </tr>
            <tr>
                <td>
                    Portfolio settings
                </td>
                <td colspan="2">
                    <a href="viewPortfolio.html?id=${pedagogicalPlan.id}">Edit</a>
                </td>
            </tr>
            <tr>
                <td>
                    Published
                </td>
                <td colspan="2">
                    <s:ajaxCheckBox model="${pedagogicalPlan}" property="published"/>
                </td>
            </tr>
        </table>
        <br/>

        <h2>Scaffolding</h2>

        <table cellpadding="10" cellspacing="10">
            <tr>
                <td width="30%">Overall level SCYLab</td>
                <td width="60%"><s:ajaxSlider sliderValues="${agentLevels}"/></td>
                <td width="10%"><a href="viewAgents.html?pedagogicalPlanId=${pedagogicalPlan.id}">Specify</a></td>
            </tr>
            <tr>
                <td>Overall level Mission content</td>
                <td><s:ajaxSlider sliderValues="${contentLevels}"/></td>
                <td width="10%"><a href="viewAgents.html?pedagogicalPlanId=${pedagogicalPlan.id}">Specify</a></td>
            </tr>
        </table>
        <br/>
        <br/>        

        <table width="100%" border="2">
            <tr>
                <td>Assigned students</td>
                <td><s:dialog url="selectStudentsForPedagogicalPlan.html" title="Select" dialogHeader="Select student"/></td>
            </tr>
        </table>
    </tiles:putAttribute>
</tiles:insertDefinition>