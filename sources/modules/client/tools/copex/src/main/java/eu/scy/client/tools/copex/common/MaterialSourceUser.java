/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class MaterialSourceUser extends MaterialSource{

    public MaterialSourceUser() {
    }

    @Override
    public boolean isUserMaterial() {
        return true;
    }

    @Override
    public Object clone() {
        try {
            MaterialSourceUser mSource = (MaterialSourceUser) super.clone() ;
            return mSource;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }


}
