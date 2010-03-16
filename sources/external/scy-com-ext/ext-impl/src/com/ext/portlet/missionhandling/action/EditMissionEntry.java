package com.ext.portlet.missionhandling.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.missionhandling.NoSuchMissionEntryException;
import com.ext.portlet.missionhandling.model.MissionEntry;
import com.ext.portlet.missionhandling.service.MissionEntryLocalServiceUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.util.MissionDeactivatorUtil;

public class EditMissionEntry extends ViewMissionAction {
	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		String redirect = req.getParameter("redirect");
		String cmd = req.getParameter("cmd");
		String struts_action = req.getParameter("struts_action");
		String days = req.getParameter("zahlen");

		String reqex = "\\D";
		String errorMessage = "";

		redirect = redirect.replaceAll("amp;", "");

		Long missionEntryId = null;
		MissionEntry missionEntry = null;
		if (req.getParameter("missionEntryId") != null) {
			missionEntryId = Long.valueOf(req.getParameter("missionEntryId"));
			try {
				missionEntry = MissionEntryLocalServiceUtil.getMissionEntry(missionEntryId);

			} catch (NoSuchMissionEntryException nsmee) {
				System.out.println(nsmee.getMessage());
			}
		} else {
			missionEntryId = 0l;
		}

		if (days != null) {
			Pattern pattern = Pattern.compile(reqex);
			Matcher matcher = pattern.matcher(days);
			if (matcher.find()) {
				errorMessage = "Please enter only digits between 0-9 for days";
				req.setAttribute("errorMessage", errorMessage);
				req.setAttribute("redirect", redirect);
				req.setAttribute("struts_action", struts_action);
				req.setAttribute("missionEntry", missionEntry);
				setForward(req, "portlet.ext.missionhandling.view_editMissionEntry");
				return;
			} else {
				if (days.length() == 0 || days.equals("0")) {
					MissionDeactivatorUtil.deleteMissionEntry(missionEntry);
				} else {
					MissionDeactivatorUtil.changeMissionEntry(days, missionEntry);

				}

				res.sendRedirect(redirect);
				return;
			}
		} else if (cmd.equals(Constants.EDIT)) {
			// set Attributes for render page
			req.setAttribute("redirect", redirect);
			req.setAttribute("struts_action", struts_action);
			req.setAttribute("missionEntry", missionEntry);
			setForward(req, "portlet.ext.missionhandling.view_editMissionEntry");
			return;
		} else if (cmd.equals("activate")) {
			MissionDeactivatorUtil.activateOrganization(missionEntryId);
		}

		// set Forward
		res.sendRedirect(redirect);
	}

}
