/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.resultbinder;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.resultbinder.common.ResultCard;
import eu.scy.client.tools.resultbinder.healthPassport.HealthPassportPanel;
import eu.scy.client.tools.resultbinder.healthPassport.IActionHealthPassport;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * resultBinder panel, could be in the future a more generic panel
 * @author Marjolaine
 */
public class ResultBinderPanel extends JPanel implements IActionHealthPassport{

    /* xml tag for the value changed in the logs*/
    private final static String LOG_RESULT_BINDER_CHANGED = "result_binder_value_changed";
    /* result Card*/
    private ResultCard resultCard = null;

    /* ressource bundle */
    private ResourceBundleWrapper bundle;

    private JScrollPane scrollPane = null;
    private HealthPassportPanel healthPassportPanel = null;
    private String userName;

    private IActionResultBinder actionResultBinder;

    public ResultBinderPanel(String userName) {
        super();
        this.userName = userName;
        initGUI();
    }

    /** Adds the specified action resultBinder listener to receive action events from this panel.*/
    public void addActionResultBinder(IActionResultBinder actionResultBinder){
        this.actionResultBinder = actionResultBinder;
    }

    private void initGUI(){
        setLayout(new BorderLayout());
        bundle = new ResourceBundleWrapper(this);
        this.add(getScrollPane(), BorderLayout.CENTER);
    }

    private JScrollPane getScrollPane(){
        if(scrollPane == null){
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getHealthPassportPanel());
        }
        return scrollPane;
    }
    private HealthPassportPanel getHealthPassportPanel(){
        if(healthPassportPanel == null){
            healthPassportPanel = new HealthPassportPanel(bundle, resultCard, userName);
            healthPassportPanel.addActionResultBinder(this);
        }
        return healthPassportPanel;
    }


    /** loads a new ELO */
     public void newElo(){
         healthPassportPanel.initHealthPassport();
         resultCard = healthPassportPanel.getResultCardHealthPassport();
     }

     

     /** load the specified ELO */
     public void loadELO(Element e){
        try{
            ResultCard rc = new ResultCard(e);
            resultCard = rc;
            healthPassportPanel.updateHealthPassport(resultCard);
        }catch(JDOMException ex){
            JOptionPane.showConfirmDialog(this ,bundle.getString("RESULTBINDER.MSG_ERROR_LOAD_ELO")+" "+ex.getMessage() , bundle.getString("RESULTBINDER.TITLE_DIALOG_ERROR"),JOptionPane.OK_CANCEL_OPTION);
        }
     }

     /** gets the result card ELO */
     public Element getResultCard(){
         resultCard = healthPassportPanel.getResultCardHealthPassport();
         if(resultCard == null)
             return null;
         else
             return resultCard.toXML();
     }

     /** gets the container for the thumbnail */
     public Container getInterfacePanel(){
         //return this;
         return healthPassportPanel.getInterfacePanel();
     }

     /** logs */
    public void logValueChanged(String valueName, String value){
        actionResultBinder.logAction(LOG_RESULT_BINDER_CHANGED, valueName, value);
    }

    @Override
    public void logAction(String attributeKey, String attributeValue) {
        logValueChanged(attributeKey, attributeValue);
    }

    /** sets the specified userName for the health passport*/
    public void setUserName(String userName){
        this.userName = userName;
        healthPassportPanel.setUserName(userName);
    }

    /** sets the specified url for a picture for the health passport*/
    public void setPicture(String pictureUrl){
        healthPassportPanel.setPicture(pictureUrl);
    }
}
