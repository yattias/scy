package eu.scy.server.assessment;

import eu.scy.core.model.User;
import eu.scy.core.model.transfer.Portfolio;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.sep.2011
 * Time: 09:18:16
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentStatusTransporter {

    private Portfolio portfolio;
    private User user;

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
