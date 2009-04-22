package eu.scy.colemo.server.uml;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.nov.2008
 * Time: 12:38:52
 * To change this template use File | Settings | File Templates.
 */
public class AbstractDiagramData implements Serializable {
    protected int x;
    protected int y;
    protected String name;

    protected String id;

    public AbstractDiagramData(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the y.
     */
    public int getY() {
        return y;
    }

    /**
	 * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the x.
     */
    public int getX() {
        return x;
    }

    /**
	 * @param x The x to set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
	 * @param y The y to set.
	 */
	public void setY(int y) {
		this.y = y;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
