/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class MaterialSourceCopex extends MaterialSource{

    public MaterialSourceCopex() {
    }

    @Override
    public boolean isGeneralCopexMaterial() {
        return true;
    }

    @Override
    public Object clone() {
        try {
            MaterialSourceCopex mSource = (MaterialSourceCopex) super.clone() ;
            return mSource;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }


}
