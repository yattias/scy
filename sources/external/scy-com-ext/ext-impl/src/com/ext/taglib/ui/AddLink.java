package com.ext.taglib.ui;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import com.liferay.taglib.util.IncludeTag;

/**
 * The class for taglib ui add_link. This taglib is to add extern and intern
 * links.
 * 
 * @author Daniel
 * 
 */
public class AddLink extends IncludeTag {

	private static final long serialVersionUID = 1L;

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		request.setAttribute("liferay-ui-ext:add-link:className", _className);
		request.setAttribute("liferay-ui-ext:add-link:classPK", String.valueOf(_classPK));
		request.setAttribute("liferay-ui-ext:add-link:message", _message);
		request.setAttribute("liferay-ui-ext:add-link:portletURL", _portletURL);
		request.setAttribute("liferay-ui-ext:add-link:strutsAction", _strutsAction);
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

	private static final String _PAGE = "/html/taglib/ui/add_link/page.jsp";

	private String _className;
	private long _classPK;
	private String _message;
	private PortletURL _portletURL;
	private String _strutsAction;

}