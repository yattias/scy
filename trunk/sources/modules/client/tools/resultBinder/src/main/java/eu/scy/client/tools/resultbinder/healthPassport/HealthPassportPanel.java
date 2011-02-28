/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HealthPassportPanel2.java
 *
 * Created on 23 févr. 2011, 09:48:39
 */

package eu.scy.client.tools.resultbinder.healthPassport;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.resultbinder.common.ResultCard;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

/**
 *
 * @author Marjolaine
 */
public class HealthPassportPanel extends javax.swing.JPanel {

    private final static Color TITLE_COLOR = Color.RED;
    private final static Font TITLE_FONT = new Font("Arial", Font.BOLD, 36);
    private final static Color NO_PICTURE_COLOR = new Color(0, 255, 102);
    private final static Color BORDER_PICTURE_COLOR = Color.BLACK;
    private final static Font FONT_LABEL = new Font(Font.DIALOG, Font.PLAIN, 12);
    private final static Color COLOR_NAME = new Color(102,102, 102);
    private final static Color COLOR_BMI = new Color(0, 51, 204);
    private final static Color COLOR_EER = new Color(0, 153, 153);
    private final static Color COLOR_EVALUATION = Color.BLACK;
    public final static int HEALTH_PASSPORT_WIDTH = 700;
    public final static int HEALTH_PASSPORT_HEIGHT = 540;

    private ResultCard resultCardHealthPassport = null;
    private String userName;
    private HealthPassportEnum healthPassportEnum;

     /* ressource bundle */
    private ResourceBundleWrapper bundle;
    private NumberFormat numberFormat;

    private IActionHealthPassport actionHealthPassport;

    public HealthPassportPanel(ResourceBundleWrapper bundle, ResultCard resultCardHealthPassport, String username) {
        super();
        this.bundle = bundle;
        this.userName = username;
        this.resultCardHealthPassport = resultCardHealthPassport;
        initComponents();
        initGUI();
    }

    public void addActionResultBinder(IActionHealthPassport actionHealthPassport){
        this.actionHealthPassport = actionHealthPassport;
    }
    
    /** Creates new form HealthPassportPanel2 */
    public HealthPassportPanel() {
        initComponents();
    }

    private void initGUI(){
        numberFormat = NumberFormat.getNumberInstance(getLocale());
        numberFormat.setGroupingUsed(false);
        initHealthPassport();
        ButtonGroup group = new ButtonGroup();
        group.add(rbBoy);
        group.add(rbGirl);
        setPreferredSize(new Dimension(HEALTH_PASSPORT_WIDTH, HEALTH_PASSPORT_HEIGHT));
        setSize(HEALTH_PASSPORT_WIDTH, HEALTH_PASSPORT_HEIGHT);
        labelTitle.setFont(TITLE_FONT);
        labelTitle.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_TITLE"));
        pictureLabel.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_NO_PICTURE"));
        labelName.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_NAME"));
        labelBirthDate.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_BIRTH_DATE"));
        labelWeight.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_WEIGHT"));
        labelHeight.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_HEIGHT"));
        rbBoy.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_BOY"));
        rbGirl.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_GIRL"));
        labelBMI.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_BMI"));
        labelHeartRate.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_HEART_RATE"));
        labelBMR.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_BMR"));
        labelCalorieIntake.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_DAILY_CALORIE_INTAKE"));
        labelEER.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_EER"));
        labelCalorieBalance.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_CALORIE_BALANCE"));
        labelEvaluation.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_EVALUATION"));
        labelConclusion.setText(bundle.getString("RESULTBINDER.HEALTH_PASSPORT_CONCLUSION"));

    }

    public void updateHealthPassport(ResultCard resultCardHealthPassport){
         this.resultCardHealthPassport = resultCardHealthPassport;
         updateElements();
     }

     public void initHealthPassport(){
         resultCardHealthPassport = new ResultCard();
         for (HealthPassportEnum v : HealthPassportEnum.values()) {
            resultCardHealthPassport.getResultCard().put(v.name(), "");
         }
         updateElements();
     }

     private String getValue(String key){
         Object o = resultCardHealthPassport.getResultCard().get(key);
         if(o == null)
             return "";
         else
             return o.toString();
     }


