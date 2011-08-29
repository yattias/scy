package eu.scy.agents.processguidanceservice;

import java.util.ArrayList;

public class ActionList {

    private ArrayList<UserAction> actionList;

    public ActionList() {
        actionList = new ArrayList<UserAction>();
    }

    public void setActionHistory(ArrayList<UserAction> aArrayList) {
        actionList = aArrayList;
    }

    public ArrayList<UserAction> getActionHistory() {
        return actionList;
    }

    public void addAction(UserAction anAction) {
        actionList.add(anAction);
    }

    public UserAction[] getRecentActionsOnELORun(ELORun aELORun, int number) {
        int counter = number - 1;
        UserAction[] recentActions = new UserAction[number - 1];
        for (int i = actionList.size() - 1; i > 0; i--) {
            if (actionList.get(i).getELORun() == aELORun) {
                recentActions[counter] = actionList.get(i);
                counter--;
                if (counter < 0)
                    return recentActions;
            }
        }
        UserAction[] recentActions2 = new UserAction[recentActions.length];
        int diff = number - recentActions.length;
        for (int i = 0; i < recentActions.length; i++) {
            recentActions2[i] = recentActions[i + diff];
        }
        return recentActions2;
    }
}
