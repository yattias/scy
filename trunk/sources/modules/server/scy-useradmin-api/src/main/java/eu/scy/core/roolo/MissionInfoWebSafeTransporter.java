package eu.scy.core.roolo;

import eu.scy.common.scyelo.ScyElo;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mai.2011
 * Time: 15:36:06
 * To change this template use File | Settings | File Templates.
 */
public class MissionInfoWebSafeTransporter extends ELOWebSafeTransporter {

    private Integer numberOfActiveStudents;

    public MissionInfoWebSafeTransporter(ScyElo scyElo) {
        super(scyElo);
    }

    public Integer getNumberOfActiveStudents() {
        return numberOfActiveStudents;
    }

    public void setNumberOfActiveStudents(Integer numberOfActiveStudents) {
        this.numberOfActiveStudents = numberOfActiveStudents;
    }
}
