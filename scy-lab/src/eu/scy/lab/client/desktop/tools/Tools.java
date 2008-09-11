package eu.scy.lab.client.desktop.tools;

import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

class Tools extends TreePanel {  
   
         public Tools() {  
   
             TreeNode root = new TreeNode("SCY Toolbox");  
   
             TreeNode drawing = new TreeNode("Drawing");  
             drawing.setExpanded(true);  
   
             TreeNode jDT = new TreeNode("JDT");  
             jDT.setExpanded(true);  
             TreeNode paint = new TreeNode("alternativ Paint");  
             paint.setExpanded(true);  
             
             drawing.appendChild(jDT);  
             drawing.appendChild(paint);  
             
   
             TreeNode simulation = new TreeNode("Simulation");  
             simulation.setExpanded(true);  
   
             TreeNode co2sim = new TreeNode("CO2-Simulation");  
             co2sim.setExpanded(true);  
   
             simulation.appendChild(co2sim);  
   
             root.appendChild(drawing);
             root.appendChild(simulation);
  
             setRootVisible(false);  
   
             setTitle("Tools");  
             setWidth(200);  
             setHeight(400);  
             setRootNode(root);  
             root.setExpanded(true);  
         }  
     }  