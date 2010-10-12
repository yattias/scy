<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Agent configuration</h1>

        <c:choose>
           <c:when test="${fn:length(agents) > 0}">
               <table id="agentTable" width="100%">
                   <tr>
                       <th>Agent</th>
                   </tr>
                   <c:forEach var="agent" items="${agents}">
                       <tr class="${oddEven.oddEven}">
                           <td>
                                <s:modellink href="viewAgent.html" model="${agent}">${agent.name}</s:modellink>
                           </td>
                       </tr>
                   </c:forEach>
               </table>
               <br>
           </c:when>
       </c:choose>


        <c:choose>
           <c:when test="${fn:length(agentPropertyValueLevels) > 0}">
               <table id="agentPropertyValueLevelsTable" width="100%">
                   <tr>
                       <th width="15%">Level</th>
                       <th width="15%">Level index</th>
                       <th>Description</th>
                   </tr>
                   <c:forEach var="agentPropertyValueLevel" items="${agentPropertyValueLevels}">
                       <tr class="${oddEven.oddEven}">
                           <td>
                                <s:ajaxTextField model="${agentPropertyValueLevel}" property="name"/>
                           </td>
                           <td>
                                <s:ajaxNumberField model="${agentPropertyValueLevel}" property="levelIndex"/>
                           </td>
                           <td>
                               <s:ajaxTextField model="${agentPropertyValueLevel}" property="description" isMultiLine="true"></s:ajaxTextField>
                           </td>
                       </tr>
                   </c:forEach>
               </table>
               <br>
           </c:when>
       </c:choose>

        <a href="agentBank.html?action=addAgentPropertyValueLevel">Add Agent Level</a>


        </tiles:putAttribute>
    </tiles:insertDefinition>