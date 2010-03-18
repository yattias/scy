<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <h1>Activity: ${model.name}</h1>

        <table width="100%" border="2">
            <tr>
                <td rowspan="3">Work arrangement</td>
                <td><input type="radio">Individual</td>
            </tr>
            <tr>
                <td><input type="radio">Group</td>
            </tr>
            <tr>
                <td><input type="radio">Peer to peer</td>
            </tr>

            </tr>
            <tr>
                <td rowspan="3">Teacher role</td>
                <td><input type="radio">Activator</td>
            </tr>
            <tr>
                <td><input type="radio">Facilitator</td>
            </tr>
            <tr>
                <td><input type="radio">Observer</td>
            </tr>

            </tr>
            <tr>
                <td>
                    Expected duration in minutes
                </td>
                <td>
                    <input type="text">
                </td>

            </tr>
            <tr>
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
