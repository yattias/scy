/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.CopexTree;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Marjolaine
 */
public class MyBasicTreeUI extends BasicTreeUI{

    private CopexTree myTree;
    private boolean paintLines = true;
    private boolean leftToRight = true;

    public MyBasicTreeUI(CopexTree myTree) {
        super();
        this.myTree = myTree;
    }

    @Override
    protected void paintHorizontalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        if (!paintLines) {
	    return;
	}

        // Don't paint the legs for the root'ish node if the
        int depth = path.getPathCount() - 1;
	if((depth == 0 || (depth == 1 && !isRootVisible())) &&
	   !getShowsRootHandles()) {
	    return;
        }

	int clipLeft = clipBounds.x;
        int clipRight = clipBounds.x + clipBounds.width;
	int clipTop = clipBounds.y;
        int clipBottom = clipBounds.y + clipBounds.height;
	int lineY = bounds.y+12 ;

        if (leftToRight) {
            int leftX = bounds.x - getRightChildIndent();
            int nodeX = bounds.x - getHorizontalLegBuffer();

            if(lineY >= clipTop
                    && lineY < clipBottom
                    && nodeX >= clipLeft
                    && leftX < clipRight
                    && leftX < nodeX) {

                g.setColor(getHashColor());
                paintHorizontalLine(g, tree, lineY, leftX, nodeX - 1);
            }
        } else {
            int nodeX = bounds.x + bounds.width + getHorizontalLegBuffer();
            int rightX = bounds.x + bounds.width + getRightChildIndent();

            if(lineY >= clipTop
                    && lineY < clipBottom
                    && rightX >= clipLeft
                    && nodeX < clipRight
                    && nodeX < rightX) {

                g.setColor(getHashColor());
                paintHorizontalLine(g, tree, lineY, nodeX, rightX - 1);
            }
        }
    }

    
   

}
