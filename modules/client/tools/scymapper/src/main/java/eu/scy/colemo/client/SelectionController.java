package eu.scy.colemo.client;

import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.AddLink;
import eu.scy.colemo.client.actions.DoubleSelectAction;

import java.util.logging.Logger;
import java.util.List;
import java.util.LinkedList;

import eu.scy.colemo.client.actions.DoubleSelectAction;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.nov.2008
 * Time: 14:07:48
 * To change this template use File | Settings | File Templates.
 */
public class SelectionController {

    private static SelectionController selectionController;

    public final static String SINGLE_SELECT_MODUS = "SINGLE_SELECT_MODUS";
    public final static String DOUBLE_SELECT_MODUS = "DOUBLE_SELECT_MODUS";

    private String selectModus = SINGLE_SELECT_MODUS;

    private Logger log = Logger.getLogger("SelectionController.class");

    private Object currentPrimarySelection = null;
    private Object currentSecondarySelection = null;
    private DoubleSelectAction doubleSelectAction;

    private List<SelectionControllerListener> listeners = new LinkedList<SelectionControllerListener>();

    

    private SelectionController() {

    }

    public String getSelectModus() {
        return selectModus;
    }

    public void setSelectModus(String selectModus) {
        log.info("Setting selection modus: " + selectModus);
        this.selectModus = selectModus;
        setCurrentPrimarySelection(null);
        setCurrentSecondarySelection(null);
    }

    public static SelectionController getDefaultInstance() {
        if (selectionController == null) {
            selectionController = new SelectionController();
        }

        return selectionController;

    }

    public void setSelected(Object selected) {
        if (getSelectModus().equals(SINGLE_SELECT_MODUS)) {
            setCurrentPrimarySelection(selected);
            setCurrentSecondarySelection(null);
        } else {
            setCurrentSecondarySelection(getCurrentPrimarySelection());
            setCurrentPrimarySelection(selected);
        }

        if (getSelectModus().equals(DOUBLE_SELECT_MODUS)) {
            if (getCurrentPrimarySelection() != null && getCurrentSecondarySelection() != null) {
                if(getDoubleSelectAction() != null) {
                    getDoubleSelectAction().performDoubleSelectAction(null);
                }

            }
        }
        fireSelectEvent(selected);

    }

    private void fireSelectEvent(Object selected) {
        for (int i = 0; i < listeners.size(); i++) {
            SelectionControllerListener selectionControllerListener = listeners.get(i);
            selectionControllerListener.selectionPerformed(selected);
        }
    }

    public Object getCurrentPrimarySelection() {
        return currentPrimarySelection;
    }

    private void setCurrentPrimarySelection(Object currentPrimarySelection) {
        log.info("Primary selection: " + currentPrimarySelection);
        this.currentPrimarySelection = currentPrimarySelection;
    }

    public Object getCurrentSecondarySelection() {
        return currentSecondarySelection;
    }

    private void setCurrentSecondarySelection(Object currentSecondarySelection) {
        log.info("SecondarySelection: " + currentSecondarySelection);
        this.currentSecondarySelection = currentSecondarySelection;
    }

    public void setCurrentDoubleSelectAction(DoubleSelectAction doubleSelectAction) {
        this.doubleSelectAction=doubleSelectAction;
    }

    public DoubleSelectAction getDoubleSelectAction() {
        return doubleSelectAction;
    }

    public void addSelectionControllerListnenr(SelectionControllerListener selectionControllerListener) {
        listeners.add(selectionControllerListener);
    }

    public void removeSelectionControllerListener(SelectionControllerListener selectionControllerListener) {
        listeners.remove(selectionControllerListener);
    }
}
