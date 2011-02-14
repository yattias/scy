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

<%@ include file="init.jsp" %>



<%

String searchResults = ParamUtil.getString(request,"test");

%>


<portlet:defineObjects />

	


<p><liferay-ui:message key="add-new" />...2</p>


<form name="formName" action="">
	<div>
		<select id="selector" name="customAssetURL" onchange="addNewAsset();">
			<option value=""><liferay-ui:message key="add-new" />...</option>				
			<option value="http://localhost:48080/web/guest/content?p_p_id=20&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_20_struts_action=%2Fdocument_library%2Fedit_file_entry&_20_folderId=0&_20_uploader=classic&_20_redirect=%2Fweb%2Fguest%2Fcontent&_20_backURL=%2Fweb%2Fguest%2Fcontent&_20_tagsEntries=approved"><liferay-ui:message key='<%= "model.resource.com.liferay.portlet.documentlibrary.model.DLFileEntry" %>' /></option>	
			<option value="http://localhost:48080/web/guest/content?p_p_id=31&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_31_struts_action=%2Fimage_gallery%2Fedit_image&_31_folderId=0&_31_uploader=classic&_31_redirect=%2Fweb%2Fguest%2Fcontent&_31_backURL=%2Fweb%2Fguest%2Fcontent&_31_tagsEntries=approved"><liferay-ui:message key='<%= "model.resource.com.liferay.portlet.imagegallery" %>' /></option>
		</select>
	</div>
</form>

<script language="JavaScript">
	function addNewAsset() {

		var val = document.getElementById("selector").selectedIndex;
		var url = document.getElementById("selector").options[val].value;
		/* var url = document.formName.customAssetURL.options[val].value; */

		if (url != '') {
			location = url;
		}	
	}
</script>





<!-- 
http://localhost:48080/web/guest/content?p_p_id=20&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_20_struts_action=%2Fdocument_library%2Fedit_file_entry&_20_folderId=0&_20_uploader=classic&_20_redirect=%2Fweb%2Fguest%2Fcontent&_20_backURL=%2Fweb%2Fguest%2Fcontent&_20_tagsEntries=approved
 -->
 <!-- 
 http://localhost:48080/web/guest/content?p_p_id=31&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_31_struts_action=%2Fimage_gallery%2Fedit_image&_31_folderId=0&_31_uploader=classic&_31_redirect=%2Fweb%2Fguest%2Fcontent&_31_backURL=%2Fweb%2Fguest%2Fcontent&_31_tagsEntries=approved
  -->