<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
        <c:when test="${author}">

            <h1>Mission: ${missionSpecificationTransporter.elo.title}</h1>


        </c:when>
        <c:otherwise>

        <div style="width: 100%; height: 800px">
            <div id="teacherTabContainer" dojoType="dijit.layout.TabContainer"  style="width: 100%; height: 100%;">
            </div>
        </div>

        <script type="text/javascript">
            dojo.addOnLoad(function() {
                var teacherTabs = dijit.byId("teacherTabContainer");
                var fineTuneTabContainer = new dijit.layout.TabContainer({nested:true, id:"fineTuneTabContainer", title:"<spring:message code="FINETUNE_PEDAGOGICAL_PLAN"/>"});
                 teacherTabs.addChild(fineTuneTabContainer);
                var missionDesc = new dijit.layout.ContentPane({ title:"<spring:message code="MISSION_DESCRIPTION"/>", href:"/webapp/app/scyauthor/tabs/MissionDescription.html?eloURI=${missionSpecificationTransporter.uri}" });

                fineTuneTabContainer.addChild(missionDesc);
                fineTuneTabContainer.selectChild(missionDesc);

                var pedagogicalPlan = new dijit.layout.ContentPane({ title:"<spring:message code="PEDAGOGICAL_PLAN"/>", href:"missionPlanner.html?eloURI=${missionSpecificationTransporter.uri}&action=initializeMissionPlanning" });
                fineTuneTabContainer.addChild(pedagogicalPlan);

                var pane1 = new dijit.layout.ContentPane({ id:"learningGoalsConfiguration", title:"<spring:message code="LEARNING_GOALS"/>", href:"/webapp/app/scyauthor/LearningGoals.html?eloURI=${missionSpecificationTransporter.uri}" });
                fineTuneTabContainer.addChild(pane1);

                var portfolioTab = new dijit.layout.ContentPane({ id:"portfolioConfiguration", title:"<spring:message code="PORTFOLIO_CONFIGURATION"/>", href:"/webapp/app/scyauthor/ConfigureAssessment.html?eloURI=${missionSpecificationTransporter.uri}" });
                fineTuneTabContainer.addChild(portfolioTab);

                var scaffoldingLevelTab = new dijit.layout.ContentPane({ id:"scaffoldingLevelConfiguration", title:"<spring:message code="SCAFFOLDING_LEVEL"/>", href:"/webapp/app/scyauthor/ScaffoldingLevel.html?eloURI=${missionSpecificationTransporter.uri}" });
                fineTuneTabContainer.addChild(scaffoldingLevelTab);

                var pane2 = new dijit.layout.ContentPane({ id:"viewStudents", title:"<spring:message code="STUDENTS"/>", href:"viewStudentsForPedagogicalPlan.html?eloURI=${missionSpecificationTransporter.uri}" });
                fineTuneTabContainer.addChild(pane2);

                //var paneAnchorElos = new dijit.layout.ContentPane({ title:"<spring:message code="ANCHOR_ELOS"/>", href:"MissionHighLevelOverview.html?eloURI=${missionSpecificationTransporter.uri}" });
                //fineTuneTabContainer.addChild(paneAnchorElos);

                 var runTimeTabContainer = new dijit.layout.TabContainer({nested:true, id:"runTimeTabContainer", title:"<spring:message code="RUNTIME"/>"});
                 teacherTabs.addChild(runTimeTabContainer);

                var refreshActive = false;
                var currentActivity = new dojox.layout.ContentPane({ title:"<spring:message code="CURRENT_ACTIVITY"/>", executeScripts: true,  id:"currentActivityTab", href:"/webapp/app/scyauthorruntime/currentActivityView.html?eloURI=${missionSpecificationTransporter.uri}" });
                currentActivity.refreshOnShow = true;
                runTimeTabContainer.addChild(currentActivity);
                if(dijit.byId("currentActivityTab")){
                    if(!refreshActive) {
                        setInterval('dijit.byId("currentActivityTab").refresh()', 25000);
                        refreshActive = true;
                    }
                }

                var usersInLasRefreshActive = false;
                var usersInLasActivity = new dojox.layout.ContentPane({title: "<spring:message code="CURRENT_ACTIVITY_IN_LAS"/>", executeScripts: true, id: "usersInLasTab", href:"/webapp/app/scyauthorruntime/viewUsersInLas.html?eloURI=${missionSpecificationTransporter.uri}"});
                usersInLasActivity.refreshOnShow = true;
                runTimeTabContainer.addChild(usersInLasActivity);
                if(dijit.byId("usersInLasTab")) {
                    if(!usersInLasRefreshActive) {
                        setInterval('dijit.byId("usersInLasTab").refresh()', 25000);
                        usersInLasRefreshActive = true;
                    }
                }

                 var assessmentTabContainer = new dijit.layout.TabContainer({nested:true, id:"assessmentTabContainer", title:"<spring:message code="SCY_ASSESSMENT"/>"});
                 teacherTabs.addChild(assessmentTabContainer);

                var assessmentTab = new dojox.layout.ContentPane({ title:"<spring:message code="SCY_ASSESSMENT"/>", executeScripts: true,  href:"/webapp/app/assessment/assessmentindex.html?eloURI=${missionSpecificationTransporter.uri}" });
                assessmentTabContainer.addChild(assessmentTab);
            })
        </script>






                <c:choose><c:when test="${author}">
                    var paneAgent = new dijit.layout.ContentPane({ title:"Current activity", href:"viewAgentConfiguration.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(paneAgent);
                </c:when></c:choose>




            <br/>
            </fieldset>

        </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>