package eu.scy.scymapper.impl.ui.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.model.INodeModelListener;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;
import eu.scy.scymapper.impl.model.ComboNodeLinkModel;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.shapes.links.Arrowhead;
import eu.scy.scymapper.impl.shapes.links.QuadCurvedLine;
import eu.scy.scymapper.impl.ui.Localization;
import org.apache.log4j.Logger;

public class ComboConceptLinkView extends LinkView implements INodeModelListener {

    private final static Logger logger = Logger.getLogger(ComboConceptLinkView.class);
	
    private JComboBox comboBox;

    JPanel debugPanel;

    private JSlider slider;

    private ISCYMapperToolConfiguration conf;

    private JPanel panel;

    private JButton switchButton;

    public ComboConceptLinkView(ILinkController controller, final ComboNodeLinkModel model) {
        super(controller, model);
        conf = SCYMapperToolConfiguration.getInstance();

        // I want to observe changes in my connected nodes
        if (model.getFromNode() != null)
            model.getFromNode().addListener(this);
        if (model.getToNode() != null)
            model.getToNode().addListener(this);
        panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(0, 0, 0, 0));
//        panel.setBorder(BorderFactory.createTitledBorder("Relation"));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        comboBox = new JComboBox();


        comboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (comboBox.getSelectedIndex() == 0) {
                        Arrow a = new Arrow();
                        a.setHead(new Arrowhead(10));
                        a.setTail(new Arrowhead(10));


                        model.setShape(a);
                        if (switchButton != null) {
                            switchButton.setVisible(false);
                        }

                    } else {
                        Arrow a = new Arrow();
                        a.setHead(new Arrowhead(10));
                        a.setTail(null);
                        model.setShape(a);
                        if (switchButton != null) {
                            switchButton.setVisible(true);
                        }
                    }
                    model.setLabel(comboBox.getSelectedItem().toString());
                }

            }
        });
        String modelLabel = model.getLabel();
        String selectedOption = null;
        for (String option : model.getOptions()) {
            comboBox.addItem(option);
            if (option.equals(modelLabel)) {
                selectedOption = option;
            }
        }
        if (selectedOption != null) {
            comboBox.setSelectedItem(selectedOption);
        }
        switchButton = new JButton("<>");
        switchButton.setVisible(false);
        switchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                INodeModel formerToNode = model.getToNode();
                INodeModel formerFromNode = model.getFromNode();
                Point formerToPoint = model.getTo();
                Point formerFromPoint = model.getFrom();
                model.setFromNode(formerToNode);
                model.setToNode(formerFromNode);
                model.setFrom(formerToPoint);
                model.setTo(formerFromPoint);
                model.edgeFlipped();
                model.notifyUpdated();

            }
        });

        panel.add(comboBox, BorderLayout.CENTER);
        panel.add(switchButton, BorderLayout.EAST);
        add(panel);
        comboBox.setToolTipText(comboBox.getSelectedItem().toString());
        comboBox.setMinimumSize(new Dimension(100, 20));
        comboBox.setMinimumSize(new Dimension(100, 20));

        if (conf.isDebug()) {
            addCurveSlider();
        }

        updatePosition();
        layoutComponents();
    }

    private void addCurveSlider() {
        ILinkShape linkShape = getModel().getShape();
        if (!(linkShape instanceof QuadCurvedLine))
            return;

        slider = new JSlider();
        slider.setExtent(1);
        slider.setPaintTicks(true);
        slider.setMaximum(100);
        slider.setMinimum(1);
        slider.setBounds(0, 0, 100, 25);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                ILinkShape linkShape = getModel().getShape();
                if (linkShape instanceof QuadCurvedLine) {
                    logger.debug("source.getValue() = " + source.getValue());
                    double curving = ((double) source.getValue()) / 100d;
                    ((QuadCurvedLine) linkShape).setCurving(curving);
                    logger.debug("curving = " + curving);
                    repaint();
                }
            }
        });
        add(slider);
    }

    public JComboBox getComboBox(){
        return comboBox;
    }

    @Override
    public boolean contains(int x, int y) {
        for (Component component : getComponents()) {
            if (component.getBounds().contains(x, y))
                return true;
        }
        return super.contains(x, y);
    }

    //
    // public Dimension calculateStringBounds(JTextArea textComponent,
    // int breakWidth) {
    // FontMetrics fm = textComponent.getFontMetrics(textComponent.getFont());
    // FontRenderContext frc = fm.getFontRenderContext();
    // String text = textComponent.getText();
    // if (text.equals(""))
    // text = " ";
    // String[] lines = StringUtils.splitPreserveAllTokens(text,
    // System.getProperty("line.separator"));
    // int numLines = 0;
    // double maxWidth = 0.0;
    // for (String line : lines) {
    // // Ignore blank lines
    // if (line.equals(""))
    // line = " ";
    //
    // AttributedString str = new AttributedString(line);
    // AttributedCharacterIterator lineIt = str.getIterator();
    // int paragraphStart = lineIt.getBeginIndex();
    // int paragraphEnd = lineIt.getEndIndex();
    // LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(lineIt, frc);
    // lineMeasurer.setPosition(paragraphStart);
    // // Get lines until the entire line has been iterated over.
    // while (lineMeasurer.getPosition() < paragraphEnd) {
    //
    // TextLayout layout = lineMeasurer.nextLayout(breakWidth);
    //
    // double w = layout.getBounds().getWidth();
    // if (w > maxWidth)
    // maxWidth = w;
    // numLines++;
    // }
    // }
    // int lineHeight = fm.getMaxAscent() + fm.getMaxDescent()
    // + fm.getLeading();
    // return new Dimension((int) Math.ceil(maxWidth), numLines * lineHeight);
    // }

    private void layoutComponents() {

        Rectangle innerBounds = new Rectangle(getInnerBounds());
        innerBounds.translate(-getX(), -getY());

        if (innerBounds.width < 150) {
            double diff = innerBounds.width - 150;
            innerBounds.x += diff / 2d;
            innerBounds.width = 150;
        }
        if (innerBounds.height < 50) {
            double diff = innerBounds.height - 50;
            innerBounds.y += diff / 2;
            innerBounds.height = 50;
        }

        if (conf.isDebug()) {
            if (debugPanel == null) {
                debugPanel = new JPanel();
                debugPanel.setOpaque(false);
                debugPanel.setBorder(BorderFactory.createLineBorder(Color.red, 1));
                add(debugPanel);
            }
            debugPanel.setBounds(innerBounds);
        }

        Dimension prefSize = new Dimension(180, 20);

        Insets insets = panel.getInsets();
        Insets borderInsets = panel.getBorder().getBorderInsets(panel);
        prefSize.width += insets.left + insets.right + borderInsets.left + borderInsets.right;
        prefSize.height += insets.top + insets.bottom + borderInsets.top + borderInsets.bottom;

        if (prefSize.width < 100)
            prefSize.width = 100;
        else if (prefSize.width > innerBounds.width)
            prefSize.width = innerBounds.width;

        if (prefSize.height < 25)
            prefSize.height = 25;

        Point relFrom = new Point(getModel().getFrom());
        relFrom.translate(-getX(), -getY());

        Point relTo = new Point(getModel().getTo());
        relTo.translate(-getX(), -getY());
        //
        // // This is the point that should be the x,y of the text box
        Point2D casteljaPt = getModel().getShape().getDeCasteljauPoint(relFrom, relTo, 0.5);
        int x = (int) casteljaPt.getX();
        int y = (int) casteljaPt.getY();

        x -= prefSize.width / 2;
        y -= prefSize.height / 2;
        //
        if (x < innerBounds.x)
            x = innerBounds.x;
        if (y < innerBounds.y)
            y = innerBounds.y;

        if (prefSize.height > innerBounds.height) {
            prefSize.height = innerBounds.height;
        }

        while (x + prefSize.width > innerBounds.x + innerBounds.width) {
            x--;
        }
        while (y + prefSize.height > innerBounds.y + innerBounds.height) {
            y--;
        }
        panel.setBounds(x, y, 200, 30);
        panel.revalidate();
    }

    @Override
    public void labelChanged(ILinkModel link) {

        layoutComponents();
        repaint();
    }

    @Override
    public void moved(INodeModel node) {
        updatePosition();
        layoutComponents();
    }

    @Override
    public void resized(INodeModel node) {
        updatePosition();
        layoutComponents();
    }

    // Do nothing when these events happens in one of my nodes

    @Override
    public void labelChanged(INodeModel node) {
        logger.debug("label changed");
    }

    @Override
    public void shapeChanged(INodeModel node) {}

    @Override
    public void styleChanged(INodeModel node) {}

    @Override
    public void selectionChanged(INodeModel conceptNode) {}

    @Override
    public void deleted(INodeModel nodeModel) {

    }

}
