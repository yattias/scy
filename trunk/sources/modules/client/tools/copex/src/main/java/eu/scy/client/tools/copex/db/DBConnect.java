/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * cette classe definit toutes les methodes de connexion et d'acces a la base
 * @author MBO
 */ 
public class DBConnect {

    // ATTRIBUTS 
    private static Connection conToDB;
    
    // METHODES
    /* connexion a la base */
    public static synchronized Connection connect(String user, String passwd, String url, ArrayList v) {

	try {
            try{
                 Class.forName("com.mysql.jdbc.Driver").newInstance();
                //Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            }catch(ClassNotFoundException e){
                System.out.println("classe non trouvee ! : com.mysql.jdbc.Driver " +e);
            }
           
            conToDB = DriverManager.getConnection(url, user, passwd);
            return conToDB;
	} catch ( Exception e ) {
            System.out.println("url : "+url);
            System.out.println("user : "+user);
            System.out.println("passwd : "+passwd);
            v.add(e.getMessage());
	    System.out.println("\n--- Exception caught ---\n") ;
	    while ( e != null ) {
		    System.out.println("Message :   " + e.getMessage()) ;
                    e.printStackTrace();
		    if (e instanceof SQLException) {
			    System.out.println("SQLState :  " + ((SQLException) e).getSQLState()) ;
		    	System.out.println("ErrorCode : " + ((SQLException) e).getErrorCode()) ;
			    e = ((SQLException) e).getNextException() ;
		    }
		    else
		    	e = null;
		    System.out.println("") ;
	    }
            
	    return null ;
	}
    }
        /* deconnexion a la base */
    public static synchronized boolean  closeConnection() {
	try {
            if (conToDB != null)
                conToDB.close();
            return true;
	} catch ( Exception e ) {
	    System.out.println("\n--- Exception caught ---\n") ;
	    while ( e != null ) {
		    System.out.println("Message :   " + e.getMessage()) ;
		    if (e instanceof SQLException) {
			    System.out.println("SQLState :  " + ((SQLException) e).getSQLState()) ;
		    	System.out.println("ErrorCode : " + ((SQLException) e).getErrorCode()) ;
			    e = ((SQLException) e).getNextException() ;
		    }
		    else
		    	e = null;
		    System.out.println("") ;
	    }
	    return false ;
	}
    }
}
