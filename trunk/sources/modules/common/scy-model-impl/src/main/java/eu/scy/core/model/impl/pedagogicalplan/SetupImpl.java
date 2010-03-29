package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Setup;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 14:56:20
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table (name = "setup")
public class SetupImpl extends BaseObjectImpl implements Setup {
}
