package village;



//import javax.swing.*;

//import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
//import java.awt.Composite;
import java.awt.GradientPaint;
//import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
//import java.awt.Polygon;
//import java.awt.RadialGradientPaint;
import java.awt.Shape;
//import java.awt.Polygon;
//import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


public class ELOProcess extends StateObject implements Comparable<ELOProcess>{
	
	static public Rectangle activeClip;
	static int width = 24;
	int actor,name; //when was performed and by whom and name of the process
	long time;
	Product from,to; //from can be null
	Line2D link = new Line2D.Double();
	// state values: 0 - default 1 - mouse over 2 - active
	
	public ELOProcess(Product ifrom, Product ito, int iname, int iactor, long time2){
		from = ifrom;
		to = ito;
		name = iname;
		actor = iactor;
		time = time2;
		
	}
	
	public void update(){
		link.setLine(from.getCenterX(), from.getCenterY(), to.getCenterX(), to.getCenterY());
	}
	
	public void draw(java.awt.Graphics gr){
		draw(gr,0);
		
	}
	
	public int getName(){
		return name;
	}
	
	public Product getFrom(){
		return from;
	}
	
	public Product getTo(){
		return to;
	}
	
	public int getFatherName(){
		return from.name;
	}
	
	public int getAuthorID(){
		return actor;
	}
	
	public long getCenterX(){
		return (long)Math.floor((link.getX1()+link.getX2())/2);
	}
	
	public long getCenterY(){
		return (long)Math.floor((link.getY1()+link.getY2())/2);
	}
	
	public void drawAtPosition(Graphics gr, int drawMethod, Line2D newLink, int wdth){
		Line2D oldLink = link;
		link = newLink;
		int ww=width;
		width = wdth;
		draw(gr, drawMethod);
		width = ww;
		link = oldLink;
		
	}
	
	public void draw(java.awt.Graphics gr,int drawMethod){
		if(link.intersects(activeClip)) {
			switch (drawMethod) {
			case 0: //Crude
				drawCrude(gr);
				break;
			case 1: //Metro
				drawMetro(gr);
				break;
			case 2: //FancyMetro
				drawFancyMetro(gr);
				break;

			default:
				drawCrude(gr);
				break;
			}
			
		}
	}
	
