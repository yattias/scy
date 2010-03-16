package com.ext.portlet.cart.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="DiplomeModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the xxx_diplome table in the
 * database.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       Diplome
 * @see       com.ext.portlet.cart.model.impl.DiplomeImpl
 * @see       com.ext.portlet.cart.model.impl.DiplomeModelImpl
 * @generated
 */
public interface DiplomeModel extends BaseModel<Diplome> {
    public String getPrimaryKey();

    public void setPrimaryKey(String pk);

    public String getDiplomeId();

    public void setDiplomeId(String diplomeId);

    public String getCvId();

    public void setCvId(String cvId);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public String getIntitule();

    public void setIntitule(String intitule);

    public String getEtablissement();

    public void setEtablissement(String etablissement);

    public String getAnneeDebut();

    public void setAnneeDebut(String anneeDebut);

    public String getAnneeFin();

    public void setAnneeFin(String anneeFin);

    public Diplome toEscapedModel();
}
