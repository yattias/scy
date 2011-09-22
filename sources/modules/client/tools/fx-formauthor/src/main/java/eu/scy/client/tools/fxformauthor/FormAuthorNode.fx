/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import javafx.scene.layout.Resizable;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.tools.fxformauthor.FormAuthorRepositoryWrapper;
import eu.scy.client.tools.fxformauthor.viewer.FormViewer;
import eu.scy.client.tools.fxformauthor.datamodel.DataHandler;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import java.net.URI;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author pg
 */

public class FormAuthorNode extends CustomNode, Resizable, ScyToolFX, ILoadXML, EloSaverCallBack  {
    public-init var formAuthorRepositoryWrapper:FormAuthorRepositoryWrapper;
    public var toolBrokerAPI:ToolBrokerAPI;
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var technicalFormatKey: IMetadataKey;
    public var scyWindow:ScyWindow on replace {
        formList.setScyWindow(scyWindow);
    };
    public var spacing:Number on replace { requestLayout() }

    public override var width = bind scyWindow.width;
    public override var height = bind scyWindow.height;

    var formList:FormList;
    var viewer:FormViewer;

    var scyFormAuthorType = "scy/formauthor";
    var elo:IELO;
    var eloUri:String = "n/a";

    public var windowTitle:String;

    public override function create() : Node {
        formList = FormList{formNode: this};
        return formList;
    }

    public function loadViewer():Void {
        delete formList from children;
        viewer = FormViewer {
            scyWindow: scyWindow;
            repository: repository;
            eloFactory: eloFactory;
            metadataTypeManager: metadataTypeManager;
            formNode: this;
        }
        var fdm = DataHandler.getInstance().getLastFDM();
        if(fdm != null) {
            viewer.loadFDM(fdm);
        }
        else { 
            fdm = formList.createFDM();
            viewer.loadFDM(fdm);
        }

        //viewer.loadFDM(DataHandler.getInstance().getLastFDM());
        insert viewer into children;
    }

    public function loadAuthor():Void {
        delete viewer from children;
        insert formList into children;
        //formList.loadFDM(DataHandler.getInstance().getLastFDM());
    }

    override function getPrefWidth(height:Number):Number {
        return 600;
    }

    override function getPrefHeight(width:Number):Number {
        return 400;
    }

    override function setTitle(title:String):Void {
        windowTitle = title;
    }

    public function setFormAuthorRepositoryWrapper(wrapper:FormAuthorRepositoryWrapper):Void {
        formAuthorRepositoryWrapper = wrapper;
    }

    override function loadXML(xml:String):Void {
        formList.createFromString(xml);
    }

    override function getDescription() : String {
        return formList.description;
    }


    override function getXML():String {
        return formList.getXMLString();
    }

    public override function postInitialize(): Void {
        formAuthorRepositoryWrapper = new FormAuthorRepositoryWrapper(this);
        formAuthorRepositoryWrapper.setRepository(repository);
        formAuthorRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        formAuthorRepositoryWrapper.setEloFactory(eloFactory);
        formAuthorRepositoryWrapper.setDocName(scyWindow.title);
    }


    public override function initialize(windowContent: Boolean):Void {
        repository = toolBrokerAPI.getRepository();
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
        eloFactory = toolBrokerAPI.getELOFactory();
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
    }
    
    public function browseElos():Void {
        formAuthorRepositoryWrapper.loadFormAction();
    }

    public function saveElo():Void {
        //formAuthorRepositoryWrapper.saveFormAction();
        doSaveELO();
    }

    public function saveAsElo():Void {
        doSaveAsELO();
    }

    public override function loadElo(uri:URI) {
        doLoadELO(uri);
    }
 
    function doLoadELO(eloUri:URI) {
        var newElo = repository.retrieveELO(eloUri);
        if(newElo != null) {
            loadXML(newElo.getContent().getXmlString());
            this.eloUri = eloUri.toString();
            //logger.info("youtubeR: elo loaded");
            elo = newElo;
        }
    }
    
    function doSaveELO() {
        eloSaver.eloUpdate(getELO(), this);
        this.eloUri = elo.getUri().toString(); // stolen from filtex, dont know why (:
    }

    function doSaveAsELO() {
        eloSaver.eloSaveAs(getELO(), this);
    }

    function getELO():IELO {
        if(elo == null) {
            elo = eloFactory.createELO();
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyFormAuthorType);
        }
        elo.getContent().setXmlString(getXML()); 
        return elo;
    }

   public override function onQuit(): Void {
      if (elo != null) {
         def oldContentXml = elo.getContent().getXmlString();
         def newContentXml = getELO().getContent().getXmlString();
         if (oldContentXml == newContentXml) {
            // nothing changed
            return;
         }
      }
      doSaveELO();
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        eloUri = elo.getUri().toString();
    }




}
