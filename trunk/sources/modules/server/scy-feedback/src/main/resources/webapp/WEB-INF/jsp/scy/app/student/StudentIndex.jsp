<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>${missionSpecificationTransporter.elo.title} - WHAT DO YOU WANT TO DO?</h1>

        <table>
            <tr class="${oddEven.oddEven}">
                <td colspan="2">
                    <a href="${jnlpRef}">Start SCYLab</a>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    <a href="/webapp/app/eportfolio/EPortfolioIndex.html?eloURI=${missionSpecificationTransporter.uri}">Open my portfolio</a>
                </td>
                <td>
                    Portfolio contains <strong>2</strong> pieces of work. You need to complete <strong>4</strong> more! 
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    <a href="/webapp/app/feedback/ScyFeedbackIndex.html?eloURI=${missionSpecificationTransporter.uri}">Give Feedback</a>
                </td>
                <td>
                    <strong>4</strong> others have requested you to give feedback to their work
                </td>
            </tr>
            <tr  class="${oddEven.oddEven}">
                <td>
                    <a href="/webapp/app/feedback/FeedbackToolIndex.html?eloURI=${missionSpecificationTransporter.uri}">View Feedback</a>
                </td>
                <td>
                    Your work has gotten feedback from <strong>3</strong> others
                </td>
            </tr>

        </table>
    </tiles:putAttribute>
</tiles:insertDefinition>