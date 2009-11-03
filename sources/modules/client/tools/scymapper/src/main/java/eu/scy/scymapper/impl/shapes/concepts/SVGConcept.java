package eu.scy.scymapper.impl.shapes.concepts;

import eu.scy.scymapper.api.shapes.INodeShape;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 25.jun.2009
 * Time: 10:22:48
 */
public class SVGConcept extends INodeShape {

    private transient GraphicsNode rootNode;
    private transient SVGDocument document;

    private URL url;
    private String name;

	private Object readResolve() throws IOException {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        this.document = (SVGDocument) factory.createDocument(url.toString());
        this. rootNode = getRootNode(document);
		return this;
	}
    public SVGConcept(String urlStr) throws IOException {
        this(SVGConcept.class.getResource(urlStr));
    }

    public SVGConcept(URL url) throws IOException {
        if (url == null) throw new IOException("URL cannot be null");
        this.url = url;
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        this.document = (SVGDocument) factory.createDocument(url.toString());
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

        g2.translate(bounds.x, bounds.y);
        g2.transform(usr2dev);

        rootNode.paint(g2);

        g2.dispose();

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
