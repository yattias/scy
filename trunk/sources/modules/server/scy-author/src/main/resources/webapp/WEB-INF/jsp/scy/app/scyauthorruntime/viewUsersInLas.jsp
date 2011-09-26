<%@ include file="common-taglibs.jsp" %>
<c:choose>
    <c:when test="${fn:length(lasActivityList) > 0}">
        <c:forEach var="lasActivityInfo" items="${lasActivityList}" varStatus="rowCounter">
                    <table style="width:100%;">
                    <tr>
                        <th>
                            ${lasActivityInfo.humanReadableName}
                        </th>
                    </tr>
                    <tr>
                        <td style="width:100%;height:150px;">
                            <div dojoType="dojox.widget.FisheyeList"
		itemWidth="80" itemHeight="80"
		itemMaxWidth="110" itemMaxHeight="110"
		orientation="horizontal"
		effectUnits="2"
		itemPadding="5"
		attachEdge="top"
		labelEdge="bottom"
		id="fisheye${rowCounter.count}"
        conservativeTrigger="true" width="100% !important;">

                        <c:choose>
                            <c:when test="${fn:length(lasActivityInfo.activeUsers) > 0}">
                                <c:forEach var="activeUser" items="${lasActivityInfo.activeUsers}">


         <div dojoType="dojox.widget.FisheyeListItem" onMouseEnter="renderHtmlLabel(this)" label="<strong>${activeUser.userDetails.firstName} ${activeUser.userDetails.lastName}</strong>" iconSrc="/webapp/common/filestreamer.html?username=${activeUser.userDetails.username}&showIcon" style="float:left;" ><div>yata</div></div>




                                </c:forEach>
                            </c:when>
                        </c:choose>
                           </div> 
                        </td>
                     </tr>
                </c:forEach>
        </table>
    </c:when>
    <c:when test="${fn:length(lasActivityList) == 0}">
        <spring:message code="NO_STUDENTS_CURRENTLY_LOGGED_ON"/>
    </c:when>
</c:choose>

