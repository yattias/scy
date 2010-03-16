package com.ext.portlet.freestyler.action;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
import com.liferay.portal.struts.PortletAction;
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
		FreestylerImage entry = FreestylerImageLocalServiceUtil.getFreestylerImage(entryId);
		String redirect = req.getParameter("redirect");

		CartEntry cartEntry = CartActionUtil.cartEntryElement(req, FreestylerImage.class.getName(), entryId);
		CartActionUtil.addNewAddToCartElement(req, cartEntry);

		int actualImageEntry = 0;

		List<FreestylerImage> listImages = FreestylerEntryLocalServiceUtil.getFreestylerImages(entry.getFreestylerId());
		for (FreestylerImage freestylerImage : listImages) {
			if (freestylerImage.getImageId() < entryId) {
				actualImageEntry++;
			}
		}

		res.sendRedirect(redirect);
	}
}
