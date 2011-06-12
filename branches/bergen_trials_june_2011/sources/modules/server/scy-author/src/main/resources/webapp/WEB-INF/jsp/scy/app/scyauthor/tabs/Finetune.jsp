<%@ include file="common-taglibs.jsp" %>

    <div style="width: 100%; height: 800px">
        <div id="teacherTabsContainer" dojoType="dijit.layout.TabContainer" nested="true"  style="width: 100%; height: 100%;">
        </div>
    </div>

      <script type="text/javascript">
            dojo.addOnLoad(function() {
                var tabsTabContainer = dijit.byId("teacherTabsContainer");
                var missionDesc = new dijit.layout.ContentPane({ title:"<spring:message code="MISSION_DESCRIPTION"/>", href:"/webapp/app/scyauthor/tabs/MissionDescription.html?eloURI=${missionSpecificationTransporter.uri}" });

                tabsTabContainer.addChild(missionDesc);
                tabsTabContainer.selectChild(missionDesc);

                var pedagogicalPlan = new dijit.layout.ContentPane({ title:"<spring:message code="PEDAGOGICAL_PLAN"/>", href:"missionPlanner.html?eloURI=${missionSpecificationTransporter.uri}&action=initializeMissionPlanning" });
                tabsTabContainer.addChild(pedagogicalPlan);

                var pane1 = new dijit.layout.ContentPane({ id:"portfolioConfiguration", title:"<spring:message code="PORTFOLIO_CONFIGURATION"/>", href:"/webapp/app/scyauthor/ConfigureAssessment.html?eloURI=${missionSpecificationTransporter.uri}" });
                tabsTabContainer.addChild(pane1);

                var pane2 = new dojox.layout.ContentPane({ id:"manageStudentsForPedagogicalPlan", title:"<spring:message code="STUDENTS"/>", href:"viewStudentsForPedagogicalPlan.html?eloURI=${missionSpecificationTransporter.uri}" });
                tabsTabContainer.addChild(pane2);

                var pane4 = new dojox.layout.ContentPane({ title:"Assessment", executeScripts: true,  href:"/webapp/app/assessment/assessmentindex.html?eloURI=${missionSpecificationTransporter.uri}" });
                tabsTabContainer.addChild(pane4);

                var paneAnchorElos = new dijit.layout.ContentPane({ title:"<spring:message code="ANCHOR_ELOS"/>", href:"MissionHighLevelOverview.html?eloURI=${missionSpecificationTransporter.uri}" });
                tabsTabContainer.addChild(paneAnchorElos);

                var refreshActive = false;
                var currentActivity = new dojox.layout.ContentPane({ title:"<spring:message code="CURRENT_ACTIVITY"/>", executeScripts: true,  id:"currentActivityTab", href:"/webapp/app/scyauthorruntime/currentActivityView.html?eloURI=${missionSpecificationTransporter.uri}" });
                currentActivity.refreshOnShow = true;
                tabsTabContainer.addChild(currentActivity);
                if(dijit.byId("currentActivityTab")){
                    if(!refreshActive) {
                        setInterval('dijit.byId("currentActivityTab").refresh()', 15000);
                        refreshActive = true;
                    }
                }
            });
        </script>
                


