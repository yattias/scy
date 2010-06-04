/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;



import eu.scy.client.tools.copex.common.CopexUser;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.sql.*;
import java.util.*;

/**
 * gestion des utilisateurs
 * @author MBO
 */
public class UserFromDB {
    /* charge les utilisateurs */
    public static CopexReturn getAllUserFromDB(Connection c, Vector v) {
        Vector listUser = new Vector();
        try{
           String query = "SELECT ID_USER, USER_NAME, FIRSTNAME, LOGIN, PASSWORD " +
                   "FROM COPEX_USER ;";
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()){
                    long dbKey = rs.getLong("ID_USER");
                    String name = rs.getString("USER_NAME");
                    String firstname = rs.getString("FIRSTNAME");
                    String login = rs.getString("LOGIN");
                    String pwd = rs.getString("PASSWORD");
                    CopexUser user = new CopexUser(dbKey, login, pwd, name, firstname);
                    
                    // DETERMINE S'IL EST ELEVE OU ENSEIGNANT 
                    Statement stmt2 = c.createStatement();
                    String queryTeacher = "SELECT ID_TEACHER FROM TEACHER " +
                        "WHERE ID_TEACHER = "+dbKey+" ;" ;
                    ResultSet rs2 = stmt2.executeQuery(queryTeacher);
                    boolean isTeacher = false;
                    while(rs2.next()){
                      isTeacher = true;
                    }
                    user.setTeacher(isTeacher);
                    rs2.close();
                    String queryLearner = "SELECT ID_LEARNER FROM LEARNER " +
                        "WHERE ID_LEARNER = "+dbKey+" ;" ;
                   ResultSet rs3 = stmt2.executeQuery(queryLearner);
                    boolean isLearner = false;
                    while(rs3.next()){
                      isLearner = true;
                    }
                    user.setLearner(isLearner);
                    rs3.close();
                    stmt2.close();
                    listUser.add(user);
            }
             rs.close();
            stmt.close();
            v.add(listUser);
            return new CopexReturn();
        }catch ( SQLException e1 ){
		String messageError ="";
		System.out.println("\n--- SQLException caught getAllUserFromDB ---\n") ;
			
		while ( e1 != null ) {
			System.out.println("Message :   " + e1.getMessage()) ;
			System.out.println("SQLState :  " + e1.getSQLState()) ;
			System.out.println("ErrorCode : " + e1.getErrorCode()) ;
			messageError += "SQL error : " + e1.getErrorCode() + "\n" + e1.getMessage()+"\n";
			e1 = e1.getNextException() ;
			System.out.println("") ;
		}
		return new CopexReturn( messageError, false) ;
	}
	catch( Throwable t) {
  		String msg = "--- Throwable caught in method getAllUserFromDB  of class UserFromDB ---\n"+
  					 "Erreur Fatale ===> Exception : " + t.getMessage() ;
		System.out.println("\n"+msg);  					 
  		return new CopexReturn (msg,false);
	 }
    }
    
    
    /* identification */
    public static CopexReturn getUserFromDB(Connection c, String login, String pwd, Vector v) {
        CopexUser user = null;
        try{
           String query = "SELECT ID_USER, USER_NAME, FIRSTNAME, LOGIN, PASSWORD " +
                   "FROM COPEX_USER WHERE LOGIN = '"+login+"' AND PASSWORD = '"+pwd +"';";
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()){
                    long dbKey = rs.getLong("ID_USER");
                    String name = rs.getString("USER_NAME");
                    String firstname = rs.getString("FIRSTNAME");
                    user = new CopexUser(dbKey, login, pwd, name, firstname);
            }
             rs.close();
            stmt.close();
            if (user  != null)
                v.add(user);
            return new CopexReturn();
        }catch ( SQLException e1 ){
		String messageError ="";
		System.out.println("\n--- SQLException caught getUserFromDB ---\n") ;
			
		while ( e1 != null ) {
			System.out.println("Message :   " + e1.getMessage()) ;
			System.out.println("SQLState :  " + e1.getSQLState()) ;
			System.out.println("ErrorCode : " + e1.getErrorCode()) ;
			messageError += "SQL error : " + e1.getErrorCode() + "\n" + e1.getMessage()+"\n";
			e1 = e1.getNextException() ;
			System.out.println("") ;
		}
		return new CopexReturn( messageError, false) ;
	}
	catch( Throwable t) {
  		String msg = "--- Throwable caught in method getUserFromDB  of class UserFromDB ---\n"+
  					 "Erreur Fatale ===> Exception : " + t.getMessage() ;
		System.out.println("\n"+msg);  					 
  		return new CopexReturn (msg,false);
	 }
    }
    
    
    public static CopexReturn addUserInDB(Connection c, CopexUser user, Vector v){
       String name = user.getUserName() != null ? user.getUserName() :"";
        name =  AccesDB.replace("\'",name,"''") ;
        String firstname = user.getUserFirstName() != null ? user.getUserFirstName() :"";
        firstname =  AccesDB.replace("\'",firstname,"''") ;
        String login = user.getCopexLogin() != null ? user.getCopexLogin() :"";
        login =  AccesDB.replace("\'",login,"''") ;
        String pwd = user.getCopexPassWord() != null ? user.getCopexPassWord() :"";
        pwd =  AccesDB.replace("\'",pwd,"''") ;
        try{
            String query = "INSERT INTO COPEX_USER " +
                "(ID_USER, USER_NAME, FIRSTNAME, LOGIN, PASSWORD) " +
                "VALUES (NULL,'"+name+"', '"+firstname+"', '"+login+"', '"+pwd+"' );";
       
            
            long dbKey = -1;
            java.sql.Statement stmt = c.createStatement();
            stmt.executeUpdate(query);
            // recupere l'id :
            String queryID = "SELECT max(last_insert_id(`ID_USER`))   FROM  COPEX_USER ;";  
            ResultSet rs  = stmt.executeQuery(queryID);
            while (rs.next()){
                dbKey = (Long)rs.getObject("max(last_insert_id(`ID_USER`))");
            }
            rs.close();
            // creation 
            String queryTeacher = "INSERT INTO TEACHER (ID_TEACHER) " +
                    "VALUES ("+dbKey+") ;";
            if (user.isTeacher())
                stmt.executeUpdate(queryTeacher);
            String queryLearner = "INSERT INTO LEARNER (ID_LEARNER) " +
                    "VALUES ("+dbKey+") ;";
            if (user.isLearner())
                stmt.executeUpdate(queryLearner);
            stmt.close();
            v.add(dbKey);
            return new CopexReturn();
        }catch ( SQLException e1 ){
		String messageError ="";
		System.out.println("\n--- SQLException caught addUserInDB ---\n") ;
			
		while ( e1 != null ) {
			System.out.println("Message :   " + e1.getMessage()) ;
			System.out.println("SQLState :  " + e1.getSQLState()) ;
			System.out.println("ErrorCode : " + e1.getErrorCode()) ;
			messageError += "SQL error : " + e1.getErrorCode() + "\n" + e1.getMessage()+"\n";
			e1 = e1.getNextException() ;
			System.out.println("") ;
		}
		return new CopexReturn( messageError, false) ;
	}
	catch( Throwable t) {
  		String msg = "--- Throwable caught in method addUserInDB  of class UserDB ---\n"+
  					 "Erreur Fatale ===> Exception : " + t.getMessage() ;
		System.out.println("\n"+msg);  					 
  		return new CopexReturn (msg,false);
	 }
    }
    
    /* modification d'un user */
    public static CopexReturn updateUserInDB(Connection c, CopexUser user){
        long dbKey = user.getDbKey();
        String name = user.getUserName() != null ? user.getUserName() :"";
        name =  AccesDB.replace("\'",name,"''") ;
        String firstname = user.getUserFirstName() != null ? user.getUserFirstName() :"";
        firstname =  AccesDB.replace("\'",firstname,"''") ;
        String login = user.getCopexLogin() != null ? user.getCopexLogin() :"";
        login =  AccesDB.replace("\'",login,"''") ;
        String pwd = user.getCopexPassWord() != null ? user.getCopexPassWord() :"";
        pwd =  AccesDB.replace("\'",pwd,"''") ;
        try{
            String query = "UPDATE COPEX_USER SET USER_NAME = '"+name+"' ," +
                    "FIRSTNAME = '"+firstname+"', LOGIN = '"+login+"', PASSWORD = '"+pwd+"' WHERE " +
                    "ID_USER = "+dbKey+";";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(query);
            query = "DELETE FROM TEACHER WHERE ID_TEACHER  = "+dbKey+" ;";
            stmt.executeUpdate(query);
            query = "DELETE FROM LEARNER WHERE ID_LEARNER  = "+dbKey+" ;";
            String queryTeacher = "INSERT INTO TEACHER (ID_TEACHER) " +
                    "VALUES ("+dbKey+") ;";
            if (user.isTeacher())
                stmt.executeUpdate(queryTeacher);
            String queryLearner = "INSERT INTO LEARNER (ID_LEARNER) " +
                    "VALUES ("+dbKey+") ;";
            if (user.isLearner())
                stmt.executeUpdate(queryLearner);
            stmt.executeUpdate(query);
            stmt.close();
            return new CopexReturn();
        }catch ( SQLException e1 ){
		String messageError ="";
		System.out.println("\n--- SQLException caught updateUser ---\n") ;
			
		while ( e1 != null ) {
			System.out.println("Message :   " + e1.getMessage()) ;
			System.out.println("SQLState :  " + e1.getSQLState()) ;
			System.out.println("ErrorCode : " + e1.getErrorCode()) ;
			messageError += "SQL error : " + e1.getErrorCode() + "\n" + e1.getMessage()+"\n";
			e1 = e1.getNextException() ;
			System.out.println("") ;
		}
		return new CopexReturn( messageError, false) ;
	}
	catch( Throwable t) {
  		String msg = "--- Throwable caught in method updateUser  of class UserDB ---\n"+
  					 "Erreur Fatale ===> Exception : " + t.getMessage() ;
		System.out.println("\n"+msg);  					 
  		return new CopexReturn (msg,false);
	 }
    }
    
    /* suppression d'un user */
    public static CopexReturn deleteUserFromDB(Connection c, CopexUser user){
        long dbKey = user.getDbKey();
        try{
            String queryTeacher = "DELETE FROM TEACHER WHERE ID_TEACHER = "+dbKey+" ;";
            String queryLearner = "DELETE FROM LEARNER WHERE ID_LEARNER = "+dbKey+" ;";
            String query = "DELETE FROM COPEX_USER WHERE ID_USER = "+dbKey+" ;";
            String queryMission = "DELETE FROM LINK_MISSION_LEARNER WHERE ID_LEARNER = "+dbKey+" ;";
            String queryproc = "DELETE FROM LINK_MISSION_PROC WHERE ID_USER = "+dbKey+" ;";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(queryTeacher);
            stmt.executeUpdate(queryLearner);
            stmt.executeUpdate(queryMission);
            stmt.executeUpdate(queryproc);
            stmt.executeUpdate(query);
            stmt.close();
            return new CopexReturn();
        }catch ( SQLException e1 ){
		String messageError ="";
		System.out.println("\n--- SQLException caught deleteUserFromDB ---\n") ;
			
		while ( e1 != null ) {
			System.out.println("Message :   " + e1.getMessage()) ;
			System.out.println("SQLState :  " + e1.getSQLState()) ;
			System.out.println("ErrorCode : " + e1.getErrorCode()) ;
			messageError += "SQL error : " + e1.getErrorCode() + "\n" + e1.getMessage()+"\n";
			e1 = e1.getNextException() ;
			System.out.println("") ;
		}
		return new CopexReturn( messageError, false) ;
	}
	catch( Throwable t) {
  		String msg = "--- Throwable caught in method deleteUserFromDB  of class UserDB ---\n"+
  					 "Erreur Fatale ===> Exception : " + t.getMessage() ;
		System.out.println("\n"+msg);  					 
  		return new CopexReturn (msg,false);
	 }
    }
    
    public static CopexReturn getUserFromDB_xml(DataBaseCommunication dbC, long dbKeyUser, ArrayList v) {
        dbC.updateDb(MyConstants.DB_LABBOOK);
        CopexUser user = null;
        String query = "SELECT USER_NAME, FIRSTNAME FROM COPEX_USER WHERE ID_USER =  "+dbKeyUser+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("USER_NAME");
        listFields.add("FIRSTNAME");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String name = rs.getColumnData("USER_NAME");
            if (name == null)
                continue;
            String firstName = rs.getColumnData("FIRSTNAME");
            if (firstName == null)
                continue;
            
             user = new CopexUser(dbKeyUser, "", "", name, firstName);
        }
        dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
        v.add(user);
        return new CopexReturn();     
        
    }

    public static CopexReturn getEdPUserFromDB(DataBaseCommunication dbC, String idUser, String champ, ArrayList v){
        long dbKeyUser = -1;
        String query = "SELECT ID_USER FROM EDP_USERS WHERE "+champ+" = '"+idUser+"' ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_USER");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_USER");
            if (s == null)
                continue;
            try{
                dbKeyUser = Long.parseLong(s);
            }catch(NumberFormatException e){
            }
        }
        v.add(dbKeyUser);
        return new CopexReturn();
    }

    /* creation d'un utilisateur */
    public static CopexReturn createUserInDB(DataBaseCommunication dbC, CopexUser user, ArrayList v){
        String name = user.getUserName() != null ? user.getUserName() :"";
        name =  AccesDB.replace("\'",name,"''") ;
        String firstname = user.getUserFirstName() != null ? user.getUserFirstName() :"";
        firstname =  AccesDB.replace("\'",firstname,"''") ;
        String login = user.getCopexLogin() != null ? user.getCopexLogin() :"";
        login =  AccesDB.replace("\'",login,"''") ;
        String pwd = user.getCopexPassWord() != null ? user.getCopexPassWord() :"";
        pwd =  AccesDB.replace("\'",pwd,"''") ;
        String query = "INSERT INTO COPEX_USER " +
                "(ID_USER, USER_NAME, FIRSTNAME, LOGIN, PASSWORD) " +
                "VALUES (NULL,'"+name+"', '"+firstname+"', '"+login+"', '"+pwd+"' );";
       dbC.updateDb(MyConstants.DB_LABBOOK);
       // recupere l'id :
       String queryID = "SELECT max(last_insert_id(`ID_USER`))   FROM  COPEX_USER ;";
       ArrayList v2 = new ArrayList();
       CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
       if (cr.isError()){
           dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
           return cr;
       }
       long dbKey = (Long)v2.get(0);
       // creation
       String queryLearner = "INSERT INTO LEARNER (ID_LEARNER) " +
                    "VALUES ("+dbKey+") ;";
       if(user.isTeacher()){
           queryLearner = "INSERT INTO TEACHER (ID_TEACHER) " +
                    "VALUES ("+dbKey+") ;";
       }
       v2 = new ArrayList();
      String[]querys = new String[1];
      querys[0] = queryLearner ;
      cr = dbC.executeQuery(querys, v2);
      if (cr.isError())
        return cr;        

       v.add(dbKey);
       return new CopexReturn();

    }

    /* creation d'un utilisateur externe */
    public static CopexReturn createEdPUsersInDB(DataBaseCommunication dbC, long dbKeyUser, String exIdUser, String champ){
        String query = "INSERT INTO EDP_USERS (ID_USER, "+champ+") " +
                    "VALUES ("+dbKeyUser+", '"+exIdUser+"') ;";

        ArrayList v2 = new ArrayList();
        String[]querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }
}
