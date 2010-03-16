package com.ext.portlet.cart.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="DiplomeSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * {@link com.ext.portlet.cart.service.http.DiplomeServiceSoap}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       com.ext.portlet.cart.service.http.DiplomeServiceSoap
 * @generated
 */
public class DiplomeSoap implements Serializable {
    private String _diplomeId;
    private String _cvId;
    private Date _createDate;
    private Date _modifiedDate;
    private String _intitule;
    private String _etablissement;
    private String _anneeDebut;
    private String _anneeFin;

    public DiplomeSoap() {
    }

    public static DiplomeSoap toSoapModel(Diplome model) {
        DiplomeSoap soapModel = new DiplomeSoap();

        soapModel.setDiplomeId(model.getDiplomeId());
        soapModel.setCvId(model.getCvId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setIntitule(model.getIntitule());
        soapModel.setEtablissement(model.getEtablissement());
        soapModel.setAnneeDebut(model.getAnneeDebut());
        soapModel.setAnneeFin(model.getAnneeFin());

        return soapModel;
    }

    public static DiplomeSoap[] toSoapModels(Diplome[] models) {
        DiplomeSoap[] soapModels = new DiplomeSoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static DiplomeSoap[][] toSoapModels(Diplome[][] models) {
        DiplomeSoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new DiplomeSoap[models.length][models[0].length];
        } else {
            soapModels = new DiplomeSoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static DiplomeSoap[] toSoapModels(List<Diplome> models) {
        List<DiplomeSoap> soapModels = new ArrayList<DiplomeSoap>(models.size());

        for (Diplome model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new DiplomeSoap[soapModels.size()]);
    }

    public String getPrimaryKey() {
        return _diplomeId;
    }

    public void setPrimaryKey(String pk) {
        setDiplomeId(pk);
    }

    public String getDiplomeId() {
        return _diplomeId;
    }

    public void setDiplomeId(String diplomeId) {
        _diplomeId = diplomeId;
    }

    public String getCvId() {
        return _cvId;
    }

    public void setCvId(String cvId) {
        _cvId = cvId;
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

    public String getIntitule() {
        return _intitule;
    }

    public void setIntitule(String intitule) {
        _intitule = intitule;
    }

    public String getEtablissement() {
        return _etablissement;
    }

    public void setEtablissement(String etablissement) {
        _etablissement = etablissement;
    }

    public String getAnneeDebut() {
        return _anneeDebut;
    }

    public void setAnneeDebut(String anneeDebut) {
        _anneeDebut = anneeDebut;
    }

    public String getAnneeFin() {
        return _anneeFin;
    }

    public void setAnneeFin(String anneeFin) {
        _anneeFin = anneeFin;
    }
}
