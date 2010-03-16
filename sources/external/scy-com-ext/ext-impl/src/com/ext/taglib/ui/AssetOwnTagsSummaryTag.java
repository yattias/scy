package com.ext.taglib.ui;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import com.liferay.taglib.util.IncludeTag;

/**
 * The class for taglib ui add_asset_own_tags. This taglib shows the user his
 * own tags for a given resource and the user can delete them by press button.
 * 
 * @author Daniel
 * 
 */
public class AssetOwnTagsSummaryTag extends IncludeTag {

	private static final long serialVersionUID = 1L;

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();

		request.setAttribute("liferay-ui-ext:asset-owntags-summary:className",
				_className);
		request.setAttribute("liferay-ui-ext:asset-owntags-summary:classPK", String
				.valueOf(_classPK));
		request.setAttribute("liferay-ui-ext:asset-owntags-summary:message", _message);
		request.setAttribute("liferay-ui-ext:asset-owntags-summary:portletURL",
				_portletURL);	
		request.setAttribute("liferay-ui-ext:add-own-tags:strutsAction",
				_strutsAction);
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

	private static final String _PAGE = "/html/taglib/ui/asset_owntags_summary/page.jsp";

	private String _className;
	private long _classPK;
	private String _message;
	private PortletURL _portletURL;
	private String _strutsAction;

}