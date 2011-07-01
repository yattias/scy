/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.SystemOutActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.IAction;
import eu.scy.client.common.datasync.DataSyncException;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.utilities.ActionCopex;
import eu.scy.common.datasync.ISyncObject;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import org.jdom.Element;
import javax.swing.JPanel;
import roolo.elo.api.IELO;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.EloFunctionalRole;
import java.io.StringReader;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * swing panel for copex
 * @author Marjolaine
 */
public class ScyCopexPanel extends JPanel implements ActionCopex, ISyncListener{
    private CopexPanel copex;
    private ToolBrokerAPI tbi;
    private String session_name = "n/a";
    private IActionLogger actionLogger;
    private String eloUri = "n/a";
    private final Logger logger;

    private String toolName;

    private CopexNotificationManager copexNotificationManager;
    private ResourceBundleWrapper bundle;

    private ISyncSession session = null;

    public ScyCopexPanel(String toolName) {
        super();
        this.toolName = toolName;
        this.copexNotificationManager = new CopexNotificationManager();
        logger = Logger.getLogger(ScyCopexPanel.class.getName());
        this.setLayout(new BorderLayout());
        this.bundle = new ResourceBundleWrapper(this);
    }

    public void initCopex(){
        // i18n
        Locale locale = Locale.getDefault();
        //locale = new Locale("en", "GB");
        //locale = new Locale("fr", "FR");
        copex = new CopexPanel(true, locale);
        setPreferredSize(new Dimension(550,350));
        copex.addActionCopex(this);
        this.add(this.copex, BorderLayout.CENTER);
        //copex.loadData();
    }

    public void setEloUri(String eloUri){
        this.eloUri = eloUri;
    }


    public void setTBI(ToolBrokerAPI tbi) {
        this.tbi = tbi;
    }
    
    /* initialization action logger */
    public void initActionLogger(){
        if(tbi != null){
            actionLogger = tbi.getActionLogger();
            //actionLogger = new SystemOutActionLogger();
        }
        else{
            actionLogger = new DevNullActionLogger();
        }
    }

    /* load ELO into copex */
    public void loadELO(String xmlContent){
        this.copex.loadELO(new JDomStringConversion().stringToXml(xmlContent));
    }

    public void newElo(){
        this.copex.newELO();
    }

    /* returns  the experimental proc ELO*/
    public Element getExperimentalProcedure(){
        return this.copex.getXProc();
    }

    @Override
    public void loadHelpProc(LearnerProcedure helpProc) {
        
    }

    /* logs a user action
     * @param type type of the action (action_added, ...)
     * @param attribute list of the attribute of the action (position in the tree, name of the action...)
     */
    @Override
    public void logAction(String type, List<CopexProperty> attribute) {
        // action
        IAction action = new Action();
        action.setType(type);
        if(tbi != null){
            action.setUser(tbi.getLoginUserName());
            action.addContext(ContextConstants.tool, this.toolName);
            // generic way now
            //action.addContext(ContextConstants.mission, tbi.getMissionSpecificationURI().toString());
            action.addContext(ContextConstants.session, session_name);
            action.addContext(ContextConstants.eloURI, eloUri);
        }
        for(Iterator<CopexProperty> p = attribute.iterator();p.hasNext();){
            CopexProperty property = p.next();
            if(property.getSubElement() == null) {
                action.addAttribute(property.getName(), property.getValue());
            } else {
                action.addAttribute(property.getName(), property.getValue());
                action.addAttribute(property.getName()+"_sub", property.getSubElement().getValue());
            }
        }
        // log action
        if(actionLogger != null)
            actionLogger.log(action);
    }

    /* returns the interface panel for the thumbnail */
    public Container getInterfacePanel(){
        return copex.getInterfacePanel();
    }

    public Dimension getRealSize(){
        return copex.getInterfacePanel().getSize();
    }

    /* received an agent notification*/
    public boolean processNotification(INotification notification) {
        return this.copexNotificationManager.processNotification(notification);
    }

    public String getNotification(){
        return this.copexNotificationManager.getNotification();
    }

    public void keepNotification(boolean keep){
        this.copexNotificationManager.keepNotification(keep);
    }

    private String getBundleString(String key){
       return this.bundle.getString(key);
   }

