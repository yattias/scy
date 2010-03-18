<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="main">
       ${model.name}
    </tiles:putAttribute>
</tiles:insertDefinition>
