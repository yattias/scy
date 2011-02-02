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
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javafx.scene.control.Button;

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
//    public var focusLostCount: Integer;
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
        table = new JTable();
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
        /*
        table.addFocusListener(
            FocusListener {
                public override function focusGained(event: FocusEvent) {
                    println("focus gained");
                }
                public override function focusLost(event: FocusEvent) {
                    if (table.isEditing())
                        table.getCellEditor().stopCellEditing();
                    focusLostCount++;
                    println("focus lost");
                }
            }
        );
        */
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
    public-init var width: Number;
    public-init var height: Number;
    public-init var font: javafx.scene.text.Font;
    public-init var headerFont: javafx.scene.text.Font;
    public-init var namelyShow: Boolean on replace{if (namelyShow) offset=30 else offset=0};
    public var namelyChecked: Boolean on replace{isModified=true};
//    var focusLostCount: Integer = 0 on replace{if (focusLostCount>0) saveTable()};
    def defaultNamelyChecked = namelyChecked;
    var isModified: Boolean = false;
    var offset: Integer;
    var objects: InterviewObject[];
    var selection: Integer;
    var table = InterviewTable {
                    width: width
                    height: height
                    font: font
                    headerFont: headerFont
//                    focusLostCount: bind focusLostCount with inverse
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
    var modifiedLabel = Label {
            text: "Not saved!"
            font: font
            textFill: javafx.scene.paint.Color.RED
            translateY: height + 10
            translateX: 320
            visible: bind isModified
        }
    function addRow() {
        var ctx:FXLocal.Context = FXLocal.getContext();
        var cls:FXLocal.ClassType = ctx.findClass("eu.scy.client.tools.interviewtool.{classType}");
        var objVal:FXObjectValue = cls.allocate();
        objVal.initVar("id", ctx.mirrorOf(nextID));
        objVal = objVal.initialize();
        var o: InterviewObject = ((objVal as FXLocal.ObjectValue).asObject()) as InterviewObject;
        // FXEvaluator doesnt work in browser with javafx version 1.2
//        var o: InterviewObject = evaluator.eval("eu.scy.client.tools.interviewtool.{classType}\{id: {nextID}\}") as InterviewObject;
        insert o into objects;
        nextID++;
    }
    function saveTable() {
        for (i in [0..sizeof objects-1]) {
            objects[i].setValue(table.model.getValueAt(i, 0).toString());
        }
        oldObjects=objects;
        isModified=false;
        if (refreshAction != null) {
            refreshAction();
        }
    }
    public override function create() {
        isModified = false;
        Group {
            content: [
                table,
                modifiedLabel,
                CheckBox {
                        translateY: height+10
                        text: "Include \"Other, namely…\" option"
                        allowTriState: false
                        selected: bind namelyChecked with inverse
                        visible: namelyShow
                    }
                HBox{
                    spacing: 5
                    translateY: height+10+offset
                    // FXEvaluator doesnt work in browser with javafx version 1.2
//                    translateY: height+10+(evaluator.eval("if ({namelyShow}==Boolean.TRUE) 30 else 0;") as Integer)
                    content: [
                        Button{
                            text: "Add"
                            action: function(){
                                addRow();
                                isModified=true;
                            }
                        },
                        Button{
                            text: "Remove"
                            action: function(){
                                delete objects[selection];
                                isModified=true;
                            }
                        },
                        Button{
                            text: "Save"
                            action: function(){
                                saveTable();
                            }
                        },
                        Button{
                            text: "Cancel"
                            action: function(){
                                // insert needed for refresh
                                if (sizeof objects == sizeof oldObjects) {
                                    addRow();
                                }
                                objects=oldObjects;
                                namelyChecked = defaultNamelyChecked;
                                isModified=false;
                            }
                        }
                    ]
                }
            ]
        }
    }
}
