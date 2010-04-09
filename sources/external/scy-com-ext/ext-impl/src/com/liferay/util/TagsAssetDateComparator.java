package com.liferay.util;

import java.util.Comparator;
import java.util.Date;

import com.liferay.portlet.tags.model.TagsAsset;

public class TagsAssetDateComparator implements Comparator<TagsAsset> {

	public TagsAssetDateComparator() {
	}

	@Override
	public int compare(TagsAsset tag0, TagsAsset tag1) {

		Date date0 = tag0.getModifiedDate();
		Date date1 = tag1.getModifiedDate();

		if (tag0.getModifiedDate() == null) {
			date0 = tag0.getCreateDate();
		}

		if (tag1.getModifiedDate() == null) {
			date1 = tag0.getCreateDate();
		}

		return date1.compareTo(date0);
	}
}
