package com.ext.portlet.bookreports.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="MetadataEntryModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the MetadataEntry table in the
 * database.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       MetadataEntry
 * @see       com.ext.portlet.bookreports.model.impl.MetadataEntryImpl
 * @see       com.ext.portlet.bookreports.model.impl.MetadataEntryModelImpl
 * @generated
 */
public interface MetadataEntryModel extends BaseModel<MetadataEntry> {
    public Long getPrimaryKey();

    public void setPrimaryKey(Long pk);

    public String getUuid();

    public void setUuid(String uuid);

    public Long getEntryId();

    public void setEntryId(Long entryId);

    public Long getGroupId();

    public void setGroupId(Long groupId);

    public Long getCompanyId();

    public void setCompanyId(Long companyId);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public String getContributor();

    public void setContributor(String contributor);

    public String getCoverage();

    public void setCoverage(String coverage);

    public String getCreator();

    public void setCreator(String creator);

    public String getDate();

    public void setDate(String date);

    public String getDescription();

    public void setDescription(String description);

    public String getFormat();

    public void setFormat(String format);

    public String getIdentifier();

    public void setIdentifier(String identifier);

    public String getLanguage();

    public void setLanguage(String language);

    public String getPublisher();

    public void setPublisher(String publisher);

    public String getRelation();

    public void setRelation(String relation);

    public String getRights();

    public void setRights(String rights);

    public String getSource();

    public void setSource(String source);

    public String getSubject();

    public void setSubject(String subject);

    public String getTitle();

    public void setTitle(String title);

    public String getType();

    public void setType(String type);

    public MetadataEntry toEscapedModel();
}
