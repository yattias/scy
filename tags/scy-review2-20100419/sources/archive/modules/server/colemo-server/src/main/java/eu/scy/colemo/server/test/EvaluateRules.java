/*
 * Created on 27.jul.2005
 *
 * 
 */
package eu.scy.colemo.server.test;
import java.io.File;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import eu.scy.colemo.server.network.Connection;
import eu.scy.colemo.server.network.Server;
import eu.scy.colemo.server.network.UserInfo;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import eu.scy.colemo.server.agent.AgentMessage;
import java.util.Vector;

/**
 * @author Øystein
 *
 * 
 */
public class EvaluateRules implements Runnable{
    
    private Document userInfo;
    private Document agentInfo;
    private Server server;
    private Thread t;
    
    public EvaluateRules(Server server){
        this.server=server;
        t = new Thread(this);
        t.start();
    }
    
    public void run() {
		/*for(;;) {
			try {
			    setXmlDocuments();
			    
			    Thread.sleep(getVariable("//eu.scy.colemo.server.agent/check_interval",1000,agentInfo));

				evaluateRules();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch (DocumentException de){
			    de.printStackTrace();
			}
		}*/
	}
    
    public Document readXMLFile(File xmlFile) throws DocumentException{
		SAXReader reader;
		reader=new SAXReader();
		return reader.read(xmlFile);
	}
    public void evaluateRules(){
        //sjekker reglene
        idle();
        rule1();  
        rule2();//UFERDIG
    }
    
    public void rule2(){
        /**
         * Ikke sjekk før bruker har vært online 'minimumOnlineTime' min.
         * Må være minst 'minimumTimeBetween' mellom hver melding.
         * Må ha gjort mindre enn 'limitNotEnough' % av sin andel av antall poeng totalt ELLER
         * Må ha gjort mer enn 'limitTooMuch' % av sin andel av antall poeng
         * Ikke sjekk før bruker har min. 'minimumPoints' points.
         */
        
        String path;
        List list;
        
        int minimumOnlineTime=getVariable("//eu.scy.colemo.server.agent/compared_to_rest/minimum_online_time",60000,agentInfo);
        int minimumTimeBetween=getVariable("//eu.scy.colemo.server.agent/compared_to_rest/minimum_time_between",60000,agentInfo);
        float limitNotEnough=getVariable("//eu.scy.colemo.server.agent/compared_to_rest/limit_not_enough",1,agentInfo);
        float limitTooMuch=getVariable("//eu.scy.colemo.server.agent/compared_to_rest/limit_too_much",1,agentInfo);
        int minimumPoints=getVariable("//eu.scy.colemo.server.agent/compared_to_rest/minimum_points",1,agentInfo);
        
        path="//user_information/user/name";
        list = userInfo.selectNodes(path);
        
        for (Iterator it = list.iterator(); it.hasNext(); ) {
  	        Element user =(Element)it.next();
  	        String name = user.getStringValue();
	        
  	        Element loggedOn = user.getParent().element("time_logged_on");
  	        long loggOnTime =new Long(loggedOn.getStringValue()).longValue();
  	        long onlineTime =System.currentTimeMillis()-loggOnTime;
  	        
  	        Element last = user.getParent().element("lastNotEnoughComparedToRest");
  	        long lastMessage = new Long(last.getStringValue()).longValue();
           	            
  	        if(onlineTime>minimumOnlineTime){
  	            
  	            Element totalPoints = user.getParent().element("totalPoints");
    	        String points = totalPoints.getStringValue();
    	        float point=new Integer(points).longValue();
    	        
    	        float totalPointsAll = getVariable("//user_information/allUsers/totalPointsAll",1,userInfo);
    	           	             
    	        if(point>minimumPoints){
    	            if(System.currentTimeMillis()-lastMessage>minimumTimeBetween){
    	             	             
    	                if((totalPointsAll/numberOfUsers())*(limitNotEnough/100)>point){
    	                    //Send melding til bruker
        	                Element e =totalPoints.getParent().element("ip");
        	                sendMessage(e.getStringValue(),"DU GJØR IKKE NOK SAMMENLIGNET MED RESTEN!",true,"notEnoughComparedToRest");
    	                }
    	                if((totalPointsAll/numberOfUsers())*((limitTooMuch+100)/100)<point){
    	                    Element e =totalPoints.getParent().element("ip");
    	                    sendMessage(e.getStringValue(),"DU GJØR FOR MYE SAMMENLIGNET MED RESTEN!",true,"tooMuchComparedToRest");
    	                }
    	            }  
    	        }
  	        }
        }        
    }
    
