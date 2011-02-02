package com.ext.portlet.bookreports.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.struts.PortletAction;

public class AddBookAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form,
			PortletConfig portletConfig, ActionRequest req, ActionResponse res)
			throws Exception {
		String title = req.getParameter("title");
		String redirect = req.getParameter("redirect") + "&title=" + title;
		String error = req.getParameter("error");
		String cmd = ParamUtil.getString(req, Constants.CMD);
		if (cmd.equals(Constants.EDIT)) {
			BookLocalServiceUtil.updateBook(req);
			res.sendRedirect(redirect);
		} else if (cmd.equals(Constants.DELETE)) {
			BookLocalServiceUtil.deleteBook(req);
		} else if (cmd.equals(Constants.ADD)) {

			if (Validator.isNull(title)) {
				res.sendRedirect(error);
				// setForward(req, "portlet.ext.book_reports.error");
			} else {
				req.setAttribute("title", title);
				BookLocalServiceUtil.addBook(req);
				res.sendRedirect(redirect);
				// setForward(req, "portlet.ext.book_reports.success");
			}
		}
	}

	public ActionForward render(ActionMapping mapping, ActionForm form,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse) throws Exception {
		String title = renderRequest.getParameter("title");
		if (title == null) {
			setForward(renderRequest, "portlet.ext.book_reports.view_books");
		}
		return mapping.findForward(getForward(renderRequest,
				"portlet.ext.book_reports.view"));
	}

}
