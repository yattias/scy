package com.liferay.util;

import java.util.Comparator;
import java.util.List;

import com.liferay.portal.SystemException;
import com.liferay.portlet.ratings.model.RatingsEntry;
import com.liferay.portlet.ratings.service.RatingsEntryLocalServiceUtil;
import com.liferay.portlet.tags.model.TagsAsset;

public class TagsAssetRatioComparator implements Comparator<TagsAsset> {

	public TagsAssetRatioComparator() {
	}

	@Override
	public int compare(TagsAsset tag0, TagsAsset tag1) {

		Double ratioTag0 = 0.0;
		Double ratioTag1 = 0.0;

		String classNameTag0 = tag0.getClassName();
		String classNameTag1 = tag1.getClassName();

		long classPKTag0 = tag0.getClassPK();
		long classPKTag1 = tag1.getClassPK();

		List<RatingsEntry> listRatingsTag0;
		List<RatingsEntry> listRatingsTag1;
		try {
			listRatingsTag0 = RatingsEntryLocalServiceUtil.getEntries(classNameTag0, classPKTag0);
			if (listRatingsTag0.size() > 0) {
				for (RatingsEntry ratingsEntryTag0 : listRatingsTag0) {
					if (ratioTag0 == 0.0) {
						ratioTag0 += ratingsEntryTag0.getScore();
					} else {
						ratioTag0 = (ratioTag0 + ratingsEntryTag0.getScore()) / 2;
					}
				}
			}

		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			listRatingsTag1 = RatingsEntryLocalServiceUtil.getEntries(classNameTag1, classPKTag1);
			if (listRatingsTag1.size() > 0) {
				for (RatingsEntry ratingsEntryTag1 : listRatingsTag1) {
					if (ratioTag1 == 0.0) {
						ratioTag1 += ratingsEntryTag1.getScore();
					} else {
						ratioTag1 = (ratioTag1 + ratingsEntryTag1.getScore()) / 2;
					}
				}
			}

		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ratioTag1.compareTo(ratioTag0);
	}
}
