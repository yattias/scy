import javax.swing.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import village.City;


public class MyFrame extends JFrame 
implements ActionListener, ItemListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2572319814082883076L;
	static int width=550;
	static int height=400;
	static int productWidth=50;
	static int productHeight=50;
//	private final static String newline = "\n";
	java.sql.Connection conn;
	ArrayList<village.City> cities;
	village.City activeCity,defaultCity;
	MyPanel canvas;
	 
	JButton butt,butt2 ;
	JTextField textField,textField2;
	JComboBox actors;
	ArrayList<String> userNames;
	String[] bannedActionsList = { "tool_quit", "tool_started", "tool_lost_focus", "tool_got_focus"};// maybe also "tool_opened", "tool_closed"
	//PriorityQueue<String> bannedActionsQueue;
	public MyFrame(String title){
		super(title);
		//*************** Add and configure internal elements (City...)
	/*	bannedActionsQueue = new PriorityQueue<String>();
		for (String b : bannedActionsList) {
			bannedActionsQueue.add(b);
		}*/
		cities = new ArrayList<City>();
        defaultCity = new City(width,height,productWidth,productHeight);
        activeCity = defaultCity;
        userNames = new ArrayList<String>();
        //*************** END Add and configure internal elements (City...)
		//*************** Add all GUI elements
      //setLayout(new FlowLayout());
        //Top Panel
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
		//-Button
        butt = new JButton ( "Load SQL" ) ; 
//		p.add(butt);
		//-Text Field
		textField = new JTextField("mysql://127.0.0.1/dummy?user=dumdum&password=password",33);
//		p.add(textField);
		//-Combo Box
		actors = new JComboBox(); 
		MutableComboBoxModel model = (MutableComboBoxModel) actors.getModel();
		model.addElement("No User");
		actors.addItemListener(this);
		p.add(actors);
		//-Button 2
		//butt2 = new JButton ( "Load XML" ) ; 
		butt2 = new JButton ( "Load CSV" ) ;
		p.add(butt2);
		//-Text Field 2
		//textField2 = new JTextField("c:\\data\\doc\\infomedia.uib.no\\Scy project\\logs\\actionLogsUT2(1).xml",33);
		//textField2 = new JTextField("actionLogsUT2(2).xml",33);
		//textField2 = new JTextField("c:\\data\\doc\\infomedia.uib.no\\Scy project\\logs\\cyprus_lpv_3.csv",33);
		textField2 = new JTextField("cyprus_lpv_2.csv",33);
		p.add(textField2);
		//Top Panel add
		add(p,BorderLayout.PAGE_START);
		//Panel for drawing - canvas
		canvas = new MyPanel(activeCity);
		canvas.setBorder(BorderFactory.createLineBorder(Color.black));
		//canvas.setPreferredSize(new Dimension(width,height));
        add(canvas,BorderLayout.CENTER);
        //Set up Frame and GUI properties
        pack();
        setVisible(true);
        //*************** END Add all GUI elements
        //*************** Add action listeners
		butt.addActionListener(this) ;  
		textField.addActionListener(this);
		butt2.addActionListener(this) ;  
		textField2.addActionListener(this);
		
		//*************** END Add action listeners
     }
	
	  
	 /*public void windowClosed(WindowEvent e) {} 
	 public void windowDeactivated(WindowEvent e) {} 
	 public void windowActivated(WindowEvent e) {} 
	 public void windowDeiconified(WindowEvent e) {} 
	 public void windowIconified(WindowEvent e) {} 
	 public void windowOpened(WindowEvent e) {}
	 public void windowClosing(WindowEvent e) { 
		    setVisible(false); 
		    dispose(); 
		    System.exit(0); 
		  } */
	
	public void itemStateChanged(ItemEvent e)
    {
		
        int result = actors.getSelectedIndex();
        if(result>-1){
        	activeCity = cities.get(result);
        
        activeCity.setUserPerspective(1);
  //      activeCity.setHeight(Math.max(activeCity.get, b)canvas.getHeight());
    //    activeCity.setWidth(canvas.getWidth());
		canvas.setCity(activeCity);
		canvas.renew();
		
		if((getExtendedState() & MAXIMIZED_BOTH) == 0)	setSize(new Dimension(Math.max(this.getWidth(), Math.max(activeCity.width,butt.getWidth()+actors.getWidth()+textField.getWidth()+40)), Math.max(activeCity.height+butt.getHeight(),this.getHeight())));
        }
	
       // validate();
      //  repaint();
    }
	
	public void actionPerformed ( ActionEvent ev )   
	{	
		Object src = ev.getSource();
		activeCity = defaultCity;
		cities.clear();
		userNames.clear();
		
		
		if (src == butt || src == textField ){
			//String uurl = textField.getText();
			textField.selectAll();
			//readSQL(uurl);
		}
		if (src == butt2 || src == textField2 ){
			String uurl = textField2.getText();
			textField2.selectAll();
			//readXML(uurl);
			readCSV(uurl);
		}
		
		for (City city : cities) {
			city.organize();	
		}
		
		actors.removeAllItems();
		
		for(int i=0; i<userNames.size(); i++)
				actors.addItem(userNames.get(i));
	}
	
	 void readXML(String xmlUrl) {
		try {
		     SAXParserFactory factory = SAXParserFactory.newInstance();
		     SAXParser saxParser = factory.newSAXParser();
		 
		     DefaultHandler handler = new DefaultHandler() {
		 

		    	 boolean bUser= false;
		    	 boolean bTime = false;
		    	 boolean bType= false;
		    	 boolean bContext = false;
		    	 boolean bMission = false;
		    	 boolean bTool = false;
		    	 boolean bProperties = false;
		    	 boolean bOld_uri = false;
		    	 boolean bElo_uri = false;
		    	 boolean bEloUri = false;
		     

		    	 String tool = null, user = null, old_uri = null, elo_uri=null, elouri = null,mission = null, type = null;
		    	 long time = 0;
		 
		    	 public void startElement(String uri, String localName,
		 		        String qName, Attributes attributes) throws SAXException {

		 		        if (qName.equalsIgnoreCase("user")) {
		 		        	bUser = true;
		 		        }        
		 		        if (qName.equalsIgnoreCase("time")) {
		 		        	bTime = true;
		 		        }        
		 		        if (qName.equalsIgnoreCase("type")) {
		 		        	bType = true;
		 		        }        
		 		        if (qName.equalsIgnoreCase("context")) {
		 		        	bContext = true;
		 		        }        
		 		        if (qName.equalsIgnoreCase("tool")) {
		 		        	bTool = true;
		 		        }        
		 		        if (qName.equalsIgnoreCase("mission")) {
		 		        	bMission = true;
		 		        }        
		 		        if (qName.equalsIgnoreCase("properties")) {
		 		        	bProperties = true;
		 		        }        
		 		        if (qName.equalsIgnoreCase("old_uri")) {
		 		        	bOld_uri = true;
		 		        }        
		 		        if (qName.equalsIgnoreCase("elo_uri")) {
		 		        	bElo_uri = true;
		 		        } 
		 		       if (qName.equalsIgnoreCase("elouri")) {
		 		        	bEloUri = true;
		 		        }
		 		 
		    	 }
		 		 
		 		     public void endElement(String uri, String localName,String qName)
		 		          throws SAXException {
		 		    	 
		 		    	// System.out.println("End Element :" + qName);

		 		    	 if (qName.equalsIgnoreCase("action")) {
		 		    		 
		 		    			 Calendar C = Calendar.getInstance();
		 		    			 C.setTimeInMillis(time);
		 		    			 C.setTimeZone(TimeZone.getDefault());
		 		    			 //String ts = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(C.getTime());
		 		    			 //System.out.println(user+" "+ts+" "+tool+" "+elo_uri+" "+old_uri);
		 		                 if((elo_uri != null) && (old_uri != null) && type.equals("elo_saved")){
			 		    			 if(elo_uri.equals(old_uri)) {
			 		    				 System.out.println(time+": elo_uri "+elo_uri+" old_uri "+old_uri);
			 		    		
			 		    			 }
		 		                	 
			 		    			 if(!userNames.contains(user)) {
			 		    				 userNames.add(user);
			 		    				 City city = new City(width,height,productWidth,productHeight);
			 		    				 cities.add(city);
			 		    			 }
			 		    			//System.out.println(elo_uri+" "+mission+" "+type);
			 		    			 cities.get(userNames.indexOf(user)).addProduct(elo_uri,mission,type);
			 		    			 cities.get(userNames.indexOf(user)).addProcess(old_uri, elo_uri, tool,user,time);

			 		    		
		 		                 } else {
		 		                	 if(type.equals("elo_saved"))System.out.println(time+": elo_uri is null on elo_saved");
		 		                	 if(!type.equals("elo_saved") &&  elouri != null && !elouri.equalsIgnoreCase("n/a") && user != null){
		 		                		// System.out.println("adding minor actions:" +type+ " elouri:"+elouri);
		 		                		if(!userNames.contains(user)) {
				 		    				 userNames.add(user);
				 		    				 City city = new City(width,height,productWidth,productHeight);
				 		    				 cities.add(city);
				 		    			 }
		 		                		//System.out.println(elo_uri+" "+mission+" "+type);
				 		    			 cities.get(userNames.indexOf(user)).addProduct(elouri,mission,type);
				 		    			 cities.get(userNames.indexOf(user)).addProcess(elouri, elouri, tool,user,time);

		 		                	 }
		 		                 }
		 		    		 


		 		    		 tool = null; 
		 		    		 user = null; 
		 		    		 old_uri = null; 
		 		    		 elo_uri=null;
		 		    		 elouri=null;
		 		    		 mission = null;
		 		    		 type=null;
		 		    		 time = 0;
		 			     }
		 		    	 if (qName.equalsIgnoreCase("user")) {
		 			        	bUser = false;
		 			     }        
		 		    	 if (qName.equalsIgnoreCase("time")) {
		 			        	bTime = false;
		 			     }        
		 		    	 if (qName.equalsIgnoreCase("type")) {
		 			        	bType = false;
		 			     }        
		 			     if (qName.equalsIgnoreCase("context")) {
		 			        	bContext = false;
		 			     }        
		 			     if (qName.equalsIgnoreCase("mission")) {
		 			        	bMission = false;
		 			     }        
		 			     if (qName.equalsIgnoreCase("tool")) {
		 			        	bTool = false;
		 			     }        
		 			     if (qName.equalsIgnoreCase("properties")) {
		 			        	bProperties = false;
		 			     }        
		 			     if (qName.equalsIgnoreCase("old_uri")) {
		 			        	bOld_uri = false;
		 			     }        
		 			     if (qName.equalsIgnoreCase("elo_uri")) {
		 			        	bElo_uri = false;
		 			     }   
		 			    if (qName.equalsIgnoreCase("elouri")) {
	 			        	bEloUri = false;
	 			     }   
		 		     }
		 
				     public void characters(char ch[], int start, int length)
			         throws SAXException {

			    	 
				         if (bUser) {
				        	 user = new String(ch, start, length);
				          }
				 
				          if (bTime) {
				        	 // time = Integer.parseInt(new String(ch, start, length));
				        	  time = Long.parseLong(new String(ch, start, length));
				          }
				 
				          if (bType) {
				        	  type = new String(ch, start, length);
				          }
				          if (bTool) {
				              tool = new String(ch, start, length);
				              
				           }
				 
				          if (bOld_uri && bProperties) {
				              old_uri = new String(ch, start, length);
				           }

				          if (bMission ) {
				              mission = new String(ch, start, length);
				           }

				          if (bElo_uri  && bProperties) {
				        	  elo_uri = new String(ch, start, length);		           
				          }
				          if (bEloUri  && bContext) {
				        	  elouri = new String(ch, start, length);		           
				          }
			        }
		      };
		 
		      File file = new File(xmlUrl);
		      InputStream inputStream= new FileInputStream(file);
		      Reader reader = new InputStreamReader(inputStream,"UTF-8");
		      InputSource is = new InputSource(reader);
		      is.setEncoding("UTF-8");
		      saxParser.parse(is, handler);
		      System.out.println("Finished parsing xml file");
		     //System.out.println(city.productTypeDict.toString());
		} catch (Exception e) {
		      e.printStackTrace();
		}

		
	}

	public void readCSV(String csvUrl){
		try {
		    BufferedReader in = new BufferedReader(new FileReader(csvUrl));
		    String str;
		    while ((str = in.readLine()) != null) {
		        String[] l = str.split(",");
		        for(int i = 0; i<l.length;i++){
		        	if(l[i] == "") l[i] = null;
		        }
		        if(l.length>6){
		        	addIntoCity(Long.parseLong(l[0]), l[1], l[2], l[3], l[4], l[5], l[5], l[6]);
		        }
		        else {
		        	if(l.length == 6)
		        		addIntoCity(Long.parseLong(l[0]), l[1], l[2], l[3], l[4], l[5], l[5], null);
		        	if(l.length < 6)
		        		System.out.println("cvs input error: not enough fields");
		        }
		    }
		    in.close();
		} catch (IOException e) {
		}
		System.out.println(cities.get(0).productTypeDict);
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
        	 
			 if(!userNames.contains(user)) {
				 userNames.add(user);
				 City city = new City(width,height,productWidth,productHeight);
				 cities.add(city);
			 }
			 cities.get(userNames.indexOf(user)).addProduct(elo_uri,mission,type);
			 cities.get(userNames.indexOf(user)).addProcess(old_uri, elo_uri, tool,user,time);

		
         } 
		else {
        	 if(type.equals("elo_saved"))System.out.println(time+": elo_url is null on elo_saved");
        	 //if(!type.equals("elo_saved") &&  elouri != null && !elouri.equalsIgnoreCase("n/a") && user != null)
        	 if(elouri != null && !elouri.equalsIgnoreCase("n/a") && user != null){
        		if(!userNames.contains(user)) {
    				 userNames.add(user);
    				 City city = new City(width,height,productWidth,productHeight);
    				 cities.add(city);
    			 }
    			 cities.get(userNames.indexOf(user)).addProduct(elouri,mission,type);
    			 cities.get(userNames.indexOf(user)).addProcess(elouri, elouri, tool,user,time);
        	 }
         }
        	 
	}
	
