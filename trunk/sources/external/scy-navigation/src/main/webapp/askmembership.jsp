<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />


<% 
/**
* Create view:
* Show groupname as heading.
*/

if(request.getParameterNames() != null) {
	%>
	<h3><liferay-ui:message key="membershipAt"/>
	<%= request.getParameter("groupName") %>
    </h3>
	<%    
}
%>
<br/>
<liferay-ui:message key="comment"/>
<%
/**
* Form for requesting comments according to the membership request.
*/
%>
<FORM NAME="form" METHOD="POST" action="<portlet:renderURL><portlet:param name="jspPage" value="/view.jsp" /></portlet:renderURL>">
	<INPUT TYPE="HIDDEN" NAME="community" value="<%= request.getParameter("groupId") %>">
	<INPUT TYPE="HIDDEN" NAME="button">
	<textarea name="comment" cols="50" rows="10"></textarea>
	<INPUT TYPE="BUTTON" VALUE="<liferay-ui:message key="askForMembership"/>" ONCLICK="buttonPressed('submit')">
	<INPUT TYPE="BUTTON" VALUE="<liferay-ui:message key="cancel"/>" ONCLICK="buttonPressed('cancel')">
</FORM>

<SCRIPT type="text/javascript">
	function buttonPressed(res){
    	document.form.button.value = res;
    	document.form.submit();
    }    
</SCRIPT>