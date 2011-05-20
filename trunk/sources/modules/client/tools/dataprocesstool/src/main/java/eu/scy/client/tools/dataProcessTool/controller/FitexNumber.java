/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.controller;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * manage the different format of double
 * @author Marjolaine
 */
public class FitexNumber {
    public static final String SCIENTIFIC_NOTATION_E = "E";

    
    public static String getFormat(String stringValue, boolean scientificNotation, int nbShownDecimals, int nbSignificantDigits, Locale locale){
        if(scientificNotation){
            if(nbSignificantDigits == DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED){
                int nb = getNbSignificantDigits(stringValue);
                return getFormat(stringValue,scientificNotation, nbShownDecimals, nb, locale);
            }else{
                NumberFormat numberFormat = getNumberFormat(locale);
                DecimalFormat dformat = (DecimalFormat)numberFormat;
                dformat.applyPattern("0E0");
                dformat.setMaximumFractionDigits(nbSignificantDigits-1);
                dformat.setMinimumFractionDigits(nbSignificantDigits-1);
                return dformat.format(getDoubleValue(stringValue));
            }
        }else{
            if(nbShownDecimals == DataConstants.NB_DECIMAL_UNDEFINED){
                int nb = getNbSignificantDigits(stringValue);
                NumberFormat numberFormat = getNumberFormat(locale);
                return setNbSignificantDigitTo(numberFormat.format(getDoubleValue(stringValue)), nb, locale);
            }else{
                NumberFormat numberFormat = getNumberFormat(locale);
                DecimalFormat dformat = (DecimalFormat)numberFormat;
                dformat.applyPattern("0.0");
                dformat.setMinimumFractionDigits(nbShownDecimals);
                dformat.setMaximumFractionDigits(nbShownDecimals);
                return dformat.format(getDoubleValue(stringValue));
            }
        }
    }

    public static NumberFormat getNumberFormat(Locale locale){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        numberFormat.setGroupingUsed(false);
        return numberFormat;
    }


    public static double getDoubleValue(String stringValue){
        try{
            return Double.parseDouble(stringValue.replace(",", "."));
        }catch(NumberFormatException e){
            return Double.NaN;
        }
    }

    public static int getNbSignificantDigits(String d){
        int idE = d.toUpperCase().indexOf(SCIENTIFIC_NOTATION_E);
        String s = "";
        if(idE == -1){
            // without scientific notation
            s = getStringWithoutZero(d);
        }else{
            // scientific notation xEy
            s = getStringWithoutZero(getXeY_X(d));
        }
        int idP = indexOfDecimalSeparator(s);
        if(idP != -1)
            return s.length()-1;
        return s.length();
    }

    private static String getStringWithoutZero(String s){
        String myS = s;
        while(myS.length() > 0 && (myS.startsWith("0") || myS.startsWith(".") || myS.startsWith(","))){
            myS = myS.substring(1);
        }
        return myS;
    }

    private static String getXeY_X(String xEy){
        int idE = xEy.toUpperCase().indexOf(SCIENTIFIC_NOTATION_E);
        if(idE != -1){
            return xEy.substring(0, idE);
        }
        return xEy;
    }

    private static String getXeY_Y(String xEy){
        int idE = xEy.toUpperCase().indexOf(SCIENTIFIC_NOTATION_E);
        if(idE != -1 && idE < xEy.length()-1){
            return xEy.substring(idE+1);
        }
        return "";
    }
    
    private static int indexOfDecimalSeparator(String s){
        int id = s.indexOf(".");
        if(id ==-1){
            id = s.indexOf(",");
        }
        return id;
    }

    private static String setNbSignificantDigitTo(String d, int nb, Locale locale){
        String sep = getDecimalSeparator(locale);
        int nbS = getNbSignificantDigits(d);
        int idP = indexOfDecimalSeparator(d);
        while (nbS < nb){
            idP = indexOfDecimalSeparator(d);
            if(idP == -1){
                d += sep+"0";
            }else{
                d += "0";
            }
            nbS++;
        }
        return d;
    }

    private static String getDecimalSeparator(Locale locale){
        NumberFormat numberFormat = getNumberFormat(locale);
        String s = numberFormat.format(1.2);
        if(s.contains(","))
            return ",";
        else
            return ".";
    }

