package com.ext.portlet.metadata.model;

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
 * This interface is a model that represents the <code>MetadataEntry</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.metadata.model.MetadataEntry
 * @see com.ext.portlet.metadata.model.impl.MetadataEntryImpl
 * @see com.ext.portlet.metadata.model.impl.MetadataEntryModelImpl
 *
 */
public interface MetadataEntryModel extends BaseModel<MetadataEntry> {
    public Long getPrimaryKey();

    public void setPrimaryKey(Long pk);

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

    public Long getAssertEntryId();

    public void setAssertEntryId(Long assertEntryId);

    public String getDc_contributor();

    public void setDc_contributor(String dc_contributor);

    public String getDc_coverage();

    public void setDc_coverage(String dc_coverage);

    public String getDc_creator();

    public void setDc_creator(String dc_creator);

    public String getDc_date();

    public void setDc_date(String dc_date);

    public String getDc_description();

    public void setDc_description(String dc_description);

    public String getDc_format();

    public void setDc_format(String dc_format);

    public String getDc_identifier();

    public void setDc_identifier(String dc_identifier);

    public String getDc_language();

    public void setDc_language(String dc_language);

    public String getDc_publisher();

    public void setDc_publisher(String dc_publisher);

    public String getDc_relation();

    public void setDc_relation(String dc_relation);

    public String getDc_rights();

    public void setDc_rights(String dc_rights);

    public String getDc_source();

    public void setDc_source(String dc_source);

    public String getDc_subject();

    public void setDc_subject(String dc_subject);

    public String getDc_title();

    public void setDc_title(String dc_title);

    public String getDc_type();

    public void setDc_type(String dc_type);

    public MetadataEntry toEscapedModel();
}
