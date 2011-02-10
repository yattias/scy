<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Agents</h1>


        <fieldset>

             <c:choose>
                    <c:when test="${fn:length(agentConfigurationWrappers) > 0}">
                        <c:forEach var="agentConfigurationWrapper" items="${agentConfigurationWrappers}">
                            <div>
                                <h2>${agentConfigurationWrapper.name} (${agentConfigurationWrapper.status})</h2>

                                <c:choose>
                                    <c:when test="${fn:length(agentConfigurationWrapper.agentParameters) > 0}">
                                        <table>
                                            <tr>
                                                <th>
                                                    Parametere
                                                </th>
                                                <th>
                                                    Value
                                                </th>
                                            </tr>
                                        <c:forEach var="parameter" items="${agentConfigurationWrapper.agentParameters}">
                                            <tr  class="${oddEven.oddEven}">
                                                <td width="20%">
                                                    ${parameter.parameterName}
                                                </td>
                                                <td>
                                                    <s:agentParameterTextField agentId="${agentConfigurationWrapper.name}" parameterName="${parameter.parameterName}" agentParameterAPI="${agentParameterAPI}" missionURI="${missionSpecificationTransporter.uri}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </table>
                                    </c:when>
                                </c:choose>
                            </div>
                        </c:forEach>

                    </c:when>

            </c:choose>

        </fieldset>

    </tiles:putAttribute>
</tiles:insertDefinition>