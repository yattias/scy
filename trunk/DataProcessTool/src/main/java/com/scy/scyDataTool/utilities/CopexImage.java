/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.utilities;

import javax.swing.ImageIcon;

/**
 * image 
 * @author MBO
 */
public class CopexImage {
    // ATTRIBUTS
    /* nom */
    private String imgName;
    /* image */
    private ImageIcon img;

    // CONSTRUCTEURS
    public CopexImage(String imgName, ImageIcon img) {
        this.imgName = imgName;
        this.img = img;
    }

    
    // GETTER AND SETTER
    public ImageIcon getImg() {
        return img;
    }

    public String getImgName() {
        return imgName;
    }
    
    
}
