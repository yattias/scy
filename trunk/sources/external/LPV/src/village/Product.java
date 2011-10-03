package village;

//import javax.swing.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
//import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Vector;


public class Product extends StateObject implements Comparable<Product>{

	 int width=30, height=30;
	private long hardX = 0; //x,y are coordinates of the top-left corner
	private long hardY = 0;
	long x=0, y=0;

	long newWidth= width, newHeight = height;
	int  name, mission=0;
	private int type=0;
	private boolean important=false;
	static public Rectangle activeClip;
	static public int savedType = 0, loadType = 0;
	public static int toolOpenedType = 0;
	public static int toolQuitType = 0;
	ELOProcess madeByProcess;
	public Vector<ELOProcess> usedByProcesses;
	LinkedList<Product> majorChildrenProducts;
	// state values: 0 - default 1 - mouse over 2 - active
	//picture
	
	public Product(int n){
		name=n;
		usedByProcesses = new Vector<ELOProcess>();
		majorChildrenProducts = new LinkedList<Product>();
	}
	
	public Product(int n, int ix, int iy){
		name=n;
		x=ix;
		y=iy;
		usedByProcesses = new Vector<ELOProcess>();
		majorChildrenProducts = new LinkedList<Product>();
	}
	
	Product(int n, int ix, int iy, int w, int h){
		name=n;
		x=ix;
		y=iy;
		width=w;
		height=h;
		usedByProcesses = new Vector<ELOProcess>();
		majorChildrenProducts = new LinkedList<Product>();
	}

	Product(int n, int ix, int iy, int w, int h, int m){
		name=n;
		x=ix;
		y=iy;
		width=w;
		height=h;
		usedByProcesses = new Vector<ELOProcess>();
		majorChildrenProducts = new LinkedList<Product>();
		mission = m;
	}


	
	public void setMission(int m){
		mission = m;
	}
	
	public int getMission(){
		return mission;
	}
	
	public int getAuthorID(){
		return madeByProcess.getAuthorID();
	}
	
	public int getMadeByName(){
		return madeByProcess.name;
	}
	
	public void draw(java.awt.Graphics gr){
		draw(gr,0);
	}
	
	public void drawAtPosition(Graphics gr, int drawMethod, long cornerX, long cornerY, int wdth, int hght){
		long xx,yy;
		int ww,hh;
		
		xx=x;
		yy=y;
		ww=width;
		hh=height;
		x=cornerX;
		y=cornerY;
		width = wdth;
		height = hght;
		draw(gr, drawMethod);
		x=xx;
		y=yy;
		width = ww;
		height = hh;
		
	}
	
