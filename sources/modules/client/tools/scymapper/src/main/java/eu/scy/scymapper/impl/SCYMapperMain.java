package eu.scy.scymapper.impl;

import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.impl.component.ConceptDiagramView;
import eu.scy.scymapper.impl.controller.DiagramController;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBlue;

/**
 * User: Bjoerge
 * Date: 27.aug.2009
 * Time: 13:29:56
 */
public class SCYMapperMain extends JFrame {
	//private JXTaskPaneContainer taskPaneContainer;
	private JTabbedPane tabPane;
	private IDiagramModel diagramModel;
	private ConceptDiagramView diagramView;
	private DiagramController diagramController;
	private JToolBar toolBar;

	public static void main(String[] args) {
		new SCYMapperMain().setVisible(true);
	}

	public SCYMapperMain() throws HeadlessException {
		super("SCYMapper");
		initComponents();
		getContentPane().add(tabPane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 900);
		//setJMenuBar();
	}

	private void initLaf() {
        Options.setDefaultIconSize(new Dimension(18, 18));

        // Set font options
        UIManager.put(
            Options.USE_SYSTEM_FONTS_APP_KEY,
            true);
        Options.setUseNarrowButtons(false);

        // Global options
        Options.setTabIconsEnabled(true);
        UIManager.put(Options.POPUP_DROP_SHADOW_ENABLED_KEY, true);

        // Swing Settings
        LookAndFeel selectedLaf = UIManager.getLookAndFeel();
        if (selectedLaf instanceof PlasticLookAndFeel) {
            PlasticLookAndFeel.setCurrentTheme(new DesertBlue());
            PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
            PlasticLookAndFeel.setHighContrastFocusColorsEnabled(false);
        } else if (selectedLaf.getClass() == MetalLookAndFeel.class) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        }

        // Work around caching in MetalRadioButtonUI
        JRadioButton radio = new JRadioButton();
        radio.getUI().uninstallUI(radio);
        JCheckBox checkBox = new JCheckBox();
        checkBox.getUI().uninstallUI(checkBox);

        try {
            UIManager.setLookAndFeel(selectedLaf);
        } catch (Exception e) {
            System.out.println("Can't change L&F: " + e);
        }

	}

	private void initComponents() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			LookAndFeelAddons.setAddon(MetalLookAndFeelAddons.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (InstantiationException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IllegalAccessException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		//taskPaneContainer = new JXTaskPaneContainer();
		diagramModel = new DiagramModel();

		diagramView = new ConceptDiagramView(new DiagramController(diagramModel), diagramModel);
		tabPane = new JTabbedPane();

		tabPane.add("SCYMapper", diagramView);
	}
}
