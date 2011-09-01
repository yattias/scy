<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="dialog-page">
    <tiles:putAttribute name="main">

        add new student


        <a href="javascript:openPage('studentsForPedagogicalPlan', 'viewStudentsForPedagogicalPlan.html?eloURI=' + encodeURIComponent('${missionSpecificationEloURI}'));">Add student</a>        <br/>
    </tiles:putAttribute>
</tiles:insertDefinition>
