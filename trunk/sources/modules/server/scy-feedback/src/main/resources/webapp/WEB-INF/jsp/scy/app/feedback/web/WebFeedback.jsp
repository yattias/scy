<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="main">
        <script type="text/javascript">
            function loadAccordionContent(container, url){
                
                dijit.byId(container).attr('href', url);
            }

            function loadAccordionContentWithEscapedURL(container, url){
               dijit.byId(container).attr('href', escape(url)); 
            }

            

            function openDialog(title){
                var dialog = new dijit.Dialog({
                    title:title, 
                    content: "<h2>Show the ELO here!</h2>"
                });
                dialog.style.width = "600px";

                dialog.show();
            }
        </script>
        <style type="text/css">
            .feedbackEloContainer{
                border:2px solid #ff9933;
                margin:2px;
                /*background-color:#cc6600;*/
            }

            .feedbackEloContainer .thumbContainer{
                margin:2px;
                padding:2px;
                text-align:center;
                /*background-color:#e5994c;*/
            }

            .feedbackEloContainer .eloInfoContainer{
                padding:2px;
            }

            .soria .dijitAccordionTitle {
               background-color:#6bcfdf !important;
                background-image:url() !important;

            }

            .soria .dijitAccordionTitle-selected{
                background-color:#03a5be !important;
                background-image:url() !important;
                color:#ffffff;
            }

            .greenBackgrounds{
                background-color:#03a5be !important;    
            }

            .lightGreenBackgrounds{
                background-color:#6bcfdf !important;
            }
            .extraLightGreenBackgrounds{
                background-color:#e3f5f8 !important;
            }

            .greenBorders{
                border-color:#03a5be !important;
            }

            .feedbackHeader{
                background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                background-repeat:no-repeat;
                color:#ffffff;
                height:50px;
                background-color:#03a5be !important;
                font-weight:bold;
                font-size:25px;
                text-align:center;
                padding-top:20px;
            }

        </style>
        <div dojoType="dojox.layout.ContentPane" executeScripts="true" parseOnLoad="true" style="border:4px solid #cc6600;width:786px;height:95%;padding:4px;" class="greenBorders">
            <!--img src="/webapp/themes/scy/default/images/feedback_header.png" alt="" class="greenBackgrounds" /-->
            <div class="feedbackHeader" >ELO Gallery</div>
            <!--p>Click on an ELO to give feedback</p-->
        <div dojoType="dijit.layout.TabContainer" style="width:100%;height:90%;">
            <div dojoType="dojox.layout.ContentPane" refreshOnShow="true" title="Newest ELOs" href="/webapp/app/feedback/webversion/NewestElosList.html?eloURI=${eloURI}" executeScripts="true" parseOnLoad="true" id="newestElosContainer"></div>
            <div dojoType="dojox.layout.ContentPane" refreshOnShow="true"  title="My ELOs" href="/webapp/app/feedback/webversion/MyElosList.html?eloURI=${eloURI}" executeScripts="true"parseOnLoad="true" id="myElosContainer"></div>
            <div dojoType="dojox.layout.ContentPane" refreshOnShow="true"  title="ELOs I have commented on" href="/webapp/app/feedback/webversion/ContributedElosList.html?eloURI=${eloURI}" executeScripts="true"parseOnLoad="true" id="commentedElosContainer"></div>

        </div>
        </div>
        </tiles:putAttribute>
    </tiles:insertDefinition>
