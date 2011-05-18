<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
        <c:when test="${author}">

            <h1>Mission: ${missionSpecificationTransporter.elo.title}</h1>


        </c:when>
        <c:otherwise>

            <h1>Mission name: ${missionSpecificationTransporter.elo.title}</h1>


            <fieldset>
                <table>
                    <tr>
                        <td>
                            <strong>Scaffolding level</strong>
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
                    var missionDesc = new dijit.layout.ContentPane({ title:"Mission description", href:"${descriptionUrl}" });
                    tabs.addChild(missionDesc);
                    tabs.selectChild(missionDesc);

                    var pane1 = new dijit.layout.ContentPane({ title:"Portfolio configuration", href:"http://localhost:8080/webapp/app/scyauthor/ConfigureAssessment.html?eloURI=roolo%3A%2F%2Fscy.collide.info%2Fscy-collide-server%2F261.261%230" });
                    tabs.addChild(pane1);

                    var pane2 = new dijit.layout.ContentPane({ title:"Students", href:"viewStudentsForPedagogicalPlan.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(pane2);

                    var pane3 = new dijit.layout.ContentPane({ title:"Portfolios", href:"/webapp/app/assessment/assessmentindex.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(pane3);

                    var pane3 = new dijit.layout.ContentPane({ title:"Current activity", href:"/webapp/app/assessment/assessmentindex.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(pane3);

                    var paneAnchorElos = new dijit.layout.ContentPane({ title:"Anchor ELOs", href:"MissionHighLevelOverview.html?eloURI=${missionSpecificationTransporter.uri}" });
                    tabs.addChild(paneAnchorElos);

                    var refreshActive = false;
                    var currentActivity = new dojox.layout.ContentPane({ title:"Current activity", executeScripts: true,  id:"currentActivityTab", href:"/webapp/app/scyauthorruntime/currentActivityView.html?eloURI=${missionSpecificationTransporter.uri}" });
                    currentActivity.refreshOnShow = true;
                    tabs.addChild(currentActivity);
                    if(dijit.byId("currentActivityTab")){
                        if(!refreshActive) {
                            setInterval('dijit.byId("currentActivityTab").refresh()', 5000);
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