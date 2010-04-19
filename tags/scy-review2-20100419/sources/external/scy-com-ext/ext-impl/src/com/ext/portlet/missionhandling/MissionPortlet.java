package com.ext.portlet.missionhandling;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portlet.StrutsPortlet;

public class MissionPortlet extends StrutsPortlet {

	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		try {
			super.doView(renderRequest, renderResponse);
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
