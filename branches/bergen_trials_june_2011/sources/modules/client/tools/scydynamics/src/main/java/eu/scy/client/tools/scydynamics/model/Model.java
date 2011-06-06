package eu.scy.client.tools.scydynamics.model;


import colab.um.draw.JdAux;
import colab.um.draw.JdColor;
import colab.um.draw.JdConst;
import colab.um.draw.JdDataset;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdHandle;
import colab.um.draw.JdLink;
import colab.um.draw.JdNode;
import colab.um.draw.JdObject;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;
import colab.um.draw.JdSubObject;
import colab.um.draw.JdTools;
import colab.um.parser.JParser;
import colab.um.tools.JTools;
import colab.um.xml.model.JxmAux;
import colab.um.xml.model.JxmConst;
import colab.um.xml.model.JxmDataset;
import colab.um.xml.model.JxmFlow;
import colab.um.xml.model.JxmGraphExpr;
import colab.um.xml.model.JxmModel;
import colab.um.xml.model.JxmRelation;
import colab.um.xml.model.JxmSpec;
import colab.um.xml.model.JxmSpecAux;
import colab.um.xml.model.JxmSpecConst;
import colab.um.xml.model.JxmSpecDataset;
import colab.um.xml.model.JxmSpecStock;
import colab.um.xml.model.JxmSpecTime;
import colab.um.xml.model.JxmStock;
import colab.um.xml.model.JxmVarSpec;
import colab.um.xml.types.JxtIntID;
import colab.um.xml.types.JxtTools;
import colab.um.xml.types.JxtVariable;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.logging.IModellingLogger;

public class Model {
	private final static Logger DEBUGLOGGER = Logger.getLogger(Model.class.getName());
	private Hashtable<String,JdObject> objs = new Hashtable<String,JdObject>(); // key(String)=object name, value(JdObject)=the object
	private Color bColor = Color.white;
	private Color fColor = Color.black;
	private double start,stop,step;
	private String method;
	private String name = "model";
	private boolean bQualitative = true; // allow qualitative modelling?
	//private IModellingLogger logger;
	private ModelEditor editor;

	public Model(ModelEditor editor) {
		this.editor = editor;
		setDefaultTimes();
	}

	public void  setForeground(Color c) { fColor = c; }
	public void  setBackground(Color c) { bColor = c; }
	public Color getForeground()        { return fColor; }
	public Color getBackground()        { return bColor; }

	public double getStart()  { return start; }
	public double getStop()   { return stop; }
	public double getStep()   { return step; }
	public String getMethod() { return method; }
	public String getName()   { return name; }
	
	public void setStart(double d)  { start  = d; }
	public void setStop(double d)   { stop   = d; }
	public void setStep(double d)   { step   = d; }
	public void setMethod(String s) { method = s; }
	public void setName(String s)   { name = s; }
	
	public void setDefaultTimes() {
		start  = 0.0;
		stop   = 10.0;
		method = "Euler";
		step   = 0.1;
	}
	
	public Hashtable<String,JdObject> getObjects() { return objs; }
	
	public void clearObjects() {
		objs.clear();
		JdColor.resetColors();
	}
	
	public void addObject(JdObject aObj, boolean bNewLabelColor) {
		if (aObj.isNode()) {
			if (bNewLabelColor)
				aObj.setLabelColor(JdColor.getNewFreeColor());
			else
				JdColor.registerColor(aObj.getLabelColor());
		}
		objs.put(aObj.getLabel(), aObj);
		// this has to be logged in the EditorMouseListener
		// because of the "adding link in progress problem"
		//logger.logAddAction(aObj);
	}

	public void removeObject(JdObject aObj) {
		JdColor.freeColor(aObj.getLabelColor());
		objs.remove(aObj.getLabel());
		editor.getActionLogger().logDeleteAction(aObj, this.getXmModel().getXML("", true));
	}

	public void renameObject(String oldName, String newName) {
		JdObject aObj = objs.get(oldName);
		objs.remove(oldName);
		objs.put(newName, aObj);
	}

	public boolean hasObjectOfName(String name) {
		String lowerName = name.toLowerCase();
		for (JdObject o : objs.values()) {
			if (lowerName.equals(o.getLabel().toLowerCase())) return true;
		}
		return false;
	}

