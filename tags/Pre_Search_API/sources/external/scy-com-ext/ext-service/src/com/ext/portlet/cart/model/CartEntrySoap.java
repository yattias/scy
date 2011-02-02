package com.ext.portlet.cart.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="CartEntrySoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.cart.service.http.CartEntryServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.cart.service.http.CartEntryServiceSoap
 *
 */
public class CartEntrySoap implements Serializable {
    private long _cartEntryId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private long _resourceId;
    private String _userName;
    private Date _createDate;
    private Date _modifiedDate;
    private String _tagNames;
    private String _resourceType;

    public CartEntrySoap() {
    }

    public static CartEntrySoap toSoapModel(CartEntry model) {
        CartEntrySoap soapModel = new CartEntrySoap();

        soapModel.setCartEntryId(model.getCartEntryId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setUserId(model.getUserId());
        soapModel.setResourceId(model.getResourceId());
        soapModel.setUserName(model.getUserName());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setTagNames(model.getTagNames());
        soapModel.setResourceType(model.getResourceType());

        return soapModel;
    }

    public static CartEntrySoap[] toSoapModels(CartEntry[] models) {
        CartEntrySoap[] soapModels = new CartEntrySoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static CartEntrySoap[][] toSoapModels(CartEntry[][] models) {
        CartEntrySoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new CartEntrySoap[models.length][models[0].length];
        } else {
            soapModels = new CartEntrySoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static CartEntrySoap[] toSoapModels(List<CartEntry> models) {
        List<CartEntrySoap> soapModels = new ArrayList<CartEntrySoap>(models.size());

        for (CartEntry model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new CartEntrySoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _cartEntryId;
    }

    public void setPrimaryKey(long pk) {
        setCartEntryId(pk);
    }

    public long getCartEntryId() {
        return _cartEntryId;
    }

    public void setCartEntryId(long cartEntryId) {
        _cartEntryId = cartEntryId;
    }

    public long getGroupId() {
        return _groupId;
    }

    public void setGroupId(long groupId) {
        _groupId = groupId;
    }

    public long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(long companyId) {
        _companyId = companyId;
    }

    public long getUserId() {
        return _userId;
    }

    public void setUserId(long userId) {
        _userId = userId;
    }

    public long getResourceId() {
        return _resourceId;
    }

    public void setResourceId(long resourceId) {
        _resourceId = resourceId;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        _userName = userName;
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    public Date getModifiedDate() {
        return _modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        _modifiedDate = modifiedDate;
    }

    public String getTagNames() {
        return _tagNames;
    }

    public void setTagNames(String tagNames) {
        _tagNames = tagNames;
    }

    public String getResourceType() {
        return _resourceType;
    }

    public void setResourceType(String resourceType) {
        _resourceType = resourceType;
    }
}
