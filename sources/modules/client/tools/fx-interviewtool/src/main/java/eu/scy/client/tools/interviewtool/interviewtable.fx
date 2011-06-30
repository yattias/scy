/*
 * InterviewTable.fx
 *
 * Created on 22.08.2009, 1:19:18
 */

package eu.scy.client.tools.interviewtool;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javafx.ext.swing.SwingComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Font;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.control.CheckBox;
import javafx.reflect.*;
import eu.scy.client.tools.interviewtool.InterviewObject;
import javafx.scene.control.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;
import javafx.scene.layout.LayoutInfo;

/**
 * @author kaido
 */

/**
 * Table column
 */
package class TableColumn {
    public var text: String;
}
/**
 * Table cell
 */
package class TableCell {
    public var text: String;
}
/**
 * Table row
 */
package class TableRow {
    public var cells: TableCell[];
}
/**
 * Wrapping class for JTable
 */
package class InterviewTable extends SwingComponent {
    var table: JTable;
    public var model: DefaultTableModel;
    public var selection: Integer;
    public var isModified: Boolean;
    public override var font on replace {
        var fontStyle = Font.PLAIN;
        if (font.style.toUpperCase().equals("BOLD")) {
            fontStyle = Font.BOLD;
        } else if (font.style.toUpperCase().equals("ITALIC")) {
            fontStyle = Font.ITALIC;
        }
        var myFont = new Font(font.name, fontStyle, font.size);
        table.setFont(myFont);
        table.getTableHeader().setFont(myFont);
    }
    public var headerFont: javafx.scene.text.Font on replace {
        var fontStyle = Font.PLAIN;
        if (headerFont.style.toUpperCase().equals("BOLD")) {
            fontStyle = Font.BOLD;
        } else if (headerFont.style.toUpperCase().equals("ITALIC")) {
            fontStyle = Font.ITALIC;
        }
        var myFont = new Font(headerFont.name, fontStyle, headerFont.size);
        table.getTableHeader().setFont(myFont);
    }
    public var columns: TableColumn[] on replace{
        model = new DefaultTableModel(for(column in columns) column.text, 0);
        table.setModel(model);
    };
    public var rows: TableRow[] on replace oldValue[lo..hi] = newVals{
        for(index in [hi..lo step -1]){
            model.removeRow(index);
        }
        for(row in newVals){
            model.addRow(for(cell in row.cells) cell.text);
        }
    };
    public override function createJComponent(){
        table = JTable{
            public override function isCellEditable(x:Integer, y:Integer) {
                return false;
            }
        };
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        model = table.getModel() as DefaultTableModel;
        var selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(
            ListSelectionListener{
                public override function valueChanged(e: ListSelectionEvent ) {
                    selection = table.getSelectedRow();
                }
            }
        );
        table.addPropertyChangeListener("tableCellEditor",
            PropertyChangeListener {
                public override function propertyChange(evt: PropertyChangeEvent) {
                    var newEditor : TableCellEditor = evt.getNewValue() as TableCellEditor;
                    if (newEditor != null) {
                        (newEditor as DefaultCellEditor).getComponent().addKeyListener(
                            KeyListener{
                                public override function keyPressed(e:KeyEvent) {
                                }
                                public override function keyReleased(e:KeyEvent) {
                                }
                                public override function keyTyped(e:KeyEvent) {
                                    isModified = true;
                                }
                            }
                        );
                    }
                }
            }
        );
        table.addFocusListener(
            FocusListener {
                public override function focusGained(event: FocusEvent) {
                }
                public override function focusLost(event: FocusEvent) {
                    if (table.isEditing())
                        table.getCellEditor().stopCellEditing();
                }
            }
        );
        return new JScrollPane(table);
    }
}
/**
 * Table editor for topics, indicators and answers.
 * @see InterviewTopic
 * @see InterviewIndicator
 * @see InterviewAnswer
 */
