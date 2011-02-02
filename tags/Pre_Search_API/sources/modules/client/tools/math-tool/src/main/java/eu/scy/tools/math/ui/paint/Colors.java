package eu.scy.tools.math.ui.paint;

import java.awt.Color;
import java.awt.LinearGradientPaint;

import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

/**
 * Colors is an enumeration class that makes it easier to work with colors. Methods are provided for
 * conversion to hex strings, and for getting alpha channel colors.
 */
public enum Colors {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// various colors in the pallete
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  Pink(255, 175, 175),
  Green(159, 205, 20),
  Orange(213, 113, 13),
  Yellow(Color.yellow),
  Red(189, 67, 67),
  LightBlue(208, 223, 245),
  Blue(Color.blue),
  Black(0, 0, 0),
  White(255, 255, 255),
  Gray(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue());

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructors
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

Colors(Color c) {
  _myColor = c;
}

Colors(int r, int g, int b) {
  _myColor = new Color(r, g, b);
}

Colors(int r, int g, int b, int alpha) {
  _myColor = new Color(r, g, b, alpha);
}

Colors(float r, float g, float b, float alpha) {
  _myColor = new Color(r, g, b, alpha);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

private Color _myColor;

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public Color alpha(float t) {
  return new Color(_myColor.getRed(), _myColor.getGreen(), _myColor.getBlue(), (int) (t * 255f));
}

public static Color alpha(Color c, float t) {
  return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (t * 255f));
}

public Color color() { return _myColor; }

public Color color(float f) {
  return alpha(f);
}

public String toString() {
  StringBuilder sb = new StringBuilder();
  sb.append("r=")
      .append(_myColor.getRed())
      .append(", g=")
      .append(_myColor.getGreen())
      .append(", b=")
      .append(_myColor.getBlue())
      .append("\n");
  return sb.toString();
}

public String toHexString() {
  Color c = _myColor;
  StringBuilder sb = new StringBuilder();
  sb.append("#");
  sb.append(Integer.toHexString(_myColor.getRed()));
  sb.append(Integer.toHexString(_myColor.getGreen()));
  sb.append(Integer.toHexString(_myColor.getBlue()));
  return sb.toString();
}

public static Painter getHighlightOffPainter() {
	int width = 100;
	int height = 100;
	Color color1 = Colors.White.color(0.2f);
	Color color2 = Colors.Black.color(0.8f);
	Color color3 = Colors.Gray.color(0.0f);

	LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
			width, height, new float[] { 0.0f, 1.0f }, new Color[] {
			color3, color3 });
	MattePainter mattePainter = new MattePainter(gradientPaint);
	return mattePainter;
}


public static Painter getMessageBGPainter() {
	int width = 100;
	int height = 100;

	Color color3 = Colors.Gray.color(0.1f);

	LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
			width, height, new float[] { 0.0f, 1.0f }, new Color[] {
			color3, color3 });
	MattePainter mattePainter = new MattePainter(gradientPaint);
	return mattePainter;
}

public static Painter getHighlightOnPainterBuddy() {
	int width = 600;
	int height = 100;
	Color color1 = Colors.White.color(0.5f);
	Color color2 = Colors.Yellow.color(1.0f);

	LinearGradientPaint gradientPaint = new LinearGradientPaint(width, height,
			0.0f, height, new float[] { 0.0f, .5f }, new Color[] {
					color2, color2 });
	
//	LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, height,
//			width, height, new float[] { 0.0f, .6f }, new Color[] {
//					color1, color2 });
	MattePainter mattePainter = new MattePainter(gradientPaint);
	return mattePainter;
}


public static Painter getHighlightOnPainter() {
	int width = 600;
	int height = 100;
	Color color1 = Colors.White.color(0.5f);
	Color color2 = Colors.Yellow.color(0.4f);

	LinearGradientPaint gradientPaint = new LinearGradientPaint(width, height,
			0.0f, height, new float[] { 0.0f, .5f }, new Color[] {
					color1, color2 });
	
//	LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, height,
//			width, height, new float[] { 0.0f, .6f }, new Color[] {
//					color1, color2 });
	MattePainter mattePainter = new MattePainter(gradientPaint);
	return mattePainter;
}

}//end enum Colors
