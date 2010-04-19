package com.liferay.util;

import com.liferay.portal.kernel.job.IntervalJob;
import com.liferay.portal.kernel.job.JobExecutionContext;
import com.liferay.portal.kernel.job.JobExecutionException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.util.PropsUtil;

public class MissionDeactivatorJob implements IntervalJob {

	public MissionDeactivatorJob() {
		int rawInterval = MISSION_DEACTIVATOR_JOB_INTERVAL;

		_interval = rawInterval * Time.MINUTE;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		_log.info("deactivate mission jobs  started");
		_log.info("time interval is set at portal.properties to: " + MISSION_DEACTIVATOR_JOB_INTERVAL + " minutes");
		MissionDeactivatorUtil.missionDeactiveJob();

	}

	@Override
	public long getInterval() {
		return _interval;
	}

	private long _interval;

	public static final int MISSION_DEACTIVATOR_JOB_INTERVAL = GetterUtil.getInteger(PropsUtil.get("mission.deactivator.job.interval"));

	private static Log _log = LogFactoryUtil.getLog(MissionDeactivatorJob.class);
}
