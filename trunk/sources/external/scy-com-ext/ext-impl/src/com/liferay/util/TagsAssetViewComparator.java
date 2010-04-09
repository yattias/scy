package com.liferay.util;

import java.util.Comparator;

import com.liferay.portlet.tags.model.TagsAsset;

public class TagsAssetViewComparator implements Comparator<TagsAsset> {

	public TagsAssetViewComparator() {
	}

	@Override
	public int compare(TagsAsset tag0, TagsAsset tag1) {

		Integer view0 = tag0.getViewCount();
		Integer view1 = tag1.getViewCount();

		if (view0 == null) {
			view0 = 0;
		}

		if (view1 == null) {
			view1 = 0;
		}

		return view1.compareTo(view0);
	}
}
