package com.ext.portlet.cart.service;


/**
 * <a href="DiplomeServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * {@link DiplomeService} bean. The static methods of
 * this class calls the same methods of the bean instance. It's convenient to be
 * able to just write one line to call a method on a bean instead of writing a
 * lookup call and a method call.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       DiplomeService
 * @generated
 */
public class DiplomeServiceUtil {
    private static DiplomeService _service;

    public static DiplomeService getService() {
        if (_service == null) {
            throw new RuntimeException("DiplomeService is not set");
        }

        return _service;
    }

    public void setService(DiplomeService service) {
        _service = service;
    }
}
