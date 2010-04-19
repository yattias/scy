package com.ext.portlet.assetviewbyratio.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.metadata.NoSuchEntryException;
import com.ext.portlet.metadata.model.MetadataEntry;
import com.ext.portlet.metadata.service.MetadataEntryLocalServiceUtil;
import com.ext.portlet.resourcehandling.ResourceTypeList;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.ActionRequestImpl;
import com.liferay.portlet.PortletSessionImpl;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.model.TagsEntry;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil;
import com.liferay.util.TagsAssetDateComparator;
import com.liferay.util.TagsAssetRatioComparator;
import com.liferay.util.TagsAssetViewComparator;

/**
 * The portlet shows top 5 content.
 * 
 * @author Daniel
 * 
 */
public class ViewAction extends PortletAction {

	public final static String[] APPROVED_COMMUNITIES = { "Guest" };
	public final static String MOST_RECENT = "mostRecent";
	public final static String LATEST_DATE = "latestDate";
	public final static String MOST_VIEWS = "mostViews";
	public final static String AGE_10_12 = "1012";
	public final static String AGE_12_14 = "1214";
	public final static String AGE_14_16 = "1416";
	public final static String AGE_16_18 = "1618";
	public final static String LANGUAGE_ALL = "all";

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		String assetSortType = req.getParameter("assetSortType");
		String assetLanguageType = req.getParameter("assetLanguageType");
		String assetContentType = req.getParameter("assetContentType");
		String[] metadataAgeChecklist = req.getParameterValues("metadataAgeChecklist");
		Boolean showDurable = Boolean.valueOf(req.getParameter("showDurable"));
		Boolean changeDurable = Boolean.valueOf(req.getParameter("changeDurable"));

		HttpSession oHttpSession = getActionRequestSession(req);

		if (assetSortType != null) {
			oHttpSession.setAttribute("assetSortType", assetSortType);
		}

		if (assetLanguageType != null) {
			oHttpSession.setAttribute("assetLanguageType", assetLanguageType);
		}

		if (assetContentType != null) {
			oHttpSession.setAttribute("assetContentType", assetContentType);
		}

		if (metadataAgeChecklist != null) {
			oHttpSession.setAttribute("metadataAgeChecklist", metadataAgeChecklist);
		}

		if (changeDurable != null && changeDurable == true && showDurable != null) {
			oHttpSession.setAttribute("showDurable", showDurable);
				if(showDurable == true){
					
				try {

					Thread.sleep(2000);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		
		}

		// start sim calculation and set parameter to user session
	}

	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		HttpSession oHttpSession = getRenderRequestSession(renderRequest);
		String assetSortType = (String) oHttpSession.getAttribute("assetSortType");
		String assetLanguageType = (String) oHttpSession.getAttribute("assetLanguageType");
		String assetContentType = (String) oHttpSession.getAttribute("assetContentType");
		String[] metadataAgeChecklist = (String[]) oHttpSession.getAttribute("metadataAgeChecklist");
		Boolean showDurable = (Boolean) oHttpSession.getAttribute("showDurable");

		String sortBy = "";

		sortBy = getAssetSortType(assetSortType);

		if (metadataAgeChecklist == null) {
			metadataAgeChecklist = new String[] {};
		}
		if (showDurable == null) {
			showDurable = false;
		}
		if (assetLanguageType == null) {
			assetLanguageType = LANGUAGE_ALL;
		}
		if (assetContentType == null) {
			assetContentType = "allContent";
		}

		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		Group group = GroupLocalServiceUtil.getGroup(themeDisplay.getScopeGroupId());

		// get all asset entries from db
		List<TagsAsset> allAssetEntries = TagsAssetLocalServiceUtil.getTagsAssets(0, TagsAssetLocalServiceUtil.getTagsAssetsCount());
		List<TagsAsset> allUserAllowedEntries = getAllUserAllowedEntries(group, allAssetEntries);
		List<TagsAsset> allAllowdAssetEntries = getAllAllowedAssetTypesEntries(allUserAllowedEntries);

		// add only tagsAsset with tagsEntry approved if in APPROVED_COMMUNITIES
		for (int i = 0; i < APPROVED_COMMUNITIES.length; i++) {

			if (group.getName().equals(APPROVED_COMMUNITIES[i])) {
				List<TagsAsset> allApprovedAssetEntries = new ArrayList<TagsAsset>();
				for (TagsAsset tagsAsset : allAllowdAssetEntries) {
					List<TagsEntry> tagsEntries = TagsEntryLocalServiceUtil.getEntries(tagsAsset.getClassNameId(), tagsAsset.getClassPK());
					for (TagsEntry tagsEntry : tagsEntries) {
						if (tagsEntry.getName().equals("approved")) {
							allApprovedAssetEntries.add(tagsAsset);
						}
					}
				}
				allAllowdAssetEntries = allApprovedAssetEntries;
			}
		}

		// only limit assetEntries if user check more than one or not all age
		// checkboxes
		if (metadataAgeChecklist.length > 0 && metadataAgeChecklist.length < 4) {
			allAllowdAssetEntries = getAllMetadataAgeAssetEntries(metadataAgeChecklist, allAllowdAssetEntries);
		}

		if (!assetLanguageType.equals(LANGUAGE_ALL)) {
			allAllowdAssetEntries = getAllMetadataLanguageAssetEntries(assetLanguageType, allAllowdAssetEntries);
		}

		if (!assetContentType.equals("allContent")) {
			allAllowdAssetEntries = getAllMetadataContentAssetEntries(assetContentType, allAllowdAssetEntries);
		}

		// choose comparator
		Comparator<TagsAsset> comparator = null;
		if (sortBy.equals(MOST_RECENT)) {
			comparator = new TagsAssetRatioComparator();
		} else if (sortBy.equals(LATEST_DATE)) {
			comparator = new TagsAssetDateComparator();
		} else if (sortBy.equals(MOST_VIEWS)) {
			comparator = new TagsAssetViewComparator();
		} else {
			comparator = new TagsAssetRatioComparator();

		}

		// sort list
		java.util.Collections.sort(allAllowdAssetEntries, comparator);

		renderRequest.setAttribute("assetSortType", sortBy);
		renderRequest.setAttribute("assetLanguageType", assetLanguageType);
		renderRequest.setAttribute("assetContentType", assetContentType);
		renderRequest.setAttribute("metadataAgeChecklist", metadataAgeChecklist);
		renderRequest.setAttribute("showDurable", showDurable);
		renderRequest.setAttribute("allAllowdAssetEntries", allAllowdAssetEntries);

		return mapping.findForward("portlet.ext.asset_by_ratio.view");

	}

