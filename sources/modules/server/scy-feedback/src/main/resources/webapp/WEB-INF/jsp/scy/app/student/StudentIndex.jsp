<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>${missionSpecificationTransporter.elo.title}</h1>

        <table>
            <tr class="${oddEven.oddEven}">
                <td colspan="2">
                    <a href="${jnlpUrl}"><spring:message code="START_SCYLAB"/> </a>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    <a href="/webapp/app/webeport/webEportIndex.html?eloURI=${missionSpecificationTransporter.uri}"><spring:message code="OPEN_MY_PORTFOLIO"/></a>
                </td>
                <td>
                    <spring:message code="${portfolioStatus}"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    <a href="/webapp/app/feedback/webversion/fbIndex.html?eloURI=${missionSpecificationTransporter.uri}"><spring:message code="FEEDBACK"/></a>
                </td>
                <td>
                    <spring:message code="YOU_HAVE_CONTRIBUTED_WITH_FEEDBACK_TO"/> ${elosWhereIHaveProvidedFeedback} <spring:message code="ELOS"/>. <spring:message code="YOUR_WORK_HAS_GOTTEN_FEEDBACK_FROM"/> <strong>${numberOfFeedbacksToMyElos}</strong> <spring:message code="OTHERS"/>
                </td>
            </tr>
        </table>
    </tiles:putAttribute>
</tiles:insertDefinition>