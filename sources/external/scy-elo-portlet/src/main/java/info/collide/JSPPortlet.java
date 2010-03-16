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

package info.collide;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.RemoteAccessException;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.search.SearchResult;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * <a href="JSPPortlet.java.html"><b><i>View Source</i></b></a>
 * 
 * @author Brian Wing Shun Chan
 * 
 */
public class JSPPortlet extends GenericPortlet {

	private IRepository repository;
	private IMetadataTypeManager mtm;
	private IExtensionManager extMan;

	public void init() throws PortletException {
		editJSP = getInitParameter("edit-jsp");
		helpJSP = getInitParameter("help-jsp");
		viewJSP = getInitParameter("view-jsp");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		String[] locs = new String[] { "../beans.xml" };
		context.setConfigLocations(locs);
		context.refresh();
		repository = (IRepository) context.getBean("repository");
		mtm = (IMetadataTypeManager) context.getBean("metadataTypeManager");
		extMan = (IExtensionManager) context.getBean("extensionManager");
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

	@SuppressWarnings("unchecked")
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {

		renderResponse.setContentType("text/html");

		List<IELO> iEloList = (List<IELO>) renderRequest.getAttribute("iEloList");
		String eloUri = (String) renderRequest.getAttribute("eloUri");

		if (iEloList != null) {
			renderRequest.setAttribute("iEloList", iEloList);
			renderRequest.setAttribute("mtm", mtm);
			PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher("/viewISearchList.jsp");
			prd.include(renderRequest, renderResponse);
		} else if (iEloList == null && eloUri != null) {
			URI uri = URI.create(eloUri);
			IELO ielo = repository.retrieveELO(uri);
			String redirect = (String) renderRequest.getAttribute("redirect");
			renderRequest.setAttribute("mtm", mtm);
			renderRequest.setAttribute("ielo", ielo);
			renderRequest.setAttribute("redirect", redirect);
			PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher("/viewEloDetail.jsp");
			prd.include(renderRequest, renderResponse);

		} else {
			PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher("/view.jsp");
			prd.include(renderRequest, renderResponse);
		}
		// include(viewJSP, renderRequest, renderResponse);
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException, PortletException {

		String yourname = (String) actionRequest.getParameter("yourname");
		String redirect = actionRequest.getParameter("redirect");
		String eloUri = actionRequest.getParameter("eloUri");

		if (yourname == null) {
			yourname = "";
		}

		System.out.println(yourname);

		actionResponse.setRenderParameter("title", yourname);
		actionResponse.setRenderParameter("redirect", redirect);
		actionResponse.setRenderParameter("eloUri", eloUri);

	}

	@Override
	public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {

		List<ISearchResult> search = null;

		if (request.getParameter("eloUri") != null) {
			String eloUri = request.getParameter("eloUri");
			String redirect = request.getParameter("redirect");
			request.setAttribute("eloUri", eloUri);
			request.setAttribute("redirect", redirect);
		} else {
			try {
				search = repository.search(null);
				List<IELO> iEloList = new ArrayList<IELO>();

				for (int i = 0; i < search.size(); i++) {
					SearchResult res = (SearchResult) search.get(i);
					URI eloUri = res.getUri();
					IELO elo = (IELO) repository.retrieveELO(eloUri);
					iEloList.add(elo);
				}

				Comparator<IELO> comparator = new IEloDateComparator(mtm);
				java.util.Collections.sort(iEloList, comparator);

				request.setAttribute("iEloList", iEloList);
			} catch (RemoteAccessException rae) {
				System.err.println("Cant connect to roolo Server");
				System.out.println(rae.getMessage());
			}

		}

		super.render(request, response);
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