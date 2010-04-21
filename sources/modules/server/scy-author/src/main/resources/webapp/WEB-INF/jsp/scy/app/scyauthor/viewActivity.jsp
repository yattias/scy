<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h1>Activity: ${model.name}</h1>

        <table width="100%">
            <tr>
                <th>Activity properties</th>
                <th>Values</th>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>Work arrangement</td>
                <td><s:ajaxCombobox property="workArrangementType" model="${model}" comboBoxValues="${workArrangement}"/></td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>Teacher role</td>
                <td><s:ajaxCombobox property="teacherRoleType" model="${model}" comboBoxValues="${teacherRoles}"/></td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Expected duration in minutes
                </td>
                <td>
                    <s:ajaxNumberField model="${model}" property="expectedDurationInMinutes"/>                    
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Anchor ELO
                </td>
                <td>
                    <a href="viewAnchorELO.html?anchorELOId=${model.anchorELO.id}">${model.anchorELO.name}</a>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Auto add to student plan
                </td>
                <td>
                    <s:ajaxCheckBox model="${model}" property="autoaddToStudentPlan"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Start date
                </td>
                <td>
                    <s:ajaxDatePicker model="${model}" property="startDate"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    End Date
                </td>
                <td>
                    <s:ajaxDatePicker model="${model}" property="endDate"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    Start time
                </td>
                <td>
                    <s:ajaxTimePicker model="${model}" property="startTime"/>
                </td>
            </tr>
            <tr class="${oddEven.oddEven}">
                <td>
                    End time
                </td>
                <td>
                    <s:ajaxTimePicker model="${model}" property="endTime"/>
                </td>
            </tr>
        </table>

    </tiles:putAttribute>
</tiles:insertDefinition>
