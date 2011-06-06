package eu.scy.server.utils;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.feb.2010
 * Time: 13:30:23
 * To change this template use File | Settings | File Templates.
 */
public class  MissionMapImporterTest extends TestCase {

    private ImportedXMLMission importedXMLMission;

    public ImportedXMLMission getImportedXMLMission() {
        return importedXMLMission;
    }

    public void setImportedXMLMission(ImportedXMLMission importedXMLMission) {
        this.importedXMLMission = importedXMLMission;
    }

    protected String[] getConfigLocations() {
        return new String[]{"/co2-mission-map.xml"};
    }

    @Test
    public void testGetMissionMap() {
        //assertNotNull(getApplicationContext().getBean("co2Mission"));
    }

    @Test
    public void testBuildMission() {
        List lases = getImportedXMLMission().getBasicMissionMap().getLasses();
        for (int i = 0; i < lases.size(); i++) {
            BasicLas basicLas = (BasicLas) lases.get(i);
            System.out.println(basicLas.getId());
            BasicMissionAnchor anchor = getImportedXMLMission().getAnchor(basicLas.getAnchorEloId());
            if(anchor != null) System.out.println("---> " + anchor.getId());
        }
    }

}
