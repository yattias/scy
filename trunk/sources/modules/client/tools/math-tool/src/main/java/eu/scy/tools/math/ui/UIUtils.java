package eu.scy.tools.math.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LinearGradientPaint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import eu.scy.tools.math.doa.json.CircleToolbarShape;
import eu.scy.tools.math.doa.json.CylinderToolbarShape;
import eu.scy.tools.math.doa.json.MathToolbarShape;
import eu.scy.tools.math.doa.json.RectanglarPrismToolbarShape;
import eu.scy.tools.math.doa.json.RectangleToolbarShape;
import eu.scy.tools.math.doa.json.SphereToolbarShape;
import eu.scy.tools.math.doa.json.TriangleToolbarShape;
import eu.scy.tools.math.ui.paint.Colors;
import eu.scy.tools.math.ui.paint.RoundedBorder;

public class UIUtils {

	public static final String JSON_CONFIG = "shapeconfiguration.json";
	public static final String PATTERN = "#.#";
	public static final int _PIXEL = 30;
	public static HashMap<String, Object> componentLookup;
	public static final String _3D = "3D"; //$NON-NLS-1$
	public static final String _2D = "2D"; //$NON-NLS-1$
	public static final String SHAPE_CANVAS = "SHAPE_CANVAS";
	public static final String TABS = "TABS";
	public static final String MATH_TOOL_PANEL = "MATH_TOOL_PANEL";
	public static final int SHAPE_END_POINT_SIZE = 8;
	public static final Font plainFont = new Font("Times New Roman", Font.PLAIN, 13);
    public static final Color dottedShapeLine = Color.blue;
    public static final String CIRCLE = "CIRCLE";
	public static final String TRIANGLE = "TRIANGLE";
	public static final String RECTANGLE = "RECTANGLE";
	public static final String RECTANGLE3D = "RECTANGLE3D";
	public static final String RECTANGLE3D_SMALL = RECTANGLE3D + "_SMALL";
	
	public static final String SPHERE3D = "SPHERE3D";
	public static final String CYLINDER3D = "CYLINDER3D";
	public static final Color ERROR_SHAPE_COLOR = Color.red;
	public static final String TYPE = "TYPE";
	public final static String SHAPE = "SHAPE";

	public static final String RECTANGLURAL_PRISM_NAME = "Rectanglural Prism";
	public static final String CLYINDER_NAME = "Clyinder";
	public static final String SPHERE_NAME = "Sphere";
	public static final String CIRCLE_NAME = "Circle";
	public static final String TRIANGLE_NAME = "Triangle";
	public static final String RECTANGLE_NAME = "Rectangle";
	
	public final static Color SHAPE_3D_DASH_BORDER_COLOR = Color.BLACK;
	public final static String METERS = "m";
	public static final String SHAPE_ID = "SHAPE_ID";
	public static final int ALPHA = 140;
	public static final String SKETCHUP_FILE = "googlesketchup.html";
	public static final Object SHAPE_OBJ = "SHAPE_OBJ";
	
    
	public static Dimension frameDimension;
	public static Color NONSHAPE_SHAPE_COLOR = Color.white;
	
	public static String unitsVolume = "<html><body>m<sup>3</sup><html><body>";
	public static String unitsGeneral = "<html><body>m<html><body>";
	public static String unitsSurfaceArea = "<html><body>m<sup>2</sup><html><body>";
	
	private static String startTags = "<html><body>";
	
	private static String endTags = "</body></html>";
	
	

	public static String forumla2dHtml = "<b>2D Formula Guide:</b><br>"+
	"<p><b>Square:</b> Surface Area (SA) = w*h" +
	"<p><b>Rectangle:</b> Surface Area (SA) = w*h/2" +
	"<p><b>Circle:</b> Surface Area (SA) = p*r^2" +
	"<br>" +
	"<br>Use ctrl+c and ctrl+v to copy and paste into Scratch Pad.";
	
	public static final String notation2DHelpMessage = startTags + forumla2dHtml + endTags;
	
		
		
		
		
