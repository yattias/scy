<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Anchor ELO: ${model.humanReadableName}</h1>
        <table width="100%">
            <tr>
                <th>Anchor ELO portfolio properties</th>
                <th>Values</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    <strong>Name</strong>
                </td>
                <td>
                    <s:ajaxTextField model="${model}" property="humanReadableName"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Can be included in portfolio
                </td>
                <td>
                    <s:ajaxCheckBox model="${model}" property="includedInPortfolio"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Obligatory in portfolio
                </td>
                <td>
                    <s:ajaxCheckBox model="${model}" property="obligatoryInPortfolio"/>
                </td>
            </tr>


        </table>
        <br/>
        <br/>
        <h2>Assessment score</h2>
        <p>These scores will appear as headings over the feedback table that students will use to give peer feedback</p>
        <c:choose>
            <c:when test="${fn:length(assessment.assessmentScoreDefinitions) > 0}">
               <table id="scoreDefinitionTable" width="100%">
                   <tr>
                       <th>Heading</th>
                       <th>Score</th>
                   </tr>
                   <c:forEach var="assessmentScoreDefinition" items="${assessment.assessmentScoreDefinitions}">
                       <tr class="${oddEven.oddEven}">
                           <td>
                               <s:ajaxTextField model="${assessmentScoreDefinition}" property="heading"/>
                           </td>
                           <td>
                               <s:ajaxNumberField model="${assessmentScoreDefinition}" property="score"/>
                           </td>
                           <td>
                               <c:if test="${assessmentScoreDefinition.fileRef != null}">
                                    <img style="background-color:#cccccc;padding:15px;"src="/webapp/components/resourceservice.html?id=${assessmentScoreDefinition.fileRef.id}&showIcon=true"/>
                               </c:if>
                               <s:uploadFile listener="fileUploadedForAssessmentScoreDefinition" model="${assessmentScoreDefinition.class.name}_${assessmentScoreDefinition.id}"/>
                           </td>
                       </tr>

                   </c:forEach>
                </table>
            </c:when>
        </c:choose>
        <a href="viewAnchorELO.html?anchorELOId=${model.id}&action=addScoreDefinition">Add new Score Definition</a>



        <br/><br/>
        <h2>Assessment Criterias</h2>
        <p>These criterias will be listed in a table with the score definitions on top for students to give peer feedback</p>
        <c:choose>
           <c:when test="${fn:length(assessment.assessmentCriterias) > 0}">
               <table id="criteriaTable" width="100%">
                   <tr>
                       <th>Criteria</th>
                   </tr>
                   <c:forEach var="criteria" items="${assessment.assessmentCriterias}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <s:ajaxTextField model="${criteria}" property="criteria"/>
                            </td>

                        </tr>
                   </c:forEach>
                </table>
            </c:when>
        </c:choose>

        <a href="viewAnchorELO.html?anchorELOId=${model.id}&action=addCriteria">Add criteria</a>

    </tiles:putAttribute>
</tiles:insertDefinition>