package eu.scy.scymapper.impl.ui.diagram;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Logger;

import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.model.INodeModelListener;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleListener;
import eu.scy.scymapper.impl.ui.Localization;

/**
 * Created by IntelliJ IDEA. User: Bjorge Naess Date: 22.jun.2009 Time: 19:27:37
 */
public class RichNodeView extends NodeViewComponent implements INodeModelListener, INodeStyleListener {

    private final static Logger logger = Logger.getLogger(RichNodeView.class);

    private static final String RESIZEHANDLE_FILENAME = "resize.png";

    protected static final int MAX_TEXTLENGTH = 100;

    protected JTextPane labelTextPane;

    protected JComponent resizeHandle;

    //protected JScrollPane labelScroller;

    private boolean isEditing;

    private Border selectionBorder;

    public RichNodeView(INodeController controller, INodeModel model) {

        super(controller, model);

        // Subscribe to events in the model
        getModel().addListener(this);

        // Subscribe to events in the models style
        getModel().getStyle().addStyleListener(this);

        setSize(model.getSize());
        setLocation(model.getLocation());

        setOpaque(false);
        setLayout(null);
        setFocusable(true);

        @SuppressWarnings("serial")
        DefaultStyledDocument document = new DefaultStyledDocument() {

            @Override
            public void insertString(int offs, String str, AttributeSet a)
                  throws BadLocationException {
              final int newLength = getLength() + str.length();
              if (newLength > MAX_TEXTLENGTH) {
                str = str.substring(0, MAX_TEXTLENGTH - getLength());
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        if (labelTextPane!=null){
                            labelTextPane.setBackground(Color.RED);
                        }
                    }
                });
              }

