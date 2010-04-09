<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Pedagogical Plan: ${pedagogicalPlan.name}</h1>

        <table width="100%">
            <tr>
                <th width="35%">Pedagogical plan properties</th>
                <th width="65%">Values</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Name
                </td>
                <td>
                    <s:ajaxTextField model="${pedagogicalPlan}" property="name"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Description
                </td>
                <td>
                    <s:ajaxTextField model="${pedagogicalPlan}" property="description" isMultiLine="true"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Published
                </td>
                <td>
                    <s:ajaxCheckBox model="${pedagogicalPlan}" property="published"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Make all students buddies <s:helpLink helpId="HELP_PAGE_1_AUTO_MAKE_BUDDIES"/>
                </td>
                <td>
                    <s:ajaxCheckBox model="${pedagogicalPlan}" property="makeAllAssignedStudentsBuddies"/>
                </td>
            </tr>
        </table>
        <br/>
        <br/>

        <table width="100%">
            <tr>
                <th width="35%">Main components pedagogical plan</th>
                <th width="65%">Component</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Scenario
                </td>
                <td>
                    <a href="viewScenario.html?scenarioId=${pedagogicalPlan.scenario.id}&pedagogicalPlanId=${pedagogicalPlan.id}">${pedagogicalPlan.scenario.name}</a>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Mission
                </td>
                <td>
                    <a href="viewMission.html?missionId=${pedagogicalPlan.mission.id}">${pedagogicalPlan.mission.name}</a>
                </td>                
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Portfolio
                </td>
                <td>
                    <a href="viewPortfolio.html?id=${pedagogicalPlan.id}">Edit</a>
                </td>
            </tr>            
        </table>
        <br/>
        <br/>

        <table cellpadding="10" cellspacing="10">
            <tr>
                <th>Overall scaffold level</th>
                <th>Values</th>
                <th>Zoom in</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td width="30%">SCYLab use</td>
                <td width="60%"><s:ajaxSlider sliderValues="${agentLevels}"/></td>
                <td width="10%"><a href="viewAgents.html?pedagogicalPlanId=${pedagogicalPlan.id}">Specify</a></td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>Mission content</td>
                <td><s:ajaxSlider sliderValues="${contentLevels}"/></td>
                <td width="10%"><a href="viewAgents.html?pedagogicalPlanId=${pedagogicalPlan.id}">Specify</a></td>
            </tr>
        </table>
        <br/>
        <br/>        

        <h2>Assigned students</h2>

        <s:dialog url="selectStudentsForPedagogicalPlan.html" title="Select" dialogHeader="Select students" extraParameters="id=${pedagogicalPlan.id}"/>

        <c:choose>
            <c:when test="${fn:length(assignedPedagogicalPlans) > 0}">
                <table id="teachersTable" width="100%">
                    <tr>
                        <th></th>
                        <th></th>
                        <th>User name</th>
                        <th>First name</th>
                        <th>Last name</th>
                    </tr>
                    <c:forEach var="assignedPedagogicalPlan" items="${assignedPedagogicalPlans}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <s:deleteLink href="viewPedagogicalPlan.html?id=${pedagogicalPlan.id}&action=removeStudent&username=${assignedPedagogicalPlan.user.userDetails.username}" title="-" confirmText="Do you really want to remove ${assignedPedagogicalPlan.user.userDetails.username} from ${pedagogicalPlan.name}?" />
                            </td>
                            <td><img src="/webapp/common/filestreamer.html?username=${assignedPedagogicalPlan.user.userDetails.username}&showIcon"/>
                            </td>
                            <td>
                                ${assignedPedagogicalPlan.user.userDetails.username}
                            </td>
                            <td>${assignedPedagogicalPlan.user.userDetails.firstname} </td>
                            <td>${assignedPedagogicalPlan.user.userDetails.lastname}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>


    </tiles:putAttribute>
</tiles:insertDefinition>