<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h2>${user.userDetails.firstName} ${user.userDetails.lastName}</h2>
    </tiles:putAttribute>
</tiles:insertDefinition>