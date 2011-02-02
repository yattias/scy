package com.ext.portlet.cart.service;


/**
 * <a href="CvServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * {@link CvService} bean. The static methods of
 * this class calls the same methods of the bean instance. It's convenient to be
 * able to just write one line to call a method on a bean instead of writing a
 * lookup call and a method call.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       CvService
 * @generated
 */
public class CvServiceUtil {
    private static CvService _service;

    public static CvService getService() {
        if (_service == null) {
            throw new RuntimeException("CvService is not set");
        }

        return _service;
    }

    public void setService(CvService service) {
        _service = service;
    }
}
