package eu.scy.listeners;

import info.collide.sqlspaces.commons.TupleSpaceException;
//import info.collide.swat.SWAT;
//import info.collide.swat.SWATConfiguration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class SWATStartupListener implements ServletContextListener {

/*
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        new Thread(){
            public void run() {
                SWATConfiguration.getConfiguration().setInitialServices("IoService","ScyseService","ScyseCompareService");
                SWATConfiguration.getConfiguration().setServerHost("localhost");
                SWATConfiguration.getConfiguration().setServerPort(2525);
                SWATConfiguration.getConfiguration().setPrologExecutable("C\\:\\Program Files\\pl\\bin\\plcon.exe");
                SWATConfiguration.getConfiguration().setStandalone(false);
                try {
                    SWAT.start();
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            }}.start();
    }
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

    */

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
