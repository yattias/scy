package eu.scy.client.desktop.scydesktop;

import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.Node;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELO;
import java.net.URI;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.client.desktop.scydesktop.config.Config;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import java.lang.String;
import java.util.UUID;

/**
 * @author lars
 */
public class ScyToolActionLogger extends CustomNode, ScyToolFX, EloSaverCallBack {

    public-init var window: ScyWindow;
    public-init var config: Config;
    public-init var missionRuntimeEloUri: URI;
    var toolId = "scy://unsaved_elo_{UUID.randomUUID().toString()}";
    var oldURI = getURI();
    def actionLogger = config.getToolBrokerAPI().getActionLogger();
    def username = config.getToolBrokerAPI().getLoginUserName();
    def toolname = config.getEloToolConfig(window.eloType).getContentCreatorId();
    def missionname = "mission 1";
    def sessionname = "n/a";
    def logger = Logger.getLogger(this.getClass());
    def technicalFormatKey = config.getMetadataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
    def TOOL_STARTED = "tool_started";
    def TOOL_OPENED = "tool_opened";
    def TOOL_CLOSED = "tool_closed";
    def TOOL_MINI = "tool_minimized";
    def TOOL_UNMINI = "tool_unminimized";
    def TOOL_GOTFOCUS = "tool_got_focus";
    def TOOL_LOSTFOCUS = "tool_lost_focus";
    def TOOL_QUIT = "tool_quit";
    def ELO_LOADED = "elo_loaded";
    def ELO_SAVED = "elo_saved";
    def ELO_ADDTOPORTFOLIO = "elo_added_to_portfolio";

    public function getURI(): String {
        if (window.eloUri == null) {
            return toolId;
        } else {
             return window.eloUri.toString();
        }
    }

    postinit {
        logger.debug("ScyToolActionLogger created for {username}@{toolname}...");
    }

    public override function create(): Node {
        Group { content: [] }
    }

    public override function postInitialize(): Void {
        var action = createBasicAction(TOOL_STARTED);
        actionLogger.log(action);
//        System.out.println("***** logging postInitialize for {username}@{toolname}");
    }

    public override function onOpened(): Void {
        var action = createBasicAction(TOOL_OPENED);
        actionLogger.log(action);
//        System.out.println("***** logging onOpened for {username}@{toolname}");
    }

    public override function onClosed(): Void {
        var action = createBasicAction(TOOL_CLOSED);
        actionLogger.log(action);
//        System.out.println("***** logging onClosed for {username}@{toolname}");
    }

    public override function onMinimized(): Void {
        var action = createBasicAction(TOOL_MINI);
        actionLogger.log(action);
//        System.out.println("***** logging onMinimized for {username}@{toolname}");
    }

    public override function onUnMinimized(): Void {
        var action = createBasicAction(TOOL_UNMINI);
        actionLogger.log(action);
//        System.out.println("***** logging onUnMinimized for {username}@{toolname}");
    }

    public override function onGotFocus(): Void {
        var action = createBasicAction(TOOL_GOTFOCUS);
        actionLogger.log(action);
//        System.out.println("***** logging onGotFocus for {username}@{toolname}");
    }

    public override function onLostFocus(): Void {
        var action = createBasicAction(TOOL_LOSTFOCUS);
        actionLogger.log(action);
    }

   public override function onQuit(): Void {
        var action = createBasicAction(TOOL_QUIT);
        actionLogger.log(action);
   }

    public override function loadElo(eloUri: URI): Void {
        var action = createBasicAction(ELO_LOADED);
        action.addAttribute("elo_uri", eloUri.toString());
        action.addAttribute("old_uri", oldURI);
        actionLogger.log(action);
        oldURI = eloUri.toString();
    }

    public override function eloSaved(elo: IELO): Void {
        var action = createBasicAction(ELO_SAVED);
        action.addAttribute("elo_uri", elo.getUri().toString());
        action.addAttribute("old_uri", oldURI);
        var eloType = elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() as String;
        action.addAttribute("elo_type", eloType);
        actionLogger.log(action);
        oldURI = elo.getUri().toString();
//        System.out.println("***** logging eloSaved for {username}@{toolname}");
    }

    public override function eloSaveCancelled(elo: IELO): Void {    }

    public function logAddToPortfolio(): Void {
        var action = createBasicAction(ELO_ADDTOPORTFOLIO);
        actionLogger.log(action);
    }

    public function createBasicAction(type: String): IAction {
        var action = new Action();
        action.setUser(username);
        action.setType(type);
        action.addContext(ContextConstants.tool, toolname);
        action.addContext(ContextConstants.mission, missionRuntimeEloUri.toString());
        action.addContext(ContextConstants.session, sessionname);
        if (window.eloUri == null) {
            action.addContext(ContextConstants.eloURI, toolId);
        } else {
             action.addContext(ContextConstants.eloURI, window.eloUri.toString());
        }
        return action;
    }

}