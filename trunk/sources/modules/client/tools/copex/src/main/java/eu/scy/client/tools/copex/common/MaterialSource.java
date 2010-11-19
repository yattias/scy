/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public abstract class MaterialSource implements Cloneable {
    public boolean isGeneralCopexMaterial(){
        return false;
    }

    public boolean isTeacherMaterial(){
        return false;
    }

    public boolean isUserMaterial(){
        return false;
    }

    public boolean isMaterialProduce(){
        return false;
    }

}