              super.insertString(offs, str, a);
            }


        };
        labelTextPane = new JTextPane(document);
        try {
            SimpleAttributeSet set = new SimpleAttributeSet();
            set.addAttribute(StyleConstants.ParagraphConstants.Alignment, StyleConstants.ParagraphConstants.ALIGN_CENTER);
            StyleConstants.setFontSize(set, 14);
            StyleConstants.setFontFamily(set, "Times New Roman");
            document.setParagraphAttributes(0, 1, set, true);
            document.insertString(0, getModel().getLabel(), set);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        labelTextPane.setForeground(getModel().getStyle().getForeground());
        labelTextPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        //labelScroller = new JScrollPane(labelTextPane);
        labelTextPane.setMargin(new Insets(0, 0, 0, 0));
        labelTextPane.setBorder(BorderFactory.createEmptyBorder());
        labelTextPane.setOpaque(false);
        //labelScroller.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	labelTextPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        setLabelEditable(false, true);

        //add(labelScroller);
	add(labelTextPane);

        MouseListener dblClickListener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    setLabelEditable(true);
                }
            }
        };
        addMouseListener(dblClickListener);

        labelTextPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                setLabelEditable(true);
            }
        });

        labelTextPane.addFocusListener(new FocusAdapter() {

            @Override
            public synchronized void focusLost(FocusEvent e) {
                setLabelEditable(false);
                labelTextPane.setText(labelTextPane.getText().trim());
                if (!labelTextPane.getText().equals(getModel().getLabel())) {
                    getController().setLabel(labelTextPane.getText());
                }
            }
        });
        labelTextPane.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    RichNodeView.this.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                layoutComponents();
                //Back to original foreground color....
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        labelTextPane.setBackground(Color.WHITE);
                    }
                });

            }

        });

        logger.debug("Can resize: " + model.getConstraints().getCanResize());
        if (model.getConstraints().getCanResize()) {
            resizeHandle = createResizeHandle();
            add(resizeHandle);

            MouseListener resizeHandleListener = new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    resizeHandle.setVisible(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    resizeHandle.setVisible(false);
                }
            };

            addMouseListener(resizeHandleListener);
            resizeHandle.addMouseListener(resizeHandleListener);

            resizeHandle.addMouseMotionListener(new MouseAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    Dimension size = getModel().getSize();
                    size.height += e.getPoint().y;
                    size.width += e.getPoint().x;
                    getController().setSize(size);
                }
            });
        }

        // setToolTipText(model.getId());

        labelTextPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                // Create a border using the inverted background color
                int c = getModel().getStyle().getBackground().getRGB() ^ 0xFFFFFF;
                if (!isEditing) {
                    //labelScroller.setBorder(BorderFactory.createLineBorder(new Color(c), 1));
                    labelTextPane.setBorder(BorderFactory.createLineBorder(new Color(c), 1));
		}
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isEditing) {
                    //labelScroller.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                    labelTextPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		}
            }
        });
        labelTextPane.setToolTipText(Localization.getString("Mainframe.ConceptMap.NodeLabel.Tooptip"));
        layoutComponents();
	model.setSize(model.getSize());
	this.revalidate();
    }

    void setLabelEditable(boolean editable, final boolean selected) {

        if (!getModel().getConstraints().getCanEditLabel())
            editable = false;

//        labelScroller.setOpaque(editable);
	labelTextPane.setOpaque(editable);
//        labelScroller.setBorder(editable ? BorderFactory.createEtchedBorder() : BorderFactory.createEmptyBorder(1, 1, 1, 1));
        labelTextPane.setBorder(editable ? BorderFactory.createEtchedBorder() : BorderFactory.createEmptyBorder(1, 1, 1, 1));

        labelTextPane.setFocusable(editable);
        labelTextPane.setEditable(editable);
        labelTextPane.setOpaque(editable);

        labelTextPane.setForeground(editable ? Color.BLACK : getModel().getStyle().getForeground());
        labelTextPane.setBackground(editable ? Color.WHITE : getModel().getStyle().getBackground());

        // labelTextarea.setCaretPosition(caretPos);
        if (editable) {
            SwingUtilities.invokeLater(new Thread() {

                @Override
                public void run() {
                    labelTextPane.requestFocus();
                    if (selected)
                        labelTextPane.selectAll();
                }
            });
        }
        if (!editable)
            labelTextPane.select(0, 0);

        isEditing = editable;
    }

    void setLabelEditable(boolean editable) {
        setLabelEditable(editable, false);
    }

    public String getLabel() {
        return this.labelTextPane.getText();
    }

    private JComponent createResizeHandle() {
        if (resizeHandle == null) {
            try {
                URL uri = getClass().getResource(RESIZEHANDLE_FILENAME);
                if (uri == null)
                    throw new FileNotFoundException("File " + RESIZEHANDLE_FILENAME + " not found");
                BufferedImage i = ImageIO.read(uri);
                resizeHandle = new JLabel(new ImageIcon(i));
                resizeHandle.setSize(15, 15);
            } catch (IOException e) {
                JLabel button = new JLabel(">");
                Font f = new Font("Times New Roman", Font.PLAIN, 10);
                button.setFont(f);
                button.setSize(10, 10);
                resizeHandle = button;
            }
        }
        resizeHandle.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
        resizeHandle.setVisible(false);
        return resizeHandle;
    }

    void layoutComponents() {

        FontMetrics f = labelTextPane.getFontMetrics(labelTextPane.getFont());
        int width = f.stringWidth(labelTextPane.getText()) + 10;

        int maxHeight = getHeight() - 20; // 10 px spacing on each side
        int maxWidth = getWidth() - 20; // 10 px spacing on each side

        if (width < getModel().getStyle().getMinWidth())
            width = getModel().getStyle().getMinWidth();
        // Add some space
        else
            width = (width + 10 > maxWidth) ? maxWidth : width + 10;

        int height = labelTextPane.getPreferredScrollableViewportSize().height + 10;
	//int height = labelTextPane.getPreferredSize().height + 10;

        if (height > maxHeight) {
            height = maxHeight;
//            labelScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        } else {
//  /          labelScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }

        //labelTextPane.setSize(width, height);
        labelTextPane.setVisible(!getModel().isLabelHidden());

        double x = ((maxWidth / 2) - (width / 2)) + 10;
        double y = ((maxHeight / 2d) - (height / 2d)) + 10;

        labelTextPane.setBounds((int) x, (int) y, width - 2, height-2);
//        labelScroller.setBounds((int) x, (int) y, width - 2, height-2);
//	labelScroller.revalidate();
	labelTextPane.revalidate();

        if (resizeHandle != null) {
            resizeHandle.setBounds(getWidth() - resizeHandle.getWidth() - 2, getHeight() - resizeHandle.getHeight() - 2, resizeHandle.getWidth(), resizeHandle.getHeight());

            resizeHandle.setForeground(getForeground());
            resizeHandle.setBackground(getBackground());
        }
    }

    @Override
    public void moved(INodeModel model) {
        setLocation(model.getLocation());
    }

    @Override
    public void resized(INodeModel model) {
        setSize(model.getSize());
        layoutComponents();
    }

    @Override
    public void labelChanged(INodeModel node) {
        labelTextPane.setText(node.getLabel());
        layoutComponents();
    }

    @Override
    public void shapeChanged(INodeModel node) {
        repaint();
    }

    @Override
    public void styleChanged(INodeModel node) {
        labelTextPane.setForeground(node.getStyle().getForeground());
        labelTextPane.setBackground(node.getStyle().getBackground());
        labelTextPane.setOpaque(false);
        //labelScroller.getViewport().setOpaque(false);
        repaint();
    }

    @Override
    public void selectionChanged(INodeModel conceptNode) {
        if (selectionBorder == null) {
            selectionBorder = new SelectionBorder();
        }
        setBorder(conceptNode.isSelected() ? selectionBorder : BorderFactory.createEmptyBorder());
        if (conceptNode.isSelected())
            requestFocus();
    }

    @Override
    public void deleted(INodeModel nodeModel) {
        this.setVisible(false);
    }

    @Override
    public String toString() {
        return "RichNodeView{" + "model=" + getModel() + '}';
    }

    @Override
    public void styleChanged(INodeStyle s) {
        labelTextPane.setForeground(s.getForeground());
        labelTextPane.setBackground(s.getBackground());
	labelTextPane.setOpaque(false);
//        labelScroller.getViewport().setOpaque(false);
        repaint();
    }
}
