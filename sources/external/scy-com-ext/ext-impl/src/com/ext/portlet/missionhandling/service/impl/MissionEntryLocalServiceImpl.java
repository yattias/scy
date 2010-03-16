package com.ext.portlet.missionhandling.service.impl;

import java.util.List;

import com.ext.portlet.missionhandling.model.MissionEntry;
import com.ext.portlet.missionhandling.service.base.MissionEntryLocalServiceBaseImpl;
import com.liferay.portal.SystemException;


public class MissionEntryLocalServiceImpl
    extends MissionEntryLocalServiceBaseImpl {

	@Override
	public List<MissionEntry> getMissionEntries(long groupId) throws SystemException {		
		return missionEntryPersistence.findByGroupId(groupId);
	}
}
