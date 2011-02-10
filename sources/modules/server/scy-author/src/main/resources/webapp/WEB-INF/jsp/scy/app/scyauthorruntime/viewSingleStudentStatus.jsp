<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <s:currentStudentActivity username="${user.userDetails.username}"/>

    </tiles:putAttribute>
</tiles:insertDefinition>