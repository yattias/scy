package eu.scy.colemo.client;

import eu.scy.colemo.server.uml.ConceptMapNodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.nov.2008
 * Time: 12:03:56
 * To change this template use File | Settings | File Templates.
 */
public class GraphicsConcept extends AbstractMapNode{

    private ConceptMapNodeData umlClass;
    public JLabel nameLabel;
    public FieldLabel fieldLabel;
    public MethodLabel methodLabel;
    private int paddingX=7;
    private int paddingY=7;
    public static final int WEST=0;
    public static final int EAST=1;
    public static final int NORTH=2;
    public static final int SOUTH=3;
    private long time = System.currentTimeMillis();
    private PopUpMenu popMenu;

    public GraphicsConcept(ConceptMapNodeData conceptMapNodeData,GraphicsDiagram gDiagram){
        super(gDiagram);
        this.umlClass=conceptMapNodeData;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(new Color(204, 204, 255));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setToolTipText(conceptMapNodeData.getName()+" created by ");

        layoutComponents();

    }
    public void paint(Graphics g){
        super.paint(g);
    }

    public void setUmlClass(ConceptMapNodeData umlClass){
        this.umlClass=umlClass;
        layoutComponents();
    }
    public void layoutComponents() {
        String preName =" PRENAME";
        /*if(umlClass.getType().equals("a")) preName=new String("{ab} ");
        else if(umlClass.getType().equals("i")) preName=new String("<<i>> ");
        else preName=new String();
           */
        nameLabel=new JLabel("PRENAME");
        //fieldLabel=new FieldLabel("Fields: "+"("+umlClass.getFields().size()+")",this);
        //methodLabel=new MethodLabel("Methods: "+"("+umlClass.getMethods().size()+")",this);
        fieldLabel.setFont(new Font("sansserif", Font.BOLD, 12));
        methodLabel.setFont(new Font("sansserif", Font.BOLD, 12));

        this.removeAll();
        int height=0;
        this.add(nameLabel);

        /*if(!umlClass.getType().equals("i")){
            this.add(fieldLabel);
        } */

        setBounds();

        createPopUpMenu();
    }

    public void createPopUpMenu() {
        //popMenu= new PopUpMenu(this);
    }

    public int getHeight(){
        int height=0;
        Component[] components= getComponents();
        for (int i=0;i<components.length;i++) {
            height+=components[i].getHeight();
        }
        return height+paddingY;
    }
    public int getWidth() {
        int width=0;
        Component[] components= getComponents();
        for (int i=0;i<components.length;i++) {
            width=components[i].getWidth()>width?components[i].getWidth():width;
        }
        return width+paddingX;
    }
    public ConceptMapNodeData getUmlClass(){
        return umlClass;
    }
    public void changePosition(int deltaX,int deltaY){
        //int x=umlClass.getX()+deltaX;
        //int y=umlClass.getY()+deltaY;
        //umlClass.setX(x);
        //umlClass.setY(y);
        setBounds();
    }
    public void setBounds() {
        setBounds(20,20,30,30);
        //this.setBounds(umlClass.getX(),umlClass.getY(),100,150);
        //this.setBounds(umlClass.getX(),umlClass.getY(),300,450);
    }

    public void deleteClass() {
        //getGraphicsDiagram().deleteClass(this);
    }

    public void showMenu(int x,int y) {
        popMenu.show(this,x,y);
    }
    public void mousePressed(MouseEvent e) {
        /*if(e.getSource() instanceof FieldLabel) {
            setFieldsMaximized(!getUmlClass().showFields());
        }
        else if(e.getSource() instanceof MethodLabel) {
            setMethodsMaximized(!getUmlClass().showMethods());
        }
         */
        //MainFrame frame = getGraphicsDiagram().getMainFrame();
        //this.getGraphicsDiagram().getMainFrame().setSelected(this);
        if(e.getModifiers() == InputEvent.BUTTON3_MASK){
            showMenu(e.getX(),e.getY());
        }
    }

    public void mouseReleased(MouseEvent ae) {
        //MainFrame frame = getGraphicsDiagram().getMainFrame();

        if(ae.getModifiers() !=InputEvent.BUTTON3_MASK){
          //  Client client = getGraphicsDiagram().getMainFrame().getClient();
            //Connection connection = client.getConnection();
            //InetAddress ip = connection.getSocket().getLocalAddress();

            /*if(umlClass.isMove()){
                umlClass.setMove(false);
                ClassMoving classMoving = new ClassMoving(umlClass,ip);
                connection.send(classMoving);
            }

            getParent().repaint();
            MoveClass movedClass = new MoveClass(umlClass,ip,client.getPerson());
            connection.send(movedClass);
            */
        }
    }

    public void mouseDragged(MouseEvent e) {
        //if(e.getModifiers() != InputEvent.BUTTON3_MASK && !umlClass.isMove()){
            //umlClass.setMove(true);
            //Client client = getGraphicsDiagram().getMainFrame().getClient();
            //Connection connection = client.getConnection();
            /*ClassMoving classMoving = new ClassMoving(umlClass,connection.getSocket().getLocalAddress());
            connection.send(classMoving);*/
        //}

        this.changePosition(e.getX(),e.getY());
        this.repaint();
        getParent().repaint();
    }

    public void mouseClicked(MouseEvent arg0) {}

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {}

    public void mouseMoved(MouseEvent arg0) {}

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==popMenu.deleteClass){
            //getGraphicsDiagram().deleteClass(this);
        }
    }

    public String getClassName() {
        return umlClass.getName();
    }

    public Point getCenterPoint() {
        return (new Point(this.getX()+(this.getWidth()/2),this.getY()+(this.getHeight()/2)));
    }
    public Point getConnectionPoint(int point){
        switch(point) {
        case ConceptNode.WEST: return new Point(this.getX()-2,(int) this.getCenterPoint().getY());
        case ConceptNode.EAST: return new Point(this.getX()+this.getWidth()+2,(int) this.getCenterPoint().getY());
        case ConceptNode.NORTH: return new Point((int) this.getCenterPoint().getX(),this.getY()-2);
        case ConceptNode.SOUTH: return new Point((int) this.getCenterPoint().getX(),this.getY()+this.getHeight()+2);
        }
        return null;
    }

    public void setFieldsMaximized(boolean fieldsMaximized) {
        //umlClass.setShowFields(fieldsMaximized);
        layoutComponents();
        this.invalidate();
        this.validate();
        this.repaint();
        this.getParent().repaint();
    }

    public void setMethodsMaximized(boolean methodsMaximized) {
        //umlClass.setShowMethods(methodsMaximized);
        layoutComponents();
        this.invalidate();
        this.validate();
        this.repaint();
        this.getParent().repaint();
    }
}