	public Vector<JdObject> getObjectsOfType(int type) {
		Vector<JdObject> v = new Vector<JdObject>();
		for (JdObject o : objs.values()) {
			if (o.getType()==type) { v.add(o); }
		}
		return v;
	}

	public JdObject getObjectOfName(Hashtable<String,JdObject> h, String s) {
		if (s==null) return null;
		for (JdObject o : h.values()) {
			if (o.getLabel().equals(s)) { return o; }
		}
		return null;
	}

	public JdObject getObjectOfName(String s) {
		return getObjectOfName(objs,s);
	}

	public void renameObjectKey(String oldName, String newName) {
		renameObject(oldName,newName);
		for (JdObject o : objs.values()) {
			if (o.isObject()) {
				String expr = o.getExpr();
				if (expr!=null && expr.length()>0) // change name in other object expressions
					o.setExpr(JParser.replaceToken(expr,oldName,newName));
			}
		}
	}

	public void changeNodeRelations(String name, int type) {
		for (JdRelation l : getRelations()) {
			JdFigure f2 = l.getFigure2();
			if (f2.isNode()) {
				JdNode n = (JdNode) f2;
				if (n.getLabel().equals(name)) l.setRelationType(type==JdNode.EXPR_QUALITATIVE ? JdRelation.R_UNKNOWN : JdRelation.R_FX);
			}
		}
	}

	public void updateNodeRelations(String name, Hashtable hr) {
		for (JdRelation l : getRelations()) {
			JdFigure f1 = l.getFigure1();
			JdFigure f2 = l.getFigure2();
			if (f1!=null && f2!=null && f1.isNode() && f2.isNode()) {
				String n1 = ((JdNode) f1).getLabel();
				String n2 = ((JdNode) f2).getLabel();
				if (n2.equals(name) && hr.containsKey(n1))
					l.setRelationType(((Integer) hr.get(n1)).intValue());
			}
		}
	}

	public Vector<JdStock> getStocks() {
		Vector<JdStock> v = new Vector<JdStock>();
		for (JdObject o : objs.values()) if (o.isStock()) { v.add((JdStock) o); }
		return v;
	}
	//-------------------------------------------------------------------------
	public Vector<JdAux> getAuxs() { 
		Vector<JdAux> v = new Vector<JdAux>();
		for (JdObject o : objs.values()) if (o.isAux()) { v.add((JdAux) o); }
		return v;
	}
	//-------------------------------------------------------------------------
	public Vector<JdConst> getConstants() { 
		Vector<JdConst> v = new Vector<JdConst>();
		for (JdObject o : objs.values()) if (o.isConstant()) { v.add((JdConst) o); }
		return v;
	}
	//-------------------------------------------------------------------------
	public Vector<JdDataset> getDatasets() { 
		Vector<JdDataset> v = new Vector<JdDataset>();
		for (JdObject o : objs.values()) if (o.isDataset()) { v.add((JdDataset) o); }
		return v;
	}
	//-------------------------------------------------------------------------
	public Vector<JdFlow> getFlows() { 
		Vector<JdFlow> v = new Vector<JdFlow>();
		for (JdObject o : objs.values()) if (o.isFlow()) { v.add((JdFlow) o); }
		return v;
	}
	//-------------------------------------------------------------------------
	public Vector<JdRelation> getRelations() { 
		Vector<JdRelation> v = new Vector<JdRelation>();
		for (JdObject o : objs.values()) if (o.isRelation()) { v.add((JdRelation) o); }
		return v;
	}
	//-------------------------------------------------------------------------
	public Hashtable<String,JdObject> getNodes() {
		Hashtable<String,JdObject> h = new Hashtable<String,JdObject>();
		for (JdObject o : objs.values()) if (o.isNode()) { h.put(o.getLabel(),o); }
		return h;
	}
	//-------------------------------------------------------------------------
	public Hashtable<String,Color> getNodeNamesAndColors() {
		Hashtable<String,Color> h = new Hashtable<String,Color>();
		for (JdObject o : objs.values()) if (o.isNode()) { h.put(o.getLabel(),o.getLabelColor()); }
		return h;
	}
	//-------------------------------------------------------------------------
	public Vector<Color> getObjectsLabelColors(Vector<String> v) {
		Vector<Color> vc = new Vector<Color>();
		for (String on : v) if (objs.containsKey(on)) vc.add((objs.get(on)).getLabelColor());
		return vc;
	}
	//-------------------------------------------------------------------------
	public Vector<JdLink> getLinks() {
		Vector<JdLink> v = new Vector<JdLink>();
		for (JdObject o : objs.values()) if (o.isLink()) { v.add((JdLink) o); }
		return v;
	}
	//-------------------------------------------------------------------------
	// remove objects from model and links connected to it
	//-------------------------------------------------------------------------
	public JdLink getLink(String start, String end) {
		for (JdLink l : getLinks()) {
			JdFigure f1 = l.getFigure1();
			JdFigure f2 = l.getFigure2();
			if (f1!=null && f2!=null && f1.isNode() && f2.isNode()) {
				JdNode n1 = (JdNode) f1;
				JdNode n2 = (JdNode) f2;
				if (n1.getLabel().equals(start) && n2.getLabel().equals(end)) return l;
			}
		}
		return null;
	}

