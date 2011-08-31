<%@ page import="java.net.URLEncoder" %>
<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

    <script type="text/javascript">
dojo.require("dojox.widget.FisheyeList");
dojo.require("dojo.parser")
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

            .greenBorders{
                border-color:#03a5be !important;
            }

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


             /* FISHEYE START */
            .dojoxFisheyeListItemLabel {

                font-family: Arial, Helvetica, sans-serif;

                background-color: #d4e2ca;

                border: 2px solid #2085b1;

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



            #fisheye1 ul, #fisheye1 ul li{

            text-align:left !important;

            }



            #fisheye1 p{

            margin:0;

            padding:0;

            }

            .assessedtrue{
                background-color:#00ff00;
            }

            .assessedfalse{
                background-color:#666666;
            }
               
             /* FISHEYE END */


        </style>

        <div style="border:4px solid #cc6600;width:786px;height:95%;padding:4px;" class="greenBorders">
            <!--img src="/webapp/themes/scy/default/images/feedback_header.png" alt="" class="greenBackgrounds" /-->
            <div class="feedbackHeader" >My ePortfolio</div>
            <p>My ELOs (Grey = missing, Green = added)</p>
        <div dojoType="dojox.layout.ContentPane" style="width:100%;height:90%;">
            <!--div dojoType="dojox.layout.ContentPane"  title="Newest ELOs" href="/webapp/app/feedback/webversion/NewestElosList.html?eloURI=${eloURI}" executeScripts="true" id="newestElosContainer"></div>
            <div dojoType="dojox.layout.ContentPane"  title="My ELOs" href="/webapp/app/feedback/webversion/MyElosList.html?eloURI=${eloURI}" executeScripts="true"></div>
            <div dojoType="dojox.layout.ContentPane"  title="ELOs I have commented on" href="/webapp/app/feedback/webversion/ContributedElosList.html?eloURI=${eloURI}" executeScripts="true"></div-->
          <!--table>
            <tr>
                <td>owner</td>
                <td>${portfolio.owner}</td>
            </tr>
            <tr>
                <td>portfolioStatus</td>
                <td>${portfolio.portfolioStatus}</td>
            </tr>
            <tr>
                <td>missionRuntimeURI</td>
                <td>${portfolio.missionRuntimeURI}</td>
            </tr>
            <tr>
                <td>missionName</td>
                <td>${portfolio.missionName}</td>
            </tr>
            <tr>
                <td>reflectionMission</td>
                <td>${portfolio.reflectionMission}</td>
            </tr>
            <tr>
                <td>reflectionCollaboration</td>
                <td>${portfolio.reflectionCollaboration}</td>
            </tr>
            <tr>
                <td>reflectionInquiry</td>
                <td>${portfolio.reflectionInquiry}</td>
            </tr>
            <tr>
                <td>reflectionEffort</td>
                <td>${portfolio.reflectionEffort}</td>
            </tr>
            <tr>
                <td>assessmentPortfolioComment</td>
                <td>${portfolio.assessmentPortfolioComment}</td>
            </tr>
            <tr>
                <td>assessmentPortfolioRating</td>
                <td>${portfolio.assessmentPortfolioRating}</td>
            </tr>
            <tr>
                <td>assessed</td>
                <td>${portfolio.assessed}</td>
            </tr>
        </table-->

            <div dojoType="dojox.widget.FisheyeList"
		itemWidth="40" itemHeight="40"
		itemMaxWidth="100" itemMaxHeight="100"
		orientation="horizontal"
		effectUnits="2"
		itemPadding="10"
		attachEdge="top"
		labelEdge="bottom"
		id="fisheye1"
        conservativeTrigger="true">




            <c:choose>
        <c:when test="${fn:length(obligatoryAnchorElos) > 0}">
            <c:forEach var="anchorElo" items="${obligatoryAnchorElos}">
               
               <a href="/webapp/app/webeport/selectELOFromGallery.html?eloURI=${anchorElo.uri}">
                    OPEN: ${anchorElo.myname}
               </a>
                   <div dojoType="dojox.widget.FisheyeListItem"  onclick=""  label="${anchorElo.myname}" iconSrc="${anchorElo.thumbnail}" isContainer="true" style="cursor:pointer; margin:3px;" class="assessed${anchorElo.assessed}">
                         ${anchorElo.myname}
	                </div>


            </c:forEach>
        </c:when>
    </c:choose>
                </div>
        </div>
        </div>

   







    </tiles:putAttribute>
</tiles:insertDefinition>