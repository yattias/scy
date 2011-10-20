<%@ include file="common-taglibs.jsp" %>

<h2><spring:message code="MISSION_PROGRESS_BY_STUDENT"/> </h2>

<c:choose>
    <c:when test="${fn:length(userActivityList) > 0}">
        <table>

            <tr>
                <th>
                    <spring:message code="STUDENT"/>
                </th>
                <th>
                    <spring:message code="LAS"/>
                </th>
                <th>
                    ELOs submitted to ePortfolio
                </th>
                <th>
                    ELOs added to ePortfolio
                </th>
            </tr>

        <c:forEach var="userActivityInfo" items="${userActivityList}">
            <tr>
                <td>
                    <img src="/webapp/common/filestreamer.html?username=${userActivityInfo.parsedUserName}&showIcon"/>
                    ${userActivityInfo.parsedUserName}
                </td>
                <td>
                    ${userActivityInfo.lasName}
                </td>
                <td>
                    <c:choose>
                        <c:when test="${fn:length(userActivityInfo.portfolio.eloAnchorEloPairs) > 0}">
                            <c:forEach var="eloAnchorEloPair" items="${userActivityInfo.portfolio.eloAnchorEloPairs}">
                                <!--span style="width:80px;height:90px;float:left;"-->
                                <span style="width:40px;height:40px;float:left;">
                                    <a href="javascript:loadDialog('/webapp/components/openEloInScyLabDialog.html?eloURI=' +  encodeURIComponent('${eloAnchorEloPair.elo.uri}'), '${eloAnchorEloPair.elo.myname}');">
                                        <img src="${eloAnchorEloPair.elo.thumbnail}" width="60%" style="background-color:#cccccc;padding:2px;border:1px solid #cccccc;border-radius:3px;"/>
                                    </a>
                                </span>
                                <!--/span-->   
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </td>
                <td>
                    ${userActivityInfo.numberOfElosInPorfolio} / ${fn:length(obligatoryElos)}
                    <!--div dojoType="dijit.ProgressBar" style="width:300px" progress="${userActivityInfo.numberOfElosInPorfolio}" maximum="${fn:length(obligatoryElos)}">
                    </div-->
                </td>

            </tr>
        </c:forEach>
        </table>
    </c:when>
    <c:when test="${fn:length(userActivityList) == 0}">
        <spring:message code="NO_STUDENTS_CURRENTLY_LOGGED_ON"/>        
    </c:when>
</c:choose>


