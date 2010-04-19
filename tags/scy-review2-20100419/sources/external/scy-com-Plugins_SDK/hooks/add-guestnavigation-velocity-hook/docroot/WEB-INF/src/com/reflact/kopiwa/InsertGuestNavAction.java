package com.reflact.kopiwa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.model.impl.LayoutImpl;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.service.persistence.UserGroupFinderImpl;
import com.liferay.portal.service.persistence.UserGroupFinderUtil;
import com.liferay.portal.theme.NavItem;
import com.liferay.portal.theme.RequestVars;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;



/**
 * Action Listener for PreServletActions.<br />
 * run(req, res) will be called before a ServletAction takes place.
 * 
 * @author Gunnar Meyer
 */
public class InsertGuestNavAction extends Action {
	
	HttpServletRequest req;
	HttpServletResponse res;
	
	public void run(HttpServletRequest req, HttpServletResponse res)
	throws ActionException {
		this.req =req;
		this.res = res;
		
		injectVmVariables();
	}
	
	/**
	 * Prepares a couple of Objects for the velocity context.
	 * 
	 * @see com.liferay.portal.velocity.VelocityVariables
	 */
	@SuppressWarnings("unchecked")
	private void injectVmVariables() {
		ThemeDisplay themeDisplay = (ThemeDisplay)req.getAttribute(WebKeys.THEME_DISPLAY);

		try {
			long guestGroupId = 0;
			try {
				guestGroupId = GroupServiceUtil.getGroup(themeDisplay.getCompanyId(), GroupConstants.GUEST).getGroupId();
			} catch(NullPointerException e) {
				//We assume that this is the first start of the portal, and the guest group does not exist yet.
				//So we just ignore it an go ahead.
				return;
			}
			long currentGroupId = themeDisplay.getScopeGroupId();
			
			//is the current community the guest community?
			boolean isGuestCommunity = (guestGroupId == currentGroupId);
			
			//build NavItems of guest community
			List<Layout> guestLayouts = new LinkedList<Layout>(); 
			for(Layout l : LayoutLocalServiceUtil.getLayouts(guestGroupId, false)) {
				
				if(l.isRootLayout()) {
					guestLayouts.add(l);
				
				}
			}
			

			RequestVars requestVars = new RequestVars(
					req, themeDisplay, guestLayouts.get(0).getAncestorPlid(),
					guestLayouts.get(0).getAncestorLayoutId());
			List<NavItem> guestNavItems = NavItem.fromLayouts(requestVars, guestLayouts);
			
			
			//NavItem of the current page's root ancestor
			Layout rootLayout = themeDisplay.getLayout();
			while(!rootLayout.isRootLayout()) {
				rootLayout = LayoutLocalServiceUtil.getLayout(rootLayout.getAncestorPlid());
			}
			NavItem currentPageRootNavItem = NavItem.fromLayout(requestVars, rootLayout);
			

	
			//put the stuff where liferay will find it in order to put it into the velocity context
			Map vmVariables = (Map)req.getAttribute(WebKeys.VM_VARIABLES);
			if (vmVariables == null) {
				vmVariables = new HashMap();
			}
			
			
			//get the current layout, user and group
			ThemeDisplay td = (ThemeDisplay)req.getAttribute("THEME_DISPLAY");
			Layout la = td.getLayout();
			User user = td.getUser();
			Group group = la.getGroup();
			
			
			//The layouts needed to bring public and private pages in one navigation.
			ArrayList<Layout>privateLayouts = new ArrayList<Layout>();
			ArrayList<Layout>publicLayouts = new ArrayList<Layout>();
			
			for(Layout l : LayoutLocalServiceUtil.getLayouts(group.getGroupId(), false)) {
				if(l.isRootLayout()) {
					
					publicLayouts.add(l);
					
				}
			}
			//check if user is in group, if not ArrayList will be empty 
			if(GroupServiceUtil.hasUserGroup(user.getUserId(),group.getGroupId()))
			{		
				
				for(Layout l : LayoutLocalServiceUtil.getLayouts(group.getGroupId(), true)) {
					if(l.isRootLayout()) {
						
						privateLayouts.add(l);
						
					}
				}
			}
			
		//create the nav items of the layouts
		List<NavItem> publicItems = NavItem.fromLayouts(requestVars, publicLayouts);
		List<NavItem> privateItems = NavItem.fromLayouts(requestVars, privateLayouts);
			
			
			vmVariables.put("publicItems",publicItems);
			vmVariables.put("privateItems",privateItems);
			vmVariables.put("currentRootPage", currentPageRootNavItem);
			vmVariables.put("guestNavItems", guestNavItems);
			vmVariables.put("isGuestCommunity", isGuestCommunity);
						
			
			req.setAttribute(WebKeys.VM_VARIABLES, vmVariables);

		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
