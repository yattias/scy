package com.ext.portlet.linkTool.service;


/**
 * <a href="LinkEntryServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.linkTool.service.LinkEntryService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.linkTool.service.LinkEntryService
 *
 */
public class LinkEntryServiceUtil {
    private static LinkEntryService _service;

    public static LinkEntryService getService() {
        if (_service == null) {
            throw new RuntimeException("LinkEntryService is not set");
        }

        return _service;
    }

    public void setService(LinkEntryService service) {
        _service = service;
    }
}
