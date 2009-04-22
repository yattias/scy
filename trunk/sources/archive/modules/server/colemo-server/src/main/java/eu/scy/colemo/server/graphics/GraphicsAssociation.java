/*
 * Created on 04.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import eu.scy.colemo.server.uml.UmlAssociation;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicsAssociation {

	private GraphicsClass from;
	private GraphicsClass to;
	private GraphicsDiagram diagram;
	private UmlAssociation link;
	private Color color;

	public GraphicsAssociation(UmlAssociation link, GraphicsDiagram diagram) {
		this.link=link;
		this.diagram=diagram;
		from = diagram.getClass(link.getFrom());
		to = diagram.getClass(link.getTo());
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d =(Graphics2D)g;
		g2d.setStroke(new BasicStroke());
		
		Point fromConnectioPoint=from.getConnectionPoint(findDirection(from.getCenterPoint(),to.getCenterPoint()));
		Point toConnectionPoint=to.getConnectionPoint(findDirection(to.getCenterPoint(),from.getCenterPoint()));
		g2d.drawLine((int)fromConnectioPoint.getX(),(int)fromConnectioPoint.getY(),(int)toConnectionPoint.getX(),(int)toConnectionPoint.getY());
	}
	
	public int findDirection(Point from, Point to) {
		double x= (to.getX()-from.getX());
		double y= (to.getY()-from.getY());
		
		if(Math.abs(x)>Math.abs(y)){
			if(from.getX()<to.getX()){
				return GraphicsClass.EAST;
			}
			else{
				return GraphicsClass.WEST;
			}
		}
		else{
			if(from.getY()>to.getY()){
				return GraphicsClass.NORTH;
			}
			else {
				return GraphicsClass.SOUTH;
			}	
		}	
	}
	
	public void setColor(Color color){
		this.color=color;
	}
	
	/**
	 * @return Returns the from.
	 */
	public GraphicsClass getFrom() {
		return from;
	}
	/**
	 * @return Returns the to.
	 */
	public GraphicsClass getTo() {
		return to;
	}
}
