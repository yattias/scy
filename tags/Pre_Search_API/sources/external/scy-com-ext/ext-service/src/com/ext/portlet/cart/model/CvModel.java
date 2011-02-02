package com.ext.portlet.cart.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="CvModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the xxx_cv table in the
 * database.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       Cv
 * @see       com.ext.portlet.cart.model.impl.CvImpl
 * @see       com.ext.portlet.cart.model.impl.CvModelImpl
 * @generated
 */
public interface CvModel extends BaseModel<Cv> {
    public String getPrimaryKey();

    public void setPrimaryKey(String pk);

    public String getCvId();

    public void setCvId(String cvId);

    public String getUserId();

    public void setUserId(String userId);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public Cv toEscapedModel();
}
