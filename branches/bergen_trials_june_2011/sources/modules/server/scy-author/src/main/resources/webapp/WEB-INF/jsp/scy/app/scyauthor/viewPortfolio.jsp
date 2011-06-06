<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Portfolio for pedagogical Plan: ${pedagogicalPlan.name}</h1>

        <table width="100%">
            <tr>
                <th width="35%">
                    Settings
                </th>
                <th width="65%">
                    Number
                </th>
            </tr>

            <tr class="${oddEven.oddEven}">
                <td>
                    Minimum number of AnchorELOs
                </td>
                <td>
                    <s:ajaxNumberField model="${pedagogicalPlan}" property="minimumNumberOfAnchorELOsInPortfolio"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Maximum number of AnchorELOs
                </td>
                <td>
                    <s:ajaxNumberField model="${pedagogicalPlan}" property="maximumNumberOfAnchorELOsInPortfolio"/>
                </td>
            </tr>
        </table>
        <br/>
        <br/>

        <h2>Anchor ELOs in portfolio</h2>
        <c:choose>
            <c:when test="${fn:length(anchorElos) > 0}">
                <table id="activityTable" width="100%">
                    <tr>
                        <th>Can be included</th>
                        <th>Obligatory</th>
                        <th>Anchor ELO</th>
                        <th>Assessment strategy</th>

                    </tr>
                    <c:forEach var="anchorElo" items="${anchorElos}">
                        <tr class="${oddEven.oddEven}">
                            <td><s:ajaxCheckBox model="${anchorElo}" property="includedInPortfolio"/></td>
                            <td><s:ajaxCheckBox model="${anchorElo}" property="obligatoryInPortfolio"/></td>
                            <td><a href="viewAnchorELO.html?anchorELOId=${anchorElo.id}">${anchorElo.name}</a></td>
                            <td>
                                <c:choose>
                                    <c:when test="${anchorElo.assessment != null}">
                                        <s:ajaxCombobox property="assessmentStrategyType" model="${anchorElo.assessment}" comboBoxValues="${assessmentStrategies}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="viewPortfolio.html?id=${pedagogicalPlan.id}&newAssessment=${anchorElo.id}">Create assessment</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>