    /*format the sum result */
    public static String getSumValue(ArrayList<String> numberList, String result, Locale locale){
        int rs = getMaxSignificantRank(numberList);
        if(rs == 0){
            // show an integer
            return getFormat(""+Math.floor(getDoubleValue(result)), false, 0, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, locale);
        }else if (rs > 0){
            // scientific notation
            int nbSigDig = getMaxY(numberList)- rs + 1;
            return getFormat(result, true, DataConstants.NB_DECIMAL_UNDEFINED,nbSigDig, locale);
        }else{
            // rs <0
            int fp = getFloorPart(result);
            if (fp  == 0){
                int nbSigDig =getNbSigDigWithRank(result, -rs+1);
                return getFormat(result, true, DataConstants.NB_DECIMAL_UNDEFINED, nbSigDig, locale);
            }else{
                return getFormat(result, false, -rs, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, locale);
            }
        }
    }

    /*format the avg result */
    public static String getAvgValue(ArrayList<String> numberList, String sumResult, String result, Locale locale){
        int nbSDSum = getNbSignificantDigits(sumResult);
        int floorPartAvg = getFloorPart(result);
        int nbNumberInFoorPartAvg = (""+floorPartAvg).length();
        if(floorPartAvg == 0){
            // scientific notation
            return getFormat(result, true, DataConstants.NB_DECIMAL_UNDEFINED, nbSDSum, locale);
        }
        if(nbNumberInFoorPartAvg ==  nbSDSum){
            return getFormat(""+Math.floor(getDoubleValue(result)), false, 0, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, locale);
        }else if (nbNumberInFoorPartAvg > nbSDSum){
            // scientific notation
            return getFormat(result, true, DataConstants.NB_DECIMAL_UNDEFINED, nbSDSum, locale);
        }else{
//            if(floorPartAvg == 0){
//                // scientific notation
//                return getFormat(result, true, DataConstants.NB_DECIMAL_UNDEFINED, nbSDSum, locale);
//            }else{
                int nbD = nbSDSum - nbNumberInFoorPartAvg;
                return getFormat(result, false, nbD, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, locale);
//            }
        }
    }

    private static int getMaxSignificantRank(ArrayList<String> numberList){
        int nb = numberList.size();
        int maxRs;
        if(nb > 0){
            maxRs = getSignificantRank(numberList.get(0));
            for(int i=1; i<nb; i++){
                maxRs = Math.max(maxRs, getSignificantRank(numberList.get(i)));
            }
        }else{
            maxRs = 0;
        }
        return maxRs;
    }

    private static int getSignificantRank(String d){
        int  rs = -getNbDecimals(getXeY_X(d)) + getValueOf(getXeY_Y(d));
        return rs;
    }

    private static int getValueOf(String n){
        if(n == null || n.equals(""))
            return 0;
        else{
            try{
                int t = Integer.parseInt(n);
                return t;
            }catch(NumberFormatException e){
                return 0;
            }
        }
    }

    private static int getNbDecimals(String s){
        int idP = indexOfDecimalSeparator(s);
        if(idP == -1 || idP >= s.length()-1)
            return 0;
        else{
            return s.substring(idP+1).length();
        }
    }

    private static int getFloorPart(String d){
        int idP = indexOfDecimalSeparator(d);
        if(idP == 0){
            try{
                Integer i = Integer.parseInt(d);
                return i;
            }catch(NumberFormatException e){
                return 0;
            }
        }else{
            String s = d.substring(0, idP);
            try{
                Integer i = Integer.parseInt(s);
                return i;
            }catch( NumberFormatException e){
                return 0;
            }
        }
    }

    private static int getMaxY(ArrayList<String> numberList){
        int nb = numberList.size();
        int maxY;
        if(nb > 0){
            maxY = getIntegerValue(getXeY_Y(numberList.get(0)));
            for(int i=1; i<nb; i++){
                maxY = Math.max(maxY, getIntegerValue(getXeY_Y(numberList.get(i))));
            }
        }else{
            maxY = 0;
        }
        return maxY;
    }

    private static int getIntegerValue(String s){
        if(s == null || s.equals(""))
            return 0;
        try{
            int i = Integer.parseInt(s);
            return i;
        }catch(NumberFormatException e){
            return 0;
        }
    }

    private static int getNbSigDigWithRank(String s, int rs){
        if(rs < s.length())
            return getNbSignificantDigits(s.substring(0, rs+1));
        return getNbSignificantDigits(s);
    }
}