	public static String forumla3dHtml = "<b>3D Formula Guide:</b><br>"+
	"<p><b>Sphere:</b>" +
	"<br>Volume (V) = (4/3)*pi*r^3" +
	"<br>Surface Area (SA) = 4*pi*r^2"+
	"<p><b>Cylinder:</b>" +
	"<br>Volume (V) = pi*r^2*height" +
	"<br>Surface Area (SA) = 2*pi*r^2+2*pi*r*height"+
	"<p><b>Rectangular Prism:</b>" +
	"<br>Volume (V) = length*width*height" +
	"<br>Surface Area (SA) = 2*(width*height + length*width + length*height)" +
	"<br>" +
	"<br>Use ctrl+c and ctrl+v to copy and paste into Scratch Pad.";
	
	
	public static final String notation3DHelpMessage = startTags + forumla3dHtml + endTags;
	
	
	public static String notationHtml = "<b>Notation Guide:</b><br>"+
	"<p><b>Operators:</b> 2*(h+2)+(1/2)" +
	"<p><b>Cube root:</b> cbrt(r+1)" +
	"<p><b>Square root:</b> sqrt(r+1)" +
	"<p><b>Power:</b> r^2" +
	"<br><br>" +
	"<i>Example: -5-6/(-2)^2 + sqrt(15+r)</i>" +
	"<br><p><b>Hint:</b> In order to use a variable such as <b>(r, w, h)</b> a shape must be selected.";

	
	public static String invalidExpressionErrorMessage = startTags +
														"Formula was invalid. Please check its notation.<br><br>" +
														notationHtml + endTags;
	
	public static String duplicate3DShapeError = startTags +
			"Only one shape of this type is allowed on the canvas at a time<br><br>" + endTags;
	
														
	public static String notationHelpMessage = startTags + notationHtml +
												endTags;
	
	public static String rootAddMessage = "<html>Enter number or expression for this root.<br><br>" +
			"							  <i>ie: 28, 4+pi, w+2</i></html>";
														
	public static String dragAndDropShapeTip = "Drag and Drop a shape on the canvas";
						
	
	public static List getShapeList() {
		InputStream resource = UIUtils.class.getResourceAsStream(JSON_CONFIG);
		
		try {
			String json = convertStreamToString(resource);
			
	        XStream xstream = new XStream(new JettisonMappedXmlDriver());
	        xstream.alias("MathToolbarShape",
					MathToolbarShape.class);	
	        xstream.alias("RectanglarPrismToolbarShape", RectanglarPrismToolbarShape.class);
	        xstream.alias("CylinderToolbarShape", CylinderToolbarShape.class);
	        xstream.alias("SphereToolbarShape", SphereToolbarShape.class);   
			xstream.alias("CircleToolbarShape", CircleToolbarShape.class);
			xstream.alias("TriangleToolbarShape", TriangleToolbarShape.class);
			xstream.alias("RectangleToolbarShape", RectangleToolbarShape.class);
			
	        return (List) xstream.fromXML(json);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String convertStreamToString(InputStream is) throws IOException {

		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];

			try {

				Reader reader = new BufferedReader(

				new InputStreamReader(is, "UTF-8"));

				int n;

				while ((n = reader.read(buffer)) != -1) {

					writer.write(buffer, 0, n);

				}

			} finally {

				is.close();

			}

			return writer.toString();

		} else {

			return "";

		}

	}
	
