package eu.scy.server.externalcomponents;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.server.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLSpacesComponent implements IExternalComponent {

    private Logger log = Logger.getLogger("SQLSpacesComponent.class");

    @Override
    public void startComponent() throws ExternalComponentFailedException {
        log.info("Initializing SQL SPACES SERVER");
        Configuration.getConfiguration().setLogLevel(Level.INFO);
        Configuration.getConfiguration().setDbType(Configuration.Database.MYSQL);
        Configuration.getConfiguration().setSSLEnabled(false);
        Configuration.getConfiguration().setMysqlHost("localhost");
        Configuration.getConfiguration().setMysqlPort(3306);
        Configuration.getConfiguration().setMysqlSchema("sqlspaces");
        Configuration.getConfiguration().setDbUser("sqlspaces");
        Configuration.getConfiguration().setDbPassword("sqlspaces");
        Configuration.getConfiguration().setWebPort(8091);
        Configuration.getConfiguration().setWebEnabled(false);
        Configuration.getConfiguration().setWebServicesEnabled(false);
        Server.startServer();
    }

    @Override
    public void stopComponent() throws ExternalComponentFailedException {
        Server.stopServer();
    }

}
