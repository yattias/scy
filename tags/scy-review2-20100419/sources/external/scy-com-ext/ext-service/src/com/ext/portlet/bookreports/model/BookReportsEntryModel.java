package com.ext.portlet.bookreports.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="BookReportsEntryModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>BookReportsEntry</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.bookreports.model.BookReportsEntry
 * @see com.ext.portlet.bookreports.model.impl.BookReportsEntryImpl
 * @see com.ext.portlet.bookreports.model.impl.BookReportsEntryModelImpl
 *
 */
public interface BookReportsEntryModel extends BaseModel<BookReportsEntry> {
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

    public Long getUserId();

    public void setUserId(Long userId);

    public String getUserName();

    public void setUserName(String userName);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public String getName();

    public void setName(String name);

    public String getTitle();

    public void setTitle(String title);

    public BookReportsEntry toEscapedModel();
}
