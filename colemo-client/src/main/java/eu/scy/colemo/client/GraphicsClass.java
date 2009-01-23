/*
 * Created on 30.sep.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import eu.scy.colemo.network.Connection;
import eu.scy.colemo.network.Client;
import eu.scy.colemo.network.Person;
import eu.scy.colemo.contributions.ClassMoving;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.server.uml.UmlClass;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.InetAddress;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.graphics.ShadowRenderer;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.border.DropShadowBorder;

/**
 * @author Øystein
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicsClass extends JPanel implements Selectable, MouseListener, ActionListener, MouseMotionListener, SelectionControllerListener {
    private Logger log = Logger.getLogger("GraphicsClass.class");
    private UmlClass umlClass;
    public JLabel nameLabel;
    public FieldLabel fieldLabel;
    public MethodLabel methodLabel;
    private int paddingX = 7;
    private int paddingY = 7;
    public static final int WEST = 0;
    public static final int EAST = 1;
    public static final int NORTH = 2;
    public static final int SOUTH = 3;
    private long time = System.currentTimeMillis();
    private GraphicsDiagram gDiagram;
    private PopUpMenu popMenu;

    private BufferedImage shadow = null;
    private boolean isSelected = false;


    public GraphicsClass(UmlClass umlClass, GraphicsDiagram gDiagram) {
        this.umlClass = umlClass;
        this.gDiagram = gDiagram;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setToolTipText(umlClass.getName() + " created by " + umlClass.getAuthor());

        //setPreferredSize(new Dimension(200,200));

        setOpaque(false);
        //setBounds(100, 100, 100, 100);
        //topPanel.setBackground(Color.GREEN);

        layoutComponents();

    }


    public void setUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
        layoutComponents();
    }

    public void layoutComponents() {
        String preName;

        nameLabel = new JLabel(getUmlClass().getName());//new ClassLabel(this, preName);

        this.removeAll();


        add(nameLabel);

        setBounds();
        nameLabel.setLocation(45,45);

        createPopUpMenu();
    }

    public void createPopUpMenu() {
        popMenu = new PopUpMenu(this);
    }

    /*public int getHeight() {
        int height = 0;
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            height += components[i].getHeight();
        }
        return height + paddingY;
    } */

    /* public int getWidth() {
       int width = 0;
       Component[] components = getComponents();
       for (int i = 0; i < components.length; i++) {
           width = components[i].getWidth() > width ? components[i].getWidth() : width;
       }
       return width + paddingX;
   } */

    public UmlClass getUmlClass() {
        return umlClass;
    }

    public void changePosition(int deltaX, int deltaY) {
        int x = umlClass.getX() + deltaX;
        int y = umlClass.getY() + deltaY;
        umlClass.setX(x);
        umlClass.setY(y);
        setBounds();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        int w = getWidth() - 68;
        int h = getHeight() - 68;
        int arc = 30;
        int shadowSize = 20;

        shadow = GraphicsUtilities.createCompatibleTranslucentImage(w, h);
        Graphics2D g2 = shadow.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w, h, arc, arc);
        g2.dispose();

        ShadowRenderer renderer = new ShadowRenderer(shadowSize, 0.5f, Color.BLACK);
        shadow = renderer.createShadow(shadow);

        g2 = shadow.createGraphics();
