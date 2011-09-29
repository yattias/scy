/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.main.CopexPanel;
import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.utilities.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * copex panel, menu and tree in a tab pane for eg standalone app or labbook alone in scy
 *
 * @author Marjolaine
 */
public class EdPPanel extends JPanel implements ActionMenuEvent {

    private CopexPanel copexPanel;

    private ControllerInterface controller;

    // proc
    private ExperimentalProcedure proc = null;

    /* physical quantities lis */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity;

    private File lastUsedFile = null;

    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    /* mode to show comments */
    private char modeComments = MyConstants.COMMENTS;

    /* showed level */
    private int levelMenu = 1;

    private boolean procModif;

    private boolean openQuestion = false;

    /* panel */
    private JPanel backgroundPanel;

    private JScrollPane scrollPaneTree = null;

    private CopexTree copexTree;

    /* copied elements */
    private SubTree subTreeCopy;

    // menu buttons
    private JMenuBar menuBar;

    private JSeparator sep1;

    private JSeparator sep2;

    private JSeparator sep3;

    private MyMenu menuArbo = null;

    private MyMenuItem menuItem1 = null;

    private MyMenuItem menuItem2 = null;

    private MyMenuItem menuItem3 = null;

    private MyMenuItem menuItem4 = null;

    private MyMenuItem menuItem5 = null;

    private MyMenuItem menuItem6 = null;

    private MyMenuItem menuItem7 = null;

    private MyMenuItem menuItem8 = null;

    private MyMenuItem menuItem9 = null;

    private MyMenuItem menuItemComm = null;

    private MyMenuItem menuItemUndo = null;

    private MyMenuItem menuItemRedo = null;

    private MyMenuItem menuItemPrint = null;

    private MyMenuItem menuItemHelp = null;

    private MyMenuItem menuItemSave = null;

    private MyMenuItem menuItemHypothesis = null;

    private MyMenuItem menuItemPrinciple = null;

    private MyMenuItem menuItemEvaluation = null;

    private boolean scyMode;