	private void removeLinksConnectedTo(JdObject aObj) {
		for (JdLink l : getLinks()) {
			boolean bRemove = false;
			JdFigure f1 = l.getFigure1();
			JdFigure f2 = l.getFigure2();
			if (aObj.containsFigure(f1)) {
				if (l.isFlow()) { l.setFigure1(null); } else { bRemove = true; }
			}
			if (aObj.containsFigure(f2)) {
				if (l.isFlow()) { l.setFigure2(null); } else { bRemove = true; }
			}
			if (bRemove) { removeObject(l); }
		}
	}

	public void removeObjectAndRelations(JdFigure aObj) {
		if (aObj == null) { return; }
		switch (aObj.getType()) {
		case JdFigure.STOCK:
		case JdFigure.AUX:
		case JdFigure.CONSTANT:
		case JdFigure.DATASET:
			removeLinksConnectedTo((JdObject) aObj);
			removeObject((JdObject) aObj);
			break;
		case JdFigure.RELATION:
			removeObject((JdObject) aObj);
			break;
		case JdFigure.FLOW:
			removeLinksConnectedTo((JdObject) aObj);
			removeObject((JdObject) aObj);
			break;
		case JdFigure.FLOWCTR:
		case JdFigure.FLOWSRC:
			removeLinksConnectedTo(((JdSubObject) aObj).getParent());
			removeObject(((JdSubObject) aObj).getParent());
			break;
		case JdFigure.HANDLE:
			removeObject(((JdHandle) aObj).getParent());
			break;
		}
	}

	public Vector<String> getLinkedVariablesTo(JdNode aNode, boolean bRelType) {
		Vector<String> v = new Vector<String>();
		//if (aNode.isStock()) return v; // stocks has not linked variables
		for (JdRelation l : getRelations()) {
			JdFigure f1 = l.getFigure1();
			JdFigure f2 = l.getFigure2();
			if (f2==aNode && f1!=null && f1.isNode()) { // add only relations that end on node
				String s = ((JdNode) f1).getLabel();
				if (bRelType && l.isRelation()) s += "[" + l.getRelationType() + "]";
				v.add(s);
			}
		}
		return v;
	}

	public Vector<JdNode> getVarsConnectedToFlow(JdFlow aFlow) {
		Vector<JdNode> v = new Vector<JdNode>();
		for (JdLink l : getLinks()) {
			JdFigure f1 = l.getFigure1();
			JdFigure f2 = l.getFigure2();
			if (aFlow.containsFigure(f2)) { if (f1!=null && f1.isNode()) v.add((JdNode) f1); }
		}
		return v;
	}
	//-------------------------------------------------------------------------
	private void updateStocksFlows() {
		for (JdStock s : getStocks()) {
			s.delInflow();
			s.delOutflow();
		}
		for (JdFlow f : getFlows()) {
			JdFigure f1 = f.getFigure1();
			JdFigure f2 = f.getFigure2();
			if (f1!=null && f1.isStock()) { // must be stocks -> outflows
				for (JdNode node1 : getVarsConnectedToFlow(f)) {
					((JdStock) f1).addOutflow(node1.getLabel());
				}
			}
			if (f2!=null && f2.isStock()) { // must be stocks -> inflows
				for (JdNode node2 : getVarsConnectedToFlow(f)) {
					((JdStock) f2).addInflow(node2.getLabel());
				}
			}
		} // while flows
	}

