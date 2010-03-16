<%@ include file="/html/portlet/ext/freestyler/init.jsp" %>

<script type="text/javascript">
	var maxDimension = <%= PrefsPropsUtil.getInteger(PropsKeys.IG_IMAGE_THUMBNAIL_MAX_DIMENSION) %>;

	function <portlet:namespace />viewImage(id, token, name, width, height) {
		var page = Liferay.Util.viewport.page();

		var maxWidth = Math.max(page.x - 150, maxDimension);
		var maxHeight = Math.max(page.y - 150, maxDimension);

		var imgWidth = width;
		var imgHeight = height;

		if (imgWidth > maxWidth || imgHeight > maxHeight) {
			if (imgWidth > maxWidth) {
				var x = maxWidth / imgWidth;

				imgWidth = maxWidth;
				imgHeight = x * imgHeight;
			}

			if (imgHeight > maxHeight) {
				var y = maxHeight / imgHeight;

				imgHeight = maxHeight;
				imgWidth = y * imgWidth;
			}
		}

		var winWidth = imgWidth + 36;

		if (winWidth < maxDimension) {
			winWidth = maxDimension;
		}

		var html =
			"<div style='text-align: center;'><img alt='<liferay-ui:message key="image" />' src='<%= themeDisplay.getPathImage() %>/image_gallery?img_id=" + id + "&t=" + token + "' style='height: " + imgHeight + "px; width" + imgWidth + "px;' /></div>" +
			"<div style='text-align: center;'>" + name + "</div>";

		var messageId = "<portlet:namespace />popup_" + id;
		var buttonsId = "<portlet:namespace />buttons_" + id;

		var popup = Liferay.Popup(
			{
				width: winWidth,
				modal: true,
				noDraggable: true,
				noTitleBar: true,
				message: html,
				messageId: messageId,
				onClose: function() {
					var buttons = jQuery("#<portlet:namespace />buttons_" + id);

					jQuery("#<portlet:namespace />buttonsContainer_" + id).append(buttons);
				}
			}
		);

		jQuery(popup).addClass('image-popup');

		var buttons = jQuery("#" + buttonsId);

		jQuery("#" + messageId).append(buttons);
	}
</script>


<%
String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	redirect = PortalUtil.getLayoutURL(layout, themeDisplay) + Portal.FRIENDLY_URL_SEPARATOR + "freestyler";
}

FreestylerEntry entry = (FreestylerEntry)request.getAttribute("freestyler_entry");
int actualImageEntry = (Integer)request.getAttribute("actualImageEntry");
List<FreestylerImage> listFreeImages = (List<FreestylerImage>)request.getAttribute("listFreeImages");
long entryId = BeanParamUtil.getLong(entry, request, "freestylerId");
boolean windowStateMax = ((Boolean)request.getAttribute("windowStateMax"));

FreestylerImage actualImage = listFreeImages.get(actualImageEntry);
int imageCount = listFreeImages.size();

for(FreestylerImage fi: listFreeImages){
	TagsAssetLocalServiceUtil.incrementViewCounter(FreestylerImage.class.getName(), fi.getImageId());
}

TagsAssetLocalServiceUtil.incrementViewCounter(FreestylerEntry.class.getName(), entry.getFreestylerId());
TagsUtil.addLayoutTagsEntries(request, TagsEntryLocalServiceUtil.getEntries(FreestylerEntry.class.getName(), entry.getFreestylerId(), true));
String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
%>

<div align="right">
	&laquo; <a href="<%= HtmlUtil.escape(redirect) %>"<%= onClickHtml %>><liferay-ui:message key="back" /></a>
</div>

