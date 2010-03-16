package com.liferay.util;

import com.liferay.portal.kernel.job.IntervalJob;
import com.liferay.portal.kernel.job.JobSchedulerUtil;
import com.liferay.portal.kernel.job.Scheduler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class MissionDeativateScheduler implements Scheduler {

	@Override
	public void schedule() {
		_log.info("start mission deactivate scheduler");
		JobSchedulerUtil.schedule(_missionDeactivator);

	}

	@Override
	public void unschedule() {
		// TODO Auto-generated method stub

	}

	private IntervalJob _missionDeactivator = new MissionDeactivatorJob();

	private static Log _log = LogFactoryUtil.getLog(MissionDeativateScheduler.class);
}
