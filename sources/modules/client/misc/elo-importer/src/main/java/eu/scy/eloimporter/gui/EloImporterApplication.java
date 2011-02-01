package eu.scy.eloimporter.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.eloimporter.ELOImporter;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;
import eu.scy.eloimporter.gui.panels.general.GeneralPanel;

public class EloImporterApplication extends JFrame {

	private final class ExitAction extends AbstractAction {

		public ExitAction() {
			super("Exit");
			this.putValue(Action.SHORT_DESCRIPTION, "Save as ELO");
			this.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		}

		public void actionPerformed(ActionEvent e) {
			EloImporterApplication.this.dispatchEvent(new WindowEvent(
					EloImporterApplication.this, WindowEvent.WINDOW_CLOSING));
		}
	}

	private final class SaveAction extends AbstractAction {

		public SaveAction() {
			super("Save");
			this.putValue(Action.SHORT_DESCRIPTION, "Save as ELO");
			this.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		}

		public void actionPerformed(ActionEvent e) {
			EloImporterApplication.this.getImporter().saveElo(
					EloImporterApplication.this.getElo());
		}
	}

	private final class ImportAction extends AbstractAction {

		public ImportAction() {
			super("Import");
			this.putValue(Action.SHORT_DESCRIPTION, "Import File");
			this.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_I));
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			if (chooser.showOpenDialog(EloImporterApplication.this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				EloImporterApplication.this.setElo(EloImporterApplication.this
						.getImporter().importFile(file));
			}

		}
	}

	private IELO<IMetadataKey> elo;
	private ELOImporter importer;
	private JSplitPane splitPane;

	public EloImporterApplication() {
		super("EloImporter");
		this.importer = new ELOImporter();
		this.elo = this.importer.createNewElo();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		this.initMenu();
		this.initComponents();

		setElo(elo);

		this.setSize(600, 400);
		// this.pack();
		// System.out.println(this.getSize());
	}

	public void setElo(IELO<IMetadataKey> importedElo) {
		this.elo = importedElo;
		Component component = this.splitPane.getRightComponent();
		if (component instanceof AbstractEloDisplayPanel) {
			((AbstractEloDisplayPanel) component).setElo(importedElo);
		}
		this.validate();
	}

	public IELO<IMetadataKey> getElo() {
		return this.elo;
	}

	public ELOImporter getImporter() {
		return this.importer;
	}

	private void initMenu() {
		this.setJMenuBar(new JMenuBar());
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new ImportAction());
		fileMenu.add(new SaveAction());
		fileMenu.add(new ExitAction());
		this.getJMenuBar().add(fileMenu);
	}

	private void initComponents() {
		this.splitPane = new JSplitPane();
		JTree metadataTree = new JTree(new DefaultTreeModel(this.initNodes()));
		metadataTree.addTreeSelectionListener(new MetadataKeySelectedListener(
				this));
		this.splitPane.setLeftComponent(metadataTree);
		this.splitPane.setRightComponent(new GeneralPanel(this));
		this.splitPane.setDividerSize(5);
		this.splitPane.setDividerLocation(200);
		this.add(this.splitPane, BorderLayout.CENTER);
		this.expandAll(metadataTree, true);
	}

	public void expandAll(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();

		// Traverse tree from root
		this.expandAll(tree, new TreePath(root), expand);
	}

	@SuppressWarnings("unchecked")
	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				this.expandAll(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	private DefaultMutableTreeNode initNodes() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Lom");

		DefaultMutableTreeNode general = new DefaultMutableTreeNode("General");

		DefaultMutableTreeNode title = new DefaultMutableTreeNode("Title");
		general.add(title);

		DefaultMutableTreeNode aggregationLevel = new DefaultMutableTreeNode(
				"Aggregation Level");
		general.add(aggregationLevel);
		DefaultMutableTreeNode description = new DefaultMutableTreeNode(
				"Description");
		general.add(description);
		DefaultMutableTreeNode keyword = new DefaultMutableTreeNode("Keywords");
		general.add(keyword);
		DefaultMutableTreeNode structure = new DefaultMutableTreeNode(
				"Logical Representation");
		general.add(structure);
		root.add(general);

		DefaultMutableTreeNode technical = new DefaultMutableTreeNode(
				"Technical");
		// DefaultMutableTreeNode format = new DefaultMutableTreeNode("Format");
		// technical.add(format);
		// DefaultMutableTreeNode size = new DefaultMutableTreeNode("Size");
		// technical.add(size);
		root.add(technical);

		DefaultMutableTreeNode lifecycle = new DefaultMutableTreeNode(
				"Lifecycle");
		DefaultMutableTreeNode contribute = new DefaultMutableTreeNode(
				"Contribute");
		lifecycle.add(contribute);
		root.add(lifecycle);

		DefaultMutableTreeNode educational = new DefaultMutableTreeNode(
				"Educational");
		DefaultMutableTreeNode learningResource = new DefaultMutableTreeNode(
				"Functional Role");
		educational.add(learningResource);
		root.add(educational);

		DefaultMutableTreeNode rights = new DefaultMutableTreeNode("Rights");
		DefaultMutableTreeNode copyright = new DefaultMutableTreeNode(
				"Copyright");
		rights.add(copyright);
		root.add(rights);

		DefaultMutableTreeNode relation = new DefaultMutableTreeNode("Relation");
		root.add(relation);

		DefaultMutableTreeNode content = new DefaultMutableTreeNode("Content");
		root.add(content);

		return root;
	}

	public static void main(String[] args) {
		new EloImporterApplication().setVisible(true);
	}

	public JSplitPane getSplitPane() {
		return this.splitPane;
	}

}
