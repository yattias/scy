package edu.scy.tools.math.test.data;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

import eu.scy.tools.math.doa.json.CircleToolbarShape;
import eu.scy.tools.math.doa.json.CylinderToolbarShape;
import eu.scy.tools.math.doa.json.ICircleToolbarShape;
import eu.scy.tools.math.doa.json.ICylinderToolbarShape;
import eu.scy.tools.math.doa.json.IMathToolbarShape;
import eu.scy.tools.math.doa.json.IRectanglarPrismToolbarShape;
import eu.scy.tools.math.doa.json.IRectangleToolbarShape;
import eu.scy.tools.math.doa.json.IToolbarShape;
import eu.scy.tools.math.doa.json.ITriangleToolbarShape;
import eu.scy.tools.math.doa.json.MathToolbarShape;
import eu.scy.tools.math.doa.json.RectanglarPrismToolbarShape;
import eu.scy.tools.math.doa.json.RectangleToolbarShape;
import eu.scy.tools.math.doa.json.SphereToolbarShape;
import eu.scy.tools.math.doa.json.ToolbarShape;
import eu.scy.tools.math.doa.json.TriangleToolbarShape;
import eu.scy.tools.math.ui.UIUtils;

public class JSONExample {

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ArrayList l = new ArrayList();
        
		ArrayList<IMathToolbarShape> prisms = new ArrayList<IMathToolbarShape>();
		ArrayList<IMathToolbarShape> spheres = new ArrayList<IMathToolbarShape>();
		ArrayList<IMathToolbarShape> clyinders = new ArrayList<IMathToolbarShape>();

		
		  IRectanglarPrismToolbarShape prism = new RectanglarPrismToolbarShape();
		  prism.setName(UIUtils.RECTANGLURAL_PRISM_NAME);
		  prism.setType(UIUtils.RECTANGLE3D);
		  prism.setSurfaceType(UIUtils._3D);
		  prism.setToolbarIcon("rectangle3d.png");
		  prism.setCanvasIcon("rectangle3dLarge.png");
		  prism.setVolume("100.0");
		  prism.setHeight("5.0");
		  prism.setWidth("5.0");
		  prism.setLength("4");
		  prism.setSurfaceArea("130");
		  prism.setSurfaceAreaMinValue("130");
		  prism.setSurfaceAreaMaxValue("130");
		  prism.setSurfaceAreaRatio("1.3");
		  prism.setSurfaceAreaRatioMinValue("1.3");
		  prism.setSurfaceAreaRatioMaxValue("1.3");
		  
		  prisms.add(prism);
		  
		  prism = new RectanglarPrismToolbarShape();
		  prism.setName(UIUtils.RECTANGLURAL_PRISM_NAME);
		  prism.setType(UIUtils.RECTANGLE3D);
		  prism.setSurfaceType(UIUtils._3D);
		  prism.setToolbarIcon("rectangle3dRed.png");
		  prism.setCanvasIcon("rectangle3dLargeRed.png");
		  prism.setVolume("200.0");
		  prism.setHeight("5.0");
		  prism.setWidth("5.0");
		  prism.setLength("8");
		  prism.setSurfaceArea("210");
		  prism.setSurfaceAreaMinValue("210");
		  prism.setSurfaceAreaMaxValue("210");
		  prism.setSurfaceAreaRatio("1.05");
		  prism.setSurfaceAreaRatioMinValue("1.05");
		  prism.setSurfaceAreaRatioMaxValue("1.05");
		  
		  prisms.add(prism);
		  
		  l.add(prisms);
		  
		  ICylinderToolbarShape cylinder = new CylinderToolbarShape();
		  cylinder.setName(UIUtils.CLYINDER_NAME);
		  cylinder.setType(UIUtils.CYLINDER3D);
		  cylinder.setSurfaceType(UIUtils._3D);
		  cylinder.setToolbarIcon("cylinder3d.png");
		  cylinder.setCanvasIcon("cylinder3dLarge.png");
		  cylinder.setVolume("100.0");
		  cylinder.setHeight("10");

		  cylinder.setRadiusMinValue("1.78");
		  cylinder.setRadiusMaxValue("1.79");
		  
		  
		  cylinder.setSurfaceArea("131.5");
		  cylinder.setSurfaceAreaMinValue("131.5");
		  cylinder.setSurfaceAreaMaxValue("132.5");
		  
		  cylinder.setSurfaceAreaRatio("1.31");
		  cylinder.setSurfaceAreaRatioMinValue("1.31");
		  cylinder.setSurfaceAreaRatioMaxValue("1.33");
		  
		  
		  clyinders.add(cylinder);
		  
		  cylinder = new CylinderToolbarShape();
		  cylinder.setName(UIUtils.CLYINDER_NAME);
		  cylinder.setType(UIUtils.CYLINDER3D);
		  cylinder.setSurfaceType(UIUtils._3D);
		  cylinder.setToolbarIcon("cylinder3dRed.png");
		  cylinder.setCanvasIcon("cylinder3dLargeRed.png");
		  cylinder.setVolume("200.0");
		  cylinder.setHeight("10");

