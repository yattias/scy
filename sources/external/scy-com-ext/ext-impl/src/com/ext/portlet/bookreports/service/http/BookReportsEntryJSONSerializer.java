package com.ext.portlet.bookreports.service.http;

import com.ext.portlet.bookreports.model.BookReportsEntry;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;
import java.util.List;


/**
 * <a href="BookReportsEntryJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.bookreports.service.http.BookReportsEntryServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.bookreports.service.http.BookReportsEntryServiceJSON
 *
 */
public class BookReportsEntryJSONSerializer {
    public static JSONObject toJSONObject(BookReportsEntry model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();

        jsonObj.put("uuid", model.getUuid());
        jsonObj.put("entryId", model.getEntryId());
        jsonObj.put("groupId", model.getGroupId());
        jsonObj.put("companyId", model.getCompanyId());
        jsonObj.put("userId", model.getUserId());
        jsonObj.put("userName", model.getUserName());

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
        jsonObj.put("name", model.getName());
        jsonObj.put("title", model.getTitle());

        return jsonObj;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.bookreports.model.BookReportsEntry[] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (BookReportsEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.bookreports.model.BookReportsEntry[][] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (BookReportsEntry[] model : models) {
            jsonArray.put(toJSONArray(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        List<com.ext.portlet.bookreports.model.BookReportsEntry> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (BookReportsEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }
}
