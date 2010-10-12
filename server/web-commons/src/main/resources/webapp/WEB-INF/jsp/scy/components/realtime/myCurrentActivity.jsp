<%@ taglib prefix="s" uri="http://scy.collide.info/taglibs" %>
<table>
    <tr>
        <td>ELO</td>
        <td>${currentELO}</td>
    </tr>
    <tr>
        <td>
            TOOL
        </td>
        <td>
            ${currentTool}
        </td>
    </tr>
</table>

<s:dialog url="/webapp/app/feedback/uploadELOForFeedbackForm.html?username=${user.userDetails.username}" title="Upload ELO" dialogHeader="Upload ELO"/> 
