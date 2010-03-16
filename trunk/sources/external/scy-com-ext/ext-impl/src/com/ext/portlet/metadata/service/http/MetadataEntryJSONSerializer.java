package com.ext.portlet.metadata.service.http;

import com.ext.portlet.metadata.model.MetadataEntry;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;
import java.util.List;


/**
 * <a href="MetadataEntryJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.metadata.service.http.MetadataEntryServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.metadata.service.http.MetadataEntryServiceJSON
 *
 */
public class MetadataEntryJSONSerializer {
    public static JSONObject toJSONObject(MetadataEntry model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();

        jsonObj.put("entryId", model.getEntryId());
        jsonObj.put("groupId", model.getGroupId());
        jsonObj.put("companyId", model.getCompanyId());

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
        jsonObj.put("assertEntryId", model.getAssertEntryId());
        jsonObj.put("dc_contributor", model.getDc_contributor());
        jsonObj.put("dc_coverage", model.getDc_coverage());
        jsonObj.put("dc_creator", model.getDc_creator());
        jsonObj.put("dc_date", model.getDc_date());
        jsonObj.put("dc_description", model.getDc_description());
        jsonObj.put("dc_format", model.getDc_format());
        jsonObj.put("dc_identifier", model.getDc_identifier());
        jsonObj.put("dc_language", model.getDc_language());
        jsonObj.put("dc_publisher", model.getDc_publisher());
        jsonObj.put("dc_relation", model.getDc_relation());
        jsonObj.put("dc_rights", model.getDc_rights());
        jsonObj.put("dc_source", model.getDc_source());
        jsonObj.put("dc_subject", model.getDc_subject());
        jsonObj.put("dc_title", model.getDc_title());
        jsonObj.put("dc_type", model.getDc_type());

        return jsonObj;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.metadata.model.MetadataEntry[] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (MetadataEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.metadata.model.MetadataEntry[][] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (MetadataEntry[] model : models) {
            jsonArray.put(toJSONArray(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        List<com.ext.portlet.metadata.model.MetadataEntry> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (MetadataEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }
}
