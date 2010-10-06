<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <c:choose>
            <c:when test="${author}">
                <h1>Learning Activity Space</h1>
            <table>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <th width="35%">Learning Activity Space properties</th>
                                <th width="65%">Values</th>
                            </tr>
                            <tr class="${oddEven.oddEven}">
                                <td><strong>Name</strong></td>
                                <td><s:ajaxTextField property="name" model="${model}"/></td>
                            </tr>
                            <tr class="${oddEven.oddEven}">
                                <td><strong>Description</strong></td>
                                <td><s:ajaxTextField property="description" model="${model}" isMultiLine="true"/></td>
                            </tr>
                            <tr class="${oddEven.oddEven}">
                                <td><strong>Assessment</strong></td>
                                <td><s:ajaxTextField property="name" model="${model.assessment}"/></td>
                            </tr>
                            <tr class="${oddEven.oddEven}">
                                <td><strong>Assessment strategy</strong></td>
                                <td><s:ajaxCombobox property="assessmentStrategyType" model="${model.assessment}" comboBoxValues="${assessmentStrategies}"/></td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <a href="/webapp/components/resourceservice.html?id=${model.image.id}" target="_blank">
                            <img src="/webapp/components/resourceservice.html?id=${model.image.id}&showIcon=true"/>
                        </a>
                        <s:uploadFile listener="addImageToLearningActivitySpaceListener" model="eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl_${model.id}"/>
                    </td>
                </tr>
            </table>

            <br/>

            <table width="100%">
                <tr>
                    <th width="35%">Diagram properties</th>
                    <th>Values</th>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td><strong>XPos</strong></td>
                    <td><s:ajaxNumberField model="${model}" property="xPos"/></td>
                </tr>
                <tr class="${oddEven.oddEven}">
                    <td><strong>YPos</strong></td>
                    <td><s:ajaxNumberField model="${model}" property="yPos"/></td>
                </tr>

            </table>

            <div>
                <c:choose>
                    <c:when test="${fn:length(model.activities) > 0}">
                        <table id="activityTable" width="100%">
                            <h2>Activities</h2>
                            <tr>
                                <th>Activity</th>
                                <th>ELO</th>
                                <th>Tools</th>
                                <th>Auto add to student plan</th>
                            </tr>
                            <c:forEach var="activity" items="${model.activities}">
                                <tr class="${oddEven.oddEven}">
                                    <td><a href="viewActivity.html?activityId=${activity.id}">${activity.name}</a></td>
                                    <td>${activity.anchorELO.name}</td>
                                    <td>
                                        <s:dialog url="selectToolForActivity.html?id=${activity.id}&clazz=${activity.class.name}" dialogHeader="Tools" title="+"/>
                                    </td>
                                    <td><s:ajaxCheckBox model="${activity}" property="autoaddToStudentPlan"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                        <br>
                    </c:when>
                </c:choose>
            </div>
        </c:when>
        <c:otherwise>
            <h1>Learning Activity Space</h1>
            <div>
                ${model.description}
            </div>
            <a href="/webapp/components/resourceservice.html?id=${model.image.id}" target="_blank">
                <img src="/webapp/components/resourceservice.html?id=${model.image.id}" width="40%"/>
            </a>
        </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>
