/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.VPos;
import javafx.util.Math;

/**
 * @author SikkenJ
 */
public class SimpleScySearchResultCellNode extends CustomNode {

    public var scySearchResult: ScySearchResult on replace { newScySearchResult() };
    def titleDisplay = Label {};
    def authorDisplay = Label {};
    def dateDisplay = Label {};
    def spacing = 5.0;
    def relevanceBarWidth = 30.0;
    def relevanceBarHeight = 8.0;
    var relevance: Number;
    def hBox = HBox {
                spacing: spacing
                content: []
                nodeVPos:VPos.CENTER
            }

    public override function create(): Node {
        hBox
    }

    public function getRelevanceNode(relevance: Number){
        def fillColor = (if (relevance > 0.75) Color.GREEN else if (relevance > 0.5) Color.YELLOW else Color.RED);
        def relevanceNode = Group {
                content: [
                    Rectangle {
                        width: relevanceBarWidth
                        height: relevanceBarHeight
                        stroke: Color.GRAY
                        fill: Color.GRAY
                    },
                     Rectangle {
                         //TODO This has to be done as the relevance might be greater than 1.0 and normalization has to be discussed
                        width: Math.min(relevanceBarWidth, relevanceBarWidth * relevance);
                        height: relevanceBarHeight
                        stroke: Color.BLACK
                        fill: fillColor
                    }
                ]
            }
            if(scySearchResult!=null){
                return relevanceNode;
            }
            return Group{content:[]};
    }


    function newScySearchResult() {
        relevance = scySearchResult.getRelevance();
        titleDisplay.text = scySearchResult.getScyElo().getTitle();
        authorDisplay.text = ScyEloDisplayProperties.getAuthorsText(scySearchResult.getScyElo());
        dateDisplay.text = ScyEloDisplayProperties.getDateString(scySearchResult.getScyElo());
        hBox.content = [
                    getRelevanceNode(relevance),
                    scySearchResult.getEloIcon() as EloIcon,
                    VBox {
                        spacing: -1
                        content: [
                            titleDisplay,
                            authorDisplay,
                            dateDisplay
                        ]
                    }
                ];
    }

}
