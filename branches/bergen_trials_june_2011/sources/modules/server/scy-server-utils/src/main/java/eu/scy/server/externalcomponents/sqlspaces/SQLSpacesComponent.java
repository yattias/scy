package eu.scy.server.externalcomponents.sqlspaces;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.server.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

import eu.scy.server.externalcomponents.ExternalComponentFailedException;
import eu.scy.server.externalcomponents.IExternalComponent;

public class SQLSpacesComponent implements IExternalComponent {

    private Logger log = Logger.getLogger("SQLSpacesComponent.class");

    @Override
    public void startComponent() throws ExternalComponentFailedException {
        log.info("Initializing SQLSPACES SERVER");
        eu.scy.common.configuration.Configuration scyConf = eu.scy.common.configuration.Configuration.getInstance();
        Configuration sqlsConf = Configuration.getConfiguration();
        
        sqlsConf.setConnectionPoolSize(100);
        sqlsConf.setLogLevel(Level.INFO);
        sqlsConf.setDbType(Configuration.Database.MYSQL);
        sqlsConf.setSSLEnabled(false);
        sqlsConf.setDbHost(scyConf.getSQLSpacesDBHost());
        sqlsConf.setDbPort(3306);
        sqlsConf.setDbSchema("sqlspaces");
        sqlsConf.setDbUser(scyConf.get("sqlspaces.db.user"));
        sqlsConf.setDbPassword(scyConf.get("sqlspaces.db.password"));
        sqlsConf.setWebEnabled(false);
        sqlsConf.setOpenFireHost(scyConf.getOpenFireHost());
        sqlsConf.setOpenFirePortClient(scyConf.getOpenFirePort());
        sqlsConf.setOpenFirePortExternal(scyConf.getOpenFireExternalComponentPort());
        sqlsConf.setXMPPServiceName("sqlspaces");
        sqlsConf.setXMPPServiceSecret("sqlspaces");
        Level level = Logger.getLogger("").getLevel();
        log.info("USING: " + sqlsConf.getDbUser() + " " + sqlsConf.getDbPassword() + " " + sqlsConf.getDbHost() + " " +sqlsConf.getDbType());
        Server.startServer();
        Logger.getLogger("").setLevel(level);
    }

    @Override
    public void stopComponent() throws ExternalComponentFailedException {
        log.info("-----> STOPPING: SQLSPACES");
        Server.stopServer();
    }

}