package com.ext.taglib.ui;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import com.liferay.taglib.util.IncludeTag;

/**
 * The class for taglib ui sim_button. This taglib start new sim similirty for
 * given resource and ipc from the resource portlet to similirity portlet. The
 * user arrives new portlet maximized view in similarity portlet.
 * 
 * @author Daniel
 * 
 */
public class SimButton extends IncludeTag {

	private static final long serialVersionUID = 1L;

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		request.setAttribute("liferay-ui-ext:sim-button:className", _className);
		request.setAttribute("liferay-ui-ext:sim-button:classPK", String.valueOf(_classPK));
		request.setAttribute("liferay-ui-ext:sim-button:portletURL", _portletURL);
		request.setAttribute("liferay-ui-ext:sim-button:strutsAction", _strutsAction);
		return EVAL_BODY_BUFFERED;
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setClassPK(long classPK) {
		_classPK = classPK;
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

	private static final String _PAGE = "/html/taglib/ui/sim_button/page.jsp";

	private String _className;
	private long _classPK;
	private PortletURL _portletURL;
	private String _strutsAction;

}