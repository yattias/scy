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
    var toolId = "scy://unsaved_elo_{UUID.randomUUID().toString()}";
    var oldURI = getURI();
    def actionLogger = config.getToolBrokerAPI().getActionLogger();
    def username = config.getToolBrokerAPI().getLoginUserName();
    def toolname = config.getEloToolConfig(window.eloType).getContentCreatorId();
    def missionname = "mission 1";
    def sessionname = "n/a";
    def logger = Logger.getLogger(this.getClass());
    def technicalFormatKey = config.getMetadataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);

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
        var action = createBasicAction("tool_start");
        actionLogger.log(action);
//        System.out.println("***** logging postInitialize for {username}@{toolname}");
    }

    public override function onClosed(): Void {
        var action = createBasicAction("tool_closed");
        actionLogger.log(action);
//        System.out.println("***** logging onClosed for {username}@{toolname}");
    }

    public override function onMinimized(): Void {
        var action = createBasicAction("tool_minimized");
        actionLogger.log(action);
//        System.out.println("***** logging onMinimized for {username}@{toolname}");
    }

    public override function onUnMinimized(): Void {
        var action = createBasicAction("tool_unminimized");
        actionLogger.log(action);
//        System.out.println("***** logging onUnMinimized for {username}@{toolname}");
    }

    public override function onGotFocus(): Void {
        var action = createBasicAction("tool_gotfocus");
        actionLogger.log(action);
//        System.out.println("***** logging onGotFocus for {username}@{toolname}");
    }

    public override function onLostFocus(): Void {
        var action = createBasicAction("tool_lostfocus");
        actionLogger.log(action);
//        System.out.println("***** logging onLostFocus for {username}@{toolname}");
    }

    public override function loadElo(eloUri: URI): Void {
        var action = createBasicAction("elo_load");
        action.addAttribute("elo_uri", eloUri.toString());
        action.addAttribute("old_uri", oldURI);
        actionLogger.log(action);
        oldURI = eloUri.toString();
//        System.out.println("***** logging loadElo for {username}@{toolname}");
    }

    public override function eloSaved(elo: IELO): Void {
        var action = createBasicAction("elo_save");
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
        var action = createBasicAction("elo_addtoportfolio");
        actionLogger.log(action);
    }

    public function logToolOpened(): Void {
        var action = createBasicAction("tool_opened");
        actionLogger.log(action);
    }

    public function createBasicAction(type: String): IAction {
        var action = new Action();
        action.setUser(username);
        action.setType(type);
        action.addContext(ContextConstants.tool, toolname);
        action.addContext(ContextConstants.mission, missionname);
        action.addContext(ContextConstants.session, sessionname);
        if (window.eloUri == null) {
            action.addContext(ContextConstants.eloURI, toolId);
        } else {
             action.addContext(ContextConstants.eloURI, window.eloUri.toString());
        }
        return action;
    }

}