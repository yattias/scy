package eu.scy.colemo.client;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.nov.2008
 * Time: 12:04:15
 * To change this template use File | Settings | File Templates.
 */
public class AbstractMapNode extends JPanel implements MouseListener, ActionListener, MouseMotionListener, Selectable {
    protected GraphicsDiagram gDiagram;

    public AbstractMapNode(GraphicsDiagram gDiagram) {
        this.gDiagram = gDiagram;
    }

    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void actionPerformed(ActionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseDragged(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseMoved(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public GraphicsDiagram getGraphicsDiagram(){
        return gDiagram;
    }

    public void setSelected(boolean selected) {
        if(selected) {
            this.setBorder(BorderFactory.createLineBorder(Color.RED,3));
        }
        else{
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }

    public boolean isSelected() {
        //return getGraphicsDiagram().getMainFrame().getSelected()==this;
        return false;
    }
}
