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
import javax.swing.JOptionPane;

/**
 * @author pg
 */

public class FormAuthorNode extends CustomNode, Resizable, ScyToolFX, ILoadXML {
    public-init var formAuthorRepositoryWrapper:FormAuthorRepositoryWrapper;
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle();
        formList.setScyWindow(scyWindow);
    };
    public var spacing:Number on replace { requestLayout() }
    var nodes:Node[];
    override var children = bind nodes;
    var formList:FormList = FormList{formNode: this};

    postinit {
        insert formList into nodes;

    }

    override function getPrefWidth(height:Number):Number {
        return 600;
    }

    override function getPrefHeight(width:Number):Number {
        return 400;
    }

    function setScyWindowTitle():Void {

    }

    public function setFormAuthorRepositoryWrapper(wrapper:FormAuthorRepositoryWrapper):Void {
        formAuthorRepositoryWrapper = wrapper;
    }

    override function loadXML(xml:String):Void {
        formList.fromString(xml);
    }

    override function getXML():String {
        return formList.getXML();
    }


    public override function postInitialize(): Void {
        formAuthorRepositoryWrapper = new FormAuthorRepositoryWrapper(this);
        formAuthorRepositoryWrapper.setRepository(repository);
        formAuthorRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        formAuthorRepositoryWrapper.setEloFactory(eloFactory);
        formAuthorRepositoryWrapper.setDocName(scyWindow.title);
    }

    public function browseElos():Void {
        formAuthorRepositoryWrapper.loadFormAction();
    }

    public function saveElo():Void {
        formAuthorRepositoryWrapper.saveFormAction();
    }


}
