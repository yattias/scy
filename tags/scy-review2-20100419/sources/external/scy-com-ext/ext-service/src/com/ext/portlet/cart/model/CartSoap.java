package com.ext.portlet.cart.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="CartSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.cart.service.http.CartServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.cart.service.http.CartServiceSoap
 *
 */
public class CartSoap implements Serializable {
    private long _cartId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private String _title;
    private Date _createDate;
    private Date _modifiedDate;
    private String _tagNames;

    public CartSoap() {
    }

    public static CartSoap toSoapModel(Cart model) {
        CartSoap soapModel = new CartSoap();

        soapModel.setCartId(model.getCartId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setUserId(model.getUserId());
        soapModel.setTitle(model.getTitle());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setTagNames(model.getTagNames());

        return soapModel;
    }

    public static CartSoap[] toSoapModels(Cart[] models) {
        CartSoap[] soapModels = new CartSoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static CartSoap[][] toSoapModels(Cart[][] models) {
        CartSoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new CartSoap[models.length][models[0].length];
        } else {
            soapModels = new CartSoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static CartSoap[] toSoapModels(List<Cart> models) {
        List<CartSoap> soapModels = new ArrayList<CartSoap>(models.size());

        for (Cart model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new CartSoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _cartId;
    }

    public void setPrimaryKey(long pk) {
        setCartId(pk);
    }

    public long getCartId() {
        return _cartId;
    }

    public void setCartId(long cartId) {
        _cartId = cartId;
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

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
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
}
