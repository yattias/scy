package com.ext.portlet.similarity.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.similarity.model.Sim;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.ActionRequestImpl;
import com.liferay.portlet.PortletSessionImpl;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;

/**
 * Generate all data for the view from the similarity portlet. The start
 * resource can be chose by press the taglib ui sim_button. Three sorted list
 * with tags assetId needed for highest similarity, lasted date and most views.
 * 
 * @author Daniel
 * 
 */
public class ViewSimilarityAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		Long entryId = Long.valueOf(req.getParameter("entryId"));
		String className = req.getParameter("className");
		String sType = req.getParameter("sType");
		String simMeasure = req.getParameter("simMeasure");
		String[] checklist = req.getParameterValues("checklist");
		String redirect = req.getParameter("redirect");

		HttpSession oHttpSession = getActionRequestSession(req);

		if (checklist != null) {
			oHttpSession.setAttribute("checklist", checklist);
		} else if (oHttpSession.getAttribute("checklist") != null) {
			checklist = (String[]) oHttpSession.getAttribute("checklist");
		} else {
			checklist = new String[] {};
		}

		if (simMeasure != null) {
			oHttpSession.setAttribute("simMeasure", simMeasure);
		} else if (oHttpSession.getAttribute("simMeasure") != null) {
			simMeasure = (String) oHttpSession.getAttribute("simMeasure");
		} else {
			simMeasure = new String();
		}

		if (sType != null) {
			oHttpSession.setAttribute("sType", sType);
		}

		// start sim calculation and set parameter to user session
		if (entryId != null && className != null && checklist != null && simMeasure != null) {
			Hashtable<Long, Double> simValueList = new Hashtable<Long, Double>();
			Hashtable<Long, Date> simDateList = new Hashtable<Long, Date>();
			Hashtable<Long, Integer> simViewCountList = new Hashtable<Long, Integer>();

			SimilarityCalculator simCalculator = new SimilarityCalculator();
			List<Sim> calcSimList = simCalculator.similarityCalculator(entryId, className, checklist, simMeasure);

			for (Sim sim : calcSimList) {
				simValueList.putAll(sim.getSimValueList());
				simDateList.putAll(sim.getSimDateList());
				simViewCountList.putAll(sim.getSimViewList());
			}

			ArrayList<Hashtable<Long, Double>> mySortetValueList = simCalculator.getSortByValueList(simValueList);
			ArrayList<Hashtable<Long, Date>> mySortetDateList = simCalculator.getSortByDateList(simDateList);
			ArrayList<Hashtable<Long, Integer>> mySortetViewCountList = simCalculator.getSortByViewCountList(simViewCountList);

			oHttpSession.setAttribute("entryId", entryId);
			oHttpSession.setAttribute("className", className);
			oHttpSession.setAttribute("mySortetValueList", mySortetValueList);
			oHttpSession.setAttribute("mySortetDateList", mySortetDateList);
			oHttpSession.setAttribute("mySortetViewCountList", mySortetViewCountList);
		}
		if (redirect != null) {
			res.sendRedirect(redirect);
		}

	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		HttpSession oHttpSession = getRenderRequestSession(renderRequest);
		if (oHttpSession.getAttribute("entryId") != null) {
			// values from session
			Long entryId = (Long) oHttpSession.getAttribute("entryId");
			String className = (String) oHttpSession.getAttribute("className");
			String sType = (String) oHttpSession.getAttribute("sType");
			String[] checklist = checkStringArraySessionAttribute(oHttpSession, "checklist");
			String simMeasure = checkStringSessionAttribute(oHttpSession, "simMeasure");
			ArrayList<Hashtable<Long, Double>> mySortetValueList = (ArrayList<Hashtable<Long, Double>>) oHttpSession.getAttribute("mySortetValueList");
			ArrayList<Hashtable<Long, Date>> mySortetDateList = (ArrayList<Hashtable<Long, Date>>) oHttpSession.getAttribute("mySortetDateList");
			ArrayList<Hashtable<Long, Integer>> mySortetViewCountList = (ArrayList<Hashtable<Long, Integer>>) oHttpSession
					.getAttribute("mySortetViewCountList");

			TagsAsset entry = TagsAssetLocalServiceUtil.getAsset(className, Long.valueOf(entryId));

			renderRequest.setAttribute("entry", entry);
			// set Attributes for render page
			renderRequest.setAttribute("entryId", String.valueOf(entryId));
			renderRequest.setAttribute("className", className);
			renderRequest.setAttribute("simValueList", mySortetValueList);
			renderRequest.setAttribute("simDateList", mySortetDateList);
			renderRequest.setAttribute("simViewCountList", mySortetViewCountList);
			renderRequest.setAttribute("sType", sType);
			renderRequest.setAttribute("checklist", checklist);
			renderRequest.setAttribute("simMeasure", simMeasure);
			return mapping.findForward(getForward(renderRequest, "portlet.ext.similarity.view_sim"));

		}

		return mapping.findForward("portlet.ext.similarity.view");

	}

	/**
	 * Validate if string[] attribute at http session is set.
	 * 
	 * @param oHttpSession
	 * @param sessionAttribute
	 *            name for session attribute
	 * @return String array from session or empty string array
	 */
	private String[] checkStringArraySessionAttribute(HttpSession oHttpSession, String sessionAttribute) {
		String[] attribute;
		if (oHttpSession.getAttribute(sessionAttribute) != null) {
			attribute = (String[]) oHttpSession.getAttribute(sessionAttribute);
		} else {
			attribute = new String[] {};
		}
		return attribute;
	}

	/**
	 * Validate if string attribute at http session is set.
	 * 
	 * @param oHttpSession
	 * @param sessionAttribute
	 *            name for session attribute
	 * @return String from session or empty string
	 */
	private String checkStringSessionAttribute(HttpSession oHttpSession, String sessionAttribute) {
		String attribute;
		if (oHttpSession.getAttribute(sessionAttribute) != null) {
			attribute = (String) oHttpSession.getAttribute(sessionAttribute);
		} else {
			attribute = new String();
		}
		return attribute;
	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		return _CHECK_METHOD_ON_PROCESS_ACTION;
	}

	private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;

	/**
	 * Get the httpSession from renderRequest to get data from user session in
	 * render method.
	 * 
	 * @param renderRequest
	 * @return httpSession with user session.
	 */
	private HttpSession getRenderRequestSession(RenderRequest renderRequest) {
		RenderRequest oActionRequestImpl = (RenderRequest) renderRequest;
		PortletSession oPortletSession = oActionRequestImpl.getPortletSession(false);
		PortletSessionImpl oPortletSessionImpl = (PortletSessionImpl) oPortletSession;
		HttpSession oHttpSession = oPortletSessionImpl.getHttpSession();
		return oHttpSession;
	}

	/**
	 * Get the httpSession from actionRequest to add data to user session in
	 * processAction method.
	 * 
	 * @param req
	 * @return httpSession with user session.
	 */
	public static HttpSession getActionRequestSession(ActionRequest req) {
		ActionRequestImpl oActionRequestImpl = (ActionRequestImpl) req;
		PortletSession oPortletSession = oActionRequestImpl.getPortletSession(false);
		PortletSessionImpl oPortletSessionImpl = (PortletSessionImpl) oPortletSession;
		HttpSession oHttpSession = oPortletSessionImpl.getHttpSession();
		return oHttpSession;
	}

}
