/*
 * FlyingSaucerBrowser.fx
 *
 * Created on 17-sep-2009, 11:13:33
 */
package eu.scy.client.tools.fxflyingsaucer;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Resizable;
import java.awt.Dimension;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.ScyToolGetter;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import javafx.ext.swing.SwingComponent;

/**
 * @author sikkenj
 */
public class FlyingSaucerBrowser extends CustomNode, Resizable, ScyToolGetter {

    public override var width on replace { resizeBrowser()
            };
    public override var height on replace { resizeBrowser()
            };
    public-init var flyingSaucer: EloFlyingSaucerPanel;
    public-init var urlSource: UrlSource;
    public-init var scyWindow: ScyWindow;

    function resizeBrowser() {
        flyingSaucer.setPreferredSize(new Dimension(width, height));
    }

    def drawerDescription = Text {
                font: Font.font("Verdana", FontWeight.BOLD, 12);
                fill: scyWindow.windowColorScheme.mainColor
                content: "{urlSource.name().charAt(0)}{urlSource.name().substring(1).toLowerCase()}"
            }
    def spacing = 5.0;

    public override function create(): Node {
        var content;
        if (urlSource == UrlSource.ELO) {
            content = [createBrowserComponent()];
        } else {
            content = [drawerDescription, createBrowserComponent()];
        }
        return VBox {
                    spacing: spacing
                    nodeHPos: HPos.CENTER
                    padding: Insets {
                        top: spacing
                        right: spacing
                        bottom: spacing
                        left: spacing
                    }

                    content: content
                };
    }

    function createBrowserComponent(): Node {
        SwingComponent.wrap(flyingSaucer)
    }

    override function getPrefWidth(width: Number): Number {
        return flyingSaucer.getPreferredSize().width;
    }

    override function getPrefHeight(width: Number): Number {
        return flyingSaucer.getPreferredSize().height;
    }

    override function getScyTool(): ScyTool {
        return flyingSaucer;
    }

}

function run() {
    var scene: Scene;
    Stage {
        title: "Flying saucer browser test"
        scene: scene = Scene {
                    width: 400
                    height: 400
                    content: [
                        FlyingSaucerBrowser {
                            width: bind scene.width;
                            height: bind scene.height;
                        }
                    ]
                }
    }

}
