package com.liferay.portlet.imagegallery.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.cart.model.CartEntry;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.util.CartActionUtil;

/**
 * This class is for adding new cart entry to user session. It is fired after
 * pressing the taglib ui add_to_cart. The current entered cart entry can be
 * shown in cart portlet.
 * 
 * @author Daniel
 * 
 */
public class AddCartAction extends PortletAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		long entryId = Long.valueOf(req.getParameter("entryId"));		
		String redirect = req.getParameter("redirect");

		CartEntry cartEntry = CartActionUtil.cartEntryElement(req, IGImage.class.getName(), entryId);
		CartActionUtil.addNewAddToCartElement(req, cartEntry);

		res.sendRedirect(redirect);
	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		return mapping.findForward(getForward(renderRequest, "portlet.image_gallery.view"));
	}

}