     private void checkBirthDate(){
         String oldV = getValue(HealthPassportEnum.BIRTH_DATE.name());
         String s = fieldBirthDate.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.BIRTH_DATE.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.BIRTH_DATE.name(), "");
             }
         }else{
             boolean isok = controlBirthDate(s);
             if(isok){
                 resultCardHealthPassport.getResultCard().put(HealthPassportEnum.BIRTH_DATE.name(), s);
                 if(!oldV.equals(s)){
                    logValueChanged(HealthPassportEnum.BIRTH_DATE.name(), s);
                }
             }else{
                 fieldBirthDate.setText(resultCardHealthPassport.getResultCard().get(HealthPassportEnum.BIRTH_DATE.name()) == null ?"" :resultCardHealthPassport.getResultCard().get(HealthPassportEnum.BIRTH_DATE.name()).toString());
             }
         }

     }

     private boolean controlBirthDate(String s){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date birthDate = df.parse(s);
            Date today = new Date();
            Calendar cBirthday = new GregorianCalendar();
            Calendar cToday = new GregorianCalendar();
            cBirthday.setTime(birthDate);
            cToday.setTime(today);
            if(cToday.before(cBirthday)){
                JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_BIRTHDATE_BEFORE_TODAY") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_BIRTHDATE_FORMAT") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
     }

     private double getDoubleValue(String key){
         Object o = resultCardHealthPassport.getResultCard().get(key);
         if(o == null || o.equals("")){
             return Double.NaN;
         }else{
             try{
                 double d = Double.parseDouble(o.toString());
                 return d;
             }catch(NumberFormatException ex){
                 return Double.NaN;
             }
         }
     }

     

     private Date getBirthDate(){
         Object o = resultCardHealthPassport.getResultCard().get(HealthPassportEnum.BIRTH_DATE.name());
         if(o == null){
             return null;
         }else{
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date birthDate = df.parse(o.toString());
                return birthDate;
             }catch(ParseException ex){
                 return null;
             }
         }
     }

     private boolean isMale(){
         Object o = resultCardHealthPassport.getResultCard().get(HealthPassportEnum.GENDER.name());
         if(o == null || o.equals("")){
             return true;
         }else{
            return (o.toString().equals("true"));
         }
     }

     private double getCalorieIntake(){
         Object o = resultCardHealthPassport.getResultCard().get(HealthPassportEnum.DAILY_CALORIE_INTAKE.name());
         if(o == null){
             return Double.NaN;
         }else{
             try{
                 double d = Double.parseDouble(o.toString());
                 return d;
             }catch(NumberFormatException ex){
                 return Double.NaN;
             }
         }
     }

      private double getEER(){
         Object o = resultCardHealthPassport.getResultCard().get(HealthPassportEnum.EER.name());
         if(o == null){
             return Double.NaN;
         }else{
             try{
                 double d = Double.parseDouble(o.toString());
                 return d;
             }catch(NumberFormatException ex){
                 return Double.NaN;
             }
         }
     }

     /** BMI, body mass index = weight / height² */
    private void computeBMI(){
        double weight = getDoubleValue(HealthPassportEnum.WEIGHT.name());
        double height = getDoubleValue(HealthPassportEnum.HEIGHT.name());
        double bmi = Double.NaN;
        if (!Double.isNaN(weight) && !Double.isNaN(height) && height != 0){
            bmi = weight / (Math.pow(height, 2));
        }
    }

    /** BMR, basal metabolic rate, depends  male/female :
     *  BMR = (10,0 * weight) + (6.25 * height) - (5.0 * age) + 5 (male)
     *  BMR = (10,0 * weight) + (6.25 * height) - (5.0 * age) + 161 (female)
     */
    private void computeBMR(){
        double weight = getDoubleValue(HealthPassportEnum.WEIGHT.name());
        double height = getDoubleValue(HealthPassportEnum.HEIGHT.name());
        Date birthDate = getBirthDate();
        double bmr = Double.NaN;
        if(!Double.isNaN(weight) && !Double.isNaN(height ) && birthDate != null){
            double age = getAge();
            bmr = (10.0 * weight) + (6.25 * height) - (5.0 * age);
            if(isMale()){
                bmr += 5;
            }else{
                bmr -= 161;
            }
        }
    }

    /*How old are you?*/
    private int getAge(){
        Date birthDate = getBirthDate();
        if(birthDate == null){
            return 0;
        }else{
            Date today = new Date();
            Calendar cBirthday = new GregorianCalendar();
            Calendar cToday = new GregorianCalendar();
            cBirthday.setTime(birthDate);
            cToday.setTime(today);

            int yearDiff = cToday.get(Calendar.YEAR) - cBirthday.get(Calendar.YEAR);
            cBirthday.set(Calendar.YEAR, cToday.get(Calendar.YEAR));
            if (cBirthday.before(cToday)) {
                return yearDiff;
            } else {
                return Math.max(0, yearDiff-1);
            }
        }
    }

    /** Calorie Balance = calorie intake - eer */
    private void computeCalorieBalance(){
        double calorieIntake = getCalorieIntake();
        double eer = getEER();
        double calorieBalance = Double.NaN;
        if(!Double.isNaN(calorieIntake) && !Double.isNaN(eer)){
            calorieBalance = calorieIntake - eer;
            resultCardHealthPassport.getResultCard().put(HealthPassportEnum.CALORIE_BALANCE.name(), calorieBalance);
            fieldCalorieBalance.setText(numberFormat.format(calorieBalance));
        }else{
            calorieBalance = Double.NaN;
            resultCardHealthPassport.getResultCard().put(HealthPassportEnum.CALORIE_BALANCE.name(), null);
            fieldCalorieBalance.setText("");
        }
    }

    private void checkWeight(){
        String oldV = getValue(HealthPassportEnum.WEIGHT.name());
         String s = fieldWeight.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.WEIGHT.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.WEIGHT.name(), "");
             }
         }else{
             try{
                 double w = Double.parseDouble(s.replace(',', '.'));
                 resultCardHealthPassport.getResultCard().put(HealthPassportEnum.WEIGHT.name(), w);
                 if(!oldV.equals(Double.toString(w))){
                    logValueChanged(HealthPassportEnum.WEIGHT.name(), Double.toString(w));
                }
             }catch(NumberFormatException ex){
                 JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_WEIGHT_FORMAT") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
                 String oldW = "";
                 double we = getDoubleValue(HealthPassportEnum.WEIGHT.name());
                 if(!Double.isNaN(we)){
                     oldW = numberFormat.format(we);
                 }
                 fieldWeight.setText(oldW);
             }
         }
     }

    private void checkHeight(){
         String oldV = getValue(HealthPassportEnum.HEIGHT.name());
         String s = fieldHeight.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.HEIGHT.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.HEIGHT.name(), "");
             }
         }else{
             try{
                 double w = Double.parseDouble(s.replace(',', '.'));
                 resultCardHealthPassport.getResultCard().put(HealthPassportEnum.HEIGHT.name(), w);
                 if(!oldV.equals(Double.toString(w))){
                    logValueChanged(HealthPassportEnum.HEIGHT.name(), Double.toString(w));
                }
             }catch(NumberFormatException ex){
                 JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_HEIGHT_FORMAT") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
                 String oldH = "";
                 double he = getDoubleValue(HealthPassportEnum.HEIGHT.name());
                 if(!Double.isNaN(he)){
                     oldH = numberFormat.format(he);
                 }
                 fieldHeight.setText(oldH);
             }
         }
     }

    private void checkGender(boolean isMale){
        String oldV = getValue(HealthPassportEnum.GENDER.name());
        resultCardHealthPassport.getResultCard().put(HealthPassportEnum.GENDER.name(),Boolean.toString(isMale));
        if(!oldV.equals(Boolean.toString(isMale))){
            logValueChanged(HealthPassportEnum.GENDER.name(),Boolean.toString(isMale));
        }
    }

    private void checkGender(){
        checkGender(rbBoy.isSelected());
    }

    private void checkBMI(){
        String oldV = getValue(HealthPassportEnum.BMI.name());
         String s = fieldBMI.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.BMI.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.BMI.name(), "");
             }
         }else{
             try{
                 double w = Double.parseDouble(s.replace(',', '.'));
                 resultCardHealthPassport.getResultCard().put(HealthPassportEnum.BMI.name(), w);
                 if(!oldV.equals(Double.toString(w))){
                    logValueChanged(HealthPassportEnum.BMI.name(), Double.toString(w));
                 }
             }catch(NumberFormatException ex){
                 JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_BMI_FORMAT") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
                 String old = "";
                 double n = getDoubleValue(HealthPassportEnum.BMI.name());
                 if(!Double.isNaN(n)){
                     old = numberFormat.format(n);
                 }
                 fieldBMI.setText(old);
             }
         }
     }

    private void checkHeartRate(){
        String oldV = getValue(HealthPassportEnum.HEART_RATE.name());
         String s = fieldHeartRate.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.HEART_RATE.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.HEART_RATE.name(), "");
             }
         }else{
             try{
                 double w = Double.parseDouble(s.replace(',', '.'));
                 resultCardHealthPassport.getResultCard().put(HealthPassportEnum.HEART_RATE.name(), w);
                 if(!oldV.equals(Double.toString(w))){
                    logValueChanged(HealthPassportEnum.HEART_RATE.name(), Double.toString(w));
                 }
             }catch(NumberFormatException ex){
                 JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_HEART_RATE_FORMAT") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
                 String old = "";
                 double n = getDoubleValue(HealthPassportEnum.HEART_RATE.name());
                 if(!Double.isNaN(n)){
                     old = numberFormat.format(n);
                 }
                 fieldHeartRate.setText(old);
             }
         }
     }

    private void checkBMR(){
         String oldV = getValue(HealthPassportEnum.BMR.name());
         String s = fieldBMR.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.BMR.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.BMR.name(), "");
             }
         }else{
             try{
                 double w = Double.parseDouble(s.replace(',', '.'));
                 resultCardHealthPassport.getResultCard().put(HealthPassportEnum.BMR.name(), w);
                 if(!oldV.equals(Double.toString(w))){
                    logValueChanged(HealthPassportEnum.BMR.name(), Double.toString(w));
                 }
             }catch(NumberFormatException ex){
                 JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_BMR_FORMAT") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
                 String old = "";
                 double n = getDoubleValue(HealthPassportEnum.BMR.name());
                 if(!Double.isNaN(n)){
                     old = numberFormat.format(n);
                 }
                 fieldBMR.setText(old);
             }
         }
     }

    private void checkCalorieIntake(){
        String oldV = getValue(HealthPassportEnum.DAILY_CALORIE_INTAKE.name());
         String s = fieldCalorieIntake.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.DAILY_CALORIE_INTAKE.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.DAILY_CALORIE_INTAKE.name(), "");
             }
         }else{
             try{
                 double w = Double.parseDouble(s.replace(',', '.'));
                 resultCardHealthPassport.getResultCard().put(HealthPassportEnum.DAILY_CALORIE_INTAKE.name(), w);
                 if(!oldV.equals(Double.toString(w))){
                    logValueChanged(HealthPassportEnum.DAILY_CALORIE_INTAKE.name(), Double.toString(w));
                 }
             }catch(NumberFormatException ex){
                 JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_CALORIE_INTAKE_FORMAT") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
                 String old = "";
                 double n = getDoubleValue(HealthPassportEnum.DAILY_CALORIE_INTAKE.name());
                 if(!Double.isNaN(n)){
                     old = numberFormat.format(n);
                 }
                 fieldCalorieIntake.setText(old);
             }
         }
         computeCalorieBalance();
     }

    private void checkEER(){
        String oldV = getValue(HealthPassportEnum.EER.name());
         String s = fieldEER.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.EER.name(), "");
              if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.EER.name(), "");
             }
         }else{
             try{
                 double w = Double.parseDouble(s.replace(',', '.'));
                 resultCardHealthPassport.getResultCard().put(HealthPassportEnum.EER.name(), w);
                 if(!oldV.equals(Double.toString(w))){
                    logValueChanged(HealthPassportEnum.EER.name(), Double.toString(w));
                 }
             }catch(NumberFormatException ex){
                 JOptionPane.showMessageDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_EER_FORMAT") , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.ERROR_MESSAGE);
                 String old = "";
                 double n = getDoubleValue(HealthPassportEnum.EER.name());
                 if(!Double.isNaN(n)){
                     old = numberFormat.format(n);
                 }
                 fieldEER.setText(old);
             }
         }
         computeCalorieBalance();
     }

    private void checkEvaluation(){
        String oldV = getValue(HealthPassportEnum.EVALUATION.name());
         String s = editorEvaluation.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.EVALUATION.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.EVALUATION.name(), "");
             }
         }else{
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.EVALUATION.name(), s);
             if(!oldV.equals(s)){
                 logValueChanged(HealthPassportEnum.EVALUATION.name(),s);
             }
         }
     }

    private void checkConclusion(){
        String oldV = getValue(HealthPassportEnum.CONCLUSION.name());
         String s = editorConclusion.getText();
         if(s==null || s.equals("")){
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.CONCLUSION.name(), "");
             if(!oldV.equals("")){
                 logValueChanged(HealthPassportEnum.CONCLUSION.name(), "");
             }
         }else{
             resultCardHealthPassport.getResultCard().put(HealthPassportEnum.CONCLUSION.name(), s);
             if(!oldV.equals(s)){
                 logValueChanged(HealthPassportEnum.CONCLUSION.name(),s);
             }
         }
     }

    private void checkAll(){
        checkBirthDate();
        checkWeight();
        checkHeight();
        checkGender();
        checkBMI();
        checkHeartRate();
        checkBMR();
        checkCalorieIntake();
        checkEER();
        checkEvaluation();
        checkConclusion();
    }
     

    /** logs */
    private void logValueChanged(String valueName, String value){
        if(actionHealthPassport != null)
            actionHealthPassport.logAction(valueName, value);
    }


     private void updateElements(){
         fieldName.setText(userName);
         fieldBirthDate.setText(resultCardHealthPassport.getResultCard().get(HealthPassportEnum.BIRTH_DATE.name()) == null ? "":resultCardHealthPassport.getResultCard().get(HealthPassportEnum.BIRTH_DATE.name()).toString());
         double weight = getDoubleValue(HealthPassportEnum.WEIGHT.name());
         fieldWeight.setText(Double.isNaN(weight) ?"": numberFormat.format(weight));
         double userHeight = getDoubleValue(HealthPassportEnum.HEIGHT.name());
         fieldHeight.setText(Double.isNaN(userHeight) ?"": numberFormat.format(userHeight));
         boolean g = isMale();
         rbBoy.setSelected(g);
         rbGirl.setSelected(!g);
         double bmi = getDoubleValue(HealthPassportEnum.BMI.name());
         fieldBMI.setText(Double.isNaN(bmi) ?"": numberFormat.format(bmi));
         double heartRate = getDoubleValue(HealthPassportEnum.HEART_RATE.name());
         fieldHeartRate.setText(Double.isNaN(heartRate) ?"": numberFormat.format(heartRate));
         double bmr = getDoubleValue(HealthPassportEnum.BMR.name());
         fieldBMR.setText(Double.isNaN(bmr) ?"": numberFormat.format(bmr));
         double calorieIntake = getDoubleValue(HealthPassportEnum.DAILY_CALORIE_INTAKE.name());
         fieldCalorieIntake.setText(Double.isNaN(calorieIntake) ?"": numberFormat.format(calorieIntake));
         double eer = getDoubleValue(HealthPassportEnum.EER.name());
         fieldEER.setText(Double.isNaN(eer ) ?"": numberFormat.format(eer));
         double calorieBalance = getDoubleValue(HealthPassportEnum.CALORIE_BALANCE.name());
         fieldCalorieBalance.setText(Double.isNaN(calorieBalance) ?"": numberFormat.format(calorieBalance));
         editorEvaluation.setText(resultCardHealthPassport.getResultCard().get(HealthPassportEnum.EVALUATION.name()) == null ?"" :resultCardHealthPassport.getResultCard().get(HealthPassportEnum.EVALUATION.name()).toString());
         editorConclusion.setText(resultCardHealthPassport.getResultCard().get(HealthPassportEnum.CONCLUSION.name()) == null ?"" :resultCardHealthPassport.getResultCard().get(HealthPassportEnum.CONCLUSION.name()).toString());
     }

    public ResultCard getResultCardHealthPassport() {
        checkAll();
        return resultCardHealthPassport;
    }

    public Container getInterfacePanel(){
        panelName.setSize(128,128);
        return panelName;
    }

    public void setUserName(String userName){
        this.userName = userName;
        fieldName.setText(userName);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTitle = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();
        panelData = new javax.swing.JPanel();
        pictureLabel = new javax.swing.JLabel();
        panelName = new javax.swing.JPanel();
        labelBirthDate = new javax.swing.JLabel();
        labelWeight = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        fieldBirthDate = new javax.swing.JTextField();
        fieldWeight = new javax.swing.JTextField();
        labelName = new javax.swing.JLabel();
        labelHeight = new javax.swing.JLabel();
        fieldHeight = new javax.swing.JTextField();
        rbBoy = new javax.swing.JRadioButton();
        rbGirl = new javax.swing.JRadioButton();
        panelBMI = new javax.swing.JPanel();
        labelHeartRate = new javax.swing.JLabel();
        labelBMI = new javax.swing.JLabel();
        labelBMR = new javax.swing.JLabel();
        fieldBMI = new javax.swing.JTextField();
        fieldHeartRate = new javax.swing.JTextField();
        fieldBMR = new javax.swing.JTextField();
        sep = new javax.swing.JSeparator();
        panelFoodExercise = new javax.swing.JPanel();
        labelCalorieBalance = new javax.swing.JLabel();
        fieldCalorieIntake = new javax.swing.JTextField();
        labelEER = new javax.swing.JLabel();
        fieldEER = new javax.swing.JTextField();
        labelCalorieIntake = new javax.swing.JLabel();
        fieldCalorieBalance = new javax.swing.JTextField();
        panelText = new javax.swing.JPanel();
        labelEvaluation = new javax.swing.JLabel();
        labelConclusion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        editorEvaluation = new javax.swing.JEditorPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        editorConclusion = new javax.swing.JEditorPane();

        setMaximumSize(new Dimension(HEALTH_PASSPORT_WIDTH, HEALTH_PASSPORT_HEIGHT));
        setMinimumSize(new Dimension(HEALTH_PASSPORT_WIDTH, HEALTH_PASSPORT_HEIGHT));
        setLayout(new java.awt.BorderLayout());

        panelTitle.setLayout(new java.awt.BorderLayout());

        labelTitle.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        labelTitle.setForeground(TITLE_COLOR);
        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle.setText("HEALTH PASSPORT");
        panelTitle.add(labelTitle, java.awt.BorderLayout.CENTER);

        add(panelTitle, java.awt.BorderLayout.NORTH);

        pictureLabel.setFont(FONT_LABEL);
        pictureLabel.setForeground(NO_PICTURE_COLOR);
        pictureLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pictureLabel.setText("<html>place<br> your<br> picture<br> here</html>");
        pictureLabel.setBorder(new javax.swing.border.LineBorder(BORDER_PICTURE_COLOR, 1, true));

        labelBirthDate.setFont(FONT_LABEL);
        labelBirthDate.setForeground(COLOR_NAME);
        labelBirthDate.setText("BIRTH DATE");

        labelWeight.setFont(FONT_LABEL);
        labelWeight.setForeground(COLOR_NAME);
        labelWeight.setText("WEIGHT (kg)");

        fieldName.setEditable(false);
        fieldName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldName.setBorder(new javax.swing.border.LineBorder(COLOR_NAME, 1, true));
        fieldName.setPreferredSize(new java.awt.Dimension(2, 20));

        fieldBirthDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldBirthDate.setBorder(new javax.swing.border.LineBorder(COLOR_NAME, 1, true));
        fieldBirthDate.setPreferredSize(new java.awt.Dimension(2, 20));
        fieldBirthDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldBirthDateActionPerformed(evt);
            }
        });
        fieldBirthDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldBirthDateFocusLost(evt);
            }
        });

        fieldWeight.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldWeight.setBorder(new javax.swing.border.LineBorder(COLOR_NAME, 1, true));
        fieldWeight.setPreferredSize(new java.awt.Dimension(2, 20));
        fieldWeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldWeightActionPerformed(evt);
            }
        });
        fieldWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldWeightFocusLost(evt);
            }
        });

        labelName.setFont(FONT_LABEL);
        labelName.setForeground(COLOR_NAME);
        labelName.setText("NAME");

        labelHeight.setFont(FONT_LABEL);
        labelHeight.setForeground(COLOR_NAME);
        labelHeight.setText("HEIGHT (cm)");

        fieldHeight.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldHeight.setBorder(new javax.swing.border.LineBorder(COLOR_NAME, 1, true));
        fieldHeight.setMinimumSize(new java.awt.Dimension(2, 20));
        fieldHeight.setPreferredSize(new java.awt.Dimension(2, 20));
        fieldHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldHeightActionPerformed(evt);
            }
        });
        fieldHeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldHeightFocusLost(evt);
            }
        });

        rbBoy.setFont(FONT_LABEL);
        rbBoy.setForeground(COLOR_NAME);
        rbBoy.setSelected(true);
        rbBoy.setText("BOY");
        rbBoy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbBoyActionPerformed(evt);
            }
        });

        rbGirl.setFont(FONT_LABEL);
        rbGirl.setForeground(COLOR_NAME);
        rbGirl.setText("GIRL");
        rbGirl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbGirlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelNameLayout = new javax.swing.GroupLayout(panelName);
        panelName.setLayout(panelNameLayout);
        panelNameLayout.setHorizontalGroup(
            panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbBoy)
                    .addComponent(labelHeight)
                    .addComponent(labelWeight)
                    .addComponent(labelBirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelName))
                .addGap(4, 4, 4)
                .addGroup(panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(fieldHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(fieldWeight, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(fieldBirthDate, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rbGirl))
                .addGap(10, 10, 10))
        );
        panelNameLayout.setVerticalGroup(
            panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNameLayout.createSequentialGroup()
                .addGroup(panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldBirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBirthDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelWeight))
                .addGap(18, 18, 18)
                .addGroup(panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelHeight))
                .addGap(7, 7, 7)
                .addGroup(panelNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbBoy)
                    .addComponent(rbGirl))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        labelHeartRate.setFont(FONT_LABEL);
        labelHeartRate.setForeground(COLOR_BMI);
        labelHeartRate.setText("HEART RATE (bpm)");

        labelBMI.setFont(FONT_LABEL);
        labelBMI.setForeground(COLOR_BMI);
        labelBMI.setText("<html>BMI<br>(body mass index)</html>)");

        labelBMR.setFont(FONT_LABEL);
        labelBMR.setForeground(COLOR_BMI);
        labelBMR.setText("<html>BMR (kcal/day)<br>(basal metabolic rate)</html>");

        fieldBMI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldBMI.setBorder(new javax.swing.border.LineBorder(COLOR_BMI, 1, true));
        fieldBMI.setPreferredSize(new java.awt.Dimension(55, 20));
        fieldBMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldBMIActionPerformed(evt);
            }
        });
        fieldBMI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldBMIFocusLost(evt);
            }
        });

        fieldHeartRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldHeartRate.setBorder(new javax.swing.border.LineBorder(COLOR_BMI, 1, true));
        fieldHeartRate.setPreferredSize(new java.awt.Dimension(55, 20));
        fieldHeartRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldHeartRateActionPerformed(evt);
            }
        });
        fieldHeartRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldHeartRateFocusLost(evt);
            }
        });

        fieldBMR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldBMR.setBorder(new javax.swing.border.LineBorder(COLOR_BMI, 1, true));
        fieldBMR.setPreferredSize(new java.awt.Dimension(55, 20));
        fieldBMR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldBMRActionPerformed(evt);
            }
        });
        fieldBMR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldBMRFocusLost(evt);
            }
        });

        javax.swing.GroupLayout panelBMILayout = new javax.swing.GroupLayout(panelBMI);
        panelBMI.setLayout(panelBMILayout);
        panelBMILayout.setHorizontalGroup(
            panelBMILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBMILayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBMILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelBMI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBMR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelHeartRate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBMILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldBMI, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldHeartRate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldBMR, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBMILayout.setVerticalGroup(
            panelBMILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBMILayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(panelBMILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldBMI, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBMI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelBMILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelHeartRate)
                    .addComponent(fieldHeartRate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(panelBMILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBMR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldBMR, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        sep.setBackground(new java.awt.Color(0, 0, 0));
        sep.setForeground(new java.awt.Color(0, 0, 0));

        labelCalorieBalance.setFont(FONT_LABEL);
        labelCalorieBalance.setForeground(COLOR_EER);
        labelCalorieBalance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCalorieBalance.setText("<html>CALORIE<br>BALANCE</html>");

        fieldCalorieIntake.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldCalorieIntake.setBorder(new javax.swing.border.LineBorder(COLOR_EER, 1, true));
        fieldCalorieIntake.setPreferredSize(new java.awt.Dimension(2, 20));
        fieldCalorieIntake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldCalorieIntakeActionPerformed(evt);
            }
        });
        fieldCalorieIntake.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldCalorieIntakeFocusLost(evt);
            }
        });

        labelEER.setFont(FONT_LABEL);
        labelEER.setForeground(COLOR_EER);
        labelEER.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelEER.setText("<html>ESTIMATED<br>ENERGY<br>REQUIREMENTS</html>");

        fieldEER.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldEER.setBorder(new javax.swing.border.LineBorder(COLOR_EER, 1, true));
        fieldEER.setPreferredSize(new java.awt.Dimension(2, 20));
        fieldEER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldEERActionPerformed(evt);
            }
        });
        fieldEER.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldEERFocusLost(evt);
            }
        });

        labelCalorieIntake.setFont(FONT_LABEL);
        labelCalorieIntake.setForeground(COLOR_EER);
        labelCalorieIntake.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCalorieIntake.setText("<html>DAILY<br>CALORIE<br>INTAKE</html>");

        fieldCalorieBalance.setEditable(false);
        fieldCalorieBalance.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldCalorieBalance.setBorder(new javax.swing.border.LineBorder(COLOR_EER, 1, true));
        fieldCalorieBalance.setPreferredSize(new java.awt.Dimension(2, 20));

        javax.swing.GroupLayout panelFoodExerciseLayout = new javax.swing.GroupLayout(panelFoodExercise);
        panelFoodExercise.setLayout(panelFoodExerciseLayout);
        panelFoodExerciseLayout.setHorizontalGroup(
            panelFoodExerciseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFoodExerciseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelCalorieIntake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fieldCalorieIntake, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(labelEER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fieldEER, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(labelCalorieBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fieldCalorieBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        panelFoodExerciseLayout.setVerticalGroup(
            panelFoodExerciseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFoodExerciseLayout.createSequentialGroup()
                .addGroup(panelFoodExerciseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFoodExerciseLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(panelFoodExerciseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldCalorieIntake, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldEER, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFoodExerciseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelFoodExerciseLayout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addComponent(fieldCalorieBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(labelCalorieBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelFoodExerciseLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelFoodExerciseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelEER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCalorieIntake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelEvaluation.setFont(FONT_LABEL);
        labelEvaluation.setForeground(COLOR_EVALUATION);
        labelEvaluation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelEvaluation.setText("<html>EVALUATION OF DIET<br>(compared to the food pyramid)</html>");

        labelConclusion.setFont(FONT_LABEL);
        labelConclusion.setForeground(COLOR_EVALUATION);
        labelConclusion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelConclusion.setText("<html>CONCLUSIONS AND RECOMMENDATIONS<br>(taking into account the calorie balance)</html>");

        editorEvaluation.setBorder(new javax.swing.border.LineBorder(COLOR_EVALUATION, 1, true));
        editorEvaluation.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                editorEvaluationFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(editorEvaluation);

        editorConclusion.setBorder(new javax.swing.border.LineBorder(COLOR_EVALUATION, 1, true));
        editorConclusion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                editorConclusionFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(editorConclusion);

        javax.swing.GroupLayout panelTextLayout = new javax.swing.GroupLayout(panelText);
        panelText.setLayout(panelTextLayout);
        panelTextLayout.setHorizontalGroup(
            panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTextLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelEvaluation, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelConclusion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelTextLayout.setVerticalGroup(
            panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTextLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelTextLayout.createSequentialGroup()
                        .addComponent(labelConclusion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelTextLayout.createSequentialGroup()
                        .addComponent(labelEvaluation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout panelDataLayout = new javax.swing.GroupLayout(panelData);
        panelData.setLayout(panelDataLayout);
        panelDataLayout.setHorizontalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(pictureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(panelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelBMI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sep, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelFoodExercise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelDataLayout.setVerticalGroup(
            panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelDataLayout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addComponent(pictureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(panelName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panelBMI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sep, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFoodExercise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(panelData, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void fieldBirthDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldBirthDateActionPerformed
        // TODO add your handling code here:
        checkBirthDate();
    }//GEN-LAST:event_fieldBirthDateActionPerformed

    private void fieldBirthDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldBirthDateFocusLost
        // TODO add your handling code here:
         checkBirthDate();
    }//GEN-LAST:event_fieldBirthDateFocusLost

    private void fieldWeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldWeightActionPerformed
        checkWeight();
    }//GEN-LAST:event_fieldWeightActionPerformed

    private void fieldWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldWeightFocusLost
        checkWeight();
    }//GEN-LAST:event_fieldWeightFocusLost

    private void fieldHeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldHeightActionPerformed
        checkHeight();
    }//GEN-LAST:event_fieldHeightActionPerformed

    private void fieldHeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldHeightFocusLost
        checkHeight();
    }//GEN-LAST:event_fieldHeightFocusLost

    private void rbBoyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBoyActionPerformed
        checkGender(true);
    }//GEN-LAST:event_rbBoyActionPerformed

    private void rbGirlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbGirlActionPerformed
        checkGender(false);
    }//GEN-LAST:event_rbGirlActionPerformed

    private void fieldBMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldBMIActionPerformed
        checkBMI();
    }//GEN-LAST:event_fieldBMIActionPerformed

    private void fieldBMIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldBMIFocusLost
        checkBMI();
    }//GEN-LAST:event_fieldBMIFocusLost

    private void fieldHeartRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldHeartRateActionPerformed
       checkHeartRate();
    }//GEN-LAST:event_fieldHeartRateActionPerformed

    private void fieldHeartRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldHeartRateFocusLost
        checkHeartRate();
    }//GEN-LAST:event_fieldHeartRateFocusLost

    private void fieldBMRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldBMRActionPerformed
        checkBMR();
    }//GEN-LAST:event_fieldBMRActionPerformed

    private void fieldBMRFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldBMRFocusLost
        checkBMR();
    }//GEN-LAST:event_fieldBMRFocusLost

    private void fieldCalorieIntakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldCalorieIntakeActionPerformed
        checkCalorieIntake();
    }//GEN-LAST:event_fieldCalorieIntakeActionPerformed

    private void fieldCalorieIntakeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCalorieIntakeFocusLost
        checkCalorieIntake();
    }//GEN-LAST:event_fieldCalorieIntakeFocusLost

    private void fieldEERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldEERActionPerformed
        checkEER();
    }//GEN-LAST:event_fieldEERActionPerformed

    private void fieldEERFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldEERFocusLost
        checkEER();
    }//GEN-LAST:event_fieldEERFocusLost

    private void editorEvaluationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_editorEvaluationFocusLost
        checkEvaluation();
    }//GEN-LAST:event_editorEvaluationFocusLost

    private void editorConclusionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_editorConclusionFocusLost
       checkConclusion();
    }//GEN-LAST:event_editorConclusionFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane editorConclusion;
    private javax.swing.JEditorPane editorEvaluation;
    private javax.swing.JTextField fieldBMI;
    private javax.swing.JTextField fieldBMR;
    private javax.swing.JTextField fieldBirthDate;
    private javax.swing.JTextField fieldCalorieBalance;
    private javax.swing.JTextField fieldCalorieIntake;
    private javax.swing.JTextField fieldEER;
    private javax.swing.JTextField fieldHeartRate;
    private javax.swing.JTextField fieldHeight;
    private javax.swing.JTextField fieldName;
    private javax.swing.JTextField fieldWeight;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelBMI;
    private javax.swing.JLabel labelBMR;
    private javax.swing.JLabel labelBirthDate;
    private javax.swing.JLabel labelCalorieBalance;
    private javax.swing.JLabel labelCalorieIntake;
    private javax.swing.JLabel labelConclusion;
    private javax.swing.JLabel labelEER;
    private javax.swing.JLabel labelEvaluation;
    private javax.swing.JLabel labelHeartRate;
    private javax.swing.JLabel labelHeight;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JLabel labelWeight;
    private javax.swing.JPanel panelBMI;
    private javax.swing.JPanel panelData;
    private javax.swing.JPanel panelFoodExercise;
    private javax.swing.JPanel panelName;
    private javax.swing.JPanel panelText;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JLabel pictureLabel;
    private javax.swing.JRadioButton rbBoy;
    private javax.swing.JRadioButton rbGirl;
    private javax.swing.JSeparator sep;
    // End of variables declaration//GEN-END:variables

}
