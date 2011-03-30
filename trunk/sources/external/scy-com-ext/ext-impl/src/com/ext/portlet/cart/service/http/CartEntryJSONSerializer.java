package com.ext.portlet.cart.service.http;

import com.ext.portlet.cart.model.CartEntry;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;
import java.util.List;


/**
 * <a href="CartEntryJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.cart.service.http.CartEntryServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.cart.service.http.CartEntryServiceJSON
 *
 */
public class CartEntryJSONSerializer {
    public static JSONObject toJSONObject(CartEntry model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();

        jsonObj.put("cartEntryId", model.getCartEntryId());
        jsonObj.put("groupId", model.getGroupId());
        jsonObj.put("companyId", model.getCompanyId());
        jsonObj.put("userId", model.getUserId());
        jsonObj.put("resourceId", model.getResourceId());
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
        jsonObj.put("tagNames", model.getTagNames());
        jsonObj.put("resourceType", model.getResourceType());

        return jsonObj;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.cart.model.CartEntry[] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (CartEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.cart.model.CartEntry[][] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (CartEntry[] model : models) {
            jsonArray.put(toJSONArray(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        List<com.ext.portlet.cart.model.CartEntry> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (CartEntry model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }
}