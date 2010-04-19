package eu.scy.server.externalcomponents.swat;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.swat.SWAT;
import info.collide.swat.SWATConfiguration;
import info.collide.swat.model.SWATException;

import java.util.Vector;
import java.util.logging.Logger;

import eu.scy.server.externalcomponents.ExternalComponentFailedException;
import eu.scy.server.externalcomponents.IExternalComponent;

public class SWATComponent implements IExternalComponent {

    private Logger log = Logger.getLogger("SWATComponent.class");

    @Override
    public void startComponent() throws ExternalComponentFailedException {
        log.info("Initializing SWAT SERVER");
        SWATConfiguration conf = SWATConfiguration.getConfiguration();
        conf.setStandalone(true);
        conf.setInitialServices("ScyseCompareService", "IoService", "ScyseService");
        Configuration.getConfiguration().setLocal(true);
        Vector<String> admins = new Vector<String>();
        admins.add("Jan"); // for SCY-SE
        Configuration.getConfiguration().setAdmins(admins);
        try {
            SWAT.start();
        } catch (TupleSpaceException e) {
            throw new ExternalComponentFailedException(e);
        } catch (SWATException e) {
            throw new ExternalComponentFailedException(e);
        }
    }

    @Override
    public void stopComponent() throws ExternalComponentFailedException {
        log.info("-----> STOPPING: SWAT");
        SWAT.killAllAgents();
    }

}
