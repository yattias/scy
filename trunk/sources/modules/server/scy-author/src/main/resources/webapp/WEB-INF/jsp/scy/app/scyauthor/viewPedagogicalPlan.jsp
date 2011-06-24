<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
            <c:when test="${author}">

                <h1>Mission: ${missionSpecificationTransporter.elo.title}</h1>


            </c:when>
            <c:otherwise>

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


            </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>