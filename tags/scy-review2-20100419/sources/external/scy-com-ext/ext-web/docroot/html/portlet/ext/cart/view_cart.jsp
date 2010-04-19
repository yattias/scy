<%@ include file="/html/portlet/ext/cart/init.jsp" %>
<%
List<CartEntry> cartEntryList = (List<CartEntry>)request.getAttribute("cartEntryList");
List<Cart> cartList = (List<Cart>)request.getAttribute("cartList");
boolean windowStateNormal = ((Boolean)request.getAttribute("windowStateNormal"));
String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");
String typeRemovoeMarked;
boolean listNotempty= true;
boolean onlyView= false;
if(cartEntryList.size() < 1 ){
  listNotempty = false;
}
%>

<div style="text-align: center" >
 <h2><liferay-ui:message key="actual-cart-entry" /></h2>
 <br/>
</div>

<% 
List<TagsAsset> results = (List<TagsAsset>)request.getAttribute("assetList");
request.setAttribute("view.jsp-results", results);
request.setAttribute("onlyView", onlyView);
    
if (results.size() > 0) {
%>      
	<%@ include file="/html/portlet/ext/cart/view_dynamic_list_asset.jspf" %>				
	
	<br/> 
	
	<liferay-ui:tabs
		names="save"
	/>	
	  <form action="<portlet:actionURL windowState="<%= WindowState.MAXIMIZED.toString() %>">
                  <portlet:param name="struts_action" value="/ext/cart/save_cart" />
              </portlet:actionURL>"
                method="post" name="<portlet:namespace />fmSave">
                
         <c:if test="<%= listNotempty %>">
         	<table class="lfr-table">
         		<tr>
					<td>
						<liferay-ui:message key="name" />
					</td>
					<td>
        			     <input name="<portlet:namespace />title" size="35" type="text" value="">
			             <input name="<portlet:namespace />referringPortletResource" type="hidden" value="<%= HtmlUtil.escape(referringPortletResource) %>" />
			             <input name="<portlet:namespace />redirect" type="hidden" value="<%= currentURL %>" />
					</td>
				</tr>
<%
				long classPK = 0;
%>
				<tr>
					<td>
						<liferay-ui:message key="Tags" />
					</td>
					<td>
						<liferay-ui:tags-selector
							className="<%= Cart.class.getName() %>"
							classPK="<%= classPK %>"
							hiddenInput="tagsEntries"
						/>
        			  </td>
				</tr>
			</table>
			<br/>
             <input onClick="submitForm(document.<portlet:namespace />fmSave);" style="margin-top 5px;" type="button" value="<liferay-ui:message key="save-cart-entry" />">
         </c:if>                
    </form>
<%
}
else{
%>
	<div style="text-align: center" >
		<liferay-ui:message key="no-cart-entry-exist" />
				
	</div>
<%
}
%>

 
    
 	<div class="separator"><!-- --></div>

	<table style="maxwidth: 500;">
		<tr class="portlet-section-header results-header">
			<th width=300>
				<liferay-ui:message key="saved-cart-entries" />
			</th>
			<th></th>
			<th width=40><liferay-ui:message key="delete-cart-entry" /></th>
		</tr>
<%
%>
			

<%
		  for (int cartCount = 0; cartCount <cartList.size(); cartCount++) {
		  	Cart cart = (Cart)cartList.get(cartCount);
   		 	int assetIndex = cartCount;			
			String style = "class=\"portlet-section-body results-row\" onmouseover=\"this.className = 'portlet-section-body-hover results-row hover';\" onmouseout=\"this.className = 'portlet-section-body results-row';\"";
			if (assetIndex % 2 == 0) {
				style = "class=\"portlet-section-alternate results-row alt\" onmouseover=\"this.className = 'portlet-section-alternate-hover results-row alt hover';\" onmouseout=\"this.className = 'portlet-section-alternate results-row alt';\"";
			}
%>
      		<portlet:actionURL var="viewCartEntryList">
	            <portlet:param name="struts_action" value="/ext/cart/view_cartListEntry" />
	            <portlet:param name="entryId" value="<%= String.valueOf(cart.getCartId()) %>" />						        
	          	<portlet:param name="redirect2" value="<%= currentURL %>" />                  	
			</portlet:actionURL>
<%
			String onClickHtml = StringPool.BLANK;
			onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
%>

			<tr <%= style %>>
				<td  width=40>  
					    <a href="<%= viewCartEntryList %>" <%= onClickHtml %>>
					         <%= cart.getTitle() %>
					    </a> 	
				</td>
				<td> 
				</td>	
				 <td align="center"> 
			      	<portlet:actionURL var="removeCart">
						<portlet:param name="struts_action" value="/ext/cart/remove_Cart" />
						<portlet:param name="entryId" value="<%= String.valueOf(cart.getCartId()) %>" />	      
            		</portlet:actionURL>
					<liferay-ui:icon-delete url="<%= removeCart %>" />
				</td>
			</tr>
<%
      	}
%>            
	</table>

