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
                            <c:choose>
                                <c:when test="${fn:length(lasActivityInfo.activeUsers) > 0}">
                                    <c:forEach var="activeUser" items="${lasActivityInfo.activeUsers}">
                                        <div dojoType="dijit.Tooltip" connectId="toolTip-${activeUser.userDetails.username}" position="below">
                                            <table>
                                                <tr>
                                                    <td align="left" valign="top">
                                                         ${activeUser.userDetails.firstName} ${activeUser.userDetails.lastName}
                                                    </td>
                                                </tr>
                                            </table>

                                        </div>
                                        <span>
                                            <div>
                                                <center>
                                                    <img  id="toolTip-${activeUser.userDetails.username}" src="/webapp/common/filestreamer.html?username=${activeUser.userDetails.username}&showIcon"/>
                                                    <br/>
                                                    ${activeUser.userDetails.firstName}
                                                </center>
                                            </div>
                                        </span>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </td>
                     </tr>
                </c:forEach>
        </table>
    </c:when>
    <c:when test="${fn:length(lasActivityList) == 0}">
        <spring:message code="NO_STUDENTS_CURRENTLY_LOGGED_ON"/>
    </c:when>
</c:choose>

