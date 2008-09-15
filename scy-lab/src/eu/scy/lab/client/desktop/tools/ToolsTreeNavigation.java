package eu.scy.lab.client.desktop.tools;

 import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.tree.TreePanel;
   
 public class ToolsTreeNavigation   {  
   
	 Panel panel;
	 
     public ToolsTreeNavigation() {  
         panel = new Panel();  
         panel.setBorder(false);  
         panel.setPaddings(5);

 		panel.setAutoHeight(true);
 		panel.setAutoWidth(true);
   
         final TreePanel treePanel = new Tools();  
         treePanel.setHeader(false);
         treePanel.setBorder(false);
    
         panel.add(treePanel);  
   
          
     }  
     
     public Panel getPanel(){
    	 return  this.panel;
     }
     
   
     
 }  