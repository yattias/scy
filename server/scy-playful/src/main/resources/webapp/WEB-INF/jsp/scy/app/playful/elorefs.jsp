<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="extrahead">
		<script type="text/javascript">
			dojo.require("dijit.layout.ContentPane");
		</script>
	</tiles:putAttribute>
	<tiles:putAttribute name="main">
		<c:choose>
			<c:when test="${fn:length(elorefs) > 0}">
				<h5>Assessable ELOs</h5>
				<table border="2">
					<tr>
						<th></th>
						<th>Title</th>
						<th>User</th>
						<th>Score</th>
					</tr>
					<c:forEach var="elo" items="${elorefs}">
						<tr>
							<td></td>
							<td><a href="assesselo.html?id=${elo.id}">${elo.title}</a></td>
							<td>${elo.ELOURI} </td>
							<td></td>
						</tr>
					</c:forEach>
				</table>
				<br>
			</c:when>
		</c:choose>
	</tiles:putAttribute>
</tiles:insertDefinition>