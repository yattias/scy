package village;

//import javax.swing.*;

import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.geom.Line2D;
//import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Rectangle;
import java.awt.RenderingHints;

//import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
//import java.util.Vector;



public class City {

	static int DRAWMETHOD_CRUDE = 0;
	static int DRAWMETHOD_METRO = 1;
	public static double labelWidth = 150;
	static double timeScale = 500, heightSpacing=2.5;
	long zerotime;
	private Product root;
	public int width;
	public int height;
	public double totalWidth=0,totalHeight=0;
	int productWidth, productHeight;
	int maxwdth=0, maxdepth=0;
	int userview=-1; //  number of active user view : userID-1
	Random generator;
	Vector<Vector<Product>> usersTimeline;
	Vector<Integer> actorsid; // actorsid.get(3) is the external name (id) of user with internal #3
	static double zeroX, zeroY; // coordinates of the external zero point (in local base)
	private double zeroXdelta = 0; //zeroX + zeroXdelta = position in hard space 
	static double scale, scaleY=0.5;
	public ArrayList<String> userDict,productDict,toolDict,missionDict;
	public ArrayList<String> productTypeDict;
	private Rectangle visualWindow; 
	/*
	 * 0 -> "elo_saved"
	 */
	private final double jumpPadding = 130;
	private ArrayList<ArrayList<Long>> jumps;
	PriorityQueue<Product> timeOrderQueue;
	ArrayList<Product> timeOrderedProducts;
	
	
	
	public City(int w, int h){
		width=w;
		height=h;
		init();
		
	}
	
	public City(int w, int h, int pw, int ph){
		width=w;
		height=h;
		productHeight=ph;
		productWidth=pw;
		init();
	}
	
	private void init(){
		   jumps = null;
		   jumps = new ArrayList<ArrayList<Long>>();
		   timeOrderedProducts = new ArrayList<Product>();
		generator = new Random(42);
		setRoot(new Product(-1));
		usersTimeline = new Vector<Vector<Product>>();
		actorsid = new Vector<Integer>();
		actorsid.add(new Integer(-1));
		zeroX=0;
		zeroY=0;
		scale = 1;
		//addProcess(-1, -1, -1, -1, -1); Implement later (bad design)
		userDict = new ArrayList<String>();
		productDict = new ArrayList<String>();
		productDict.add("elo_saved");
		productTypeDict = new ArrayList<String>();
		productTypeDict.add("elo_saved");
		productTypeDict.add("elo_loaded");
		productTypeDict.add("tool_opened");
		productTypeDict.add("tool_closed");
		Product.loadType = productTypeDict.indexOf("elo_loaded");
		Product.savedType = productTypeDict.indexOf("elo_saved");
		Product.toolOpenedType = productTypeDict.indexOf("tool_opened");
		Product.toolQuitType = productTypeDict.indexOf("tool_closed");
		toolDict = new ArrayList<String>();
		missionDict = new ArrayList<String>();
		visualWindow = new Rectangle();
		updateClip();
	}
	
	int addToDict(ArrayList<String> dict, String value){
		//try {
			for (String string : dict) {
				if(string.equals(value)) {
					return dict.indexOf(string);
				}
			}
			dict.add(value);
			return (dict.size()-1);
	/*	} catch (Exception e) {
			System.out.println("Problem with dictionary");
			System.out.println(e);
			return -1;
		}*/
	}
	
	public void setZeroX(double zx){
		zeroX=zx;
		updateClip();
	}
	
	public void setZeroY(double zy){
		zeroY=zy;
		updateClip();
	}
	
	public double getZeroX(){
		return zeroX;
	}
	
	public double getZeroY(){
		return zeroY;
	}
	
	public void setScale(double s){
		scale = s;
		updateJumps();
		UpdateJumpedCoordinates();
		updateClip();
	}
	
