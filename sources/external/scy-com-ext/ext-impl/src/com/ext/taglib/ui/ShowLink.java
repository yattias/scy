package com.ext.taglib.ui;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import com.liferay.taglib.util.IncludeTag;

/**
 * The class for taglib ui show_link. This taglib shows all linked entries for
 * given resource.
 * 
 * @author Daniel
 * 
 */
public class ShowLink extends IncludeTag {

	private static final long serialVersionUID = 1L;

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		request.setAttribute("liferay-ui-ext:show-link:classPK", String.valueOf(_classPK));
		request.setAttribute("liferay-ui-ext:show-link:portletURL", _portletURL);
		request.setAttribute("liferay-ui-ext:show-link:strutsAction", _strutsAction);
		return EVAL_BODY_BUFFERED;
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

	private static final String _PAGE = "/html/taglib/ui/show_link/page.jsp";

	private long _classPK;
	private PortletURL _portletURL;
	private String _strutsAction;

}