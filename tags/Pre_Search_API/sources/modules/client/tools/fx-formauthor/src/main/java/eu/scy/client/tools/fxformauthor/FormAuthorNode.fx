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

/**
 * @author pg
 */

public class FormAuthorNode extends CustomNode, Resizable, ScyToolFX, ILoadXML, EloSaverCallBack  {
    public-init var formAuthorRepositoryWrapper:FormAuthorRepositoryWrapper;
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var technicalFormatKey: IMetadataKey;
    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle();
        formList.setScyWindow(scyWindow);
    };
    public var spacing:Number on replace { requestLayout() }
    var nodes:Node[];
    override var children = bind nodes;
    var formList:FormList = FormList{formNode: this};
    var viewer:FormViewer;

    var scyFormAuthorType = "scy/formauthor";
    var elo:IELO;
    var eloUri:String = "n/a";

    public var windowTitle:String;

    postinit {
        insert formList into nodes;
    }

    public function loadViewer():Void {
        delete formList from nodes;
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
        insert viewer into nodes;
    }

    public function loadAuthor():Void {
        delete viewer from nodes;
        insert formList into nodes;
        //formList.loadFDM(DataHandler.getInstance().getLastFDM());
    }



    override function getPrefWidth(height:Number):Number {
        return 600;
    }

    override function getPrefHeight(width:Number):Number {
        return 400;
    }

    function setScyWindowTitle():Void {
        scyWindow.title = "FormAuthor: {windowTitle}";

    }

   override function setTitle(title:String):Void {
       windowTitle = title;
       setScyWindowTitle();
   }



    public function setFormAuthorRepositoryWrapper(wrapper:FormAuthorRepositoryWrapper):Void {
        formAuthorRepositoryWrapper = wrapper;
    }

    override function loadXML(xml:String):Void {
        formList.createFromString(xml);
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

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        eloUri = elo.getUri().toString();
    }




}