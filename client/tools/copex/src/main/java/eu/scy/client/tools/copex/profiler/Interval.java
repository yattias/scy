/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.profiler;

/**
 *
 * @author MBO
 */
public class Interval {
    // ATTRIBUTS
    private long start;
    private long end;
    private String comment;
    
    //CONSTRUCTEURS
    public Interval(long st,long en) {
	start = st;
	end = en;
    }
    
    // METHODES
    public long getDuration() {
	return end-start;
    }

    public long getEnd() {
	return end;
    }

    public long getStart() {
	return start;
    }

    public Interval intersection(Interval inter) {
	long left;
	long right;
	Interval ret;

	left = Math.max(getStart(),inter.getStart());
	right = Math.min(getEnd(),inter.getEnd());

	ret = (left>=right)?null:new Interval(left,right);
	
	return ret;
    }

    public void setComment(String comm) {
	comment = comm;
    }
   
    @Override
    public String toString() {
	String ret = "\""+(start-Profiler.getReference())+" - "+(end-Profiler.getReference())+"\"";
	if(comment!=null) {
		ret+= " ( "+comment+" )";
	}
	return ret;
    }

}
