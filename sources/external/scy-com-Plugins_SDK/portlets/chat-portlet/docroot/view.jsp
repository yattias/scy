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

<c:if test="<%= themeDisplay.isSignedIn() %>">

	<%
	Portlet portlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), portletDisplay.getId());
	%>

	<liferay-util:html-top>
		<link href="<%= PortalUtil.getStaticResourceURL(request, request.getContextPath() + "/css.jsp", portlet.getTimestamp()) %>" rel="stylesheet" type="text/css" />
	</liferay-util:html-top>

	<liferay-util:html-bottom>
		<script src="<%= PortalUtil.getStaticResourceURL(request, request.getContextPath() + "/javascript.js", portlet.getTimestamp()) %>" type="text/javascript"></script>
	</liferay-util:html-bottom>

	<%
	Status status = StatusLocalServiceUtil.getUserStatus(themeDisplay.getUserId());

	boolean online = status.getOnline();
	String activePanelId = status.getActivePanelId();
	String statusMessage = HtmlUtil.escape(status.getMessage());
	boolean playSound = status.getPlaySound();

	List<Object[]> buddies = ChatUtil.getBuddies(themeDisplay.getUserId());

	int buddiesCount = buddies.size();
	%>

	<div class="portlet-chat">
		<div class="chat-bar">
			<div class="chat-sound"></div>

			<div class="chat-status">
				<div class="status-message">
					<c:if test="<%= Validator.isNotNull(statusMessage) %>">
						<%= LanguageUtil.format(pageContext, "you-are-x", "<strong>" + statusMessage + "</strong>", false) %>
					</c:if>
				</div>
			</div>

			<div class="chat-tabs-container">
				<ul class="chat-tabs">
					<li class="buddy-list <%= activePanelId.equals("buddylist") ? "selected" : "" %>">
						<div class="panel-trigger" panelId="buddylist">
							<span class="trigger-name"><%= LanguageUtil.format(pageContext, "online-friends-x", "(" + buddiesCount + ")", false) %></span>
						</div>

						<div class="chat-panel">
							<div class="panel-window">
								<div class="panel-button minimize"></div>

								<div class="panel-title"><%= LanguageUtil.format(pageContext, "online-friends-x", "(" + buddiesCount + ")", false) %></div>

								<div class="panel-content">
									<ul class="lfr-component online-users">

										<%
										for (Object[] buddy : buddies) {
											long userId = (Long)buddy[0];
											String firstName = (String)buddy[1];
											String middleName = (String)buddy[2];
											String lastName = (String)buddy[3];
											long portraitId = (Long)buddy[4];
											boolean awake = (Boolean)buddy[5];

											String fullName = ContactConstants.getFullName(firstName, middleName, lastName);
										%>

											<li class="active" userId="<%= userId %>">
												<img alt="" src="<%= themeDisplay.getPathImage() %>/user_portrait?img_id=<%= portraitId %>">

												<div class="name">
													<%= fullName %>
												</div>
											</li>

										<%
										}
										%>

									</ul>
								</div>

								<div style="clear: both;"></div>
							</div>
						</div>
					</li>
					<li class="chat-settings <%= activePanelId.equals("settings") ? "selected" : "" %>">
						<div class="panel-trigger" panelId="settings">
							<span class="trigger-name"><liferay-ui:message key="settings" /></span>
						</div>

						<div class="chat-panel">
							<div class="panel-window">
								<div class="panel-button minimize"></div>

								<div class="panel-title"><liferay-ui:message key="settings" /></div>

								<ul class="lfr-component settings">
									<li>
										<label for="statusMessage"><%= LanguageUtil.format(pageContext, "x-is", user.getFullName(), false) %></label>

										<input id="statusMessage" type="text" value="<%= statusMessage %>" />
									</li>
									<li>
										<label for="onlineStatus"><input <%= online ? "checked=\"checked\"" : "" %> id="onlineStatus" type="checkbox" /> <liferay-ui:message key="show-me-as-online" /></label>
									</li>
									<li>
										<label for="playSound"><input <%= playSound ? "checked=\"checked\"" : "" %> id="playSound" type="checkbox" /> <liferay-ui:message key="play-a-sound-when-i-receive-a-new-message-in-a-hidden-window" /> </label>
									</li>
								</ul>

								<div class="ctrl-holder">
									<input id="saveSettings" type="submit" value="<liferay-ui:message key="save" />">
								</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>

		<input id="activePanelId" type="hidden" value="<%= activePanelId %>" />
		<input id="chatPortletId" type="hidden" value="<%= portletDisplay.getId() %>" />
	</div>
</c:if>