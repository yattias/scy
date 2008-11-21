package eu.scy.colemo.client;

import java.util.logging.Logger;

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

    private Object currentPrimarySelection;
    private Object currentSecondarySelection;


    private SelectionController() {

    }

    public String getSelectModus() {
        return selectModus;
    }

    public void setSelectModus(String selectModus) {
        log.info("Setting selection modus: " + selectModus);
        this.selectModus = selectModus;
    }

    public static SelectionController getDefaultInstance() {
        if(selectionController == null) {
            selectionController = new SelectionController();
        }

        return selectionController;

    }

    public void setSelected(Object selected) {
        if(getSelectModus().equals(SINGLE_SELECT_MODUS)) {
            setCurrentPrimarySelection(selected);
            setCurrentSecondarySelection(null);
        } else {
            setCurrentSecondarySelection(getCurrentPrimarySelection());
            setCurrentPrimarySelection(selected);
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
}
