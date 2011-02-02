package com.ext.portlet.missionhandling.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.util.MissionDeactivatorUtil;

public class AddMissionEntry extends ViewMissionAction {
	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		String redirect = req.getParameter("redirect");
		String cmd = req.getParameter("cmd");
		String struts_action = req.getParameter("struts_action");
		String days = req.getParameter("zahlen");

		String reqex = "\\D";
		String errorMessage = "";

		redirect = redirect.replaceAll("amp;", "");

		Long organizationId = null;
		Organization organization = null;
		if (req.getParameter("organizationId") != null) {
			organizationId = Long.valueOf(req.getParameter("organizationId"));
			organization = OrganizationLocalServiceUtil.getOrganization(organizationId);
		} else {
			organizationId = 0l;
		}

		if (days != null) {
			Pattern pattern = Pattern.compile(reqex);
			Matcher matcher = pattern.matcher(days);
			if (matcher.find()) {
				errorMessage = "Please enter only digits between 0-9 for days";
				req.setAttribute("errorMessage", errorMessage);
				req.setAttribute("redirect", redirect);
				req.setAttribute("struts_action", struts_action);
				req.setAttribute("organization", organization);
				setForward(req, "portlet.ext.missionhandling.view_addMissionEntry");
				return;
			} else {
				if (days.length() == 0 || days.equals("0")) {
					MissionDeactivatorUtil.deactivateMission(organizationId);
				} else {
					MissionDeactivatorUtil.addMissionEntry(days, organization);
				}
				res.sendRedirect(redirect);
				return;
			}
		}

		if (cmd.equals(Constants.VIEW)) {
			// set Attributes for render page
			req.setAttribute("redirect", redirect);
			req.setAttribute("struts_action", struts_action);
			req.setAttribute("organization", organization);
			setForward(req, "portlet.ext.missionhandling.view_addMissionEntry");
			return;
		}

		// set Attributes for render page

		// set Forward
		res.sendRedirect(redirect);
	}
}
