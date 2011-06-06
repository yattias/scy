/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;


import org.jdom.Element;

/**
 * visualization of  data, can be a SimpleVisualization (pie or bar chart) or a graph
 * @author Marjolaine Bodin
 */
public class Visualization implements Cloneable {
    private final static String TAG_VISUALIZATION = "visualization";
    private final static String TAG_VIS_TYPE = "type";
    

    /* db key identifier*/
    protected long dbKey;
    /* name */
    protected String name;
    /* visualization type */
    protected TypeVisualization type;
    /* visualization open in fitex / labbook */
    protected boolean isOpen;

    public Visualization(long dbKey,String name, TypeVisualization type ) {
        this.dbKey = dbKey;
        this.name = name;
        this.type = type;
        this.isOpen = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    

    public TypeVisualization getType() {
        return type;
    }

    public void setType(TypeVisualization type) {
        this.type = type;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    
     @Override
    public Object clone()  {
        try {
            Visualization vis = (Visualization) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            TypeVisualization typeC = (TypeVisualization)this.type.clone();
            vis.setDbKey(dbKeyC);
            vis.setName(nameC);
            vis.setType(typeC);
            vis.setOpen(isOpen);
            return vis;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

     /* returns true if the visualization is on the specified column index  */
     public boolean isOnNo(int no){
         return false;
     }

    

    public Element toXMLLog(){
        Element element = new Element(TAG_VISUALIZATION);
        element.addContent(new Element(TAG_VIS_TYPE).setText(type.getName()));
        return element;
    }
    
}
