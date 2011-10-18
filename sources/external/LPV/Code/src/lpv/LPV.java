package lpv;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.Callback.Command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.BorderFactory;



public class LPV {

	private static int height=400;
	private static int productHeight=50;
	private static int productWidth=50;
	private static int width=550;
	private lpv.City activeCity;
	private static String[] bannedActionsList = { "tool_quit", "tool_started", "tool_lost_focus", "tool_got_focus"};// maybe also "tool_opened", "tool_closed"
	private ArrayList<lpv.City> cities;
	private lpv.City defaultCity;
	private ArrayList<String> userNames;
	private LPVPanel canvas;
	private TupleSpace ts;
	private int seqNo;
	private LPVCallback cb;
	private ArrayList<LPVEventListener> _listeners = new ArrayList<LPVEventListener>();


	public LPV(){
		/**
		 * Constructor
		 */
		
		
		cities = new ArrayList<City>();
        defaultCity = new City(width,height,productWidth,productHeight);
        activeCity = defaultCity;
        userNames = new ArrayList<String>();
        
        canvas = new LPVPanel(activeCity);
		canvas.setBorder(BorderFactory.createLineBorder(Color.black));

	}
	private void addIntoCity(Long time, String mission, String user,String tool, String type, String elouri, String elo_uri, String old_uri){
		for (String listElement : bannedActionsList) {
			if(type.equals(listElement)) {
				return;
			}
		}
	
		if((elo_uri != null) && (old_uri != null) && type.equals("elo_saved")){
			 if(elo_uri.equals(old_uri)) {
				 System.out.println(time+": on save old_url=elo_url "+elo_uri);
		
			 }
	    	 
			 if(!getUserNames().contains(user)) {
				 getUserNames().add(user);
				 City city = new City(width,height,productWidth,productHeight);
				 cities.add(city);
			 }
			 cities.get(getUserNames().indexOf(user)).addProduct(elo_uri,mission,type);
			 cities.get(getUserNames().indexOf(user)).addProcess(old_uri, elo_uri, tool,user,time);
	
		
	     } 
		else {
	    	 if(type.equals("elo_saved"))System.out.println(time+": elo_url is null on elo_saved");
	    	 //if(!type.equals("elo_saved") &&  elouri != null && !elouri.equalsIgnoreCase("n/a") && user != null)
	    	 if(elouri != null && !elouri.equalsIgnoreCase("n/a") && user != null){
	    		if(!getUserNames().contains(user)) {
					 getUserNames().add(user);
					 City city = new City(width,height,productWidth,productHeight);
					 cities.add(city);
				 }
				 cities.get(getUserNames().indexOf(user)).addProduct(elouri,mission,type);
				 cities.get(getUserNames().indexOf(user)).addProcess(elouri, elouri, tool,user,time);
	    	 }
	     }
	    	 
	}

	
	private void addIntoCityOnTheFly(Long time, String mission, String user,String tool, String type, String elouri, String elo_uri, String old_uri){
		for (String listElement : bannedActionsList) {
			if(type.equals(listElement)) {
				return;
			}
		}
	
		if((elo_uri != null) && (old_uri != null) && type.equals("elo_saved")){
			 if(elo_uri.equals(old_uri)) {
				 System.out.println(time+": on save old_url=elo_url "+elo_uri);
		
			 }
	    	 
			 if(!getUserNames().contains(user)) {
				 getUserNames().add(user);
				 City city = new City(width,height,productWidth,productHeight);
				 
				 cities.add(city);
				 city.addProduct(elouri,mission,type);
				 city.addProcess(elouri, elouri, tool,user,time);
				 city.organize();
				 fireEvent(user,getUserNames().indexOf(user));
				 return;
			 }
			 cities.get(getUserNames().indexOf(user)).addProduct(elo_uri,mission,type);
			 cities.get(getUserNames().indexOf(user)).addProcessAndOrganize(old_uri, elo_uri, tool,user,time);
			 cities.get(getUserNames().indexOf(user)).jumpZero();
			 canvas.updateScales();
		
	     } 
		else {
	    	 if(type.equals("elo_saved"))System.out.println(time+": elo_url is null on elo_saved");
	    	 //if(!type.equals("elo_saved") &&  elouri != null && !elouri.equalsIgnoreCase("n/a") && user != null)
	    	 if(elouri != null && !elouri.equalsIgnoreCase("n/a") && user != null){
	    		if(!getUserNames().contains(user)) {
					 getUserNames().add(user);
					 City city = new City(width,height,productWidth,productHeight);
					 cities.add(city);
					 city.addProduct(elouri,mission,type);
					 city.addProcess(elouri, elouri, tool,user,time);
					 city.organize();
					 fireEvent(user,getUserNames().indexOf(user));
					 return;
				 }
				 cities.get(getUserNames().indexOf(user)).addProduct(elouri,mission,type);
				 cities.get(getUserNames().indexOf(user)).addProcessAndOrganize(elouri, elouri, tool,user,time);
				 cities.get(getUserNames().indexOf(user)).updateJumps();
				 cities.get(getUserNames().indexOf(user)).UpdateJumpedCoordinates();
	    	 }
	     }

	    	 
	}