	/**
	 * Get all tags assets where DC_type equals selected content type at filter
	 * in user interface.
	 * 
	 * @param assetContentType
	 * @param allAllowdAssetEntries
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private List<TagsAsset> getAllMetadataContentAssetEntries(String assetContentType, List<TagsAsset> allAllowdAssetEntries) throws PortalException,
			SystemException {
		List<TagsAsset> allMetadataContentAssetEntries = new ArrayList<TagsAsset>();
		for (TagsAsset tagsAsset : allAllowdAssetEntries) {
			try {
				MetadataEntry metadataEntry = MetadataEntryLocalServiceUtil.getMetadataEntryByAssetId(tagsAsset.getClassPK());
				String metadataContent[] = metadataEntry.getDc_type().split(",");
				for (int i = 0; i < metadataContent.length; i++) {
					metadataContent[i] = metadataContent[i].replaceAll(" ", "");
					if (metadataContent[i].length() > 0 && metadataContent[i].contains(assetContentType)) {
						if (!allMetadataContentAssetEntries.contains(tagsAsset)) {
							allMetadataContentAssetEntries.add(tagsAsset);
						}
					}
				}

			} catch (NoSuchEntryException nsee) {

			}
		}
		return allMetadataContentAssetEntries;
	}

	/**
	 * Get all tags assets where DC_language equals selected language at filter
	 * in user interface.
	 * 
	 * @param assetLanguageType
	 * @param allAllowdAssetEntries
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private List<TagsAsset> getAllMetadataLanguageAssetEntries(String assetLanguageType, List<TagsAsset> allAllowdAssetEntries) throws PortalException,
			SystemException {
		List<TagsAsset> allMetadataLanguageAssetEntries = new ArrayList<TagsAsset>();
		for (TagsAsset tagsAsset : allAllowdAssetEntries) {
			try {
				MetadataEntry metadataEntry = MetadataEntryLocalServiceUtil.getMetadataEntryByAssetId(tagsAsset.getClassPK());
				if (metadataEntry.getDc_language().startsWith(assetLanguageType)) {
					allMetadataLanguageAssetEntries.add(tagsAsset);
				}

			} catch (NoSuchEntryException nsee) {

			}
		}
		return allMetadataLanguageAssetEntries;
	}

	/**
	 * Get all tags assets where DC_subject is at metadataAgeListVector.
	 * 
	 * @param metadataAgeChecklist
	 * @param allAllowdAssetEntries
	 * @return retrieve only entries where DC_subject is at age filter.
	 * @throws PortalException
	 * @throws SystemException
	 */
	private List<TagsAsset> getAllMetadataAgeAssetEntries(String[] metadataAgeChecklist, List<TagsAsset> allAllowdAssetEntries) throws PortalException,
			SystemException {
		Vector<String> metadataAgeListVector = new Vector<String>();
		if (metadataAgeChecklist != null) {
			for (int i = 0; i < metadataAgeChecklist.length; i++) {
				metadataAgeListVector.add(metadataAgeChecklist[i]);
			}
		}

		List<TagsAsset> allMetadataAssetEntries = new ArrayList<TagsAsset>();
		for (TagsAsset tagsAsset : allAllowdAssetEntries) {
			MetadataEntry metadataEntry = MetadataEntryLocalServiceUtil.getMetadataEntryByAssetId(tagsAsset.getClassPK());

			String metadataAge[] = metadataEntry.getDc_subject().split(",");
			for (int i = 0; i < metadataAge.length; i++) {
				metadataAge[i] = metadataAge[i].replaceAll(" ", "");
				if (metadataAge[i].length() > 0 && metadataAgeListVector.contains(metadataAge[i])) {
					if (!allMetadataAssetEntries.contains(tagsAsset)) {
						allMetadataAssetEntries.add(tagsAsset);
					}
				}

			}
		}
		return allMetadataAssetEntries;
	}

