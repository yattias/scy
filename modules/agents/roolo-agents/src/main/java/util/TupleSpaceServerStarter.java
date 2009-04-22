package util;

import java.util.logging.Level;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.server.Server;

public class TupleSpaceServerStarter {
    
    public static void main(String[] args) {
        Configuration.getConfiguration().setDbUser("root");
        Configuration.getConfiguration().setDbPassword("");
        Configuration.getConfiguration().setMysqlSchema("sqlspaces");
        Configuration.getConfiguration().setLogLevel(Level.FINE);
        Server.startServer();
    }
    
}
