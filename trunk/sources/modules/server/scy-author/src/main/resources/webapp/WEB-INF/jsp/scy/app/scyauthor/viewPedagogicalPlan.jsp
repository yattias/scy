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
                <h2>Mission setup</h2>
                <table>
                    <tr class="${oddEven.oddEven}">
                        <td width="20%">
                            <strong>Mission name</strong>
                        </td>
                        <td>
                            <s:ajaxELOTextField property="title" eloURI="${missionSpecificationTransporter.uri}" rooloServices="${rooloServices}"/>
                        </td>
                    </tr>
                    <tr class="${oddEven.oddEven}">
                        <td>
                            <strong>Mission description</strong>
                        </td>
                        <td>
                            <a href="${descriptionUrl}" target="_blank">${missionSpecificationTransporter.elo.title}</a> 
                        </td>
                    </tr>
                    <tr class="${oddEven.oddEven}">
                        <td>
                            <strong>${numberOfStudentsAssigned} assigned students</strong>
                        </td>
                        <td>
                            <a href="viewStudentsForPedagogicalPlan.html?eloURI=${missionSpecificationTransporter.uri}" >Click to assign students to mission</a>
                        </td>
                    </tr>
                    <tr class="${oddEven.oddEven}">
                        <td>
                            <strong>Scaffolding level</strong>
                        </td>
                        <td>
                            <s:ajaxELOSlider sliderValues="${agentLevels}" defaultValue="${scaffoldingLevel}" eloURI="${missionSpecificationTransporter.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/>
                        </td>
                    </tr>
                    <tr class="${oddEven.oddEven}">
                        <td colspan="2">
                            <a href="MissionHighLevelOverview.html?eloURI=${missionSpecificationTransporter.uri}">Anchor ELOs</a>

                        </td>
                    </tr>
                    <c:choose><c:when test="${author}">
                    <tr class="${oddEven.oddEven}">
                        <td colspan="2">
                            <a href="viewAgentConfiguration.html?eloURI=${missionSpecificationTransporter.uri}">Agent configuration</a>

                        </td>
                    </tr>
                    </c:when></c:choose>
                    <tr class="${oddEven.oddEven}">
                        <td colspan="2">
                            <a href="ConfigureAssessment.html?eloURI=${missionSpecificationTransporter.uri}">Configure assessment</a>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <br/>


            <fieldset>
                <h2>Portfolios</h2>
                <table>
                    <tr  class="${oddEven.oddEven}">
                        <td width="20%">
                            <strong>Portfolios</strong>
                        </td>
                        <td>
                            <strong><a href="/webapp/app/assessment/assessmentindex.html?eloURI=${missionSpecificationTransporter.uri}">${numberOfPortfoliosReadyForAssessment}  portfolios are ready to be assessed</a></strong>
                        </td>
                    </tr>
                </table>
            </fieldset>

            <br/>
            
            <fieldset>
                <h2>Current activity</h2>
                <table>
                    <tr class="${oddEven.oddEven}">
                        <td width="20%">
                            <strong>Active students</strong>
                        </td>
                        <td>
                            <strong>15</strong>
                        </td>
                    </tr>
                    <tr class="${oddEven.oddEven}">
                        <td>
                            <strong>Activity monitor</strong>
                        </td>
                        <td>
                            <strong>1</strong> student has not been engaged in collaboration
                            <br/><strong>2</strong> students have used too much time on a task
                        </td>
                    </tr>
                    <tr class="${oddEven.oddEven}">
                        <td>
                            <strong>Current grouping</strong>
                        </td>
                        <td>
                            <strong>4</strong> groups. Click here to rearrange or communicate
                        </td>
                    </tr>

                </table>
                <a href="/webapp/app/scyauthorruntime/ScyAuthorRuntimeIndex.html">Graphical activity view</a> | <a href="/webapp/app/scyauthorruntime/ScyAuthorRuntimeIndex.html">Time line view</a> 
            </fieldset>

        </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>