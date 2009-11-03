package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.IConceptMapListener;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.IDiagramSelectionModel;
import eu.scy.scymapper.impl.DiagramModel;

import java.util.ArrayList;
import java.util.List;


/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 15:36:54
 */
public class DefaultConceptMap implements IConceptMap {
	private String name = "No name";
	private IDiagramModel diagram = new DiagramModel();

	private transient List<IConceptMapListener> listeners = new ArrayList<IConceptMapListener>();

	private transient IDiagramSelectionModel selectionModel = new DefaultDiagramSelectionModel();

	private Object readResolve() {
		listeners = new ArrayList<IConceptMapListener>();
		selectionModel = new DefaultDiagramSelectionModel();
		System.out.println("After instantiating MyExecutor");
		// at the end returns itself
		return this;
	}

	public DefaultConceptMap(String name, IDiagramModel diagram) {
		this.name = name;
		this.diagram = diagram;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		notifyListeners();
	}

	@Override
	public IDiagramModel getDiagram() {
		return diagram;
	}

	@Override
	public IDiagramSelectionModel getDiagramSelectionModel() {
		return selectionModel;
	}

    @Override
	public void setSelectionModel(IDiagramSelectionModel selectionModel) {
		this.selectionModel = selectionModel;
	}

	@Override
	public void setDiagram(IDiagramModel diagram) {
		this.diagram = diagram;
		notifyListeners();
	}

	@Override
	public void addConceptMapListener(IConceptMapListener l) {
		listeners.add(l);
	}

	@Override
	public void removeConceptMapListener(IConceptMapListener l) {
		listeners.remove(l);
	}

	@Override
	public void notifyListeners() {
		for (IConceptMapListener listener : listeners) {
			listener.conceptMapUpdated(this);
		}
	}
}
