package village;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import java.awt.RenderingHints;

//import java.awt.RenderingHints;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class CityTimeScale {
	public final static int DRAWMETHOD_CRUDE = 0;
	public final static int DRAWMETHOD_METRO = 1;
	public final static int DRAWMETHOD_FANCY = 2;
	static String BGSOURCE = "../bergen-78white.png";
	
	BufferedImage background;
	
	private static double timeScale; //number of milliseconds per pixel
	static double unit; //number of milliseconds per unit on meter (from 100 to 200)
	
	private long timeStart=0;
	private long timeEnd=0;
	static long height;
	long width;
	double zeroX=0;
	double zeroY=0;
	private long hardStart;
	private long hardEnd;
	int drawMethod;
	boolean redraw = true;
	
	public CityTimeScale(){
		redraw = true;
		calculateUnit();
	}
	
	
	private void calculateUnit() {
		//System.out.println("changing unit bacause unit/timeScale="+unit/timeScale);
		if(unit/timeScale<100){
			unit=200*timeScale;
		}
		else{
			if(unit/timeScale>200){	
				unit=100*timeScale;
			}
		}

	}

	private String timeToString(long t){
		Calendar C = Calendar.getInstance();
		C.setTimeInMillis(t);
		C.setTimeZone(TimeZone.getDefault());
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(C.getTime());
	}

	public void draw(Graphics g) {
		//System.out.println(width+" "+height+" "+zeroX+" "+zeroY);
		if(timeEnd-timeStart>0){
			Graphics2D gr2 =(Graphics2D) g;
			gr2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			gr2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gr2.translate(zeroX, zeroY);
			RoundRectangle2D bgRect = new RoundRectangle2D.Double(5, height-45, width-10, 35, 10, 10);
			
			Composite composite = gr2.getComposite();// 0.5f is alphaAlphaComposite 
			Composite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
			gr2.setComposite(alphaComposite);
			
			gr2.setColor(Color.WHITE);
			gr2.fill(bgRect);
			gr2.setColor(Color.lightGray);
			gr2.draw(bgRect);
			Rectangle2D clip = new Rectangle2D.Double(5, height-45, width-10, height);
			Shape oldClip = gr2.getClip();
			gr2.setClip(clip);
			gr2.fillRect(0, 0, 100, 100);
			
			
			double minisegs = 10;
			double us = unit/timeScale;
			//System.out.println((int)( us)+" unit"+unit+" timeScale"+timeScale);
			double offset =( -(timeStart%unit))/timeScale;
			//System.out.println( -(double)(timeStart)%unit+" "+timeStart+" "+timeEnd);
			for(double i = 0; i<=1+width/unit*timeScale;i++){
				gr2.setColor(Color.gray);
				gr2.fill(new Rectangle2D.Double( offset+i*us, height-40,2,12));
				for(double j = 1; j< minisegs;j++){
					if(j%5 == 0) {
						gr2.fill(new Rectangle2D.Double( offset+(i+j/minisegs)*us, height-40,2,9));
					}
					else gr2.fill(new Rectangle2D.Double( offset+(i+j/minisegs)*us, height-40,2,5));
				}
				Font of = gr2.getFont();
		        Font font = new Font("SansSerif", Font.PLAIN, 10);
		        String label = timeToString(Math.round(timeStart+offset*timeScale+i*unit));
		        gr2.setFont(font);
		        FontMetrics metrics = gr2.getFontMetrics(font);
		        int fh = metrics.getHeight();
		        int fw = metrics.stringWidth(label);
		        gr2.setColor(Color.black);
		        gr2.drawString(label, (int)(offset+i*us-fw/2-5), height-40+fh+9);
		        gr2.setFont(of);
		        	
			}	
			
			gr2.setClip(oldClip);
			gr2.translate(-zeroX, -zeroY);
			gr2.setComposite(composite);
			
		}
	}
	
	
	
	public void setWidth(long w){
			width=w;
	}
	
	public void setHeight(long h){
			height=h;
	}
	

	

	
	public void setTimeScale() {
		//this.timeScale = timeScale;
		if(width>1){
			timeScale = (timeEnd-timeStart)/width;
		}else
			timeScale = 0;
		if(unit/timeScale<100 || unit/timeScale > 200) calculateUnit();
	}


	public double getTimeScale() {
		return timeScale;
	}


	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}


	public double getTimeStart() {
		return timeStart;
	}


	public void setTimeEnd(long timeEnd) {
		this.timeEnd = timeEnd;
	}


	public double getTimeEnd() {
		return timeEnd;
	}


	public void setStart(double start) {
		zeroX = start;
		
	}
	
	public double getStart(){ //onscreen position
		return zeroX;
	}
	
	public double getEnd(){
		return zeroX + width;
	}
	


	public void setEnd(double end) {
		setWidth((long)(end - zeroX));
		
	}


	public void setHardEnd(long hardEnd) {
		this.hardEnd = hardEnd;
	}


	public long getHardEnd() {
		return hardEnd;
	}


	public void setHardStart(long hardStart) {
		this.hardStart = hardStart;
	}


	public long getHardStart() {
		return hardStart;
	}

}
