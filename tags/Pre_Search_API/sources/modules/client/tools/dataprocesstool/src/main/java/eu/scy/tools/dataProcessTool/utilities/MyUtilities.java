/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

import eu.scy.tools.dataProcessTool.common.LocalText;
import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * utilitaires
 * @author Marjolaine Bodin
 */
public class MyUtilities {
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
			// System.out.println("problems converting string status to jdom "+ e);
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
			// System.out.println("problems converting jdom status to string"+ e);
		}
		return stringWriter.toString();
	}

    public static boolean isXMLFile(File file){
        String ext = getExtensionFile(file);
        return ext.equals("xml");
    }

    public static boolean isCSVFile(File file){
        String ext = getExtensionFile(file);
        return ext.equals("csv");
    }

    public static boolean isGMBLFile(File file){
        String ext = getExtensionFile(file);
        return ext.equals("gmbl");
    }

    private static  String getExtensionFile(File file){
        int id = file.getName().lastIndexOf(".");
        if(id == -1 || id==file.getName().length()-1)
            return "";
        return file.getName().substring(id+1);
    }

    public static File getXMLFile(File file){
        return new File(file.getParent(), file.getName()+".xml");
    }
     public static File getCSVFile(File file){
        return new File(file.getParent(), file.getName()+".csv");
    }

    /**
     * Arrondi d'un double avec n éléments après la virgule.
    * @param a La valeur à convertir.
    * @param n Le nombre de décimales à conserver.
    * @return La valeur arrondi à n décimales.
    */
    public static double floor(double a, int n) {
	double p = Math.pow(10.0, n);
	return Math.floor((a*p)+0.5) / p;
    }

    /* tri la liste du plus grand au plus petit */
    public static ArrayList<Integer> getSortList(ArrayList<Integer> list){
        int nb = list.size();
        for (int i=1; i<nb; i++){
            if (list.get(i)> list.get(i-1)){
                int val = list.get(i);
                for (int j=0; j<i; j++){
                    if (list.get(j) < val ){
                        list.remove(i);
                        list.add(j, val);
                        break;
                    }
                }
            }
        }
        return list;
    }

    public static String getText(List<LocalText> list, Locale locale){
        for (Iterator<LocalText> t= list.iterator(); t.hasNext();) {
            LocalText locaText = t.next();
            if(locaText.getLocale().getLanguage().equals(locale.getLanguage()))
                return locaText.getText();
        }
        // try to load in en
        for (Iterator<LocalText> t= list.iterator(); t.hasNext();) {
            LocalText locaText = t.next();
            if(locaText.getLocale().getLanguage().equals("en"))
                return locaText.getText();
        }
        for (Iterator<LocalText> t= list.iterator(); t.hasNext();) {
            return t.next().getText();
        }
        return null;

    }

    public static String getTextWithoutHTML(String s){
        if(s == null)
            return s;
        int id1 = s.indexOf("<");
        int id2 = s.indexOf(">");
        while(id1 != -1 && id2 !=-1){
            String end = "";
            if(id2<s.length()-1){
                end = s.substring(id2+1, s.length());
            }
            s = s.substring(0, id1)+end;
            id1 = s.indexOf("<");
            id2 = s.indexOf(">");
        }
        return s;
    }
}
