<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <h1>Tools</h1>

        <c:choose>
           <c:when test="${fn:length(tools) > 0}">
               <table id="toolTable" width="100%">
                   <tr>
                       <th>Tool</th>
                       <th>Tool id</th>
                   </tr>
                   <c:forEach var="tool" items="${tools}">
                       <tr class="${oddEven.oddEven}">
                           <td>
                                <s:modellink href="viewTool.html" model="${tool}">${tool.name}</s:modellink>    
                           </td>
                           <td>${tool.toolId}</td>
                       </tr>
                   </c:forEach>
               </table>
               <br>
           </c:when>
       </c:choose>

        <a href="Toolbank.html?addTool">Add Tool</a>


    </tiles:putAttribute>
</tiles:insertDefinition>