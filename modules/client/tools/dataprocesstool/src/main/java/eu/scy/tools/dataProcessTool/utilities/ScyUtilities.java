/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

import java.awt.FontMetrics;

/**
 * utilitaires pour projets SCY
 * @author Marjolaine Bodin
 */
public class ScyUtilities {
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
}
