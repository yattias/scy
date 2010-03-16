package com.ext.portlet.freestyler.service.http;

import com.ext.portlet.freestyler.model.FreestylerImage;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;
import java.util.List;


/**
 * <a href="FreestylerImageJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.freestyler.service.http.FreestylerImageServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.http.FreestylerImageServiceJSON
 *
 */
public class FreestylerImageJSONSerializer {
    public static JSONObject toJSONObject(FreestylerImage model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();

        jsonObj.put("imageId", model.getImageId());
        jsonObj.put("groupId", model.getGroupId());
        jsonObj.put("companyId", model.getCompanyId());
        jsonObj.put("userId", model.getUserId());

        Date createDate = model.getCreateDate();

        String createDateJSON = StringPool.BLANK;

        if (createDate != null) {
            createDateJSON = String.valueOf(createDate.getTime());
        }

        jsonObj.put("createDate", createDateJSON);

        Date modifiedDate = model.getModifiedDate();

        String modifiedDateJSON = StringPool.BLANK;

        if (modifiedDate != null) {
            modifiedDateJSON = String.valueOf(modifiedDate.getTime());
        }

        jsonObj.put("modifiedDate", modifiedDateJSON);
        jsonObj.put("freestylerId", model.getFreestylerId());
        jsonObj.put("folderId", model.getFolderId());
        jsonObj.put("name", model.getName());
        jsonObj.put("description", model.getDescription());
        jsonObj.put("smallImageId", model.getSmallImageId());
        jsonObj.put("largeImageId", model.getLargeImageId());
        jsonObj.put("custom1ImageId", model.getCustom1ImageId());
        jsonObj.put("custom2ImageId", model.getCustom2ImageId());

        return jsonObj;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.freestyler.model.FreestylerImage[] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (FreestylerImage model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.freestyler.model.FreestylerImage[][] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (FreestylerImage[] model : models) {
            jsonArray.put(toJSONArray(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        List<com.ext.portlet.freestyler.model.FreestylerImage> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (FreestylerImage model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }
}
