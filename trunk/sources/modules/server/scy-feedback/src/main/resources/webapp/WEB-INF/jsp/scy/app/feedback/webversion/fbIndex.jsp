<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="main">
        <script type="text/javascript">
            function loadAccordionContent(container, url){

                dijit.byId(container).set('href', url);
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

             html, body { height: 100%; width: 100%; margin: 0; padding: 0; }

            .feedbackEloContainer{
                border:2px solid #ff9933;
                margin:2px;
                /*background-color:#cc6600;*/
            }

            .feedbackEloContainer a:hover{
                background-color:transparent !important;
                text-decoration:underline;
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
        <div dojoType="dojox.layout.ContentPane" executeScripts="true" parseOnLoad="true" style="border:4px solid #cc6600;border-bottom-left-radius:40px;width:786px;height:95%;padding:4px;" class="greenBorders" parseWidgets="true">
            <div class="feedbackHeader" >ELO Gallery



            </div>
            <div>
                <form action="fbIndex.html" id="filterForm">
                    <strong>Show:</strong>
                    <select name="criteria" onChange="document.getElementById('filterForm').submit();">
                        <option value="NEWEST">Newest</option>
                        <option value="COMMENTED">Commented</option>
                        <option value="MOST_COMMENTED">Most commented</option>
                        <option value="NOT_COMMENTED">Not commented</option>
                        <option value="COMMENTED_BY_ME">Commented by me</option>
                        <option value="HIGHEST_SCORED">Highest scored</option>
                        <option value="MOST_VIEWED">Most Viewed</option>
                    </select>
                    <strong>Category:</strong>
                    <select name="anchorElo" onChange="document.getElementById('filterForm').submit();">
                        <option value="ALL">All</option>
                        <c:choose>
                            <c:when test="${fn:length(anchorElos) > 0}">
                                <c:forEach var="anchorElo" items="${anchorElos}">
                                    <option value="anchorElo.uri">${anchorElo.title}</option>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </select>
                    <strong>Person:</strong>
                    <select name="user" onChange="document.getElementById('filterForm').submit();">
                        <option value="ALL"><spring:message code="ALL"/> </option>
                        <option value="MINE"><spring:message code="MINE"/> </option>
                        <option>Users</option>
                        <option>From</option>
                        <option>Authoring</option>
                        <option>Tool</option>
                    </select>
                    <input type="submit" value="search"/>
                    <br/><h2>Click on an ELO to give feedback</h2>
                    <input type="hidden" name="eloURI" value="${eloURI}">

                </form>
            </div>
            <c:choose>
                <c:when test="${fn:length(elos) > 0}">
                        <c:forEach var="elo" items="${elos}">
                            <div dojoType="dojox.layout.ContentPane" class="feedbackEloContainer greenBackgrounds greenBorders" style="width:30%;height:246px;float:left;">
                                <div class="thumbContainer lightGreenBackgrounds">
                                    <a href="/webapp/app/feedback/webversion/ViewFeedbackForElo.html?eloURI=${elo.uri}" style="color:#ffffff;">
                                        <img src="${elo.thumbnail}" />
                                    </a>
                                </div>
                                <div class="eloInfoContainer">
                                <p><strong><a href="/webapp/app/feedback/webversion/ViewFeedbackForElo.html?eloURI=${elo.uri}" style="color:#ffffff;">${elo.myname}</a></strong></p>
                                <p>Category: ${elo.catname}</p>
                                <p>By: <a href="fbIndex.html?eloURI=${eloURI}&user=${elo.createdBy}">${elo.createdBy}</a></p>
                                <p>Date: ${elo.createdDate}</p>
                                <p>Shown: ${elo.feedbackEloTransfer.shown}<br/> / Score: ${elo.feedbackEloTransfer.score}</p>

                                </div>
                            </div>
                        </c:forEach>

                </c:when>
            </c:choose>
            



        </div>
        </tiles:putAttribute>
    </tiles:insertDefinition>
