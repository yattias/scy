package com.ext.portlet.cart.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="CvSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * {@link com.ext.portlet.cart.service.http.CvServiceSoap}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       com.ext.portlet.cart.service.http.CvServiceSoap
 * @generated
 */
public class CvSoap implements Serializable {
    private String _cvId;
    private String _userId;
    private Date _createDate;
    private Date _modifiedDate;

    public CvSoap() {
    }

    public static CvSoap toSoapModel(Cv model) {
        CvSoap soapModel = new CvSoap();

        soapModel.setCvId(model.getCvId());
        soapModel.setUserId(model.getUserId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());

        return soapModel;
    }

    public static CvSoap[] toSoapModels(Cv[] models) {
        CvSoap[] soapModels = new CvSoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static CvSoap[][] toSoapModels(Cv[][] models) {
        CvSoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new CvSoap[models.length][models[0].length];
        } else {
            soapModels = new CvSoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static CvSoap[] toSoapModels(List<Cv> models) {
        List<CvSoap> soapModels = new ArrayList<CvSoap>(models.size());

        for (Cv model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new CvSoap[soapModels.size()]);
    }

    public String getPrimaryKey() {
        return _cvId;
    }

    public void setPrimaryKey(String pk) {
        setCvId(pk);
    }

    public String getCvId() {
        return _cvId;
    }

    public void setCvId(String cvId) {
        _cvId = cvId;
    }

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        _userId = userId;
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
}