    /* accepts drop from rich text editor, with functional role= research question or hypothesis */
    public void acceptDrop(IELO textElo){
        if(textElo != null){
            String[] yesNoOptions = new String[2];
            yesNoOptions[0] = getBundleString("FX-COPEX.YES");
            yesNoOptions[1] = getBundleString("FX-COPEX.NO");
            int n = -1;
            ScyElo scyElo = new ScyElo(textElo, tbi);
            if(scyElo == null)
                return;
            String technicalFormat = scyElo.getTechnicalFormat();
            if(technicalFormat == null || !technicalFormat.equals("scy/rtf"))
                return;
            if(textElo.getContent() == null)
                return;
            String functionnalRole = null;
            if(scyElo.getFunctionalRole() != null){
                functionnalRole = scyElo.getFunctionalRole().name();
            }
            if(functionnalRole != null && functionnalRole.equals(EloFunctionalRole.HYPOTHESIS.name())){
                n = JOptionPane.showOptionDialog( null,
                            getBundleString("FX-COPEX.MSG_MERGE_HYPOTHESIS"),               // question
                            getBundleString("FX-COPEX.TITLE_DIALOG_MERGE_TEXT"),           // title
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,  // icon
                            null, yesNoOptions,yesNoOptions[0] );
                if (n == 0) {
                    Element el = new JDomStringConversion().stringToXml(textElo.getContent().getXmlString());
                    if(el.getName().equals("RichText") && canEditHypothesis()){
                        copex.setProcedureHypothsesis(getPlainText(el.getText()));
                    }
                }
            }else if(functionnalRole != null && functionnalRole.equals(EloFunctionalRole.RESEARCH_QUESTION.name())){
                n = JOptionPane.showOptionDialog( null,
                            getBundleString("FX-COPEX.MSG_MERGE_RESEARCH_QUESTION"),               // question
                            getBundleString("FX-COPEX.TITLE_DIALOG_MERGE_TEXT"),           // title
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,  // icon
                            null, yesNoOptions,yesNoOptions[0] );
                if (n == 0) {
                    Element el = new JDomStringConversion().stringToXml(textElo.getContent().getXmlString());
                    if(el.getName().equals("RichText") && canEditResearchQuestion()){
                        copex.setProcedureQuestion(getPlainText(el.getText()));
                    }
                }
              }
        }
    }

    /* returns the text corresponding to the specified rtf text (rich text editor)*/
    private String getPlainText(String rtfText){
        RTFEditorKit rtfEditor = new RTFEditorKit();
        Document doc = rtfEditor.createDefaultDocument();
        try {
            rtfEditor.read(new StringReader(rtfText), doc, 0);
            String plainText = doc.getText(0, doc.getLength());
            while (plainText.endsWith("\n")){
                plainText = plainText.substring(0, plainText.length()-1);
            }
            return plainText;
        } catch (IOException ex) {
            logger.severe("IOException while reading text elo "+ex);
        } catch (BadLocationException ex) {
            logger.severe("BadLocationException while reading text elo "+ex);
        }
        return "";
    }

    /** returns true if the user can edit the research question */
    public boolean canEditResearchQuestion(){
        return copex.canEditResearchQuestion();
    }

    /** returns true if the user can edit the hypothesis */
    public boolean canEditHypothesis(){
         return copex.canEditHypothesis();
    }

    // joins the session for sync.
    public ISyncSession joinSession(String mucID){
        if(session != null){
            leaveSession(session.getId());
        }
        try {
            session = tbi.getDataSyncService().joinSession(mucID, this, toolName);
	} catch (DataSyncException e) {
		JOptionPane.showMessageDialog(null, getBundleString("FX-FITEX.MSG_ERROR_SYNC"));
		e.printStackTrace();
                return session;
	}
        if (session == null) {
            JOptionPane.showMessageDialog(null, getBundleString("FX-FITEX.MSG_ERROR_SYNC"));
            return session;
        }
        return session;
    }

    public void leaveSession(String mucID){
        if(session != null){
            session.removeSyncListener(this);
        }
        session = null;
    }

    public void startCollaboration(){
        this.copex.startCollaboration();
    }
    
    public void endCollaboration(){
        this.copex.endCollaboration();
    }

    @Override
    public void syncObjectAdded(final ISyncObject syncObject) {
        if (syncObject.getToolname() != null && syncObject.getToolname().equals(toolName)){
            if (syncObject.getCreator().equals(session.getUsername())) {
                // creator of the object
            }else{
                copex.syncNodeChanged(syncObject);
            }
        }
    }

    @Override
    public void syncObjectChanged(ISyncObject syncObject) {
        if (syncObject.getToolname() != null && syncObject.getToolname().equals(toolName)){
            if (syncObject.getCreator().equals(session.getUsername())) {
                // creator of the object
            }else{
                copex.syncNodeChanged(syncObject);
            }
        }
    }

    @Override
    public void syncObjectRemoved(ISyncObject syncObject) {
        if (syncObject.getToolname() != null && syncObject.getToolname().equals(toolName)){
            if (syncObject.getCreator().equals(session.getUsername())) {
                // creator of the object
            }else{
                copex.syncNodeRemoved(syncObject);
            }
        }
    }

    @Override
    public void addCopexSyncObject(ISyncObject syncObject) {
        if(session != null)
            session.addSyncObject(syncObject);
    }

    @Override
    public void changeCopexSyncObject(ISyncObject syncObject) {
        if(session != null)
            session.changeSyncObject(syncObject);
    }

    @Override
    public void removeCopexSyncObject(ISyncObject syncObject) {
        if(session != null)
            session.removeSyncObject(syncObject);
    }

    public void setReadOnly(boolean readonly){
        if(copex != null){
            this.copex.setReadOnly(readonly);
        }
    }

}
