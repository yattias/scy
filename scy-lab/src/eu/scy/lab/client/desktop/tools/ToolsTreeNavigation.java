package eu.scy.lab.client.desktop.tools;

 import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.tree.TreePanel;
   
 public class ToolsTreeNavigation   {  
   
	 Panel panel;
	 
     public ToolsTreeNavigation() {  
         panel = new Panel("Tools");  
         panel.setBorder(false);  
         panel.setPaddings(15);
         panel.setCollapsible(true);
   
         final TreePanel treePanel = new Tools();  
         treePanel.setHeader(false);
//         treePanel.setTitle("Tools");  
         treePanel.setCollapsible(true);
         treePanel.setWidth(190);  
         treePanel.setHeight(150);  
   
         panel.add(treePanel);  
   
          
     }  
     
     public Panel getPanel(){
    	 return  this.panel;
     }
     
   
     
 }  