/*	public  void readSQL(String sqlUrl) {
		//java.sql.Connection conn=null;
		conn=connect("jdbc:"+sqlUrl);
        int i=0;
        
		try {
            java.sql.Statement s = conn.createStatement();
            java.sql.ResultSet r = s.executeQuery
            ("SELECT who, tool, source, product, timestamp FROM history ORDER BY timestamp ASC");
            while(r.next()) {
            	i++;
                city.addProduct(r.getInt("product"));
            	city.addProcess(r.getInt("source"), r.getInt("product"), r.getInt("tool"),r.getInt("who"),r.getInt("timestamp"));
            }
		}
		catch (Exception e) {
            System.out.println("X: " + e);
           // System.exit(0);
		}
		
		System.out.println(i+" data succesfully read");
		disconnect(conn);

	}
	private  java.sql.Connection connect(String url){
		java.sql.Connection conn = null;
        try {

        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	//String url = "jdbc:mysql://127.0.0.1/";
        	conn = DriverManager.getConnection(url);
        }
        catch (Exception e) {
                System.out.println(e);
                System.exit(0);
        }
        
        	System.out.println("Connection established");
        	return conn;
	}

	private  void disconnect(java.sql.Connection conn){
		try {
			if(conn != null)
			conn.close();
			System.out.println("Closing connection"); 
		}
		catch (Exception e){
    		System.out.println("Connection not closed");
    		System.out.println(e);
    		System.exit(0);
		}
	}
	*/
	public void init()

	{
		/*setSize(width, height);	
		//System.out.println("haha");
		butt.addActionListener ( this ) ;  
		textField.addActionListener(this);
		add ( butt ) ;
		add (textField);
		
		actors = new JComboBox(); 
		MutableComboBoxModel model = (MutableComboBoxModel) actors.getModel();
		model.addElement("No User");
		actors.addItemListener(this);
		add(actors);    
		
		validate () ;*/
		
		
	}

	
}
