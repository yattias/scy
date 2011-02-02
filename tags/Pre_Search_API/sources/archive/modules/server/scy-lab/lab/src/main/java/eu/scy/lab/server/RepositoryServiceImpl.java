package eu.scy.lab.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.scy.lab.client.repository.RepositoryService;

public class RepositoryServiceImpl extends RemoteServiceServlet implements RepositoryService {
    
    public String[] addELO(String elo) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String[] getELO(String id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String[][] searchELO(String search) {
        // title, author, date, version, id
        
        
        String[][] result = new String[][] { { "Test ELO", "roolo.cms.metadata.keys.Contribute@13a46b6", "1223297745893", "1", "roolo://memory/0/Test_ELO.scydraw" } };
        return result;
        // return new String[][] { new String[] { "Kryptoarithmetics", "Sven",
        // "9/1 12:00am", "res/icons/App2.png" }, new String[] { "Graphsearch",
        // "Sven M", "9/2 12:00am", "res/icons/App1.png" }, new String[] {
        // "Kryptoarithmetics II", "Sven", "9/4 12:00am",
        // "res/icons/Document.png" }, new String[] { "Dancing with animals",
        // "Sven", "9/5 12:01pm", "res/icons/Mail1.png" }, new String[] {
        // "Kryptoarithmetics III", "Sven", "10/1 12:10am",
        // "res/icons/Help1.png" } };
    }
}
