<%@ include file="/html/portlet/ext/freestyler/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");

String uploadProgressId = "igImageUploadProgress";

IGImage image = (IGImage)request.getAttribute(WebKeys.IMAGE_GALLERY_IMAGE);

long imageId = BeanParamUtil.getLong(image, request, "imageId");

FreestylerEntry entry = (FreestylerEntry)request.getAttribute("freestyler_entry");

long entryId = BeanParamUtil.getLong(entry, request, "freestylerId");

long folderId = BeanParamUtil.getLong(image, request, "folderId");

String name = BeanParamUtil.getString(image, request, "name");

String extension = StringPool.BLANK;

if (image != null) {
	extension = StringPool.PERIOD + image.getImageType();
}

String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
String tagsEntries = ParamUtil.getString(renderRequest, "tagsEntries");

IGFolder folder = null;
Image largeImage = null;

if (image != null) {
	folder = image.getFolder();
	largeImage = ImageLocalServiceUtil.getImage(image.getLargeImageId());
}
%>

<div align="right">
	&laquo; <a href="<%= HtmlUtil.escape(redirect) %>"<%= onClickHtml %>><liferay-ui:message key="back" /></a>
</div>
	

<script type="text/javascript">
	function <portlet:namespace />saveImage() {
		<%= HtmlUtil.escape(uploadProgressId) %>.startProgress();

		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = "<%= image == null ? Constants.ADD : Constants.UPDATE %>";
		submitForm(document.<portlet:namespace />fm);
	}

	function <portlet:namespace />selectFolder(folderId, folderName) {
		document.<portlet:namespace />fm.<portlet:namespace />folderId.value = folderId;

		var nameEl = document.getElementById("<portlet:namespace />folderName");

		nameEl.href = "<portlet:renderURL windowState="<%= WindowState.MAXIMIZED.toString() %>"><portlet:param name="struts_action" value="/ext/freestyler/view_freestyler" /></portlet:renderURL>&<portlet:namespace />folderId=" + folderId;
		nameEl.innerHTML = folderName + "&nbsp;";
	}
</script>

<form action="<portlet:actionURL><portlet:param name="struts_action" value="/ext/freestyler/add_free" /></portlet:actionURL>" enctype="multipart/form-data" method="post" name="<portlet:namespace />fm" onSubmit="<portlet:namespace />saveImage(); return false;">
<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="" />
<input name="<portlet:namespace />redirect" type="hidden" value="<%= HtmlUtil.escape(redirect) %>" />
<input name="<portlet:namespace />referringPortletResource" type="hidden" value="<%= HtmlUtil.escape(referringPortletResource) %>" />
<input name="<portlet:namespace />uploadProgressId" type="hidden" value="<%= HtmlUtil.escape(uploadProgressId) %>" />
<input name="<portlet:namespace />imageId" type="hidden" value="<%= imageId %>" />
<input name="<portlet:namespace />folderId" type="hidden" value="<%= folderId %>" />

<liferay-ui:error exception="<%= DuplicateImageNameException.class %>" message="please-enter-a-unique-image-name" />

<liferay-ui:error exception="<%= ImageNameException.class %>">
	<liferay-ui:message key="image-names-must-end-with-one-of-the-following-extensions" /> <%= StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.FREESTYLER_EXTENSIONS, StringPool.COMMA), StringPool.COMMA_AND_SPACE) %>.
</liferay-ui:error>

<liferay-ui:error exception="<%= ImageSizeException.class %>" message="please-enter-a-file-with-a-valid-file-size" />
<liferay-ui:error exception="<%= NoSuchFolderException.class %>" message="please-enter-a-valid-folder" />

<liferay-ui:tags-error />

<%
String imageMaxSize = String.valueOf(PrefsPropsUtil.getLong(PropsKeys.IG_IMAGE_MAX_SIZE) / 1024);
%>

<c:if test='<%= !imageMaxSize.equals("0") %>'>
	<%= LanguageUtil.format(pageContext, "upload-images-no-larger-than-x-k", imageMaxSize, false) %>

	<br /><br />
</c:if>

<table class="lfr-table">


<tr>
	<td class="lfr-label">
		<liferay-ui:message key="file" />
	</td>
	<td>
		<input class="lfr-input-text" id="<portlet:namespace />file" name="<portlet:namespace />file" type="file" />
	</td>
</tr>
<tr>
	<td class="lfr-label">
		<liferay-ui:message key="name" />
	</td>
	<td>
		<liferay-ui:input-field model="<%= IGImage.class %>" bean="<%= image %>" field="name" /><%= extension %>
	</td>
</tr>
<tr>
	<td class="lfr-label">
		<liferay-ui:message key="description" />
	</td>
	<td>
		<liferay-ui:input-field model="<%= IGImage.class %>" bean="<%= image %>" field="description" />
	</td>
</tr>
<tr>
	<td colspan="2">
		<br />
	</td>
</tr>
<tr>
	<td class="lfr-label">
		<liferay-ui:message key="tags" />
	</td>
	<td>

		<%
		long classPK = 0;

		if (image != null) {
			classPK = image.getImageId();
		}
		%>

		<liferay-ui:tags-selector
			className="<%= IGImage.class.getName() %>"
			classPK="<%= classPK %>"
			hiddenInput="tagsEntries"
		/>
	</td>
</tr>

<c:if test="<%= image == null %>">
	<tr>
		<td colspan="2">
			<br />
		</td>
	</tr>
	<tr>
		<td class="lfr-label">
			<liferay-ui:message key="permissions" />
		</td>
		<td>
			<liferay-ui:input-permissions
				modelName="<%= IGImage.class.getName() %>"
			/>
		</td>
	</tr>
</c:if>

</table>

<br />

<input type="submit" value="<liferay-ui:message key="save" />" />

<input type="button" value="<liferay-ui:message key="cancel" />" onClick="window.location = '<%= HtmlUtil.escape(redirect) %>';" />

</form>

<script type="text/javascript">
	<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />file);
	</c:if>

	jQuery(
		function() {
			jQuery("#<portlet:namespace />file").change(
				function() {
					var value = jQuery(this).val();

					if ((value != null) && (value != "")) {
						var extension = value.substring(value.lastIndexOf(".")).toLowerCase();

						var validExtensions = new Array('<%= StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.FREESTYLER_EXTENSIONS, StringPool.COMMA), "', '") %>');

						if (jQuery.inArray(extension, validExtensions) == -1) {
							alert('<%= UnicodeLanguageUtil.get(pageContext, "image-names-must-end-with-one-of-the-following-extensions") %> <%= StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.FREESTYLER_EXTENSIONS, StringPool.COMMA), StringPool.COMMA_AND_SPACE) %>');

							jQuery(this).val("");
						}
					}
				}
			).change();
		}
	);
</script>

<liferay-ui:upload-progress
	id="<%= uploadProgressId %>"
	message="uploading"
	redirect="<%= redirect %>"
/>

<c:if test="<%= image == null %>">
	</div>
</c:if>