	public void zoom(int amount){
		scale = scale + 0.2*amount;
		if (scale <0.2) 
			scale = 0.2;
		if (scale > 2)
			scale = 2;
		updateClip();
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

	
	public int GetInternalIDofWho(int who){
		int i=0;
		while(actorsid.size()>i && actorsid.get(i) != who ){
			i++;
		}
		if(i == actorsid.size()) actorsid.add(who);
		return i;
	}
	
	public int GetExternalWhoOfID(int id){
		if(id<actorsid.size() && id>-1){
			return actorsid.get(id);
		}
		else{
			System.out.println(id+" not within actorsid range, returning 0");
			return 0;
		}
		
	}
	
	public void addProduct(int name){
		Product pr = new Product(name, 20 + generator.nextInt(width - 40),20 + generator.nextInt(height - 40), productWidth, productHeight);
		ELOProcess prc = new ELOProcess(getRoot(),pr,-1,-1,-1);
		getRoot().addUsedByProcess(prc);
		pr.addMadeByProcess(prc);
		pr.setImportant(true);
	}
	
	public void addProduct(int name, int mission){
		Product pr = new Product(name, 20 + generator.nextInt(width - 40),20 + generator.nextInt(height - 40), productWidth, productHeight);
		pr.setMission(mission);
		ELOProcess prc = new ELOProcess(getRoot(),pr,-1,-1,-1);
		getRoot().addUsedByProcess(prc);
		pr.addMadeByProcess(prc);
		pr.setImportant(true);
	}
	
	private void addProduct(int name, int mission, int type) {
		Product pr = new Product(name, 20 + generator.nextInt(width - 40),20 + generator.nextInt(height - 40), productWidth, productHeight);
		pr.setMission(mission);
		pr.setType(type);
		ELOProcess prc = new ELOProcess(getRoot(),pr,-1,-1,-1);
		getRoot().addUsedByProcess(prc);
		pr.addMadeByProcess(prc);
		if(productTypeDict.get(type).equalsIgnoreCase("elo_saved")||productTypeDict.get(type).equalsIgnoreCase("elo_loaded")){
			pr.setImportant(true);
		}else
			pr.setImportant(false);
		
	}
	
	public void addProcess(int from, int to, int name, int who, long time){
		int id=GetInternalIDofWho(who);
		ELOProcess pom = new ELOProcess(findLast(from), findDefault(to), name, id, time);
		if(pom.from == pom.to) pom.from = getRoot();
		if(pom.to.madeByProcess.from == getRoot()){
			getRoot().removeUsedByProcess(pom.to.madeByProcess);
		}
		pom.from.addUsedByProcess(pom);
		pom.to.addMadeByProcess(pom);
	/*	if(root.UsedByProcessContainsAsResult(pom.to))
			root.removeUsedByProcess(pom.to);
	*/	
		while(usersTimeline.size() < id + 1) // (maxId = size -1)
			usersTimeline.add(new Vector<Product>());
		if(usersTimeline.get(id).size() == 0 || 
		   usersTimeline.get(id).lastElement() == pom.from ||
		   pom.from.name < 1){ // Caution, may raise NoSuchElementException if 2nd condition is checked first  
			
			usersTimeline.get(id).add(pom.to);
		}
		else{
		//	usersTimeline.get(id).add(pom.from);
			usersTimeline.get(id).add(pom.to);
		}
	}
	
	public void addProduct(String elo_uri) {
		addProduct(addToDict(productDict, elo_uri));
		
	}
	
	public void addProduct(String elo_uri, String mission) {
		addProduct(addToDict(productDict, elo_uri),addToDict(missionDict, mission));
		
	}
	
	public void addProduct(String elo_uri, String mission, String product_type) {
		addProduct(addToDict(productDict, elo_uri),addToDict(missionDict, mission),addToDict(productTypeDict, product_type));
		
	}



	public void addProcess(String old_uri, String elo_uri, String tool,
			String user, long time) {
		// TODO Auto-generated method stub
		addProcess(productDict.indexOf(old_uri), productDict.indexOf(elo_uri), addToDict(toolDict, tool), addToDict(userDict, user), time);
		
	}

	
	private Product findDefault(int name){
		// TODO can be optimised by keeping pointers to all objects in an efficient structure (e.g. by a heap)
		Product candidate = getRoot();
		if(name < 0){
			return getRoot();
		}
		else{
			for (ELOProcess po : getRoot().usedByProcesses) {
				if(po.name==-1 && po.to.name==name && po.time == -1 && po.actor == -1) return po.to;
			}
			
			Stack<Product> stack = new Stack<Product>();
			stack.push(getRoot());
			Product dummy;
			while(!stack.empty()){
				dummy = stack.pop();
				if(dummy.name == name){
					if(candidate == getRoot() || (candidate != getRoot() && dummy.getTime()<candidate.getTime())) candidate = dummy;
				}
				
				for(int i=dummy.usedByProcesses.size()-1; i>-1;i--){
					stack.push(dummy.usedByProcesses.get(i).to);
				
				}
			}
		}
		if(candidate == getRoot()) 	System.out.println("node not found, returning root");
		return candidate;
	}
	
	private Product findLast(int name){
		// TODO can be optimised by keeping pointers to all objects in an efficient structure (e.g. by a heap)
		Product candidate = getRoot();
		if(name < 0){
			return getRoot();
		}
		else{
			Stack<Product> stack = new Stack<Product>();
			stack.push(getRoot());
			Product dummy;
			while(!stack.empty()){
				dummy = stack.pop();
				if(dummy.name == name){
					if(candidate == getRoot() || (candidate != getRoot() && dummy.getTime()>=candidate.getTime())) candidate = dummy;
				}
				
				for(int i=dummy.usedByProcesses.size()-1; i>-1;i--){
					stack.push(dummy.usedByProcesses.get(i).to);
				
				}
			}
		}
		if(candidate == getRoot()) 	System.out.println("node not found, returning root");
		return candidate;
	}
	
	public double screenToCityCoordX(double x){
		return 1/scale*(x + zeroX);
	}
	
	public double screenToCityCoordY(double y){
		return 1/scaleY*(y + zeroY);
	}
	
	public double cityToScreenCoordX(double x){
		//System.out.println("city to screen X: "+(x*scale-zeroX)+"zeroX: "+getZeroX()+" scale: "+scale);
		return x*scale-zeroX;
		

	}
	public double cityToScreenCoordY(double y){
		return y*scaleY-zeroY;

	}
	
	public StateObject find(int x, int y){
		// TODO can be optimised by keeping pointers to all objects in an efficient structure (e.g. by a heap)
		
	/*		x = (int) Math.round(screenToCityCoordX(x));
			y = (int) (screenToCityCoordY(y));
	*/
			Stack<Product> stack = new Stack<Product>();
			StateObject result = getRoot();
		//search for best fitting node	
			stack.push(getRoot());
			Product dummy;
			while(!stack.empty()){
				dummy = stack.pop();
				if(dummy != getRoot() && isAtScreenPosition(dummy,x,y)){
					result = dummy;
				}
				for(int i=dummy.usedByProcesses.size()-1; i>-1;i--)
					stack.push(dummy.usedByProcesses.get(i).to);
			}
			if(result != getRoot()) return result;
		//search for best link if no node was found	
			stack.push(getRoot());
			double bestDst = 9999;
			while(!stack.empty()){
				dummy = stack.pop();
					double dst = 999999;
					if(dummy != getRoot()) dst = getAtScreenDistance(dummy.madeByProcess.link,x,y);
					if(dummy != getRoot() && dummy.madeByProcess.from != getRoot() && dst<65){//if x,y is close enough to the link between dummy and its i-th child, return link
						if(result == getRoot() || dst<bestDst){
							result = dummy.madeByProcess;
							bestDst = dst;
						}
							
					}
					for(int i=dummy.usedByProcesses.size()-1; i>-1;i--)
						stack.push(dummy.usedByProcesses.get(i).to);
			}
		return result;
	}

	private double getAtScreenDistance(Line2D link, int x, int y) {
		Line2D newLink = new Line2D.Double(cityToScreenPoint(link.getP1()), cityToScreenPoint(link.getP2()));
		
		return newLink.ptSegDistSq(x, y);
	}

	
	private boolean isAtScreenPosition(Product dummy, int x, int y) {
		Rectangle2D box;
		if(dummy.isImportant()){
			box = new Rectangle2D.Double(cityToScreenCoordX(dummy.getCenterX())-dummy.width/2, cityToScreenCoordY(dummy.getCenterY())-dummy.height/2, dummy.width, dummy.height);
		}
		else {
			box = new Rectangle2D.Double(cityToScreenCoordX(dummy.getCenterX())-dummy.width/2, cityToScreenCoordY(dummy.getCenterY())-dummy.height/2, dummy.width, dummy.height);
		}
		return box.contains(x, y);
	}

	public void organize(){
		Stack<ELOProcess> st = new Stack<ELOProcess>();
		Collections.sort(getRoot().usedByProcesses,Collections.reverseOrder());
		st.addAll(getRoot().usedByProcesses);
		
		village.ELOProcess dummy;
		int maxwidth = 0;
		zerotime = st.peek().getTime();
		long maxtime = zerotime;
		while(st.size()>0){
			dummy = st.pop();
			if(dummy.getTime()>maxtime) maxtime = dummy.getTime();
			if(dummy.isOriginal()){
				dummy.getTo().setHardCenterPosition((long)((dummy.getTime()-zerotime)/timeScale), (long)(-(++maxwidth)*heightSpacing*productHeight));
			}
			else{
				dummy.getTo().setHardCenterPosition((long)((dummy.getTime()-zerotime)/timeScale), dummy.getFrom().getCenterY());
			}
			//System.out.println((int)((dummy.getTime()-zerotime)/90000)+" "+(dummy.getTime()-zerotime));
			dummy.update();
			Collections.sort(dummy.getTo().usedByProcesses,Collections.reverseOrder());
			st.addAll(dummy.getTo().usedByProcesses);
		}

		//Make a simplified minor of the graph
		Stack<Product> mainStack = new Stack<Product>();
		Stack<ELOProcess> helperStack = new Stack<ELOProcess>();
		mainStack.push(getRoot());

		while(!mainStack.empty()){
			// find Original Links Of mainStack.peek()
			helperStack.addAll(mainStack.peek().usedByProcesses);
			while(!helperStack.empty()){
				if(helperStack.peek().to.usedByProcesses.size()>1 || (!helperStack.peek().to.usedByProcesses.isEmpty() && helperStack.peek().to.usedByProcesses.firstElement().isOriginal())){
					mainStack.peek().addMajorChildrenProduct(helperStack.pop().getTo());
				}else{
					helperStack.addAll(helperStack.pop().to.usedByProcesses);
				}	
			}
			mainStack.addAll(mainStack.pop().majorChildrenProducts);
		}
		
		
		totalWidth = (maxtime - zerotime)/timeScale+productWidth;
		totalHeight = (maxwidth)*heightSpacing*productHeight+productHeight;	
		width=Math.min(1024,Math.max(width,(maxdepth*3+2)*productWidth)+900);
		height=Math.min(600, Math.max(height,(maxwdth*2+2)*productHeight)+200);
	//	width=(Math.max(width,(maxdepth*3+2)*productWidth)+900);
	//	height=Math.max(height,(maxwdth*2+2)*productHeight)+200;
		if(getRoot().usedByProcesses.size()>0){
			setZeroY(Math.max(-height+(int)heightSpacing*productHeight,(int)(-(maxwidth)*heightSpacing*productHeight)));
			setZeroX(-15);
		}
		//create the time ordered queue
		Comparator<Product> comp = new ProductComparator();
		PriorityQueue<Product> q = new PriorityQueue<Product>(10, comp );
		timeOrderQueue = new PriorityQueue<Product>(10,comp);
		for (ELOProcess pr : root.usedByProcesses) {
			q.add(pr.getTo());
		}
		while(!q.isEmpty()){
			timeOrderQueue.add(q.peek());
			for (ELOProcess pr : q.poll().usedByProcesses) {
				q.add(pr.getTo());
			}
		}
		timeOrderedProducts.clear();
		timeOrderedProducts.addAll(timeOrderQueue);
		//Collections.sort(timeOrderedProducts);
		updateJumps();
		UpdateJumpedCoordinates();
		
	}

	
	public void organizeO(){
		Queue<ELOProcess> queue = new LinkedList<ELOProcess>(getRoot().usedByProcesses);
		
		village.ELOProcess dummy;
		int maxwidth = 0;
		zerotime = queue.peek().getTime();
		long maxtime = zerotime;
		while(queue.size()>0){
			dummy = queue.poll();
			if(dummy.getTime()>maxtime) maxtime = dummy.getTime();
			if(dummy.isOriginal()){
				dummy.getTo().setCenterPosition((long)((dummy.getTime()-zerotime)/500), (long)(-(++maxwidth)*heightSpacing*productHeight));
			}
			else{
				dummy.getTo().setCenterPosition((long)((dummy.getTime()-zerotime)/500), dummy.getFrom().getCenterY());
			}
			//System.out.println((int)((dummy.getTime()-zerotime)/90000)+" "+(dummy.getTime()-zerotime));
			dummy.update();
			queue.addAll(dummy.getTo().usedByProcesses);
		}

		//Make a simplified minor of the graph
		Stack<Product> mainStack = new Stack<Product>();
		Stack<ELOProcess> helperStack = new Stack<ELOProcess>();
		mainStack.push(getRoot());

		while(!mainStack.empty()){
			// find Original Links Of mainStack.peek()
			helperStack.addAll(mainStack.peek().usedByProcesses);
			while(!helperStack.empty()){
				if(helperStack.peek().to.usedByProcesses.size()>1 || (!helperStack.peek().to.usedByProcesses.isEmpty() && helperStack.peek().to.usedByProcesses.firstElement().isOriginal())){
					mainStack.peek().addMajorChildrenProduct(helperStack.pop().getTo());
				}else{
					helperStack.addAll(helperStack.pop().to.usedByProcesses);
				}	
			}
			mainStack.addAll(mainStack.pop().majorChildrenProducts);
		}
		
		
		totalWidth = (maxtime - zerotime)/90000+productWidth;
		totalHeight = (maxwidth)*heightSpacing*productHeight+productHeight;	
		width=Math.min(1027,Math.max(width,(maxdepth*3+2)*productWidth)+900);
		height=Math.min(800, Math.max(height,(maxwdth*2+2)*productHeight)+200);
		if(getRoot().usedByProcesses.size()>0){
			setZeroY(Math.max(-height+(int)heightSpacing*productHeight,(int)(-(maxwidth)*heightSpacing*productHeight)));
			setZeroX(-15);
		}
	}
	
	
	public Vector<String> actors(){
		Vector<String> bla = new Vector<String>();
		for(int i=0; i<actorsid.size();i++){
			if(actorsid.get(i) != -1){
				bla.add("User "+actorsid.get(i));
				// actorsid.add(new Integer(i));
			}
				
		}
		
		return bla;
	}
	
	public void draw(java.awt.Graphics gr){
		int drawmethod=2;
		Stroke oldStroke = ((Graphics2D) gr).getStroke();
		Color oldColor = gr.getColor();
		switch (drawmethod) {
		case 0: //Crude
			drawCrude(gr);
			break;
		case 1: //Metro
			drawMetro(gr);
			break;
		case 2: //Fancy Metro
			drawFancyMetro(gr);
			break;

		default:
			drawCrude(gr);
			break;
		}
		gr.setColor(Color.LIGHT_GRAY);
	
	/*	for (ArrayList<Double> jump : jumps) {
			Rectangle2D rr = new Rectangle2D.Double(cityToScreenCoordX(jump.get(0)), 0, jump.get(1)*scale, height*0.7);
			((Graphics2D)gr).draw(rr);
		
	
		}*/
		
		double buff = 0;
		for (ArrayList<Long> jump : jumps) {
			drawJump(gr,cityToScreenCoordX(jump.get(0)-buff));
			//Rectangle2D rr = new Rectangle2D.Double(cityToScreenCoordX(jump.get(0)-buff), 0, 1, height);
			//((Graphics2D)gr).draw(rr);
			buff+=jump.get(1);
	
		}
	/*	for (Product pr : timeOrderedProducts) {
			Rectangle2D rr = new Rectangle2D.Double(cityToScreenCoordX(pr.getCornerX()),cityToScreenCoordY(pr.getCornerY()),pr.width,pr.height);
			((Graphics2D)gr).draw(rr);
		}*/
		//Rectangle2D rr = new Rectangle2D.Double(arg0, arg1, arg2, arg3)
		drawLabels((Graphics2D) gr);
		gr.setColor(oldColor);
		((Graphics2D) gr).setStroke(oldStroke);
	}
	
 	private void drawJump(Graphics gr, double d) {
		Graphics2D gr2 = (Graphics2D) gr;
		Color oldColor = gr2.getColor();
		Stroke oldStroke = gr2.getStroke();
		gr2.setColor(Color.lightGray);
		gr2.setStroke(new BasicStroke(4.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, new float[] {10.0f}, 0.0f));
		gr2.draw(new Line2D.Double(new Point2D.Double(d, 0), new Point2D.Double(d, height)));
		gr2.setColor(Color.white);
		gr2.setStroke(new BasicStroke(4.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, new float[] {10.0f}, 10.0f));
		gr2.draw(new Line2D.Double(new Point2D.Double(d, 0), new Point2D.Double(d, height)));
		gr2.setStroke(oldStroke);
		gr2.setColor(oldColor);
	}

	private void drawFancyMetro(Graphics gr) {
		Graphics2D gr2 = (Graphics2D)gr;
		
		
		Product.activeClip= new Rectangle(width, height);
		ELOProcess.activeClip= Product.activeClip;
 		gr.setPaintMode();
		Color c = new Color(0,255,255);
		gr.setColor(c);
		
		//Rectangle2D rrr = new Rectangle2D.Double(screenToCityCoordX(+2), screenToCityCoordY(+10), (width-15)*scale, 10);
		//gr2.fill(Product.activeClip);
		gr2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		gr2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//gr2.setClip(zeroX, zeroY,(int)(width/scale),(int)(height/scale));
		
		Stack<Product> stack = new Stack<Product>();
		stack.push(getRoot());
		Product dummy;
		while(!stack.empty()){
			dummy = stack.pop();
			for(int i=dummy.usedByProcesses.size()-1; i>-1;i--)
				stack.push(dummy.usedByProcesses.get(i).to);

			if(dummy != getRoot()) dummy.drawAtCenterPosition(gr, 10, (long)cityToScreenCoordX(dummy.getCenterX()), (long)cityToScreenCoordY(dummy.getCenterY()), (int)(dummy.getWidth()*getVisualScale()), (int)((double)dummy.getHeight()*getVisualScale()));
		}
		stack.push(getRoot());
		while(!stack.empty()){
			dummy = stack.pop();
			for(int i=dummy.usedByProcesses.size()-1; i>-1;i--){
				stack.push(dummy.usedByProcesses.get(i).to);
				Line2D line = new Line2D.Double(cityToScreenPoint(dummy.usedByProcesses.get(i).link.getP1()), cityToScreenPoint(dummy.usedByProcesses.get(i).link.getP2()));
				dummy.usedByProcesses.get(i).drawAtPosition(gr2, 2, line, (int)((double)ELOProcess.width*getVisualScale()));
			}
		}

		LinkedList<Integer> usedTypes = new LinkedList<Integer>();
		for(int i = 0; i<timeOrderedProducts.size();i++){
			if(i<timeOrderedProducts.size()-1){
				if((long)cityToScreenCoordX(timeOrderedProducts.get(i).getCenterX()) != (long)cityToScreenCoordX(timeOrderedProducts.get(i+1).getCenterX()) ||
						(long)cityToScreenCoordY(timeOrderedProducts.get(i).getCenterY()) != (long)cityToScreenCoordY(timeOrderedProducts.get(i+1).getCenterY())){
					dummy = timeOrderedProducts.get(i);
					dummy.drawAtCenterPosition(gr, 2, (long)cityToScreenCoordX(dummy.getCenterX()), (long)cityToScreenCoordY(dummy.getCenterY()), (int)((double)dummy.getWidth()*getVisualScale()), (int)((double)dummy.getHeight()*getVisualScale()));
					usedTypes = new LinkedList<Integer>();
				}else{
					if(!usedTypes.contains(timeOrderedProducts.get(i).getType())){
						dummy = timeOrderedProducts.get(i);
						dummy.drawAtCenterPosition(gr, 2, (long)cityToScreenCoordX(dummy.getCenterX()), (long)cityToScreenCoordY(dummy.getCenterY()), (int)((double)dummy.getWidth()*getVisualScale()), (int)((double)dummy.getHeight()*getVisualScale()));
						usedTypes.add(dummy.getType());
					}
					
				}
			}else{
				dummy = timeOrderedProducts.get(i);
				dummy.drawAtCenterPosition(gr, 2, (long)cityToScreenCoordX(dummy.getCenterX()), (long)cityToScreenCoordY(dummy.getCenterY()), (int)((double)dummy.getWidth()*getVisualScale()), (int)((double)dummy.getHeight()*getVisualScale()));
			}
		}
		/*		stack.push(getRoot());
		while(!stack.empty()){
			dummy = stack.pop();
			for(int i=dummy.usedByProcesses.size()-1; i>-1;i--){
				stack.push(dummy.usedByProcesses.get(i).to);
			}
			dummy.drawAtCenterPosition(gr, 2, (long)cityToScreenCoordX(dummy.getCenterX()), (long)cityToScreenCoordY(dummy.getCenterY()), (int)((double)dummy.getWidth()*getVisualScale()), (int)((double)dummy.getHeight()*getVisualScale()));
		}
	*/	
		//******************if(actorsid.size() > userview && userview>0) drawUser(gr,userview,2);
		
		
		
	}

 	 private void drawLabels(Graphics2D gr2) {
 		 
 		gr2.setColor(new Color(0,0,0,0.05f));
 		gr2.setStroke(new BasicStroke(1));
 		
 		int max = getRoot().usedByProcesses.size();
 		for(int i=0;i<=max;i++) {
			double y = cityToScreenCoordY(-(0.5+i)*productHeight*heightSpacing);
			Line2D l = new Line2D.Double(0, y, width, y);
			gr2.draw(l);
		}
 		
 		
        gr2.setStroke(new BasicStroke(3));
 		for(int i=0;i<max;i++) {
 			gr2.setColor(new Color(1f,0.2f,0.0f,0.7f));
			double y = cityToScreenCoordY(-(1.45+i)*productHeight*heightSpacing);
			RoundRectangle2D r = new RoundRectangle2D.Double(4, y, labelWidth, productHeight*heightSpacing/2.2 ,20,40);
			gr2.fill(r);
			gr2.setColor(new Color(1,1,1,0.7f));
			gr2.draw(r);
			
			
		}
 		
 		Font of = gr2.getFont();
        Font font = new Font("SansSerif", Font.PLAIN, 17);
 		for(int i=0;i<max;i++) {
			double yCenter = cityToScreenCoordY(-(1+i)*productHeight*heightSpacing);
			
			String label = toolDict.get(getRoot().usedByProcesses.get(max-i-1).getName());
	        gr2.setFont(font);
	        FontMetrics metrics = gr2.getFontMetrics(font);
	        int fh = metrics.getHeight();
	        int fw = metrics.stringWidth(label);
	        gr2.setColor(Color.white);
	        gr2.drawString(label, (float)(labelWidth/2-fw/2),(float)(yCenter+fh/3));
	        
		}
 		
 		gr2.setFont(of);
	}

	private Point2D cityToScreenPoint(Point2D p1) {
 		Point2D result = new Point2D.Double(cityToScreenCoordX(p1.getX()), cityToScreenCoordY(p1.getY()));
		return result;
	}

	public double getVisualScale() {
		return 1;
	/*	if(scale<0.7) return 0.7;
		if(scale>1.5) return 1.5;
		return scale;
		*/ 
	}

	void drawFancyMetroNew(Graphics gr) {
		gr.setPaintMode();
		Color c = new Color(255,255,255);
		gr.setColor(c);
		//gr.fillRect(0, 0, width+1000, height+1000);
		Graphics2D gr2 = (Graphics2D)gr;
		gr2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		gr2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//gr2.setClip(zeroX, zeroY,(int)(width/scale),(int)(height/scale));
		
		Stack<Product> stack = new Stack<Product>();
		stack.push(getRoot());
		Product dummy;
		Product runner;
		Line2D tempLine = new Line2D.Double();
		while(!stack.empty()){
			dummy = stack.pop();
			for(int i=dummy.majorChildrenProducts.size()-1; i>-1;i--)
				stack.push(dummy.majorChildrenProducts.get(i));
			
			if(dummy != getRoot()) {
				if(visualWindow.contains(dummy.getCenterX(), dummy.getCenterY())) dummy.draw(gr,10);
				for (Product pr : dummy.majorChildrenProducts) {
					tempLine.setLine(dummy.getCenterX(), dummy.getCenterY(), pr.getCenterX(), pr.getCenterY());
					if(tempLine.intersects(visualWindow)){
						runner = pr.madeByProcess.from;
						while(runner != dummy){
							runner.draw(gr,10);
							runner  = runner.madeByProcess.from;
						}
					}
				}	
			}
		}
		stack.push(getRoot());
		while(!stack.empty()){
			dummy = stack.pop();
			for(int i=dummy.majorChildrenProducts.size()-1; i>-1;i--)
				stack.push(dummy.majorChildrenProducts.get(i));
			
			if(dummy != getRoot()) {
				for (Product pr : dummy.majorChildrenProducts) {
					tempLine.setLine(dummy.getCenterX(), dummy.getCenterY(), pr.getCenterX(), pr.getCenterY());
					if(tempLine.intersects(visualWindow)){
						runner = pr.madeByProcess.from;
						while(runner != dummy){
							runner.madeByProcess.draw(gr,2);
							//System.out.println("drawing "+runner.madeByProcess.name);
							runner.draw(gr,2);
							runner  = runner.madeByProcess.from;
						}
					}
				}	
				if(visualWindow.contains(dummy.getCenterX(), dummy.getCenterY())) dummy.draw(gr,2);
			}	
			//gr2.fill(visualWindow);
//			gr2.setColor(Color.red);
	//		gr2.fill(new Rectangle2D.Double(zeroX/scale, zeroY/scale, productWidth, productHeight));
		}

		
		
		
		
		while(!stack.empty()){
			dummy = stack.pop();
			for(int i=dummy.usedByProcesses.size()-1; i>-1;i--){
				stack.push(dummy.usedByProcesses.get(i).to);
				dummy.usedByProcesses.get(i).draw(gr,2);
			}
			dummy.draw(gr,2);
		}
		

		
	}

	private void drawCrude(java.awt.Graphics gr){

	}
 	
 	private void drawMetro(java.awt.Graphics gr){

 	}
 	
 	public void setUserPerspective(int userid){
 		userview=userid;
 	}
 	

	public void setWidth(int w){
		if(width != w){
			width=w;
			//redraw = true;
			updateClip();
		}
		
	}
	
	private void updateClip() {
		
		//visualWindow.setBounds((int)(zeroX/scale)-productWidth, (int)(zeroY/scale)-productHeight, (int)(width/scale)+2*productWidth, (int)(height/scale)+2*productHeight);
		//System.out.println(visualWindow.toString());
	//	if(cityToScreenCoordX(totalWidth)>width){
/*			if(cityToScreenCoordX(0)>40) zeroX =-40;
			if(screenToCityCoordX(width-40)>totalWidth) zeroX =totalWidth*scale-width+40;
	//	}
	//	if(cityToScreenCoordY(-totalHeight)>height){
			if(cityToScreenCoordY(0)<height-40) zeroY=-height+40;
			if(screenToCityCoordY(0+40)<-totalHeight) zeroY =-totalHeight*scale-40;
	//	}
	*/	
		
		//if(visualWindow.getX()+visualWindow.width>totalWidth) zeroX = ((int)((totalWidth-visualWindow.getWidth())*scale));
		//if(visualWindow.getY()<-totalHeight) zeroY = ((int)(-totalHeight*scale));
		//if(visualWindow.getY()+visualWindow.height>0) zeroY = ((int)((-totalHeight+visualWindow.getHeight())*scale));
		
		//visualWindow.setBounds((int)(Math.ceil(screenToCityCoordX(-productWidth*scale))), (int)(zeroY/scale-productHeight*scale), (int)(width/scale+8*productWidth*scale), (int)(height/scale+8*productHeight*(scale)));
		visualWindow.setBounds((int)(Math.ceil(screenToCityCoordX(0))), (int)(Math.ceil(screenToCityCoordY(0))), (int)((width)/scale), (int)((height)/scale));
		//System.out.println("new "+visualWindow.toString());
		ELOProcess.activeClip=visualWindow;
		Product.activeClip=visualWindow;
	}

	public void setHeight(int h){
		if(height != h){
			height=h;
			//redraw = true;
			updateClip();
		}
	}
	
	public ArrayList<ArrayList<Double>> getScreenJumpSequences(){
		ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
		long hardScreenEnd = softToHardX((long)screenToCityCoordX(width));
		ArrayList<Double> seqElement;
		seqElement = new ArrayList<Double>();
		seqElement.add((double)0);
		if(jumps.size() == 0 || jumps.get(0).get(0)>hardScreenEnd){
			seqElement.add((double)width);
			result.add(seqElement);
			return result;
		}
		double v;
		for(int i = 0; i<jumps.size();i++){
			v = cityToScreenCoordX(hardToSoftX(jumps.get(i).get(0))); //* quadratic complexity, could be reduced
			if(v>0){
				if(v>width){
					seqElement.add((double)width);
					result.add(seqElement);
					return result;
				}
				seqElement.add(v);    
				result.add(seqElement);
				seqElement = new ArrayList<Double>();
				seqElement.add(v+1);
			}
						
		}
		seqElement.add((double)width);
		result.add(seqElement);
		return result;
	}
	
	public ArrayList<ArrayList<Long>> getJumpTimeSequences(){
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		long hardScreenEnd = softToHardX((long)screenToCityCoordX(width));
		ArrayList<Long> seqElement;
		seqElement = new ArrayList<Long>();
		seqElement.add(getTimeStart());
		if(jumps.size() == 0 || jumps.get(0).get(0)>hardScreenEnd){
			seqElement.add(getTimeEnd());
			result.add(seqElement);
			return result;
		}
		double v;
		for(int i = 0; i<jumps.size();i++){
			
			v = cityToScreenCoordX(hardToSoftX(jumps.get(i).get(0))); //! quadratic complexity, could be reduced
			if(v>0){
				if(v>width){
					seqElement.add(getTimeEnd());
					result.add(seqElement);
					return result;
				}
				seqElement.add(zerotime+((long)timeScale*jumps.get(i).get(0)));    
				result.add(seqElement);
				seqElement = new ArrayList<Long>();
				seqElement.add(zerotime+((long)timeScale*(jumps.get(i).get(0)+jumps.get(i).get(1))));	
			}
						
		}
		seqElement.add(getTimeEnd());
		result.add(seqElement);
		return result;
	}
	
	public void updateJumps(){
		jumps.clear();
		if(timeOrderedProducts.size()>1){
			double last=timeOrderedProducts.get(0).getHardCenterX();
			for (Product pr : timeOrderedProducts) {
				if(scale*(pr.getHardCenterX()-last)>2*jumpPadding){
					ArrayList<Long> newJump = new ArrayList<Long>();
					newJump.add((long)(last+jumpPadding/scale));
					newJump.add((long)(pr.getHardCenterX()-2*jumpPadding/scale-last));
					jumps.add(newJump);
//					if((newJump.get(0)-last) - (pr.getHardCenterX()-newJump.get(0)-newJump.get(1))!= 0 ){
	//					System.out.println("PRUSER "+((newJump.get(0)-last) - (pr.getHardCenterX()-newJump.get(0)-newJump.get(1))));
		//			}
				}
				last = pr.getHardCenterX();
				
			}
		}
		
	}
	

	public void UpdateJumpedCoordinates(){
		long kravina = 0;
		int j=0;
		for(int i = 0; i<jumps.size();i++){
			while (timeOrderedProducts.get(j).getHardCenterX()<jumps.get(i).get(0)){
				timeOrderedProducts.get(j).setCenterX(timeOrderedProducts.get(j).getHardCenterX()-kravina);
				timeOrderedProducts.get(j).madeByProcess.update();
				j++;
			}
			kravina+=jumps.get(i).get(1);
		}
		while (j<timeOrderedProducts.size()){
			timeOrderedProducts.get(j).setCenterX(timeOrderedProducts.get(j).getHardCenterX()-kravina);
			timeOrderedProducts.get(j).madeByProcess.update();
			j++;
		}	
	}
	
	public void jumpZero(){
		long ffer = 0;
		for(int i = 0; i<jumps.size();i++){
			if(zeroX + zeroXdelta<jumps.get(i).get(0)){
				setZeroX(zeroX + zeroXdelta - ffer);
				zeroXdelta = ffer;
				return;
			}
			if(zeroX + zeroXdelta<jumps.get(i).get(0)+jumps.get(i).get(1)){
				setZeroX(jumps.get(i).get(0) - ffer);
				zeroXdelta = ffer;
				return;
			}

			ffer += jumps.get(i).get(1);
		}
	}
	
	public long softToHardX(long px){
		long sum = 0;
		for(int i=0; i<jumps.size();i++){
			if(px<jumps.get(i).get(0)-sum) return (px+sum);
			sum += jumps.get(i).get(1);
		}
		return (px + sum);
	}
	
	public long hardToSoftX(long px){
		long sum = 0;
		for(int i=0; i<jumps.size();i++){
			if(px<jumps.get(i).get(0)) return (px-sum);
			if(px<jumps.get(i).get(0)+jumps.get(i).get(1)) return (jumps.get(i).get(0)-sum);
			sum += jumps.get(i).get(1);
		}
		
		return (px - sum);
	}
/*	public void updateCoordsWithJumps(){
		int i=0,j=0;
		
		boolean changedZeroX = false;
		long buffer=0; //sum of all jumps; will subtract from coordinates
		while(i<timeOrderedProducts.size() && j<jumps.size()){
			if(timeOrderedProducts.get(i).getHardCenterX()>Math.round(jumps.get(j).get(0))){
				if(!changedZeroX && (zeroX + zeroXdelta<jumps.get(j).get(0))){
					setZeroX(zeroX + zeroXdelta - buffer);
					zeroXdelta = buffer;
					changedZeroX = true;
				}
				buffer = buffer + Math.round(jumps.get(j).get(1));
				j++;
				System.out.println(buffer);
			}
			timeOrderedProducts.get(i).setCenterX(timeOrderedProducts.get(i).getHardCenterX()-buffer);
			timeOrderedProducts.get(i).madeByProcess.update();
			i++;
		}
	}
	*/
/*	void updateJumpsOld(){
		   Comparator<Product> comp = new ProductComparator();
		   PriorityQueue<Product> q = new PriorityQueue<Product>(10, comp );
		   double JumpsSum = 0;
		   double paddings = 0;
		   
		   if(getRoot() != null){
			   q.add(getRoot());
			   boolean end = false;
			   long maxX = 0; //maximum X coordinate that is shown
			   double maxScreenX = 0;
			   while(!end && !q.isEmpty()){
				   double goo = cityToScreenCoordX(q.peek().getCornerX());
				   if(goo-JumpsSum+paddings > width && maxScreenX+jumpPadding<width) { //add Jump from maxX+jumpPadding to q.peek -padding, if(Jump.width<0 end=true) 
					   Point2D newJump = new Point2D.Double(maxX+jumpPadding/scale, q.peek().getCornerX()-jumpPadding/scale);
					   if(newJump.getX()-newJump.getY()>=0){ 
						   end = true;
					   }
					   else
					   {
						   jumps.add(newJump);
						   paddings+=2*jumpPadding;
						   JumpsSum+=(newJump.getY()-newJump.getX())*scale;
				
					   }
					   				  
				   }
				   maxX = q.peek().getCornerX();
				   for (ELOProcess pr : q.poll().usedByProcesses) {
					   if(cityToScreenCoordX(pr.getTo().getCornerX())<=0){
						   q.add(pr.getTo());
					   }
				   }
			   }
		   }
	   }
*/
	public long getTimeStart() {
		return zerotime+((long)timeScale*softToHardX((long)screenToCityCoordX(0)));
	}
	public long getTimeEnd() {
		return zerotime+((long)timeScale*softToHardX((long)screenToCityCoordX(width)));
	}

	public void setRoot(Product root) {
		this.root = root;
	}

	public Product getRoot() {
		return root;
	}

	public ArrayList<ArrayList<Long>> getJumps(){
		return jumps;
	}
	
}
