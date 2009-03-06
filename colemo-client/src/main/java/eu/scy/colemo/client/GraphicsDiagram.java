/*
 * Created on 01.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;

import eu.scy.colemo.server.uml.*;

/**
 * @author Øystein
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicsDiagram extends JPanel implements MouseListener, ActionListener {
    private UmlDiagram umlDiagram;
    private List links;
    private Vector associate;
    private Hashtable components;

    public static final int CONNECT_MODE_ON = 0;
    public static final int CONNECT_MODE_OFF = 1;

    private int connectMode = CONNECT_MODE_OFF;

    private ConceptNode source = null;
    private ConceptNode target = null;

    public int getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(int connectMode) {
        this.connectMode = connectMode;
        if (connectMode == CONNECT_MODE_OFF) {
            //setTarget(null);
            //setSource(null);
        }
    }


    public ConceptNode getSource() {
        return source;
    }

    public void setSource(ConceptNode source) {
        this.source = source;
    }

    public ConceptNode getTarget() {
        return target;
    }

    public void setTarget(ConceptNode target) {
        if (target == null) {
            System.out.println("Setting target to null");
        } else {
            System.out.println("Setting target: " + target.getName());
        }

        this.target = target;
    }

    public ConceptNode getConceptMapNode(String id) {
        return (ConceptNode) components.get(id);
    }

    public GraphicsDiagram(UmlDiagram umlDiagram) {
        this.umlDiagram = umlDiagram;
        this.setLayout(null);
        addMouseListener(this);
        links = new ArrayList();
        associate = new Vector();
        components = new Hashtable();
    }

    public void addClass(UmlClass umlClass) {
        ConceptNode gClass = new ConceptNode(umlClass, this);

        components.put(gClass.getUmlClass().getId(), gClass);
        System.out.println("Graphics diagram: ADDED " + umlClass.getName() + "; " + umlClass.getId() + " to components: " + components);
        this.add(gClass);
        //updatePopUpMenus();
        gClass.invalidate();
        gClass.validate();
        gClass.repaint();
        invalidate();
        validate();
        repaint();
        SelectionController.getDefaultInstance().addSelectionControllerListnenr(gClass);
    }

    public void addConceptMapNodeData(ConceptMapNodeData conceptMapNodeData) {
        GraphicsConcept gClass = new GraphicsConcept(conceptMapNodeData, this);
        components.put(gClass.getUmlClass().getName(), gClass);
        this.add(gClass);
        //updatePopUpMenus();
        gClass.invalidate();
        gClass.validate();
        gClass.repaint();
    }

    public void addLink(UmlLink umlLink) {
        try {
            LabeledLink link = new LabeledLink();
            ConceptNode fromNode = getClass(umlLink.getFrom());
            ConceptNode toNode = getClass(umlLink.getTo());
            fromNode.addOutboundLink(link);
            toNode.addInboundLink(link);
            add(link);
            link.setFrom(fromNode.getCenterPoint());
            link.setTo(toNode.getCenterPoint());
        } catch (Exception e) {
            System.out.println("Evil bug...");
        }

    }

   /* public void addAssociation(UmlAssociation umlAssociation) {
        GraphicsAssociation gAss = new GraphicsAssociation(umlAssociation, this);
        components.put(gAss.getFrom().getUmlClass().getName() + gAss.getTo().getUmlClass().getName(), gAss);
        associate.add(gAss);
        gAss.paint(getGraphics());
    }*/

    public void deleteClass(UmlClass umlClass) {
        ConceptNode gClass = (ConceptNode) components.remove(umlClass.getName());
        this.remove(gClass);
        this.repaint();
    }

    public void deleteLink(UmlLink umlLink) {
        GraphicsLink gLink = (GraphicsLink) components.remove(umlLink.getFrom() + umlLink.getTo());
        links.remove(gLink);
        this.repaint();
    }

    public void deleteAssociation(UmlAssociation umlAssociation) {
        GraphicsAssociation gAss = (GraphicsAssociation) components.remove(umlAssociation.getFrom() + umlAssociation.getTo());
        associate.remove(gAss);
        this.repaint();
    }

    public void updateClass(UmlClass umlClass) {
        ConceptNode gClass = (ConceptNode) components.get(umlClass.getId());
        System.out.println("UPDATIN A CLASS: " + umlClass.getId() + " name. " + umlClass.getName());
        if (umlClass.isMove()) {
            gClass.setBackground(new Color(236, 236, 236));
            gClass.setToolTipText("<html>" + umlClass.getName() + " created by " + umlClass.getAuthor() + ".<br>" +
                    "A faded class represents a class being moved by another user." + "<br>" +
                    "You can still manipulate this class, but you should not try to move it" + "<br>" +
                    "since this can interfer with the other user!" + "</html>");
        }
        if (gClass != null) {


            if (!umlClass.isMove()) {

                gClass.setBackground(new Color(212, 208, 200));
                gClass.setToolTipText(umlClass.getName() + " created by " + umlClass.getAuthor());
            }
            gClass.layoutComponents();
            gClass.invalidate();
            gClass.validate();
            gClass.repaint();
        }
        this.repaint();
    }

    public void renameClass(UmlClass umlClass) {
        //Hente ut evt linker og rename de
        ConceptNode gClass = (ConceptNode) components.remove(umlClass.getName());
        String oldName = umlClass.getName();

        components.put(gClass.getUmlClass().getName(), gClass);
        findLink(oldName, gClass.getUmlClass().getName());

        gClass.layoutComponents();
        gClass.invalidate();
        gClass.validate();
        gClass.repaint();

        this.repaint();

    }

    //Kalles kun fra renameClass, sjekker om klassen som er renamet har linker
    //Om den har det må alle tilhørende linker renames
    //Tar seg av både extensions og associations
    public void findLink(String oldName, String newName) {
        for (Enumeration e = components.elements(); e.hasMoreElements();) {
            Object o = e.nextElement();
            if (o instanceof ConceptNode) {
                ConceptNode gClass = (ConceptNode) o;
                if (components.get(oldName + gClass.getUmlClass().getName()) != null) {
                    if (components.get(oldName + gClass.getUmlClass().getName()) instanceof GraphicsLink) {
                        GraphicsLink gLink = (GraphicsLink) components.remove(oldName + gClass.getUmlClass().getName());
                        links.remove(gLink);

                        components.put(newName + gClass.getUmlClass().getName(), gLink);
                        links.add(gLink);
                        gLink.paint(getGraphics());
                    } else if (components.get(oldName + gClass.getUmlClass().getName()) instanceof GraphicsAssociation) {
                        GraphicsAssociation gAss = (GraphicsAssociation) components.remove(oldName + gClass.getUmlClass().getName());
                        associate.remove(gAss);

                        components.put(newName + gClass.getUmlClass().getName(), gAss);
                        associate.add(gAss);
                        gAss.paint(getGraphics());
                    }
                } else if (components.get(gClass.getUmlClass().getName() + oldName) != null) {
                    if (components.get(gClass.getUmlClass().getName() + oldName) instanceof GraphicsLink) {
                        GraphicsLink gLink = (GraphicsLink) components.remove(gClass.getUmlClass().getName() + oldName);
                        links.remove(gLink);

                        components.put(gClass.getUmlClass().getName() + newName, gLink);
                        links.add(gLink);
                        gLink.paint(getGraphics());
                    } else if (components.get(gClass.getUmlClass().getName() + oldName) instanceof GraphicsAssociation) {
                        GraphicsAssociation gAss = (GraphicsAssociation) components.remove(gClass.getUmlClass().getName() + oldName);
                        associate.remove(gAss);

                        components.put(gClass.getUmlClass().getName() + newName, gAss);
                        associate.add(gAss);
                        gAss.paint(getGraphics());
                    }
                }
                gClass.layoutComponents();
                //gClass.createPopUpMenu();
                gClass.invalidate();
                gClass.validate();
                gClass.repaint();
            }
        }
    }

    /*public void updatePopUpMenus() {
        for (Enumeration e = components.elements(); e.hasMoreElements();) {
            Object o = e.nextElement();
            if (o instanceof ConceptNode) {
                ConceptNode gClass = (ConceptNode) o;
            }
        }

    } */

    public void paint(Graphics g) {
        super.paint(g);
        paintLinks(g);
        //paintAssociations(g);
    }

    public void paintLinks(Graphics g) {
        for (int i = 0; i < links.size(); i++) {
            GraphicsLink current = (GraphicsLink) links.get(i);
            current.paint(g);
            add(current.getLabelComponent());
            current.getLabelComponent().setBounds(current.getLabelComponentXPos(),current.getLabelComponentYPos(), 100,20);
            current.getLabelComponent().revalidate();
        }
    }

    public void createPopUpMenus() {
        /*Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof ConceptNode) {
                ((ConceptNode) components[i]).createPopUpMenu();
            }
        } */
    }

    public ConceptNode getClass(String name) {
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof ConceptNode) {
                ConceptNode gc = (ConceptNode) components[i];
                if (((ConceptNode) components[i]).getClassName().equals(name)) {
                    return (ConceptNode) components[i];
                }
            }
        }
        return null;
    }

    public Vector getAllClassNames() {
        Vector classNames = new Vector();

        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof ConceptNode) {
                classNames.add(((ConceptNode) components[i]).getClassName());
            }
        }
        return classNames;
    }

    public UmlDiagram getUmlDiagram() {
        return umlDiagram;
    }

    /**
     * called locally
     */
    public void setUmlDiagram(UmlDiagram umlDiagram, String action) {
        this.umlDiagram = umlDiagram;
    }

    /**
     * called from server
     */
    public void updateUmlDiagram(UmlDiagram umlDiagram) {
        this.umlDiagram = umlDiagram;
        Vector v = umlDiagram.getComponents();
        for (int i = 0; i < v.size(); i++) {
            Object o = v.elementAt(i);
            if (o instanceof UmlClass) {
                addClass((UmlClass) o);
            }
            if (o instanceof UmlLink) {
                addLink((UmlLink) o);
            }
            /*if (o instanceof UmlAssociation) {
                addAssociation((UmlAssociation) o);
            } */
        }
    }

    public void deleteClass(ConceptNode gClass) {
        /* //Kalle opp alle klienter og spørre om de vil slette klassen
        //frame.getClient().getConnection().send(new StartVote());
        int n = JOptionPane.showConfirmDialog(frame, "The deletion of this class will affect" + "\n" +
                "other classes and links with a relation to it." + "\n" +
                "To continue press \"yes\" or to discuss it further press \"no\"", "DELETION WARNING!", JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            if (frame.getClient() != null) {
                frame.getClient().getConnection().send(new StartVote(frame.getClient().getConnection().getSocket().getLocalAddress(), frame.getClient().getPerson().getUserName(), gClass.getUmlClass().getName()));
            } else {
                removeClass(gClass);
            }
        }
        */
    }

    public void removeClass(ConceptNode gClass) {
        /*//Client client = gClass.getGraphicsDiagram().getMainFrame().getClient();
        //Client client = ApplicationController.getDefaultInstance().getClient();
        Connection connection = client.getConnection();
        InetAddress ip = connection.getSocket().getLocalAddress();

        Vector components = umlDiagram.getComponents();
        for (int i = 0; i < components.size(); i++) {
            if (components.elementAt(i) instanceof UmlLink) {
                UmlLink current = (UmlLink) components.elementAt(i);

                if (current.getFrom().equals(gClass.getClassName()) || current.getTo().equals(gClass.getClassName())) {
                    DeleteLink deleteLink = new DeleteLink(current, ip, client.getPerson());
                    connection.send(deleteLink);
                }
            }
            if (components.elementAt(i) instanceof UmlAssociation) {
                UmlAssociation current = (UmlAssociation) components.elementAt(i);

                if (current.getFrom().equals(gClass.getClassName()) || current.getTo().equals(gClass.getClassName())) {
                    DeleteAssociation deleteAssociation = new DeleteAssociation(current, ip, client.getPerson());
                    connection.send(deleteAssociation);
                }
            }
        }

        DeleteClass deleteClass = new DeleteClass(gClass.getUmlClass(), ip, client.getPerson());
        connection.send(deleteClass);
        */
    }

    public void deleteLinks(ConceptNode gClass) {
        /*
        Client client = ApplicationController.getDefaultInstance().getClient();
        //gClass.getGraphicsDiagram().getMainFrame().getClient();
        Connection connection = client.getConnection();
        InetAddress ip = connection.getSocket().getLocalAddress();

        Vector components = umlDiagram.getComponents();
        for (int i = 0; i < components.size(); i++) {
            if (components.elementAt(i) instanceof UmlLink) {
                UmlLink current = (UmlLink) components.elementAt(i);
                if (current.getFrom() == gClass.getClassName() || current.getTo() == gClass.getClassName()) {

                    DeleteLink deleteLink = new DeleteLink(current, ip, client.getPerson());
                    //gClass.getGraphicsDiagram().getMainFrame().getClient().getConnection().send(deleteLink);
                    ApplicationController.getDefaultInstance().getClient().getConnection().send(deleteLink);
                }
            }
        }
        */
    }

    /*public MainFrame getMainFrame() {
        return frame;
    } */

    public void createMenu(MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem addClass = new JMenuItem("Add concept");
        addClass.addActionListener(this);
        menu.add(addClass);
        menu.show(this, e.getX(), e.getY());

    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getActionCommand().equals("Add concept")) {
            ApplicationController.getDefaultInstance().getColemoPanel().addNewConcept(umlDiagram, "c");
        }
    }

    public void mousePressed(MouseEvent ae) {
        if (ae.getModifiers() == InputEvent.BUTTON3_MASK) {
            createMenu(ae);
        }
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

}