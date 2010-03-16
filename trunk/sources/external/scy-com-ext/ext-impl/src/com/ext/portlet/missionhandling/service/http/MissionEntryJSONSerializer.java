package com.ext.portlet.missionhandling.service.http;

import com.ext.portlet.missionhandling.model.MissionEntry;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;
import java.util.List;


/**
 * <a href="MissionEntryJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.missionhandling.service.http.MissionEntryServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.missionhandling.service.http.MissionEntryServiceJSON
 *
 */
public class MissionEntryJSONSerializer {
    public static JSONObject toJSONObject(MissionEntry model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();

        jsonObj.put("missionEntryId", model.getMissionEntryId());
        jsonObj.put("companyId", model.getCompanyId());
        jsonObj.put("groupId", model.getGroupId());
        jsonObj.put("organizationId", model.getOrganizationId());

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

        Date endDate = model.getEndDate();

        String endDateJSON = StringPool.BLANK;

        if (endDate != null) {
            endDateJSON = String.valueOf(endDate.getTime());
        }

        jsonObj.put("endDate", endDateJSON);
        jsonObj.put("active", model.getActive());

        return jsonObj;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.missionhandling.model.MissionEntry[] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (MissionEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.missionhandling.model.MissionEntry[][] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (MissionEntry[] model : models) {
            jsonArray.put(toJSONArray(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        List<com.ext.portlet.missionhandling.model.MissionEntry> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (MissionEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }
}
