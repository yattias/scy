package village;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
//import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CityBackground {
	public final static int DRAWMETHOD_CRUDE = 0;
	public final static int DRAWMETHOD_METRO = 1;
	public final static int DRAWMETHOD_FANCY = 2;
	static String BGSOURCE = "../bergen-78white.png";
	
	BufferedImage background;
	
	double scale;
	double zeroX,zeroY;
	int width,height;
	double zeroZeroX=1800;
	double zeroZeroY=1800;
	int drawMethod;
	boolean redraw = true;
	
	public CityBackground(int mode){
		redraw = true;
		zeroX=zeroZeroX;
		zeroY=zeroZeroY;
		scale =1 ;
		drawMethod = mode;
		if(mode == DRAWMETHOD_METRO){
			try {
			    background = ImageIO.read(new File(BGSOURCE));
			} catch (IOException e) {
				System.out.println("error reading background picture");
			}
		}
	}
	
	public void draw(Graphics g) {
		switch (drawMethod) {
		case DRAWMETHOD_CRUDE:
			drawCrude(g);
			break;
		case DRAWMETHOD_METRO:
			drawMetro(g);
			break;
		case DRAWMETHOD_FANCY:
			drawFancy(g);
			break;

		default:
			break;
		}
		
	}
	
	public void drawMetro(Graphics g) {
		//System.out.println(width+" "+height+" "+zeroX+" "+zeroY);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		gr.drawImage(background, 0, 0, width, height, (int)zeroX, (int)zeroY, (int)Math.round((zeroX+width)/scale), (int)Math.round((zeroY+height)/scale), null);
		
		gr.setColor(Color.GRAY);
		gr.drawString("background © OpenStreetMap contributors, CC-BY-SA", 10, 15);
		
	}
	
	public void drawCrude(Graphics g) {
		//System.out.println(width+" "+height+" "+zeroX+" "+zeroY);
	}
	
	public void drawFancy(Graphics g) {
		//System.out.println(width+" "+height+" "+zeroX+" "+zeroY);
		Graphics2D gr2 = (Graphics2D)g;
		if(redraw){
			gr2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gr2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

			int patternWidth = 4;
			int patternHeight = 4;
			BufferedImage pattern;
			Shape bgRect = new Rectangle(width, height);
			pattern = new BufferedImage(patternWidth, patternHeight, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D gpat = pattern.createGraphics();
			gpat.setColor(Color.green);
//			gpat.setColor(Color.getHSBColor(0.6f, 0.1f, 1f));
			gpat.draw(new Line2D.Double(0,patternHeight/2,patternWidth/2,0));
			gpat.draw(new Line2D.Double(patternWidth/2,patternHeight,patternWidth,patternHeight/2));
//			gpat.draw(new Line2D.Double(0,0,patternWidth,0));
//			gpat.draw(new Line2D.Double(patternWidth/2,patternHeight,patternWidth,patternHeight/2));
			
			if(width<1) width =1;
			if(height<1) height = 1;
			background = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D gb = background.createGraphics();
			gb.setPaint(new TexturePaint(pattern, new Rectangle2D.Double(0, 0, patternWidth, patternHeight)));
			gb.fill(bgRect);
			/*Paint op = gr2.getPaint();
			gr2.setPaint(new TexturePaint(pattern, new Rectangle2D.Double(0, 0, patternWidth, patternHeight)));
			gr2.fill(bgRect);
			gr2.setPaint(op);
			*/	
			redraw = false;
		}
		gr2.drawImage(background, 0, 0, null);
		
	}
	
	public void setZeroX(double zx){
		zeroX=zx+zeroZeroX;
	}
	
	public void setZeroY(double zeroY2){
		zeroY=zeroY2+zeroZeroY;
	}
	
	public void setWidth(int w){
		if(width != w){
			width=w;
			redraw = true;
		}
		
	}
	
	public void setHeight(int h){
		if(height != h){
			height=h;
			redraw = true;
		}
	}
	
	public double getZeroX(){
		return zeroX;
	}
	
	public double getZeroY(){
		return zeroY;
	}
	
	public void setScale(double s){
		scale = s;
	}
	
	public void zoom(int amount){
		scale = scale + 0.2*amount;
		if (scale <0.2) 
			scale = 0.2;
		if (scale > 2)
			scale = 2;
	}
	
	public void zoom(int amount, int x, int y) {
		double newScale = scale + 0.2*amount;
		if (newScale <0.2) 
			newScale = 0.2;
		zeroX = (int) Math.round( newScale/scale*(zeroX+x) - x);
		zeroY = (int) Math.round( newScale/scale*(zeroY+y) - y);
		//zeroX = (int) Math.round( x + newScale/scale*(zeroX-x) );
		//zeroY = (int) Math.round( y + newScale/scale*(zeroY-y) );
		scale = newScale;
	}
}
