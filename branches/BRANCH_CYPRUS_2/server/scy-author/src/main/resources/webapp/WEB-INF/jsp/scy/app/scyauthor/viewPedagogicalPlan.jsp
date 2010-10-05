<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
        <c:when test="${author}">


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
                <tr class="${oddEven.oddEven}">
                    <td colspan="2" align="right">
                        <a href="MissionHighLevelOverview.html?pedagogicalPlanId=${pedagogicalPlan.id}">More details...</a>

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

            <table width="100%" cellpadding="10" cellspacing="10">
                <tr>
                    <th>Overall scaffold level</th>
                    <th>Values</th>
                    <th>Zoom in</th>
                </tr>

                <tr class="${oddEven.oddEven}">
                    <td width="30%">SCYLab use</td>
                    <td width="60%"><s:ajaxSlider sliderValues="${agentLevels}" model="${pedagogicalPlan}" property="overallSCYLabScaffoldingLevel"/></td>
                    <td width="10%"><a href="/webapp/app/agents/agentBank.html?pedagogicalPlanId=${pedagogicalPlan.id}">Specify</a></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Mission content</td>
                    <td><s:ajaxSlider sliderValues="${contentLevels}" model="${pedagogicalPlan}" property="overallMissionContentScaffoldingLevel"/></td>
                    <td width="10%"><a href="/webapp/app/agents/agentBank.html?pedagogicalPlanId=${pedagogicalPlan.id}">Specify</a></td>
                </tr>
            </table>

            <br/>
            <br/>
            <table width="100%">
                <tr>
                    <th colspan="2">Assessment</th>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>
                        <strong>Limit number of ELOs to be peer assessed</strong>
                    </td>
                    <td>
                        <s:ajaxCheckBox model="${pedagogicalPlan}" property="limitNumberOfELOsAvailableForPeerAssessment"/>
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${fn:length(anchorElos) > 0}">
                        <c:forEach var="anchorElo" items="${anchorElos}">
                            <tr class="${oddEven.oddEven}">
                                <td colspan="2">
                                    <a href="viewAnchorELO.html?anchorELOId=${anchorElo.id}">${anchorElo.humanReadableName}</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                </c:choose>

                <tr class="${oddEven.oddEven}">
                    <td colspan="2">
                        <s:dialog url="selectAnchorELOsToBePeerAssessed.html" title="Select Anchor ELO" extraParameters="id=${pedagogicalPlan.id}"/>
                    </td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>
                        Assessment score icon
                    </td>
                    <td>
                        <c:if test="${pedagogicalPlan.assessmentScoreIcon != null}">
                             <img src="/webapp/components/resourceservice.html?id=${pedagogicalPlan.assessmentScoreIcon.id}"/>
                        </c:if>
                        <s:uploadFile listener="addAssessmentScoreIconListener" model="${pedagogicalPlan.class.name}_${pedagogicalPlan.id}"/>
                    </td>

                </tr>
            </table>

            <br/>
            <br/>
            <table width="100%">
                <tr>
                    <th width="50%">Pedagogical plan users and groups</th>
                    <th width="50%">Object</th>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Students</td>
                    <td><s:modellink model="${pedagogicalPlan}" href="viewStudentsForPedagogicalPlan.html">${assignedPedagogicalPlansCount} assigned students</s:modellink></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Grouping</td>
                    <td><s:modellink model="${pedagogicalPlan}" href="viewGroupsForPedagogicalPlan.html">${pedagogicalPlanGroupsCount} groups</s:modellink></td>
                </tr>
            </table>
        </c:when>
        <c:otherwise>
            <table width="100%">
                <tr class="${oddEven.oddEven}">
                    <td>
                        Mission Name
                    </td>
                    <td>
                        <s:ajaxTextField model="${pedagogicalPlan}" property="name"/>
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
                    <td>Assigned Students</td>
                    <td><s:modellink model="${pedagogicalPlan}" href="viewStudentsForPedagogicalPlan.html">${assignedPedagogicalPlansCount} assigned students</s:modellink></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>Scaffolding level</td>
                    <td><s:ajaxSlider sliderValues="${agentLevels}" model="${pedagogicalPlan}" property="overallSCYLabScaffoldingLevel"/></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td colspan="2" align="right">
                        <a href="MissionHighLevelOverview.html?pedagogicalPlanId=${pedagogicalPlan.id}">More details...</a>

                    </td>
                </tr>


                
            </table>
        </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>