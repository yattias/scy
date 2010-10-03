/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import java.awt.Image;

/**
 * image 
 * @author MBO
 */
public class CopexImage {
    // ATTRIBUTS
    /* nom */
    private String imgName;
    /* image */
    private Image img;

    // CONSTRUCTEURS
    public CopexImage(String imgName, Image img) {
        this.imgName = imgName;
        this.img = img;
    }

    
    // GETTER AND SETTER
    public Image getImg() {
        return img;
    }

    public String getImgName() {
        return imgName;
    }
    
    
}
