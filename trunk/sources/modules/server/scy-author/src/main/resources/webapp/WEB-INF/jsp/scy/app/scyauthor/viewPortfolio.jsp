<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Portfolio for pedagogical Plan: ${pedagogicalPlan.name}</h1>

        <table width="100%">
            <tr>
                <th>
                    Settings
                </th>
                <th>
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
                            <td><s:ajaxCombobox property="name" model="${anchorElo}" comboBoxValues="${assessmentStrategies}"/></td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>