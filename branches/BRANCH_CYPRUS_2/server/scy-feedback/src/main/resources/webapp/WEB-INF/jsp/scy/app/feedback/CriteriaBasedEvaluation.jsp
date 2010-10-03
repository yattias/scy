<%@ include file="common-taglibs.jsp" %>

<form method="POST" accept-charset="UTF-8" action="storeCriteriaBasedEvaluation.html">
    <table>
        <tr>
            <td colspan="2" align="left">
                Criteria:(you can alter the text in the field below)
            </td>
        </tr>
        <tr>
            <td colspan="2" align="left">
                <textarea rows="3" cols="50" name="criteriaText">${assessmentCriteria.criteria}</textarea>
                <input type="hidden" value="${anchorELO.id}" name="modelId">
                <input type="hidden" value="${eloRef.id}" name="eloRefId">
                <input type="hidden" value="${assessmentCriteria.id}" name="evaluationCriteriaId">
                <input type="hidden" value="${assessment.id}" name="assessmentId"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <table>
                    <c:choose>
                        <c:when test="${fn:length(assessmentCriteria.assessment.assessmentScoreDefinitions) > 0}">
                            <tr>
                                <c:forEach var="assessmentScoreDefinition" items="${assessmentCriteria.assessment.assessmentScoreDefinitions}">
                                    <td>
                                        <strong>${assessmentScoreDefinition.heading}</strong>  <br/>
                                        <input type="radio" name="score" value="${assessmentScoreDefinition.score}">
                                        <c:if test="${assessmentScoreDefinition.fileRef != null}">
                                            <img src="/webapp/components/resourceservice.html?id=${assessmentScoreDefinition.fileRef.id}&showIcon=true"/>
                                       </c:if>

                                    </td>
                                </c:forEach>
                                </tr>
                        </c:when>
                    </c:choose>
                </table>
            </td>
        </tr>
        <tr>
            <td align="left" colspan="2">
                Comments/Proposed changes:
            </td>
        </tr>

        <tr>
            <td align="left" colspan="2">
                <textarea rows="3" cols="50" name="comment"></textarea>
            </td>


        </tr>
        <tr>
            <td colspan="2" align="right">
                <input type="submit" title="Send">
            </td>


        </tr>

    </table>
</form>