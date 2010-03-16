package com.ext.portlet.bookreports.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="MetadataEntrySoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * {@link com.ext.portlet.bookreports.service.http.MetadataEntryServiceSoap}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       com.ext.portlet.bookreports.service.http.MetadataEntryServiceSoap
 * @generated
 */
public class MetadataEntrySoap implements Serializable {
    private String _uuid;
    private Long _entryId;
    private Long _groupId;
    private Long _companyId;
    private Date _createDate;
    private Date _modifiedDate;
    private String _contributor;
    private String _coverage;
    private String _creator;
    private String _date;
    private String _description;
    private String _format;
    private String _identifier;
    private String _language;
    private String _publisher;
    private String _relation;
    private String _rights;
    private String _source;
    private String _subject;
    private String _title;
    private String _type;

    public MetadataEntrySoap() {
    }

    public static MetadataEntrySoap toSoapModel(MetadataEntry model) {
        MetadataEntrySoap soapModel = new MetadataEntrySoap();

        soapModel.setUuid(model.getUuid());
        soapModel.setEntryId(model.getEntryId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setContributor(model.getContributor());
        soapModel.setCoverage(model.getCoverage());
        soapModel.setCreator(model.getCreator());
        soapModel.setDate(model.getDate());
        soapModel.setDescription(model.getDescription());
        soapModel.setFormat(model.getFormat());
        soapModel.setIdentifier(model.getIdentifier());
        soapModel.setLanguage(model.getLanguage());
        soapModel.setPublisher(model.getPublisher());
        soapModel.setRelation(model.getRelation());
        soapModel.setRights(model.getRights());
        soapModel.setSource(model.getSource());
        soapModel.setSubject(model.getSubject());
        soapModel.setTitle(model.getTitle());
        soapModel.setType(model.getType());

        return soapModel;
    }

    public static MetadataEntrySoap[] toSoapModels(MetadataEntry[] models) {
        MetadataEntrySoap[] soapModels = new MetadataEntrySoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static MetadataEntrySoap[][] toSoapModels(MetadataEntry[][] models) {
        MetadataEntrySoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new MetadataEntrySoap[models.length][models[0].length];
        } else {
            soapModels = new MetadataEntrySoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static MetadataEntrySoap[] toSoapModels(List<MetadataEntry> models) {
        List<MetadataEntrySoap> soapModels = new ArrayList<MetadataEntrySoap>(models.size());

        for (MetadataEntry model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new MetadataEntrySoap[soapModels.size()]);
    }

    public Long getPrimaryKey() {
        return _entryId;
    }

    public void setPrimaryKey(Long pk) {
        setEntryId(pk);
    }

    public String getUuid() {
        return _uuid;
    }

    public void setUuid(String uuid) {
        _uuid = uuid;
    }

    public Long getEntryId() {
        return _entryId;
    }

    public void setEntryId(Long entryId) {
        _entryId = entryId;
    }

    public Long getGroupId() {
        return _groupId;
    }

    public void setGroupId(Long groupId) {
        _groupId = groupId;
    }

    public Long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(Long companyId) {
        _companyId = companyId;
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

    public String getContributor() {
        return _contributor;
    }

    public void setContributor(String contributor) {
        _contributor = contributor;
    }

    public String getCoverage() {
        return _coverage;
    }

    public void setCoverage(String coverage) {
        _coverage = coverage;
    }

    public String getCreator() {
        return _creator;
    }

    public void setCreator(String creator) {
        _creator = creator;
    }

    public String getDate() {
        return _date;
    }

    public void setDate(String date) {
        _date = date;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getFormat() {
        return _format;
    }

    public void setFormat(String format) {
        _format = format;
    }

    public String getIdentifier() {
        return _identifier;
    }

    public void setIdentifier(String identifier) {
        _identifier = identifier;
    }

    public String getLanguage() {
        return _language;
    }

    public void setLanguage(String language) {
        _language = language;
    }

    public String getPublisher() {
        return _publisher;
    }

    public void setPublisher(String publisher) {
        _publisher = publisher;
    }

    public String getRelation() {
        return _relation;
    }

    public void setRelation(String relation) {
        _relation = relation;
    }

    public String getRights() {
        return _rights;
    }

    public void setRights(String rights) {
        _rights = rights;
    }

    public String getSource() {
        return _source;
    }

    public void setSource(String source) {
        _source = source;
    }

    public String getSubject() {
        return _subject;
    }

    public void setSubject(String subject) {
        _subject = subject;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }
}
