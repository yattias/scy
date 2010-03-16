package com.ext.portlet.metadata.model;

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
 * <code>com.ext.portlet.metadata.service.http.MetadataEntryServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.metadata.service.http.MetadataEntryServiceSoap
 *
 */
public class MetadataEntrySoap implements Serializable {
    private Long _entryId;
    private Long _groupId;
    private Long _companyId;
    private Date _createDate;
    private Date _modifiedDate;
    private Long _assertEntryId;
    private String _dc_contributor;
    private String _dc_coverage;
    private String _dc_creator;
    private String _dc_date;
    private String _dc_description;
    private String _dc_format;
    private String _dc_identifier;
    private String _dc_language;
    private String _dc_publisher;
    private String _dc_relation;
    private String _dc_rights;
    private String _dc_source;
    private String _dc_subject;
    private String _dc_title;
    private String _dc_type;

    public MetadataEntrySoap() {
    }

    public static MetadataEntrySoap toSoapModel(MetadataEntry model) {
        MetadataEntrySoap soapModel = new MetadataEntrySoap();

        soapModel.setEntryId(model.getEntryId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setAssertEntryId(model.getAssertEntryId());
        soapModel.setDc_contributor(model.getDc_contributor());
        soapModel.setDc_coverage(model.getDc_coverage());
        soapModel.setDc_creator(model.getDc_creator());
        soapModel.setDc_date(model.getDc_date());
        soapModel.setDc_description(model.getDc_description());
        soapModel.setDc_format(model.getDc_format());
        soapModel.setDc_identifier(model.getDc_identifier());
        soapModel.setDc_language(model.getDc_language());
        soapModel.setDc_publisher(model.getDc_publisher());
        soapModel.setDc_relation(model.getDc_relation());
        soapModel.setDc_rights(model.getDc_rights());
        soapModel.setDc_source(model.getDc_source());
        soapModel.setDc_subject(model.getDc_subject());
        soapModel.setDc_title(model.getDc_title());
        soapModel.setDc_type(model.getDc_type());

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

    public Long getAssertEntryId() {
        return _assertEntryId;
    }

    public void setAssertEntryId(Long assertEntryId) {
        _assertEntryId = assertEntryId;
    }

    public String getDc_contributor() {
        return _dc_contributor;
    }

    public void setDc_contributor(String dc_contributor) {
        _dc_contributor = dc_contributor;
    }

    public String getDc_coverage() {
        return _dc_coverage;
    }

    public void setDc_coverage(String dc_coverage) {
        _dc_coverage = dc_coverage;
    }

    public String getDc_creator() {
        return _dc_creator;
    }

    public void setDc_creator(String dc_creator) {
        _dc_creator = dc_creator;
    }

    public String getDc_date() {
        return _dc_date;
    }

    public void setDc_date(String dc_date) {
        _dc_date = dc_date;
    }

    public String getDc_description() {
        return _dc_description;
    }

    public void setDc_description(String dc_description) {
        _dc_description = dc_description;
    }

    public String getDc_format() {
        return _dc_format;
    }

    public void setDc_format(String dc_format) {
        _dc_format = dc_format;
    }

    public String getDc_identifier() {
        return _dc_identifier;
    }

    public void setDc_identifier(String dc_identifier) {
        _dc_identifier = dc_identifier;
    }

    public String getDc_language() {
        return _dc_language;
    }

    public void setDc_language(String dc_language) {
        _dc_language = dc_language;
    }

    public String getDc_publisher() {
        return _dc_publisher;
    }

    public void setDc_publisher(String dc_publisher) {
        _dc_publisher = dc_publisher;
    }

    public String getDc_relation() {
        return _dc_relation;
    }

    public void setDc_relation(String dc_relation) {
        _dc_relation = dc_relation;
    }

    public String getDc_rights() {
        return _dc_rights;
    }

    public void setDc_rights(String dc_rights) {
        _dc_rights = dc_rights;
    }

    public String getDc_source() {
        return _dc_source;
    }

    public void setDc_source(String dc_source) {
        _dc_source = dc_source;
    }

    public String getDc_subject() {
        return _dc_subject;
    }

    public void setDc_subject(String dc_subject) {
        _dc_subject = dc_subject;
    }

    public String getDc_title() {
        return _dc_title;
    }

    public void setDc_title(String dc_title) {
        _dc_title = dc_title;
    }

    public String getDc_type() {
        return _dc_type;
    }

    public void setDc_type(String dc_type) {
        _dc_type = dc_type;
    }
}
