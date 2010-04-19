<%
/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
%>

<%@ include file="/init.jsp" %>

<c:if test="<%= permissionChecker.isCompanyAdmin(company.getCompanyId()) %>">
	<liferay-ui:icon-list>

		<%
		PortletURL addMeetupsEntryURL = renderResponse.createRenderURL();

		addMeetupsEntryURL.setWindowState(WindowState.MAXIMIZED);

		addMeetupsEntryURL.setParameter("jspPage", "/meetups/edit_entry.jsp");
		addMeetupsEntryURL.setParameter("redirect", currentURL);
		%>

		<liferay-ui:icon
			image="add_article"
			message="add-meetup"
			url="<%= addMeetupsEntryURL.toString() %>"
			method="get"
		/>

		<%
		PortletURL myMeetupsEntriesURL = renderResponse.createRenderURL();

		myMeetupsEntriesURL.setWindowState(WindowState.MAXIMIZED);

		myMeetupsEntriesURL.setParameter("jspPage", "/meetups/view_entries.jsp");
		myMeetupsEntriesURL.setParameter("personal", "1");
		%>

		<liferay-ui:icon
			image="view"
			message="my-meetups"
			url="<%= myMeetupsEntriesURL.toString() %>"
			method="get"
		/>

		<%
		PortletURL allMeetupsEntriesURL = renderResponse.createRenderURL();

		allMeetupsEntriesURL.setWindowState(WindowState.MAXIMIZED);

		allMeetupsEntriesURL.setParameter("jspPage", "/meetups/view_entries.jsp");
		allMeetupsEntriesURL.setParameter("personal", "0");
		%>

		<liferay-ui:icon
			image="view"
			message="all-meetups"
			url="<%= allMeetupsEntriesURL.toString() %>"
			method="get"
		/>
	</liferay-ui:icon-list>
</c:if>

<%
List<MeetupsEntry> meetupsEntries = MeetupsEntryLocalServiceUtil.getMeetupsEntries(themeDisplay.getCompanyId());
%>

<c:if test="<%= meetupsEntries.size() > 0 %>">
	<br />
</c:if>

<table class="lfr-table" width="100%">

<%
for (int i = 0; i < meetupsEntries.size(); i++) {
	MeetupsEntry meetupsEntry = meetupsEntries.get(i);

	int yesTotal = MeetupsRegistrationLocalServiceUtil.getMeetupsRegistrationsCount(meetupsEntry.getMeetupsEntryId(), MeetupsConstants.STATUS_YES);
%>

	<tr>
		<td align="center" valign="top">

			<%
			PortletURL viewMeetupsEntryURL = renderResponse.createRenderURL();

			viewMeetupsEntryURL.setWindowState(WindowState.MAXIMIZED);

			viewMeetupsEntryURL.setParameter("jspPage", "/meetups/view_entry.jsp");
			viewMeetupsEntryURL.setParameter("meetupsEntryId", String.valueOf(meetupsEntry.getMeetupsEntryId()));
			%>

			<a href="<%= viewMeetupsEntryURL %>"><img src="<%= themeDisplay.getPathImage() %>?img_id=<%= meetupsEntry.getThumbnailId() %>&t=<%= ImageServletTokenUtil.getToken(meetupsEntry.getThumbnailId()) %>" /></a>
		</td>
		<td valign="top" width="99%">
			<div>
				<%= meetupsEntry.getTitle() %>
			</div>

			<br />

			<div>
				<%= meetupsEntry.getDescription() %>
			</div>

			<br />

			<c:if test="<%= yesTotal > 1 %>">
				<div>
					<%= LanguageUtil.format(pageContext, "x-people-are-planning-to-attend-this-meetup", String.valueOf(yesTotal)) %>
				</div>

				<br />
			</c:if>

			<liferay-ui:icon-list>

				<c:if test="<%= permissionChecker.isCompanyAdmin(company.getCompanyId()) %>">

					<%
					PortletURL editMeetupsEntryURL = renderResponse.createRenderURL();

					editMeetupsEntryURL.setWindowState(WindowState.MAXIMIZED);

					editMeetupsEntryURL.setParameter("jspPage", "/meetups/edit_entry.jsp");
					editMeetupsEntryURL.setParameter("redirect", currentURL);
					editMeetupsEntryURL.setParameter("meetupsEntryId", String.valueOf(meetupsEntry.getMeetupsEntryId()));
					%>

					<liferay-ui:icon
						image="edit"
						url="<%= editMeetupsEntryURL.toString() %>"
						method="get"
					/>
				</c:if>

				<liferay-ui:icon
					image="join"
					message="register"
					url="<%= viewMeetupsEntryURL.toString() %>"
					method="get"
				/>

				<c:if test="<%= permissionChecker.isCompanyAdmin(company.getCompanyId()) %>">

					<%
					PortletURL deleteMeetupsEntryURL = renderResponse.createActionURL();

					deleteMeetupsEntryURL.setWindowState(WindowState.MAXIMIZED);

					deleteMeetupsEntryURL.setParameter(ActionRequest.ACTION_NAME, "deleteMeetupsEntry");
					deleteMeetupsEntryURL.setParameter("redirect", currentURL);
					deleteMeetupsEntryURL.setParameter("meetupsEntryId", String.valueOf(meetupsEntry.getMeetupsEntryId()));
					%>

					<liferay-ui:icon-delete
						url="<%= deleteMeetupsEntryURL.toString() %>"
					/>
				</c:if>
			</liferay-ui:icon-list>
		</td>
	</tr>

	<c:if test="<%= (i + 1) < meetupsEntries.size() %>">
		<tr>
			<td colspan="2">
				<div class="separator"><!-- --></div>
			</td>
		</tr>
	</c:if>

<%
}
%>

</table>