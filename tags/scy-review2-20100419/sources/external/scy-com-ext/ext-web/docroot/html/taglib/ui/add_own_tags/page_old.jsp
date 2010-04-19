
<form action="<portlet:actionURL windowState="<%= WindowState.MAXIMIZED.toString() %>">
                    <portlet:param name="struts_action" value="<%= struts_action  %>" />
                    <portlet:param name="entryId" value="<%= String.valueOf(classPK) %>" />
                  	<portlet:param name="cmd" value="<%= Constants.VIEW %>" />
                  	<portlet:param name="className" value="<%= className  %>" />
              </portlet:actionURL>"
                method="post" name="<portlet:namespace />fm56667">

      </form>  
      <div style="color: #12558E">
           <input onClick="submitForm(document.<portlet:namespace />fm56667);" style="margin-top 5px;" type="button" value="Tags">
      </div>


