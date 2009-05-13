package eu.scy.core;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.aug.2008
 * Time: 13:31:46
 * To change this template use File | Settings | File Templates.
 */
public class Constants {

    public static final String DAO_TYPE = "daoType";
    public static final String DAO_TYPE_HIBERNATE = "hibernate";

    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";


    // ROLES:
    public static final String ROLE_GLOBAL_ADMINISTRATOR = "ROLE_GLOBAL_ADMINISTRATOR";

    public static final String ROLE_PROJECT_ADMINISTRATOR = "ROLE_PROJECT_ADMINISTRATOR";

    public static final String ROLE_TEACHER = "ROLE_TEACHER";
    public static final String ROLE_STUDENT = "ROLE_STUDENT";

    public static final String NAME_SPACE = "http://www.scy-net.eu/schemas";

    //Content types
    public final static String CONTENT_TYPE_IMAGE_PNG = "image/png";

    //Time
    public static final long ONE_DAY_MILLISECONDS = 86400000;
    public static final long ONE_WEEK_MILLISECONDS =  ONE_DAY_MILLISECONDS * 7;
    public static final long TWO_WEEKS_MILLISECONDS =  ONE_WEEK_MILLISECONDS * 2 ;
    public static final long ONE_MONTH_MILLISECONDS =  TWO_WEEKS_MILLISECONDS * 2 ;
    public static final long ONE_YEAR_MILLISECONDS =  ONE_DAY_MILLISECONDS * 365 ;

    //Strings
    public final static String EMPTY_STRING = "";
    public final static String ONSE_SPACE_STRING = " ";
}
