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

package com.ext.portlet.assetviewbyratio.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.resourcehandling.ResourceTypeList;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.ratings.model.RatingsEntry;
import com.liferay.portlet.ratings.service.RatingsEntryLocalServiceUtil;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.TagsAssetComparator;

/**
 * View last 5 tagsAsset with max ratio.
 * 
 * @author Daniel
 * 
 */
public class ViewAction extends PortletAction {

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

		Group group = GroupLocalServiceUtil.getGroup(themeDisplay.getScopeGroupId());

		User user = themeDisplay.getUser();

		List<TagsAsset> allUserAllowedEntries = new ArrayList<TagsAsset>();
		List<TagsAsset> allAssetEntries = TagsAssetLocalServiceUtil.getTagsAssets(0, TagsAssetLocalServiceUtil.getTagsAssetsCount());
		PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();

		for (TagsAsset tagsAsset : allAssetEntries) {
			String className = ClassNameLocalServiceUtil.getClassName(tagsAsset.getClassNameId()).getClassName();
			if (group.getGroupId() == tagsAsset.getGroupId() && permissionChecker.hasPermission(tagsAsset.getGroupId(), className, tagsAsset.getClassPK(), ActionKeys.VIEW)) {
				allUserAllowedEntries.add(tagsAsset);
			}
		}

		Vector<Class<?>> allowedClassesList = ResourceTypeList.getAllSingleValueClasses();
		List<TagsAsset> allAllowdAssetEntries = new ArrayList<TagsAsset>();

		for (TagsAsset assetEntry : allUserAllowedEntries) {
			for (Class<?> allowedClass : allowedClassesList) {
				if (assetEntry.getClassName().equals(allowedClass.getName())) {
					List<RatingsEntry> listRatingsTag0;
					try {
						listRatingsTag0 = RatingsEntryLocalServiceUtil.getEntries(assetEntry.getClassName(), assetEntry.getClassPK());
						if (listRatingsTag0.size() > 0) {
							allAllowdAssetEntries.add(assetEntry);
						}

					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

		Comparator<TagsAsset> comparator = new TagsAssetComparator();
		java.util.Collections.sort(allAllowdAssetEntries, comparator);

		for (TagsAsset tagsAsset : allAllowdAssetEntries) {
			System.out.println(tagsAsset.getTitle());
			List<RatingsEntry> listRatingsTag0;
			Double ratioTag0 = 0.0;
			try {
				listRatingsTag0 = RatingsEntryLocalServiceUtil.getEntries(tagsAsset.getClassName(), tagsAsset.getClassPK());
				if (listRatingsTag0.size() > 0) {
					for (RatingsEntry ratingsEntryTag0 : listRatingsTag0) {
						if (ratioTag0 == 0.0) {
							ratioTag0 += ratingsEntryTag0.getScore();
						} else {
							ratioTag0 = (ratioTag0 + ratingsEntryTag0.getScore()) / 2;
						}
					}
				}
				System.out.println(ratioTag0);

			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		renderRequest.setAttribute("allAllowdAssetEntries", allAllowdAssetEntries);

		return mapping.findForward("portlet.ext.asset_by_ratio.view");

	}

}