	public void drawAtCenterPosition(Graphics gr, int drawMethod,
			long centerX, long centerY, int wdth, int hght) {
		long xx,yy;
		int ww,hh;
		
		xx=x;
		yy=y;
		ww=width;
		hh=height;
		setCenterPosition(centerX, centerY);
		width = wdth;
		height = hght;
		draw(gr, drawMethod);
		x=xx;
		y=yy;
		width = ww;
		height = hh;
		
	}
	public void draw(java.awt.Graphics gr,int drawMethod){
		if(activeClip.intersects(x, y, width, height)){
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

			case 10: //Metro
				drawShadowItem((Graphics2D)gr);
				break;

			default:
				drawCrude(gr);
				break;
			}
	
		}
	}
	
	private void drawCrude(java.awt.Graphics gr){
		if(name != -1){

		}
		
	}

	private void drawMetro(java.awt.Graphics gr){
		if(name != -1){
			Graphics2D gr2 = (Graphics2D)gr;
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
			drawBasicCircle(gr2);
		}
		
	}
	

	private void drawBasicCircle(Graphics2D gr2) {
		int brd = width/2;
		Shape circle = new Ellipse2D.Double(x, y, width, height);
		gr2.setStroke(new BasicStroke(brd));
		gr2.draw(circle);
		gr2.setColor(Color.white);
		gr2.fill(circle);
		gr2.setColor(Color.black);
		gr2.drawString(""+name, (int) (x+width/2-3-4*(Math.floor(Math.log10(Math.max(1,name))))), y+height/2+4);

		
	}

	private void drawFancyMetro(java.awt.Graphics gr){
		if(name != -1){
			Graphics2D gr2 = (Graphics2D)gr;
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
			drawFancyItem(gr2);
		}
		
	}

	
	private void drawShadowCircle(Graphics2D gr2){
	    Paint op = gr2.getPaint();
		float offsetX = -0.03f, offsetY = 0.075f;

	    if(isImportant()){
			float radius = width*0.53f;
			Point2D center = new Point2D.Float(x+width/2+offsetX*width, y+height/2+offsetY*height);
		    
		    float[] dist = {0.3f, 1.0f};
		    Color[] colors = {Color.black, new Color(0.5f, 0.5f, 0.5f, 0f)};

		    RadialGradientPaint p =
		 	         new RadialGradientPaint(center, radius, dist, colors);
		    Shape circle = new Ellipse2D.Double(center.getX()-radius, center.getY()-radius,2*(radius), 2*(radius));
		    gr2.setPaint(p);
			gr2.fill(circle);

	    }
	    else{
			float radius = newWidth*0.7f;
			Point2D center = new Point2D.Float(x+width/2+offsetX*width, y+height/2+offsetY*height);
		    
		    float[] dist = {0.3f, 1.0f};
		    Color[] colors = {Color.black, new Color(0.5f, 0.5f, 0.5f, 0f)};

		    RadialGradientPaint p =
		 	         new RadialGradientPaint(center, radius, dist, colors);
		    Shape circle = new Ellipse2D.Double(center.getX()-radius, center.getY()-radius,2*(radius), 2*(radius));
		    gr2.setPaint(p);
			gr2.fill(circle);

	    }

		gr2.setPaint(op);			
	}

	private void drawShadowShape(Graphics2D gr2, Shape s){
		Color oldColor = gr2.getColor();
		Stroke oldStroke = gr2.getStroke();
		float wdth = 10f;
		float detail =6;
		float stepsize = wdth/detail;
		for(float i=0;i<=detail;i++){
			float p = Math.min(1, (detail-i)/detail);
			gr2.setColor(new Color(1*p, 1*p, 1*p,0.2f));
			gr2.setStroke(new BasicStroke(stepsize*(detail-i)));
			gr2.draw(s);
		}
		gr2.setColor(oldColor);
		gr2.setStroke(oldStroke);
	}
	

	
	private void drawShadowItem(Graphics2D gr2){
		
		double dx = -2, dy =2, rw=5, rh=5;

		if(type == toolQuitType){
			int brd = (int)(width-newWidth)/2;
			//gr2.setPaint(color);
		    Shape rect = new RoundRectangle2D.Double(x+brd+dx, y+brd+dy, width-2*brd, height-2*brd,rw,rh);
			//Shape rect = new Rectangle2D.Double(x+brd+dx, y+brd+dy, width-2*brd, height-2*brd);
		    drawShadowShape(gr2, rect);
		}
		if(type == savedType){
	    	int brd = width/20;
			//gr2.setPaint(color);
		    Shape rect = new RoundRectangle2D.Double(x+brd+dx, y+brd+dy, width-2*brd, height-2*brd,rw,rh);
		    drawShadowShape(gr2, rect);
	    }
		if(type == loadType){
			drawShadowCircle(gr2);
			return;
		}
		if(type == toolOpenedType){
			drawShadowCircle(gr2);
			return;
		}
	}

	private void drawFancyItem(Graphics2D gr2) {
		if(type == loadType){
			drawFancyCircle(gr2);
			return;
		}
		if(type == savedType){
			drawFancySquare(gr2);
			return;
		}
		if(type == toolOpenedType){
			drawSmallFancyCircle(gr2);
			return;
		}
		if(type == toolQuitType){
			drawSmallFancySquare(gr2);
			return;
		}
		drawDash(gr2);
		
	}
	
	private void drawFancyCircle(Graphics2D gr2) {
		int brd = width/20;
		Color color = gr2.getColor();
	    Shape circle = new Ellipse2D.Double(x+brd, y+brd, width-2*brd, height-2*brd);
		gr2.setColor(Color.white);
		gr2.fill(circle);
		gr2.setColor(color);
		gr2.setStroke(new BasicStroke(brd));
		gr2.draw(circle);
		int brd2 = brd*2;
		int brd3 = brd;
		gr2.setStroke(new BasicStroke(brd2));
		circle = new Ellipse2D.Double(x+brd+brd2+brd3, y+brd+brd2+brd3, width-2*(+brd+brd2+brd3), height-2*(+brd+brd2+brd3));
		gr2.draw(circle);
		gr2.setColor(Color.black);
	}
	private void drawFancySquare(Graphics2D gr2) {
		int brd = width/20;
		//gr2.setPaint(color);
		Color color = gr2.getColor();
	    Rectangle2D rect = new Rectangle2D.Double(x+brd, y+brd, width-2*brd, height-2*brd);
		gr2.setColor(Color.white);
		gr2.fill(rect);
		gr2.setColor(color);
		gr2.setStroke(new BasicStroke(brd));
		gr2.draw(rect);
		int brd2 = brd*2;
		int brd3 = brd;
		gr2.setStroke(new BasicStroke(brd2));
		rect = new Rectangle2D.Double(x+brd+brd2+brd3, y+brd+brd2+brd3, width-2*(+brd+brd2+brd3), height-2*(+brd+brd2+brd3));
		gr2.draw(rect);
		gr2.setColor(Color.black);
		//	gr2.drawString(""+name, (int) (x+width/2-3-4*(Math.floor(Math.log10(Math.max(1,name))))), y+height/2+4);
	}

	private void drawSmallFancySquare(Graphics2D gr2){
		int brd = (int)(width-newWidth)/2;
		//gr2.setPaint(color);
		Color color = gr2.getColor();
	    Shape rect = new Rectangle2D.Double(x+brd, y+brd, width-2*brd, height-2*brd);
		gr2.setColor(Color.white);
		gr2.fill(rect);
		gr2.setColor(color);
		gr2.setStroke(new BasicStroke(brd/4));
		gr2.draw(rect);
		gr2.setColor(Color.black);
	//	gr2.drawString(""+name, (int) (x+width/2-3-4*(Math.floor(Math.log10(Math.max(1,name))))), y+height/2+4);

	}
	
	private void drawSmallFancyCircle(Graphics2D gr2){
		int brd = (int)(width-newWidth)/2;
		//gr2.setPaint(color);
		Color color = gr2.getColor();
	    Shape circle = new Ellipse2D.Double(x+brd, y+brd, width-2*brd, height-2*brd);
		gr2.setColor(Color.white);
		gr2.fill(circle);
		gr2.setColor(color);
		gr2.setStroke(new BasicStroke(brd/4));
		gr2.draw(circle);
		gr2.setColor(Color.black);
	//	gr2.drawString(""+name, (int) (x+width/2-3-4*(Math.floor(Math.log10(Math.max(1,name))))), y+height/2+4);

	}
	
	private void drawDash(Graphics2D gr2){
		Color color = gr2.getColor();
	    Rectangle2D rect = new Rectangle2D.Double(getCornerX(), getCornerY(),0.7*width, height);
	    gr2.fill(rect);
		gr2.setColor(color);

	}
	
	public void setCenterPosition(long ix, long iy){
		setCenterX(ix);
		setCenterY(iy);
	}
	
	public void setCornerPosition(long ix, long iy){
		
		x=ix;
		y=iy;
	}
	
	public long getCornerX(){
		return x;
	}
	
	public long getCornerY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getName(){
		return name;
	}
	
	public void addMajorChildrenProduct(Product p){
		majorChildrenProducts.add(p);
	}
	
	public void addMadeByProcess(ELOProcess pr){
		if( madeByProcess == null || madeByProcess.name == -1 ){ //Caution, if madeByProcess == null, madeByProcess.name can throw NULL exception
			madeByProcess = pr;
		}
		else 
			System.out.println("Trying to create multiple parent processes, old:"+madeByProcess.name+" new:"+pr.name);
	}
	
	public void addUsedByProcess(ELOProcess pr){
		usedByProcesses.add(pr);
	}

	public boolean UsedByProcessContainsAsResult(Product prod){
		for(int i = usedByProcesses.size()-1; i>-1 ;i--){
			if(usedByProcesses.get(i).to == prod){
				return true;
			}
		}
		return false;
		
	}
	
	public void removeUsedByProcess(ELOProcess pr){
		usedByProcesses.remove(pr);
	}
	
	public void setPicture(Image i){
		
	}

	public long getTime() {
		
		return madeByProcess.time;
	}

	void setType(int type) {
		this.type = type;
		if(type!=toolOpenedType && type!=toolQuitType && type!=savedType && type!=loadType) {
			width = 5;
			newWidth=width;
			height = 30;
			newHeight = height;
		}
		
	}

	public int getType() {
		return type;
	}

	void setImportant(boolean important) {
		this.important = important;
	/*	if(!important){
			newWidth=width/2;
			newHeight=height/2;
		}
	*/	
	}

	boolean isImportant() {
		return important;
	}

	boolean isAtPosition(long ax, long ay){
		long diff = (width-newWidth)/2;
		if(x+diff<=ax && x+diff+newWidth>=ax && y+diff<=ay && y+diff+newHeight>=ay){
			return true;	
		}else
			return false;
		
	}

	public void setCenterX(long centerX) {
		this.x = centerX-width/2;
	}
	
	public void setHardCenterX(long centerX) {
		this.x = centerX-width/2;
		hardX = x;
	}

	public long getCenterX() {
		return (x+width/2);
	}
	
	public long getHardCenterX() {
		return (hardX+width/2);
	}

	public void setCenterY(long centerY) {
		this.y = centerY-height/2;
	}
	
	public void setHardCenterY(long centerY) {
		this.y = centerY-height/2;
		hardY = y;
	}

	public long getCenterY() {
		return (y+height/2);
	}
	
	public long getHardCenterY() {
		return (hardY+height/2);
	}

	@Override
	public int compareTo(Product o) {
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

	public void setHardX(long hardX) {
		this.hardX = hardX;
	}

	public long getHardX() {
		return hardX;
	}

	public void setHardY(long hardY) {
		this.hardY = hardY;
	}

	public long getHardY() {
		return hardY;
	}

	public void setHardCenterPosition(long ix, long iy) {
		
			setHardCenterX(ix);
			setHardCenterY(iy);
			//x=ix;
			//y=iy;
		
	}
	




}
