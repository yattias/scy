package eu.scy.lab.client.startupview.missionBrowser;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

import eu.scy.lab.client.mission.Mission;

public class MissionBrowser extends Panel {
    
    /**
     * 
     */
    private ComboBox missions;
    
    private Mission actualMission;
    
    /**
     * @return the actualMission
     */
    public Mission getActualMission() {
        return actualMission;
    }
    
    /**
     * @param actualMission
     *            the actualMission to set
     */
    public void setActualMission(Mission actualMission) {
        this.actualMission = actualMission;
    }
    
    public MissionBrowser() {
        super("Mission Browser");
        
        setPaddings(15);
        setLayout(new FitLayout());
        
        missions = new ComboBox("Missions", "missions");
        
        // create a Store using local array data (for titles)
        final String[] missionData = new String[2];
        missionData[0] = "CO2 neutral house";
        missionData[1] = "Tutorial Mission";
        
        final Store store = new SimpleStore("missions", missionData);
        store.load();
        
        // Combobox attributes
        missions.setEmptyText("select Mission");
        missions.setForceSelection(true);
        missions.setMinChars(1);
        missions.setStore(store);
        missions.setDisplayField("missions");
        missions.setMode(ComboBox.LOCAL);
        missions.setTriggerAction(ComboBox.ALL);
        missions.setLoadingText("Getting Data");
        missions.setTypeAhead(true);
        missions.setHideTrigger(false);
        missions.setEditable(false);
        missions.setSelectOnFocus(true);
        missions.expand();
        missions.focus(true);
        
        Button info = new Button("Info", new ButtonListenerAdapterImpl(this, "info"));
        final Button start = new Button("Start", new ButtonListenerAdapterImpl(this, "start"));
        
        // initial Mission set to null to invoke a MessageBox in ButtonListener
        actualMission = null;
        
        // ComboBoxListener -> Select Mission and store it to actual
        final ButtonListenerAdapterImpl startMissionListener = new ButtonListenerAdapterImpl(this, "start");
        missions.addListener(new ComboBoxListenerAdapter() {
            
            public void onSelect(ComboBox comboBox, Record record, int index) {
                actualMission = Mission.GetMissionByName(missionData[index]);
            }
            
            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == EventObject.RETURN) {
                    startMissionListener.onClick(start, e);
                }
            }
        });
        
        add(missions);
        addButton(info);
        addButton(start);
        
        
        
    }
    
    public ComboBox getMissions() {
        return this.missions;
    }
    
}
