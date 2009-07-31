package eu.scy.colemo.client.shapes.concepts;

import eu.scy.colemo.client.shapes.ConceptShape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.IOException;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 25.jun.2009
 * Time: 10:22:48
 */
public class SVGConcept extends ConceptShape {

    private GraphicsNode rootNode;
    private SVGDocument document;

    public SVGConcept(URL url) throws IOException {
        if (url == null) throw new IOException("URL cannot be null");
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        this.document = (SVGDocument) factory.createDocument(url.toString());
        this. rootNode = getRootNode(document);
    }

    public SVGConcept(SVGDocument shape) {
        this.document = shape;
        this. rootNode = getRootNode(document);
    }

    private double getSX(Rectangle bounds) {
        return bounds.width / rootNode.getBounds().getWidth();
    }

    /**
     * @param bounds The bounds of the resulting shape
     * @return The factor by which the shape are scaled along the Y axis direction
     */
    private double getSY(Rectangle bounds) {
        return bounds.height / rootNode.getBounds().getHeight();
    }

    @Override
    public Point getConnectionPoint(Point point, Rectangle bounds) {
        double x = point.x;
        double y = point.y;
        if (point.x > bounds.getMaxX()) x = bounds.getMaxX();
        else if (point.x < bounds.getMinX()) x = bounds.getMinX();

        if (point.y > bounds.getMaxY()) y = bounds.getMaxY();
        else if (point.y < bounds.getMinY()) y = bounds.getMinY();

        return new Point((int) x, (int) y);
    }

    /**
     * Get svg root from the given document.
     *
     * @param document svg resource
     */
    private static GraphicsNode getRootNode(SVGDocument document) {
        // Build the tree and get the document dimensions
        UserAgentAdapter userAgentAdapter = new UserAgentAdapter();
        BridgeContext bridgeContext = new BridgeContext(userAgentAdapter);
        GVTBuilder builder = new GVTBuilder();

        return builder.build(bridgeContext, document);
    }

    @Override
    public void paint(Graphics g, Rectangle bounds) {
        Graphics2D g2 = (Graphics2D) g.create();

        int width = bounds.width;
        int height = bounds.height;

        // Scale image to desired size
        Element elt = document.getRootElement();

        AffineTransform usr2dev = ViewBox.getViewTransform(null, elt, width, height, null);

        usr2dev.scale(getSX(bounds), getSY(bounds));
        g2.transform(usr2dev);

        rootNode.paint(g2);

        g2.dispose();

    }
}
