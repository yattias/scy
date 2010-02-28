package eu.scy.webapp.services;

import java.io.IOException;

import org.apache.tapestry5.*;
import org.apache.tapestry5.internal.services.ContextResource;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.InjectService;

import org.slf4j.Logger;
//import org.telscenter.sail.webapp.service.authentication.UserDetailsService;
//import org.telscenter.sail.webapp.service.authentication.impl.UserDetailsServiceImpl;
import eu.scy.framework.*;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.SCYGroupImpl;
import eu.scy.core.model.User;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.ScyBase;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule {

    private static java.util.logging.Logger log = java.util.logging.Logger.getLogger("AppModule.class");

    public static final String CGLIB_TOKEN = "CGLIB";
    /**
     * enhanced cglib class starts with this string after the real name of the class
     */
    public static final String CGLIB_ADDED_SIGNS = "$";
    public static final String CGLIB_AT = "@";


    public static void bind(ServiceBinder binder) {
        binder.bind(PageManager.class, PageManagerImpl.class);
        //binder.bind(UserDetailsService.class, UserDetailsServiceImpl.class);
        System.out.println("BIDING SERVICES!");

        //binder.bind(User.class, SCYUserImpl.class);
        //binder.bind(ActionManager.class, ActionManagerImpl.class);
        //binder.bind(SCYCoercer.class, SCYCoercerServiceImpl.class);
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    /**
     * sets up the action manager service
     *
     * @param userDAOHibernate
     * @param projectDAOHibernate
     * @return
     */
    public static ActionManager build(UserDAOHibernate userDAOHibernate, ProjectDAOHibernate projectDAOHibernate) {
        ActionManager actionManager = new ActionManagerImpl();
        actionManager.setProjectDAOHibernate(projectDAOHibernate);
        actionManager.setUserDAOHibernate(userDAOHibernate);
        return actionManager;
    }


    /**
     * Sets up the SCYCOercerService. Responsible for translating strings to SCYBaseObjects
     *
     * @param userDAOHibernate
     * @return
     */
    public static SCYCoercer build(UserDAOHibernate userDAOHibernate) {
        SCYCoercer coercer = new SCYCoercerServiceImpl();
        coercer.setUserDAOHibernate(userDAOHibernate);
        System.out.println("COERCER BUILD");
        return coercer;
    }


    public static void contributeApplicationDefaults(

            MappedConfiguration<String, String> configuration) {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).

        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");

        // The factory default is true but during the early stages of an application
        // overriding to false is a good idea. In addition, this is often overridden
        // on the command line as -Dtapestry.production-mode=false
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
    }


    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    public RequestFilter buildTimingFilter(final Logger log) {
        return new RequestFilter() {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException {
                long startTime = System.currentTimeMillis();

                try {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                }
                finally {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @Local
                                         RequestFilter filter) {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Timing", filter);
    }


    public static void contributeIgnoredPathsFilter(Configuration<String> configuration) {
        configuration.add("/services/.*");
        configuration.add("/resources/.*");
        configuration.add("/applets/.*");
        configuration.add("/css/.*");
        configuration.add("/graphics/.*");
        configuration.add("/remoting/.*");
        configuration.add("/webstart/.*");

    }

    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration) {
        configuration.add("eu.scy.core.model.impl");
    }


    /*public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration, final @InjectService("SCYCoercer") SCYCoercer coerser) {

        Coercion<String, ScyBaseObject> fromStringToScy = new Coercion<String, ScyBaseObject>() {
            public ScyBaseObject coerce(String input) {
                log.info("---------------------------------------------------------------------------------------");
                log.info("COERCING: " + input);
                log.info("---------------------------------------------------------------------------------------");
                String clazz = input;
                if(input.indexOf("-") > 0) {
                    clazz = input.substring(0, input.indexOf("-"));
                }
                if (clazz.indexOf(CGLIB_TOKEN) > 0) {
                    clazz = clazz.substring(0, clazz.indexOf(CGLIB_ADDED_SIGNS));
                }
                if(clazz.indexOf(CGLIB_AT )> 0 ) {
                    clazz = clazz.substring(0, clazz.indexOf(CGLIB_AT));
                }


                Class theClass = null;

                try {
                    theClass = Class.forName(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                String id = input.substring(input.indexOf("-SCY-") + 5, input.length());
                //log.info("** ID IS: " + id);
                return coerser.get(theClass, id);
            }
        };

        Coercion<ScyBaseObject, String> fromScyBaseObjectToString = new Coercion<ScyBaseObject, String>() {
            public String coerce(ScyBaseObject input) {
                return input.getClass().getName() + "-SCY-" + input.getId();
            }
        };


        configuration.add(new CoercionTuple<String, ScyBaseObject>(String.class, ScyBaseObject.class, fromStringToScy));
        //configuration.add(new CoercionTuple<String, ScyBaseObject>(String.class, SCYGroupImpl.class, fromStringToScy));
        configuration.add(new CoercionTuple<ScyBaseObject, String>(ScyBaseObject.class, String.class, fromScyBaseObjectToString));
    } */


}