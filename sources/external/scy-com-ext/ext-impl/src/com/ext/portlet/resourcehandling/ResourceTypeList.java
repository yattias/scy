package com.ext.portlet.resourcehandling;

import java.util.Vector;

import com.ext.portlet.resourcehandling.interfaces.AllowedResourceType;
import com.ext.portlet.resourcehandling.interfaces.MultivalueResource;
import com.ext.portlet.resourcehandling.interfaces.ResourceViewInterface;
import com.ext.portlet.resourcehandling.interfaces.SimilarityComparable;
import com.ext.portlet.resourcehandling.interfaces.SingleValueResource;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.portlet.tags.util.TagsUtil;
import com.liferay.portlet.wiki.model.WikiPage;

/**
 * Class to receive all allowed class names for resource types.
 * 
 * @author Daniel
 * 
 */
public class ResourceTypeList {

	public static Vector<Class<?>> resourceList;

	/**
	 * The all classes as vector that implement the ResourceViewInterface or are
	 * standard classes.
	 * 
	 * @return vector with all classes which are allowed to search at linked
	 *         tool
	 */
	public static Vector<Class<?>> getAllowedClassNameList() {
		resourceList = new Vector<Class<?>>();
		for (String className : TagsUtil.ASSET_TYPE_CLASS_NAMES) {
			try {
				resourceList.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Vector<Class<?>> allAssetTypes = getTagsAssetTypes();
		for (Class<?> tagsAssetClass : allAssetTypes) {
			if (ResourceViewInterface.class.isAssignableFrom(tagsAssetClass)) {
				resourceList.add(tagsAssetClass);
			}
		}

		return resourceList;
	}

	/**
	 * The all classes as vector that implement the MultiValuedResource and/or
	 * SingleValueResource interface or are standard classes. All founded
	 * classes where shown as entires in similarity portlet.
	 * 
	 * @see SimilarityComparable
	 * 
	 * @return vector with all classes which are allowed to search at linked
	 *         tool
	 */
	public static Vector<Class<?>> getAllSimilarityComparableClasses() {
		resourceList = new Vector<Class<?>>();
		for (String className : TagsUtil.ASSET_TYPE_CLASS_NAMES) {
			try {
				resourceList.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Vector<Class<?>> allAssetTypes = getTagsAssetTypes();
		for (Class<?> tagsAssetClass : allAssetTypes) {
			if (AllowedResourceType.class.isAssignableFrom(tagsAssetClass)) {
				resourceList.add(tagsAssetClass);
			}
		}

		return resourceList;
	}

	/**
	 * The all classes as vector that implement the SingleValueResource
	 * interface or are standard classes. All founded classes where shown as
	 * entires in similarity portlet.
	 * 
	 * @see SimilarityComparable
	 * 
	 * @return vector with all classes which are allowed to search at linked
	 *         tool
	 */
	public static Vector<Class<?>> getAllSingleValueClasses() {
		resourceList = new Vector<Class<?>>();
		for (String className : ASSET_TYPE_CLASS_NAMES) {
			try {
				resourceList.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Vector<Class<?>> allAssetTypes = getTagsAssetTypes();
		for (Class<?> tagsAssetClass : allAssetTypes) {
			if (SingleValueResource.class.isAssignableFrom(tagsAssetClass)) {
				resourceList.add(tagsAssetClass);
			}
		}

		return resourceList;
	}

	/**
	 * The all classes as vector that implement the MultiValueResource
	 * interface. All founded classes where shown as entires in similarity
	 * portlet.
	 * 
	 * @see SimilarityComparable
	 * 
	 * @return vector with all classes which are allowed to search at linked
	 *         tool
	 */
	public static Vector<Class<?>> getAllMultiValueClasses() {
		resourceList = new Vector<Class<?>>();
		
		Vector<Class<?>> allAssetTypes = getTagsAssetTypes();
		for (Class<?> tagsAssetClass : allAssetTypes) {
			if (MultivalueResource.class.isAssignableFrom(tagsAssetClass)) {
				resourceList.add(tagsAssetClass);
			}
		}

		return resourceList;
	}

	private static Vector<Class<?>> getTagsAssetTypes() {
		Vector<Class<?>> allAssetTypes = TagsAssetLocalServiceUtil.getTagsAssetTypes();
		return allAssetTypes;
	}
	
	public static final String[] ASSET_TYPE_CLASS_NAMES = {
		BookmarksEntry.class.getName(),	DLFileEntry.class.getName(), IGImage.class.getName(),
		JournalArticle.class.getName(), MBMessage.class.getName()		
	};

}
