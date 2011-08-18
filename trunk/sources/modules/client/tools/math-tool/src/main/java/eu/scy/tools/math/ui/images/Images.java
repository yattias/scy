package eu.scy.tools.math.ui.images;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

public enum Images {

			
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// images
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	SketchUp("sketchup.png"), Circle("circle.png"), Rectangle("rectangle.png"), Triangle("triangle.png"), 
	Cube("cube.png"), Sphere("sphere.png"), Prism("prism.png"), Grid("grid.png"),Rectangle3d("rectangle3d.png"),Sphere3d("sphere3d.png"),
	Cylinder3d("cylinder3d.png"),Rectangle3dLarge("rectangle3dLarge.png"),Sphere3dLarge("sphere3dLarge.png"),Sphere_v100("sphere_v100.png"),Sphere_v200("sphere_v200.png"),
	Prism_v100_h10_v2("prism_v100_h10_v2.png"),Prism_v100_h5_v2("prism_v100_h5_v2.png"),Prism_v200_h10_v2("prism_v200_h10_v2.png"),Prism_v200_h5_v2("prism_v200_h5_v2.png"),
	Cylinder3dLarge("cylinder3dLarge.png"),Trash("trash.png"), Cylinder_v100_h5_v2("cylinder_v100_h5_v2.png"),Cylinder_v100_h10_v2("cylinder_v100_h10_v2.png"),Cylinder_v200_h5_v2("Cylinder_v200_h5_v2.png"),Cylinder_v200_h10_v2("cylinder_v200_h10_v2.png");
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// data
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	String imagefilename;
	String imageName;
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// constructor
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	Images(String fileName) {
		imagefilename = fileName;
		imageName = StringUtils.upperCase(StringUtils.remove(imagefilename, ".png"));
	}
	

	public String getName() {
		return imageName;
	}
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// methods
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	public BufferedImage getImage() {
		try {
			return ImageIO.read(Images.class.getResource(imagefilename));
		} catch (IOException e) {
			return null;
		}
	}

	public static Icon getIcon(String iconName) {
		try {
			return new ImageIcon(ImageIO.read(Images.class.getResource(iconName)));
		} catch (IOException e) {
			return null;
		}
	}

	public Icon getIcon() {
		return  new ImageIcon(getImage());
	}

	public BufferedImage getImage(int width, int height) {
		return GraphicsUtilities.createThumbnail(getImage(), width, height);
	}

	public Icon getIcon(int width, int height) {
		return new ImageIcon(getImage(width, height));
	}

}// end enum Images

