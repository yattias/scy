/*
 * InterviewTree.fx
 *
 * Created on 16.08.2009, 18:38:23
 */

package eu.scy.client.tools.interviewtool;

import javafx.ext.swing.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.Font;

/**
 * @author kaido
 */

/**
* Wrapping class for JTree
*/
public class InterviewTree extends SwingComponent{
    public var tree: JTree;
    public var model: DefaultTreeModel;
    public var selectedValue: Object;
    public var action: function(obj:Object);
    public var root: InterviewTreeCell on replace{
        if (root != null){
            model.setRoot(getCell(root));
            var obj = model.getRoot();
            var tp = new TreePath(obj);
        }
    };
    public override var font on replace {
        var fontStyle = Font.PLAIN;
        if (font.style.toUpperCase().equals("BOLD")) {
            fontStyle = Font.BOLD;
        } else if (font.style.toUpperCase().equals("ITALIC")) {
            fontStyle = Font.ITALIC;
        }
        var myFont = new Font(font.name, fontStyle, font.size);
        tree.setFont(myFont);
    }
    override function createJComponent(): JComponent {
        tree = new JTree();
        model = tree.getModel() as DefaultTreeModel;
        var  mouseListener = java.awt.event.MouseAdapter {
            override function mousePressed(e: java.awt.event.MouseEvent) {
                var selRow = tree.getRowForLocation(e.getX(), e.getY());
                var selPath = tree.getPathForLocation(e.getX(), e.getY());
                var lastPath = selPath.getPathComponent(selPath.getPathCount()-1);
                var obj = (lastPath as DefaultMutableTreeNode).getUserObject();
                if(action != null ){
                   action(obj);
                }
                if(selRow != - 1) {
                    if(e.getClickCount() == 1) {
                        selectedValue = obj;
                    }
                    else if(e.getClickCount() == 2) {
                    }
                }
                getJComponent().repaint();
            }
        };
        tree.addMouseListener(mouseListener);
        tree.setRootVisible(false);
        var scrollPane = new JScrollPane(tree);
        return scrollPane;
    }
    function getCell(cell: InterviewTreeCell): MutableTreeNode {
        var node = new DefaultMutableTreeNode(cell);
        for (c in cell.cells) {
            node.<<insert>>(getCell(c),node.getChildCount());
        }
        return node;
    }
}
