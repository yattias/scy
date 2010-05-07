/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
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

    public static boolean isXMLFile(File file){
        String ext = getExtensionFile(file);
        return ext.equals("xml");
    }

    public static boolean isCSVFile(File file){
        String ext = getExtensionFile(file);
        return ext.equals("csv");
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
}