package class InterviewTableEditor extends CustomNode {
    public var oldObjects: InterviewObject[] on replace {objects=oldObjects};
    public var nextID: Integer;
    public-init var classType: String;
    public-init var headerText: String;
    public-init var refreshAction: function();
    public-init var logAction: function(type:String,attributes:InterviewObject[]);
    public-init var logNamelyAction: function(checked:Boolean);
    public-init var width: Number;
    public-init var height: Number;
    public-init var font: javafx.scene.text.Font;
    public-init var headerFont: javafx.scene.text.Font;
    public-init var namelyShow: Boolean on replace{if (namelyShow) offset=30 else offset=0};
    public var namelyChecked: Boolean on replace{
        if (isModified and logNamelyAction!=null) logNamelyAction(namelyChecked);
        if (isModified and refreshAction != null) {refreshAction();}
        isModified=true;
    };
    var isModified: Boolean = false;
    var offset: Integer;
    var objects: InterviewObject[];
    var selection: Integer;
    var table = InterviewTable {
                    translateX:translateX
                    translateY:translateY
                    layoutInfo:LayoutInfo{width: width, height: height}
                    font: font
                    headerFont: headerFont
                    isModified: bind isModified with inverse
                    columns: [
                        TableColumn {
                            text: headerText
                        }
                    ]
                    rows: bind for(p in objects)
                        TableRow {
                            cells: [
                                TableCell {
                                    text:bind p.toString()
                                }
                            ]
                        }
                    selection: bind selection with inverse
                };
    function addRow() {
        var newValue:String = JOptionPane.showInputDialog("");
        if (not newValue.equals("")) {
            var ctx:FXLocal.Context = FXLocal.getContext();
            var cls:FXLocal.ClassType = ctx.findClass("eu.scy.client.tools.interviewtool.{classType}");
            var objVal:FXObjectValue = cls.allocate();
            objVal.initVar("id", ctx.mirrorOf(nextID));
            objVal = objVal.initialize();
            var o: InterviewObject = ((objVal as FXLocal.ObjectValue).asObject()) as InterviewObject;
            // FXEvaluator doesnt work in browser with javafx version 1.2
            // var o: InterviewObject = evaluator.eval("eu.scy.client.tools.interviewtool.{classType}\{id: {nextID}\}") as InterviewObject;
            o.setValue(newValue);
            insert o into objects;
            nextID++;
            oldObjects=objects;
            if (refreshAction != null) {
                refreshAction();
            }
        }
    }
    function removeRow() {
        if (logAction != null) {
            logAction("REMOVE",objects[selection]);
        }
        delete objects[selection];
        isModified=true;
        oldObjects=objects;
        if (refreshAction != null) {
            refreshAction();
        }
    }
    public override function create() {
        def buttonFont = javafx.scene.text.Font {
            name: "Arial"
            size: 14
        }
        isModified = false;
        Group {
            content: [
                table,
                CheckBox {
                        translateX: translateX
                        translateY: translateY+height+10
                        text: ##"Include \"Other, namely…\" option"
                        allowTriState: false
                        selected: bind namelyChecked with inverse
                        visible: namelyShow
                    }
                HBox{
                    spacing: 5
                    translateX: translateX
                    translateY: translateY+height+10+offset
                    // FXEvaluator doesnt work in browser with javafx version 1.2
                    // translateY: height+10+(evaluator.eval("if ({namelyShow}==Boolean.TRUE) 30 else 0;") as Integer)
                    content: [
                        Button{
                            text: ##"Add"
                            font: buttonFont
                            action: function(){
                                addRow();
                                isModified=true;
                                if (logAction != null) {
                                    logAction("ADD",objects[selection]);
                                }
                            }
                        },
                        Button{
                            text: ##"Remove"
                            font: buttonFont
                            action: function(){
                                removeRow();
                            }
                        }
                    ]
                }
            ]
        }
    }
}
