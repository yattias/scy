/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.undoRedo;

import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.client.tools.dataProcessTool.dataTool.DataTable;
import eu.scy.client.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jdom.Element;

/**
 * edit header undo redo
 * @author Marjolaine
 */
public class EditHeaderUndoRedo extends DataUndoRedo{
    private String oldValue;
    private String newValue;
    private String oldUnit;
    private String newUnit;
    private String oldDescription;
    private String newDescription;
    private String oldType;
    private String newType;
    private int colIndex;
    private String oldFormula;
    private String newFormula;
    private boolean oldScientificNotation;
    private boolean newScientificNotation;
    private int oldNbShownDecimals;
    private int newNbShownDecimals;
    private int oldNbSignificantDigits;
    private int newNbSignificantDigits;

    public EditHeaderUndoRedo(DataTable table, FitexToolPanel dataToolPanel, ControllerInterface controller,
            String oldValue, String newValue, String oldUnit, String newUnit,  int colIndex, String oldDescription, String newDescription,
            String oldType, String newType, String oldFormula, String newFormula, boolean oldScientificNotation, boolean newScientificNotation,
            int oldNbShownDecimals, int newNbShownDecimals, int oldNbSignificantDigits, int newNbSignificantDigits) {
        super(table, dataToolPanel, controller);
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.oldUnit = oldUnit;
        this.newUnit = newUnit;
        this.oldDescription = oldDescription;
        this.newDescription = newDescription;
        this.oldType = oldType;
        this.newType = newType;
        this.oldFormula = oldFormula;
        this.newFormula = newFormula;
        this.colIndex = colIndex;
        this.oldScientificNotation = oldScientificNotation;
        this.newScientificNotation = newScientificNotation;
        this.oldNbShownDecimals = oldNbShownDecimals;
        this.newNbShownDecimals = newNbShownDecimals;
        this.oldNbSignificantDigits = oldNbSignificantDigits;
        this.newNbSignificantDigits = newNbSignificantDigits;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataHeader(getDataset(), true,colIndex, newValue, newUnit, newDescription, newType, newFormula, dataToolPanel.getFunction(newFormula), newScientificNotation, newNbShownDecimals, newNbSignificantDigits, v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        this.dataToolPanel.updateDataset(nds);
        this.dataToolPanel.logRedo(this);
        cr = this.controller.exportHTML();
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataHeader(getDataset(), true, colIndex, oldValue, oldUnit,oldDescription, oldType, oldFormula, dataToolPanel.getFunction(oldFormula),oldScientificNotation, oldNbShownDecimals, oldNbSignificantDigits, v);
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        this.dataToolPanel.updateDataset(nds);
        this.dataToolPanel.logUndo(this);
        cr = this.controller.exportHTML();
        if (cr.isError()){
            dataToolPanel.displayError(cr, dataToolPanel.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    @Override
    public Element toXMLLog(){
        Element element = new Element("undoRedoEditHeader");
        element.addContent(new Element("no_col").setText(Integer.toString(colIndex)));
        element.addContent(new Element("old_value").setText(oldValue));
        element.addContent(new Element("new_value").setText(newValue));
        element.addContent(new Element("old_unit").setText(oldUnit));
        element.addContent(new Element("new_unit").setText(newUnit));
        element.addContent(new Element("old_type").setText(oldType));
        element.addContent(new Element("new_type").setText(newType));
        element.addContent(new Element("old_description").setText(oldDescription));
        element.addContent(new Element("new_description").setText(newDescription));
        element.addContent(new Element("old_formula").setText(oldFormula == null ?"":oldFormula));
        element.addContent(new Element("new_formula").setText(newFormula == null ?"":newFormula));
        return element;
    }
}
