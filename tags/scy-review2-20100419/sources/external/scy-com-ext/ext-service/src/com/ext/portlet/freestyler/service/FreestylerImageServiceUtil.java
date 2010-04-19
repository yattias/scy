package com.ext.portlet.freestyler.service;


/**
 * <a href="FreestylerImageServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.freestyler.service.FreestylerImageService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.FreestylerImageService
 *
 */
public class FreestylerImageServiceUtil {
    private static FreestylerImageService _service;

    public static com.ext.portlet.freestyler.model.FreestylerImage addImage(
        long folderId, java.lang.String name, java.lang.String description,
        java.io.File file, java.lang.String contentType,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService()
                   .addImage(folderId, name, description, file, contentType,
            serviceContext);
    }

    public static FreestylerImageService getService() {
        if (_service == null) {
            throw new RuntimeException("FreestylerImageService is not set");
        }

        return _service;
    }

    public void setService(FreestylerImageService service) {
        _service = service;
    }
}
