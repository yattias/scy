/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class MaterialSourceTeacher extends MaterialSource {

    private long idTeacher;

    public MaterialSourceTeacher(long idTeacher) {
        this.idTeacher = idTeacher;
    }

    @Override
    public boolean isTeacherMaterial() {
        return true;
    }

    public long getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(long idTeacher) {
        this.idTeacher = idTeacher;
    }

    @Override
    public Object clone() {
        try {
            MaterialSourceTeacher mSource = (MaterialSourceTeacher) super.clone() ;
            mSource.setIdTeacher(new Long(this.idTeacher));
            return mSource;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

}