	private void readSQLSpaces(String url,int port){
		try {
			System.out.println("SQLSpaces connecting");
			ts = new TupleSpace(url, port);
				System.out.println("SQLSpaces connected");
				Tuple actionTemplate = new Tuple("action",Field.createWildCardField());
		    ArrayList<String[]> allData = new ArrayList<String[]>();
		    for (Tuple t : ts.readAll(actionTemplate, 100)) {
		        
		    	String[] l = new String[7];
		    	l[0] = ((Long) t.getField(2).getValue()).toString();
		    	if(((String)t.getField(3).getValue()).equals("elo_saved") || ((String)t.getField(3).getValue()).equals("elo_save")){
		    	}
		    	for(int i = 3; i<7;i++){
		    		l[i-2] = (String) t.getField(i).getValue();
		        	if(l[i-2] == "") l[i-2] = null;
		        }
		    	l[5] = (String) t.getField(8).getValue();
		    	int j = 9;
		    	boolean end = t.numberOfFields()<10;
		    	while(!end){
		    		String[] k = ((String) t.getField(j).getValue()).split("=");
		    		if(k.length>1){
		    			if(k[0].equals("elo_uri")){
		    				l[6] = k[1];
		    			}
		    		}
		    		j++;
		    		if(j>=t.numberOfFields()) end = true;
		    	}
		    	allData.add(l);
		    }
		    //sort allData
		    Collections.sort(allData, new AllDataComparator());
		    for (String[] l : allData){
		        if(l.length>6){
		        	addIntoCity(Long.parseLong(l[0]), l[4], l[2], l[3], l[1], l[5], l[5], l[6]);
		        }
		        else {
		        	if(l.length == 6)
		        		addIntoCity(Long.parseLong(l[0]), l[4], l[2], l[3], l[1], l[5], l[5], null);
		        	if(l.length < 6)
		        		System.out.println("input error: not enough fields");
		        }
		    }
		    cb = new LPVCallback(this);
			seqNo = ts.eventRegister(Command.WRITE, actionTemplate, cb, false);
    
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		if(cities.size()>0) System.out.println(cities.get(0).productTypeDict);
	}
	
	public void disconnectSQLSpaces(){
		if(ts == null) return;
		try {
			ts.eventDeRegister(seqNo);
			System.out.println("SQLSpaces disconnected");
		} catch (TupleSpaceException e) {
			
			e.printStackTrace();
		}
	}
	public LPVPanel getCanvas() {
		/**
		 * Returns the an ancestor of JPanel where the visualisation takes place.
		 */
		return canvas;
	}
	private void resetCities() {
		activeCity = defaultCity;
		cities.clear();
		getUserNames().clear();
		
	}
	public void connectSQLSpaces(String uurl, int port) {
		resetCities();
		readSQLSpaces(uurl, port);
		for (City city : cities) {
			city.organize();	
		}
		
	}

	public ArrayList<String> getUserNames() {
		/**
		 * Returns list of user names in database
		 */
		return userNames;
	}
	public void setActiveUser(int id) {
		/**
		 * Selects which user is to be shown on the visualisation panel. The users are indexed as in the result of getUserNames()
		 */
		activeCity = cities.get(id);
        
        activeCity.setUserPerspective(1);
  //      activeCity.setHeight(Math.max(activeCity.get, b)canvas.getHeight());
    //    activeCity.setWidth(canvas.getWidth());
		canvas.setCity(activeCity);
		canvas.renew();
	}
	public int getActiveWidth() {
		/**
		 * Returns the preferred width of the canvas
		 */
		return activeCity.width;
	}
	public int getActiveHeight() {
		/**
		 * Returns the preferred height of the canvas
		 */
		return activeCity.height;
	}
	protected void addOnTheFly(String[] l) {
		if(l.length>6){
        	addIntoCityOnTheFly(Long.parseLong(l[0]), l[4], l[2], l[3], l[1], l[5], l[5], l[6]);
        }
        else {
        	if(l.length == 6)
        		addIntoCityOnTheFly(Long.parseLong(l[0]), l[4], l[2], l[3], l[1], l[5], l[5], null);
        	if(l.length < 6)
        		System.out.println("input error: not enough fields");
        }
		canvas.repaint();
	}
	
	public synchronized void addEventListener(LPVEventListener listener)  {
		    _listeners.add(listener);
		  }

	public synchronized void removeEventListener(LPVEventListener listener)   {
		    _listeners.remove(listener);
		  }
	
	private synchronized void fireEvent(String username, int index) {
		    LPVEvent event = new LPVEvent(this);
		    event.setNewUserName(username);
		    event.setNewUserIndex(index);
		    Iterator<LPVEventListener> i = _listeners.iterator();
		    while(i.hasNext())  {
		      i.next().handleLPVEvent(event);
		    }
		  }


}