    private static String readFileAsString(File file)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(file));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
    

	public static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container) {
				compList.addAll(getAllComponents((Container) comp));
			}
		}
		return compList;
	}

	public static Component findComponentFromRoot(String name, Component c) {
		Container root = (Container) SwingUtilities.getRoot(c);

		if( root == null ) {
			return null;
		}
		List<Component> allComponents = UIUtils.getAllComponents(root);

		for (Component component : allComponents) {
			if (component.getName() != null && component.getName().equals(name)) {
				return component;
			}
		}

		return null;

	}
	
	public static Component findComponentAt(String name, Component root) {
		
		List<Component> allComponents = UIUtils.getAllComponents((Container) root);

		for (Component component : allComponents) {
			if (component.getName() != null && component.getName().equals(name)) {
				return component;
			}
		}

		return null;

	}
	

	public static void setModTitlePanel(JXTitledPanel panel) {
		panel.setBorder(new RoundedBorder(3));
		panel.setTitleFont(getTitleFont());
		panel.setTitleForeground(Colors.White.color());
		panel.setTitlePainter(getTitlePainter());
		panel.setBackgroundPainter(getSubPanelBackgroundPainter());
		panel.revalidate();
	}

	public static Font getTitleFont() {
		Font baseFont = UIManager.getFont("JXTitledPanel.titleFont");
		Font bigFont = new FontUIResource(baseFont.deriveFont(baseFont
				.getSize2D() * 1.3f));
		return bigFont;

	}

	public static Painter getTitlePainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.7f);
		Color color2 = Colors.Red.color(0.7f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1f }, new Color[] { color1,
						color2 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	public static Painter getSubPanelBackgroundPainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(1f);
		Color color2 = Colors.Black.color(0.5f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1f }, new Color[] { color1,
						color1 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	public static Painter getCalcBackgroundPainter() {
		int width = 250;
		int height = 100;
		Color color2 = Colors.White.color(1f);
		Color color1 = Colors.Gray.color(0.7f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 1.0f,
				width, height, new float[] { 0.3f, 1f }, new Color[] { color1,
						color2 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	public static Painter getSymbolButtonPainter() {

		MattePainter mp = new MattePainter(Colors.Black.alpha(0.9f));
		GlossPainter gp = new GlossPainter(Colors.White.alpha(0.3f),
				GlossPainter.GlossPosition.TOP);
		// return (new CompoundPainter(mp, gp));

		return mp;

	}

	public static Painter getNumButtonPainter() {

		MattePainter mp = new MattePainter(Colors.Blue.alpha(0.5f));
		GlossPainter gp = new GlossPainter(Colors.White.alpha(0.3f),
				GlossPainter.GlossPosition.TOP);
		// return (new CompoundPainter(mp, gp));

		return mp;

	}

	public static Painter getRootButtonPainter() {

		MattePainter mp = new MattePainter(Colors.Green.alpha(0.5f));
		GlossPainter gp = new GlossPainter(Colors.White.alpha(0.3f),
				GlossPainter.GlossPosition.TOP);
		// return (new CompoundPainter(mp, gp));

		return mp;

	}

	
	public static Painter getAdderButtonPainter() {

		MattePainter mp = new MattePainter(Colors.White.alpha(1f));
		GlossPainter gp = new GlossPainter(Colors.Black.alpha(0.1f),
				GlossPainter.GlossPosition.TOP);
		// return (new CompoundPainter(mp, gp));

		return mp;
	}

	public static Painter getEqualButtonPainter() {

		MattePainter mp = new MattePainter(Colors.Orange.alpha(0.5f));
		GlossPainter gp = new GlossPainter(Colors.Black.alpha(0.1f),
				GlossPainter.GlossPosition.TOP);
		// return (new CompoundPainter(mp, gp));
		return mp;
	}

	public static void showInformation(String message, String title) {
		JEditorPane editor = new JEditorPane();
		editor.setOpaque(false);
		editor.setContentType("text/html");
		editor.setEditable(false);
		editor.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		editor.setText(message);
		JXPanel p = new JXPanel();
		p.setOpaque(false);
		p.add(editor);
		
	
		
		JOptionPane.showMessageDialog(null, p, title, JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	public static boolean isWindows(){
		String os = System.getProperty("os.name").toLowerCase();
		//windows
	    return (os.indexOf( "win" ) >= 0); 
 
	}
 
	public static boolean isMac(){
		String os = System.getProperty("os.name").toLowerCase();
		//Mac
	    return (os.indexOf( "mac" ) >= 0); 
 
	}
 
	public static boolean isUnix(){
		String os = System.getProperty("os.name").toLowerCase();
		//linux or unix
	    return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);
 
	}

	
}