    public EdPPanel(CopexPanel copexPanel, ExperimentalProcedure proc, ControllerInterface controller, ArrayList<PhysicalQuantity> listPhysicalQuantity, boolean scyMode) {
        super();
        this.scyMode = scyMode;
        this.copexPanel = copexPanel;
        this.proc = proc;
        this.controller = controller;
        this.listPhysicalQuantity = listPhysicalQuantity;
        this.procModif = false;
        initGUI();
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        setSize(CopexPanel.PANEL_WIDTH, CopexPanel.PANEL_HEIGHT);
        initCopex();
            setMenuBar();
            updateMenu();
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeWidth(getWidth());
            }
        });
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public JFrame getOwnerFrame() {
        return copexPanel.getOwnerFrame();
    }

    public String getVersion() {
        return copexPanel.getVersion();
    }

    public Image getIconDialog() {
        return copexPanel.getIconDialog();
    }

    public ImageIcon getCopexImage(String img) {
        return copexPanel.getCopexImage(img);
    }

    /* display errors */
    public boolean displayError(CopexReturn dr, String title) {
        return copexPanel.displayError(dr, title);
    }

    /* returns a string from the specified key */
    public String getBundleString(String key) {
        return copexPanel.getBundleString(key);
    }

    @Override
    public Locale getLocale() {
        return copexPanel.getLocale();
    }

    /*
     * returns controller app
     */
    public ControllerInterface getController() {
        return this.controller;
    }

    /**
     * initialization menu bar
     */
    public void setMenuBar() {
        this.menuBar = new JMenuBar();
        this.menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.menuBar.setSize(this.getWidth(), 28);
        // this.menuBar.setBackground(backgroundColor);
        this.menuBar.setPreferredSize(this.menuBar.getSize());
        if (copexPanel.canSave()) {
            menuBar.add(getMenuItemSave());
            menuBar.add(getSep1());
        }
        if (isMenuHypothesis())
            menuBar.add(getMenuItemHypothesis());
        if (isMenuPrinciple())
            menuBar.add(getMenuItemPrinciple());
        if (isMenuEvaluation())
            menuBar.add(getMenuItemEvaluation());
        menuBar.add(getMenuItemComm());
        menuBar.add(getMenuArbo());
        menuBar.add(getSep2());
        menuBar.add(getMenuItemUndo());
        menuBar.add(getMenuItemRedo());
        menuBar.add(getSep3());
        if (copexPanel.canPrint())
            menuBar.add(getMenuItemPrint());
        menuBar.add(getMenuItemHelp());
        menuBar.setBounds(0, 0, this.getWidth(), menuBar.getHeight());
        if (!scyMode){
            this.add(menuBar, BorderLayout.NORTH);
        }
    }

    private JSeparator getSep1() {
        if (sep1 == null) {
            sep1 = new JSeparator(JSeparator.VERTICAL);
            sep1.setSize(5, menuBar.getHeight());
            // sep1.setBounds(menuItemSave.getX()+menuItemSave.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep1;
    }

    private JSeparator getSep2() {
        if (sep2 == null) {
            sep2 = new JSeparator(JSeparator.VERTICAL);
            sep2.setSize(5, menuBar.getHeight());
            // sep2.setBounds(menuArbo.getX()+menuArbo.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep2;
    }

    private JSeparator getSep3() {
        if (sep3 == null) {
            sep3 = new JSeparator(JSeparator.VERTICAL);
            sep3.setSize(5, menuBar.getHeight());
            // sep3.setBounds(menuItemRedo.getX()+menuItemRedo.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep3;
    }

    private MyMenuItem getMenuItemSave() {
        if (menuItemSave == null) {
            menuItemSave = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_SAVE"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_save.png"), getCopexImage("Bouton-AdT-28_save_survol.png"), getCopexImage("Bouton-AdT-28_save_clic.png"), getCopexImage("Bouton-AdT-28_save_grise.png"));
            menuItemSave.addActionMenuEvent(this);
            // menuItemSave.setBounds(0, 0, menuItemSave.getWidth(), menuItemSave.getHeight());
        }
        return menuItemSave;
    }

    private MyMenuItem getMenuItemHypothesis() {
        if (menuItemHypothesis == null) {
            menuItemHypothesis = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_HYPOTHESIS"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_hypothesis.png"), getCopexImage("Bouton-AdT-28_hypothesis_survol.png"), getCopexImage("Bouton-AdT-28_hypothesis_clic.png"), getCopexImage("Bouton-AdT-28_hypothesis_grise.png"));
            menuItemHypothesis.addActionMenuEvent(this);
            int x = 0;
            if (copexPanel.canSave()) {
                x = sep1.getX() + sep1.getWidth();
            }
            // menuItemHypothesis.setBounds(x, 0, menuItemHypothesis.getWidth(), menuItemHypothesis.getHeight());
        }
        return menuItemHypothesis;
    }

    private MyMenuItem getMenuItemPrinciple() {
        if (menuItemPrinciple == null) {
            menuItemPrinciple = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_PRINCIPLE"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_principle.png"), getCopexImage("Bouton-AdT-28_principle_survol.png"), getCopexImage("Bouton-AdT-28_principle_clic.png"), getCopexImage("Bouton-AdT-28_principle_grise.png"));
            menuItemPrinciple.addActionMenuEvent(this);
            int x = 0;
            if (copexPanel.canSave()) {
                x = sep1.getX() + sep1.getWidth();
            }
            if (isMenuHypothesis()) {
                x = menuItemHypothesis.getX() + menuItemHypothesis.getWidth();
            }
            // menuItemPrinciple.setBounds(x, 0, menuItemPrinciple.getWidth(), menuItemPrinciple.getHeight());
        }
        return menuItemPrinciple;
    }

    private MyMenuItem getMenuItemEvaluation() {
        if (menuItemEvaluation == null) {
            menuItemEvaluation = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_EVALUATION"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_evaluation.png"), getCopexImage("Bouton-AdT-28_evaluation_survol.png"), getCopexImage("Bouton-AdT-28_evaluation_clic.png"), getCopexImage("Bouton-AdT-28_evaluation_grise.png"));
            menuItemEvaluation.addActionMenuEvent(this);
            int x = 0;
            if (copexPanel.canSave()) {
                x = sep1.getX() + sep1.getWidth();
            }
            if (isMenuPrinciple()) {
                x = menuItemPrinciple.getX() + menuItemPrinciple.getWidth();
            } else if (isMenuHypothesis()) {
                x = menuItemHypothesis.getX() + menuItemHypothesis.getWidth();
            }
            // menuItemEvaluation.setBounds(x, 0, menuItemEvaluation.getWidth(), menuItemEvaluation.getHeight());
        }
        return menuItemEvaluation;
    }

    private MyMenu getMenuArbo() {
        if (menuArbo == null) {
            levelMenu = 1;
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO_GENERAL");
            menuArbo = new MyMenu(this, toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_1.png"), getCopexImage("Bouton-AdT-28_1_survol.png"), getCopexImage("Bouton-AdT-28_1_clic.png"), getCopexImage("Bouton-AdT-28_1na.png"));
            // menuArbo.setBackground(this.menuBar.getBackground());
            // menuArbo.setBounds(menuItemComm.getX()+menuItemComm.getWidth(), 0, menuArbo.getWidth(), menuArbo.getHeight());

        }
        return menuArbo;
    }

    private MyMenuItem getMenuItemComm() {
        if (menuItemComm == null) {
            menuItemComm = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_COMM"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_comment_clic.png"), getCopexImage("Bouton-AdT-28_comment_clicsur.png"), getCopexImage("Bouton-AdT-28_comment_clic.png"), getCopexImage("Bouton-AdT-28_comment_grise.png"));
            menuItemComm.addActionMenuEvent(this);
            int x = 0;
            if (copexPanel.canSave()) {
                x = sep1.getX() + sep1.getWidth();
            }
            if (isMenuEvaluation()) {
                x = menuItemEvaluation.getX() + menuItemEvaluation.getWidth();
            } else if (isMenuPrinciple()) {
                x = menuItemPrinciple.getX() + menuItemPrinciple.getWidth();
            } else if (isMenuHypothesis()) {
                x = menuItemHypothesis.getX() + menuItemHypothesis.getWidth();
            }
            // menuItemComm.setBounds(x, 0, menuItemComm.getWidth(), menuItemComm.getHeight());
        }
        return menuItemComm;
    }

    private MyMenuItem getMenuItemUndo() {
        if (menuItemUndo == null) {
            menuItemUndo = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_UNDO"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_undo.png"), getCopexImage("Bouton-AdT-28_undo_survol.png"), getCopexImage("Bouton-AdT-28_undo_clic.png"), getCopexImage("Bouton-AdT-28_undo_grise.png"));
            menuItemUndo.addActionMenuEvent(this);
            // menuItemUndo.setBounds(sep2.getX()+sep2.getWidth(), 0, menuItemUndo.getWidth(), menuItemUndo.getHeight());
        }
        return menuItemUndo;
    }

    private MyMenuItem getMenuItemRedo() {
        if (menuItemRedo == null) {
            menuItemRedo = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_REDO"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_redo.png"), getCopexImage("Bouton-AdT-28_redo_survol.png"), getCopexImage("Bouton-AdT-28_redo_clic.png"), getCopexImage("Bouton-AdT-28_redo_grise.png"));
            menuItemRedo.addActionMenuEvent(this);
            // menuItemRedo.setBounds(menuItemUndo.getX()+menuItemUndo.getWidth(), 0, menuItemRedo.getWidth(), menuItemRedo.getHeight());
        }
        return menuItemRedo;
    }

    private MyMenuItem getMenuItemHelp() {
        if (menuItemHelp == null) {
            menuItemHelp = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_HELP"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_help.png"), getCopexImage("Bouton-AdT-28_help_survol.png"), getCopexImage("Bouton-AdT-28_help_clic.png"), getCopexImage("Bouton-AdT-28_help.png"));
            menuItemHelp.addActionMenuEvent(this);
            int x = sep3.getX() + sep3.getWidth();
            if (copexPanel.canPrint())
                x = menuItemPrint.getX() + menuItemPrint.getWidth();
            // menuItemHelp.setBounds(x, 0, menuItemHelp.getWidth(), menuItemHelp.getHeight());
        }
        return menuItemHelp;
    }

    private MyMenuItem getMenuItemPrint() {
        if (menuItemPrint == null) {
            menuItemPrint = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_PRINT"), menuBar.getBackground(), getCopexImage("Bouton-AdT-28_pdf.png"), getCopexImage("Bouton-AdT-28_pdf_survol.png"), getCopexImage("Bouton-AdT-28_pdf_clic.png"), getCopexImage("Bouton-AdT-28_pdf_grise.png"));
            menuItemPrint.addActionMenuEvent(this);
            menuItemPrint.setSize(menuItemPrint.getWidth(), menuItemPrint.getHeight());
            menuItemPrint.setPreferredSize(new Dimension(menuItemPrint.getWidth(), menuItemPrint.getHeight()));
            // menuItemPrint.setBounds(sep3.getX()+sep3.getWidth(), 0, menuItemPrint.getWidth(), menuItemPrint.getHeight());
        }
        return menuItemPrint;
    }

    private MyMenuItem getMenuItem1() {
        if (menuItem1 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "1");
            menuItem1 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_1.png"), getCopexImage("Bouton-AdT-28_1_survol.png"), getCopexImage("Bouton-AdT-28_1_clic.png"), getCopexImage("Bouton-AdT-28_1na.png"));
            menuItem1.addActionMenuEvent(this);
            menuItem1.setSize(menuItem1.getWidth(), menuItem1.getHeight());
            menuItem1.setPreferredSize(new Dimension(menuItem1.getWidth(), menuItem1.getHeight()));
            menuItem1.setBounds(0, 0, menuItem1.getWidth(), menuItem1.getHeight());
        }
        return menuItem1;
    }

    private MyMenuItem getMenuItem2() {
        if (menuItem2 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "2");
            menuItem2 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_2.png"), getCopexImage("Bouton-AdT-28_2_survol.png"), getCopexImage("Bouton-AdT-28_2_clic.png"), getCopexImage("Bouton-AdT-28_2na.png"));
            menuItem2.addActionMenuEvent(this);
            menuItem2.setSize(menuItem2.getWidth(), menuItem2.getHeight());
            menuItem2.setPreferredSize(new Dimension(menuItem2.getWidth(), menuItem2.getHeight()));
            menuItem2.setBounds(0, menuItem1.getHeight(), menuItem2.getWidth(), menuItem2.getHeight());
        }
        return menuItem2;
    }

    private MyMenuItem getMenuItem3() {
        if (menuItem3 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "3");
            menuItem3 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_3.png"), getCopexImage("Bouton-AdT-28_3_survol.png"), getCopexImage("Bouton-AdT-28_3_clic.png"), getCopexImage("Bouton-AdT-28_3na.png"));
            menuItem3.addActionMenuEvent(this);
            menuItem3.setSize(menuItem3.getWidth(), menuItem3.getHeight());
            menuItem3.setPreferredSize(new Dimension(menuItem3.getWidth(), menuItem3.getHeight()));
            menuItem3.setBounds(0, menuItem2.getHeight(), menuItem3.getWidth(), menuItem3.getHeight());
        }
        return menuItem3;
    }

    private MyMenuItem getMenuItem4() {
        if (menuItem4 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "4");
            menuItem4 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_4.png"), getCopexImage("Bouton-AdT-28_4_survol.png"), getCopexImage("Bouton-AdT-28_4_clic.png"), getCopexImage("Bouton-AdT-28_4na.png"));
            menuItem4.addActionMenuEvent(this);
            menuItem4.setSize(menuItem4.getWidth(), menuItem4.getHeight());
            menuItem4.setPreferredSize(new Dimension(menuItem4.getWidth(), menuItem4.getHeight()));
            menuItem4.setBounds(0, menuItem3.getHeight(), menuItem4.getWidth(), menuItem4.getHeight());
        }
        return menuItem4;
    }

    private MyMenuItem getMenuItem5() {
        if (menuItem5 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "5");
            menuItem5 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_5.png"), getCopexImage("Bouton-AdT-28_5_survol.png"), getCopexImage("Bouton-AdT-28_5_clic.png"), getCopexImage("Bouton-AdT-28_5na.png"));
            menuItem5.addActionMenuEvent(this);
            menuItem5.setSize(menuItem5.getWidth(), menuItem5.getHeight());
            menuItem5.setPreferredSize(new Dimension(menuItem5.getWidth(), menuItem5.getHeight()));
            menuItem5.setBounds(0, menuItem4.getHeight(), menuItem5.getWidth(), menuItem5.getHeight());
        }
        return menuItem5;
    }

    private MyMenuItem getMenuItem6() {
        if (menuItem6 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "6");
            menuItem6 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_6.png"), getCopexImage("Bouton-AdT-28_6_survol.png"), getCopexImage("Bouton-AdT-28_6_clic.png"), getCopexImage("Bouton-AdT-28_6na.png"));
            menuItem6.addActionMenuEvent(this);
            menuItem6.setSize(menuItem6.getWidth(), menuItem6.getHeight());
            menuItem6.setPreferredSize(new Dimension(menuItem6.getWidth(), menuItem6.getHeight()));
            menuItem6.setBounds(0, menuItem5.getHeight(), menuItem6.getWidth(), menuItem6.getHeight());
        }
        return menuItem6;
    }

    private MyMenuItem getMenuItem7() {
        if (menuItem7 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "7");
            menuItem7 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_7.png"), getCopexImage("Bouton-AdT-28_7_survol.png"), getCopexImage("Bouton-AdT-28_7_clic.png"), getCopexImage("Bouton-AdT-28_7na.png"));
            menuItem7.addActionMenuEvent(this);
            menuItem7.setSize(menuItem7.getWidth(), menuItem7.getHeight());
            menuItem7.setPreferredSize(new Dimension(menuItem7.getWidth(), menuItem7.getHeight()));
            menuItem7.setBounds(0, menuItem6.getHeight(), menuItem7.getWidth(), menuItem7.getHeight());
        }
        return menuItem7;
    }

    private MyMenuItem getMenuItem8() {
        if (menuItem8 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "8");
            menuItem8 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_8.png"), getCopexImage("Bouton-AdT-28_8_survol.png"), getCopexImage("Bouton-AdT-28_8_clic.png"), getCopexImage("Bouton-AdT-28_8na.png"));
            menuItem8.addActionMenuEvent(this);
            menuItem8.setSize(menuItem8.getWidth(), menuItem8.getHeight());
            menuItem8.setPreferredSize(new Dimension(menuItem8.getWidth(), menuItem8.getHeight()));
            menuItem8.setBounds(0, menuItem7.getHeight(), menuItem8.getWidth(), menuItem8.getHeight());
        }
        return menuItem8;
    }

    private MyMenuItem getMenuItem9() {
        if (menuItem9 == null) {
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "9");
            menuItem9 = new MyMenuItem(toolTipText, menuBar.getBackground(), getCopexImage("Bouton-AdT-28_9.png"), getCopexImage("Bouton-AdT-28_9_survol.png"), getCopexImage("Bouton-AdT-28_9_clic.png"), getCopexImage("Bouton-AdT-28_9na.png"));
            menuItem9.addActionMenuEvent(this);
            menuItem9.setSize(menuItem9.getWidth(), menuItem9.getHeight());
            menuItem9.setPreferredSize(new Dimension(menuItem9.getWidth(), menuItem9.getHeight()));
            menuItem9.setBounds(0, menuItem8.getHeight(), menuItem9.getWidth(), menuItem9.getHeight());
        }
        return menuItem9;
    }

    private JScrollPane getScrollPaneTree() {
        if (scrollPaneTree == null) {
            scrollPaneTree = new JScrollPane(getCopexTree());
            // scrollPaneTree.setBounds(0, 0, this.getWidth(), this.getHeight());
        }
        return scrollPaneTree;
    }

    public CopexTree getCopexTree() {
        if (copexTree == null) {
            copexTree = new CopexTree(this, proc);
        }
        return copexTree;
    }

    /*
     * update menu
     */
    public void updateMenu() {
        if (proc == null) {
            // no activ proc => disabled menu
            if (menuItemSave != null)
                menuItemSave.setEnabled(false);
            if (menuItemHypothesis != null)
                menuItemHypothesis.setEnabled(false);
            if (menuItemPrinciple != null)
                menuItemPrinciple.setEnabled(false);
            if (menuItemEvaluation != null)
                menuItemEvaluation.setEnabled(false);
            getMenuArbo().setEnabled(false);
            getMenuItemComm().setEnabled(false);
            getMenuItemUndo().setEnabled(false);
            getMenuItemRedo().setEnabled(false);
            if (copexPanel.canPrint())
                getMenuItemPrint().setEnabled(false);
        } else { // a proc is activ
            boolean isQuestionFilled = proc.getQuestion().getDescription(getLocale()) != null && proc.getQuestion().getDescription(getLocale()).length() > 0;
            // 10/02/10: the question is not obligatory filled
            isQuestionFilled = true;
            // level menu: update
            getMenuArbo().setEnabled(true);
            updateMenuArbo();
            // comments
            getMenuItemComm().setEnabled(copexTree.isComments());
            // undo
            getMenuItemUndo().setEnabled(copexTree.canUndo());
            // redo
            getMenuItemRedo().setEnabled(copexTree.canRedo());
            // print
            if (copexPanel.canPrint())
                getMenuItemPrint().setEnabled(true);
            if (copexPanel.canSave())
                getMenuItemSave().setEnabled(proc.getRight() == MyConstants.EXECUTE_RIGHT);
            if (isMenuHypothesis()) {
                boolean b = proc.getHypothesis() == null || (proc.getHypothesis() != null && proc.getHypothesis().isHide());
                if (b) {
                    menuItemHypothesis.setItemIcon(getCopexImage("Bouton-AdT-28_hypothesis.png"));
                    menuItemHypothesis.setItemRolloverIcon(getCopexImage("Bouton-AdT-28_hypothesis_survol.png"));
                    menuItemHypothesis.setToolTipText(getBundleString("TOOLTIPTEXT_MENU_HYPOTHESIS"));
                } else {
                    menuItemHypothesis.setItemIcon(getCopexImage("Bouton-AdT-28_hypothesis_clic.png"));
                    menuItemHypothesis.setItemRolloverIcon(getCopexImage("Bouton-AdT-28_hypothesis_clicsur.png"));
                    menuItemHypothesis.setToolTipText(getBundleString("TOOLTIPTEXT_MENU_NO_HYPOTHESIS"));
                }
            }
            if (isMenuPrinciple()) {
                boolean b = proc.getGeneralPrinciple() == null || (proc.getGeneralPrinciple() != null && proc.getGeneralPrinciple().isHide());
                if (b) {
                    menuItemPrinciple.setItemIcon(getCopexImage("Bouton-AdT-28_principle.png"));
                    menuItemPrinciple.setItemRolloverIcon(getCopexImage("Bouton-AdT-28_principle_survol.png"));
                    menuItemPrinciple.setToolTipText(getBundleString("TOOLTIPTEXT_MENU_PRINCIPLE"));
                } else {
                    menuItemPrinciple.setItemIcon(getCopexImage("Bouton-AdT-28_principle_clic.png"));
                    menuItemPrinciple.setItemRolloverIcon(getCopexImage("Bouton-AdT-28_principle_clicsur.png"));
                    menuItemPrinciple.setToolTipText(getBundleString("TOOLTIPTEXT_MENU_NO_PRINCIPLE"));
                }
            }
            if (isMenuEvaluation()) {
                boolean b = proc.getEvaluation() == null || (proc.getEvaluation() != null && proc.getEvaluation().isHide());
                if (b) {
                    menuItemEvaluation.setItemIcon(getCopexImage("Bouton-AdT-28_evaluation.png"));
                    menuItemEvaluation.setItemRolloverIcon(getCopexImage("Bouton-AdT-28_evaluation_survol.png"));
                    menuItemEvaluation.setToolTipText(getBundleString("TOOLTIPTEXT_MENU_EVALUATION"));
                } else {
                    menuItemEvaluation.setItemIcon(getCopexImage("Bouton-AdT-28_evaluation_clic.png"));
                    menuItemEvaluation.setItemRolloverIcon(getCopexImage("Bouton-AdT-28_evaluation_clicsur.png"));
                    menuItemEvaluation.setToolTipText(getBundleString("TOOLTIPTEXT_MENU_NO_EVALUATION"));
                }
            }
        }
        repaint();
    }

    /* can paste? */
    public boolean canPaste() {
        return subTreeCopy != null;
    }

    // update level menu
    private void updateMenuArbo() {
        int level = copexTree.getLevelTree();
        getMenuArbo().setEnabled(level > 1);
        updateLevel(level);
    }

    public void updateLevel(int level) {
        getMenuArbo().removeAll();
        menuItem1 = null;
        menuItem2 = null;
        menuItem3 = null;
        menuItem4 = null;
        menuItem5 = null;
        menuItem6 = null;
        menuItem7 = null;
        menuItem8 = null;
        menuItem9 = null;

        int maxL = Math.min(level, 9); // 9 is a max
        for (int i = 1; i <= maxL; i++) {
            if (i == 1)
                getMenuArbo().add(getMenuItem1());
            if (i == 2)
                getMenuArbo().add(getMenuItem2());
            if (i == 3)
                getMenuArbo().add(getMenuItem3());
            if (i == 4)
                getMenuArbo().add(getMenuItem4());
            if (i == 5)
                getMenuArbo().add(getMenuItem5());
            if (i == 6)
                getMenuArbo().add(getMenuItem6());
            if (i == 7)
                getMenuArbo().add(getMenuItem7());
            if (i == 8)
                getMenuArbo().add(getMenuItem8());
            if (i == 9)
                getMenuArbo().add(getMenuItem9());
        }
    }

    /*
     * mouse events
     */
    public void clickMenuEvent(MyMenuItem item) {
        if (copexPanel.canPrint() && item.equals(getMenuItemPrint())) {
            printCopex(true);
        } else if (item.equals(getMenuItemComm())) {
            setDisplayComments();
        } else if (item.equals(getMenuItem1())) {
            displayLevel(1);
        } else if (item.equals(getMenuItem2())) {
            displayLevel(2);
        } else if (item.equals(getMenuItem3())) {
            displayLevel(3);
        } else if (item.equals(getMenuItem4())) {
            displayLevel(4);
        } else if (item.equals(getMenuItem5())) {
            displayLevel(5);
        } else if (item.equals(getMenuItem6())) {
            displayLevel(6);
        } else if (item.equals(getMenuItem7())) {
            displayLevel(7);
        } else if (item.equals(getMenuItem8())) {
            displayLevel(8);
        } else if (item.equals(getMenuItem9())) {
            displayLevel(9);
        } else if (item.equals(getMenuItemUndo())) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            copexTree.undo();
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else if (item.equals(getMenuItemRedo())) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            copexTree.redo();
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else if (item.equals(getMenuItemHelp())) {
            this.openDialogHelp();
        } else if (item.equals(menuItemSave)) {
            save();
        } else if (item.equals(menuItemHypothesis)) {
            addHypothesis();
        } else if (item.equals(menuItemPrinciple)) {
            addGeneralPrinciple();
        } else if (item.equals(menuItemEvaluation)) {
            addEvaluation();
        }
    }

    /**
     * This method is called from within the init() method to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        setBackground(new java.awt.Color(236, 233, 216));
    }// </editor-fold>

    // Variables declaration - do not modify
    // End of variables declaration

    /* open the dialog to create a step */
    public void openDialogAddE(char insertIn) {
        if (proc == null)
            return;
        InitialProcedure initProc = null;
        if (proc instanceof LearnerProcedure) {
            initProc = ((LearnerProcedure) proc).getInitialProc();
        } else if (proc instanceof InitialProcedure) {
            initProc = (InitialProcedure) proc;
        }
        StepDialog addE = new StepDialog(this, initProc.isTaskRepeat(), !proc.isTaskProc(), insertIn);
        addE.setVisible(true);
    }

    // open the dialog to add an action
    public void openDialogAddA(char insertIn) {
        if (proc == null)
            return;
        InitialProcedure initProc = null;
        if (proc instanceof LearnerProcedure) {
            initProc = ((LearnerProcedure) proc).getInitialProc();
        } else if (proc instanceof InitialProcedure) {
            initProc = (InitialProcedure) proc;
        }
        ActionDialog addA = new ActionDialog(this, initProc.isFreeAction(), initProc.getListNamedAction(), this.listPhysicalQuantity, initProc.isTaskRepeat(), insertIn, initProc.isTaskDraw());
        addA.setVisible(true);
    }

    /* open help dialog */
    public void openDialogHelp() {
        CopexReturn cr = this.controller.openHelpDialog();
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
        HelpDialog helpD = new HelpDialog(this);
        helpD.setVisible(true);
    }

    // print as pdf
    public void printCopex(boolean printProc) {
        CopexReturn cr = controller.printCopex(proc);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* show or not comments */
    private void setDisplayComments() {
        switch (modeComments) {
            case MyConstants.COMMENTS:
                // remove comments and update menu button
                modeComments = MyConstants.NO_COMMENTS;
                getMenuItemComm().setItemIcon(getCopexImage("Bouton-AdT-28_comment.png"));
                getMenuItemComm().setItemClicIcon(getCopexImage("Bouton-AdT-28_comment_clic.png"));
                getMenuItemComm().setItemRolloverIcon(getCopexImage("Bouton-AdT-28_comment_survol.png"));
                getMenuItemComm().setToolTipText(getBundleString("TOOLTIPTEXT_MENU_COMM"));
                break;
            case MyConstants.NO_COMMENTS:
                modeComments = MyConstants.COMMENTS;
                getMenuItemComm().setItemIcon(getCopexImage("Bouton-AdT-28_comment_clic.png"));
                getMenuItemComm().setItemClicIcon(getCopexImage("Bouton-AdT-28_comment_clic.png"));
                getMenuItemComm().setItemRolloverIcon(getCopexImage("Bouton-AdT-28_comment_clicsur.png"));
                getMenuItemComm().setToolTipText(getBundleString("TOOLTIPTEXT_MENU_NO_COMM"));
                break;
        }
        copexTree.setComments(modeComments);
        repaint();
    }

    public CopexReturn addAction(CopexAction newAction, TaskSelected ts, char insertIn, boolean sync) {
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.addAction(newAction, ts.getProc(), ts.getTaskBrother(), ts.getTaskParent(), v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_ACTION"), false);
        ExperimentalProcedure newProc = (ExperimentalProcedure) v.get(0);
        updateProc(newProc);
        copexTree.addTask(newAction, ts);
        copexTree.addEdit_addTask(copexTree.getTaskSelected(newAction), ts);
        updateMenu();
        procModif = true;
        if (sync) {
            copexPanel.addSyncAddTask(newAction, ts.getSelectedTask(), insertIn);
        }
        return new CopexReturn();
    }

    /* add a new action */
    public CopexReturn addAction(CopexAction newAction, char insertIn) {
        // gets the activ proc and the position to add action
        TaskSelected ts = copexTree.getTaskSelected(insertIn);
        if (ts == null || ts.getProc() == null)
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_ACTION"), false);
        return addAction(newAction, ts, insertIn, true);
    }

    /* modification action */
    public CopexReturn updateAction(CopexAction newAction) {
        // gets the activ proc and the position to add action
        TaskSelected ts = copexTree.getTaskSelected();
        if (ts == null || ts.getProc() == null || ts.getSelectedTask() == null)
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_ACTION"), false);
        CopexTask t = ts.getSelectedTask();
        if (!t.isAction())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_ACTION"), false);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateAction(newAction, ts.getProc(), (CopexAction) t, v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_ACTION"), false);
        ExperimentalProcedure newProc = (ExperimentalProcedure) v.get(0);
        updateProc(newProc);
        copexTree.updateTask(newAction, ts);
        updateMenu();
        procModif = true;
        return new CopexReturn();
    }

    public void createDrawTaskPreview(long dbKeyTask, WhiteboardPanel whiteboardPanel) {
        URL copexURL = this.controller.getCopexURL();
        if (copexURL == null || whiteboardPanel == null)
            return;
        BufferedImage outImage = new BufferedImage(whiteboardPanel.getWidth(), whiteboardPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = outImage.createGraphics();
        whiteboardPanel.paint(graphics);

        String fileName = "labdoc-task-" + dbKeyTask + ".png";
        URL imgURL;
        try {
            imgURL = new URL(copexURL, "../tools_utilities/InterfaceServer/writeObject.php");
            HttpURLConnection urlCon = (HttpURLConnection) imgURL.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.connect();
            ObjectOutputStream out = new ObjectOutputStream(urlCon.getOutputStream());
            out.writeObject(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outImage, "png", baos);
            byte[] bytesOut = baos.toByteArray();
            out.writeObject(bytesOut);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "utf8"));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                System.out.println(ligne);
            }
            out.flush();
            out.close();
            urlCon.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(EdPPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioex) {
            Logger.getLogger(EdPPanel.class.getName()).log(Level.SEVERE, null, ioex);
        }
    }

    /* add a new step */
    public CopexReturn addStep(Step newStep, TaskSelected ts, char insertIn, boolean sync) {
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.addStep(newStep, ts.getProc(), ts.getTaskBrother(), ts.getTaskParent(), v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_STEP"), false);
        ExperimentalProcedure newProc = (ExperimentalProcedure) v.get(0);
        updateProc(newProc);
        copexTree.addTask(newStep, ts);
        copexTree.addEdit_addTask(copexTree.getTaskSelected(newStep), ts);
        updateMenu();
        procModif = true;
        if (sync) {
            copexPanel.addSyncAddTask(newStep, ts.getSelectedTask(), insertIn);
        }
        return new CopexReturn();
    }

    /* add a new step */
    public CopexReturn addStep(Step newStep, char insertIn) {
        // gets the activ proc and the position to add step
        TaskSelected ts = copexTree.getTaskSelected(insertIn);
        if (ts == null || ts.getProc() == null)
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_STEP"), false);
        return addStep(newStep, ts, insertIn, true);
    }

    /* update step */
    public CopexReturn updateStep(Step newStep) {
        // gets the activ proc and the position to update step
        TaskSelected ts = copexTree.getTaskSelected();
        if (ts == null || ts.getProc() == null || ts.getSelectedTask() == null)
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_STEP"), false);
        CopexTask t = ts.getSelectedTask();
        if (!t.isStep())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_STEP"), false);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateStep(newStep, ts.getProc(), (Step) t, v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_STEP"), false);
        ExperimentalProcedure newProc = (ExperimentalProcedure) v.get(0);
        updateProc(newProc);
        copexTree.updateTask(newStep, ts);
        updateMenu();
        procModif = true;
        return new CopexReturn();
    }

    /* show the specified level */
    private void displayLevel(int level) {
        copexTree.displayLevel(level);
        // update icon depending the level
        levelMenu = level;
        updateIconMenu();
    }

    /* update the icon in menu */
    private void updateIconMenu() {
        ImageIcon img = getCopexImage("Bouton-AdT-28_" + levelMenu + ".png");
        ImageIcon imgSurvol = getCopexImage("Bouton-AdT-28_" + levelMenu + "_survol.png");
        ImageIcon imgClic = getCopexImage("Bouton-AdT-28_" + levelMenu + "_clic.png");
        ImageIcon imgDisabled = getCopexImage("Bouton-AdT-28_" + levelMenu + "na.png");
        getMenuArbo().setImage(img);
        getMenuArbo().setImageSurvol(imgSurvol);
        getMenuArbo().setImageClic(imgClic);
        getMenuArbo().setImageDisabled(imgDisabled);
        getMenuArbo().repaint();
    }

    /* remove */
    public void suppr() {
        suppr(true);
    }

    /* remove with or not confirmation */
    public void suppr(boolean confirm) {
        CopexReturn cr = suppr(copexTree.getTasksSelected(), confirm);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* remove the selection in the tree */
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, boolean confirm) {
        // gets the selection to remove
        if (listTs == null)
            return new CopexReturn(getBundleString("MSG_ERROR_DELETE_TASK"), false);
        // ask for confirmation
        boolean confirmRemove = true;
        if (confirm) {
            CopexReturn cr = new CopexReturn();
            if (listTs.size() > 1)
                cr.setConfirm(getBundleString("MSG_CONFIRM_SUPPR_ALL"));
            else
                cr.setConfirm(getBundleString("MSG_CONFIRM_SUPPR"));
            confirmRemove = displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
        }
        if (confirmRemove) {
            ArrayList v = new ArrayList();
            // copy the list while adding the childs to task
            ArrayList<TaskSelected> listTsSuppr = getListTs(listTs);

            ArrayList<TaskSelected> list = new ArrayList();
            int nbTs = listTsSuppr.size();
            for (int i = 0; i < nbTs; i++) {
                CopexTask task = listTsSuppr.get(i).getTaskToAttach();
                TaskSelected ts = copexTree.getTaskSelected(task, MyConstants.INSERT_TASK_FIX);
                list.add(ts);
            }
            CopexReturn cr = this.controller.suppr(listTs, v, confirm, MyConstants.NOT_UNDOREDO);
            if (cr.isError())
                return new CopexReturn(getBundleString("MSG_ERROR_DELETE_TASK"), false);
            ExperimentalProcedure newProc = (ExperimentalProcedure) v.get(0);
            copexTree.suppr(listTs);
            updateProc(newProc);

            if (confirm) { // remove
                subTreeCopy = null;
                copexTree.addEdit_deleteTask(listTsSuppr, list);
            } else { // CUT
                addEdit_cut(listTsSuppr, list, subTreeCopy);
            }
        }
        procModif = true;
        updateMenu();

        return new CopexReturn();
    }

    // returns tasks list to remove (add childs)
    private ArrayList<TaskSelected> getListTs(ArrayList<TaskSelected> listTs) {
        ArrayList<TaskSelected> listT = new ArrayList();
        int nbTs = listTs.size();
        for (int t = 0; t < nbTs; t++) {
            TaskSelected ts = listTs.get(t);
            listT.add(ts);
            if (ts.getSelectedTask() instanceof Step || ts.getSelectedTask() instanceof Question) {
                // gets childs
                ArrayList<CopexTask> lc = listTs.get(t).getListAllChildren();
                // add
                int n = lc.size();
                for (int k = 0; k < n; k++) {
                    // add
                    listT.add(copexTree.getTaskSelected(lc.get(k)));
                }
            }

        }
        // remove multi-occ
        int nbT = listT.size();
        ArrayList<TaskSelected> lt = new ArrayList();
        ArrayList<TaskSelected> listTClean = new ArrayList();
        for (int t = 0; t < nbT; t++) {
            int id = getId(lt, listT.get(t).getSelectedTask().getDbKey());
            if (id == -1) {
                listTClean.add(listT.get(t));
            }
            lt.add(listT.get(t));
        }
        return listTClean;
    }

    /* gets the index in the list , -1 if not found */
    private int getId(ArrayList<TaskSelected> listT, long dbKey) {
        int nbT = listT.size();
        for (int i = 0; i < nbT; i++) {
            CopexTask t = listT.get(i).getSelectedTask();
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    public TaskSelected getSelectedTask() {
        return this.copexTree.getTaskSelected();
    }

    // open the dialog to rename the proc
    public void openDialogEditProc() {
        if (proc != null && proc instanceof LearnerProcedure) {
            EditProcDialog editProcD = new EditProcDialog(this, copexPanel.isMission(), controller, (LearnerProcedure) proc);
            editProcD.setVisible(true);
        }
    }

    public ExperimentalProcedure getExperimentalProc() {
        return this.proc;
    }

    /* update the status of a proc */
    public void setActiv(boolean register) {
        if (register) {
            proc.setActiv(true);
            CopexReturn cr = controller.setProcActiv(proc);
            if (cr.isError())
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* returns the tree to cpoy */
    public SubTree getSubTreeCopy() {
        return this.subTreeCopy;
    }

    public void paste(ArrayList<CopexTask> listTask, TaskSelected t, char undoRedo) {
        copexTree.updateProc(proc);
        copexTree.addTasks(listTask, getSubTreeCopy(), t);
        procModif = true;
        updateMenu();
        procModif = true;
        // if (undoRedo == CopexUtilities.NOT_UNDOREDO){
        ArrayList<TaskSelected> listTs = new ArrayList();
        int nbT = listTask.size();
        for (int i = 0; i < nbT; i++) {
            TaskSelected ts = copexTree.getTaskSelected(listTask.get(i));
            listTs.add(ts);
        }
        addEdit_paste(subTreeCopy, t, listTs);
        // }
    }

    /* update the proc */
    public void updateProc(ExperimentalProcedure p) {
        this.proc = p;
        copexTree.setDatasheet(proc.getDataSheet());
        copexTree.updateProc(proc);
    }

    /* update proc */
    public void updateProc(ExperimentalProcedure p, boolean update) {
        this.proc = p;
        copexTree.updateProc(proc, update);
    }

    public void initCopex() {
        backgroundPanel = new JPanel();
        backgroundPanel.setBackground(Color.WHITE);
        backgroundPanel.setLayout(new BorderLayout());
        this.add(backgroundPanel, BorderLayout.CENTER);
        backgroundPanel.add(getScrollPaneTree(), BorderLayout.CENTER);
    }

    /* add event undo redo to rename proc */
    public void addEdit_renameProc(ExperimentalProcedure proc, String name) {
        copexTree.addEdit_renameProc(proc, name);
        updateMenu();
    }

    /* update tree */
    public void setSubTree(SubTree subTree) {
        this.subTreeCopy = subTree;
    }

    /* add event : cut */
    public void addEdit_cut(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs, SubTree subTree) {
        copexTree.addEdit_cut(listTask, listTs, subTree);
        updateMenu();
    }

    /* paste */
    public void addEdit_paste(SubTree subTree, TaskSelected ts, ArrayList<TaskSelected> listTask) {
        copexTree.addEdit_paste(subTree, ts, listTask);
        updateMenu();
    }

    /* returns the list of materials of the mission with the specified type of material */
    public ArrayList<Material> getListMaterial(TypeMaterial type, TypeMaterial type2, boolean andTypes, boolean modeAdd) {
        ArrayList<Material> listMaterial = new ArrayList();
        if (proc == null)
            return listMaterial;
        if (type == null) {
            // all
            if (proc instanceof LearnerProcedure)
                return ((LearnerProcedure) proc).getInitialProc().getListMaterial();
            else if (proc instanceof InitialProcedure) {
                return ((InitialProcedure) proc).getListMaterial();
            }
        }
        boolean controlType2 = type2 != null;
        long dbKeyType2 = -1;
        if (controlType2)
            dbKeyType2 = type2.getDbKey();
        ArrayList<Material> matMision = new ArrayList();
        if (proc instanceof LearnerProcedure)
            matMision = ((LearnerProcedure) proc).getInitialProc().getListMaterial();
        else if (proc instanceof InitialProcedure) {
            matMision = ((InitialProcedure) proc).getListMaterial();
        }
        int nbM = matMision.size();
        for (int i = 0; i < nbM; i++) {
            if (matMision.get(i).isType(type.getDbKey())) {
                if ((controlType2 && matMision.get(i).isType(dbKeyType2) && andTypes) || !controlType2 || (controlType2 && !andTypes))
                    listMaterial.add(matMision.get(i));
            } else if (controlType2 && !andTypes && matMision.get(i).isType(type2.getDbKey())) {
                listMaterial.add(matMision.get(i));
            }
        }
        // add materials from this type, which have been created until now
        ArrayList<CopexTask> listTaskBefore = copexTree.getListTaskBeforeSel(modeAdd);
        int nb = listTaskBefore.size();
        for (int i = 0; i < nb; i++) {
            if (listTaskBefore.get(i) instanceof CopexActionManipulation) {
                ArrayList<Object> matProd = ((CopexActionManipulation) listTaskBefore.get(i)).getListMaterialProd();
                for (int j = 0; j < matProd.size(); j++) {
                    if (matProd.get(j) instanceof Material) {
                        Material m = (Material) matProd.get(j);
                        if (isAddMaterial(m, type.getDbKey(), dbKeyType2, listMaterial, controlType2, andTypes))
                            listMaterial.add(m);
                    } else if (matProd.get(j) instanceof ArrayList) {
                        int n = ((ArrayList) matProd.get(j)).size();
                        for (int r = 0; r < n; r++) {
                            Material m = ((ArrayList<Material>) matProd.get(j)).get(r);
                            if (isAddMaterial(m, type.getDbKey(), dbKeyType2, listMaterial, controlType2, andTypes))
                                listMaterial.add(m);
                        }
                    }
                }
            }
        }
        return listMaterial;
    }

    private boolean isAddMaterial(Material m, long dbKeyType, long dbKeyType2, ArrayList<Material> listMaterial, boolean controlType2, boolean andTypes) {
        if (m.isType(dbKeyType)) {
            if ((controlType2 && m.isType(dbKeyType2)) || !controlType2 || (controlType2 && !andTypes)) {
                boolean isInList = false;
                for (int l = 0; l < listMaterial.size(); l++) {
                    if (listMaterial.get(l).getName(getLocale()).equals(m.getName(getLocale()))) {
                        isInList = true;
                        break;
                    }
                }
                if (!isInList)
                    return true;
            }
        } else if (controlType2 && !andTypes && m.isType(dbKeyType2)) {
            boolean isInList = false;
            for (int l = 0; l < listMaterial.size(); l++) {
                if (listMaterial.get(l).getName(getLocale()).equals(m.getName(getLocale()))) {
                    isInList = true;
                    break;
                }
            }
            if (!isInList)
                return true;
        }
        return false;
    }

    /* returns the materials list prod until now */
    public ArrayList<Material> getMaterialProd(boolean modeAdd) {
        ArrayList<Material> listMaterialProd = new ArrayList();
        ArrayList<CopexTask> listTaskBefore = copexTree.getListTaskBeforeSel(modeAdd);
        int nb = listTaskBefore.size();
        for (int i = 0; i < nb; i++) {
            if (listTaskBefore.get(i) instanceof CopexActionManipulation) {
                ArrayList<Object> matProd = ((CopexActionManipulation) listTaskBefore.get(i)).getListMaterialProd();
                for (int j = 0; j < matProd.size(); j++) {
                    if (matProd.get(j) instanceof Material) {
                        boolean isInList = false;
                        for (int k = 0; k < listMaterialProd.size(); k++) {
                            if (listMaterialProd.get(k).getName(getLocale()).equals(((Material) matProd.get(j)).getName(getLocale())))
                                isInList = true;
                        }
                        // if (listMaterialProd.indexOf(matProd.get(j)) == -1)
                        if (!isInList)
                            listMaterialProd.add((Material) matProd.get(j));
                    } else if (matProd.get(j) instanceof ArrayList) {
                        int n = ((ArrayList) matProd.get(j)).size();
                        for (int r = 0; r < n; r++) {
                            boolean isInList = false;
                            for (int k = 0; k < listMaterialProd.size(); k++) {
                                if (listMaterialProd.get(k).getName(getLocale()).equals(((ArrayList<Material>) matProd.get(j)).get(r).getName(getLocale())))
                                    isInList = true;
                            }
                            // if (listMaterialProd.indexOf(matProd.get(j)) == -1)
                            if (!isInList)
                                listMaterialProd.add(((ArrayList<Material>) matProd.get(j)).get(r));
                        }
                    }
                }
            }
        }
        return listMaterialProd;
    }

    /* returns the list of data prod until now */
    public ArrayList<QData> getDataProd(boolean modeAdd) {
        ArrayList<QData> listDataProd = new ArrayList();
        ArrayList<CopexTask> listTaskBefore = copexTree.getListTaskBeforeSel(modeAdd);
        int nb = listTaskBefore.size();
        for (int i = 0; i < nb; i++) {
            if (listTaskBefore.get(i) instanceof CopexActionAcquisition || listTaskBefore.get(i) instanceof CopexActionTreatment) {
                ArrayList<Object> dataProd = new ArrayList();
                if (listTaskBefore.get(i) instanceof CopexActionAcquisition)
                    dataProd = ((CopexActionAcquisition) listTaskBefore.get(i)).getListDataProd();
                else if (listTaskBefore.get(i) instanceof CopexActionTreatment)
                    dataProd = ((CopexActionTreatment) listTaskBefore.get(i)).getListDataProd();
                for (int j = 0; j < dataProd.size(); j++) {
                    if (dataProd.get(j) instanceof QData) {
                        boolean isInList = false;
                        for (int k = 0; k < listDataProd.size(); k++) {
                            if (listDataProd.get(k).getName(getLocale()).equals(((QData) dataProd.get(j)).getName(getLocale())))
                                isInList = true;
                        }
                        if (!isInList)
                            listDataProd.add((QData) dataProd.get(j));
                    } else if (dataProd.get(j) instanceof ArrayList) {
                        int n = ((ArrayList) dataProd.get(j)).size();
                        for (int r = 0; r < n; r++) {
                            boolean isInList = false;
                            for (int k = 0; k < listDataProd.size(); k++) {
                                if (listDataProd.get(k).getName(getLocale()).equals(((ArrayList<QData>) dataProd.get(j)).get(r).getName(getLocale())))
                                    isInList = true;
                            }
                            // if (listMaterialProd.indexOf(matProd.get(j)) == -1)
                            if (!isInList)
                                listDataProd.add(((ArrayList<QData>) dataProd.get(j)).get(r));
                        }
                    }

                }
            }
        }
        return listDataProd;
    }

    /* returns the list of physical quantitites */
    public ArrayList<PhysicalQuantity> getListPhysicalQuantity() {
        return this.listPhysicalQuantity;
    }

    /* returns true if it's a material from the mission proc */
    public boolean isMaterialFromMission(long dbKeyMaterial) {
        if (proc == null)
            return false;
        ArrayList<Material> listMaterial = new ArrayList();
        if (proc instanceof LearnerProcedure) {
            listMaterial = ((LearnerProcedure) proc).getInitialProc().getListMaterial();
        } else if (proc instanceof InitialProcedure) {
            listMaterial = ((InitialProcedure) proc).getListMaterial();
        }
        int nbMat = listMaterial.size();
        for (int i = 0; i < nbMat; i++) {
            if (listMaterial.get(i).getDbKey() == dbKeyMaterial)
                return true;
        }
        return false;
    }

    /* returns the point to display the dialog */
    public Point getLocationDialog() {
        try {

            return new Point((int) this.getLocationOnScreen().getX() + (this.getWidth() / 3), (int) this.getLocationOnScreen().getY() + this.menuBar.getHeight());
        } catch (IllegalComponentStateException e) {
            return new Point(100, 100);
        }
    }

    public URL getHelpManualPage() {
        String helpFile = "languages/copexHelpManual-" + getLocale().getLanguage() + ".xhtml";
        URL urlhelp = this.getClass().getClassLoader().getResource(helpFile);
        if (urlhelp != null)
            return urlhelp;
        return this.getClass().getClassLoader().getResource("languages/copexHelpManual-en.xhtml");
    }

    /* returns true if there is a list of material to display */
    public boolean isMaterialAvailable() {
        if (proc == null)
            return false;
        if (proc instanceof LearnerProcedure)
            return ((LearnerProcedure) proc).getInitialProc().getListMaterial().size() > 0;
        else if (proc instanceof InitialProcedure) {
            return ((InitialProcedure) proc).getListMaterial().size() > 0;
        }
        return false;
    }

    public void setQuestionDialog() {
        // if(!proc.isValidQuestion(getLocale()))
        // JOptionPane.showMessageDialog(this, this.getBundleString("MSG_QUESTION"), this.getBundleString("TITLE_DIALOG_WARNING"),JOptionPane.INFORMATION_MESSAGE );
        // copexTree.setQuestionEditor();
        // if(!proc.isValidQuestion(getLocale())){
        // openQuestion = true;
        // }
    }

    /* load ELO */
    public void loadELO(Element xmlContent) {
        CopexReturn cr = this.controller.loadELO(xmlContent);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* returns the experimental procedure ELO */
    public Element getExperimentalProcedure() {
        copexTree.setSelectionRow(0);
        copexTree.setSelectionRow(1);
        copexTree.setSelectionRow(0);
        if (proc != null)
            return this.controller.getExperimentalProcedure(proc);
        return null;
    }

    /* returns the list of parameters of actions in step */
    public ArrayList[] getStepInitialParam(Step step) {
        ArrayList v = new ArrayList();
        if (proc != null) {
            CopexReturn cr = this.controller.getTaskInitialParam(proc, step, v);
            if (cr.isError()) {
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            } else {
                return (ArrayList[]) v.get(0);
            }
        }
        ArrayList[] list = new ArrayList[2];
        list[0] = new ArrayList();
        list[1] = new ArrayList();
        return list;
    }

    /* returns the list of output of actions in step */
    public ArrayList[] getStepInitialOutput(Step step) {
        ArrayList v = new ArrayList();
        if (proc != null) {
            CopexReturn cr = this.controller.getTaskInitialOutput(proc, step, v);
            if (cr.isError()) {
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            } else {
                return (ArrayList[]) v.get(0);
            }
        }
        ArrayList[] list = new ArrayList[2];
        list[0] = new ArrayList();
        list[1] = new ArrayList();
        return list;
    }

    /* returns the type material by default */
    public TypeMaterial getDefaultMaterialType() {
        return this.controller.getDefaultMaterialType();
    }

    /* sho wthe help proc */
    public void displayHelpProc() {
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.getHelpProc(v);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        cr = this.controller.openHelpProc();
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
        CopexMission helpMission = (CopexMission) v.get(0);
        LearnerProcedure helpProc = (LearnerProcedure) v.get(1);
        ArrayList<Material> listMat = (ArrayList<Material>) v.get(2);
        copexPanel.displayHelpProc(helpProc);
    }

    /* close help dialog */
    public void closeHelpDialog() {
        CopexReturn cr = this.controller.closeHelpDialog();
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    public void addEtape() {
        openDialogAddE(MyConstants.INSERT_TASK_UNDEF);
    }

    public void addStepAfter() {
        openDialogAddE(MyConstants.INSERT_TASK_AFTER);
    }

    public void addStepIn() {
        openDialogAddE(MyConstants.INSERT_TASK_IN);
    }

    public void addAction() {
        openDialogAddA(MyConstants.INSERT_TASK_UNDEF);
    }

    public void addActionAfter() {
        openDialogAddA(MyConstants.INSERT_TASK_AFTER);
    }

    public void addActionIn() {
        openDialogAddA(MyConstants.INSERT_TASK_IN);
    }

    /* rename proc */
    public void updateProcName(String name) {
        // proc.setName(CopexUtilities.getTextLocal(name, getLocale()));
        proc.setName(name);
        copexTree.updateProcName(name);
    }

    public void beginregister() {
        this.copexTree.beginregister();
    }

    public void resizeWidth(int width) {
        int value = scrollPaneTree.getVerticalScrollBar().getValue();
        this.copexTree.resizeWidth();
        scrollPaneTree.getVerticalScrollBar().setValue(value);
        if (openQuestion) {
            copexTree.setQuestionEditor();
            openQuestion = false;
        }
    }

    public void cut() {
        copy();
        TaskSelected ts = getSelectedTask();
        ExperimentalProcedure p = ts.getProc();
        SubTree subTree = getSubTreeCopy();
        suppr(false);
        CopexReturn cr = this.controller.cut(p, subTree, ts);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        procModif = true;
    }

    public void copy() {
        subTreeCopy = copexTree.getSubTreeCopy(false);
        updateMenu();
        TaskSelected ts = getSelectedTask();
        ExperimentalProcedure p = ts.getProc();
        SubTree subTree = getSubTreeCopy();
        CopexReturn cr = this.controller.copy(p, ts, subTree);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }

    }

    public void pasteIn() {
        // tache selectionnee et protocole ou il faut copier
        // TaskSelected ts = getSelectedTask();
        TaskSelected ts = copexTree.getTaskSelected(MyConstants.INSERT_TASK_IN);
        if (ts == null)
            return;
        ExperimentalProcedure p = ts.getProc();
        // list of tasks to copy
        SubTree subTree = getSubTreeCopy();
        CopexReturn cr = this.controller.paste(p, ts, subTree);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    public void pasteUnder() {
        // selected task and proc
        // TaskSelected ts = getSelectedTask();
        TaskSelected ts = copexTree.getTaskSelected(MyConstants.INSERT_TASK_AFTER);
        if (ts == null)
            return;
        ExperimentalProcedure p = ts.getProc();
        // list of tasks to copy
        SubTree subTree = getSubTreeCopy();
        CopexReturn cr = this.controller.paste(p, ts, subTree);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    public CopexPanel getCopexPanel() {
        return this.copexPanel;
    }

    private void save() {
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFile != null) {
            aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFile);
        } else {
            File file = new File(aFileChooser.getCurrentDirectory(), proc.getName(getLocale()) + ".xml");
            aFileChooser.setSelectedFile(file);
        }
        int r = aFileChooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            File file = aFileChooser.getSelectedFile();
            if (!CopexUtilities.isXMLFile(file)) {
                file = CopexUtilities.getXMLFile(file);
            }
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            Element xproc = getExperimentalProcedure();
            lastUsedFile = file;
            OutputStreamWriter fileWriter = null;
            try {
                fileWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
                xmlOutputter.output(xproc, fileWriter);
                procModif = false;
                copexPanel.logSaveProc(proc);
            } catch (IOException e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                displayError(new CopexReturn(getBundleString("MSG_ERROR_SAVE"), false), getBundleString("TITLE_DIALOG_ERROR"));
            } finally {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (fileWriter != null)
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        displayError(new CopexReturn(getBundleString("MSG_ERROR_SAVE"), false), getBundleString("TITLE_DIALOG_ERROR"));
                    }
            }
        }
    }

    private InitialProcedure getInitProc() {
        if (proc instanceof LearnerProcedure)
            return ((LearnerProcedure) proc).getInitialProc();
        else if (proc instanceof InitialProcedure)
            return ((InitialProcedure) proc);
        return null;
    }

    private boolean isMenuHypothesis() {
        return getInitProc().getHypothesisMode() == MyConstants.MODE_MENU;
    }

    private boolean isMenuPrinciple() {
        return getInitProc().getPrincipleMode() == MyConstants.MODE_MENU;
    }

    private boolean isMenuEvaluation() {
        return getInitProc().getEvaluationMode() == MyConstants.MODE_MENU;
    }

    private void addHypothesis() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Hypothesis hypothesis = null;
        Hypothesis oldHypothesis = null;
        if (proc.getHypothesis() != null)
            oldHypothesis = (Hypothesis) proc.getHypothesis().clone();
        if (proc.getHypothesis() == null) {
            hypothesis = new Hypothesis(getLocale());
        } else {
            hypothesis = proc.getHypothesis();
            hypothesis.setHide(!proc.getHypothesis().isHide());
        }
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setHypothesis(proc, hypothesis, v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        hypothesis = (Hypothesis) v.get(0);
        copexTree.addEdit_hypothesis(oldHypothesis, hypothesis);
        setHypothesis(hypothesis);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        copexTree.setHypothesisEditor();

    }

    public void setHypothesis(Hypothesis h) {
        proc.setHypothesis(h);
        copexTree.setHypothesis(h);
        copexTree.updateProc(proc);
        updateMenu();
    }

    private void addGeneralPrinciple() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        GeneralPrinciple principle = null;
        GeneralPrinciple oldPrinciple = null;
        if (proc.getGeneralPrinciple() != null)
            oldPrinciple = (GeneralPrinciple) proc.getGeneralPrinciple().clone();
        if (proc.getGeneralPrinciple() == null) {
            principle = new GeneralPrinciple(getLocale());
        } else {
            principle = proc.getGeneralPrinciple();
            principle.setHide(!proc.getGeneralPrinciple().isHide());
        }
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setGeneralPrinciple(proc, principle, v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        principle = (GeneralPrinciple) v.get(0);
        copexTree.addEdit_principle(oldPrinciple, principle);
        setGeneralPrinciple(principle);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        copexTree.setPrincipleEditor();
    }

    public void setGeneralPrinciple(GeneralPrinciple p) {
        proc.setGeneralPrinciple(p);
        copexTree.setGeneralPrinciple(p);
        copexTree.updateProc(proc);
        updateMenu();
    }

    private void addEvaluation() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Evaluation evaluation = null;
        Evaluation oldEvaluation = null;
        if (proc.getEvaluation() != null)
            oldEvaluation = (Evaluation) proc.getEvaluation().clone();
        if (proc.getEvaluation() == null) {
            evaluation = new Evaluation(getLocale());
        } else {
            evaluation = proc.getEvaluation();
            evaluation.setHide(!proc.getEvaluation().isHide());
        }
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setEvaluation(proc, evaluation, v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        evaluation = (Evaluation) v.get(0);
        copexTree.addEdit_evaluation(oldEvaluation, evaluation);
        setEvaluation(evaluation);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        copexTree.setEvaluationEditor();
    }

    public void setEvaluation(Evaluation e) {
        proc.setEvaluation(e);
        copexTree.setEvaluation(e);
        copexTree.updateProc(proc);
        updateMenu();
    }

    public String updateHypothesis(Hypothesis hypothesis, String newText, String newComment, boolean sync) {
        if (controlLenght() && newText.length() > MyConstants.MAX_LENGHT_HYPOTHESIS) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("TREE_HYPOTHESIS"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_HYPOTHESIS);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return hypothesis.getHypothesis(getLocale());
        }
        if (controlLenght() && newComment.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("LABEL_COMMENTS"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_TASK_COMMENTS);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return hypothesis.getHypothesis(getLocale());
        }
        Hypothesis oldHypothesis = (Hypothesis) proc.getHypothesis().clone();
        String oldHyp = proc.getHypothesis().getHypothesis(getLocale());
        String oldHypComment = proc.getHypothesis().getComment(getLocale());
        // proc.getHypothesis().setHypothesis(CopexUtilities.getTextLocal(newText, getLocale()));
        proc.getHypothesis().setHypothesis(newText);
        // proc.getHypothesis().setComment(CopexUtilities.getTextLocal(newComment, getLocale()));
        proc.getHypothesis().setComment(newComment);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setHypothesis(proc, proc.getHypothesis(), v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            // proc.getHypothesis().setHypothesis(CopexUtilities.getTextLocal(oldHyp, getLocale()));
            proc.getHypothesis().setHypothesis(oldHyp);
            // proc.getHypothesis().setComment(CopexUtilities.getTextLocal(oldHypComment, getLocale()));
            proc.getHypothesis().setComment(oldHypComment);
            return oldHyp;
        }
        hypothesis = (Hypothesis) v.get(0);
        copexTree.addEdit_hypothesis(oldHypothesis, hypothesis);
        setHypothesis(hypothesis);
        if (sync) {
            copexPanel.addSyncUpdateHypothesis(hypothesis);
        }
        return hypothesis.getHypothesis(getLocale());
    }

    public String updateGeneralPrinciple(GeneralPrinciple principle, String newText, String newComment, boolean sync) {
        if (controlLenght() && newText.length() > MyConstants.MAX_LENGHT_GENERAL_PRINCIPLE) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("TREE_GENERAL_PRINCIPLE"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_GENERAL_PRINCIPLE);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return principle.getPrinciple(getLocale());
        }
        if (controlLenght() && newComment.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("LABEL_COMMENTS"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_TASK_COMMENTS);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return principle.getPrinciple(getLocale());
        }
        GeneralPrinciple oldPrinciple = (GeneralPrinciple) proc.getGeneralPrinciple().clone();
        String oldPrinc = proc.getGeneralPrinciple().getPrinciple(getLocale());
        String oldPrincComment = proc.getGeneralPrinciple().getComment(getLocale());
        // proc.getGeneralPrinciple().setPrinciple(CopexUtilities.getTextLocal(newText, getLocale()));
        proc.getGeneralPrinciple().setPrinciple(newText);
        // proc.getGeneralPrinciple().setComment(CopexUtilities.getTextLocal(newComment, getLocale()));
        proc.getGeneralPrinciple().setComment(newComment);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setGeneralPrinciple(proc, proc.getGeneralPrinciple(), v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            // proc.getGeneralPrinciple().setPrinciple(CopexUtilities.getTextLocal(oldPrinc, getLocale()));
            proc.getGeneralPrinciple().setPrinciple(oldPrinc);
            // proc.getGeneralPrinciple().setComment(CopexUtilities.getTextLocal(oldPrincComment, getLocale()));
            proc.getGeneralPrinciple().setComment(oldPrincComment);
            return oldPrinc;
        }
        principle = (GeneralPrinciple) v.get(0);
        copexTree.addEdit_principle(oldPrinciple, principle);
        setGeneralPrinciple(principle);
        if (sync) {
            copexPanel.addSyncUpdateGeneralPrinciple(principle);
        }
        return principle.getPrinciple(getLocale());
    }

    public String updateEvaluation(Evaluation evaluation, String newText, String newComment, boolean sync) {
        if (controlLenght() && newText.length() > MyConstants.MAX_LENGHT_EVALUATION) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("TREE_EVALUATION"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_EVALUATION);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return evaluation.getEvaluation(getLocale());
        }
        if (controlLenght() && newComment.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("LABEL_COMMENTS"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_TASK_COMMENTS);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return evaluation.getEvaluation(getLocale());
        }
        Evaluation oldEvaluation = (Evaluation) proc.getEvaluation().clone();
        String oldEval = proc.getEvaluation().getEvaluation(getLocale());
        String oldEvalComment = proc.getEvaluation().getComment(getLocale());
        // proc.getEvaluation().setEvaluation(CopexUtilities.getTextLocal(newText, getLocale()));
        proc.getEvaluation().setEvaluation(newText);
        // proc.getEvaluation().setComment(CopexUtilities.getTextLocal(newComment, getLocale()));
        proc.getEvaluation().setComment(newComment);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setEvaluation(proc, proc.getEvaluation(), v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            // proc.getEvaluation().setEvaluation(CopexUtilities.getTextLocal(oldEval, getLocale()));
            proc.getEvaluation().setEvaluation(oldEval);
            // proc.getEvaluation().setComment(CopexUtilities.getTextLocal(oldEvalComment, getLocale()));
            proc.getEvaluation().setComment(oldEvalComment);
            return oldEval;
        }
        evaluation = (Evaluation) v.get(0);
        copexTree.addEdit_evaluation(oldEvaluation, evaluation);
        setEvaluation(evaluation);
        if (sync) {
            copexPanel.addSyncUpdateEvaluation(evaluation);
        }
        return evaluation.getEvaluation(getLocale());
    }

    public String updateQuestion(Question question, String newText, String newComment, boolean sync) {
        if (controlLenght() && newText.length() > MyConstants.MAX_LENGHT_TASK_DESCRIPTION) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("LABEL_QUESTION"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_TASK_DESCRIPTION);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return question.getDescription(getLocale());
        }
        if (newText.length() == 0) {
            // if(!openQuestion){
            // String msg = getBundleString("MSG_ERROR_FIELD_NULL");
            // msg = CopexUtilities.replace(msg, 0, getBundleString("LABEL_QUESTION"));
            // displayError(new CopexReturn(msg ,false), getBundleString("TITLE_DIALOG_ERROR"));
            // }
            // return question.getDescription(getLocale());
        }
        if (controlLenght() && newComment.length() > MyConstants.MAX_LENGHT_TASK_COMMENTS) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("LABEL_COMMENTS"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_TASK_COMMENTS);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return question.getDescription(getLocale());
        }
        Question oldQuestion = (Question) proc.getQuestion().clone();
        String oldQ = proc.getQuestion().getDescription(getLocale());
        String oldQComment = proc.getQuestion().getComments(getLocale());
        // proc.getQuestion().setDescription(CopexUtilities.getTextLocal(newText, getLocale()));
        proc.getQuestion().setDescription(newText);
        proc.getQuestion().setComments(CopexUtilities.getTextLocal(newComment, getLocale()));
        // proc.getQuestion().setComments(newComment);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateQuestion(proc.getQuestion(), proc, oldQuestion, v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            // proc.getQuestion().setDescription(CopexUtilities.getTextLocal(oldQ, getLocale()));
            proc.getQuestion().setDescription(oldQ);
            proc.getQuestion().setComments(CopexUtilities.getTextLocal(oldQComment, getLocale()));
            // proc.getQuestion().setComments(oldQComment);
            return oldQ;
        }
        openQuestion = false;
        ExperimentalProcedure newProc = (ExperimentalProcedure) v.get(0);
        updateProc(newProc);
        copexTree.updateQuestion(newProc.getQuestion());
        copexTree.addEdit_updateQuestion(oldQuestion, newProc.getQuestion());
        updateMenu();
        procModif = true;
        return proc.getQuestion().getDescription(getLocale());

    }

    public boolean hasModification() {
        if (proc.getDbKey() == -2)
            return false;
        return procModif;
    }

    public void setModification() {
        this.procModif = true;
    }

    public void openMaterialDialog() {
        MaterialStrategy s = null;
        if (proc instanceof LearnerProcedure)
            s = ((LearnerProcedure) proc).getInitialProc().getMaterialStrategy();
        else if (proc instanceof InitialProcedure)
            s = ((InitialProcedure) proc).getMaterialStrategy();
        MaterialDialog matDialog = new MaterialDialog(this, proc.getRight(), proc.getListMaterialUsed(), s);
        matDialog.setVisible(true);
    }

    // update the material used
    public boolean setMaterialUsed(ArrayList<MaterialUsed> listMaterialToCreate, ArrayList<MaterialUsed> listMaterialToDelete, ArrayList<MaterialUsed> listMaterialToUpdate, boolean sync) {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setMaterialUsed(proc, listMaterialToCreate, listMaterialToDelete, listMaterialToUpdate, v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        List<MaterialUsed> listMaterialUsed = (List<MaterialUsed>) v.get(0);
        proc.setListMaterialUsed(listMaterialUsed);
        proc.getMaterials().setListMaterialUsed(listMaterialUsed);
        copexTree.updateProc(proc);
        if (sync) {
            copexPanel.addSyncUpdateMaterial(listMaterialToCreate, listMaterialToDelete, listMaterialToUpdate);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return true;
    }

    /* return the panel for the thumbnail */
    public Container getInterfacePanel() {
        // return backgroundPanel;
        copexTree.setSize(128, 128);
        return copexTree;

    }

    @Override
    public void doMenuItemMousePressed(MyMenuItem menuItem) {
        clickMenuEvent(menuItem);
    }

    public boolean controlLenght() {
        return copexPanel.controlLenght();
    }

    public void setProcedureHypothesis(String newText) {
        if (controlLenght() && newText.length() > MyConstants.MAX_LENGHT_HYPOTHESIS) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("TREE_HYPOTHESIS"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_HYPOTHESIS);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Hypothesis oldHypothesis = (Hypothesis) proc.getHypothesis().clone();
        String oldHyp = proc.getHypothesis().getHypothesis(getLocale());
        String oldHypComment = proc.getHypothesis().getComment(getLocale());
        // proc.getHypothesis().setHypothesis(CopexUtilities.getTextLocal(newText, getLocale()));
        proc.getHypothesis().setHypothesis(newText);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setHypothesis(proc, proc.getHypothesis(), v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            // proc.getHypothesis().setHypothesis(CopexUtilities.getTextLocal(oldHyp, getLocale()));
            proc.getHypothesis().setHypothesis(oldHyp);
            // proc.getHypothesis().setComment(CopexUtilities.getTextLocal(oldHypComment, getLocale()));
            proc.getHypothesis().setComment(oldHypComment);
            return;
        }
        Hypothesis hypothesis = (Hypothesis) v.get(0);
        copexTree.addEdit_hypothesis(oldHypothesis, hypothesis);
        setHypothesis(hypothesis);
    }

    public void setProcedureQuestion(String newText) {
        if (controlLenght() && newText.length() > MyConstants.MAX_LENGHT_TASK_DESCRIPTION) {
            String msg = getBundleString("MSG_LENGHT_MAX");
            msg = CopexUtilities.replace(msg, 0, getBundleString("LABEL_QUESTION"));
            msg = CopexUtilities.replace(msg, 1, "" + MyConstants.MAX_LENGHT_TASK_DESCRIPTION);
            displayError(new CopexReturn(msg, false), getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Question oldQuestion = (Question) proc.getQuestion().clone();
        String oldQ = proc.getQuestion().getDescription(getLocale());
        String oldQComment = proc.getQuestion().getComments(getLocale());
        proc.getQuestion().setDescription(newText);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateQuestion(proc.getQuestion(), proc, oldQuestion, v);
        if (cr.isError()) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            proc.getQuestion().setDescription(oldQ);
            proc.getQuestion().setComments(CopexUtilities.getTextLocal(oldQComment, getLocale()));
            return;
        }
        openQuestion = false;
        ExperimentalProcedure newProc = (ExperimentalProcedure) v.get(0);
        updateProc(newProc);
        copexTree.updateQuestion(newProc.getQuestion());
        copexTree.addEdit_updateQuestion(oldQuestion, newProc.getQuestion());
        updateMenu();
        procModif = true;
    }

    public String getPreview() {
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.getPreview(proc, v);
        if (cr.isError()) {
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return "";
        } else {
            return (String) v.get(0);
        }
    }

    /* set the current proc in read-only mode */
    public void setExperimentalProcedureReadOnly(boolean readOnly) {
        char right = readOnly ? MyConstants.NONE_RIGHT : MyConstants.EXECUTE_RIGHT;
        this.proc.setRight(right);
        copexTree.updateProc(proc, true);
    }

    public TaskSelected getTaskSelected(CopexTask selTask, char insertIn) {
        return copexTree.getTaskSelected(selTask, insertIn);
    }


}
