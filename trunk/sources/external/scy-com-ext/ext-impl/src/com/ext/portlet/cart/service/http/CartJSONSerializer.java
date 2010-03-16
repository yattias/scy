package com.ext.portlet.cart.service.http;

import com.ext.portlet.cart.model.Cart;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;
import java.util.List;


/**
 * <a href="CartJSONSerializer.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.cart.service.http.CartServiceJSON</code>
 * to translate objects.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.cart.service.http.CartServiceJSON
 *
 */
public class CartJSONSerializer {
    public static JSONObject toJSONObject(Cart model) {
        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();

        jsonObj.put("cartId", model.getCartId());
        jsonObj.put("groupId", model.getGroupId());
        jsonObj.put("companyId", model.getCompanyId());
        jsonObj.put("userId", model.getUserId());
        jsonObj.put("title", model.getTitle());

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

        return jsonObj;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.cart.model.Cart[] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (Cart model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        com.ext.portlet.cart.model.Cart[][] models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (Cart[] model : models) {
            jsonArray.put(toJSONArray(model));
        }

        return jsonArray;
    }

    public static JSONArray toJSONArray(
        List<com.ext.portlet.cart.model.Cart> models) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

        for (Cart model : models) {
            jsonArray.put(toJSONObject(model));
        }

        return jsonArray;
    }
}
