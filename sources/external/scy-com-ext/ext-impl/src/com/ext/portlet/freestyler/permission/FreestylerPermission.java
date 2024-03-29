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

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.liferay.portal.PortalException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.journal.model.JournalArticle;

/**
 * Permission class for freestyler portlet. Check that current user from portlet
 * view has permission or not.
 * 
 * @author Daniel
 * 
 */
public class FreestylerPermission {

	public static void check(PermissionChecker permissionChecker, long groupId, String actionId) throws PortalException {

		if (!contains(permissionChecker, groupId, actionId)) {
			throw new PrincipalException();
		}
	}

	public static boolean contains(PermissionChecker permissionChecker, long groupId, String actionId) {

		return permissionChecker.hasPermission(groupId, "com.ext.portlet.freestyler", groupId, actionId);
	}

	public static boolean contains(PermissionChecker permissionChecker, FreestylerEntry entry, String actionId) {

		if (permissionChecker.hasOwnerPermission(entry.getCompanyId(), JournalArticle.class.getName(), entry.getFreestylerId(), entry.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(entry.getGroupId(), FreestylerEntry.class.getName(), entry.getFreestylerId(), actionId);
	}

}