    public void rule1(){
        /**
         * Bruker må "produsere" min. 'pointsPrMinute' points pr minutt.
         * Ikke sjekk før bruker har min. 'minimumPoints' points.
         * Ikke sjekk før bruker har vært online 'minimumOnlineTime' min.
         * Må være minst 'minimumTimeBetween' mellom hver melding.
         */
        
        String path;
        List list;
        
        int pointsPrMinute=getVariable("//eu.scy.colemo.server.agent/points_pr_minute/points",1,agentInfo);
        int minimumOnlineTime=getVariable("//eu.scy.colemo.server.agent/points_pr_minute/minimum_online_time",60000,agentInfo);
        int minimumPoints=getVariable("//eu.scy.colemo.server.agent/points_pr_minute/minimum_points",1,agentInfo);
        int minimumTimeBetween=getVariable("//eu.scy.colemo.server.agent/points_pr_minute/minimum_time_between",60000,agentInfo);
        
        
  	   	//Sjekk hvor lenge bruker har vært online
  	    path="//user_information/user/name";
  	      
  	    list = userInfo.selectNodes(path);
  	    for (Iterator it = list.iterator(); it.hasNext(); ) {
  	        Element user =(Element)it.next();
  	        String name = user.getStringValue();
	        
  	        Element loggedOn = user.getParent().element("time_logged_on");
  	        long loggOnTime =new Long(loggedOn.getStringValue()).longValue();
  	        long onlineTime =System.currentTimeMillis()-loggOnTime;
  	        
  	        Element last = user.getParent().element("lastNotEnoughPrMinute");
  	        long lastMessage = new Long(last.getStringValue()).longValue();
           	            
  	        if(onlineTime>minimumOnlineTime){
  	            Element totalPoints = user.getParent().element("totalPoints");
    	        String points = totalPoints.getStringValue();
    	        long point=new Integer(points).longValue();
    	             
    	        if(point>minimumPoints){
    	            if(onlineTime/point>60000/pointsPrMinute && System.currentTimeMillis()-lastMessage>minimumTimeBetween){  
    	                //Send melding til bruker
    	                Element e =totalPoints.getParent().element("ip");
    	  	            sendMessage(e.getStringValue(),"DU GJØR IKKE NOK PR MINUTT!",true,"notEnoughPrMinute");
    	            }
    	        }
  	        }
        }        
    }
    
    public void idle(){
        String path;
        List list;
        
        int maxIdleTime=getVariable("//eu.scy.colemo.server.agent/idle_user/time",1,agentInfo);
  	   
  	    //Sjekk om noen har vært idle lenger en idletime
  	    path="//user_information/user/name";
  	    
  	    list = userInfo.selectNodes(path);
  	    
  	    for (Iterator iter = list.iterator();iter.hasNext();){
  	        Element element =(Element)iter.next();
  	        String name = element.getStringValue();
  	        Element e;
  	        
  	        long lastIdle =new Long(element.getParent().element("lastIdleMessage").getStringValue()).longValue();
  	        long lastAction =new Long(element.getParent().element("lastAction").getStringValue()).longValue();
  	        
  	        if(lastAction>lastIdle){
  	            e = element.getParent().element("lastAction");
  	        }
  	        else{
	  	        e = element.getParent().element("lastIdleMessage");
	  	    }
  	        
  	        long last = new Long(e.getStringValue()).longValue();
  	        
  	        boolean idle =checkTimes(last,System.currentTimeMillis(),maxIdleTime);
  	        		
  	        if(idle){
  	            //Finne ip addressen
  	            e =element.getParent().element("ip");
  	       
  	            //send til klient (se eu.scy.colemo.server.agent.java)
  	            //har ip, finn name, ligger i userInfo på server
  	            sendMessage(e.getStringValue(),"YOU ARE IDLE!",true,"idleMessages");
  	            
  	        }
  	    }    
    }
   
    
    public void setXmlDocuments() throws DocumentException{
        userInfo=server.getUserDoc();
        //agentInfo = readXMLFile(new File("agent.xml"));
    }
    
    public boolean checkTimes(long last, long current, int inc){
        long maxIdleTime = inc*60*1000;
        if(current-last>maxIdleTime){
            return true;
        }
        return false;
        
    }
    
    public int getVariable(String path,int multiplier,Document doc){
        List list = doc.selectNodes(path);
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
  	        Element element = (Element) iter.next();
  	        String result = element.getStringValue();
  	        int variable = Integer.parseInt(result);
  	        return variable*multiplier;
        }
        return 0;
    }
    
    public void sendMessage(String ipString,String messageString,boolean popup,String type){
        Vector userInfo=server.getUserInfo();
          for (int i=0;i<userInfo.size();i++){
              UserInfo current=(UserInfo)userInfo.elementAt(i);
              InetAddress ip=current.getIp();
              if(ip.toString().equals(ipString)){
                  Connection connection = server.getClient(ip);
                  AgentMessage message = new AgentMessage(new String(messageString),popup);
                  connection.send(message);
					
				  server.getXmlUsers().updateUser(type,current.getName(),System.currentTimeMillis());
				  break;
              }
          }
    }
    
    public float numberOfUsers() {
        return server.getUserInfo().size();
    }
}
