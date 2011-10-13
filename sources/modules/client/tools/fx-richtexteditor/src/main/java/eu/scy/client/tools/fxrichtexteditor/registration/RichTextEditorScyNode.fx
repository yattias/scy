/*
 * RichTextEditorNode.fx
 *
 * Created on 28.01.2010, 00:05:00
 */
package eu.scy.client.tools.fxrichtexteditor.registration;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import eu.scy.actionlogging.DevNullActionLogger;
import org.jdom.Element;
import eu.scy.client.common.richtexteditor.RichTextEditor;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.collaboration.api.CollaborationStartable;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.desktoputils.i18n.Composer;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.desktoputils.EmptyBorderNode;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
/**
 * @author kaido
 */
/**
 * Base class of the rich text editor tool as part of SCY-Lab.
 * Some general logic is implemented in RichTextEditorNode.
 * @see RichTextEditorNode
 */
public class RichTextEditorScyNode extends INotifiable, RichTextEditorNode, ScyToolFX, EloSaverCallBack {

    def logger = Logger.getLogger("eu.scy.client.tools.fxrichtexteditor.RichTextEditorNode");
    def scyRichTextEditorType = "scy/rtf";
    def jdomStringConversion = new JDomStringConversion();
    def toolname = "richtext";
    public var eloFactory: IELOFactory;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository: IRepository;
    public var toolBrokerAPI: ToolBrokerAPI;
    public var actionLogger: IActionLogger;
    public var scyWindow: ScyWindow;
    public var authorMode: Boolean;
    // interval in milliseconds after what typed text is wrote
    // into action log - should be configurable from authoring tools
    public var typingLogIntervalMs = 30000;
    var elo: IELO;
    var technicalFormatKey: IMetadataKey;
    def richTextTagName = "RichText";
    def saveTitleBarButton = TitleBarButton {
                actionId: TitleBarButton.saveActionId
                action: doSaveElo
            }
    def saveAsTitleBarButton = TitleBarButton {
                actionId: TitleBarButton.saveAsActionId
                action: doSaveAsElo
            }
    def openTitleBarButton = TitleBarButton {
                actionId: "open"
                iconType: "import"
                action: openFileAction
                tooltip: richTextEditor.fileToolbar.openButton.getToolTipText()
            }
    def saveRtfTitleBarButton = TitleBarButton {
                actionId: "save_rtf"
                iconType: "export"
                action: saveFileAction
                tooltip: richTextEditor.fileToolbar.saveButton.getToolTipText()
            }
    def printTitleBarButton = TitleBarButton {
                actionId: "print"
                iconType: "save_as_dataset"
                action: printAction
                tooltip: richTextEditor.fileToolbar.printButton.getToolTipText()
            }
    def notificationTitleBarButton = TitleBarButton {
                actionId: "notify"
                iconType: "information2"
                action: doNotify
                tooltip: ##"Show notification"
            }
    public var syncSession: ISyncSession;
    var collaborative: Boolean = false;
    var notificationText = ##"No notification available.";
    var notificationDialog: NotificationDialog;
    var rtfTitleBarButtonManager: TitleBarButtonManager;

    function setLoggerEloUri() {
        var myEloUri: String = (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI();
        if (myEloUri == null and scyWindow.eloUri != null)
            myEloUri = scyWindow.eloUri.toString();
        richTextEditor.setEloUri(myEloUri);
    }

    public override function initialize(windowContent: Boolean): Void {
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
        repository = toolBrokerAPI.getRepository();
        eloFactory = toolBrokerAPI.getELOFactory();
        actionLogger = toolBrokerAPI.getActionLogger();
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        if (actionLogger == null) {
            actionLogger = new DevNullActionLogger();
        }
        richTextEditor.setRichTextEditorLogger(actionLogger,
        toolBrokerAPI.getLoginUserName(), toolname, toolBrokerAPI.getMissionRuntimeURI().toString(), "n/a",
        "richtext");
        setLoggerEloUri();
    }

    public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
        rtfTitleBarButtonManager = titleBarButtonManager;
        if (windowContent) {
            if (authorMode) {
                titleBarButtonManager.titleBarButtons = [
                            saveTitleBarButton,
                            saveAsTitleBarButton,
                            openTitleBarButton,
                            saveRtfTitleBarButton,
                            printTitleBarButton
                        ]

            } else {
                titleBarButtonManager.titleBarButtons = [
                            saveTitleBarButton,
                            saveAsTitleBarButton,
                            saveRtfTitleBarButton,
                            printTitleBarButton
                        ]
            }
        }
    }

    function saveFileAction() {
        richTextEditor.fileToolbar.saveFileAction();
    }

    function printAction() {
        richTextEditor.fileToolbar.printAction();
    }

    function openFileAction() {
        richTextEditor.fileToolbar.openFileAction();
    }

    public override function loadElo(uri: URI) {
        doLoadElo(uri);
    }

