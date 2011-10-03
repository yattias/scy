import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;

//import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.LinkedList;

import java.util.TimeZone;
//import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import village.City;
import village.CityBackground;
import village.CityTimeScale;
import village.ELOProcess;
import village.Product;

import village.StateObject;

class MyPanel extends JPanel 
implements ActionListener, MouseInputListener, MouseWheelListener, HierarchyBoundsListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1789551003424858306L;
    private village.City city;
    private CityBackground background;
    private LinkedList<CityTimeScale> timeScales;
    private StateObject hover;
    private MyContentBubble bubble;
    private LinkedList<MyContentBubble> bubbles;
    private int landmarkX = 0 ,landmarkY = 0;
    private double zeroX, zeroY;
    private double scaleX = 1;
    private int hoverOldState=0;
    
    static final double scaleYmin = 0.1;
    static final double scaleYmax = 3;
    static final double scaleYstart = 0.5;

    
    public MyPanel(village.City c) {
    	city = c;
    	scaleX = 1;
    	background = new CityBackground(CityBackground.DRAWMETHOD_FANCY);
    	//timeScales = new ArrayList<CityTimeScale>();
    	updateScales();
    	
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.white);
        // hover, active = new ...
        //setPreferredSize(new Dimension(city.width,city.heigth));
       /* addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
            }
        });
	
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
            }
        });*/
        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
        addHierarchyBoundsListener(this);
        
        setLayout(null);
        bubble = new MyContentBubble(300,300,250,150); 
        add(bubble);
        bubble.setVisible(false);
        bubbles = new LinkedList<MyContentBubble>();
        
        //bubble.setLocation(70, 110);
        //bubble.setBounds(70, 110, 100, 100);
        //bubble.setVisible(true);
        //((Graphics2D)this.getGraphics()).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //((Graphics2D)this.getGraphics()).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        init();
    }
    
   private void init(){
	   city.updateJumps();
	   city.UpdateJumpedCoordinates();
   }
    
   public void renew(){
	   scaleX = 1;
	   
	   zeroX=city.getZeroX();
	   zeroY = city.getZeroY();
	   for(int i = 0 ; i<bubbles.size();i++){
		   bubbles.get(0).setVisible(false);
		   remove(bubbles.get(i));
		   bubbles.remove();
	   }
	 city.setWidth(Math.max(city.width,getWidth()));
	 scaleX = getWidth()/city.totalWidth;
	 city.setScale(scaleX);
	 scaleX = getWidth()/(double)city.hardToSoftX((long)city.totalWidth);
	 zeroX = - 90-City.labelWidth;
	 city.setZeroX(zeroX);
	 //System.out.println(city.totalWidth/(double)city.hardToSoftX((long)city.totalWidth));
	 city.setScale(scaleX);
	 city.setHeight(Math.max(city.height,getHeight()));
	 timeScales = new LinkedList<CityTimeScale>();
	updateScales();
	 
   }
    

    private void updateScales() {
    /*	for(int i = 0; i< timeScales.size(); i++){
    		//modify
    		
    		//remove
    		if(timeScales.get(i).getStart()>getWidth() || timeScales.get(i).getEnd()<0){
    			timeScales.remove(i);
    			i--;
    		}
    	}
    */	//add
    	ArrayList<ArrayList<Double>> sj = city.getScreenJumpSequences();
    	ArrayList<ArrayList<Long>> tj = city.getJumpTimeSequences();
    	timeScales = null;
    	timeScales = new LinkedList<CityTimeScale>();
    	for(int i=0; i< sj.size();i++) {
    		
    		CityTimeScale ts = new CityTimeScale();
    		ts.setHeight(getHeight());
    		//timeScale.setWidth(getWidth());
        	ts.setStart(sj.get(i).get(0));
        	ts.setEnd(sj.get(i).get(1));
        	ts.setTimeStart(tj.get(i).get(0));
        	ts.setTimeEnd(tj.get(i).get(1));
        	timeScales.add(ts);
    	}
    	if(timeScales.size()==1) {
    		timeScales.get(0).setTimeScale();
    	}
    	else {
    		if(timeScales.size()>1) timeScales.get(1).setTimeScale();
    	}
	//	System.out.println(timeScales);
		//System.out.println(sj);
		//System.out.println(tj);
   /* 	timeScale = new CityTimeScale();
    	timeScale.setWidth(city.width);
    	timeScale.setHeight(city.height);
    	timeScale.setTimeStart(city.getTimeStart());
    	timeScale.setTimeEnd(city.getTimeEnd());

    	timeScale.setTimeScale();
	*/
}

	/*public Dimension getPreferredSize() {
        return new Dimension(city.width,city.heigth);
    }*/
    
   
   
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.draw(g);
        city.draw(g);
        //timeScale.draw(g);
        for (CityTimeScale ts : timeScales) {
			ts.draw(g);
		}
    }  
    public void setCity(village.City c){
    	city = c;
    }

	@Override
	public void mouseDragged(MouseEvent arg0) {
		zeroX = city.getZeroX();
		zeroY = city.getZeroY();
		int x = arg0.getX();
		int y = arg0.getY();
		double dx = landmarkX - x;
		double dy = landmarkY - y;
		zeroX = zeroX+dx;
		landmarkX = x;
		zeroY = zeroY+dy;
		landmarkY = y;
		city.setZeroX(zeroX);
		city.setZeroY(zeroY);
		background.setZeroX(zeroX);
		background.setZeroY(zeroY);
		updateScales();
		/*timeScale.setTimeStart(city.getTimeStart());
		timeScale.setTimeEnd(city.getTimeEnd());
		*/
		for (MyContentBubble bub : bubbles) {
			bub.moveLocation(-(int)dx, -(int)dy);
		/*	bub.setFocusX((int)city.cityToScreenCoordX(bub.getOwner().getCenterX()));
			bub.setFocusY((int)city.cityToScreenCoordY(bub.getOwner().getCenterY()));
			bub.update();
		*/	
		}
		repaint();
		this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// can be improved, by redrawing only affected area and a better find function
		int x = arg0.getX();
		int y = arg0.getY();
		StateObject result = city.find(x,y);
		if(hover == null ) {
			hover = result;
			hoverOldState = result.getState();
			if(hoverOldState != 2) activateState(result,true,x);
			result.setState(1);
			repaint();
		}
		else{
			if(hover != result){
				if(hoverOldState != 2) deactivateState(hover);
				hover.setState(hoverOldState);
				hover = result;
				hoverOldState = result.getState();
				if(hoverOldState != 2) activateState(result,true,x);
				result.setState(1);
				repaint();
			}
				
		}
	//	System.out.println(x+"/"+city.screenToCityCoordX(x)+"/"+city.softToHardX((long)city.screenToCityCoordX(x))+"/"+city.hardToSoftX(city.softToHardX((long)city.screenToCityCoordX(x)))+"/"+city.cityToScreenCoordX(city.hardToSoftX(city.softToHardX((long)city.screenToCityCoordX(x)))));
	}




	private String timeToString(long t){
		Calendar C = Calendar.getInstance();
		C.setTimeInMillis(t);
		C.setTimeZone(TimeZone.getDefault());
		return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(C.getTime());
	}

	public static int doubleToInt(double l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        System.out.println("double out of int bounds");
	    }
	    return (int) l;
	}
	

	
	private int getTooltipX(StateObject o){
		if(o.getClass().getName()=="village.Product"){
			Product goo = (Product) o;
			return (int)(city.cityToScreenCoordX((goo.getCenterX()))+(goo.getWidth()/2)*(1-0.5/Math.sqrt(2))*city.getVisualScale());
		}
		else{
			village.ELOProcess goo = (ELOProcess) o;
			int start = (int)(Math.max(0,city.cityToScreenCoordX(goo.getFrom().getCenterX())));
			int end = (int)(Math.min(getWidth(),city.cityToScreenCoordX(goo.getTo().getCenterX())));
			return (start+end)/2;
		}
		
	}
	
	private int getTooltipY(StateObject o){
		if(o.getClass().getName()=="village.Product"){
			Product goo = (Product) o;
			return (int)(city.cityToScreenCoordY(goo.getCornerY())+city.getVisualScale()*goo.getHeight()/7);
		}
		else{
			village.ELOProcess goo = (ELOProcess) o;
			return (int)(city.cityToScreenCoordY(goo.getCenterY()));
		}
		
	}
	
	private void activateState(StateObject what,boolean hover, int x){
		if(what.getClass().getName()=="village.Product"){
			village.Product goo = (village.Product)what;
			if(goo.getName() != -1){
				what.setState(2);
				boolean bubbleNExists = true;
				for (MyContentBubble bub : bubbles) {
					if(bub.getOwner() == what) bubbleNExists = false;
				}
				if(bubbleNExists){
					//bubble = new MyContentBubble((int)(scaleX*(goo.getCornerX()+goo.getWidth()*(0.5+0.5/Math.sqrt(2)))-zeroX), (int)Math.round(scaleX*(goo.getCornerY()+goo.getHeight()/Math.sqrt(8))-zeroY), 300, 190, "");
					bubble = new MyContentBubble(getTooltipX(goo), getTooltipY(goo), 300, 190, "");
					bubble.setHeader(city.productTypeDict.get(goo.getType()));
					bubble.setAuthor("User: "+city.userDict.get(city.GetExternalWhoOfID(goo.getAuthorID())));
					bubble.setDescription("ELO: "+city.productDict.get(goo.getName())+
							"<br>Tool used: "+city.toolDict.get(goo.getMadeByName())+
							"<br>Mission: "+city.missionDict.get(goo.getMission()));
					bubble.setTime(timeToString(goo.getTime()));
					bubble.makeContent();
					bubble.setOwner(what);
				/*	if(hover) bubble.hideCloseButton();
					else bubble.showCloseButton();
				*/	bubble.setMouseListener(this);
					bubble.setActionListener(this);
					add(bubble,getComponentCount());
					setComponentZOrder(bubble, 0);
					validate();
					bubbles.add(bubble);
				}
				
				
			}
		}
		else {// what.getClass().getName()=="village.ELOProcess"
			village.ELOProcess goo = (village.ELOProcess)what;
			if(goo.getFatherName()!=-1){
				what.setState(2);
				boolean bubbleNExists = true;
				for (MyContentBubble bub : bubbles) {
					if(bub.getOwner() == what) bubbleNExists = false;
				}
				if(bubbleNExists){
					bubble = new MyContentBubble(getTooltipX(goo), getTooltipY(goo), 300, 140, "");
					//bubble = new MyContentBubble(x, getTooltipY(goo), 300, 140, "");
					bubble.setHeader(city.toolDict.get(goo.getName())+"<br> "+city.productDict.get(goo.getFrom().getName()));
					bubble.setDescription("Tool: "+city.toolDict.get(goo.getName()));
					bubble.setAuthor("User: "+city.userDict.get(city.GetExternalWhoOfID(goo.getAuthorID())));
					bubble.setTime(timeToString(goo.getTime()));
					bubble.makeContent();
					bubble.setOwner(what);
				/*	if(hover) bubble.hideCloseButton();
					else bubble.showCloseButton();
			*/		bubble.setMouseListener(this);
					bubble.setActionListener(this);
					add(bubble,getComponentCount());
					setComponentZOrder(bubble, 0);
					validate();
					bubbles.add(bubble);
				}
				
			}
		}
		
		repaint();
	}
	
	private void deactivateState(StateObject what){
		if(what.getClass().getName()=="village.ELOProcess" && ((village.ELOProcess)what).getFatherName() != -1){
				what.setState(0);
				MyContentBubble bounty = null;
				for (MyContentBubble bub : bubbles) {
					if(bub.getOwner() == what) bounty = bub;
				}
				if(bounty != null) {
					bounty.setVisible(false);
					remove(bounty);
					bubbles.remove(bounty);
					bounty = null;
				}
				else{
					System.out.println("nothing to deactivate");
				}
		}
		else{
			if(what.getClass().getName()=="village.Product" && ((village.Product)what).getName() != -1){
				what.setState(0);
				MyContentBubble bounty = null;
				for (MyContentBubble bub : bubbles) {
					if(bub.getOwner() == what) bounty = bub;
				}
				if(bounty != null) {
					bounty.setVisible(false);
					remove(bounty);
					bubbles.remove(bounty);
					bounty = null;
					
				}
				else{
					System.out.println("nothing to deactivate");
				}
			}
		}
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getSource() == this){
			int x = arg0.getX();
			int y = arg0.getY();
			StateObject result = city.find(x,y);
			//if(hover != null) hover.setState(0);
			//hover = null;
			System.out.println(hoverOldState);
			if(hoverOldState == 2){
				deactivateState(result);
				hoverOldState = 0;
			}
			else{
				activateState(result,false,x);
				hoverOldState = 2;
			}
		}
		else{
			Component o = (Component)(arg0.getSource());
			while(o.getParent() != this){
				o = o.getParent();
			}
			
			setComponentZOrder(o, 0);
			repaint(); 
		}
	}
	


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	
	}




	@Override
	public void mouseExited(MouseEvent arg0) {
		if(hover != null && hoverOldState != 2){
			deactivateState(hover);
			hover = null;
			hoverOldState = 0;
		}
		
	}




	@Override
	public void mousePressed(MouseEvent arg0) {
		if(arg0.getSource() == this){
			landmarkY = arg0.getY();
			landmarkX = arg0.getX();
		}
		else{
			Component o = (Component)(arg0.getSource());
			while(o.getParent() != this){
				o = o.getParent();
			}
			
			setComponentZOrder(o, 0);
			validate();
			repaint();
		}
	}




	@Override
	public void mouseReleased(MouseEvent arg0) {
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}


	private void zoom(int x, int y, int amount){
		double newScale = scaleX * (1-0.1*amount);
		if (newScale <0.00001) //amount = 7
			newScale = 0.00001;
		if (newScale >7) // amount = 7
			newScale = 7;
		
		long hardZoomCenterX = city.softToHardX((long)(city.screenToCityCoordX(x)));
		city.setScale(newScale);
		city.updateJumps();
		city.UpdateJumpedCoordinates();
		//double oldZeroX=zeroX;
		//zeroX = city.hardToSoftX(hardZoomCenterX) - x/newScale;
		zeroX = newScale*city.hardToSoftX(hardZoomCenterX) - x;
		//zeroX = newScale*(hardZoomCenterX) -x;
		///city.jumpZero();

		///zeroX = city.getZeroX();
		
	//	double oldZeroY=zeroY;
		
		
		///zeroX = ( newScale/scaleX*(zeroX+x) - x);
	//	zeroY = ( newScale/scaleX*(zeroY+y) - y);
		
			//city.updateCoordsWithJumps();
		city.setZeroX(zeroX);
		city.setZeroY(zeroY);
		zeroX=city.getZeroX();
	//	zeroY=city.getZeroY();
		background.setZeroX(zeroX);
	//	background.setZeroY(zeroY);
		background.setScale(newScale);
		
		/*timeScale.setTimeStart(city.getTimeStart());
		timeScale.setTimeEnd(city.getTimeEnd());
		timeScale.setTimeScale();*/
		updateScales();
		//city.zoom(-arg0.getWheelRotation(),x,y);
		for (MyContentBubble bub : bubbles) {
		//	int bX = bub.getFocusX();
	//		int bY = bub.getFocusY();
		//	double oldCityX =  (1/scaleX*(bX + oldZeroX));
	//		double oldCityY =  (1/scaleX*(bY + oldZeroY));
		//	int newDrawBoardX = (int)Math.round(newScale*(oldCityX) - zeroX);
	//		int newDrawBoardY = (int)Math.round(newScale*(oldCityY) - zeroY);
	//		bub.moveLocation(newDrawBoardX-bX, newDrawBoardY-bY);
			//bub.moveLocation(newDrawBoardX-bX, 0);
			bub.setFocusX(getTooltipX(bub.getOwner()));
			System.out.println(getTooltipX(bub.getOwner()));
			bub.setFocusY(getTooltipY(bub.getOwner()));
			
			//repaint();
			
		
		}
		scaleX = newScale;

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		zoom(arg0.getX(), arg0.getY(), arg0.getWheelRotation());
		repaint();
		
	}




	@Override
	public void ancestorMoved(HierarchyEvent arg0) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void ancestorResized(HierarchyEvent arg0) {
		// TODO Auto-generated method stub
		background.setHeight(getHeight());
		background.setWidth(getWidth());
		city.setHeight(getHeight());
		city.setWidth(getWidth());
		updateScales();
		/*timeScale.setHeight(getHeight());
		timeScale.setWidth(getWidth());
		timeScale.setTimeStart(city.getTimeStart());
		timeScale.setTimeEnd(city.getTimeEnd());
		*/
		zeroX = city.getZeroX();
		zeroY = city.getZeroY();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Component o = (Component)(e.getSource());
		while(o.getParent() != this){
			o = o.getParent();
		}
		o.setVisible(false);
		deactivateState(((MyContentBubble)o).getOwner());
	} 
}