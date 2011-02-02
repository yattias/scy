<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <table>
            <tr>
                <th colspan="2">These will be removed, used as examples</th>
            </tr>
            <tr>
                <td>
                    Tool provider
                </td>
                <td>
                    <a href="${toolURLProvider}">ToolURLProvider</a>
                </td>
            </tr>
            <tr>
                <td>
                    Load portfolio
                </td>
                <td>
                     <a href="/webapp/app/eportfolio/xml/loadPortfolio.html?missionURI=${missionURI}">Load portfolio</a>
                </td>
            </tr>
            <tr>
                <td>
                    Load learning goals
                </td>
                <td>
                    <a href="/webapp/app/eportfolio/xml/loadLearningGoals.html?missionURI=${missionURI}">Load learning goals</a>
                </td>
            </tr>
            <tr>
                <td>
                    ELOSearch
                </td>
                <td>
                    <a href="/webapp/app/eportfolio/xml/eloSearchService.html?missionURI=${missionURI}">Load elo search</a></td>
                </td>
            </tr>
            <tr>
                <td>
                    Add elo to portfolio
                </td>
                <td>
                    <a href="/webapp/app/eportfolio/xml/addEloToPortfolio.html?missionURI=${missionURI}">Add elo to portfolio</a>
                </td>
            </tr>
            <tr>
                <td>
                    Load obligatory elos
                </td>
                <td>
                <a href="/webapp/app/eportfolio/xml/obligatoryELOsInMission.html?missionURI=${missionURI}">Obligatory elos</a>
                </td>
            </tr>
            <tr>
                <td>
                    PortfolioConfig
                </td>
                <td>
                <a href="/webapp/app/eportfolio/xml/portfolioConfigService.html?missionURI=${missionURI}">Portfolio Configah</a>
                </td>
            </tr>
        </table>


       <link rel="stylesheet" type="text/css" href="http://www.intermedia.uio.no/www-data-public/app-scy/eportfolio/history/history.css" />
        <script type="text/javascript" src="http://www.intermedia.uio.no/www-data-public/app-scy/eportfolio/history/history.js"></script>
        <!-- END Browser History required section -->  
		    
        <script type="text/javascript" src="/webapp/themes/scy/eportfolio/swfobject.js"></script>
        <script type="text/javascript">
            <!-- For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. --> 
            var swfVersionStr = "10.0.0";
            <!-- To use express install, set to playerProductInstall.swf, otherwise the empty string. -->
            var xiSwfUrlStr = "/webapp/themes/scy/eportfolio/playerProductInstall.swf";
            var flashvars = {};
            flashvars.username = "${currentUser.userDetails.username}";
            flashvars.firstName = "${currentUser.userDetails.firstName}";
            flashvars.lastName = "${currentUser.userDetails.lastName}";
            flashvars.toolURLProvider = "${toolURLProvider}";
            flashvars.missionURI = "${missionURI}";
            flashvars.language = "${language}";
            var params = {};
            params.quality = "high";
            params.bgcolor = "#ffffff";
            params.allowscriptaccess = "always";
            params.allowfullscreen = "true";
            var attributes = {};
            attributes.id = "Main";
            attributes.name = "Main";
            attributes.align = "middle";
            swfobject.embedSWF(
                "/webapp/themes/scy/eportfolio/index.swf", "flashContent",
                "800", "600", 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
			<!-- JavaScript enabled so display the flashContent div in case it is not replaced with a swf object. -->
			swfobject.createCSS("#flashContent", "display:block;text-align:left;");
        </script>
        <div id="flashContent">
        	<p>
	        	To view this page ensure that Adobe Flash Player version 
				10.0.0 or greater is installed. 
			</p>
			<script type="text/javascript"> 
				var pageHost = ((document.location.protocol == "https:") ? "https://" :	"http://"); 
				document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
								+ pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
			</script> 
        </div>
	   	
       	<noscript>
            <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="800" height="600" id="Main">
                <param name="movie" value="/webapp/themes/scy/eportfolio/index.swf" />
                <param name="quality" value="high" />
                <param name="bgcolor" value="#ffffff" />
                <param name="allowScriptAccess" value="always" />
                <param name="allowFullScreen" value="true" />
                <!--[if !IE]>-->
                <object type="application/x-shockwave-flash" data="/webapp/themes/scy/eportfolio/index.swf" width="800" height="600">
                    <param name="quality" value="high" />
                    <param name="bgcolor" value="#ffffff" />
                    <param name="allowScriptAccess" value="always" />
                    <param name="allowFullScreen" value="true" />
                <!--<![endif]-->
                <!--[if gte IE 6]>-->
                	<p> 
                		Either scripts and active content are not permitted to run or Adobe Flash Player version
                		10.0.0 or greater is not installed.
                	</p>
                <!--<![endif]-->
                    <a href="http://www.adobe.com/go/getflashplayer">
                        <img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash Player" />
                    </a>
                <!--[if !IE]>-->
                </object>
                <!--<![endif]-->
            </object>
	    </noscript>		
    </tiles:putAttribute>
</tiles:insertDefinition>
