/*
 * Created on 30.sep.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import eu.scy.colemo.network.Connection;
import eu.scy.colemo.network.Client;
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
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.graphics.ShadowRenderer;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.border.DropShadowBorder;

/**
 * @author �ystein
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicsClass extends JPanel implements Selectable, MouseListener, ActionListener, MouseMotionListener {
    private UmlClass umlClass;
    public ClassLabel nameLabel;
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
    private JPanel topPanel = new JPanel();

    private BufferedImage shadow = null;


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
        topPanel.setSize(new Dimension(0, 0));
        topPanel.setOpaque(false);

        layoutComponents();

    }


    public void setUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
        layoutComponents();
    }

    public void layoutComponents() {
        String preName;
        if (umlClass.getType().equals("a")) preName = new String("{ab} ");
        else if (umlClass.getType().equals("i")) preName = new String("<<i>> ");
        else preName = new String();

        nameLabel = new ClassLabel(this, preName);
        fieldLabel = new FieldLabel("Fields: " + "(" + umlClass.getFields().size() + ")", this);
        methodLabel = new MethodLabel("Methods: " + "(" + umlClass.getMethods().size() + ")", this);
        fieldLabel.setFont(new Font("sansserif", Font.BOLD, 12));
        methodLabel.setFont(new Font("sansserif", Font.BOLD, 12));

        this.removeAll();
        //int height = 0;
        //this.add(topPanel);

        add(nameLabel);

        /*if(!umlClass.getType().equals("i")){
              this.add(fieldLabel);
          }
          if(umlClass.showFields()){
              for(int i=0;i<umlClass.getFields().size();i++) {
                  this.add(new Field((String)umlClass.getFields().elementAt(i),this));
              }
          }
          this.add(methodLabel);
          if(umlClass.showMethods()){
              for(int i=0;i<umlClass.getMethods().size();i++) {
                  this.add(new Method((String)umlClass.getMethods().elementAt(i),this));
              }
          }
          */
        setBounds();

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


        int w = getWidth() - 30;
        int h = getHeight() - 30;
        int arc = 10;
        int shadowSize = 5;

        shadow = GraphicsUtilities.createCompatibleTranslucentImage(w, h);
        Graphics2D g2 = shadow.createGraphics();
        g2.setColor(Color.RED);
        g2.fillRoundRect(0, 0, w, h, arc, arc);
        g2.dispose();

        ShadowRenderer renderer = new ShadowRenderer(shadowSize, 0.5f, Color.GREEN);
        shadow = renderer.createShadow(shadow);

        g2 = shadow.createGraphics();
// The color does not matter, red is used for debugging
        g2.setColor(Color.BLUE);
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRoundRect(shadowSize, shadowSize, w, h, arc, arc);
        g2.dispose();

        if (shadow != null) {
            //int xOffset = (shadow.getWidth() - w) / 2;
            int xOffset = (shadow.getWidth() - 5) ;
            int yOffset = (shadow.getHeight() - 5);
            //int yOffset = (shadow.getHeight() - h) / 2;
            g2.drawImage(shadow, x - xOffset, y - yOffset, null);
        }


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
        int w = getWidth()-1;// - 6;// - 68;
        int h = getHeight()-1;// - 6;// - 68;
        int arc = 30;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(204, 204, 255));
        g2.fillRoundRect(x, y, w, h, arc, arc);

        g2.setStroke(new BasicStroke(3f));
        g2.setColor(new Color(153, 153, 255));
        g2.drawRoundRect(x, y, w, h, arc, arc);

        g2.dispose();
    }



    public void setSelected(boolean selected) {
        /* if (selected) {

            this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.DARK_GRAY));
        } else {
            this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        }
        */
    }

    public boolean isSelected() {
        return getGraphicsDiagram().getMainFrame().getSelected() == this;
    }

    public void showMenu(int x, int y) {
        popMenu.show(this, x, y);
    }

    public void mousePressed(MouseEvent e) {

        if (e.getSource() instanceof FieldLabel) {
            setFieldsMaximized(!getUmlClass().showFields());
        } else if (e.getSource() instanceof MethodLabel) {
            setMethodsMaximized(!getUmlClass().showMethods());
        }

        MainFrame frame = getGraphicsDiagram().getMainFrame();
        this.getGraphicsDiagram().getMainFrame().setSelected(this);
        if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
            showMenu(e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent ae) {
        MainFrame frame = getGraphicsDiagram().getMainFrame();

        if (ae.getModifiers() != InputEvent.BUTTON3_MASK) {
            Client client = getGraphicsDiagram().getMainFrame().getClient();
            Connection connection = client.getConnection();
            InetAddress ip = connection.getSocket().getLocalAddress();

            if (umlClass.isMove()) {
                umlClass.setMove(false);
                ClassMoving classMoving = new ClassMoving(umlClass, ip);
                connection.send(classMoving);
            }

            getParent().repaint();
            MoveClass movedClass = new MoveClass(umlClass, ip, client.getPerson());
            connection.send(movedClass);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (e.getModifiers() != InputEvent.BUTTON3_MASK && !umlClass.isMove()) {
            umlClass.setMove(true);
            Client client = getGraphicsDiagram().getMainFrame().getClient();
            Connection connection = client.getConnection();
            ClassMoving classMoving = new ClassMoving(umlClass, connection.getSocket().getLocalAddress());
            connection.send(classMoving);
        }

        this.changePosition(e.getX(), e.getY());
        this.repaint();
        getParent().repaint();
    }

    public void mouseClicked(MouseEvent arg0) {
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

}