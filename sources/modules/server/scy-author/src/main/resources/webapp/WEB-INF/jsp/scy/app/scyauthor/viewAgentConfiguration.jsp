<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Agents</h1>


        <fieldset>
            <legend>Votat Agent</legend>

            <table width="95%">
                <tr>
                    <th></th>
                    <th></th>
                    <c:choose>
                        <c:when test="${fn:length(anchorElos) > 0}">
                            <c:forEach var="anchorElo" items="${anchorElos}">
                                <th>
                                    ${anchorElo.elo.title}
                                </th>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </tr>
                <tr  class="${oddEven.oddEven}">
                    <td width="20%">
                        Sleep time
                    </td>
                    <td>
                        <s:agentParameterTextField agentId="votat" parameterName="sleepMillis" agentParameterAPI="${agentParameterAPI}" missionURI="${missionSpecificationTransporter.uri}"/>
                    </td>
                    <c:choose>
                        <c:when test="${fn:length(anchorElos) > 0}">
                            <c:forEach var="anchorElo" items="${anchorElos}">
                                <td>
                                    <s:agentParameterTextField agentId="votat" parameterName="sleepMillis" agentParameterAPI="${agentParameterAPI}" missionURI="${missionSpecificationTransporter.uri}" las="${anchorElo.elo.title}"/>
                                </td>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </tr>
                <tr  class="${oddEven.oddEven}">
                    <td>
                        Sleep time
                    </td>
                    <td>
                        <s:agentParameterTextField agentId="votat" parameterName="stopOnCrash" agentParameterAPI="${agentParameterAPI}" missionURI="${missionSpecificationTransporter.uri}"/>            
                    </td>
                </tr>
            </table>


        </fieldset>

    </tiles:putAttribute>
</tiles:insertDefinition>