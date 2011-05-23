package eu.scy.client.tools.scydynamics.editor;

import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdLink;
import colab.um.draw.JdNode;
import colab.um.draw.JdObject;
import colab.um.draw.JdRelation;
import colab.um.draw.JdSubObject;
import colab.um.parser.JParser;
import colab.um.xml.model.JxmModel;
import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Vector;

import eu.scy.client.tools.scydynamics.model.Model;

public class ModelSelection {
  private int old_x, old_y;
  private Hashtable<String,JdObject> sObj; // table of selected objects
  private Hashtable<String,JdObject> cObj; // copy/paste/cut objects
  private JxmModel xmUndo;
  private boolean bSaveFirstModel;
  
  public ModelSelection() { 
    sObj = new Hashtable<String,JdObject>(); // table of selected objects
    cObj = new Hashtable<String,JdObject>(); // copy/paste/cut objects
    xmUndo = null;
    bSaveFirstModel = true;
  }
  
  public void clear()            { sObj.clear(); }
  public void add(JdObject o)    { sObj.put(o.getLabel(),o); }
  public void remove(JdObject o) { sObj.remove(o.getLabel()); }
  public Hashtable<String,JdObject> getObjects() { return sObj; }
  public int getObjectSize()    { return sObj.size(); }
  
  public boolean containsObj(JdObject o) {
    if (o==null) return false;
    return sObj.containsKey(o.getLabel());
  }

