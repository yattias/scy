/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import java.util.HashMap;
import java.util.Map;
import javafx.data.pull.PullParser;
import java.io.ByteArrayInputStream;
import javafx.data.pull.Event;
import java.net.URI;
import javax.xml.stream.*;
import java.io.ByteArrayOutputStream;
import javafx.geometry.Point2D;

/**
 * @author giemza
 */

public class StandardWindowPositionsState extends WindowPositionsState {

    def windowStatesDocumentTag: String = "windowstates";
    def windowStateTage: String = "window";

    def uriTag:String = "uri";
    def layoutXTag:String = "layoutX";
    def layoutYTag:String = "layoutY";
    def relativeLayoutCenterXTag:String = "relativeLayoutCenterX";
    def relativeLayoutCenterYTag:String = "relativeLayoutCenterY";
    def widthTag:String = "width";
    def heightTag:String = "height";
    def rotateTag:String = "rotation";
    def relativeWidthTag:String = "relativeWidth";
    def relativeHeightTag:String = "relativeHeight";
    def openedTag:String = "opened";
    def isManuallyRepositionedTag:String = "isManuallyRepositioned";

    def windowUriToLayoutMap:Map = new HashMap();

    def factory: XMLOutputFactory = XMLOutputFactory.newInstance();

    def stateParser = PullParser {
        documentType: PullParser.XML;
        onEvent: function (e: Event) {
            if (e.type == PullParser.START_ELEMENT) {
                if (e.qname.name.equals(windowStateTage)) {
                    def uri: URI = URI.create(e.getAttributeValue(uriTag) as String);
                    def layoutState = WindowLayoutState {
                        uri: uri;
                        layoutX: Float.parseFloat(e.getAttributeValue(layoutXTag));
                        layoutY: Float.parseFloat(e.getAttributeValue(layoutYTag));
                        relativeLayoutCenterX: Float.parseFloat(e.getAttributeValue(relativeLayoutCenterXTag));
                        relativeLayoutCenterY: Float.parseFloat(e.getAttributeValue(relativeLayoutCenterYTag));
                        width: Float.parseFloat(e.getAttributeValue(widthTag));
                        height: Float.parseFloat(e.getAttributeValue(heightTag));
                        relativeWidth: Float.parseFloat(e.getAttributeValue(relativeWidthTag));
                        relativeHeight: Float.parseFloat(e.getAttributeValue(relativeHeightTag));
                        rotate: Float.parseFloat(e.getAttributeValue(rotateTag));
                        opened: Boolean.parseBoolean(e.getAttributeValue(openedTag));
                        isManuallyRepositioned : Boolean.parseBoolean(e.getAttributeValue(isManuallyRepositionedTag))
                    }
                    windowUriToLayoutMap.put(uri, layoutState);
                }
            }
        }
    }

    public function persistWindowState(window:ScyWindow) {
        // first we remove the old state
        windowUriToLayoutMap.remove(window.eloUri);
        // now it's time to create a new state and save it
        def layoutState = WindowLayoutState {
            uri: window.eloUri;
            layoutX: window.layoutX;
            layoutY: window.layoutY;
            relativeLayoutCenterX: window.relativeLayoutCenterX;
            relativeLayoutCenterY: window.relativeLayoutCenterY;
            width: window.width;
            height: window.height;
            relativeWidth: window.relativeWidth;
            relativeHeight: window.relativeHeight;
            rotate: window.rotate;
            opened: not window.isClosed;
            isManuallyRepositioned: window.isManuallyRepositioned;
        }
        windowUriToLayoutMap.put(window.eloUri, layoutState);
    }

    public function isStateForWindowAvailable(window:ScyWindow) {
        return windowUriToLayoutMap.containsKey(window.eloUri);
    }


    public function applyStateForWindow(window:ScyWindow) {
        def layoutState:WindowLayoutState = windowUriToLayoutMap.get(window.eloUri) as WindowLayoutState;
        if (layoutState != null) {
            window.layoutX = layoutState.layoutX;
            window.layoutY = layoutState.layoutY;
            window.closedPosition = Point2D {
                x: window.layoutX;
                y: window.layoutY;
            }
            window.relativeLayoutCenterX = layoutState.relativeLayoutCenterX;
            window.relativeLayoutCenterY = layoutState.relativeLayoutCenterY;
            window.rotate = layoutState.rotate;
            window.width = layoutState.width;
            window.height = layoutState.height;
            window.relativeWidth = layoutState.relativeWidth;
            window.relativeHeight = layoutState.relativeHeight;
            window.isManuallyRepositioned = layoutState.isManuallyRepositioned;
            window.isClosed = not layoutState.opened;
        }
    }


    public override function setXml(xml:String):Void{
        windowUriToLayoutMap.clear();
        stateParser.input = new ByteArrayInputStream(xml.getBytes("utf-8"));
        stateParser.parse();
    }

    public override function getXml():String{
        var output: ByteArrayOutputStream = new ByteArrayOutputStream();
        var writer: XMLStreamWriter = factory.createXMLStreamWriter(output);

        //writer.writeStartDocument();
        writer.writeStartElement(windowStatesDocumentTag);
        for (uriObj in windowUriToLayoutMap.keySet()) {
            def uri: URI = uriObj as URI;
            def layoutState: WindowLayoutState = windowUriToLayoutMap.get(uri) as WindowLayoutState;
            writer.writeStartElement(windowStateTage);
            writer.writeAttribute(uriTag, uri.toString());
            writer.writeAttribute(layoutXTag, "{layoutState.layoutX}");
            writer.writeAttribute(layoutYTag, "{layoutState.layoutY}");
            writer.writeAttribute(relativeLayoutCenterXTag, "{layoutState.relativeLayoutCenterX}");
            writer.writeAttribute(relativeLayoutCenterYTag, "{layoutState.relativeLayoutCenterY}");
            writer.writeAttribute(widthTag, "{layoutState.width}");
            writer.writeAttribute(heightTag, "{layoutState.height}");
            writer.writeAttribute(rotateTag, "{layoutState.rotate}");
            writer.writeAttribute(relativeWidthTag, "{layoutState.relativeWidth}");
            writer.writeAttribute(relativeHeightTag, "{layoutState.relativeHeight}");
            writer.writeAttribute(openedTag, "{layoutState.opened}");
            writer.writeAttribute(isManuallyRepositionedTag, "{layoutState.isManuallyRepositioned}");
            writer.writeEndElement();
        }
        writer.writeEndDocument();
        writer.close();

        return output.toString("utf-8");
    }
}
