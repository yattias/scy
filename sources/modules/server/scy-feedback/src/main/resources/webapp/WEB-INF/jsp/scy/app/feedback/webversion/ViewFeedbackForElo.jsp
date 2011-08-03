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
            <form method="POST" accept-charset="UTF-8" action="/webapp/app/feedback/webversion/AddFeedback.html">
                <textarea id="feedbacktext" name="feedbacktext"style="width:100%;height:50px;"></textarea><br/>
                <input type="hidden" name="feedbackEloURI" id="feedbackEloURI" value="${feedbackElo.uri}"/>
                <!--s:ajaxELOSlider sliderValues="${feedbackLevels}" defaultValue="${scaffoldingLevel}" eloURI="${transferElo.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/-->
                <input type="text" name="score" id="score"/>
                Quality: (Slider here) |----------------------|-----------------------|--------------------|
                <input type="Submit" value="GIVE/GET FEEDBACK" />
            </form>





        </td>
    </tr>
</table>

<table>
    <c:choose>
        <c:when test="${fn:length(feedbackElo.feedbacks) > 0}">
                <c:forEach var="feedbackItem" items="${feedbackElo.feedbacks}">
                    <tr>
                        <td>
                            ${feedbackItem.comment}
                        </td>
                        <td>
                            ${feedbackItem.evalu}
                        </td>
                        <td>
                            ${feedbackItem.createdBy}
                        </td>
                        <td>
                            ${feedbackItem.createdByPicture}
                        </td>
                    </tr>

                </c:forEach>
        </c:when>
    </c:choose>

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