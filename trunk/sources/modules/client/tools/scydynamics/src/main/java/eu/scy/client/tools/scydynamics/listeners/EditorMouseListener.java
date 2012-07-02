package eu.scy.client.tools.scydynamics.listeners;

import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;


import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdDataset;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdHandle;
import colab.um.draw.JdLink;
import colab.um.draw.JdNode;
import colab.um.draw.JdObject;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;
import colab.um.draw.JdSubObject;
import eu.scy.client.tools.scydynamics.editor.EditorToolbar;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.model.Model;

public class EditorMouseListener implements MouseListener, MouseMotionListener {

    private JdFigure fStart;
    private ModelEditor editor;
    private Model model;
    private boolean allowDrag;
    private int dragPoint;
    private JdLink fLink;
    private JdFigure fOver;

    public EditorMouseListener(ModelEditor editor, Model model) {
        this.editor = editor;
        this.model = model;
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX(), y = e.getY();
        // Press Button3
        if (e.isPopupTrigger() || e.getModifiers() == InputEvent.BUTTON3_MASK) {
            // editor.showPopupMenu(getObjectAt(x,y), x, y);
            e.consume();
            return;
        }
        // Press Button1
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
            switch (editor.getCurrentAction()) {
                case EditorToolbar.CURSOR:
                    actionCursor(x, y, e.isControlDown());
                    break;
                case EditorToolbar.DELETE:
                    actionDelete(x, y);
                    break;
                case EditorToolbar.STOCK:
                    actionCreateNode(new JdStock(model.getFreeName(JdFigure.STOCK),
                            x, y), x, y);
                    break;
                case EditorToolbar.AUX:
                    actionCreateNode(new JdAux(model.getFreeName(JdFigure.AUX), x,
                            y), x, y);
                    break;
                case EditorToolbar.CONSTANT:
                    actionCreateNode(new JdConst(model.getFreeName(JdFigure.CONSTANT), x, y), x, y);
                    break;
                case EditorToolbar.DATASET:
                    actionCreateNode(new JdDataset(model.getFreeName(JdFigure.DATASET), x, y), x, y);
                    break;
                case EditorToolbar.FLOW:
                    actionCreateLink(new JdFlow(model.getFreeName(JdFigure.FLOW)), x, y);
                    break;
                case EditorToolbar.RELATION:
                    actionCreateLink(new JdRelation(model.getFreeName(JdFigure.RELATION), true), x, y);
                    break;
            } // switch
        } // if
        e.consume();
    }

    public void mouseDragged(MouseEvent evt) {
    	if (editor.getMode()==Mode.BLACK_BOX || editor.getMode()==Mode.CLEAR_BOX) {
    		// editing the model is not allowed -> return!
    		return;
    	}
        switch (editor.getCurrentAction()) {
            case EditorToolbar.CURSOR:
                actionDragCursor(evt.getX(), evt.getY());
                break;
            case EditorToolbar.FLOW:
            case EditorToolbar.RELATION:
                actionDragLink(evt.getX(), evt.getY());
                break;
        }
        evt.consume();
    }

    public void mouseReleased(MouseEvent evt) {
        switch (editor.getCurrentAction()) {
            case EditorToolbar.CURSOR:
                actionEndCursor();
                break;
            case EditorToolbar.FLOW:
            case EditorToolbar.RELATION:
                actionEndLink();
                break;
        }
        allowDrag = false;
        evt.consume();
    }

    public void mouseMoved(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (fStart != null) {
                if (fStart.isNode() && editor.getMode()!=Mode.BLACK_BOX) {
                    editor.showSpecDialog(fStart, e.getLocationOnScreen());
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void setAction(int action) {
        editor.setCursor(action);
    }

    private JdObject getObjectAt(int x, int y) {
        JdObject o = null;
        JdFigure f = model.getSmallFigureAt(x, y);
        if (f != null) {
            switch (f.getType()) {
                case JdFigure.STOCK:
                case JdFigure.CONSTANT:
                case JdFigure.AUX:
                case JdFigure.DATASET:
                case JdFigure.RELATION:
                case JdFigure.FLOW:
                    o = (JdObject) f;
                    break;
                case JdFigure.FLOWCTR:
                case JdFigure.FLOWSRC:
                    o = ((JdSubObject) f).getParent();
                    break;
                case JdFigure.HANDLE:
                    o = ((JdHandle) f).getParent();
                    break;
            }
        }
        return o;
    }

    private boolean validateLinkFig1(JdLink l, JdFigure f) {
        switch (l.getType()) {
            case JdFigure.FLOW:
                if (f == null) {
                    return true;
                }
                if (l.getFigure2() == f) {
                    return false;
                }
                if (f.getType() == JdFigure.STOCK) {
                    return true;
                }
                break;
            case JdFigure.RELATION:
                if (f == null) {
                    return false;
                }
                if (l.getFigure2() == f) {
                    return false;
                }
                if (f.getType() == JdFigure.CONSTANT || f.getType() == JdFigure.AUX
                        || f.getType() == JdFigure.DATASET
                        || f.getType() == JdFigure.STOCK) {
                    return true;
                }
                break;
        }
        return false;
    }

    private void actionCreateNode(JdNode aNode, int x, int y) {
    	if (editor.getMode()==Mode.BLACK_BOX || editor.getMode()==Mode.CLEAR_BOX) {
    		// editing the model is not allowed -> return!
    		return;
    	}
        fStart = model.getSmallFigureAt(x, y);
        if (fStart == null) { // free position?
        	editor.getSelection().addUndoPoint();            
        	model.addObject(aNode, true);
            editor.setModelChanged();
            editor.selectObject(aNode, false);
            editor.getEditorToolbar().toCursorAction();
            editor.getActionLogger().logAddAction(aNode, editor.getModelXML());
            if (editor.isSynchronized()) {
            	editor.getModelSyncControl().addNode(aNode);
            }
        }
    }

    private void actionCreateLink(JdLink aLink, int x, int y) {
    	if (editor.getMode()==Mode.BLACK_BOX || editor.getMode()==Mode.CLEAR_BOX) {
    		// editing the model is not allowed -> return!
    		return;
    	}
        fStart = model.getSmallFigureAt(x, y);
        if (validateLinkFig1(aLink, fStart)) { // can link start on object?
        	editor.getSelection().addUndoPoint();
        	allowDrag = true;
            dragPoint = JdHandle.H_P2;
            fLink = aLink;
            fOver = fStart;
            aLink.setFigure1(fStart);
            aLink.setPoint1(x, y);
            aLink.movePoint(dragPoint, x, y);
            model.addObject(aLink, false);
            // editor.setCursor(A_HANDLE);
            editor.updateCanvas();
        }
    }

    private void actionCursor(int x, int y, boolean isControlDown) {
    	fStart = model.getSmallFigureAt(x, y);
        
    	if (editor.getMode()==Mode.BLACK_BOX || editor.getMode()==Mode.CLEAR_BOX) {
    		// editing the model is not allowed -> return!
    		return;
    	}
        if (fStart == null) { // click in canvas -> remove selection
            editor.clearSelectedObjects();
            editor.startRect(x, y);
            allowDrag = true;
        } else { // click in figure -> what do we do?
            editor.getSelection().setStart(x, y);
            switch (fStart.getType()) {
                case JdFigure.STOCK:
                case JdFigure.AUX:
                case JdFigure.CONSTANT:
                case JdFigure.DATASET:
                    editor.selectObject((JdObject) fStart, isControlDown);
                    allowDrag = true;
                    break;
                case JdFigure.RELATION:
                    editor.selectObject((JdObject) fStart, isControlDown);
                    break;
                case JdFigure.FLOW:
                    editor.selectObject((JdObject) fStart, isControlDown);
                    break;
                case JdFigure.FLOWCTR:
                case JdFigure.FLOWSRC:
                    editor.selectObject(((JdSubObject) fStart).getParent(),
                            isControlDown);
                    allowDrag = true;
                    break;
                case JdFigure.HANDLE:
                    JdHandle h = (JdHandle) fStart;
                    allowDrag = true;
                    dragPoint = h.getHandleNumber();
                    fLink = h.getParent();
                    switch (dragPoint) {
                        case JdHandle.H_P1:
                            fOver = fLink.getFigure1();
                            break;
                        case JdHandle.H_P2:
                            fOver = fLink.getFigure2();
                            break;
                    }
                    break;
            }
        }
    }

    private void actionDelete(int x, int y) {
    	fStart = getObjectAt(x, y);
        if (fStart != null) {
        	editor.getSelection().addUndoPoint();
            editor.deleteFigure(((JdObject) fStart).getLabel(), false);
            editor.getEditorToolbar().toCursorAction();
        }
    }

    private void actionDragCursor(int x, int y) {
        if (fStart != null) {
        	if (fStart.isObject()) { // move selected objects
                editor.getSelection().translate(x, y, model.getLinks());
            } else if (fStart.isHandle()) { // drag link
                JdHandle h = (JdHandle) fStart;
                JdObject o = h.getParent();
                editor.getSelection().setEnhanced(false);
                if (h.getHandleNumber() == JdHandle.H_C1
                        || h.getHandleNumber() == JdHandle.H_C2) {
                    o.setEnhanced(true);
                }
                dragLink(x, y);
                if (o.isFlow()) {
                    editor.getSelection().updateLinks(o, model.getLinks());
                }
            }
            editor.updateCanvas();
        } else { // drag rectangle
            editor.extendRect(x, y);
        }
    }

    private void actionDragLink(int x, int y) {
        dragLink(x, y);
        editor.updateCanvas();
    }

    private void dragLink(int x, int y) {
        boolean b = true;
    	switch (dragPoint) {
            case JdHandle.H_P1:
                JdFigure f1 = model.getFigureAt(x, y, fLink);
                if (fOver != null) {
                    fOver.setEnhanced(false);
                }
                fOver = f1;
                b = allowConnection1(fLink, fOver);
                if (b) { // can link start on object?
                    if (fOver != null) {
                        fOver.setEnhanced(true);
                    }
                    fLink.setFigure1(fOver);
                } else {
                    fLink.setFigure1(null);
                }
                break;
            case JdHandle.H_P2:
                JdFigure f2 = model.getFigureAt(x, y, fLink);
                if (fOver != null) {
                    fOver.setEnhanced(false);
                }
                fOver = f2;
                b = allowConnection2(fLink, fOver);
                if (b) { // can link stop on object?
                    if (fOver != null) {
                        fOver.setEnhanced(true);
                    }
                    fLink.setFigure2(fOver);
                } else {
                    try {
                        fLink.setFigure2(null);
                    } catch (NullPointerException ex) {
                    }
                }
                break;
        }
        try {
            fLink.movePoint(dragPoint, x, y);
        } catch (NullPointerException ex) {
        }
    }

    private boolean validateLinkFig2(JdLink l, JdFigure f) {
        try {
            switch (l.getType()) {
                case JdFigure.FLOW:
                    if (f == null) {
                        return true;
                    }
                    if (l.getFigure1() == f) {
                        return false;
                    }
                    if (f.getType() == JdFigure.STOCK) {
                        return true;
                    }
                    break;
                case JdFigure.RELATION:
                    if (f == null) {
                        return false;
                    }
                    if (l.getFigure1() == f) {
                        return false;
                    }
                    if (f.getType() == JdFigure.STOCK) {
                    } else if (f.getType() == JdFigure.AUX
                    		// incoming relations to stocks are not allowed,
                    		// thus commenting out next line
                            //|| f.getType() == JdFigure.STOCK
                            || f.getType() == JdFigure.FLOWCTR) {
                        return true;
                    }
                    break;
            }
        } catch (NullPointerException ex) {
            return false;
        }
        return false;
    }

    private boolean allowConnection1(JdLink aLink, JdFigure fOver) {
        if (!validateLinkFig1(aLink, fOver)) {
            return false;
        }
        if (aLink.isFlow()) {
            return true;
        }
        // only one link allowed
        int n = 0;
        Enumeration<JdRelation> e = model.getRelations().elements();
        while (e.hasMoreElements() && n == 0) {
            JdLink l = (JdLink) e.nextElement();
            if (aLink != l) {
                JdFigure f1 = l.getFigure1();
                JdFigure f2 = l.getFigure2();
                if (fOver == f1 && aLink.getFigure2() == f2) {
                    n++;
                }
            }
        }
        if (n != 0) {
            return false;
        }
        // test loops
        boolean b = hasALoop(aLink, fOver, aLink.getFigure2());
        return !b;
    }

    private boolean allowConnection2(JdLink aLink, JdFigure fOver) {
        if (!validateLinkFig2(aLink, fOver)) {
            return false;
        }
        if (aLink.isFlow()) {
            return true;
        }
        // only one link allowed
        int n = 0;
        Enumeration<JdRelation> e = model.getRelations().elements();
        while (e.hasMoreElements() && n == 0) {
            JdLink l = (JdLink) e.nextElement();
            if (aLink != l) {
                JdFigure f1 = l.getFigure1();
                JdFigure f2 = l.getFigure2();
                if (f2 == fOver) {
                    if (aLink.getFigure1() == f1) {
                        n++;
                    } else if (fOver.isFlowCtr()) {
                        n++; // only one link to flowctrl
                    }
                }
            }
        }
        if (n != 0) {
            return false;
        }
        // test loops
        boolean b = hasALoop(aLink, aLink.getFigure1(), fOver);
        return !b;
    }

    private boolean hasALoop(JdLink aLink, JdFigure fStart, JdFigure fOver) {
        boolean b = false;
        Enumeration<JdRelation> e = model.getRelations().elements();
        while (e.hasMoreElements()) {
            JdLink l = (JdLink) e.nextElement();
            if (aLink != l) {
                JdFigure f1 = l.getFigure1();
                JdFigure f2 = l.getFigure2();
                if (f1 == fOver) {
                    if (f2 == fStart) {
                        return true;
                    }
                    b = hasALoop(l, fStart, f2);
                    if (b) {
                        return true;
                    }
                }
            }
        }
        return b;
    }

    private void actionEndCursor() {
        if (!allowDrag) {
            return;
        }
        if (fLink != null) {
            actionEndLink();
        }
        Rectangle r = editor.stopRect();
        if (r != null) {
            editor.selectObjects(r);
        }
    }

    private void actionEndLink() {
        if (!allowDrag) {
        	// go here when link has been started on nothing
            return;
        }
        boolean b = true;
        switch (dragPoint) {
            case JdHandle.H_P1:
                b = allowConnection1(fLink, fOver);
                if (b) { // can link stop on object?
                    fLink.setFigure1(fOver);
                } else {
                	System.out.println("actionEndLink (bot) - consume undo point");
                	editor.getSelection().consumeFirstUndoPoint();
                    model.removeObjectAndRelations(fLink);
                }
                editor.setModelChanged();
                break;
            case JdHandle.H_P2:
                b = allowConnection2(fLink, fOver);
                if (b) {
                	// go here is link starts and ends on valid objects
                    if (fLink.isRelation() && fOver.isAux()) {
                        if (((JdRelation) fLink).getRelationType() != JdRelation.R_FX) {
                            ((JdAux) fOver).setExprType(JdNode.EXPR_QUALITATIVE);
                        }
                    }
                    fLink.setFigure2(fOver);
                    editor.getEditorToolbar().toCursorAction();
                    editor.getActionLogger().logAddAction(fLink, editor.getModelXML());
                    if (editor.isSynchronized()) {
                    	editor.getModelSyncControl().addLink(fLink);
                    }
                } else {
                	// go here if link is released not on a valid object
                	System.out.println("actionEndLink (top) - consume undo point");
                	editor.getSelection().consumeFirstUndoPoint();
                    model.removeObjectAndRelations(fLink);
                }
                editor.setModelChanged();
                break;
        }
        if (fOver != null) {
        	if (fOver.isNode()) {
                editor.selectObject((JdNode) fOver, false);
            }
            fOver.setEnhanced(false);
            fOver = null;
        }
        //setEditorCursor(lastCursor);
        
        
        fLink = null;
        editor.getSelection().setEnhanced(true);
        editor.setModelChanged();
        editor.checkModel();
        editor.updateCanvas();
    }
}
