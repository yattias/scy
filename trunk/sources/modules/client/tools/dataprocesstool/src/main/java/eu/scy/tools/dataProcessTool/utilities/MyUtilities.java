/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

import java.awt.FontMetrics;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
}
