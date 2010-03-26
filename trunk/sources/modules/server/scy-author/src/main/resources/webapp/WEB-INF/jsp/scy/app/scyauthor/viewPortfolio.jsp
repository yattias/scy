<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Portfolio for pedagogical Plan: ${pedagogicalPlan.name}</h1>

        <table border="2" width="100%">
            <tr>
                <td>
                    Minimum number of AnchorELOs
                </td>
                <td>
                    <input type="text">
                </td>
            </tr>
            <tr>
                <td>
                    Maximum number of AnchorELOs
                </td>
                <td>
                    <input type="text">
                </td>
            </tr>
        </table>
        <br/>
        <br/>

        <h2>Anchor ELOs in portfolio</h2>
        <c:choose>
            <c:when test="${fn:length(anchorElos) > 0}">
                <table id="activityTable" border="2" width="100%">
                    <tr>
                        <th>Can be included</th>
                        <th>Obligatory</th>
                        <th>Anchor ELO</th>
                        <th>Assessment strategy</th>

                    </tr>
                    <c:forEach var="anchorElo" items="${anchorElos}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <input id="mycheck-${anchorElo.id}" name="mycheck" dojoType="dijit.form.CheckBox"
                                       value="agreed" checked onChange="alert('hei');">
                            </td>
                            <td><input type="checkbox"></td>
                            <td><a href="viewAnchorELO.html?anchorELOId=${anchorElo.id}">${anchorElo.name}</a></td>
                            <td>
                                <s:ajaxCombobox property="name" model="${anchorElo}"
                                                comboBoxValues="${assessmentStrategies}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>