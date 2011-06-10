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
                // var tabsTabContainer = dijit.byId("innerTabContainer");
                //tabsTabContainer.addChild(tabs);
                //if(!tabsTabContainer) alert("NOT TABS");
                var missionDesc = new dijit.layout.ContentPane({ title:"<spring:message code="MISSION_DESCRIPTION"/>", href:"/webapp/app/scyauthor/tabs/MissionDescription.html?eloURI=${missionSpecificationTransporter.uri}" });

                fineTuneTabContainer.addChild(missionDesc);
                fineTuneTabContainer.selectChild(missionDesc);

                var pedagogicalPlan = new dijit.layout.ContentPane({ title:"<spring:message code="PEDAGOGICAL_PLAN"/>", href:"missionPlanner.html?eloURI=${missionSpecificationTransporter.uri}&action=initializeMissionPlanning" });
                fineTuneTabContainer.addChild(pedagogicalPlan);

                var pane1 = new dijit.layout.ContentPane({ id:"portfolioConfiguration", title:"<spring:message code="PORTFOLIO_CONFIGURATION"/>", href:"/webapp/app/scyauthor/ConfigureAssessment.html?eloURI=${missionSpecificationTransporter.uri}" });
                fineTuneTabContainer.addChild(pane1);

                var pane2 = new dijit.layout.ContentPane({ title:"<spring:message code="STUDENTS"/>", href:"viewStudentsForPedagogicalPlan.html?eloURI=${missionSpecificationTransporter.uri}" });
                fineTuneTabContainer.addChild(pane2);

                //var pane3 = new dijit.layout.ContentPane({ title:"<spring:message code="PORTFOLIOS"/>", href:"/webapp/app/assessment/teacherAssessmentOverview.html?eloURI=${missionSpecificationTransporter.uri}" });
                //tabs.addChild(pane3);

                var pane4 = new dojox.layout.ContentPane({ title:"Assessment", executeScripts: true,  href:"/webapp/app/assessment/assessmentindex.html?eloURI=${missionSpecificationTransporter.uri}" });
               fineTuneTabContainer.addChild(pane4);

                var paneAnchorElos = new dijit.layout.ContentPane({ title:"<spring:message code="ANCHOR_ELOS"/>", href:"MissionHighLevelOverview.html?eloURI=${missionSpecificationTransporter.uri}" });
                fineTuneTabContainer.addChild(paneAnchorElos);

                //var technical = new dijit.layout.ContentPane({ title:"<spring:message code="TECHNICAL_INFO"/>", href:"viewTechnicalConfigurationForPedagogicalPlan.html?eloURI=${missionSpecificationTransporter.uri}" });
                //tabs.addChild(technical);




                 var runTimeTabContainer = new dijit.layout.TabContainer({nested:true, id:"runTimeTabContainer", title:"<spring:message code="RUNTIME"/>"});
                 teacherTabs.addChild(runTimeTabContainer);

                var refreshActive = false;
                var currentActivity = new dojox.layout.ContentPane({ title:"<spring:message code="CURRENT_ACTIVITY"/>", executeScripts: true,  id:"currentActivityTab", href:"/webapp/app/scyauthorruntime/currentActivityView.html?eloURI=${missionSpecificationTransporter.uri}" });
                currentActivity.refreshOnShow = true;
                runTimeTabContainer.addChild(currentActivity);
                if(dijit.byId("currentActivityTab")){
                    if(!refreshActive) {
                        setInterval('dijit.byId("currentActivityTab").refresh()', 15000);
                        refreshActive = true;
                    }
                }

                 var assessmentTabContainer = new dijit.layout.TabContainer({nested:true, id:"assessmentTabContainer", title:"<spring:message code="SCY_ASSESSMENT"/>"});
                 teacherTabs.addChild(assessmentTabContainer);


                //var teacherTabsContainer = new dijit.layout.TabContainer({id:"teacherTabsContainer", width:"100%", height:"100%", nested:true, title:"<spring:message code="FINETUNE_PEDAGOGICAL_PLAN"/>"});

              // var fineTuneTab = new dojox.layout.ContentPane({width:"100%", height:"100%", href:"tabs/Finetune.html?eloURI=${missionSpecificationTransporter.uri}" });
                //teacherTabsContainer.addChild(fineTuneTab);
                //var runtimeTab = new dijit.layout.ContentPane({ title:"<spring:message code="RUNTIME"/>", href:"${descriptionUrl}" });
                var assessmentTab = new dojox.layout.ContentPane({ title:"<spring:message code="SCY_ASSESSMENT"/>", executeScripts: true,  href:"/webapp/app/assessment/assessmentindex.html?eloURI=${missionSpecificationTransporter.uri}" });
                assessmentTabContainer.addChild(assessmentTab);
                //teacherTabsContainer.addChild(fineTuneTab);
                //teacherTabs.addChild(teacherTabsContainer);
                /*if(!teacherTabs){
                    alert("jedeg!");
                } *
               // teacherTabs.addChild(fineTuneTab);
                /*teacherTabs.addChild(runtimeTab);
                teacherTabs.addChild(assessmentTab);*/
               //teacherTabs.selectChild(fineTuneTab);

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