<%@ include file="common-taglibs.jsp" %>
<div>
    <strong>Show:</strong>
    <select>
        <option>Newest</option>
        <option>Commented</option>
        <option>Most commented</option>
        <option>Not commented</option>
        <option>Commented by me</option>
        <option>Highest scored</option>
        <option>Most Viewed</option>
    </select>
    <strong>Category:</strong>
    <select>
        <option value="ALL">All</option>
        <c:choose>
            <c:when test="${fn:length(anchorElos) > 0}">
                <c:forEach var="anchorElo" items="${anchorElos}">
                    <option value="anchorElo.uri">${anchorElo.title}</option>
                </c:forEach>
            </c:when>
        </c:choose>
        <!-- get all ELO types from the various missions in the list -->
        <!--option>Concept map</option>
        <option>House design</option>
        <option>Simulation</option-->
    </select>
    <strong>Person:</strong>
    <select>
        <option>Mine</option>
        <option>All</option>
        <option>Users</option>
        <option>From</option>
        <option>Authoring</option>
        <option>Tool</option>
    </select>
    <br/><h2>Click on an ELO to give feedback</h2>
</div>
<c:choose>
    <c:when test="${fn:length(elos) > 0}">
            <c:forEach var="elo" items="${elos}">
                <div dojoType="dojox.layout.ContentPane" class="feedbackEloContainer greenBackgrounds greenBorders" style="width:30%;height:246px;float:left;">
                    <div class="thumbContainer lightGreenBackgrounds">
                        <a href="javascript:loadAccordionContent('newestElosContainer', '../webversion/ViewFeedbackForElo.html?eloURI=${elo.uri}&amp;listUri=${listUri}');" style="color:#ffffff;">
                            <img src="${elo.thumbnail}" />
                        </a>    
                    </div>
                    <div class="eloInfoContainer">
                    <p><strong><a href="javascript:loadAccordionContent('newestElosContainer', '../webversion/ViewFeedbackForElo.html?eloURI=${elo.uri}&amp;listUri=${listUri}');" style="color:#ffffff;">${elo.myname}</a></strong></p>
                    <p>Category: ${elo.catname}</p>
                    <p>By: ${elo.createdBy}</p>
                    <p>Date: ${elo.createdDate}</p>
                    <p>Shown: ${feedbackEloTransfer.shown}<br/> / Score: ${feedbackEloTransfer.shown}</p>

               <!-- ${elo.uri}
                        ${elo.technicalFormat}
                   -->
                    </div>
                </div>
            </c:forEach>

    </c:when>
</c:choose>