<div class="entry-navigation">
	<c:choose>
		<c:when test="<%= actualImageEntry > 0 %>">
			<a class="previous" href="<portlet:renderURL>
										<portlet:param name="struts_action" value="/ext/freestyler/view_entry" />
										<portlet:param name="freestylerEntryId" value="<%= String.valueOf(entryId) %>" />
										<portlet:param name="actualImageEntry" value="<%= String.valueOf(actualImageEntry) %>" />
										<portlet:param name="nav" value="prev" />
									</portlet:renderURL>">
		</c:when>
		<c:otherwise>
			<span class="previous">
		</c:otherwise>
	</c:choose>

	<liferay-ui:message key="previous" />

	<c:choose>
		<c:when test="<%= actualImageEntry > 0 %>">
			</a>
		</c:when>
		<c:otherwise>
			</span>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="<%= actualImageEntry < (imageCount-1) %>">
			<a class="next" href="<portlet:renderURL>
									<portlet:param name="struts_action" value="/ext/freestyler/view_entry" />
									<portlet:param name="freestylerEntryId" value="<%= String.valueOf(entryId) %>" />
									<portlet:param name="actualImageEntry" value="<%= String.valueOf(actualImageEntry) %>" />
									<portlet:param name="nav" value="next" />
								</portlet:renderURL>">
		</c:when>
		<c:otherwise>
			<span class="next">
		</c:otherwise>
	</c:choose>

	<liferay-ui:message key="next" />

	<c:choose>
		<c:when test="<%= actualImageEntry < (imageCount-1) %>">
			</a>
		</c:when>
		<c:otherwise>
			</span>
		</c:otherwise>
	</c:choose>
</div>

<br/>
	
<div class="entry">			
	<div class="entry-title">
		<%= entry.getName() %> 
		<br/>
		<%= actualImage.getName() %>
	</div>
	<div class="entry-date">
		<%
		DateFormat entryDisplayDateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, locale);
		%>
		<%= entryDisplayDateFormat.format(entry.getModifiedDate()) %>
	</div>
</div>





<div class="entry-body">
	<h4>Freestyler Eintrag: <%= String.valueOf(actualImageEntry+1) %> von <%= String.valueOf(imageCount) %></h4>
</div>

<c:if test="<%= FreestylerEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE) || FreestylerEntryPermission.contains(permissionChecker, entry, ActionKeys.PERMISSIONS) %>">
			<div class="edit-actions entry">
				<table class="lfr-table">
				<tr>
					<c:if test="<%= FreestylerEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE) %>">
						<td>
							<portlet:renderURL var="editEntryURL">
								<portlet:param name="struts_action" value="/ext/freestyler/edit_entry" />
								<portlet:param name="redirect" value="<%= currentURL %>" />
								<portlet:param name="entryId" value="<%= String.valueOf(actualImage.getImageId()) %>" />
							</portlet:renderURL>

							<liferay-ui:icon image="edit" url="<%= editEntryURL %>" label="<%= true %>" />
						</td>
					</c:if>				
				</tr>
				</table>
			</div>
</c:if>




