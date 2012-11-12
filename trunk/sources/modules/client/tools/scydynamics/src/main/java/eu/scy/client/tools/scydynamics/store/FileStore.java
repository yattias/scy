package eu.scy.client.tools.scydynamics.store;

import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;

public class FileStore extends SCYDynamicsStore {

	private String storeDirName = "store";
	private File autosaveDir;
	private File loggingDir;
	
	private File screenshotDir;
	private static Logger debugLogger = Logger.getLogger(FileStore.class.getName());

	public FileStore(ModelEditor editor) {
		super(editor);
		try {
			prepareStore();
		} catch (Exception ex) {
			debugLogger.warning(ex.getMessage());
		}
	}

	private void prepareStore() throws Exception {
		if (getEditor().getProperties().getProperty("autoSave").equalsIgnoreCase("false")) {
			// the "store" is not needed; don't create it
			return;
		}
		
		File storeDir = new File(storeDirName);
		if (!storeDir.exists()) {
			debugLogger.log(Level.INFO, "creating store directory: " + storeDir.mkdir());
		}
		String userDirName = storeDirName + "/" + getEditor().getUsername();
		File userDir = new File(userDirName);
		if (!userDir.exists()) {
			debugLogger.log(Level.INFO, "creating user directory: " + userDir.mkdir());
		}
		String autosaveDirName = storeDirName + "/" + getEditor().getUsername() + "/autosave";
		autosaveDir = new File(autosaveDirName);
		if (!autosaveDir.exists()) {
			debugLogger.log(Level.INFO, "creating autosave directory: " + autosaveDir.mkdir());
		}
		String loggingDirName = storeDirName + "/" + getEditor().getUsername() + "/log";
		loggingDir = new File(loggingDirName);
		if (!loggingDir.exists()) {
			debugLogger.log(Level.INFO, "creating log directory: " + loggingDir.mkdir());
		}
		String screenshotDirName = storeDirName + "/" + getEditor().getUsername() + "/screenshots";
		screenshotDir = new File(screenshotDirName);
		if (!screenshotDir.exists()) {
			debugLogger.log(Level.INFO, "creating screenshot directory: " + screenshotDir.mkdir());
		}
	}

	private File getAutosaveDir() {
		return this.autosaveDir;
	}

	public File getLogDir() {
		return this.loggingDir;
	}

	private File getScreenshotDir() {
		return this.screenshotDir;
	}

	@Override
	public void doSaveScreenshot(BufferedImage img) throws Exception {
		File fi = new File(getScreenshotDir(), "screenshot-" + getEditor().getUsername() + "-" + System.currentTimeMillis() + ".png");
		debugLogger.info("saving screenshot to file: " + fi.getAbsolutePath());
		ImageIO.write(img, "png", fi);
		debugLogger.info("screenshot has been successfully stored to the filesystem.");
	}

