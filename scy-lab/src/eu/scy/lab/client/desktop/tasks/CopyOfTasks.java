package eu.scy.lab.client.desktop.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.menu.Menu;

public class CopyOfTasks {

	private Panel panel;
	private Database db;
	private Grid grid;
	private List<String> tasks;

	public CopyOfTasks() {
		
		 // Create the database if it doesn't exist.
	    try {
	      db = Factory.getInstance().createDatabase();
	      db.open("scy-lab-tasks");
	      // The 'int' type will store up to 8 byte ints depending on the magnitude of the 
	      // value added.
	      db.execute("create table if not exists tasks (Task string)");
	    } catch (GearsException e) {
	      MessageBox.alert(e.toString());
	    }
	    
	    // Add an entry to the database
	    Button addTask = new Button("Add Task...",new ButtonListenerAdapter(){
	    	
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				MessageBox.prompt("Add task...", "Enter the task you want to add!",new PromptCallback(){

					public void execute(String btnID, String text) {
						if (btnID.equals("ok")){
							try {
								db.execute("insert into tasks values (?)", new String[] {text});
//								update();
							}
							catch (DatabaseException de) {
								MessageBox.alert(de.toString());
							}
							
						} else if (btnID.equals("cancel")){
							
						}
						
					}


		    		
		    	}	);
				
				
			}

	    });
	  
	    grid = createGrid();
	    
		panel = new Panel();
		panel.setAutoScroll(true);
		panel.setBorder(false);
		panel.setPaddings(5);

		panel.add(grid);
		panel.add(addTask);

	}
	
//	public void update() {
//		 Record plant = recordDef.createRecord(new Object[]{  
//			                             "New Plant1", "Anguinaria Canadensis", "Mostly Shady",  
//			                              new Float(5), "", Boolean.FALSE});  
//			                 grid.stopEditing();  
//			                 store.insert(0, plant);  
//			                 grid.startEditing(0, 0);  
//			             }  
//		
//	}
	
	public Grid createGrid() {
		
		 // Fetch previous results from the database.
		tasks = new ArrayList<String>();
	    try {
	      ResultSet rs = db.execute("select * from tasks order by Task");
	      for (int i = 0; rs.isValidRow(); ++i, rs.next()) {
	        tasks.add(rs.getFieldAsString(0));
	      }
	      rs.close();
	    } catch (DatabaseException e) {
	      MessageBox.alert(e.toString());
	    }
	    
	    // Display the list of results in a table
//	    grid.removeFromParent();
	    Grid grid = new Grid(tasks.size() + 1, 1);
//	    grid.setText(0, 0, "Tasks:");
	    for (int row = 0; row < tasks.size(); ++row) {
	      String task = new String(tasks.get(row));
	      grid.setText(row , 0, task.toString()); 
	    }
	    return grid;
		
	}

	public Panel getPanel() {
		return this.panel;
	}

}