<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>${model.name}</h1>

        <table width="100%">
            <tr>
                <td width="35%">Name</td>
                <td><s:ajaxTextField model="${model}" property="name"/></td>
            </tr>

        </table>

        <c:choose>
           <c:when test="${fn:length(model.agentProperties) > 0}">
               <table id="agentPropertyTable" width="100%">
                   <tr>
                       <th>Property</th>
                       <th>Values</th>
                   </tr>
                   <c:forEach var="property" items="${model.agentProperties}">
                       <tr class="${oddEven.oddEven}">
                           <td>
                                <s:ajaxTextField model="${property}" property="name"/>
                           </td>
                           <td>

                               <c:choose>
                                   <c:when test="${fn:length(property.agentPropertyValues) > 0}">
                                       <table>
                                           <c:forEach var="propertyValue" items="${property.agentPropertyValues}">
                                               <tr>
                                                    <td with="15%">
                                                       <strong>${propertyValue.agentPropertyLevel.name}</strong>
                                                   </td>
                                                   <td>
                                                        <s:ajaxTextField model="${propertyValue}" property="name"/>
                                                   </td>

                                               </tr>

                                           </c:forEach>
                                       </table>
                                   </c:when>
                               </c:choose>
                               <a href="viewAgent.html?id=${model.id}&property=${property.id}&action=addPropertyValue">+ Add Value</a>
                           </td>
                       </tr>
                   </c:forEach>
               </table>
               <br>
           </c:when>
       </c:choose>



        <a href="viewAgent.html?id=${model.id}&action=addProperty">+ Add Property</a>

        </tiles:putAttribute>
    </tiles:insertDefinition>