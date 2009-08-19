/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;


/**
 * utilitaires pour l'applet COPEX
 * @author MBO
 */
public class CopexUtilities {
    // CONSTANTES
    
    // METHODES
    /* retourne la longueur pixel d'un texte */
    static public int lenghtOfString(String s, FontMetrics fm) {
       if (fm == null || s == null)
            return 0;
       else 
           return fm.stringWidth(s);
    }
    
    /* remplace dans la chaine de caracteres un parametre par la valeur 
     * le parametre est sous la forme {i}
     */
    static public String replace(String s, int i, String rep){
        String p = "{"+i+"}";
        int id = s.indexOf(p);
        if (id != -1){
            String s1 = s.substring(0, id);
            String s2 = s.substring(id+p.length());
            s = s1+rep+s2;
        }
        return s;
    }
    
    /* retourne la date courante */
    static public java.sql.Date getCurrentDate(){
       java.sql.Date currentDate =  new java.sql.Date(Calendar.getInstance().getTimeInMillis()) ;
        return currentDate;
    }
    
    /* retourne la date en format String pour la bd */
    static public String dateToSQL(java.sql.Date date){
       if (date == null)
           return "";
        String d = date.toString();
        return d;
    }
    
    /* retourne le temps en format String pour la bd */
    static public String timeToSQL(java.sql.Time time){
       if (time == null)
           return "";
        String t = time.toString();
        return t;
    }
    /* retourne l'heure courante */
    static public java.sql.Time getCurrentTime(){
       java.sql.Time currentTime =  new java.sql.Time(Calendar.getInstance().getTimeInMillis()) ;
        return currentTime;
    }
    
    static public java.sql.Date getDate(String strDate){
        int firstID = strDate.indexOf("-") ,
		lastID = strDate.lastIndexOf("-") ;
	int year = 0 , month = 0  , date=0;
	if ( firstID > -1 && lastID > -1 ) {
		year = Integer.parseInt(strDate.substring(lastID + 1, strDate.length())) ;
		month = Integer.parseInt(strDate.substring(firstID + 1, lastID)) ;
		date = Integer.parseInt(strDate.substring(0, firstID)) ;
	}
	else {
		year = Integer.parseInt(strDate.substring(4,8));
		month = Integer.parseInt(strDate.substring(2,4)); 
		date = Integer.parseInt(strDate.substring(0,2));
	
	}
	
	Calendar c = Calendar.getInstance();
	c.set(year , month-1, date) ;
        long msDate  = c.getTime().getTime();
        c = null;
        return new java.sql.Date(msDate);
	
    }

    /* retourne extension du fichier */
    public static String getExtensionFile(File file){
        String fileName = file.getAbsolutePath() ;
        int idP = fileName.indexOf(".");
        if (idP== -1)
            return "";
        else{
            return fileName.substring(idP+1, fileName.length());
        }
    }

    /* ajoute une extension au fichier */
    public static  File setExtensionFile(File file, String extension){
        String pathName = file.getAbsolutePath() ;
        pathName += "."+extension;
        File f = new File(pathName);
        return f;
    }

    // METHODES
    /**
     * On replace la chaine toReplace dans la chaine inText par la chaine newTextToReplace.
     * @return java.lang.String
     * @param toReplace java.lang.String
     * @param inText java.lang.String
     */
    public static String replace(String toReplace, String inText, String newTextToReplace) {
	if (inText == null )
            return null;
	String newText="";
	int j=inText.indexOf(toReplace);
	if (j!=-1){
		int i = 0;
		while(i<inText.length()){
			newText += inText.substring(i,j)+newTextToReplace;
			int l=inText.indexOf(toReplace,j+1);
			if (l==-1) {
                            if (j+1 < inText.length())
				newText+=inText.substring(j+1,inText.length());
                            break;
			}
			else {
                            i=j+1;
                            j=l;
			}
		}
	}
	else
            newText=inText;
	return newText;
    }

    public static Element stringToXml(String string){
		StringReader stringReader = new StringReader(string);
		try{
			SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(stringReader);
			return doc.getRootElement();
		}
		catch (Exception e){
			System.out.println("problems converting string status to jdom "+ e);
			return null;
		}
	}

    public static String xmlToString(Element element){
		StringWriter stringWriter = new StringWriter();
		try{
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            xmlOutputter.output(element, stringWriter);
		}
		catch (IOException e){
			System.out.println("problems converting jdom status to string"+ e);
		}
		return stringWriter.toString();
	}

    public static String getUTF8String(String s){
        byte[] bytes;
        try {
            bytes = s.getBytes("UTF8");
            s = new String(bytes);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CopexUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

}
