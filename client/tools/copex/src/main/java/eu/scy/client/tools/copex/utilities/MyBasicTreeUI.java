/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.edp.CopexTree;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComponent;
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

    @Override
    protected Color getHashColor() {
        return Color.GRAY;
    }

    /**
     * Paints the vertical part of the leg. The receiver should
     * NOT modify <code>clipBounds</code>, <code>insets</code>.<p>
     */
    @Override
    protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds,
					  Insets insets, TreePath path) {
	if (!paintLines) {
	    return;
	}

        int depth = path.getPathCount() - 1;
	if (depth == 0 && !getShowsRootHandles() && !isRootVisible()) {
	    return;
        }
	int lineX = getRowX(-1, depth + 1);
	if (leftToRight) {
            lineX = lineX - getRightChildIndent() + insets.left;
	}
	else {
            lineX = tree.getWidth() - lineX - insets.right +
                    getRightChildIndent() - 1;
	}
	int clipLeft = clipBounds.x;
	int clipRight = clipBounds.x + (clipBounds.width - 1);

	if (lineX >= clipLeft && lineX <= clipRight) {
	    int clipTop = clipBounds.y;
	    int clipBottom = clipBounds.y + clipBounds.height;
	    Rectangle parentBounds = getPathBounds(tree, path);
	    Rectangle lastChildBounds = getPathBounds(tree,
						     getLastChildPath(path));

	    if(lastChildBounds == null)
		// This shouldn't happen, but if the model is modified
		// in another thread it is possible for this to happen.
		// Swing isn't multithreaded, but I'll add this check in
		// anyway.
		return;

	    int       top;

	    if(parentBounds == null) {
		top = Math.max(insets.top + getVerticalLegBuffer(),
			       clipTop);
	    }
	    else
		top = Math.max(parentBounds.y + parentBounds.height +
			       getVerticalLegBuffer(), clipTop);
	    if(depth == 0 && !isRootVisible()) {
		TreeModel      model = getModel();

		if(model != null) {
		    Object        root = model.getRoot();

		    if(model.getChildCount(root) > 0) {
			parentBounds = getPathBounds(tree, path.
				  pathByAddingChild(model.getChild(root, 0)));
			if(parentBounds != null)
			    top = Math.max(insets.top + getVerticalLegBuffer(),
					   parentBounds.y +
					   parentBounds.height / 2);
		    }
		}
	    }

	    int bottom = Math.min(lastChildBounds.y +
				  (lastChildBounds.height / 2), clipBottom);

            if (top <= bottom) {
                g.setColor(getHashColor());
                paintVerticalLine(g, tree, lineX, top, bottom);
            }
	}
    }



    /**
     * Paints a vertical line.
     */
    @Override
    protected void paintVerticalLine(Graphics g, JComponent c, int x, int top,
				    int bottom) {
        drawDashedVerticalLine(g, x, top, bottom);
	
    }
   

    /**
     * Paints a horizontal line.
     */
    @Override
    protected void paintHorizontalLine(Graphics g, JComponent c, int y,
				      int left, int right) {
        drawDashedHorizontalLine(g, y, left, right);

    }

}