		  cylinder.setRadius("2.52");
		  cylinder.setRadiusMinValue("2.52");
		  cylinder.setRadiusMaxValue("2.53");
		  
		  
		  cylinder.setSurfaceArea("198.0");
		  cylinder.setSurfaceAreaMinValue("198.0");
		  cylinder.setSurfaceAreaMaxValue("199.0");
		  
		  cylinder.setSurfaceAreaRatioMinValue("0.989");
		  cylinder.setSurfaceAreaRatioMaxValue("0.996");
		  
		  
		  clyinders.add(cylinder);
		  
		  l.add(clyinders);
		  
		  SphereToolbarShape sphere = new SphereToolbarShape();
		  sphere.setName(UIUtils.SPHERE_NAME);
		  sphere.setType(UIUtils.SPHERE3D);
		  sphere.setSurfaceType(UIUtils._3D);
		  sphere.setToolbarIcon("sphere3d.png");
		  sphere.setCanvasIcon("sphere3dLarge.png");
		  sphere.setVolume("100.0");

		  sphere.setRadius("2.78");
		  sphere.setRadiusMinValue("2.78");
		  sphere.setRadiusMaxValue("2.881");
		  
		  
		  sphere.setSurfaceArea("104.16");
		  sphere.setSurfaceAreaMinValue("104.16");
		  sphere.setSurfaceAreaMaxValue("104.20");
		  
		  sphere.setSurfaceAreaRatioMinValue("1.041");
		  sphere.setSurfaceAreaRatioMaxValue("1.042");

		  spheres.add(sphere);
		  
		  sphere = new SphereToolbarShape();
		  sphere.setName(UIUtils.SPHERE_NAME);
		  sphere.setType(UIUtils.SPHERE3D);
		  sphere.setSurfaceType(UIUtils._3D);
		  sphere.setToolbarIcon("sphere3dRed.png");
		  sphere.setCanvasIcon("sphere3dLargeRed.png");
		  sphere.setVolume("200.0");

		  sphere.setRadius("3.626");
		  sphere.setRadiusMinValue("3.626");
		  sphere.setRadiusMaxValue("3.631");
		  
		  
		  sphere.setSurfaceArea("165.2");
		  sphere.setSurfaceAreaMinValue("165.2");
		  sphere.setSurfaceAreaMaxValue("165.8");
		  
		  sphere.setSurfaceAreaRatioMinValue("0.82");
		  sphere.setSurfaceAreaRatioMaxValue("0.83");
		
		  spheres.add(sphere);
		  
		  l.add(spheres);
		  
		  ICircleToolbarShape circle = new CircleToolbarShape();
		  circle.setName(UIUtils.CIRCLE_NAME);
		  circle.setType(UIUtils.CIRCLE);
		  circle.setSurfaceType(UIUtils._2D);
		  circle.setToolbarIcon("circle.png");
		  circle.setCanvasIcon(null);
		  circle.setRadius("100");
		  
		  l.add(circle);
		  
		  ITriangleToolbarShape triangle = new TriangleToolbarShape();
		  triangle.setName(UIUtils.TRIANGLE_NAME);
		  triangle.setType(UIUtils.TRIANGLE);
		  triangle.setSurfaceType(UIUtils._2D);
		  triangle.setToolbarIcon("triangle.png");
		  triangle.setCanvasIcon(null);
		  triangle.setLength("200");
		  
		  l.add(triangle);
		  
		  IRectangleToolbarShape rect = new RectangleToolbarShape();
		  rect.setName(UIUtils.RECTANGLE_NAME);
		  rect.setType(UIUtils.RECTANGLE);
		  rect.setSurfaceType(UIUtils._2D);
		  rect.setToolbarIcon("rectangle.png");
		  rect.setCanvasIcon(null);
		  rect.setHeight("100");
		  rect.setWidth("100");
		  
		  l.add(rect);
		  
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("MathToolbarShape",
				MathToolbarShape.class);	        
		xstream.alias("CylinderToolbarShape", CylinderToolbarShape.class);
		xstream.alias("RectanglarPrismToolbarShape",
				RectanglarPrismToolbarShape.class);	        
		xstream.alias("CylinderToolbarShape", CylinderToolbarShape.class);
		xstream.alias("SphereToolbarShape", SphereToolbarShape.class);    
		xstream.alias("CircleToolbarShape", CircleToolbarShape.class);
		xstream.alias("TriangleToolbarShape", TriangleToolbarShape.class);
		xstream.alias("RectangleToolbarShape", RectangleToolbarShape.class);
		
		
	        System.out.println(xstream.toXML(l));
		
		List<IToolbarShape> shapeList = UIUtils.getShapeList();
		
		
	}
}
