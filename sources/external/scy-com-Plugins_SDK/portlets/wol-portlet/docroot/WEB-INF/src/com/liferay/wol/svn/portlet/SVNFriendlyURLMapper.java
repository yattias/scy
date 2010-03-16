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

package com.liferay.wol.svn.portlet;

import com.liferay.portal.kernel.portlet.BaseFriendlyURLMapper;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayWindowState;

import java.util.Map;

import javax.portlet.PortletMode;

/**
 * <a href="SVNFriendlyURLMapper.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SVNFriendlyURLMapper extends BaseFriendlyURLMapper {

	public String buildPath(LiferayPortletURL portletURL) {
		return null;
	}

	public String getMapping() {
		return _MAPPING;
	}

	public String getPortletId() {
		return _PORTLET_ID;
	}

	public void populateParams(
		String friendlyURLPath, Map<String, String[]> params) {

		int x = friendlyURLPath.indexOf("/", 1);

		int y = friendlyURLPath.indexOf("/", x + 1);

		if (y == -1) {
			return;
		}

		String rss = friendlyURLPath.substring(x + 1, y);

		if (!rss.equals("rss")) {
			return;
		}

		x = friendlyURLPath.indexOf("/", x + 1);

		if (x == -1) {
			return;
		}

		y = friendlyURLPath.indexOf("/", x + 1);

		if (y == -1) {
			return;
		}

		String type = friendlyURLPath.substring(x + 1, y);

		x = friendlyURLPath.indexOf("/", x + 1);

		if (x == -1) {
			return;
		}

		String url = friendlyURLPath.substring(x);

		if (!url.equals("/plugins/trunk") &&
			!url.equals("/portal/trunk")) {

			return;
		}

		addParam(params, "p_p_id", _PORTLET_ID);
		addParam(params, "p_p_lifecycle", "0");
		addParam(params, "p_p_state", LiferayWindowState.EXCLUSIVE);
		addParam(params, "p_p_mode", PortletMode.VIEW);

		addParam(params, "url", url);
		addParam(params, "all", type.equals("all"));
	}

	private static final String _MAPPING = "svn";

	private static final String _PORTLET_ID = "2_WAR_wolportlet";

}