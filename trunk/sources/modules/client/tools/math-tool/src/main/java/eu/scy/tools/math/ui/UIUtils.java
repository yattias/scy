package eu.scy.tools.math.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LinearGradientPaint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import eu.scy.tools.math.ui.paint.Colors;
import eu.scy.tools.math.ui.paint.RoundedBorder;

public class UIUtils {

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
	public static final String SPHERE3D = "SPHERE3D";
	public static final String CYLINDER3D = "CYLINDER3D";
	public static final Color ERROR_SHAPE_COLOR = Color.red;
	public static final String TYPE = "TYPE";
	public final static String SHAPE = "SHAPE";

	
	public final static Color SHAPE_3D_DASH_BORDER_COLOR = Color.BLACK;
	public final static String METERS = "m";
	public static final String SHAPE_ID = "SHAPE_ID";
    
	public static Dimension frameDimension;
	public static Color NONSHAPE_SHAPE_COLOR = Color.white;
	
	private static String startTags = "<html><body>";
	
	private static String endTags = "</body></html>";
	
	public static String notationHtml = "<b>Notation Guide:</b><br>"+
	"<br>" +
	"<p>operators: 2*(h+2)+(1/2)" +
	"<p>cube root: cbrt(r+1)" +
	"<p>square root: sqrt(r+1)" +
	"<p>powers : r^2" +
	"<br><br>" +
	"<i>example: -5-6/(-2)^2 + sqrt(15+r)</i>" ;

	
	public static String invalidExpressionErrorMessage = startTags +
														"Formula was invaild check its notation.<br><br>" +
														notationHtml + endTags;
														
	public static String notationHelpMessage = startTags + notationHtml +
												endTags;
	
	public static String rootAddMessage = "<html>Enter number or expression for this root.<br><br>" +
			"							  <i>ie: 28, 4+pi, w+2</i></html>";
														
	public static String dragAndDropShapeTip = "Drag and Drop a shape on the canvas";
										

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

}