	private Vector<JdRelation> getRelationsConnectedTo(JdNode n) {
		Vector<JdRelation> v = new Vector<JdRelation>();
		for (JdRelation l : getRelations()) {
			JdFigure f2 = l.getFigure2();
			if (f2==n) v.add(l);
		}
		return v;
	}

	public void updateAuxExpressions() {
		for (JdAux a : getAuxs()) {
			String aExpr = null;
			Vector<JdRelation> v = getRelationsConnectedTo(a);
			switch (a.getExprType()) {
			case JdNode.EXPR_QUALITATIVE:
				for (JdRelation r : v) {
					if (r.getFigure1()==null) { aExpr=null; break; }
					String source = r.getFigure1().isNode() ? ((JdNode) r.getFigure1()).getLabel() : "";
					String expr = r.getRelationExpr(source);
					if (expr==null) { aExpr=null; break; }
					aExpr = (aExpr==null) ? expr : (aExpr + " * " + expr);
				}
				a.setExpr(aExpr);
				break;
			case JdNode.EXPR_GRAPH:
				if (v.size()>1) { // change to quantitative
					a.setExpr(null);
					a.setExprType(JdNode.EXPR_QUANTITATIVE);
				} else {
					JxmGraphExpr gExpr = new JxmGraphExpr(a.getExpr());
					String xname = "time";
					if (v.size() == 1) { // xvar = variable
						JdRelation r = v.elementAt(0);
						if (r.getFigure1() != null) {
							xname = r.getFigure1().isNode() ? ( (JdNode) r.getFigure1()).getLabel() : "time";
						}
					}
					gExpr.setNameVarX(xname);
					a.setExpr(gExpr.getGraphExpr());
				}
				break;
			}
		}
	}

	public String getFreeName(int t) {
		String s = "none";
		int n=1;
		switch (t) {
		case JdFigure.STOCK:
			while (hasObjectOfName("Stock_"+ n)) n++;
			s = "Stock_"+ n;
			break;
		case JdFigure.AUX:
			while (hasObjectOfName("Aux_"+ n)) n++;
			s = "Aux_"+ n;
			break;
		case JdFigure.CONSTANT:
			while (hasObjectOfName("Const_"+ n)) n++;
			s = "Const_"+ n;
			break;
		case JdFigure.DATASET:
			while (hasObjectOfName("Dataset_"+ n)) n++;
			s = "Dataset_"+ n;
			break;
		case JdFigure.FLOW:
			while (hasObjectOfName("Flow_"+ n)) n++;
			s = "Flow_"+ n;
			break;
		case JdFigure.RELATION:
			while (hasObjectOfName("Relation_"+ n)) n++;
			s = "Relation_"+ n;
			break;
		}
		return s;
	}
	//-------------------------------------------------------------------------
	public String getFreeName(String name) {
		if (!hasObjectOfName(name)) return name;
		String vname = name;
		int i = name.lastIndexOf('_');
		int n = 1;
		if (i>0) {
			int k = JxtTools.intValue(name.substring(i+1));
			if (k>0) { vname = name.substring(0,i); n=k; }
		}
		while (hasObjectOfName(vname + "_" + n)) n++;
		return (vname + "_" + n);
	}

