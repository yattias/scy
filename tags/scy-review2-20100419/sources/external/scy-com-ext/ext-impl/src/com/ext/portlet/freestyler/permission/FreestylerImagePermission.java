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

package com.ext.portlet.freestyler.permission;

import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * Permission class for freestyler image. Check that current user from portlet
 * view has permission or not.
 * 
 * @author Daniel
 * 
 */
public class FreestylerImagePermission {

	public static void check(PermissionChecker permissionChecker, long imageId, String actionId) throws PortalException, SystemException {

		if (!contains(permissionChecker, imageId, actionId)) {
			throw new PrincipalException();
		}
	}

	public static void check(PermissionChecker permissionChecker, FreestylerImage image, String actionId) throws PortalException {

		if (!contains(permissionChecker, image, actionId)) {
			throw new PrincipalException();
		}
	}

	public static boolean contains(PermissionChecker permissionChecker, long imageId, String actionId) throws PortalException, SystemException {

		FreestylerImage image = FreestylerImageLocalServiceUtil.getFreestylerImage(imageId);

		return contains(permissionChecker, image, actionId);
	}

	public static boolean contains(PermissionChecker permissionChecker, FreestylerImage image, String actionId) {

		if (permissionChecker.hasOwnerPermission(image.getCompanyId(), FreestylerImage.class.getName(), image.getImageId(), image.getUserId(), actionId)) {

			return true;
		}

		FreestylerFolder folder = image.getFolder();

		return permissionChecker.hasPermission(folder.getGroupId(), FreestylerImage.class.getName(), image.getImageId(), actionId);
	}

}