/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class MaterialSourceAction extends MaterialSource {
    private long idAction;

    public MaterialSourceAction(long idAction) {
        this.idAction = idAction;
    }

    @Override
    public boolean isMaterialProduce() {
        return true;
    }

    public long getIdAction() {
        return idAction;
    }

    public void setIdAction(long idAction) {
        this.idAction = idAction;
    }

    @Override
    public Object clone() {
        try {
            MaterialSourceAction mSource = (MaterialSourceAction) super.clone() ;
            mSource.setIdAction(new Long(this.idAction));
            return mSource;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

}
