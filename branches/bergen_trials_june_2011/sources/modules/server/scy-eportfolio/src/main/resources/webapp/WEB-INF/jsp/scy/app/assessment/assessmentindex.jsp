<%@ include file="common-taglibs.jsp" %>

        <div id="flashContent">
        	<p>
	        	To view this page ensure that Adobe Flash Player version
				10.0.0 or greater is installed.
			</p>
			
        </div>

       <link rel="stylesheet" type="text/css" href="http://www.intermedia.uio.no/www-data-public/app-scy/eportfolio/history/history.css" />
        
        <!-- END Browser History required section -->

        <!--script type="text/javascript" src="/webapp/themes/scy/eportfolio/swfobject.js"></script-->
        <script type="text/javascript">
            <!-- For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. -->
            //dojo.addOnLoad(function(){
            //alert("${toolURLProvider}");
            var swfVersionStr = "10.0.0";
            <!-- To use express install, set to playerProductInstall.swf, otherwise the empty string. -->
            var xiSwfUrlStr = "/webapp/themes/scy/eportfolio/playerProductInstall.swf";
            var flashvars = {};
            flashvars.username = "${currentUser.userDetails.username}";
            flashvars.firstName = "${currentUser.userDetails.firstName}";
            flashvars.lastName = "${currentUser.userDetails.lastName}";
            flashvars.toolURLProvider = "${toolURLProvider}";
            flashvars.missionURI = "${missionURI}";
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
                "/webapp/themes/scy/assessment/index.swf", "flashContent",
                "800", "600",
                swfVersionStr, xiSwfUrlStr,
                flashvars, params, attributes);
			<!-- JavaScript enabled so display the flashContent div in case it is not replaced with a swf object. -->
			swfobject.createCSS("#flashContent", "display:block;text-align:left;");
               // });
        </script>

