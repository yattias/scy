package com.ext.portlet.missionhandling.service;


/**
 * <a href="MissionEntryServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.missionhandling.service.MissionEntryService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.missionhandling.service.MissionEntryService
 *
 */
public class MissionEntryServiceUtil {
    private static MissionEntryService _service;

    public static MissionEntryService getService() {
        if (_service == null) {
            throw new RuntimeException("MissionEntryService is not set");
        }

        return _service;
    }

    public void setService(MissionEntryService service) {
        _service = service;
    }
}
