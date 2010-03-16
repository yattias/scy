package com.ext.taglib.ui;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import com.liferay.taglib.util.IncludeTag;

/**
 * The class for taglib ui add_own_meta. This taglib is to edit metadata from a
 * resource. The form will be shown after press the ui button. After fill the
 * user can update the metadata or cancel and get back to previews page.
 * 
 * @author Daniel
 * 
 */
public class AddOwnMeta extends IncludeTag {

	private static final long serialVersionUID = 1L;

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		request.setAttribute("liferay-ui-ext:add-own-meta:className", _className);
		request.setAttribute("liferay-ui-ext:add-own-meta:classPK", String.valueOf(_classPK));
		request.setAttribute("liferay-ui-ext:add-own-meta:message", _message);
		request.setAttribute("liferay-ui-ext:add-own-meta:portletURL", _portletURL);
		request.setAttribute("liferay-ui-ext:add-own-meta:strutsAction", _strutsAction);
		return EVAL_BODY_BUFFERED;
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public PortletURL getPortletURL() {
		return _portletURL;
	}

	public void setPortletURL(PortletURL portletURL) {
		_portletURL = portletURL;
	}

	protected String getDefaultPage() {
		return _PAGE;
	}

	public void setStrutsAction(String strutsAction) {
		this._strutsAction = strutsAction;
	}

	private static final String _PAGE = "/html/taglib/ui/add_own_meta/page.jsp";

	private String _className;
	private long _classPK;
	private String _message;
	private PortletURL _portletURL;
	private String _strutsAction;

}