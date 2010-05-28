<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
    
    <s:scenariodiagram scenario="${model.scenario}" pedagogicalPlan="${model}" includeRuntimeInfo="${true}" lasLink="${myLasLink}"/>
    <script type="text/javascript">
        setInterval("updateLasContentBox()", 3500);
    </script>
    </tiles:putAttribute>
</tiles:insertDefinition>