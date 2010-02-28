/**
 * 
 */
package eu.scy.lab.client.desktop.workspace.elobrowser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtextux.client.data.PagingMemoryProxy;

import eu.scy.lab.client.repository.RepositoryService;
import eu.scy.lab.client.repository.RepositoryServiceAsync;

/**
 * @author Sven
 */
public class SearchButtonListener extends ButtonListenerAdapter {
    
    private GridPanel grid;
    private TextField searchField;
    
    private RepositoryServiceAsync repository;
    
    public SearchButtonListener(GridPanel grid, TextField searchField) {
        this.grid = grid;
        this.searchField = searchField;
        
        repository = RepositoryService.Util.getInstance();
    }
    
    public void onClick(Button button, EventObject e) {
        
        final String content = searchField.getValueAsString();
        repository.searchELO(content, new AsyncCallback<String[][]>() {
            
            public void onFailure(Throwable caught) {
                MessageBoxConfig mbc = new MessageBoxConfig();
                mbc.setTitle("System Failure"); // TODO: i18n
                mbc.setMsg(caught.getLocalizedMessage());
                mbc.setIconCls(MessageBox.ERROR);
                mbc.setButtons(MessageBox.OK);
                MessageBox.show(mbc);
            }
            
            public void onSuccess(String[][] result) {
                grid.getStore().setDataProxy(new PagingMemoryProxy(result));
                grid.getStore().load();
                grid.reconfigure(grid.getStore(), grid.getColumnModel());
            }
            
        });
    }
    
}
