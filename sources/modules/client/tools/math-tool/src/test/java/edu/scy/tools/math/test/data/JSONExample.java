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
		prism.setName("Prism v100 h5");
		prism.setType(UIUtils.RECTANGLE3D);
		prism.setSurfaceType(UIUtils._3D);
		prism.setToolbarIcon("rectangle3d.png");
		prism.setCanvasIcon("prism_v100_h5_v2.png");
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
		prism.setName("Prism v100 h10");
		prism.setType(UIUtils.RECTANGLE3D);
		prism.setSurfaceType(UIUtils._3D);
		prism.setToolbarIcon("rectangle3d.png");
		prism.setCanvasIcon("prism_v100_h10_v2.png");
		prism.setVolume("100.0");
		prism.setHeight("10.0");
		prism.setWidth("5.0");
		prism.setLength("2");
		prism.setSurfaceArea("160");
		prism.setSurfaceAreaMinValue("160");
		prism.setSurfaceAreaMaxValue("160");
		prism.setSurfaceAreaRatio("1.6");
		prism.setSurfaceAreaRatioMinValue("1.6");
		prism.setSurfaceAreaRatioMaxValue("1.6");

		prisms.add(prism);

		prism = new RectanglarPrismToolbarShape();
		prism.setName("Prism v200 h5");
		prism.setType(UIUtils.RECTANGLE3D);
		prism.setSurfaceType(UIUtils._3D);
		prism.setToolbarIcon("rectangle3d.png");
		prism.setCanvasIcon("prism_v200_h5_v2.png");
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

		prism = new RectanglarPrismToolbarShape();
		prism.setName("Prism v200 h10");
		prism.setType(UIUtils.RECTANGLE3D);
		prism.setSurfaceType(UIUtils._3D);
		prism.setToolbarIcon("rectangle3d.png");
		prism.setCanvasIcon("prism_v200_h10_v2.png");
		prism.setVolume("200.0");
		prism.setHeight("10.0");
		prism.setWidth("5.0");
		prism.setLength("4");
		prism.setSurfaceArea("220");
		prism.setSurfaceAreaMinValue("220");
		prism.setSurfaceAreaMaxValue("220");
		prism.setSurfaceAreaRatio("1.10");
		prism.setSurfaceAreaRatioMinValue("1.10");
		prism.setSurfaceAreaRatioMaxValue("1.10");

		prisms.add(prism);

		l.add(prisms);

		ICylinderToolbarShape cylinder = new CylinderToolbarShape();
		cylinder.setName("Cylinder v100 h5 v2");
		cylinder.setType(UIUtils.CYLINDER3D);
		cylinder.setSurfaceType(UIUtils._3D);
		cylinder.setToolbarIcon("cylinder3d.png");
		cylinder.setCanvasIcon("cylinder_v100_h5_v2.png");
		cylinder.setVolume("100.0");
		cylinder.setHeight("5");

		cylinder.setRadiusMinValue("2.52");
		cylinder.setRadiusMaxValue("2.52");

		cylinder.setSurfaceArea("119.50");
		cylinder.setSurfaceAreaMinValue("119.49");
		cylinder.setSurfaceAreaMaxValue("119.51");

		cylinder.setSurfaceAreaRatio("1.19");
		cylinder.setSurfaceAreaRatioMinValue("1.18");
		cylinder.setSurfaceAreaRatioMaxValue("1.20");

		clyinders.add(cylinder);

		cylinder = new CylinderToolbarShape();
		cylinder.setName("Cylinder v100 h10 v2");
		cylinder.setType(UIUtils.CYLINDER3D);
		cylinder.setSurfaceType(UIUtils._3D);
		cylinder.setToolbarIcon("cylinder3d.png");
		cylinder.setCanvasIcon("cylinder_v100_h10_v2.png");
		cylinder.setVolume("100.0");
		cylinder.setHeight("10");

		cylinder.setRadius("1.78");
		cylinder.setRadiusMinValue("1.77");
		cylinder.setRadiusMaxValue("1.79");

		cylinder.setSurfaceArea("132.10");
		cylinder.setSurfaceAreaMinValue("132.09");
		cylinder.setSurfaceAreaMaxValue("132.11");

		cylinder.setSurfaceAreaRatioMinValue("1.31");
		cylinder.setSurfaceAreaRatioMaxValue("1.33");

		clyinders.add(cylinder);

		cylinder = new CylinderToolbarShape();
		cylinder.setName("Cylinder v200 h5 v2");
		cylinder.setType(UIUtils.CYLINDER3D);
		cylinder.setSurfaceType(UIUtils._3D);
		cylinder.setToolbarIcon("cylinder3d.png");
		cylinder.setCanvasIcon("cylinder_v200_h5_v2.png");
		cylinder.setVolume("200.0");
		cylinder.setHeight("5");

		cylinder.setRadius("3.56");
		cylinder.setRadiusMinValue("3.55");
		cylinder.setRadiusMaxValue("3.57");

		cylinder.setSurfaceArea("192.10");
		cylinder.setSurfaceAreaMinValue("192.09");
		cylinder.setSurfaceAreaMaxValue("192.11");

		cylinder.setSurfaceAreaRatioMinValue("0.95");
		cylinder.setSurfaceAreaRatioMaxValue("0.97");

		clyinders.add(cylinder);

		cylinder = new CylinderToolbarShape();
		cylinder.setName("Cylinder v200 h10 v2");
		cylinder.setType(UIUtils.CYLINDER3D);
		cylinder.setSurfaceType(UIUtils._3D);
		cylinder.setToolbarIcon("cylinder3d.png");
		cylinder.setCanvasIcon("cylinder_v200_h10_v2.png");
		cylinder.setVolume("200.0");
		cylinder.setHeight("10");

		cylinder.setRadius("2.52");
		cylinder.setRadiusMinValue("2.51");
		cylinder.setRadiusMaxValue("2.53");

		cylinder.setSurfaceArea("198.53");
		cylinder.setSurfaceAreaMinValue("198.52");
		cylinder.setSurfaceAreaMaxValue("198.54");

		cylinder.setSurfaceAreaRatioMinValue("0.97");
		cylinder.setSurfaceAreaRatioMaxValue("0.99");

		clyinders.add(cylinder);

		l.add(clyinders);

		SphereToolbarShape sphere = new SphereToolbarShape();
		sphere.setName("Sphere v100");
		sphere.setType(UIUtils.SPHERE3D);
		sphere.setSurfaceType(UIUtils._3D);
		sphere.setToolbarIcon("sphere3d.png");
		sphere.setCanvasIcon("sphere_v100.png");
		sphere.setVolume("100.0");

		sphere.setRadius("2.78");
		sphere.setRadiusMinValue("2.77");
		sphere.setRadiusMaxValue("2.79");

		sphere.setSurfaceArea("104.16");
		sphere.setSurfaceAreaMinValue("104.15");
		sphere.setSurfaceAreaMaxValue("104.17");

		sphere.setSurfaceAreaRatioMinValue("1.03");
		sphere.setSurfaceAreaRatioMaxValue("1.05");

		spheres.add(sphere);

		sphere = new SphereToolbarShape();
		sphere.setName("Sphere v200");
		sphere.setType(UIUtils.SPHERE3D);
		sphere.setSurfaceType(UIUtils._3D);
		sphere.setToolbarIcon("sphere3d.png");
		sphere.setCanvasIcon("sphere_v200.png");
		sphere.setVolume("200.0");

		sphere.setRadius("3.62");
		sphere.setRadiusMinValue("3.61");
		sphere.setRadiusMaxValue("3.63");

		sphere.setSurfaceArea("165.38");
		sphere.setSurfaceAreaMinValue("165.37");
		sphere.setSurfaceAreaMaxValue("165.39");

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
		xstream.alias("MathToolbarShape", MathToolbarShape.class);
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
