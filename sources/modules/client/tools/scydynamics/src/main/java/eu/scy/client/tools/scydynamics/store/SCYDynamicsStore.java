package eu.scy.client.tools.scydynamics.store;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;

public abstract class SCYDynamicsStore {
	
	public enum StoreType {
		MANUAL, TIMER, ON_EXIT, ON_SAVE, ON_SIMULATE_GRAPH, ON_SIMULATE_TABLE, ON_FEEDBACK, ON_LOAD, ON_NEW;
	}
	
	//private static Logger debugLogger = Logger.getLogger(SCYDynamicsStore.class.getName());
	private ModelEditor editor;
	private String modelName = null;

	public SCYDynamicsStore(ModelEditor editor) {
		this.editor = editor;
	}
	
	public ModelEditor getEditor() {
		return this.editor;
	}
	
	public void setModelName(String modelName) {
		this.modelName  = modelName;
		editor.updateTitle();
	}
	
	public String getModelName() {
		return this.modelName;
	}
 	
	public abstract void doSaveScreenshot(BufferedImage img) throws Exception;

	public abstract void doAutoSave(StoreType type) throws Exception;

	public abstract void loadModel() throws Exception;
	
	public abstract void loadModel(String fileName) throws Exception;
	
	public abstract void saveModel() throws Exception;
	
	public abstract void saveModel(String fileName) throws Exception;
	
	public abstract void saveAsModel() throws Exception;

//	public Vector<Sketch> loadExample(String fileName) {
//		// examples are always loaded as a file (from the classpath)
//		try {
//			InputStream stream = FileStore.class.getResourceAsStream(fileName);
//			File file = new File(fileName);
//			debugLogger.info("trying to load example: "+file.getAbsolutePath());
//			return loadPages(stream);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
//	protected Vector<Sketch> loadPages(InputStream stream) {
//		try {
//			SAXBuilder builder = new SAXBuilder();
//			Document document = builder.build(stream);
//			return loadPages(document);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
//	protected Vector<Sketch> loadPagesFromString(String stringDocument) {
//		SAXBuilder sb = new SAXBuilder();
//		Document doc;
//		try {
//			doc = sb.build(new StringReader(stringDocument));
//			return loadPages(doc);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		}
//	}
	
//	private Vector<Sketch> loadPages(Document document) {
//		Element root = document.getRootElement();
//		Vector<Sketch> sketches = new Vector<Sketch>();
//		if (root.getName().equals("pages")) {
//			// seems to be a multi-page document
//			for (Element sketchElement : (List<Element>) root.getChildren("sketch")) {
//				sketches.add(new Sketch(sketchElement, this.abstractDrawing.getSacaLogger()));
//			}
//		} else if (root.getName().equals("sketch")) {
//			// seems to be a single-page document
//			sketches.add(new Sketch(root, this.abstractDrawing.getSacaLogger()));					
//		}
//		abstractDrawing.getSacaPanel().setPages(sketches);
//		if (abstractDrawing instanceof AbstractDrawing) {
//			abstractDrawing.getSacaPanel().getBehaviorSpecificationScroller().setBehaviorSpecificationPanel(root);
//			abstractDrawing.getSacaPanel().moveToFront(Front.BEHAVIOR);
//			abstractDrawing.getSacaPanel().moveToFront(Front.SKETCH);
////			if (abstractDrawing instanceof SimSketchPO2Modelling) {
////				abstractDrawing.getSacaPanel().getBehaviorSpecificationPanel().setShowBehaviorInfo(((SimSketchPO2Modelling)abstractDrawing).getModellingMenu().isShowingBehaviorInfo());
////			} else {
////				abstractDrawing.getSacaPanel().getBehaviorSpecificationPanel().setShowBehaviorInfo(((SimSketch)abstractDrawing).getModellingMenu().isShowingBehaviorInfo());
////			}
//			abstractDrawing.getSacaPanel().getBehaviorSpecificationPanel().setShowBehaviorInfo(abstractDrawing.getModellingMenu().isShowingBehaviorInfo());
//		}
//		return sketches;
//	}
	public static Element findTag(Element root, String tag) throws IllegalArgumentException {
		if (root == null) {
			throw new IllegalArgumentException("Element is null.");
		}
		List<?> enumChilds = root.getChildren();
		Iterator<?> iter = enumChilds.iterator();
		while (iter.hasNext()) {
			Element childElement = (Element) iter.next();
			if (childElement.getName().equals(tag)) {
				return childElement;
			}
			try {
				Element foundElement = findTag(childElement, tag);
				if (foundElement != null) {
					return foundElement;
				}
			} catch (IllegalArgumentException e) {
			}
		}
		return null;
	}
	
}