// The color does not matter, red is used for debugging
        g2.setColor(Color.RED);
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRoundRect(shadowSize, shadowSize, w, h, arc, arc);
        g2.dispose();

    }


    public void setBounds() {
        setBounds(umlClass.getX(), umlClass.getY(), 120, 70);
        //this.setBounds(umlClass.getX(),umlClass.getY(),300,450);
    }

    public GraphicsDiagram getGraphicsDiagram() {
        return gDiagram;
    }

    public void deleteClass() {
        getGraphicsDiagram().deleteClass(this);
    }


    protected void paintComponent(Graphics g) {
        int x = 0;
        int y = 0;
        int w = getWidth() - 1;// - 68;
        int h = getHeight() - 1;// - 68;
        int arc = 30;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (shadow != null) {
            int xOffset = (shadow.getWidth() - w) / 2;
            int yOffset = (shadow.getHeight() - h) / 2;
            g2.drawImage(shadow, x - xOffset, y - yOffset, null);
        }


        g2.setColor(new Color(204, 204, 255, 200));
        g2.fillRoundRect(x, y, w, h, arc, arc);

        g2.setStroke(new BasicStroke(3f));
        if (isSelected()) {
            g2.setColor(new Color(204, 0, 255, 200));
        } else {
            g2.setColor(new Color(153, 153, 255, 200));

        }

        g2.drawRoundRect(x, y, w, h, arc, arc);

        g2.dispose();

        nameLabel.setLocation((getWidth() / 2)  -  (nameLabel.getWidth() / 2), 30);
    }


    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isSelected() {
        return this.isSelected;
        //return getGraphicsDiagram().getMainFrame().getSelected() == this;
    }

    
    public void mousePressed(MouseEvent e) {

        if (e.getSource() instanceof FieldLabel) {
            setFieldsMaximized(!getUmlClass().showFields());
        } else if (e.getSource() instanceof MethodLabel) {
            setMethodsMaximized(!getUmlClass().showMethods());
        }

        //MainFrame frame = getGraphicsDiagram().getMainFrame();
        //this.getGraphicsDiagram().getMainFrame().setSelected(this);
        /*if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
            showMenu(e.getX(), e.getY());
        } */
    }

    public void mouseReleased(MouseEvent ae) {
        log.info("Mouse Clicked!");
        if(ae.isPopupTrigger()) {
            log.info("POPUP TRIGGER!!");
            PopupMenuController.getDefaultinstance().showPopupDialog(umlClass, ae.getX(), ae.getY(), ae);
        }
        //MainFrame frame = getGraphicsDiagram().getMainFrame();

        if (ae.getModifiers() != InputEvent.BUTTON3_MASK) {
            //Client client = getGraphicsDiagram().getMainFrame().getClient();
            //Connection connection = client.getConnection();
            InetAddress ip = null;//connection.getSocket().getLocalAddress();
            Person person = null;

            if (umlClass.isMove()) {
                umlClass.setMove(false);
                ClassMoving classMoving = new ClassMoving(umlClass, ip);
                ApplicationController.getDefaultInstance().getConnectionHandler().sendObject(classMoving);
            }

            getParent().repaint();
            MoveClass movedClass = new MoveClass(umlClass, ip, person);
            ApplicationController.getDefaultInstance().getConnectionHandler().sendObject(movedClass);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (e.getModifiers() != InputEvent.BUTTON3_MASK && !umlClass.isMove()) {
            umlClass.setMove(true);
            //Client client = getGraphicsDiagram().getMainFrame().getClient();
            //Connection connection = client.getConnection();
            InetAddress adr = null;
            ClassMoving classMoving = new ClassMoving(umlClass, adr);//connection.getSocket().getLocalAddress());
            ApplicationController.getDefaultInstance().getConnectionHandler().sendObject(classMoving);
        }

        this.changePosition(e.getX(), e.getY());
        this.repaint();
        getParent().repaint();
    }

    public void mouseClicked(MouseEvent arg0) {
        log.info("Mouse Clicked!");
        if(arg0.isPopupTrigger()) {
            log.info("POPUP TRIGGE!!");
            PopupMenuController.getDefaultinstance().showPopupDialog(umlClass, arg0.getX(), arg0.getY(), arg0);
        } else {
            SelectionController.getDefaultInstance().setSelected(umlClass);
        }
    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseMoved(MouseEvent arg0) {
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == popMenu.deleteClass) {
            getGraphicsDiagram().deleteClass(this);
        }
    }

    public String getClassName() {
        return umlClass.getName();
    }

    public Point getCenterPoint() {
        return (new Point(this.getX() + (this.getWidth() / 2), this.getY() + (this.getHeight() / 2)));
    }

    public Point getConnectionPoint(int point) {
        switch (point) {
            case GraphicsClass.WEST:
                return new Point(this.getX() - 2, (int) this.getCenterPoint().getY());
            case GraphicsClass.EAST:
                return new Point(this.getX() + this.getWidth() + 2, (int) this.getCenterPoint().getY());
            case GraphicsClass.NORTH:
                return new Point((int) this.getCenterPoint().getX(), this.getY() - 2);
            case GraphicsClass.SOUTH:
                return new Point((int) this.getCenterPoint().getX(), this.getY() + this.getHeight() + 2);
        }
        return null;
    }

    public void setFieldsMaximized(boolean fieldsMaximized) {
        umlClass.setShowFields(fieldsMaximized);
        layoutComponents();
        this.invalidate();
        this.validate();
        this.repaint();
        this.getParent().repaint();
    }

    public void setMethodsMaximized(boolean methodsMaximized) {
        umlClass.setShowMethods(methodsMaximized);
        layoutComponents();
        this.invalidate();
        this.validate();
        this.repaint();
        this.getParent().repaint();
    }

    public void selectionPerformed(Object selected) {
        if(selected.equals(this) || selected.equals(umlClass)) {
            setSelected(true);
        } else {
            setSelected(false);
        }

        this.invalidate();
        this.validate();
        this.repaint();
        
    }
}