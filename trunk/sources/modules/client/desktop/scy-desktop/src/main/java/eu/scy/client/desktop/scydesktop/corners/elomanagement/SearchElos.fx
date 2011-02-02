/*
 * SearchElos.fx
 *
 * Created on 28-feb-2010, 15:38:37
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.Node;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;

/**
 * @author sikken
 */
public class SearchElos extends ModalDialogNode, ScyEloListCellDisplay, ShowSearching {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def line: javafx.scene.shape.Line = javafx.scene.shape.Line {
        layoutX: -1.0
        layoutY: 152.0
        stroke: javafx.scene.paint.Color.LIGHTBLUE
        strokeWidth: 2.0
        endX: 493.0
        endY: 0.0
    }
    
    def __layoutInfo_simpleSearchButton: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 93.0
    }
    public-read def simpleSearchButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 300.0
        layoutY: 50.0
        layoutInfo: __layoutInfo_simpleSearchButton
        text: "##Search"
        action: simpleSearchButtonAction
    }
    
    def __layoutInfo_simpleSearchField: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 192.0
    }
    public-read def simpleSearchField: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 100.0
        layoutY: 50.0
        layoutInfo: __layoutInfo_simpleSearchField
    }
    
    public-read def advancedSearchLink: javafx.scene.control.Hyperlink = javafx.scene.control.Hyperlink {
        layoutX: 400.0
        text: "##advanced search"
    }
    
    public-read def simpleSearchGroup: javafx.scene.Group = javafx.scene.Group {
        visible: true
        layoutX: 12.0
        layoutY: 6.0
        translateX: 50.0
        translateY: -35.0
        content: [ simpleSearchButton, simpleSearchField, advancedSearchLink, ]
    }
    
    public-read def label: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 539.0
        layoutY: 28.0
        text: "##Query:"
    }
    
    public-read def simpleSearchLink: javafx.scene.control.Hyperlink = javafx.scene.control.Hyperlink {
        layoutX: 526.0
        layoutY: 0.0
        translateX: 400.0
        text: "##simple search"
    }
    
    public-read def label2: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 634.0
        layoutY: 28.0
        text: "##All"
    }
    
    public-read def allTypesCheckBox: javafx.scene.control.CheckBox = javafx.scene.control.CheckBox {
        layoutX: 668.0
        layoutY: 28.0
        onMouseClicked: allTypesCheckBoxOnMouseClicked
        text: ""
        selected: true
    }
    
    def __layoutInfo_typesListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 150.0
        height: 93.0
    }
    public-read def typesListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 539.0
        layoutY: 51.0
        layoutInfo: __layoutInfo_typesListView
        onMouseClicked: typesListViewOnMouseClicked
    }
    
    public-read def label3: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 780.0
        layoutY: 22.0
        text: "##Title"
    }
    
    def __layoutInfo_nameTextbox: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 169.0
        height: 24.0
    }
    public-read def nameTextbox: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 780.0
        layoutY: 48.0
        layoutInfo: __layoutInfo_nameTextbox
    }
    
    def __layoutInfo_mineCheckBox: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 17.0
        height: 17.0
    }
    public-read def mineCheckBox: javafx.scene.control.CheckBox = javafx.scene.control.CheckBox {
        layoutX: 817.0
        layoutY: 125.0
        layoutInfo: __layoutInfo_mineCheckBox
        onMouseClicked: onlyMineCheckBoxOnMouseClicked
        text: ""
    }
    
    def __layoutInfo_label4: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 49.0
        height: 22.0
    }
    public-read def label4: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 780.0
        layoutY: 93.0
        layoutInfo: __layoutInfo_label4
        text: "##Author"
    }
    
    public-read def searchButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 946.0
        layoutY: 124.0
        text: "##Search"
        hpos: javafx.geometry.HPos.RIGHT
        action: searchButtonAction
    }
    
    public-read def label6: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 780.0
        layoutY: 125.0
        text: "##Me"
    }
    
    public-read def label7: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 850.0
        layoutY: 125.0
        text: "##Others"
    }
    
    public-read def othersCheckBox: javafx.scene.control.CheckBox = javafx.scene.control.CheckBox {
        layoutX: 908.0
        layoutY: 125.0
        text: ""
    }
    
    public-read def advancedSearchGroup: javafx.scene.Group = javafx.scene.Group {
        visible: true
        content: [ label, simpleSearchLink, label2, allTypesCheckBox, typesListView, label3, nameTextbox, mineCheckBox, label4, searchButton, label6, label7, othersCheckBox, ]
    }
    
    def __layoutInfo_stack: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 474.0
        height: 141.0
    }
    public-read def stack: javafx.scene.layout.Stack = javafx.scene.layout.Stack {
        layoutX: 6.0
        layoutY: 0.0
        layoutInfo: __layoutInfo_stack
        content: [ simpleSearchGroup, advancedSearchGroup, ]
    }
    
    public-read def foundLabel: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 25.0
        layoutY: 3.0
        text: "##Found"
    }
    
    def __layoutInfo_resultsListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 384.0
        height: 112.0
    }
    public-read def resultsListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 25.0
        layoutY: 24.0
        layoutInfo: __layoutInfo_resultsListView
        onMouseClicked: resultsListViewOnMouseClicked
    }
    
    def __layoutInfo_openButton: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 69.0
    }
    public-read def openButton: javafx.scene.control.Button = javafx.scene.control.Button {
        disable: true
        layoutX: 415.0
        layoutY: 39.0
        layoutInfo: __layoutInfo_openButton
        text: "##Open"
        action: openButtonAction
    }
    
    def __layoutInfo_cancelButton: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 68.0
    }
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 415.0
        layoutY: 86.0
        layoutInfo: __layoutInfo_cancelButton
        text: "##Cancel"
        textAlignment: javafx.scene.text.TextAlignment.LEFT
        hpos: javafx.geometry.HPos.RIGHT
        action: cancelButtonAction
    }
    
    public-read def progressIndicator: javafx.scene.control.ProgressIndicator = javafx.scene.control.ProgressIndicator {
        layoutX: 147.0
        layoutY: 45.0
    }
    
    def __layoutInfo_resultPanel: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 499.0
        height: 154.0
    }
    public-read def resultPanel: javafx.scene.layout.Panel = javafx.scene.layout.Panel {
        layoutX: 0.0
        layoutY: 162.0
        layoutInfo: __layoutInfo_resultPanel
        content: [ foundLabel, resultsListView, openButton, cancelButton, progressIndicator, ]
    }
    
    public-read def scene: javafx.scene.Scene = javafx.scene.Scene {
        width: 509.0
        height: 333.0
        content: getDesignRootNodes ()
        camera: null
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ line, stack, resultPanel, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        scene
    }
    // </editor-fold>//GEN-END:main

    function cancelButtonAction(): Void {
        cancelAction(this);
    }

    function openButtonAction(): Void {
        openAction(this);
    }

    function resultsListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
        openButton.disable = resultsListView.selectedIndex < 0;
        if (event.clickCount == 2 and resultsListView.selectedIndex >= 0) {
            openButtonAction();
        }
    }

    function searchButtonAction(): Void {
        searchAction(this);
    }

    function simpleSearchButtonAction(): Void {
        simpleSearchAction(this);
    }

    function typesListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
        allTypesCheckBox.selected = false;
        if (event.clickCount == 2) {
            searchButtonAction();
        }
    }

    function onlyMineCheckBoxOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
    //TODO
    }

    function allTypesCheckBoxOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
        typesListView.clearSelection();
    }

    public var searchAction: function(searchElos: SearchElos): Void;
    public var simpleSearchAction: function(searchElos: SearchElos): Void;
    public var openAction: function(searchElos: SearchElos): Void;
    public var cancelAction: function(searchElos: SearchElos): Void;
    var foundLabelText: String;

    init {
        resultsListView.cellFactory = scyEloCellFactory;
        progressIndicator.visible = false;
        simpleSearchGroup.visible = true;
        advancedSearchGroup.visible = false;
        simpleSearchLink.action = hyperlinkSimpleSearchAction;
        simpleSearchField.action = function(): Void {
                    simpleSearchAction(this)
                };
        advancedSearchLink.action = hyperlinkAdvancedSearchAction;
    }

    function hyperlinkSimpleSearchAction(): Void {
        delete  resultsListView.items;
        simpleSearchGroup.visible = true;
        simpleSearchField.requestFocus();
        Timeline {
            keyFrames: [
                KeyFrame {
                    time: 500ms
                    values: [
                        simpleSearchGroup.opacity => 1.0 tween Interpolator.EASEBOTH,
                        advancedSearchGroup.opacity => 0.0 tween Interpolator.EASEBOTH,
                    ]
                }
            ]
        }.play();
        advancedSearchGroup.visible = false;
    }

    function hyperlinkAdvancedSearchAction(): Void {
        delete  resultsListView.items;
        advancedSearchGroup.visible = true;
        nameTextbox.requestFocus();
        Timeline {
            keyFrames: [
                KeyFrame {
                    time: 500ms
                    values: [
                        advancedSearchGroup.opacity => 1.0 tween Interpolator.EASEBOTH,
                        simpleSearchGroup.opacity => 0.0 tween Interpolator.EASEBOTH,
                    ]
                }
            ]
        }.play();
        simpleSearchGroup.visible = false;
    }

    override public function getContentNodes(): Node[] {
        return getDesignRootNodes();
    }

    public override function showSearching(): Void {
        delete  resultsListView.items;
        resultsListView.disable = true;
        progressIndicator.visible = true;
        setNumberOfResults("?");
    }

    public override function showSearchResult(results: Object[]): Void {
        resultsListView.items = results;
        resultsListView.disable = false;
        progressIndicator.visible = false;
        setNumberOfResults("{sizeof resultsListView.items}");
    }

    postinit{}

    function setNumberOfResults(nr: String): Void {
        if (foundLabelText == "") {
            foundLabelText = foundLabel.text;
        }
        foundLabel.text = "{foundLabelText} : {nr}";
    }

}
