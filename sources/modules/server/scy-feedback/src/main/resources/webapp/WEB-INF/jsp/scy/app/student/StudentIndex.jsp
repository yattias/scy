<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <div dojoType="dijit.Tooltip" connectId="labId" position="below">
            <table>
                <tr>
                    <td align="left" valign="top">
                        <h2><spring:message code="CLICK_TO_START_SCY_LAB"/> </h2>
                        ${content}            
                    </td>
                </tr>
            </table>

        </div>

        <div dojoType="dijit.Tooltip" connectId="portfolioId" position="below">
            <table>
                <tr>
                    <td align="left" valign="top">
                        <h2><spring:message code="CLICK_TO_OPEN_YOUR_EPORTFOLIO"/> </h2>
                        <!--table>
                            <tr>
                                <td align="left" valign="top">

                                </td>
                            </tr>
                        </table-->
                    </td>
                </tr>
            </table>

        </div>


        <div dojoType="dijit.Tooltip" connectId="feedbackId" position="below">
            <table>
                <tr>
                    <td align="left" valign="top">
                        <h2><spring:message code="CLICK_TO_OPEN_THE_FEEDBACK_TOOL"/> </h2>
                        <table>
                            <tr>
                                <td align="left" valign="top">
                                    <spring:message code="FEEDBACK_EXPLANATION"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

        </div>


        <h1>${missionSpecificationTransporter.elo.title}</h1>

        <table>
            <tr class="${oddEven.oddEven}">
                <td colspan="2">
                    <a id="labId" href="${jnlpUrl}"><spring:message code="START_SCYLAB"/> </a>
                </td>
            </tr>
            <!--tr class="${oddEven.oddEven}">
                <td colspan="2">
                    <a href="${descriptionUrl}" target="_blank"><spring:message code="READ_THE_MISSION_DESCRIPTION"/></a>
                </td>
            </tr-->
            <tr class="${oddEven.oddEven}">
                <td>
                    <a id="portfolioId" href="/webapp/app/webeport/webEportIndex.html?eloURI=${missionSpecificationTransporter.uri}"><spring:message code="OPEN_MY_PORTFOLIO"/></a>
                </td>
                <td>
                    <spring:message code="${portfolioStatus}"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    <a id="feedbackId" href="/webapp/app/feedback/webversion/fbIndex.html?eloURI=${missionSpecificationTransporter.uri}"><spring:message code="FEEDBACK"/></a>
                </td>
                <td>
                    <spring:message code="YOU_HAVE_CONTRIBUTED_WITH_FEEDBACK_TO"/> ${elosWhereIHaveProvidedFeedback} <spring:message code="ELOS"/>. <spring:message code="YOUR_WORK_HAS_GOTTEN_FEEDBACK_FROM"/> <strong>${numberOfFeedbacksToMyElos}</strong> <spring:message code="OTHERS"/>
                </td>
            </tr>
        </table>
    </tiles:putAttribute>
</tiles:insertDefinition>