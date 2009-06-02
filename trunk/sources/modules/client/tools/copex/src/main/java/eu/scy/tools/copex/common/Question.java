/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.utilities.MyConstants;
import org.jdom.Element;


/**
 * represente la question du protocole 
 * @author MBO
 */
public class Question extends CopexTask implements Cloneable{

    // ATTRIBUTS
    /* hypothese */
    private String hypothesis;
    /* principe general, n'est valable que si c'est la question du proc */
    private String generalPrinciple;

    // CONSTRUCTEURS
    public Question(long dbKey, String question, String description, String hypothesis, String comments, String taskImage, Element draw, String generalPrinciple, boolean isVisible, TaskRight taskRight, boolean root) {
        super(dbKey, question, description, comments, taskImage, draw, isVisible, taskRight, root, null) ;
        this.hypothesis = hypothesis;
        this.generalPrinciple = generalPrinciple ;
    }

    public Question(long dbKey, String question, String description, String hypothesis, String comments, String taskImage, Element draw, String generalPrinciple, boolean isVisible, TaskRight taskRight, boolean root, long dbKeyBrother, long dbKeyChild) {
        super(dbKey, question, description, comments, taskImage, draw, isVisible, taskRight,  root, dbKeyBrother, dbKeyChild, null) ;
        this.hypothesis = hypothesis;
        this.generalPrinciple = generalPrinciple ;
    }
    
    /* constructeur appele par l'interface pour creer la sous question d'un utilisateur */
    public Question(String description, String hypothesis, String comments){
        super(-1, "", description, comments, null, null, true, new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT), false, null) ;
        this.hypothesis = hypothesis;
        this.generalPrinciple = null ;
    }
    
    
    /* racine fictive */
    public Question(){
        super();
        this.hypothesis = "";
        this.generalPrinciple = null;
    }

   
    // GETTER AND SETTER
    public String getGeneralPrinciple() {
        return generalPrinciple;
    }

    public void setGeneralPrinciple(String generalPrinciple) {
        this.generalPrinciple = generalPrinciple;
    }
    public String getHypothesis() {
        return hypothesis;
    }

    public void setHypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
    }

    @Override
    public Object clone()  {
        Question question = (Question) super.clone() ;
        String hypothesisC = "";
        if (this.hypothesis != null)
            hypothesisC = new String(this.hypothesis);
        String generalPrincipleC = "";
        if (this.generalPrinciple != null)
            generalPrincipleC = new String(this.generalPrinciple);
        question.setHypothesis(hypothesisC);
        question.setGeneralPrinciple(generalPrincipleC);
        return question;
    }

    
    
    
    
    
}
