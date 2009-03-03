//package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.openfire.plugin;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;



public class ScyCommunicationAdapter implements IScyCommunicationAdapter {
    
    public static final Logger logger = Logger.getLogger(ScyCommunicationAdapter.class.getName());
    private static final long DEFAULT_EXPIRATION_TIME = 30*1000;
    private SQLSpaceAdapter tupleAdapter;
    private static ScyCommunicationAdapter communicator;
    private ArrayList<IScyCommunicationListener> scyCommunicationListeners = new ArrayList<IScyCommunicationListener>();


       public ScyCommunicationAdapter() {
        logger.debug("Empty Constructor Collaboration created");
    }

    public static ScyCommunicationAdapter getInstance() {
        if (communicator == null) {
            logger.debug("Created Tuple Spaces");
            communicator = new ScyCommunicationAdapter();
        }
        return communicator;
    }


    public void actionUponDelete(String username) {
        // TODO Auto-generated method stub
    }
    
    public void actionUponWrite(String username) {
        // TODO Auto-generated method stub
    }
    
    
    public ScyBaseObject getScyBaseObject(String description) {
        ScyBaseObject sbo = new ScyBaseObject();
        sbo.setId("54321");
        sbo.setName("a nice name for the object");
        sbo.setDescription(description);
        return sbo;
    }
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (tupleAdapter == null) {
            //TODO: SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE shouldn't be hardcoded here
            tupleAdapter = SQLSpaceAdapter.createAdapter(this.getClass().getName(), SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE, this);
            logger.debug("Created Tuple Spaces");
        }
        return tupleAdapter;
    }
    
    
    public void sendCallBack(String something) { 
        for (IScyCommunicationListener cl : scyCommunicationListeners) {
            if (cl != null){
                ScyCommunicationEvent scyCommunicationEvent = new ScyCommunicationEvent(this, something, something);
                cl.handleCommunicationEvent(scyCommunicationEvent);
            }
        }
    }


    @Override
    public String create(ScyBaseObject sbo) {
        logger.debug("create");
        String id = getTupleAdapter().write(null, "openfire", sbo, DEFAULT_EXPIRATION_TIME); // if documentSqlSpaceId != null this will update the tuple
        if (id != null) {
            logger.debug("Create probably went ok");
        } else {
            logger.error("Trouble while creating");
        }
        return id;
    }

    @Override
    public ScyBaseObject delete(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScyBaseObject read(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String update(ScyBaseObject sbo, String id) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        this.scyCommunicationListeners.add(listener);
    }

}
