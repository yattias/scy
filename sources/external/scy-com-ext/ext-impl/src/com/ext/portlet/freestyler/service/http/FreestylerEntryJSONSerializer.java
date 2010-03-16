package com.ext.portlet.freestyler.service.http;

import com.ext.portlet.freestyler.model.FreestylerEntry;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;
import java.util.List;


/**
 * <a href="FreestylerEntryJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.freestyler.service.http.FreestylerEntryServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.http.FreestylerEntryServiceJSON
 *
 */
public class FreestylerEntryJSONSerializer {
    public static JSONObject toJSONObject(FreestylerEntry model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();

        jsonObj.put("freestylerId", model.getFreestylerId());
        jsonObj.put("groupId", model.getGroupId());
        jsonObj.put("companyId", model.getCompanyId());
        jsonObj.put("userId", model.getUserId());
        jsonObj.put("name", model.getName());
        jsonObj.put("description", model.getDescription());
        jsonObj.put("xmlFileId", model.getXmlFileId());

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

        return jsonObj;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.freestyler.model.FreestylerEntry[] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (FreestylerEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.freestyler.model.FreestylerEntry[][] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (FreestylerEntry[] model : models) {
            jsonArray.put(toJSONArray(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        List<com.ext.portlet.freestyler.model.FreestylerEntry> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (FreestylerEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }
}
