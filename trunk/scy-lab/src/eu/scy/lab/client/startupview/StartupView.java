/**
 * 
 */
package eu.scy.lab.client.startupview;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.TableLayout;
import com.gwtext.client.widgets.layout.TableLayoutData;

import eu.scy.lab.client.startupview.feedback.FeedbackPanel;
import eu.scy.lab.client.startupview.lastELO.LastELOPanel;
import eu.scy.lab.client.startupview.lastMission.LastMissionPanel;
import eu.scy.lab.client.startupview.missionBrowser.MissionBrowser;

/**
 * @author Sven
 * 
 */
public class StartupView extends Panel {

    private MissionBrowser missionBrowser;

    private LastMissionPanel lastMissionPanel;

    private LastELOPanel lastEloPanel;

    private FeedbackPanel feedbackPanel;

    public StartupView() {
        super();
        History.newItem("startup");
        setBorder(false);
        // setLayout(new FitLayout());

        missionBrowser = new MissionBrowser();
        missionBrowser.setSize(400, 250);

        lastMissionPanel = new LastMissionPanel();
        lastMissionPanel.setSize(400, 250);

        lastEloPanel = new LastELOPanel();
        lastEloPanel.setSize(400, 250);

        feedbackPanel = new FeedbackPanel();
        feedbackPanel.setSize(400, 250);

        Panel tableWrapper = new Panel();
        tableWrapper.setLayout(new TableLayout(2));

        tableWrapper.add(missionBrowser);
        tableWrapper.add(lastMissionPanel, new TableLayoutData(1));
        tableWrapper.add(lastEloPanel);
        tableWrapper.add(feedbackPanel, new TableLayoutData(1));

        add(tableWrapper);
    }

    public VerticalPanel getCentredPanel() {
        // Positioning in Center
        // wrap Login-Panel in vertical Panel
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setWidth("100%");
        verticalPanel.setHeight("100%");
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        verticalPanel.add(horizontalPanel);
        horizontalPanel.add(this);
        return verticalPanel;
    }

}
