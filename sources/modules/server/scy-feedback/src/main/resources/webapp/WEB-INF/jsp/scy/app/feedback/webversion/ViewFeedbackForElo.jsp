<%@ include file="common-taglibs.jsp" %>
<p>Give feedback</p>
<table>
    <tr>
        <td style="width:30%;">
            <img src="${transferElo.thumbnail}" alt="" /><br/>
            <strong>${transferElo.myname}</strong><br/>
            By: ${transferElo.createdBy}<br/>
            Entered: ${transferElo.createdDate} <br/>
            Catname: ${transferElo.catname}<br/>
            Viewed: ${feedbackElo.shown}<br/>
            Quality Score Average: ${elo.grade}
        </td>
        <td style="width:70%;">
            <textarea style="width:100%;height:50px;"></textarea><br/>
            <!--s:ajaxELOSlider sliderValues="${feedbackLevels}" defaultValue="${scaffoldingLevel}" eloURI="${transferElo.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/-->
            Quality: (Slider here) |----------------------|-----------------------|--------------------|
            <input type="Submit" value="GIVE/GET FEEDBACK" />

<c:choose>
    <c:when test="${fn:length(feedbackElo.feedbacks) > 0}">
            <c:forEach var="feedbackItem" items="${feedbackElo.feedbacks}">
                ${feedbackItem.comment}
            </c:forEach>
    </c:when>
</c:choose>


        </td>
    </tr>
</table>
<table>
    <tr>
        <td style="width:100%;height:200px;">
            <div dojoType="dojox.layout.ContentPane" href="${transferElo.feedbackEloUrl}">
                
            </div>
        </td>
    </tr>
</table>
<a href="javascript:loadAccordionContent('newestElosContainer', '/webapp/app/feedback/webversion/NewestElosList.html?eloURI=${missionURI}');">Cancel</a>


<!--Her maa du see paa TransferElo for aa se hvilke properties du kan hente ut-->