    function doLoadElo(eloUri: URI) {
        logger.info("Trying to load elo {eloUri}");
        var newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            eloContentXmlToRichText(newElo.getContent().getXmlString());
            logger.info("elo rtf loaded");
            elo = newElo;
        }
        setLoggerEloUri();
    }

    function doSaveElo() {
        elo.getContent().setXmlString(richTextToEloContentXml(richTextEditor.getRtfText()));
        eloSaver.eloUpdate(getElo(), this);
    }

    function doSaveAsElo() {
        eloSaver.eloSaveAs(getElo(), this);
    }

    override public function eloSaveCancelled(elo: IELO): Void {
    }

    override public function eloSaved(elo: IELO): Void {
        this.elo = elo;
        setLoggerEloUri();
    }

    function getElo(): IELO {
        if (elo == null) {
            elo = eloFactory.createELO();
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyRichTextEditorType);
        }
        elo.getContent().setXmlString(richTextToEloContentXml(richTextEditor.getRtfText()));
        return elo;
    }

    function richTextToEloContentXml(text: String): String {
        var textElement = new Element(richTextTagName);
        textElement.setText(text);
        return jdomStringConversion.xmlToString(textElement);
    }

    function eloContentXmlToRichText(text: String) {
        var richTextElement = jdomStringConversion.stringToXml(text);
        if (richTextTagName != richTextElement.getName()) {
            logger.error("wrong tag name, expected {richTextTagName}, but got {richTextElement.getName()}");
        }
        richTextEditor.setText(richTextElement.getText().trim());
    }

    public override function create(): Node {
        richTextEditor = new RichTextEditor(false, authorMode);
        richTextEditor.setTypingLogIntervalMs(typingLogIntervalMs);
        openTitleBarButton.tooltip = richTextEditor.fileToolbar.openButton.getToolTipText();
        saveRtfTitleBarButton.tooltip = richTextEditor.fileToolbar.saveButton.getToolTipText();
        printTitleBarButton.tooltip = richTextEditor.fileToolbar.printButton.getToolTipText();
        wrappedRichTextEditor = ScySwingWrapper.wrap(richTextEditor, true);
    }

    override function postInitialize(): Void {
    }

    public override function onQuit() {
        richTextEditor.insertedTextToActionLog();
        if (elo != null) {
            def oldContentXml = elo.getContent().getXmlString();
            def newContentXml = getElo().getContent().getXmlString();
            if (oldContentXml == newContentXml) {
                // nothing changed
                return;
            }
        }
        doSaveElo();
    }

    public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
        if (richTextEditor != null) {
            return eu.scy.client.desktop.desktoputils.UiUtils.createThumbnail(richTextEditor, richTextEditor.getSize(), new Dimension(width, height));
        } else {
            return null;
        }
    }

    public override function processNotification(notification: INotification): Boolean {
        //if (notification.getSender().equals("eu.scy.agents.hypothesis.HypothesisDecisionMakerAgent")) {
        var messageFromAgent = notification.getFirstProperty("message");
        if (not messageFromAgent.equals("")) {
            javafx.stage.Alert.inform(messageFromAgent);
            return true;
        }
        return false;
    }

    function addNotificationInTitleBar(message: String): Void {
        notificationText = message;
        if (authorMode) {
            rtfTitleBarButtonManager.titleBarButtons = [
                        saveTitleBarButton,
                        saveAsTitleBarButton,
                        openTitleBarButton,
                        saveRtfTitleBarButton,
                        printTitleBarButton,
                        notificationTitleBarButton
                    ]

        } else {
            rtfTitleBarButtonManager.titleBarButtons = [
                        saveTitleBarButton,
                        saveAsTitleBarButton,
                        saveRtfTitleBarButton,
                        printTitleBarButton,
                        notificationTitleBarButton
                    ]
        }
    }

    function removeNotificationInTitleBar() {
        if (authorMode) {
            rtfTitleBarButtonManager.titleBarButtons = [
                        saveTitleBarButton,
                        saveAsTitleBarButton,
                        openTitleBarButton,
                        saveRtfTitleBarButton,
                        printTitleBarButton
                    ]

        } else {
            rtfTitleBarButtonManager.titleBarButtons = [
                        saveTitleBarButton,
                        saveAsTitleBarButton,
                        saveRtfTitleBarButton,
                        printTitleBarButton
                    ]
        }
    }

    function doNotify() {
        notificationDialog = NotificationDialog {
                    okayAction: okayNotificationDialog
                    //            cancelAction: cancelNotificationDialog
                    notificationText: getNotification();
                }
        createModalDialog(scyWindow.windowManager.scyDesktop.windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew), ##"Notification", notificationDialog);
    }

    function okayNotificationDialog(): Void {
        notificationDialog.modalDialogBox.close();
        removeNotificationInTitleBar();
    }

    function getNotification(): String {
        if (notificationText == null or notificationText.equals("")) {
            notificationText = ##"No notification available.";
        }
        return notificationText;
    }

    function createModalDialog(windowColorScheme: WindowColorScheme, title: String, modalDialogNode: ModalDialogNode): Void {
        Composer.localizeDesign(modalDialogNode.getContentNodes(), StringLocalizer {});
        modalDialogNode.modalDialogBox = ModalDialogBox {
                    content: EmptyBorderNode {
                        content: Group {
                            content: modalDialogNode.getContentNodes();
                        }
                    }
                    eloIcon: scyWindow.windowManager.scyDesktop.windowStyler.getScyEloIcon("information2")
                    targetScene: scyWindow.windowManager.scyDesktop.scene
                    title: title
                    windowColorScheme: scyWindow.windowColorScheme
                    closeAction: function(): Void {
                    }
                }
    }

}
