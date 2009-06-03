/*
 * Created on 01.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlDiagram;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.client.figures.LabelArrow;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Vector;

/**
 * @author Øystein
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicsDiagram extends JPanel implements MouseListener, ActionListener {
    private UmlDiagram umlDiagram;

    private static final Logger log = Logger.getLogger(GraphicsDiagram.class.getName());

    private HashSet<ConceptLink> links = new HashSet<ConceptLink>();
    private HashSet<ConceptNode> nodes = new HashSet<ConceptNode>();

    public static final int CONNECT_MODE_ON = 0;
    public static final int CONNECT_MODE_OFF = 1;

    private int connectMode = CONNECT_MODE_OFF;

    private ConceptNode source = null;
    private ConceptNode target = null;


    public GraphicsDiagram(UmlDiagram d) {
        umlDiagram = d;
        nodes = new HashSet<ConceptNode>();
        setLayout(null);
        addMouseListener(this);
    }

    public int getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(int connectMode) {
        this.connectMode = connectMode;
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

    public void setTarget(ConceptNode node) {
        if (node == null && target != null)
            target.setFillColor(ConceptNode.defaultFillColor);

        else if (node != null)
            node.setFillColor(Color.yellow);

        target = node;

    }

    public ConceptNode getNodeByClassId(String id) {
        for (ConceptNode node : nodes) {
            log.info("CHECKING: " + node.getModel().getId() + " with: " + id);
            if (node.getModel().getId().equals(id)) return node;
        }
        return null;
    }

    public ConceptNode getNodeByClass(UmlClass cls) {
        for (ConceptNode node : nodes) {
            if (node.getModel().equals(cls)) return node;
        }
        return null;
    }


    public ConceptLink getLinkByClassId(String id) {
        for (ConceptLink link : links) {
            log.info("CHECKING: " + ((UmlLink)link.getModel()).getId() + " with: " + id);
            if (((UmlLink)link.getModel()).getId().equals(id)) return link;
        }
        return null;
    }


    public void addClass(UmlClass umlClass) {
        ConceptNode node = new ConceptNode(umlClass);

        nodes.add(node);

        add(node);

        NodeConnectionListener listener = new NodeConnectionListener(this);
        node.addMouseMotionListener(listener);
        node.addMouseListener(listener);

        layoutDiagram();
    }

    public void addLink(UmlLink umlLink) {
        if (getLinkByClassId(umlLink.getId()) != null) {
            ConceptLink link = getLinkByClassId(umlLink.getId());
            log.info("LINKNAME: " + umlLink.getName());
            link.setModel(umlLink);
        } else {
            log.info("**** CREATING NEW LINK");
			ConceptLink link = new ConceptLink(umlLink);
			links.add(link);
			ConceptNode fromNode = getNodeByClassId(umlLink.getFrom());
			ConceptNode toNode = getNodeByClassId(umlLink.getTo());
			fromNode.addOutboundLink(link);
			toNode.addInboundLink(link);
			add(link);
			repaint(link.getBounds());
        }

    }

    private void layoutDiagram() {
        for (ConceptNode node : nodes) {
            UmlClass cls = node.getModel();
            node.setBounds(cls.getX(), cls.getY(), 120, 70);
        }
        for (ConceptLink link : links) {
            link.updatePosition();
        }
        repaint();
    }

    public void deleteConcept(UmlClass umlClass) {
        ConceptNode node = getNodeByClassId(umlClass.getId());
        if (node == null) {
            log.info("NODE WAS NULL, could not find...");
        } else {
            log.info("Removing node:" + umlClass.getName());
            nodes.remove(node);
            this.repaint();
        }
    }

    public void deleteLink(UmlLink umlLink) {
        //TODO: Implement this
    }

    public void updateClass(UmlClass umlClass) {
        ConceptNode node = getNodeByClassId(umlClass.getId());
        log.info("UPDATING A CLASS: " + umlClass.getId() + " name. " + umlClass.getName());
        if (umlClass.isMove()) {
            node.setBackground(new Color(236, 236, 236));
            node.setToolTipText("<html>" + umlClass.getName() + " created by " + umlClass.getAuthor() + ".<br>" +
                    "A faded class represents a class being moved by another user." + "<br>" +
                    "You can still manipulate this class, but you should not try to move it" + "<br>" +
                    "since this can interfer with the other user!" + "</html>");
        }
        if (node != null) {

            if (!umlClass.isMove()) {
                node.setBackground(new Color(212, 208, 200));
                node.setToolTipText(umlClass.getName() + " created by " + umlClass.getAuthor());
            }
        }
        repaint();
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

    public void deleteNode(ConceptNode node) {
        /* //Kalle opp alle klienter og spørre om de vil slette klassen
        //frame.getClient().getConnection().send(new StartVote());
        int n = JOptionPane.showConfirmDialog(frame, "The deletion of this class will affect" + "\n" +
                "other classes and links with a relation to it." + "\n" +
                "To continue press \"yes\" or to discuss it further press \"no\"", "DELETION WARNING!", JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            if (frame.getClient() != null) {
                frame.getClient().getConnection().send(new StartVote(frame.getClient().getConnection().getSocket().getLocalAddress(), frame.getClient().getPerson().getUserName(), node.getUmlClass().getName()));
            } else {
                removeNode(node);
            }
        }
        */
    }

    public void deleteLinksForNode(ConceptNode node) {
        // TODO: Remove links for the node in param
    }

    public void createMenu(MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem addClass = new JMenuItem("Add concept");
        addClass.addActionListener(this);
        menu.add(addClass);
        JMenuItem saveElo = new JMenuItem("Save ELO");
        saveElo.addActionListener(this);
        menu.add(saveElo);
        JMenuItem joinNewSession = new JMenuItem("Join new session");
        joinNewSession.addActionListener(this);
        menu.add(joinNewSession);

        JMenuItem cleanSession = new JMenuItem("Cleanup session");
        cleanSession.addActionListener(this);
        menu.add(cleanSession);


        menu.show(this, e.getX(), e.getY());


    }

    public ConceptLink getNearestLink(Point p, int threshold) {
        double nearestDist = Double.MAX_VALUE;
        ConceptLink nearestLink = null;
        for (ConceptLink link : links) {
            double distance = link.getDistanceFromLine(p);

            if (threshold > -1 && distance > threshold) continue;

            if (distance < nearestDist) {
                nearestDist = distance;
                nearestLink = link;
            }
        }
        return nearestLink;
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getActionCommand().equals("Add concept")) {
            ApplicationController.getDefaultInstance().getColemoPanel().addNewConcept(umlDiagram, "c");
        } else if (ae.getActionCommand().equals("Save ELO")) {
            ApplicationController.getDefaultInstance().saveELO();
        } else if (ae.getActionCommand().equals("Join new session")) {
            ApplicationController.getDefaultInstance().getColemoPanel().joinSession();
        } else if (ae.getActionCommand().equals("Cleanup session")) {
            ApplicationController.getDefaultInstance().getConnectionHandler().cleanUp();
        }


    }

    public void mousePressed(MouseEvent ae) {
        if (ae.getModifiers() == InputEvent.BUTTON3_MASK) {
            createMenu(ae);
        }
    }

    public void mouseClicked(MouseEvent event) {
        ConceptLink nearestLink = getNearestLink(event.getPoint(), 30);
        if (nearestLink != null) {
            nearestLink.requestFocus();
            SelectionController.getDefaultInstance().setSelected(nearestLink);
        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void clearAll() {
        links.clear();
        nodes.clear();
        /*Iterator it = nodes.iterator();
        while (it.hasNext()) {
            Component component = (Component) it.next();
            remove(component);
        } */
        removeAll();
        invalidate();
        validate();
        repaint();
    }

    public void updateConcept(UmlClass concept) {
        ConceptNode node = getNodeByClassId(concept.getId());
        if (node != null) {
            node.setModel(concept);
        } else {
            log.warn("NODE FOR CONCEPT : " + concept.getName() + " DOES NOT EXIST!");
        }

    }

    private final static class NodeConnectionListener implements MouseListener, MouseMotionListener {
        private static GraphicsDiagram diagram;
        private LabelArrow tempLink;

        NodeConnectionListener(GraphicsDiagram d) {
            diagram = d;
            tempLink = new LabelArrow();
            tempLink.setLineStyle(LabelArrow.STYLE_DASHED);
            tempLink.setColor(new Color(100, 100, 100));
            tempLink.setVisible(false);
            diagram.add(tempLink);
        }

        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {
            ConceptNode node = (ConceptNode) e.getSource();
            if (diagram.getConnectMode() == CONNECT_MODE_OFF && node.inConnectionArea(e.getPoint())) {
                diagram.setSource(node);
                diagram.setConnectMode(CONNECT_MODE_ON);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (diagram.getTarget() != null && diagram.getConnectMode() == GraphicsDiagram.CONNECT_MODE_ON) {
                String fromId = diagram.getSource().getModel().getId();
                String toId = diagram.getTarget().getModel().getId();

                UmlLink link = new UmlLink(fromId, toId, "Bjørge :-)");

                link.setId("" + System.currentTimeMillis());
                ApplicationController.getDefaultInstance().getConnectionHandler().sendObject(link);
                diagram.repaint();
            } else {
                ConceptNode node = (ConceptNode) e.getSource();
                UmlClass umlClass = node.getModel();
                if (umlClass.isMove()) {
                    umlClass.setMove(false);
                    MoveClass movedClass = new MoveClass(umlClass, null, null);
                    ApplicationController.getDefaultInstance().getConnectionHandler().sendObject(movedClass);
                }
            }
            diagram.setConnectMode(CONNECT_MODE_OFF);
            tempLink.setVisible(false);
        }

        public void mouseEntered(MouseEvent e) {
            ConceptNode node = (ConceptNode) e.getSource();
            if (diagram.getConnectMode() == GraphicsDiagram.CONNECT_MODE_ON && !node.equals(diagram.getSource())) {
                diagram.setTarget(node);
            }
        }

        public void mouseExited(MouseEvent e) {
            diagram.setTarget(null);
            tempLink.setVisible(false);
        }

        public void mouseDragged(MouseEvent e) {
            ConceptNode node = (ConceptNode) e.getSource();
            if (diagram.getConnectMode() != CONNECT_MODE_ON) {
                UmlClass umlClass = node.getModel();
                umlClass.setMove(true);
                int x = node.getX() + e.getX() - (node.getWidth() / 2);
                int y = node.getY() + e.getY() - (node.getHeight() / 2);
                node.setLocation(x, y);
                umlClass.setX(x);
                umlClass.setY(y);

                // Uncomment for real-time moving
                //MoveClass movedClass = new MoveClass(umlClass, null, null);
                //ApplicationController.getDefaultInstance().getConnectionHandler().sendObject(movedClass);
            } else {
                Point relPoint = node.getLocation();
                relPoint.translate(e.getX(), e.getY());

                // Draw temp arrow
                tempLink.setFrom(node.getLinkConnectionPoint(tempLink.findDirection(node.getCenterPoint(), relPoint)));
                tempLink.setVisible(true);
                tempLink.setTo(relPoint);

                ConceptNode target = diagram.getTarget();
                if (target != null) {
                    int relX = (int) relPoint.getX() - target.getX();
                    int relY = (int) relPoint.getY() - target.getY();
                    target.setActiveConnectionPoint(target.getConnectionEdge(new Point(relX, relY)));
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    public HashSet<ConceptLink> getLinks() {
        return links;
    }

    public void setLinks(HashSet<ConceptLink> links) {
        this.links = links;
    }

    public HashSet<ConceptNode> getNodes() {
        return nodes;
    }

    public void setNodes(HashSet<ConceptNode> nodes) {
        this.nodes = nodes;
    }
}