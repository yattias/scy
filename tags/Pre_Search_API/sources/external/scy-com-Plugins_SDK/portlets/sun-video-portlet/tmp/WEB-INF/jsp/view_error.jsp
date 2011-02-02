<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<portlet:defineObjects/>

<div class="portlet-msg-error" style="font-size:larger">An error has occured</div>

<p>
    Possible reasons are:
    <ul>
        <li>There is some mistake in your configuration</li>.Please <a href='<portlet:renderURL portletMode="edit"/>'>check your configuration</a>
        <li>The Youtube service is not available at the moment</li>
        <li>This server has not been configured properly. Please contact the administrator</li>
    </ul>
</p>
