package com.ext.portlet.freestyler.service;


/**
 * <a href="FreestylerEntryServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.freestyler.service.FreestylerEntryService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.FreestylerEntryService
 *
 */
public class FreestylerEntryServiceUtil {
    private static FreestylerEntryService _service;

    public static FreestylerEntryService getService() {
        if (_service == null) {
            throw new RuntimeException("FreestylerEntryService is not set");
        }

        return _service;
    }

    public void setService(FreestylerEntryService service) {
        _service = service;
    }
}
