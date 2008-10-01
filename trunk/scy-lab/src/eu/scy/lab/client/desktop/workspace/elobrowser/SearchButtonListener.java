/**
 * 
 */
package eu.scy.lab.client.desktop.workspace.elobrowser;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.StoreTraversalCallback;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridPanel;


/**
 * @author Sven
 *
 */
public class SearchButtonListener extends ButtonListenerAdapter{
    
    private GridPanel grid;
    private TextField searchField;
    
    public SearchButtonListener (GridPanel grid,TextField searchField){
        this.grid = grid;
        this.searchField = searchField;
    }
    
    public void onClick(Button button, EventObject e) {

        final String content = searchField.getValueAsString();

        grid.getStore().filterBy(new StoreTraversalCallback() {

            public boolean execute(Record record) {

                String[] row = record.getFields();

                for (int i = 0; i < row.length; i++) {

                    if (record.getAsString(row[i]).contains(content)) {
                        return true;
                    } else if (record.getAsString(row[i]).toLowerCase().contains(content.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }

        });
        // store.reload();
        grid.reconfigure(grid.getStore(), grid.getColumnModel());
        // grid.disable();
        // grid.enable();
    }

}