  public boolean containsFig(JdFigure f) {
    String id = "";
    if (f==null) return false;
    if  (f.isObject())        id = ((JdObject) f).getLabel();
    else if (f.isSubObject()) id = ((JdSubObject) f).getParent().getLabel();
    return sObj.containsKey(id);
  }
  //-------------------------------------------------------------------------
  // rename object selection
  //---------------------------------------------------------------------------
  public void renameObjectKey(String oldName, String newName) {
    JdObject aObj = sObj.get(oldName);
    if (aObj!=null) {
      sObj.remove(oldName);
      sObj.put(newName, aObj);
      }
  }
  //-------------------------------------------------------------------------
  // object selection
  //---------------------------------------------------------------------------
  public void unselectAll() {
    for (JdObject o : sObj.values()) { o.setEnhanced(false); }
    clear();
  }
  //---------------------------------------------------------------------------
  public void setEnhanced(boolean b) {
    for (JdObject o : sObj.values()) { o.setEnhanced(b); }
  }
  //---------------------------------------------------------------------------
  public void selectObject(JdObject o, boolean bToggle) {
    if (bToggle) { // toggle selected?
      if (containsObj(o)) {
        o.setEnhanced(false);
        remove(o);
      } else {
        o.setEnhanced(true);
        add(o);
      }
    } else {
      if (!containsObj(o)) {
        unselectAll();
        o.setEnhanced(true);
        add(o);
        }
      }
  }
  //-------------------------------------------------------------------------
  // move selection
  //-------------------------------------------------------------------------
  public void setStart(int x, int y) { old_x=x; old_y=y; }
  //-------------------------------------------------------------------------
  // move selected objects
  //-------------------------------------------------------------------------
  public void updateLinks(JdObject o, Vector<JdLink> vLinks) {
    for (JdLink l : vLinks) {
      JdFigure f1 = l.getFigure1();
      JdFigure f2 = l.getFigure2();
      if (o.isNode()) { // node is moved -> test links connected to it
        if (!containsFig(l)) { // link is not selected
          if (o.containsFigure(f1)) { l.updatePoints(f1); if (l.isFlow()) updateLinks(l,vLinks); }
          if (o.containsFigure(f2)) { l.updatePoints(f2); if (l.isFlow()) updateLinks(l,vLinks); }
          }
      } else if (o.isRelation()) { // relation
        if (f1!=null && !containsFig(f1)) ((JdLink) o).updatePoints(f1);
        if (f2!=null && !containsFig(f2)) ((JdLink) o).updatePoints(f2);
      } else if (o.isFlow()) { // flow
        if (!containsFig(l)) {
          if (o.containsFigure(f1)) { l.updatePoints(f1); }
          if (o.containsFigure(f2)) { l.updatePoints(f2); }
          }
        }
      }
  }
  //-------------------------------------------------------------------------
  public void move(int x, int y, Vector<JdLink> vLinks) {
    int dx = x - old_x;
    int dy = y - old_y;
    for (JdObject o : sObj.values()) {
      o.translate(dx,dy);
      if (o.isFlow()) {
        JdFlow f = (JdFlow) o;
        JdFigure f1 = f.getFigure1();
        JdFigure f2 = f.getFigure2();
        if (!containsFig(f1)) f.updatePoints(f1);
        if (!containsFig(f2)) f.updatePoints(f2);
        }
      updateLinks(o,vLinks);
      }
    old_x = x;
    old_y = y;
  }
  //-------------------------------------------------------------------------
  public void translate(int x, int y, Vector<JdLink> vLinks) {
    int dx = x - old_x;
    int dy = y - old_y;
    for (JdObject o : sObj.values()) {
      if (o.isNode()) {
        o.translate(dx, dy);
        updateLinks(o,vLinks);
        }
      }
    for (JdObject o : sObj.values()) {
      if (o.isFlow()) {
        o.translate(dx, dy);
        JdFlow f = (JdFlow) o;
        JdFigure f1 = f.getFigure1();
        JdFigure f2 = f.getFigure2();
        if (!containsFig(f1)) f.updatePoints(f1);
        if (!containsFig(f2)) f.updatePoints(f2);
        updateLinks(o,vLinks);
        }
      }
    for (JdObject o : sObj.values()) {
      if (o.isRelation()) {
        o.translate(dx, dy);
        updateLinks(o,vLinks);
        }
      }
    old_x = x;
    old_y = y;
  }
  //-------------------------------------------------------------------------
  // undo/copy/paste/cut selection
  //-------------------------------------------------------------------------
  public Hashtable<String,JdObject> getCopySelection() { return cObj; }
  //-------------------------------------------------------------------------
  public void setCopySelection(Vector<JdObject> v) {
    cObj.clear();
    for (JdObject o : v) {
      cObj.put(o.getLabel(),(JdObject) o.clone());
      }
  }
  //-------------------------------------------------------------------------
  public Rectangle getSelectionBounds() {
    if (sObj.size()<1) return null;
    Rectangle r = null;
    for (JdObject o : sObj.values()) {
      if (r==null) r = new Rectangle(o.getBounds());
      else r.add(o.getBounds());
      }
    return r;
  }
  //-------------------------------------------------------------------------
  public void copySelection() {
    cObj.clear();
    for (JdObject o : sObj.values()) {
      cObj.put(o.getLabel(),(JdObject) o.clone());
      }
  }
  //-------------------------------------------------------------------------
  private void renamePasteExpressions(Hashtable<String,JdObject> hObj, Hashtable<String,String> hNames) {
    for (String oldName : hNames.keySet()) {
      String newName = hNames.get(oldName);
      if (!oldName.equals(newName)) {
        for (JdObject o : hObj.values()) {        
          if (o.isObject()) {
            String expr = o.getExpr();
            if (expr != null && expr.length() > 0) // change name in other object expressions
              o.setExpr(JParser.replaceToken(expr, oldName, newName));
            }
          }
        }
      }
  }
  //-------------------------------------------------------------------------
  private void addObject(Hashtable<String,String> hNames, JdObject o, Model aModel) {
    o.translate(20, 20);
    String s = aModel.getFreeName(o.getLabel());
    hNames.put(o.getLabel(),s);
    o.setLabel(s);
    aModel.addObject(o,true);
    selectObject(o,true);
  }
  //-------------------------------------------------------------------------
  public void pasteSelection(ModelEditor modelEditor, Model aModel) {
    if (cObj.size()<1) return;
    saveModel(modelEditor);
    Hashtable<String,String> hNames = new Hashtable<String,String>();
    unselectAll();
    // 1. nodes
    for (JdObject o : cObj.values()) {
      if (o.isNode()) addObject(hNames,o,aModel);
      }
    // 2. flows
    for (JdObject o : cObj.values()) {
      if (o.isFlow()) {
        JdFlow f = (JdFlow) o;
        // update flow figures
        if (f.getFigure1()!=null) {
          JdNode n = (JdNode) f.getFigure1();
          f.setFigure1( cObj.containsKey(n.getLabel()) ? cObj.get(n.getLabel()) : null );
          }
        if (f.getFigure2()!=null) {
          JdNode n = (JdNode) f.getFigure2();
          f.setFigure2( cObj.containsKey(n.getLabel()) ? cObj.get(n.getLabel()) : null );
          }
        addObject(hNames,o,aModel);
        }
      }
    // 3. relations
    for (JdObject o : cObj.values()) {
      if (o.isRelation()) {
        JdRelation r = (JdRelation) o;
        if (r.getFigure1().isNode() && r.getFigure2().isNode()) {
          JdNode n1 = (JdNode) r.getFigure1();
          JdNode n2 = (JdNode) r.getFigure2();
          JdObject o1 = cObj.get(n1.getLabel());
          JdObject o2 = cObj.get(n2.getLabel());
          if (o1!=null && o2!=null) {
            r.setFigure1(o1);
            r.setFigure2(o2);
            addObject(hNames,o,aModel);
            }
        } else if (r.getFigure2().isFlowCtr()) {
          JdNode n1 = (JdNode) r.getFigure1();
          JdObject o1 = cObj.get(n1.getLabel());
          JdFlowCtr ctr = (JdFlowCtr) r.getFigure2();
          JdObject f = ctr.getParent();
          JdFlow p = (JdFlow) cObj.get(f.getLabel());
          if (p!=null) {
            r.setFigure2(p.getFlowCtrl());
            r.setFigure1(o1);
            addObject(hNames,o,aModel);
            }
          }
        }
      }
    renamePasteExpressions(cObj,hNames);
    //modelEditor.updateSpecDialog(true);
    modelEditor.setModelChanged();
    modelEditor.checkModel();
    modelEditor.updateCanvas();
    copySelection();
  }
  //-------------------------------------------------------------------------
  public void cutSelection(ModelEditor ed) {
    copySelection();
    deleteSelection(ed);
  }
  //-------------------------------------------------------------------------
  public void deleteSelection(ModelEditor ed) {
    if (sObj.size()<1) return;
    saveModel(ed);
    Vector<String> v = new Vector<String>();
    for (String objName : sObj.keySet()) {
      v.add(objName);
      }
    for (String objName : v) {
      ed.deleteFigure(objName,false);
    }
  }
  //-------------------------------------------------------------------------
  // undo actions
  //-------------------------------------------------------------------------
  public JxmModel getUndoModel()           { return xmUndo; }
  public void     setUndoModel(JxmModel m) { xmUndo = m; }
  //-------------------------------------------------------------------------
//  public void undoModel(ModelEditor modelEditor) {
//    if (xmUndo!=null) {
//      modelEditor.setXmModel(xmUndo);
//      xmUndo = null;
//      bSaveFirstModel = true;
//      modelEditor.enableUndoButton(false);
//      }
//  }
  //-------------------------------------------------------------------------
  public void saveModel(ModelEditor modelEditor) {
    xmUndo = modelEditor.getModel().getXmModel();
    bSaveFirstModel = false;
    //modelEditor.enableUndoButton(true);
  }
  //-------------------------------------------------------------------------
  public void clearSaveFirstModel() {
    bSaveFirstModel = true;
  }
  //-------------------------------------------------------------------------
  public void saveFirstModel(ModelEditor modelEditor) {
    if (bSaveFirstModel) {
      xmUndo = modelEditor.getModel().getXmModel();
      bSaveFirstModel = false;
      //modelEditor.enableUndoButton(true);
      }
  }
  //-------------------------------------------------------------------------
  public void clearUndoModel() {
    xmUndo = null;
    bSaveFirstModel = true;
  }
  //-------------------------------------------------------------------------
}
