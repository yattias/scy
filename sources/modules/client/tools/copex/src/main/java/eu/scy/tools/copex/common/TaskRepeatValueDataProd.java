/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 *
 * @author Marjolaine
 */
public class TaskRepeatValueDataProd extends TaskRepeatValue{
    /* data prod */
    private QData data;

    public TaskRepeatValueDataProd(long dbKey, int noRepeat, QData data) {
        super(dbKey, noRepeat);
        this.data = data;
    }

    public QData getData() {
        return data;
    }

    public void setData(QData data) {
        this.data = data;
    }
    // OVERRIDE
    @Override
    public Object clone() {
        TaskRepeatValueDataProd v = (TaskRepeatValueDataProd) super.clone() ;

        v.setData((QData)this.data.clone());
        return v;
    }
}
