package com.ext.portlet.bookreports.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.struts.PortletAction;


public class ViewBookReportsAction extends PortletAction {

	public ActionForward render(ActionMapping mapping, ActionForm form,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse) throws Exception {
		if (renderRequest.getWindowState().equals(WindowState.NORMAL)) {
			return mapping.findForward("portlet.ext.book_reports.view");
		} else {
			List<String> reports = Collections
					.synchronizedList(new ArrayList<String>());
			PortletPreferences prefs = renderRequest.getPreferences();
			reports.add("1: Preferences book - "
					+ prefs.getValue("test", ""));
			reports.add("2: Window State - " + renderRequest.getWindowState());
			reports.add("3: Portlet Mode - "
					+ renderRequest.getPortletMode().toString());
			reports.add("4: Portlet Session - "
					+ renderRequest.getPortletSession().getId());
			renderRequest.setAttribute("reports", reports);
			return mapping.findForward("portlet.ext.book_reports.view_reports");
		}
	}
}
