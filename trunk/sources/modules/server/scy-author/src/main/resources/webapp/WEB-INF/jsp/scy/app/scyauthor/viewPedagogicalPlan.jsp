<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <style type="text/css">
             /* FISHEYE START */
            .dojoxFisheyeListItemLabel {

                font-family: Arial, Helvetica, sans-serif;

                background-color: #666666;

                border: 2px solid #333333;

                color:#ffffff;

                padding: 2px;

                text-align: center;

                position: absolute;

                display: none;

                white-space:nowrap;

            }

            .dojoxFisheyeListItemLabel.dojoxFishSelected {

                display: block;

            }

            .dojoxFisheyeListItemImage {

                border: 0px;

                position: absolute;

            }

            .dojoxFisheyeListItem {

                position: absolute;

                z-index: 2;

                width:40px;
                height:40px;
                box-shadow: 10px 10px 5px #888;
                border-radius:5px;
                /*background-color:#cccccc;*/

            }

            .dojoxFisheyeListBar {

                position: relative;

            }



                    .dojoxFisheyeListBar {

                        margin: 0 auto;

                        text-align: left;

                    }

                    .outerbar {

                        background-color: #666;

                        text-align: center;

                        position: absolute;

                        left: 0px;

                        top: 0px;

                        width: 100%;

                        border-bottom:2px solid #333;

                    }



            .fisheye ul, .fisheye ul li{

            text-align:left !important;


            }



            .fisheye p{

            margin:0;

            padding:0;

            }

            

             /* FISHEYE END */
        </style>



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
                        initTeacherTabs('${tab}');
                    })
                </script>

    </tiles:putAttribute>
</tiles:insertDefinition>