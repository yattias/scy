<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

                <h2><spring:message code="MISSION"/> ${missionSpecificationTransporter.elo.title}</h2>

                <div style="width: 100%; height: 800px">
                    <div id="teacherTabContainer" dojoType="dijit.layout.TabContainer"
                         style="width: 100%; height: 100%;">
                    </div>
                </div>
                <br/>
                </fieldset>

                <script type="text/javascript">
                    dojo.addOnLoad(function() {
                        initTeacherTabs();
                    })
                </script>

    </tiles:putAttribute>
</tiles:insertDefinition>