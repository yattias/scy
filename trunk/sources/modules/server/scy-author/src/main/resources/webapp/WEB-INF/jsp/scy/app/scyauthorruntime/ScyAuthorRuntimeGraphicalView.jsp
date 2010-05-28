<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
    
    <s:scenariodiagram scenario="${model.scenario}" pedagogicalPlan="${model}" includeRuntimeInfo="${true}" lasLink="${myLasLink}"/>
    
    </tiles:putAttribute>
</tiles:insertDefinition>