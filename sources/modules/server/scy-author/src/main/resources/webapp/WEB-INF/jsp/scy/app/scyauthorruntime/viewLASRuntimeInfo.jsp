<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <c:if test="${showUserInfo}">
            <h1><img src="/webapp/common/filestreamer.html?username=${userdetails.username}&showIcon"/>Activity overview ${userdetails.firstname}&nbsp;${userdetails.lastname}</h1>

            <s:currentStudentActivity username="${userdetails.username}"/>

            <table>
                <tr>
                    <th colspan="2">Current activity details</th>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>
                        Current ELO
                    </td>
                    <td>
                        ${currentELO}
                    </td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td>
                        Current tool
                    </td>
                    <td>
                        ${currentTool}
                    </td>
                </tr>
            </table>
            <br/>
            <br/>
            <br/>
            <c:choose>
            <c:when test="${fn:length(lastElos) > 0}">
                <table id="activityTable" width="100%">
                    <tr>
                        <th>Time</th>
                        <th>Last ELOs used</th>
                    </tr>
                    <c:forEach var="elo" items="${lastElos}">
                        <tr  class="${oddEven.oddEven}">
                            <td>
                                ${elo.date}
                            </td>
                            <td>
                                ${elo.eloUri}
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>

        </c:if>
        <c:if test="${!showUserInfo}">
            LAS INFO
        </c:if>


    </tiles:putAttribute>
</tiles:insertDefinition>