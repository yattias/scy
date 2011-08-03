<%@ include file="common-taglibs.jsp" %>
<p>Mission: ${elo.myname}</p>
<p>URI: ${elo.uri}</p>
<p>Give feedback</p>
<table>
    <tr>
        <td style="width:30%;">
            <img src="${elo.thumbnail}" alt="" /><br/>
            <strong>${elo.myname}</strong><br/>
            By: ${feedbackEloTransfer.shown}<br/>
            Entered: ${elo.createdDate} <br/>
            Catname: ${elo.catname}<br/> 
            Viewed: <br/>
            Quality Score Average: ${elo.grade}
        </td>
        <td style="width:70%;">
            <textarea style="width:100%;height:50px;"></textarea><br/>
            <!--s:ajaxELOSlider sliderValues="${feedbackLevels}" defaultValue="${scaffoldingLevel}" eloURI="${elo.uri}" property="globalMissionScaffoldingLevel" rooloServices="${rooloServices}"/-->
            Quality: (Slider here) |----------------------|-----------------------|--------------------|
            <input type="Submit" value="GIVE/GET FEEDBACK" />


        </td>
    </tr>
</table>
<table>
    <tr>
        <td style="width:100%;height:200px;">
            <div dojoType="dojox.layout.ContentPane" href="${elo.feedbackEloUrl}">
                
            </div>
        </td>
    </tr>
</table>
<a href="javascript:loadAccordionContent('newestElosContainer', '/webapp/app/feedback/webversion/NewestElosList.html?eloURI=${missionURI}');">Cancel</a>


<!--Her maa du see paa TransferElo for aa se hvilke properties du kan hente ut-->