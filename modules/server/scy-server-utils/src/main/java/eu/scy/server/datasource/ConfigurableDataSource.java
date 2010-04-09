package eu.scy.server.datasource;

import eu.scy.common.configuration.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.feb.2010
 * Time: 22:39:29
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurableDataSource extends DriverManagerDataSource {

    private static Logger log = Logger.getLogger("ConfigurableDataSource.class");

    public ConfigurableDataSource() {
        String dbHost = Configuration.getInstance().getSailDBHost();
        String dbName = Configuration.getInstance().getSailDBName();
        String dbUserName = Configuration.getInstance().getSailDBUserName();
        String dbPassword = Configuration.getInstance().getSailDBPassword();
        setUsername(dbUserName);
        setPassword(dbPassword);
        setDriverClassName("com.mysql.jdbc.Driver");
        setUrl("jdbc:mysql://" + dbHost + "/" + dbName + "?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;useServerPrepStmts=false&amp;zeroDateTimeBehavior=round");
    }          

}