<%
			FreestylerImage image = actualImage;
			Image smallImage = ImageLocalServiceUtil.getImage(actualImage.getSmallImageId());	
			Image largeImage = ImageLocalServiceUtil.getImage(actualImage.getLargeImageId());
				
			long smallImageId = 0;
			int smallImageHeight = 200;
			int smallImageWidth = 200;
			
			long largeImageId = 0;
			int largeImageHeight = 500;
			int largeImageWidth = 500;

			if (smallImage != null) {
					smallImageId = smallImage.getImageId();
					smallImageHeight = smallImage.getHeight();
					smallImageWidth = smallImage.getWidth();
			}
				
			if (largeImage != null) {
					largeImageId = largeImage.getImageId();
					largeImageHeight = largeImage.getHeight();
					largeImageWidth = largeImage.getWidth();
			}
				
			DLFileEntry fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(entry.getXmlFileId());
					
			String fileExtension = DLUtil.getFileExtension(fileEntry.getName());
			if(fileExtension.length() > 3){
				fileExtension = fileExtension.subSequence(0, 3).toString();
			}
						
		%>
		<div>		
			<c:if test="<%= !windowStateMax %>" >	
				<a href="javascript: ;" onClick="<portlet:namespace />viewImage(<%= largeImage.getImageId() %>, '<%= ImageServletTokenUtil.getToken(largeImage.getImageId()) %>', '<b><%= UnicodeFormatter.toString(image.getName()) %></b><br /><%= UnicodeFormatter.toString(image.getDescription()) %>', <%= largeImage.getWidth() %>, <%= largeImage.getHeight() %>)">
					<img alt="<liferay-ui:message key="image" />" border="no" src="<%= themeDisplay.getPathImage() %>/image_gallery?img_id=<%= smallImageId %>&t=<%= ImageServletTokenUtil.getToken(smallImageId) %>" style="height: <%= smallImageHeight %>; width: <%= smallImageWidth %>;" />
				</a>
			</c:if>
			<c:if test="<%= windowStateMax %>" >	
				<img alt="<liferay-ui:message key="image" />" border="no" src="<%= themeDisplay.getPathImage() %>/image_gallery?img_id=<%= largeImageId %>&t=<%= ImageServletTokenUtil.getToken(largeImageId) %>" style="height: <%= largeImageHeight %>; width: <%= largeImageWidth %>;" />
			</c:if>
		</div>
		<div>
			
			<a href="<%= themeDisplay.getPathMain() %>/document_library/get_file?p_l_id=<%= themeDisplay.getPlid() %>&folderId=<%= fileEntry.getFolderId() %>&name=<%= HttpUtil.encodeURL(fileEntry.getName()) %>">
				Download XML-Dokument:  <img border="0" class="dl-file-icon" src="<%= themeDisplay.getPathThemeImages() %>/document_library/<%= fileExtension %>.png" /><%= fileEntry.getTitle() %>
			</a>
		</div>
	

<div class="entry-footer">
				<div class="entry-tags">
					<liferay-ui:tags-summary
						className="<%= FreestylerImage.class.getName() %>"
						classPK="<%= actualImage.getImageId() %>"
					/>
				
				<div class="entry-tags">
					<liferay-ui-ext:asset-owntags-summary
						className="<%= FreestylerImage.class.getName() %>"
						classPK="<%= actualImage.getImageId() %>"
						portletURL="<%= renderResponse.createRenderURL() %>"
						strutsAction="/ext/freestyler/delete_tag"
					/>
				</div>

				
				<liferay-ui-ext:add-own-tags
					className="<%= FreestylerImage.class.getName() %>"
					classPK="<%= actualImage.getImageId() %>"
					portletURL="<%= renderResponse.createRenderURL() %>"
					strutsAction="/ext/freestyler/add_tag"
				/>		
				
				<liferay-ui-ext:add-own-meta
					className="<%= FreestylerImage.class.getName() %>"
					classPK="<%= actualImage.getImageId() %>"
					portletURL="<%= renderResponse.createRenderURL() %>"
					strutsAction="/ext/freestyler/add_meta"
				/>
			
		
				<liferay-ui-ext:add-to-cart
					className="<%= FreestylerImage.class.getName() %>"
					classPK="<%= actualImage.getImageId() %>"
					portletURL="<%= renderResponse.createRenderURL() %>"
					strutsAction="/ext/freestyler/add_cart"
				/>
				
				
				<liferay-ui-ext:add-link
					className="<%= FreestylerImage.class.getName() %>"
					classPK="<%= actualImage.getImageId() %>"
					portletURL="<%= renderResponse.createRenderURL() %>"
					strutsAction="/ext/freestyler/link"
				/>
				
				<liferay-ui-ext:sim-button
					className="<%= FreestylerImage.class.getName() %>"
					classPK="<%= actualImage.getImageId() %>"
					portletURL="<%= renderResponse.createRenderURL() %>"
					strutsAction="/ext/similarity/view_sim"
				/>
				
				<liferay-ui-ext:show-link
					classPK="<%= actualImage.getImageId() %>"
					portletURL="<%= renderResponse.createRenderURL() %>"
					strutsAction="/ext/freestyler/"
				/>
				

						
			</div>
</div>

<div>
		Beschreibung:
	</h4>
	<%= entry.getDescription() %>			
</div>







