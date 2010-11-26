<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">

        <h1>Assigned students for ${pedagogicalPlan.name}</h1>

        <s:dialog url="selectStudentsForPedagogicalPlan.html" title="Select" dialogHeader="Select students" extraParameters="eloURI=${eloURI}"/>

        <c:choose>
            <c:when test="${fn:length(assignedPedagogicalPlans) > 0}">
                <table id="teachersTable" width="100%">
                    <tr>
                        <th></th>
                        <th></th>
                        <th>User name</th>
                        <th>First name</th>
                        <th>Last name</th>
                    </tr>
                    <c:forEach var="assignedPedagogicalPlan" items="${assignedPedagogicalPlans}">
                        <tr class="${oddEven.oddEven}">
                            <td>
                                <s:deleteLink href="viewStudentsForPedagogicalPlan.html?id=${pedagogicalPlan.id}&action=removeStudent&username=${assignedPedagogicalPlan.user.userDetails.username}" title="-" confirmText="Do you really want to remove ${assignedPedagogicalPlan.user.userDetails.username} from ${pedagogicalPlan.name}?" />
                            </td>
                            <td><img src="/webapp/common/filestreamer.html?username=${assignedPedagogicalPlan.user.userDetails.username}&showIcon"/>
                            </td>
                            <td>
                                ${assignedPedagogicalPlan.user.userDetails.username}
                            </td>
                            <td>${assignedPedagogicalPlan.user.userDetails.firstName} </td>
                            <td>${assignedPedagogicalPlan.user.userDetails.lastName}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
            </c:when>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertDefinition>