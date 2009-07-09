package eu.scy.nutpadfx;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.effect.*;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import org.jfxtras.scene.shape.Star2;
import javafx.scene.control.Button;
import org.jfxtras.scene.border.*;





class ANode extends CustomNode {
    public var text:String;
    public var width: Number = 100;
    public var height: Number = 100;
    public var raised: Boolean;

    public override function create(): Node {
        var txt:Text;
        return Group {
            content: [
                Rectangle {
                    width: bind width
                    height: bind height
                    fill: Color.RED
                    onMouseClicked: function(e):Void {
                        raised = not raised;
                    }

                },
                txt = Text {
                    translateX: bind (width - txt.layoutBounds.width)/2.0 - txt.layoutBounds.minX
                    translateY: bind (height - txt.layoutBounds.height)/2.0 - txt.layoutBounds.minY
                    content: bind text
                }
            ]
        };
    }
}



Stage {
    title: "JFXtras Border Demo"
    width: 800
    height: 800
    scene: Scene {
        stylesheets: "/Users/anthonjp/dev/libs/javaFx/layouts/JFXtras Core 0.5/test/org/jfxtras/scene/border/Border.css"
        fill: Color.YELLOW
        content: VBox {
            translateX: 10
            translateY: 10
            spacing: 20
            content: [
                HBox {
                    spacing: 20
                    content: [
                        BevelBorder {
                            var anode: ANode;
                            raised: bind anode.raised
                            node: anode = ANode { text: "BevelBorder" }
                        },
                        EllipseBorder {
                            var anode: ANode;
                            raised: bind anode.raised
                            node: anode = ANode { text: "EllipseBorder" }
                        },
                        Group {
                            var border: Border;
                            content: [
                                Rectangle { // shows the positioning of the content
                                            // within the  Border
                                    translateX: bind border.layoutBounds.minX
                                    translateY: bind border.layoutBounds.minY
                                    width: bind border.layoutBounds.width
                                    height: bind border.layoutBounds.height
                                    fill: null
                                    stroke: Color.BLACK
                                    strokeWidth: 0.0
                                    strokeDashArray: [4.0, 4.0]

                                },
                                border = EmptyBorder {
                                    shapeToFit: false
                                    nodeHPos: HPos.LEFT
                                    nodeVPos: VPos.TOP
                                    layoutInfo: LayoutInfo {
                                        width: 150
                                        height: 150
                                    }
                                    node: ANode { text: "EmptyBorder" }
                                },
                            ]
                        },
                        EtchedBorder {
                            var anode: ANode;
                            raised: bind anode.raised
                            node: anode = ANode { text: "EtchedBorder" }
                        }
                    ]
                },
                HBox {
                    spacing: 20
                    content: [
                        FrameBorder {
                            id: "MainFrame"
                            var anode: ANode;
                            raised: bind anode.raised

                            node: anode = ANode { text: "FrameBorder" }
                        },
                        LineBorder {
                            id: "MainLine"
                            node: ANode { text: "LineBorder" }
                        },
                        MetallicBorder {
                            var anode: ANode;
                            raised: bind anode.raised
                            node: anode = ANode { text: "MetallicBorder" }
                        },
                        PipeBorder {
                            var anode: ANode;
                            raised: bind anode.raised
                            node: anode = ANode { text: "PipeBorder" }
                        },
                    ]
                },
                HBox {
                    spacing: 20
                    content: [
                        SoftBevelBorder {
                            var anode: ANode;
                            raised: bind anode.raised
                            node: anode = ANode { text: "SoftBevelBorder" }
                        },
                        TitledBorder {
                            var anode: ANode;
                            id: "MainTitle"
                            text: "Foopar"
                            node: MetallicBorder{
                                raised: bind anode.raised
                                node: anode = ANode {
                                    width: 200
                                    text: "TitledBorder"
                                }
                            }
                        },
                        LineBorder {
                            id: "LineShadowBorder"
                            effect: DropShadow{};
                            node: ANode { text: "Line Shadow" }
                        },
                        RoundedRectBorder {
                            effect: DropShadow{};
                            var anode: ANode;
                            raised: bind anode.raised
                            node: anode = ANode { text: "Rounded\nShadow" }
                        }

                    ]
                },
                HBox {
                    spacing: 20
                    content: [
                        ImageBorder {
                            image: Image{ url: "{__DIR__}AusArt.jpg" }
                            node: ANode { text: "Image" }
                        },
                        Group {
                            var border: Border;
                            content: [
                                Rectangle { // shows the positioning of the content
                                            // within the  Border, should be centered.
                                    translateX: bind border.layoutBounds.minX
                                    translateY: bind border.layoutBounds.minY
                                    width: bind border.layoutBounds.width
                                    height: bind border.layoutBounds.height
                                    fill: null
                                    stroke: Color.BLACK
                                    strokeWidth: 0.0
                                    strokeDashArray: [4.0, 4.0]

                                },
                                border = EmptyBorder {
                                    shapeToFit: false
                                    layoutInfo : LayoutInfo {
                                        width: 150
                                        height: 150
                                    }
                                    node:
                                    ANode { text: "CenteredBorder" }
                                },
                            ]
                        },
                        // Demonstrates a Combo Border
                        TitledBorder {
                            text: "Combo Border"
                            id: "Combo"
                            node: FrameBorder {
                                var anode: ANode;
                                raised: bind anode.raised
                                node: ImageBorder {
                                    image: Image{ url: "{__DIR__}AusArt.jpg" }
                                    node: anode = ANode { text: "Image" }
                                }
                            }
                        },

                        Group {
                            var border: Border;
                            content: [
                                Rectangle { // shows the positioning of the content
                                            // within the  Border, should be centered.
                                    translateX: bind border.layoutBounds.minX
                                    translateY: bind border.layoutBounds.minY
                                    width: bind border.layoutBounds.width
                                    height: bind border.layoutBounds.height
                                    fill: null
                                    stroke: Color.BLACK
                                    strokeWidth: 0.0
                                    strokeDashArray: [4.0, 4.0]

                                },
                                border = ShapeBorder {
                                    var anode: ANode;
                                    shape: Star2 {
                                        centerX: bind anode.layoutBounds.width/2
                                        centerY: bind anode.layoutBounds.height/2
                                        outerRadius: bind anode.layoutBounds.width/2
                                        innerRadius: bind anode.layoutBounds.width/4
                                        count: 5
                                    }
                                    node:
                                        anode = ANode { text: "Shape Border" }
                                },
                            ]
                        }

                    ]
                },
            ]
        }

    }
}