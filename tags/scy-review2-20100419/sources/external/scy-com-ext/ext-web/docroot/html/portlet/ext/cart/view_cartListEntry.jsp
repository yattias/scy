<%@ include file="/html/portlet/ext/cart/init.jsp" %>
<%
List<CartEntry> cartEntryList2 = (List<CartEntry>)request.getAttribute("cartEntryList2");
List<Cart> cartList2 = (List<Cart>)request.getAttribute("cartList2");
Cart actualCart = (Cart)request.getAttribute("actualCart");
String redirect2 = ParamUtil.getString(request, "redirect2");
boolean windowStateNormal = ((Boolean)request.getAttribute("windowStateNormal"));
boolean onlyView = true;

String typeRemovoeMarked;
boolean listNotempty= true;
if(cartEntryList2.size() < 1 ){
  listNotempty = false;
}
%>

<div style="text-align: center" >
 <liferay-ui:message key="cart-entry" /> <%= actualCart.getTitle() %>
 <br/><br/>
</div>

		<liferay-ui:tabs
		names="<%= actualCart.getTitle() %>"
		backURL="<%= redirect2 %>"
	/>
    

    <% 
    List<TagsAsset> results = (List<TagsAsset>)request.getAttribute("assetList2");
    request.setAttribute("view.jsp-results", results);
    request.setAttribute("onlyView", onlyView);
    

    if (results.size() > 0) {
    %>
      	

      	
			<%@ include file="/html/portlet/ext/cart/view_dynamic_list_asset.jspf" %>
			
			
<%
		}
%>
				<br/>

      			<div class="entry-tags">		
					<liferay-ui:tags-summary
						className="<%= Cart.class.getName() %>"
						classPK="<%= actualCart.getCartId() %>"
					/>
				</div>
				
				<br />
				
					
      			<div class="entry-tags">		
				<liferay-ui-ext:add-own-tags
					className="<%= Cart.class.getName() %>"
					classPK="<%= actualCart.getCartId() %>"
					portletURL="<%= renderResponse.createRenderURL() %>"
					strutsAction="/ext/cart/add_tag"
				/>

			      				<portlet:actionURL var="removeCart">
						                    <portlet:param name="struts_action" value="/ext/cart/remove_Cart" />
						                    <portlet:param name="entryId" value="<%= String.valueOf(actualCart.getCartId()) %>" />	      
            					</portlet:actionURL>
	
<%
		String onClickHtml = StringPool.BLANK;
		onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
%>
		<a href="<%= removeCart %>" <%= onClickHtml %>>
			 <input type="button" value=" <liferay-ui:message key="delete-cart-entrie-button" />">
	    </a> 							
	</div>
	<br/><br/>
    	
	<table style="maxwidth: 500;">
		<tr class="portlet-section-header results-header">
			<th width=300 >
				<liferay-ui:message key="saved-cart-entries" />
			</th>
			<th width=40><liferay-ui:message key="delete-cart-entry" /></th>
		</tr>
<%
%>
			

<%
		  for (int cartCount = 0; cartCount <cartList2.size(); cartCount++) {
		  	Cart cart = (Cart)cartList2.get(cartCount);
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

			<tr <%= style %>>
				<td>  
					    <a href="<%= viewCartEntryList %>" <%= onClickHtml %>>
					         <%= cart.getTitle() %>
					    </a> 	
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

