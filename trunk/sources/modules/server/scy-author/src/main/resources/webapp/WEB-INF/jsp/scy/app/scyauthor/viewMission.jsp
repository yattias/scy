<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Mission: ${model.name}</h1>
        <table>
            <tr>
                <th width="35%">Mission properties</th>
                <th width="65%">Values</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Name
                </td>
                <td class="tablerow-odd">
                   <s:ajaxTextField property="name" model="${model}"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>Target group</th>
                <td class="tablerow-odd"><s:ajaxTextField property="targetGroup" model="${model}"/></td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>Mission outline</th>
                <td class="tablerow-odd"><s:ajaxTextField property="missionOutline" model="${model}" isMultiLine="true"/></td>
            </tr>
        </table>

        <br/><br/>


        <h2>Learning goals</h2>
        <c:choose>
            <c:when test="${fn:length(learningGoals) > 0}">
                <table id="learningGoalsTable" width="100%">
                    <tr>
                        <th width="35%">Name</th>
                        <th width="65%">Description</th>

                    </tr>
                    <c:forEach var="lg" items="${learningGoals}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <s:ajaxTextField property="name" model="${lg}"/>
                            </td>
                            <td>
                                <s:ajaxTextField property="description" model="${lg}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                0 learning goals found
            </c:otherwise>
        </c:choose>

        <td><a href="viewMission.html?missionId=${model.id}&newObject=${'NEW_LEARNING_GOAL'}">+</a></td>
        <br/>

        <h2>Learning materials</h2>
        <c:choose>
            <c:when test="${fn:length(learningMaterials) > 0}">
                <table id="learningMaterialsTable" width="100%">
                    <tr>
                        <th></th>
                        <th>Name</th>
                        <th></th>

                    </tr>
                    <c:forEach var="lm" items="${learningMaterials}">
                        <tr class="${oddEven.oddEven}">
                            <td><img src="${lm.icon}"/></td>
                            <td><a href="http://${lm.url}" target="_blank">${lm.name}</a></td>
                            <td>
                                <s:deleteLink href="viewMission.html?action=deleteLearningMaterial&lmid=${lm.id}&missionId=${model.id}" confirmText="Do you really want to delete ${lm.name}?" title="Delete"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                0 learning materials found
            </c:otherwise>
        </c:choose>

        <s:dialog url="addURLToMission.html?model=${model.id}" dialogHeader="Add URL" title="+"/>

        <s:dialog url="addFileToMission.html?model=${model.id}&action=addImage" dialogHeader="Add file" title="Add file"/>

        <br/><br/>



    </tiles:putAttribute>
</tiles:insertDefinition>