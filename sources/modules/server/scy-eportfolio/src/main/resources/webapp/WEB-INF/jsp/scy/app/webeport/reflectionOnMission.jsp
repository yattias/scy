<%@ include file="common-taglibs.jsp" %>
<!--tiles:insertDefinition name="default-page"-->
    <!--tiles:putAttribute name="main"-->

        <script type="text/javascript">
            function checkTextAreasAndEnableSubmit(formid){
                var textareas = document.getElementById(formid).getElementsByTagName("textarea");
                var isReadyToSubmit = false;
                for(var i = 0;i<textareas.length;i++){
                    if(textareas[i].value.length >= 4){
                        isReadyToSubmit = true;
                    } else {
                        isReadyToSubmit = false;
                        break;
                    }
                }
                if(isReadyToSubmit){
                    document.getElementById("submitEport").disabled = false;
                }
            }
        </script>

        <style type="text/css">
                .feedbackHeader{
                        background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                        background-repeat:no-repeat;
                        color:#ffffff;
                        height:50px;
                        background-color:#333333 !important;
                        font-weight:bold;
                        font-size:25px;
                        text-align:center;
                        padding-top:20px;
                    }

            .soria .dijitTabPaneWrapper{
                background-color:transparent !important;
            }
            </style>
                <!--div style="border:4px solid #cc6600;width:786px;height:95%;padding:4px;" class="greenBorders"-->
                            <!--img src="/webapp/themes/scy/default/images/feedback_header.png" alt="" class="greenBackgrounds" /-->
                     <!--div class="feedbackHeader" >My ePortfolio</div-->

                        <div dojoType="dojox.layout.ContentPane" style="width:100%;height:90%;background-color:transparent;" id="eportfolioReflectionPane" parseOnLoad="true" executeScripts="true">

       

        <c:if test="${fn:contains(portfolio.portfolioStatus, 'PORTFOLIO_STATUS_NOT_SUBMITTED')}">
            <c:choose>
                <c:when test="${fn:length(pedagogicalPlan.assessmentSetup.reflectionTabs) > 0}">
                    <form action="storeReflectionsOnMission.html" id="storeReflectionsOnMission" style="display:block;height:50%;">
                    <div dojoType="dijit.layout.TabContainer" style="height:100%;background-color:transparent;">

                            <c:forEach var="tab" items="${pedagogicalPlan.assessmentSetup.reflectionTabs}">
                                <div dojoType="dojox.layout.ContentPane" style="background-color:transparent;" title="${tab.title}">
                                    <table>
                                        <tr>
                                    <td width="30%" style="text-align:right; vertical-align:top;">
                                        <strong>${tab.question}</strong>
                                    </td>
                                    <td style="text-align:left;">
                                        <c:if test="${fn:contains(tab.type, 'text')}">
                                            <textarea rows="4" cols="30" name="${tab.id}" style="width:100%;" onkeyup="checkTextAreasAndEnableSubmit('storeReflectionsOnMission')"></textarea>
                                        </c:if>
                                        <c:if test="${fn:contains(tab.type, 'slider')}">
                                            <input name="${tab.id}" type="text" id="${tab.id}" value="1" style="display:none;"/>
                                            <div id="horizontalSlider" dojoType="dijit.form.HorizontalSlider" value="1" minimum="1" maximum="4" discreteValues="1" intermediateChanges="false" showButtons="false" style="width:90%;margin-top:5px;" onChange="document.getElementById('${tab.id}').value = Math.round(this.value);">
                                                <ol dojoType="dijit.form.HorizontalRuleLabels" container="topDecoration" style="height:1.5em;font-size:75%;color:gray;">
                                                    <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_1.png" alt=""  /></li>
                                                    <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_2.png" alt=""  /></li>
                                                    <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_3.png" alt=""  /></li>
                                                    <li style="margin-bottom:5px;"><img src="/webapp/themes/scy/default/images/smiley_4.png" alt=""  /></li>
                                                </ol>
                                                <div dojoType="dijit.form.HorizontalRule" container="bottomDecoration" count="4" style="height:5px;">
                                                    <ol dojoType="dijit.form.HorizontalRuleLabels" container="bottomDecoration" style="height:1em;font-size:75%;color:gray;"></ol>
                                                </div>
                                            </div>
                                        </c:if>
                                    </td>
                                </tr>
                                        </table>
                                    </div>
                            </c:forEach>




                        </div>
                        <table style="width:100%;">
                        <tr>
                            <td style="text-align:center;">
                                <input type="hidden" value="${missionRuntimeURI}" name="missionRuntimeURI"/>
                                <input type="submit" value="<spring:message code="SUBMIT_PORTFOLIO"/>" id="submitEport" disabled="true"/>
                            </td>
                        </tr>
                        </table>
                       </form>

                </c:when>
            </c:choose>
            </c:if>
        </div>
        <!--/div-->
        
    <!--/tiles:putAttribute-->
<!--/tiles:insertDefinition-->