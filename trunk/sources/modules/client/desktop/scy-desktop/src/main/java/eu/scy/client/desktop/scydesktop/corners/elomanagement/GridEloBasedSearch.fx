/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.Node;
import com.javafx.preview.layout.Grid;
import com.javafx.preview.layout.GridRow;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.javafx.preview.layout.GridLayoutInfo;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.HPos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.desktoputils.art.EloIcon;

/**
 * @author SikkenJ
 */
public class GridEloBasedSearch extends GridSearchNode {

    def baseEloInfo = ExtendedScyEloDisplayNode {
                layoutInfo: eloInfoLayoutInfo
                newEloCreationRegistry: newEloCreationRegistry
                scyElo: bind baseElo
                eloIcon: bind baseEloIcon
            }
    public def searchersList: ListView = ListView {
                layoutInfo: LayoutInfo {
                    height: 75
                    minHeight: 50
                    vfill: true
                    vgrow: Priority.NEVER
                    vshrink: Priority.NEVER
                }
            };
    var baseButton: Button;
    public var doSearch: function(gridEloBasedSearch: GridEloBasedSearch): Void;
    public var baseOnAction: function(gridEloBasedSearch: GridEloBasedSearch): Void;
    public var baseElo: ScyElo;
    public var baseEloIcon: EloIcon;
    public-read def selectedEloBasedSearchers = bind selectedSearchers as EloBasedSearcher[] on replace {
                doSearch(this);
            };

    init {
        FX.deferAction(function(): Void {
            searchersList.cellFactory = eloBasedSearcherCellFactory;
            showSearchResult(null);
//         sizeChanged();
        });
    }

    public override function create(): Node {
        grid = Grid {
                    managed: false
                    hgap: spacing
                    vgap: spacing
                    padding: gridPadding
                    growRows: [0, 0, 0, 0, 1, 0]
                    growColumns: [0, 1]
                    rows: [
                        GridRow {
                            cells: [
                                getLeftColumnFiller(),
                                Label {
                                    text: ##"ELO"
                                }
                            ]
                        }
                        GridRow {
                            cells: [
                                baseEloInfo
                            ]
                        }
                        getDevideLine(),
                        GridRow {
                            cells: [
                                getLeftColumnFiller(),
                                Label {
                                    text: ##"Search for"
                                    layoutInfo: GridLayoutInfo { hspan: 2 }
                                }
                            ]
                        }
                        GridRow {
                            cells: [
                                getLeftColumnFiller(),
                                searchersList
                            ]
                        }
                        getDevideLine(),
                        getResultDisplayPart(),
                        GridRow {
                            cells: [
                                getLeftColumnFiller(),
                                HBox {
                                    spacing: spacing
                                    layoutInfo: LayoutInfo {
                                    //                           hpos: HPos.RIGHT
                                    }
                                    hpos: HPos.RIGHT
                                    content: [
                                        baseButton = Button {
                                                    disable: bind selectedSearchResult == null
                                                    text: ##"Use as base"
                                                    action: function() {
                                                        baseOnAction(this);
                                                    }
                                                }
                                        openButton,
                                        cancelButton
                                    ]
                                }
                            ]
                        }
                    ]
                }
    }

    var selectedSearchers:EloBasedSearcher[];

    function eloBasedSearcherCellFactory(): ListCell {
        var listCell: SearcherListCell = SearcherListCell {}
        def onSelectionChange = function():Void{
            if(listCell.checkBox.selected){
                insert listCell.eloSearcher into selectedSearchers;
            } else {
                delete listCell.eloSearcher from selectedSearchers;
            }
        };
        listCell.onSelectionChanged = onSelectionChange; 
        return listCell;
    }
}

function run() {
    Stage {
        title: "Grid elo based seach test"
        onClose: function() {
        }
        scene: Scene {
            width: 400
            height: 400
            content: [
                GridEloBasedSearch {
                    layoutX: 10
                    layoutY: 10
                }
            ]
        }
    }

}
