package com.ext.portlet.freestyler.service.http;

import com.ext.portlet.freestyler.service.FreestylerImageServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LongWrapper;
import com.liferay.portal.kernel.util.MethodWrapper;
import com.liferay.portal.kernel.util.NullWrapper;
import com.liferay.portal.security.auth.HttpPrincipal;
import com.liferay.portal.service.http.TunnelUtil;


/**
 * <a href="FreestylerImageServiceHttp.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a HTTP utility for the
 * <code>com.ext.portlet.freestyler.service.FreestylerImageServiceUtil</code> service
 * utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it requires an
 * additional <code>com.liferay.portal.security.auth.HttpPrincipal</code>
 * parameter.
 * </p>
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <code>tunnel.servlet.hosts.allowed</code> in
 * portal.properties to configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.security.auth.HttpPrincipal
 * @see com.ext.portlet.freestyler.service.FreestylerImageServiceUtil
 * @see com.ext.portlet.freestyler.service.http.FreestylerImageServiceSoap
 *
 */
public class FreestylerImageServiceHttp {
    private static Log _log = LogFactoryUtil.getLog(FreestylerImageServiceHttp.class);

    public static com.ext.portlet.freestyler.model.FreestylerImage addImage(
        HttpPrincipal httpPrincipal, long folderId, java.lang.String name,
        java.lang.String description, java.io.File file,
        java.lang.String contentType,
        com.liferay.portal.service.ServiceContext serviceContext)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        try {
            Object paramObj0 = new LongWrapper(folderId);

            Object paramObj1 = name;

            if (name == null) {
                paramObj1 = new NullWrapper("java.lang.String");
            }

            Object paramObj2 = description;

            if (description == null) {
                paramObj2 = new NullWrapper("java.lang.String");
            }

            Object paramObj3 = file;

            if (file == null) {
                paramObj3 = new NullWrapper("java.io.File");
            }

            Object paramObj4 = contentType;

            if (contentType == null) {
                paramObj4 = new NullWrapper("java.lang.String");
            }

            Object paramObj5 = serviceContext;

            if (serviceContext == null) {
                paramObj5 = new NullWrapper(
                        "com.liferay.portal.service.ServiceContext");
            }

            MethodWrapper methodWrapper = new MethodWrapper(FreestylerImageServiceUtil.class.getName(),
                    "addImage",
                    new Object[] {
                        paramObj0, paramObj1, paramObj2, paramObj3, paramObj4,
                        paramObj5
                    });

            Object returnObj = null;

            try {
                returnObj = TunnelUtil.invoke(httpPrincipal, methodWrapper);
            } catch (Exception e) {
                if (e instanceof com.liferay.portal.PortalException) {
                    throw (com.liferay.portal.PortalException) e;
                }

                if (e instanceof com.liferay.portal.SystemException) {
                    throw (com.liferay.portal.SystemException) e;
                }

                throw new com.liferay.portal.SystemException(e);
            }

            return (com.ext.portlet.freestyler.model.FreestylerImage) returnObj;
        } catch (com.liferay.portal.SystemException se) {
            _log.error(se, se);

            throw se;
        }
    }
}