	/**
	 * Get only asset entries from classes defined at ResourceTypeList.
	 * 
	 * @param allUserAllowedEntries
	 *            all assetEntries where user has VIEW permission.
	 * @return only singleValueClasses.
	 */
	private List<TagsAsset> getAllAllowedAssetTypesEntries(List<TagsAsset> allUserAllowedEntries) {
		List<TagsAsset> allAllowdAssetEntries = new ArrayList<TagsAsset>();

		Vector<Class<?>> allowedClassesList = ResourceTypeList.getAllSingleValueClasses();
		// add only content with classes from specific classes
		for (TagsAsset assetEntry : allUserAllowedEntries) {
			for (Class<?> allowedClass : allowedClassesList) {
				if (assetEntry.getClassName().equals(allowedClass.getName())) {

					allAllowdAssetEntries.add(assetEntry);

				}
			}
		}
		return allAllowdAssetEntries;
	}

	/**
	 * get list of all tagsAsset where user has VIEW permission for and in same
	 * group(community) as theme display
	 * 
	 * @param group
	 * @param allAssetEntries
	 *            all assetEntries from db.
	 * @return only assetEntries where user has VIEW permission for and in same
	 *         group as viewed portlet.
	 * @throws PortalException
	 * @throws SystemException
	 */
	private List<TagsAsset> getAllUserAllowedEntries(Group group, List<TagsAsset> allAssetEntries) throws PortalException, SystemException {
		PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();
		List<TagsAsset> allUserAllowedEntries = new ArrayList<TagsAsset>();
		for (TagsAsset tagsAsset : allAssetEntries) {
			String className = ClassNameLocalServiceUtil.getClassName(tagsAsset.getClassNameId()).getClassName();
			if (group.getGroupId() == tagsAsset.getGroupId()
					&& permissionChecker.hasPermission(tagsAsset.getGroupId(), className, tagsAsset.getClassPK(), ActionKeys.VIEW)) {
				allUserAllowedEntries.add(tagsAsset);
			}
		}
		return allUserAllowedEntries;
	}

	private String getAssetSortType(String assetSortType) {
		String sortBy;
		if (assetSortType != null && assetSortType.equals(MOST_RECENT)) {
			sortBy = MOST_RECENT;
		} else if (assetSortType != null && assetSortType.equals(LATEST_DATE)) {
			sortBy = LATEST_DATE;
		} else if (assetSortType != null && assetSortType.equals(MOST_VIEWS)) {
			sortBy = MOST_VIEWS;
		} else {
			sortBy = MOST_RECENT;
		}
		return sortBy;
	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		return _CHECK_METHOD_ON_PROCESS_ACTION;
	}

	private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;

	/**
	 * Get the httpSession from renderRequest to get data from user session in
	 * render method.
	 * 
	 * @param renderRequest
	 * @return httpSession with user session.
	 */
	private HttpSession getRenderRequestSession(RenderRequest renderRequest) {
		RenderRequest oActionRequestImpl = (RenderRequest) renderRequest;
		PortletSession oPortletSession = oActionRequestImpl.getPortletSession(false);
		PortletSessionImpl oPortletSessionImpl = (PortletSessionImpl) oPortletSession;
		HttpSession oHttpSession = oPortletSessionImpl.getHttpSession();
		return oHttpSession;
	}

	/**
	 * Get the httpSession from actionRequest to add data to user session in
	 * processAction method.
	 * 
	 * @param req
	 * @return httpSession with user session.
	 */
	public static HttpSession getActionRequestSession(ActionRequest req) {
		ActionRequestImpl oActionRequestImpl = (ActionRequestImpl) req;
		PortletSession oPortletSession = oActionRequestImpl.getPortletSession(false);
		PortletSessionImpl oPortletSessionImpl = (PortletSessionImpl) oPortletSession;
		HttpSession oHttpSession = oPortletSessionImpl.getHttpSession();
		return oHttpSession;
	}
}