	public void getXmModelObjects(JxmModel m, Hashtable<String,JdObject> hObj) {
		for (JdObject o : hObj.values()) {
			switch (o.getType()) {
			case JdFigure.STOCK:
				JdStock s = (JdStock) o;
				JxmSpecStock stock_spec = new JxmSpecStock(s.getLabel(),s.getExpr(),s.getUnit(),s.getInflow(),s.getOutflow());
				JxtVariable  stock_var  = new JxtVariable(s.getColor());
				stock_var.setDescriptor(new JxtIntID(s.getLabel()));
				stock_var.setColor(s.getLabelColor());
				JxmVarSpec stock_vs = new JxmVarSpec(s.getLabel(),stock_var,stock_spec);
				m.addVarSpec(stock_vs);
				m.addStock(new JxmStock(s.getLabel(),s.getPoint1(),s.getPoint2(),s.getLabelPosStr(),s.getColor(),s.getLabelColor(), s.getID()));
				break;
			case JdFigure.AUX:
				JdAux a = (JdAux) o;
				JxmSpecAux aux_spec = new JxmSpecAux(a.getLabel(),a.getExpr(),a.getUnit());
				switch (a.getExprType()) {
				case JdNode.EXPR_QUALITATIVE:
					if (bQualitative) aux_spec.setExprType(a.getExprType());
					break;
				default:
					aux_spec.setExprType(a.getExprType());
					break;
				}
				JxtVariable  aux_var  = new JxtVariable(a.getColor());
				aux_var.setDescriptor(new JxtIntID(a.getLabel()));
				aux_var.setColor(a.getLabelColor());
				JxmVarSpec var_vs = new JxmVarSpec(a.getLabel(),aux_var,aux_spec);
				m.addVarSpec(var_vs);
				m.addAux(new JxmAux(a.getLabel(),a.getPoint1(),a.getPoint2(),a.getLabelPosStr(),a.getColor(),a.getLabelColor(), a.getID()));
				break;
			case JdFigure.CONSTANT:
				JdConst c = (JdConst) o;
				JxmSpecConst const_spec = new JxmSpecConst(c.getLabel(),c.getExpr(),c.getUnit());
				JxtVariable  const_var  = new JxtVariable(c.getColor());
				const_var.setDescriptor(new JxtIntID(c.getLabel()));
				const_var.setColor(c.getLabelColor());
				JxmVarSpec const_vs = new JxmVarSpec(c.getLabel(),const_var,const_spec);
				m.addVarSpec(const_vs);
				m.addConst(new JxmConst(c.getLabel(),c.getPoint1(),c.getPoint2(),c.getLabelPosStr(),c.getColor(),c.getLabelColor(), c.getID()));
				break;
			case JdFigure.DATASET:
				JdDataset d = (JdDataset) o;
				JxmSpecDataset dataset_spec = new JxmSpecDataset(d.getLabel(),d.getUnit(), d.getDsName(), d.getDsInput(), d.getDsOutput(), d.getDsMethodI(), d.getDsMethodE());
				JxtVariable  dataset_var  = new JxtVariable(d.getColor());
				dataset_var.setDescriptor(new JxtIntID(d.getLabel()));
				dataset_var.setColor(d.getLabelColor());
				JxmVarSpec dataset_vs = new JxmVarSpec(d.getLabel(),dataset_var,dataset_spec);
				m.addVarSpec(dataset_vs);
				m.addDataset(new JxmDataset(d.getLabel(),d.getPoint1(),d.getPoint2(),d.getLabelPosStr(),d.getColor(),d.getLabelColor(), d.getID()));
				break;
			case JdFigure.FLOW:
				JdFlow aFlow = (JdFlow) o;
				String sf1=null,sf2=null;
				if (aFlow.getFigure1()!=null) sf1 = (aFlow.getFigure1().isObject()) ? ((JdObject) aFlow.getFigure1()).getLabel() : null;
				if (aFlow.getFigure2()!=null) sf2 = (aFlow.getFigure2().isObject()) ? ((JdObject) aFlow.getFigure2()).getLabel() : null;
				m.addFlow(new JxmFlow(aFlow.getLabel(), aFlow.getPoint1(), aFlow.getPoint2(), aFlow.getCtrlPoint1(), sf1, sf2, aFlow.getFlowTypeStr(),aFlow.getColor(), aFlow.getID()));
				break;
			case JdFigure.RELATION:
				JdRelation aRel = (JdRelation) o;
				String sr1=null,sr2=null;
				if (aRel.getFigure1()!=null) sr1 = (aRel.getFigure1().isObject()) ? ((JdObject) aRel.getFigure1()).getLabel() : ((JdSubObject) aRel.getFigure1()).getParent().getLabel();
				if (aRel.getFigure2()!=null) sr2 = (aRel.getFigure2().isObject()) ? ((JdObject) aRel.getFigure2()).getLabel() : ((JdSubObject) aRel.getFigure2()).getParent().getLabel();
				m.addRelation(new JxmRelation(aRel.getLabel(), aRel.getPoint1(), aRel.getPoint2(), aRel.getCtrlPoint1(), aRel.getCtrlPoint2(), sr1, sr2,aRel.getColor(),aRel.getRelationType(), aRel.getID()));
				break;
			}
		}
	}
	//-------------------------------------------------------------------------
	public JxmModel getXmModel() {
		updateStocksFlows();
		updateAuxExpressions();
		JxmModel m = new JxmModel(name);
		m.addDefaultSpecTime();
		JxmSpecTime tSpec = m.getSpecTime();
		tSpec.setStart(start);
		tSpec.setStop(stop);
		tSpec.setStep(step);
		tSpec.setMethod(method);
		getXmModelObjects(m,objs);
		return m;
	}
	//-------------------------------------------------------------------------
	public Hashtable<String,JdObject> getModelObjects(JxmModel m) {
		Hashtable<String,JdObject> h = new Hashtable<String,JdObject>();
		for (JxmStock stock : m.getStocks().values()) {
			JdStock aStock = new JdStock(stock.getSymbol(),stock.getPoint1(),stock.getPoint2());
			aStock.setColor(stock.getColor());
			aStock.setLabelPosStr(stock.getLabel());
			aStock.setLabelColor(stock.getLabelColor());
			aStock.setID(stock.getID());
			JxmSpecStock specstock = (JxmSpecStock) (m.getVarSpecs().get(stock.getSymbol())).getSpec();
			if (specstock!=null) {
				aStock.setExpr(specstock.getInitial());
				aStock.setUnit(specstock.getUnit());
			}
			h.put(aStock.getLabel(),aStock);
		}
		for (JxmAux aux : m.getAuxs().values()) {
			JdAux aAux = new JdAux(aux.getSymbol(),aux.getPoint1(),aux.getPoint2());
			aAux.setColor(aux.getColor());
			aAux.setLabelPosStr(aux.getLabel());
			aAux.setLabelColor(aux.getLabelColor());
			aAux.setID(aux.getID());
			JxmSpecAux specaux = (JxmSpecAux) (m.getVarSpecs().get(aux.getSymbol())).getSpec();
			if (specaux!=null) {
				aAux.setExpr(specaux.getExpr());
				aAux.setUnit(specaux.getUnit());
				switch (specaux.getExprType()) {
				case JxmSpec.EXPR_QUALITATIVE:
					if (bQualitative) aAux.setExprType(specaux.getExprType());
					break;
				default:
					aAux.setExprType(specaux.getExprType());
					break;
				}
			}
			h.put(aAux.getLabel(),aAux);
		}
		for (JxmConst con : m.getCons().values()) {
			JdConst aConst = new JdConst(con.getSymbol(),con.getPoint1(),con.getPoint2());
			aConst.setColor(con.getColor());
			aConst.setLabelPosStr(con.getLabel());
			//aConst.setLabelColor(aConst.getLabelColor());
			aConst.setLabelColor(con.getLabelColor());
			aConst.setID(con.getID());
			JxmSpecConst specconst = (JxmSpecConst) (m.getVarSpecs().get(con.getSymbol())).getSpec();
			if (specconst!=null) {
				aConst.setExpr(specconst.getExpr());
				aConst.setUnit(specconst.getUnit());
			}
			h.put(aConst.getLabel(),aConst);
		}
		for (JxmDataset dataset : m.getDatasets().values()) {
			JdDataset aDataset = new JdDataset(dataset.getSymbol(),dataset.getPoint1(),dataset.getPoint2());
			aDataset.setColor(dataset.getColor());
			aDataset.setLabelPosStr(dataset.getLabel());
			aDataset.setLabelColor(aDataset.getLabelColor());
			aDataset.setID(dataset.getID());

			JxmSpecDataset specdataset = (JxmSpecDataset) (m.getVarSpecs().get(dataset.getSymbol())).getSpec();
			if (specdataset!=null) {
				aDataset.setDsName(specdataset.getDsName());
				aDataset.setDsInput(specdataset.getDsInput());
				aDataset.setDsOutput(specdataset.getDsOutput());
				aDataset.setDsMethodI(specdataset.getDsMethodI());
				aDataset.setDsMethodE(specdataset.getDsMethodE());
				aDataset.setUnit(specdataset.getUnit());
			}
			h.put(aDataset.getLabel(),aDataset);
		}
		for (JxmFlow flow : m.getFlows().values()) {
			JdFlow aFlow = new JdFlow(flow.getSymbol());
			aFlow.setPoint1(flow.getPoint1());
			aFlow.setPoint2(flow.getPoint2());
			aFlow.setCtrlPoint1(flow.getCtrlPoint1());
			aFlow.setFlowTypeStr(flow.getType());
			aFlow.setFigure1(getObjectOfName(h,flow.getStart()));
			aFlow.setFigure2(getObjectOfName(h,flow.getEnd()));
			aFlow.setColor(flow.getColor());
			aFlow.setID(flow.getID());
			aFlow.updateSubobjects();
			h.put(aFlow.getLabel(),aFlow);
		}
		for (JxmRelation rel : m.getRelations().values()) {
			JdRelation aRel = new JdRelation(rel.getSymbol(),bQualitative);
			aRel.setPoint1(rel.getPoint1());
			aRel.setPoint2(rel.getPoint2());
			aRel.setCtrlPoint1(rel.getCtrlPoint1());
			aRel.setCtrlPoint2(rel.getCtrlPoint2());
			JdObject o1 = getObjectOfName(h,rel.getStart());
			if (o1!=null) { if (o1.isFlow()) aRel.setFigure1(((JdFlow) o1).getFlowCtrl()); else aRel.setFigure1(o1); }
			JdObject o2 = getObjectOfName(h,rel.getEnd());
			if (o2!=null) { if (o2.isFlow()) aRel.setFigure2(((JdFlow) o2).getFlowCtrl()); else aRel.setFigure2(o2); }
			aRel.setColor(rel.getColor());
			aRel.setID(rel.getID());
			if (bQualitative) aRel.setRelationType(rel.getType());
			aRel.updateHandles();
			h.put(aRel.getLabel(),aRel);
		}
		return h;
	}
	//-------------------------------------------------------------------------
	public void setXmModel(JxmModel m) {
		clearObjects();
		setDefaultTimes();
		if (m==null) { return; }
		setName(m.getSymbol()); // set model name
		Hashtable<String,JdObject> h = getModelObjects(m);
		// checks if ALL label colors of the nodes are the same
		boolean bNewLabelColor = true;
		Color aColor = null;
		for (JdObject o : h.values()) {
			if (aColor==null) aColor=o.getLabelColor();
			if (!o.getLabelColor().equals(aColor)) { bNewLabelColor=false; break; }
		}
		// add objects sorted by object name
		for (String objName : JTools.sortHashtableStringKeys(h))
			addObject(h.get(objName),bNewLabelColor);
		// simulation time spec
		JxmSpecTime tSpec = m.getSpecTime();
		start   = tSpec.getStart();
		stop    = tSpec.getStop();
		step    = tSpec.getStep();
		method  = tSpec.getMethod();
	}

	public JdFigure getSmallFigureAt(int x, int y) {
		JdFigure f, rf=null;
		for (JdObject obj : getObjects().values()) {
			f = obj.getFigureAt(x,y);
			if (f!=null) {
				if (rf!=null) {
					if (JdTools.isR1GreaterThanR2(rf.getBounds(),f.getBounds())) rf=f;
				} else
					rf = f;
			}
		}
		return rf;
	}

	public JdFigure getFigureAt(int x, int y, JdObject o) {
		if (o!=null) {
			for (JdObject mo : this.getObjects().values()) {
				JdFigure f = mo.getFigureAt(x,y);
				if (f!=null && !o.containsFigure(f)) return f;
			}
		}
		return null;
	}
}
