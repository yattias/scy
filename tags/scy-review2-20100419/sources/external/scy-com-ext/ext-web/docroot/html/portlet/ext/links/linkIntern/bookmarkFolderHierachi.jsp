<%@ include file="/html/portlet/ext/links/init.jsp" %>
<%
String startResourceclassName = StringPool.BLANK;
String redirect = ParamUtil.getString(request, "redirect");
String sType = request.getParameter("sType");
String checkedResource = (String)request.getAttribute("checkedResource");
String onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
String struts_action =  ParamUtil.getString(request, "struts_action");
String struts_actionViewContent =  ParamUtil.getString(request, "struts_actionViewContent");
String entryId =  ParamUtil.getString(request, "entryId");
%>

	<div style="text-align: left; padding:5px;" >		
	<%
		List<BookmarksFolder> hList = (List<BookmarksFolder>) request.getAttribute("hList");
	%>
		<c:if test="<%= hList != null %>">
	<%
				for (int hListCount = 0; hListCount < hList.size(); hListCount++) {			
							BookmarksFolder hEntry = (BookmarksFolder)hList.get(hListCount);	
							Boolean isLast = false;
							if(hListCount == (hList.size()-1)){
								isLast = true;
							}
							
							
	%>
						<portlet:actionURL var="changeFolder">
			                    <portlet:param name="struts_action" value="<%= struts_action %>" />
			                    <portlet:param name="entryId" value="<%= entryId %>" />
			                  	<portlet:param name="cmd" value="choseFolder" />
			                  	<portlet:param name="rootFolderId" value="<%= String.valueOf(hList.get(0).getFolderId()) %>" />
			                    <portlet:param name="folderId" value="<%= String.valueOf(hEntry.getFolderId()) %>" />
			                  	<portlet:param name="sType" value="<%= sType %>" /> 
			                  	<portlet:param name="redirect" value="<%= redirect %>" /> 
			                  	<portlet:param name="className" value="<%= startResourceclassName %>" /> 
						</portlet:actionURL>
						
	
							<c:if test="<%= !isLast %>">
								<a href="<%= changeFolder %>" <%= onClickHtml %>> 								
									<%= hEntry.getName() %></a> >>
							</c:if>										
							<c:if test="<%= isLast %>">
									<%= hEntry.getName() %></a>
							</c:if>
	
	<%
				}
	%>
		</c:if>
	</div>
