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
                box-shadow: 10px 10px 5px #888;
                border-radius:5px;
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
                background-color:#eeeeee;
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
                <br/>
                <form action="fbIndex.html" id="filterForm">
                    <strong>Show:</strong>
                    <select name="criteria" onChange="document.getElementById('filterForm').submit();">
                        <c:choose>
                            <c:when test="${criteria == 'NEWEST'}">
                                <option value="NEWEST" selected>Newest</option>
                            </c:when>
                            <c:otherwise>
                                <option value="NEWEST">Newest</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${criteria == 'COMMENTED'}">
                                <option value="COMMENTED" selected>Commented</option>
                            </c:when>
                            <c:otherwise>
                                <option value="COMMENTED">Commented</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${criteria == 'MOST_COMMENTED'}">
                                <option value="MOST_COMMENTED" selected>Most commented</option>
                            </c:when>
                            <c:otherwise>
                                <option value="MOST_COMMENTED">Most commented</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${criteria == 'NOT_COMMENTED'}">
                                <option value="NOT_COMMENTED" selected>Not commented</option>
                            </c:when>
                            <c:otherwise>
                                <option value="NOT_COMMENTED">Not commented</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${criteria == 'COMMENTED_BY_ME'}">
                                <option value="COMMENTED_BY_ME" selected>Commented by me</option>
                            </c:when>
                            <c:otherwise>
                                <option value="COMMENTED_BY_ME">Commented by me</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${criteria == 'HIGHEST_SCORED'}">
                                <option value="HIGHEST_SCORED" selected>Highest scored</option>
                            </c:when>
                            <c:otherwise>
                                <option value="HIGHEST_SCORED">Highest scored</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${criteria == 'MOST_VIEWED'}">
                                <option value="MOST_VIEWED" selected>Most Viewed</option>
                            </c:when>
                            <c:otherwise>
                                <option value="MOST_VIEWED">Most Viewed</option>
                            </c:otherwise>
                        </c:choose>





                    </select>
                    <strong>Category:</strong>
                    <select name="anchorElo" onChange="document.getElementById('filterForm').submit();">
                        <option value="ALL">All</option>
                        <c:choose>
                            <c:when test="${fn:length(allEloNames) > 0}">
                                <c:forEach var="eloName" items="${allEloNames}">
                                    <c:choose>
                                        <c:when test="${eloName == anchorElo}">
                                            <option value="${eloName}" selected>${eloName}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${eloName}">${eloName}</option>
                                        </c:otherwise>
                                    </c:choose> 
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </select>
                    <strong>Created by:</strong>
                    <select name="user" onChange="document.getElementById('filterForm').submit();">
                        <c:choose>
                            <c:when test="${uzer == 'ALL'}">
                                <option value="ALL" selected><spring:message code="ALL"/> </option>
                            </c:when>
                            <c:otherwise>
                                <option value="ALL"><spring:message code="ALL"/> </option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${uzer == 'MINE'}">
                                <option value="MINE" selected><spring:message code="MINE"/> </option>
                            </c:when>
                            <c:otherwise>
                                <option value="MINE"><spring:message code="MINE"/> </option>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${fn:length(uzers) > 0}">
                                <c:forEach var="uz" items="${uzers}">
                                    <c:choose>
                                        <c:when test="${uzer == uz.userDetails.username}">
                                            <option value="${uz.userDetails.username}" selected>${uz.userDetails.firstName}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${uz.userDetails.username}">${uz.userDetails.firstName}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                        </c:choose>

                        <option>Users</option>
                        <option>From</option>
                        <option>Authoring</option>
                        <option>Tool</option>
                    </select>
                    <input type="submit" value="search"/>
                    <br/>
                    <br/>
                    <input type="hidden" name="eloURI" value="${eloURI}">

                </form>
            </div>
            <c:choose>
                <c:when test="${fn:length(elos) > 0}">
                        <c:forEach var="elo" items="${elos}">
                            <div dojoType="dojox.layout.ContentPane" class="feedbackEloContainer greenBorders" style="width:30%;margin-right:15px;height:230px;float:left;background-color:#eeeeee;">
                                <div class="thumbContainer" style="background-color:#eeeeee;" >
                                    <a href="/webapp/app/feedback/webversion/ViewFeedbackForElo.html?eloURI=${elo.uri}&action=${action}" style="color:#23409e;">
                                        <img src="${elo.thumbnail}" />
                                    </a>
                                </div>
                                <div class="eloInfoContainer">
                                <p><strong><a href="/webapp/app/feedback/webversion/ViewFeedbackForElo.html?eloURI=${elo.uri}&action=${action}" style="color:#23409e;">${elo.myname}</a></strong></p>
                                <p>By: <a href="fbIndex.html?eloURI=${eloURI}&user=${elo.createdBy}" style="color:#23409e;">${elo.createdBy}</a></p>
                                <p>Date: ${elo.createdDate}</p>
                                <p>Viewed: ${elo.feedbackEloTransfer.shown}<br/>Average Score: ${elo.feedbackEloTransfer.averageScore}</p>
                                <p>Comments:  ${fn:length(elo.feedbackEloTransfer.feedbacks)}
                                </div>
                            </div>
                        </c:forEach>

                </c:when>
            </c:choose>
            



        </div>
        </tiles:putAttribute>
    </tiles:insertDefinition>
