<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h1>Activity: ${model.name}</h1>

        <table width="100%" border="2">
            <tr class="tablerow-odd">
                <td>Work arrangement</td>
                <td><s:ajaxCombobox property="workArrangementType" model="${model}" comboBoxValues="${workArrangement}"/></td>
            </tr>


            <tr class="tablerow-even">
                <td>Teacher role</td>
                <td><s:ajaxCombobox property="teacherRoleType" model="${model}" comboBoxValues="${teacherRoles}"/></td>
            </tr>


            <tr class="tablerow-odd">
                <td>
                    Expected duration in minutes
                </td>
                <td>
                    <input type="text">
                </td>

            </tr>
            <tr class="tablerow-even">
                <td>
                    Anchor ELO
                </td>
                <td>
                    <a href="viewAnchorELO.html?anchorELOId=${model.anchorELO.id}">${model.anchorELO.name}</a>
                </td>
            </tr>
        </table>

    </tiles:putAttribute>
</tiles:insertDefinition>
