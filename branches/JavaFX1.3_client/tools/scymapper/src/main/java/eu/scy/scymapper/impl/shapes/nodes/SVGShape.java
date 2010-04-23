package eu.scy.scymapper.impl.shapes.nodes;

import eu.scy.scymapper.api.shapes.INodeShape;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 25.jun.2009
 * Time: 10:22:48
 */
public class SVGShape extends INodeShape {

	private INodeShape outlineShape = new RoundRectangle();

	private transient GraphicsNode rootNode;
	private SVGDocument document;

	private transient URL url;
	private String name;

	private Object readResolve() throws IOException {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
		this.document = (SVGDocument) factory.createDocument(url.toString());
		this.rootNode = getRootNode(document);
		return this;
	}

	public SVGShape(String urlStr) throws IOException {
		this(SVGShape.class.getResource(urlStr));
	}

	public SVGShape(URL url) throws IOException {
		if (url == null) throw new IOException("URL cannot be null");
		this.url = url;
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
		this.document = (SVGDocument) factory.createDocument(url.toString());
		this.rootNode = getRootNode(document);
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
//		TODO: Get the shape outline of the SVG and calculate the point nearest to the point from parameter.

//		Shape outlineShape = rootNode.getOutline();
//
//		PathIterator pit = outlineShape.getPathIterator(null);
//		double[] coords = new double[6];
//		Point2D nearestPoint = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
//		Point2D.Double p = new Point2D.Double();
//		double minDist = Double.MAX_VALUE;
//		while (!pit.isDone()) {
//			int type = pit.currentSegment(coords);
//			switch (type) {
//				case PathIterator.SEG_MOVETO:
//					Point2D p1 = new Point2D.Double(coords[0], coords[1]);
//					Point2D p2 = new Point2D.Double(coords[3], coords[4]);
//					double d = point.distance(p1);
//					if (d < minDist) {
//						minDist = d;
//						nearestPoint = p1;
//					}
//					d = point.distance(p2);
//					if (d < minDist) {
//						minDist = d;
//						nearestPoint = p2;
//					}
//					break;
//				case PathIterator.SEG_LINETO:
//					Line2D.Double line = new Line2D.Double(coords[0], coords[1], coords[2], coords[3]);
//					double dist = line.ptSegDist(point.getX(), point.getY());
//					if (dist < minDist) {
//						minDist = dist;
//						double h = line.ptLineDist(point.getX(), point.getY() -10);
//					}
//					break;
//				case PathIterator.SEG_QUADTO:
//					break;
//				case PathIterator.SEG_CUBICTO:
//					break;
//				case PathIterator.SEG_CLOSE:
//					pit.next();
//					continue;
//				default:
//					System.out.println("unexpected type: " + type);
//			}
//			System.out.println("coords = " + coords[0] + "," + coords[1]);
//			double xPoint = coords[0] * getSX(bounds);
//			double yPoint = coords[1] * getSY(bounds);
//			double xPointPos = bounds.x + xPoint;
//			double yPointPos = bounds.y + yPoint;
//
//			Point thisPoint = new Point((int) xPointPos, (int) yPointPos);
//			System.out.println("thisPoint = " + thisPoint);
//
//			if (nearestPoint == null || thisPoint.distance(p) < nearestPoint.distance(p))
//				nearestPoint = thisPoint;
//
//			pit.next();
//		}
		return outlineShape.getConnectionPoint(point, bounds);
	}

	public Point2D.Float getIntersectionPoint(Line2D.Float line1, Line2D.Float line2) {
		if (!line1.intersectsLine(line2)) return null;
		double px = line1.getX1(),
				py = line1.getY1(),
				rx = line1.getX2() - px,
				ry = line1.getY2() - py;
		double qx = line2.getX1(),
				qy = line2.getY1(),
				sx = line2.getX2() - qx,
				sy = line2.getY2() - qy;

		double det = sx * ry - sy * rx;
		if (det == 0) {
			return null;
		} else {
			double z = (sx * (qy - py) + sy * (px - qx)) / det;
			if (z == 0 || z == 1) return null;  // intersection at end point!
			return new Point2D.Float(
					(float) (px + z * rx), (float) (py + z * ry));
		}
	} // end intersection line-line

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

		AffineTransform transform = new AffineTransform();

		transform.scale(getSX(bounds), getSY(bounds));
		rootNode.setTransform(transform);

		rootNode.paint(g2);

//		g2.setColor(Color.black);
//		g2.setStroke(new BasicStroke(2));

		//g2.draw(rootNode.getOutline());

		g2.dispose();

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
