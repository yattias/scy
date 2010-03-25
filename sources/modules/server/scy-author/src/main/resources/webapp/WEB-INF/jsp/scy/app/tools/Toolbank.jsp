<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <h1>Tools</h1>

        <c:choose>
           <c:when test="${fn:length(learningActivitySpaces) > 0}">
               <table id="toolTable" border="2" width="100%">
                   <tr>
                       <th>Tool</th>
                   </tr>
                   <c:forEach var="tool" items="${tools}">
                       <tr class="${oddEven.oddEven}">
                           <td>${tool.name}</td>
                       </tr>
                   </c:forEach>
               </table>
               <br>
           </c:when>
       </c:choose>

        <a href="Toolbank.html?addTool">Add Tool</a>


    </tiles:putAttribute>
</tiles:insertDefinition>