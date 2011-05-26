<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
        <c:when test="${author}">

            <h1>Mission: ${missionSpecificationTransporter.elo.title}</h1>


        </c:when>
        <c:otherwise>

            <h1><spring:message code="MISSION_NAME" /> : ${missionSpecificationTransporter.elo.title}</h1>

            <fieldset>
                <table>
                    <tr>
                        <td>
                            <strong><spring:message code="SCAFFOLDING_LEVEL"/></strong>
                        </td>
                        <td>
                            <s:ajaxELOSlider sliderValues="${agentLevels}" defaultValue="${scaffoldingLevel}" eloURI="${missionSpecificationTransporter.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <br/>


        <div style="width: 100%; height: 600px">
            <div id="tabContainer" dojoType="dijit.layout.TabContainer" style="width: 100%; height: 100%;">
            </div>
        </div>

          <script type="text/javascript">
                dojo.addOnLoad(function() {
                    var tabs = dijit.byId("tabContainer");
                    var missionDesc = new dijit.layout.ContentPane({ title:"<spring:message code="MISSION_DESCRIPTION"/>", href:"${descriptionUrl}" });
                    tabs.addChild(missionDesc);
                    tabs.selectChild(missionDesc);

                    var pedagogicalPlan = new dijit.layout.ContentPane({ title:"<spring:message code="PEDAGOGICAL_PLAN"/>", href:"missionPlanner.html?eloURI=${missionSpecificationTransporter.uri}&action=initializeMissionPlanning" });
                    tabs.addChild(pedagogicalPlan);

                    var pane1 = new dijit.layout.ContentPane({ title:"<spring:message code="PORTFOLIO_CONFIGURATION"/>", href:"/webapp/app/scyauthor/ConfigureAssessment.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(pane1);

                    var pane2 = new dijit.layout.ContentPane({ title:"<spring:message code="STUDENTS"/>", href:"viewStudentsForPedagogicalPlan.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(pane2);

                    var pane3 = new dijit.layout.ContentPane({ title:"<spring:message code="PORTFOLIOS"/>", href:"/webapp/app/assessment/teacherAssessmentOverview.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(pane3);

                    var pane3 = new dijit.layout.ContentPane({ title:"<spring:message code="CURRENT_ACTIVITY"/>", href:"/webapp/app/assessment/assessmentindex.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(pane3);

                    var paneAnchorElos = new dijit.layout.ContentPane({ title:"<spring:message code="ANCHOR_ELOS"/>", href:"MissionHighLevelOverview.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(paneAnchorElos);


                    var refreshActive = false;
                    var currentActivity = new dojox.layout.ContentPane({ title:"Current activity", executeScripts: true,  id:"currentActivityTab", href:"/webapp/app/scyauthorruntime/currentActivityView.html?eloURI=${missionSpecificationTransporter.uri}" });
                    currentActivity.refreshOnShow = true;
                    tabs.addChild(currentActivity);
                    if(dijit.byId("currentActivityTab")){
                        if(!refreshActive) {
                            setInterval('dijit.byId("currentActivityTab").refresh()', 10000);
                            refreshActive = true;
                        }
                    }




                <c:choose><c:when test="${author}">
                    var paneAgent = new dijit.layout.ContentPane({ title:"Current activity", href:"viewAgentConfiguration.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(paneAgent);
                </c:when></c:choose>


                });
            </script>


            <br/>


                <a href="/webapp/app/scyauthorruntime/ScyAuthorRuntimeIndex.html">Graphical activity view</a> | <a href="/webapp/app/scyauthorruntime/ScyAuthorRuntimeIndex.html">Time line view</a>
            </fieldset>

        </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>