	@Override
	public void doAutoSave(StoreType type) throws IOException, JDOMException {
		File fi = new File(getAutosaveDir(), "autosave-" + getEditor().getUsername() + "-" + System.currentTimeMillis() + "-" + type + ".xml");
		debugLogger.info("autosaving to file: " + fi.getAbsolutePath());
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(getEditor().getModelXML()));
		doc.getRootElement().setAttribute("mode", getEditor().getMode().toString());
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		FileWriter writer = new FileWriter(fi);
		out.output(doc, writer);
		writer.flush();
		writer.close();
		setModelName(fi.getAbsolutePath());			
	}

	// private void saveSketch(Sketch sketch) {
	// PrintWriter pw = null;
	// try {
	// JFileChooser fc = new JFileChooser();
	// if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	// File file = fc.getSelectedFile();
	// FileWriter fw = new FileWriter(file);
	// XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
	// out.output(new Document(sketch.toXMLElement()), fw);
	// fw.flush();
	// fw.close();
	// }
	// } catch (IOException e) {
	// System.err.println("Caught IOException: " + e.getMessage());
	// } finally {
	// if (pw != null) {
	// pw.close();
	// }
	// }
	// }

	// @Override
	// public void saveSketches() throws Exception {
	// ModelDrawingPanel modelDrawingPanel =
	// this.abstractDrawing.getSacaPanel();
	// JFileChooser fc = new JFileChooser();
	// if (rememberedFolder != null) {
	// fc.setSelectedFile(rememberedFolder);
	// }
	// if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	// if (fc.getSelectedFile().exists()) {
	// int n = JOptionPane.showConfirmDialog(
	// modelDrawingPanel.getCurrentSketchPanel(),
	// SimSketchLocalisation.getString("menu.file.overwrite"),
	// SimSketchLocalisation.getString("menu.file.exists"),
	// JOptionPane.YES_NO_OPTION);
	// if (n == JOptionPane.YES_OPTION) {
	// rememberedFolder = fc.getSelectedFile();
	// saveSketches(modelDrawingPanel, fc.getSelectedFile());
	// } else {
	// saveSketches();
	// }
	// } else {
	// rememberedFolder = fc.getSelectedFile();
	// saveSketches(modelDrawingPanel, fc.getSelectedFile());
	// }
	// }
	// }

	// private void saveSketches(ModelDrawingPanel modelDrawingPanel, File file)
	// throws IOException {
	// //PrintWriter pw = null;
	// FileWriter fw = new FileWriter(file);
	// XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
	// out.output(modelDrawingPanel.getDocument(), fw);
	// fw.flush();
	// fw.close();
	// // if (pw != null) {
	// // pw.close();
	// // }
	// }

	// @Override
	// public Vector<Sketch> loadPages(String fileName) {
	// try {
	// InputStream stream = FileStore.class.getResourceAsStream(fileName);
	// File file = new File(fileName);
	// debugLogger.info("trying to load "+file.getAbsolutePath());
	// return loadPages(stream);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	// private Vector<Sketch> loadPages(File file) {
	// try {
	// return loadPages(new FileInputStream(file));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	// @Override
	// public Vector<Sketch> loadPages() {
	// JFileChooser fc = new JFileChooser();
	// if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	// return loadPages(fc.getSelectedFile());
	// } else {
	// return null;
	// }
	// }

	@Override
	public void loadModel() throws Exception {
		FileDialog dialog = new FileDialog(javax.swing.JOptionPane.getFrameForComponent(getEditor()), "Load model...", FileDialog.LOAD);
		// dialog.setFile("*.xml");
		dialog.setVisible(true);
		if (dialog.getFile() != null) {
			loadModel(dialog.getDirectory() + dialog.getFile());
		}

	}

	@Override
	public void loadModel(String filename) throws Exception {
		Document doc = null;
		SAXBuilder sb = new SAXBuilder();
		InputStream stream = getClass().getResourceAsStream(filename);
		if (stream != null) {
			doc = sb.build(stream);
		} else {
			doc = sb.build(new File(filename));
		}
		Element modelElement = null;
		if (doc.getRootElement().getName().equals("model")) {
			// root element is <model>, so everything is fine
			modelElement = doc.getRootElement();
		} else {
			// root element is not <model>, try to find it
			modelElement = findTag(doc.getRootElement(), "model");
		}
		if (modelElement != null) {
			getEditor().setModel(modelElement);
			getEditor().getActionLogger().logLoadAction(filename, getEditor().getModelXML());
			getEditor().doAutosave(StoreType.ON_LOAD);
			setModelName(filename);
		} else {
			throw new JDOMException("Couldn't find <model> element in file " + filename);
		}
	}

	@Override
	public void saveModel() throws Exception {
		if (getModelName() != null) {
			saveModel(getModelName());
		} else {
			saveAsModel();
		}
	}

	@Override
	public void saveModel(String fileName) throws Exception {
		debugLogger.info("saving model to " + fileName);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(getEditor().getModelXML()));
		doc.getRootElement().setAttribute("mode", getEditor().getMode().toString());
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		FileWriter writer = new FileWriter(fileName);
		out.output(doc, writer);
		writer.flush();
		writer.close();
		setModelName(fileName);
		getEditor().getActionLogger().logSimpleAction(ModellingLogger.MODEL_SAVED);
		getEditor().doAutosave(StoreType.ON_SAVE);
	}

	@Override
	public void saveAsModel() throws Exception {
		FileDialog dialog = new FileDialog(javax.swing.JOptionPane.getFrameForComponent(getEditor()), "Save model to a file...", FileDialog.SAVE);
		dialog.setFile("*.xml");
		dialog.setVisible(true);
		if (dialog.getFile() != null) {
			saveModel(dialog.getDirectory() + dialog.getFile());
		}	
	}

}
