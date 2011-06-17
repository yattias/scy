/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package info.collide.portlet.customAsset;


import info.collide.portlet.customAsset.classes.DropdownBoxGenerator;
import info.collide.portlet.customAsset.classes.queryDatabase;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
/**
 * <a href="JSPPortlet.java.html"><b><i>View Source</i></b></a>
 * 
 * @author Brian Wing Shun Chan
 * 
 */
public class JSPPortlet extends GenericPortlet {
	
	public void init() throws PortletException {
		editJSP = getInitParameter("edit-jsp");
		helpJSP = getInitParameter("help-jsp");
		viewJSP = getInitParameter("view-jsp");
	}


	public void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {

		String jspPage = renderRequest.getParameter("jspPage");

		if (jspPage != null) {
			include(jspPage, renderRequest, renderResponse);
		} else {
			super.doDispatch(renderRequest, renderResponse);
		}
	}


	public void doEdit(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		
		if (renderRequest.getPreferences() == null) {
			super.doEdit(renderRequest, renderResponse);
		} else {
			include(editJSP, renderRequest, renderResponse);
		}
	}


	public void doHelp(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {

		include(helpJSP, renderRequest, renderResponse);
	}


	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {	

		User liferayUser = null;
		
		try{
			
			liferayUser = PortalUtil.getUser(renderRequest);
		} 
		catch(Exception e){
			System.out.println("customAsset-Portlet: doView: "+e);
		}			
		    
		ThemeDisplay td = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);  

		// Is the user a quest?
		if(liferayUser != null){  
		    String userId = String.valueOf(liferayUser.getUserId());
		    String companyId = String.valueOf(liferayUser.getCompanyId());
		    
		    String root = td.getPortalURL();
		    String pathMain = td.getPathMain();
		    String plid = String.valueOf(td.getPlid());
		    String imagePath = td.getPathImage();
		    String local = td.getLocale().toString();
		    String community = td.getURLCurrent();	    

		    if(community.contains("?")){
		    	community = community.substring(0, community.indexOf("?"));
		    }
		    
		    // Generate the Dropdown-Box Code
		    DropdownBoxGenerator d = new DropdownBoxGenerator();
		    d.setServer(root);
		    d.setLocal(local);
		    d.setCommunity(community); 
		    String dropbox = d.getDropdownBox();

			// Initialize the query.
			queryDatabase query = new queryDatabase();
			query.setRoot(root);
			query.setPathMain(pathMain);
			query.setPlid(plid);
			query.setImagePath(imagePath);
			query.setUserId(userId);
			query.setCompanyId(companyId);
			query.SetCommunity(community);
			query.setLanguage(local);

			// Start the query an get the results.
			String searchResults = query.getAllResults();

			// Generate the View.
			renderResponse.setContentType("text/html");
	 		PrintWriter out = renderResponse.getWriter();
	 		out.println(dropbox);
	 		out.println("<br>");	 		
	 		out.println(searchResults);
	 		
			//include(viewJSP, renderRequest, renderResponse);
	 		
		}
		else{
			// Generate the View.
			renderResponse.setContentType("text/html");
	 		PrintWriter out = renderResponse.getWriter();
	 		
	 		String output = td.translate("bad-request");
	 		
	 		out.println("<b>" + output +":</b><br>");
	 		
	 		output = td.translate("please-sign-in-to-access-this-application");
	 		
	 		// Adjust the German output.
	 		if(output.contains("zurück")){
	 			output = output.replace("rück", "");
	 		}
	 		
	 		out.println(output);
		}
	}


	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException, PortletException {
	}


	protected void include(String path, RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {	
	
		PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			_log.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	protected String editJSP;
	protected String helpJSP;
	protected String viewJSP;

	private static Log _log = LogFactoryUtil.getLog(JSPPortlet.class);

}