	public void drawCrude(java.awt.Graphics gr){
		gr.setColor(Color.lightGray);

		if(from.name > -1 && to.name > -1){
			
	/*old*/		//gr.drawLine(from.x, from.y, to.x, to.y);

			Graphics2D gr2 = (Graphics2D)gr;
			Line2D line = new Line2D.Double(from.x, from.y, to.x, to.y);
	         gr2.setColor(Color.darkGray);
	         gr2.setStroke(new BasicStroke(10));
	         gr2.draw(line);
	         
			BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4, new float[] {3,4}, 0);
			line = new Line2D.Double(from.x, from.y, to.x, to.y);
	        gr2.setColor(Color.lightGray);
	        gr2.setStroke(stroke);
	        gr2.draw(line);
			
	     // TODO		gr.drawString(""+name, (from.x+to.x)/2, (from.y+to.y)/2-2);
		}
		else if(from.name == -1 && to.name > -1){
			//gr.drawLine(to.x+20, to.y+20, to.x, to.y);
		}
		else System.out.println("Cannot draw edge with empty end");
	}

	public void drawMetro(java.awt.Graphics gr){
		gr.setColor(Color.lightGray);

		if(from.name > -1 && to.name > -1){
			
	/*old*/		//gr.drawLine(from.x, from.y, to.x, to.y);

			Graphics2D gr2 = (Graphics2D)gr;
			 //Line2D line = new Line2D.Double(from.x, from.y, to.x, to.y);
			switch (state) {
			case 0: //normal
				gr2.setColor(Color.orange);
		        gr2.setStroke(new BasicStroke(from.width*4/10));
		        gr2.draw(link);
				break;
			case 1: //hover
				gr2.setColor(Color.yellow);
		        gr2.setStroke(new BasicStroke(from.width*4/10));
		        gr2.draw(link);
				break;
			case 2: //active
				gr2.setColor(Color.getHSBColor((float)0.1,(float) 0.8,(float) 0.7));
		        gr2.setStroke(new BasicStroke(from.width*4/10));
		        gr2.draw(link);
				break;
			default:
				gr2.setColor(Color.orange);
		        gr2.setStroke(new BasicStroke(from.width*4/10));
		        gr2.draw(link);
				break;
			} 
			//gr.drawString(""+name, (from.x+to.x)/2, (from.y+to.y)/2-2);
		}
		else if(from.name == -1 && to.name > -1){
			//gr.drawLine(to.x+20, to.y+20, to.x, to.y);
		}
		else System.out.println("Cannot draw edge with empty end");
	}

	public void drawFancyMetro(java.awt.Graphics gr){
		gr.setColor(Color.lightGray);

		if(from.name > -1 && to.name > -1){
			
	/*old*/		//gr.drawLine(from.x, from.y, to.x, to.y);

			Graphics2D gr2 = (Graphics2D)gr;
			 //Line2D line = new Line2D.Double(from.x, from.y, to.x, to.y);
			switch (state) {
			case 0: //normal
				gr2.setColor(Color.orange);
				break;
			case 1: //hover
				gr2.setColor(Color.yellow);
				break;
			case 2: //active
				gr2.setColor(Color.getHSBColor((float)0.1,(float) 0.8,(float) 0.7));
				break;
			default:
				gr2.setColor(Color.orange);
				break;
			} 
			BasicStroke stroke = new BasicStroke(width*20/100, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
	        gr2.setStroke(stroke);
	        //drawShadowLink(gr2);
	        gr2.draw(link);

			//gr.drawString(""+name, (from.x+to.x)/2, (from.y+to.y)/2-2);
		}
		else if(from.name == -1 && to.name > -1){
			//gr.drawLine(to.x+20, to.y+20, to.x, to.y);
		}
		else System.out.println("Cannot draw an edge with empty end");
	}

	
	public void drawShadowLink(Graphics2D gr) {
		double scalar = 0.09*1;
	    Point2D offset = new Point2D.Double(-3, 5);
	    //Point2D start = new Point2D.Double(offset.getX()+(link.getX1()+link.getX2())/2,offset.getY()+(link.getY1()+link.getY2())/2);
	    Point2D start = new Point2D.Double((link.getX1()+link.getX2())/2,(link.getY1()+link.getY2())/2);
	    Point2D end = new Point2D.Double(start.getX()+scalar*(link.getY2()-link.getY1()),start.getY()+scalar*(link.getX1()-link.getX2()));
	    GradientPaint gp = new GradientPaint(start, Color.gray,end, new Color(1f, 1f, 1f, 0f), true);
	    Paint op = gr.getPaint();
	    gr.setPaint(gp);
//	    Composite compo=gr.getComposite();
//	    gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
//		int[] polyX = {(int)Math.round(link.getX1()),(int)Math.round(link.getX1()+scalar*(link.getY2()-link.getY1())),(int)Math.round(link.getX2()+scalar*(link.getY2()-link.getY1())),(int)Math.round(link.getX2())};
//		int[] polyY = {(int)Math.round(link.getY1()),(int)Math.round(link.getY1()+scalar*(link.getX1()-link.getX2())),(int)Math.round(link.getY2()+scalar*(link.getX1()-link.getX2())),(int)Math.round(link.getY2())};
//	    int[] kk = {0,100,100,0};
//	    int[] ff = {0,0,100,100};
//	    Shape polygon = new Polygon(polyX,polyY, 4);
	    Shape line = new Line2D.Double(link.getX1()+offset.getX(), link.getY1()+offset.getY(), link.getX2()+offset.getX(), link.getY2()+offset.getY());
		//gr.fill(polygon);
		gr.draw(line);
		gr.setPaint(op);
//		gr.setComposite(compo);

		
	}

	public long getTime() {
		return time;
	}
	
	@Override
	public int compareTo(ELOProcess o) {
		if(o.getTime() == getTime()){
			return 0;
		}else{
			if(o.getTime() > getTime()){
				return -1;
			}else{
				return 1;
			}
		}
	}

	public boolean isOriginal() {
		if(getFatherName() == -1) return true;
		if(getTo().getType() == Product.loadType){
			//return true;
			return false;
		}else
			return false;
		/*
		if(getFatherName() == -1) return true;
		//if(to.usedByProcesses.isEmpty()) return true;
		if(getFrom().getMadeByName() != getName()){
			return true;
		
		}
		else
			return false;
			*/
	}
}
