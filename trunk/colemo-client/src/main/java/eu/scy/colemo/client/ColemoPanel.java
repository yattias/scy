package eu.scy.colemo.client;

import eu.scy.colemo.server.uml.UmlDiagram;
import eu.scy.colemo.server.exceptions.ClassNameAlreadyExistException;
import eu.scy.colemo.network.Person;
import eu.scy.colemo.contributions.AddClass;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2009
 * Time: 06:40:09
 * To change this template use File | Settings | File Templates.
 */
public class ColemoPanel extends JPanel {

    
    private GraphicsDiagram gDiagram;
    private JScrollPane scrollPane;


    public ColemoPanel() {

        initializeGUI();
        ApplicationController.getDefaultInstance().setColemoPanel(this);
        ApplicationController.getDefaultInstance().connect();

        UmlDiagram diagram = gDiagram.getUmlDiagram();
        addNewConcept(diagram, "c");

    }

    public GraphicsDiagram getGraphicsDiagram() {
        return gDiagram;
    }

    public void setGraphicsDiagram(GraphicsDiagram gDiagram) {
        this.gDiagram = gDiagram;
    }

    private void initializeGUI() {
        //setBackground(Color.blue);
        setLayout(new BorderLayout());
        gDiagram = new GraphicsDiagram(new UmlDiagram());
        //gDiagram.setBackground(new Color(204, 204, 204));

        ApplicationController.getDefaultInstance().setGraphicsDiagram(gDiagram);
        add(BorderLayout.CENTER, gDiagram);
        //scrollPane = new JScrollPane(gDiagram);

        //this.add(scrollPane, BorderLayout.CENTER);
    }


    public void addNewConcept(UmlDiagram diagram, String type) {

        String name = JOptionPane.showInputDialog(this, "Please type name of new concept:");
        if (name != null) {
            try {
                if (!gDiagram.getUmlDiagram().nameExist(name)) {
                    InetAddress address = null;
                    Person person = null;
                    AddClass addClass = new AddClass(name, type, "", address, person);
                    ApplicationController.getDefaultInstance().getConnectionHandler().sendObject(addClass);
                } else {
                    JOptionPane.showMessageDialog(this, "This concept already exists!");
                }
            } catch (ClassNameAlreadyExistException e) {
                e.printStackTrace();
            }
        }
        invalidate();
        validate();
        repaint();
    }

}
