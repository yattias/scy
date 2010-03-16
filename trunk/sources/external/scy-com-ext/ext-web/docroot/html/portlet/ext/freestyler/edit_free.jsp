<%@ include file="/html/portlet/ext/freestyler/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
FreestylerEntry entry = (FreestylerEntry)request.getAttribute("freestyler_entry");
long entryId = BeanParamUtil.getLong(entry, request, "freestylerId");

String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
String tagsEntries = ParamUtil.getString(renderRequest, "tagsEntries");


%>


<script type="text/javascript">
	function <portlet:namespace />saveEntry() {
		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= entry == null ? "extern" : Constants.UPDATE %>";
		submitForm(document.<portlet:namespace />fm);
	}
</script>

<form action="<portlet:actionURL windowState="<%= WindowState.MAXIMIZED.toString() %>"><portlet:param name="struts_action" value="/ext/freestyler/edit_free" /></portlet:actionURL>" method="post" name="<portlet:namespace />fm" onSubmit="<portlet:namespace />saveEntry(); return false;">
<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
<input name="<portlet:namespace />redirect" type="hidden" value="<%= HtmlUtil.escape(redirect) %>" />
<input name="<portlet:namespace />entryId" type="hidden" value="<%= entryId %>" />

<liferay-ui:tabs
	names="entry"
	backURL="<%= redirect %>"
/>


<liferay-ui:tags-error />


<table class="lfr-table">


<tr>
	<td>
		<liferay-ui:message key="name" />
	</td>
	<td>
		<liferay-ui:input-field model="<%= FreestylerEntry.class %>" bean="<%= entry %>" field="name" />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="description" />
	</td>
	<td>
		<textarea class="lfr-textarea" id="description" name="description" wrap="soft" onKeyDown="Liferay.Util.disableEsc();" onKeyPress="Liferay.Util.checkMaxLength(this, 4000);"><%= entry.getDescription() %></textarea>
	</td>
</tr>


<tr>
	<td colspan="2">
		<br />
	</td>
</tr>
<tr>
	<td>
		<liferay-ui:message key="tags" />
	</td>
	<td>

		<%
		long classPK = 0;

		if (entry != null) {
			classPK = entry.getFreestylerId();
		}
		%>

		<liferay-ui:tags-selector
			className="<%= FreestylerEntry.class.getName() %>"
			classPK="<%= classPK %>"
			hiddenInput="tagsEntries"
		/>
	</td>
</tr>
</table>

<br />

	<portlet:actionURL var="linkButton">
                    <portlet:param name="struts_action" value="/blogs/link_intern" />
                    <portlet:param name="entryId" value="<%= String.valueOf(entryId) %>" />
                  	<portlet:param name="redirect" value="<%= currentURL %>" />                  	
	</portlet:actionURL>

<input type="submit" value="<liferay-ui:message key="Save" />" />
		

<input type="button" value="<liferay-ui:message key="cancel" />" onClick="location.href = '<%= HtmlUtil.escape(redirect) %>';" />

</form>

<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
	<script type="text/javascript">
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />name);
	</script>
</c:if>