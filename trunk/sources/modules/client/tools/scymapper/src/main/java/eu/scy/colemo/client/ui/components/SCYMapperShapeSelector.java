package eu.scy.colemo.client.ui.components;

import eu.scy.colemo.client.shapes.ConceptShape;
import eu.scy.colemo.client.shapes.LinkShape;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 17.jun.2009
 * Time: 16:04:19
 * To change this template use File | Settings | File Templates.
 */
public class SCYMapperShapeSelector extends JPanel {
    private static final String FIGURES_PACKAGE = "eu.scy.colemo.client.eu.scy.colemo.client.shapes.concepts";
    private static final String CONNECTORS_PACKAGE = "eu.scy.colemo.client.eu.scy.colemo.client.shapes.links";

    private JPanel shapePanel;
    private JPanel connectorPanel;

    public SCYMapperShapeSelector() {
        setLayout(new GridLayout(2, 1));

        shapePanel = new JPanel();
        shapePanel.setBorder(new TitledBorder("Select concept style"));
        connectorPanel = new JPanel();
        connectorPanel.setBorder(new TitledBorder("Select connector style"));


        readShapes();
        readConnectors();
        add(shapePanel);
        add(connectorPanel);
    }

    private void readShapes() {
        Class[] classes = null;
        try {
            classes = getClasses(FIGURES_PACKAGE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (classes != null) {
            for (Class clazz : classes) {
                if (!ConceptShape.class.isAssignableFrom(clazz)) {
                    System.out.println("Not a ConceptShape subclass, skipping  "+clazz);
                    continue;
                }
                try {
                    ConceptShape figure = (ConceptShape) clazz.newInstance();
                    //Shape shape = figure.paint(new Rectangle(50, 50));
                    //ShapedButton b = new ShapedButton(clazz.getSimpleName(), shape);
                    //shapePanel.add(b);

                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    private void readConnectors() {

        Class[] classes = null;
        try {
            classes = getClasses(CONNECTORS_PACKAGE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (classes != null) {
            for (Class clazz : classes) {
                if (!LinkShape.class.isAssignableFrom(clazz)) {
                    System.out.println("Not a LinkShape subclass, skipping "+clazz);
                    continue;
                }
                try {
                    LinkShape figure = (LinkShape) clazz.newInstance();
                    Shape shape = figure.getShape(new Point(0, 10), new Point(150, 10));
                    System.out.println(" s.gb()= " + shape.getBounds());
                    ShapedButton b = new ShapedButton(clazz.getSimpleName(), shape);
                    connectorPanel.add(b);

                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public static Class[] getClasses(String pckgname) throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<Class>();
        // Get a File object for the package
        File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = pckgname.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " (" + directory + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (String file : files) {
                // we are only interested in .class files
                if (file.endsWith(".class")) {
                    // removes the .class extension
                    classes.add(Class.forName(pckgname + '.'  + file.substring(0, file.length() - 6)));
                }
            }
        } else {
            throw new ClassNotFoundException(pckgname
                    + " does not appear to be a valid package");
        }
        Class[] classesA = new Class[classes.size()];
        classes.toArray(classesA);
        return classesA;
    }

}
