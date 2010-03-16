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

<%@ include file="/html/portlet/requests/init.jsp" %>

<%@ page import="java.io.File" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil" %>


<%


String userAgent = request.getHeader("User-Agent"); 
boolean mozilla = userAgent.startsWith("Mozilla/5.0");

InputStream is = DLFileEntryLocalServiceUtil.getFileAsStream(10122, 10150, 10315, "DLFE-401.mp4");
File f = new File("bla.mp4");

OutputStream out2 = new FileOutputStream(f);
byte buf[] = new byte[1024];
int len;
while ((len = is.read(buf)) > 0)
	out2.write(buf, 0, len);
out2.close();
is.close();



%>

<%= f.getPath() %>
<%= f.getAbsoluteFile() %>

<c:if test="<%= mozilla %>">
<div>
<p align="center">
<a  
    href="<%= f.getAbsoluteFile()%>"  
    style="display:block;width:425px;height:300px;"  
    id="player"> 
</a> 
</p>
</div>
</c:if>

<c:if test="<%= !mozilla %>">
<div>
<p align="center">
<object width="320" height="290" classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" id="mediaplayer1">
<param name="Filename" value="<%= f.getAbsoluteFile()%>">
<param name="AutoStart" value="False">
<param name="ShowControls" value="True">
<param name="ShowStatusBar" value="False">
<param name="ShowDisplay" value="False"><param name="AutoRewind" value="True">
</object>
</p>
</div>
</c:if>


<%
List<SocialRequest> requests = (List<SocialRequest>)request.getAttribute(WebKeys.SOCIAL_REQUESTS);
%>

<c:if test="<%= requests != null %>">

	<%
	PortletURL portletURL = renderResponse.createActionURL();

	portletURL.setParameter("struts_action", "/ext/book_reports/update_request");
	portletURL.setParameter("redirect", currentURL);
	%>

	<table class="lfr-table" width="100%">

	<%
	for (int i = 0; i < requests.size(); i++) {
		SocialRequest socialRequest = requests.get(i);

		SocialRequestFeedEntry requestFeedEntry = SocialRequestInterpreterLocalServiceUtil.interpret(socialRequest, themeDisplay);
	%>

		<tr>
			<td align="center" valign="top">
				<liferay-ui:user-display
					userId="<%= socialRequest.getUserId() %>"
					displayStyle="<%= 2 %>"
				/>
			</td>
			<td valign="top" width="99%">
				<c:choose>
					<c:when test="<%= requestFeedEntry == null %>">
						<div class="portlet-msg-error">
							<liferay-ui:message key="request-cannot-be-interpreted-because-it-does-not-have-an-associated-interpreter" />
						</div>
					</c:when>
					<c:otherwise>

						<%
						portletURL.setParameter("requestId", String.valueOf(socialRequest.getRequestId()));
						%>

						<div>
							<%= requestFeedEntry.getTitle() %>
						</div>

						<br />

						<c:if test="<%= Validator.isNotNull(requestFeedEntry.getBody()) %>">
							<div>
								<%= requestFeedEntry.getBody() %>
							</div>

							<br />
						</c:if>

						<liferay-ui:icon-list>

							<%
							portletURL.setParameter("status", String.valueOf(SocialRequestConstants.STATUS_CONFIRM));
							%>

							<liferay-ui:icon
								image="activate"
								message="confirm"
								url="<%= portletURL.toString() %>"
							/>

							<%
							portletURL.setParameter("status", String.valueOf(SocialRequestConstants.STATUS_IGNORE));
							%>

							<liferay-ui:icon
								image="deactivate"
								message="ignore"
								url="<%= portletURL.toString() %>"
							/>
						</liferay-ui:icon-list>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>

		<c:if test="<%= (i + 1) < requests.size() %>">
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
</c:if>

<script src="http://localhost:8080/flowplayer-3.1.4.min.js"></script>

<script> 
flowplayer("player", "http://localhost:8080/flowplayer-3.1.5.swf", { 
    clip:  { 
        autoPlay: false, 
        autoBuffering: true 
    } 
}); 
</script>

