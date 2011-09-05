<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <style type="text/css">

        .assessmentHeader{
                background-image:url(/webapp/themes/scy/default/images/feedback_header.png);
                background-repeat:no-repeat;
                color:#ffffff;
                height:50px;
                background-color:#686808 !important;
                font-weight:bold;
                font-size:25px;
                text-align:center;
                padding-top:20px;
            }

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
                /*background-color:#cccccc;*/

            }

            .dojoxFisheyeListBar {

                position: relative;

            }



                    .dojoxFisheyeListBar {

                        margin: 0 auto;

                        text-align: center;

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



            #assessmentFisheye ul, #assessmentFisheye ul li{

            text-align:left !important;

            }



            #assessmentFisheye p{

            margin:0;

            padding:0;

            }

        </style>




        <div style="border:4px solid #686808;width:786px;height:95%;padding:4px;" class="brownBorders">
                    <!--img src="/webapp/themes/scy/default/images/feedback_header.png" alt="" class="greenBackgrounds" /-->
                    <div class="assessmentHeader" >Assess ELO</div>
                    
                <div dojoType="dojox.layout.ContentPane" style="width:100%;height:90%;" id="eportfolioPane" parseOnLoad="true" executeScripts="true">
                   <div dojoType="dojox.widget.FisheyeList"
		itemWidth="80" itemHeight="80"
		itemMaxWidth="150" itemMaxHeight="150"
		orientation="horizontal"
		effectUnits="2"
		itemPadding="10"
		attachEdge="top"
		labelEdge="bottom"
		id="assessmentFisheye"
        conservativeTrigger="true">
                    <c:choose>
                        <c:when test="${fn:length(elos) > 0}">
                            <!--table-->
                                <c:forEach var="elo" items="${elos}">
                                    <!--tr>
                                        <td-->
                                            <div dojoType="dojox.widget.FisheyeListItem" onclick="location.href='assessElo.html?eloURI=${elo.uri}&missionRuntimeURI=${missionRuntimeURI}'"  label="${elo.myname}" iconSrc="${elo.thumbnail}" isContainer="true" style="cursor:pointer; margin:3px;border:3px solid #ffffff;" ></div>
                                            <!--img src="${elo.thumbnail}"/-->
                                        <!--/td>
                                        <td>
                                            <a href="assessElo.html?eloURI=${elo.uri}&missionRuntimeURI=${missionRuntimeURI}">${elo.myname}</a>
                                        </td>
                                    </tr-->
                                </c:forEach>
                                <!--/table-->
                        </c:when>
                    </c:choose>

        </div>
        </div>      <a href="assessMission.html?missionRuntimeURI=${missionRuntimeURI}"><spring:message code="ASSESS_MISSION"/> </a>

    </tiles:putAttribute>
</tiles:insertDefinition>