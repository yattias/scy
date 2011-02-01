/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.profiler;

import java.util.*;

/**
 *
 * Sequence d'intervalles de temps enregistrant l'activite d'un morceau de code.
 * @author MBO
 */
public class Profile {
    //ATTRIBUTS
    private String name;
    private Vector intervals = new Vector();
    private long currentStart=-1;
    
    // CONSTRUCTEURS
    public Profile(String name) {
	this.name = name;
    }
    
    //METHODES
    public void addInterval(Interval inter) {
	intervals.addElement(inter);
    }
    
    public void end() {
	if(currentStart!=-1) {
            intervals.addElement(new Interval(currentStart,System.currentTimeMillis()));
            currentStart=-1;
	} else {
            // System.out.println(""+getName()+" not started");		
	}
    }
    
    /**
    * Compare le nom seulement
    * @return boolean
    * @param o java.lang.Object
    */
    @Override
    public boolean equals(Object o) {
	return (o instanceof Profile) && (((Profile)o).getName().compareTo(getName())==0);
    }

    public Enumeration getIntervals() {
	return intervals.elements();
    }
    
    public long getLength() {
	return ((Interval)intervals.lastElement()).getEnd()-((Interval)intervals.firstElement()).getStart();
    }
    
    public long getMax() {
	long max = 0;
	long val = 0;
	Enumeration enumI = intervals.elements();
	while(enumI.hasMoreElements()) {
		val = ((Interval)enumI.nextElement()).getDuration();
		max = val>max?val:max;
	}
	
	return max;
    }
    
    public long getMin() {
	long min = 0;
	long val = 0;
	Enumeration enumI = intervals.elements();
	if(enumI.hasMoreElements()) {
		min = ((Interval)enumI.nextElement()).getDuration();
	}
	else {
            return 0;
	}
	
	while(enumI.hasMoreElements()) {
		val = ((Interval)enumI.nextElement()).getDuration();
		min = val<min?val:min;
	}
	
	return min;
    }
    
    public String getName() {
	return name;
    }
    
    /**
    * Affiche des statistiques.
    * @return java.lang.String
    */
    public String getStats() {
	String ret;
	if(currentStart!=-1) {
		ret = getName()+" not ended !!!";
	} else {
		ret = getName()+" : \tTotal time :"+ getLength()+"("+(((Interval)intervals.firstElement()).getStart()-Profiler.getReference())+" -> "+(((Interval)intervals.lastElement()).getEnd()-Profiler.getReference())+") \tRunning time : "+getSum()+" \tran "+intervals.size()+" times, \tMin : "+getMin()+" \tMed : "+(long)(getSum()/intervals.size())+" \tMax:"+getMax();
	}
	
	return ret;
    }
    
    public long getSum() {
	long sum = 0;
	Enumeration enumI = intervals.elements();
	while(enumI.hasMoreElements()){
		sum+= ((Interval)enumI.nextElement()).getDuration();
	}
	return sum;
    }
    
    /**
    * Realise l'intersection enttre deux profils.
    * @return profiler.Profile
    * @param profiler.Profile
    */
public Profile intersection(Profile pro) {

	Enumeration localEnum;
	Enumeration enumI;
	Interval interval,intersect;
	Profile ret;
	boolean notNull;

	if(pro == null) {
		return null;
	}

	enumI = pro.getIntervals(); 
	ret = new Profile(getName()+" and "+pro.getName());
	notNull = false;
	
	while(enumI.hasMoreElements()){
		interval= (Interval) enumI.nextElement();
		localEnum = intervals.elements();
		
		while(localEnum.hasMoreElements()) {
			
			intersect = interval.intersection((Interval)localEnum.nextElement());
			
			if(intersect!=null) {
				ret.addInterval(intersect);
				notNull=true;
			}
		}
	}

	if(!notNull) {
		ret=null;
	}
	
	return ret;
    }

    public void start() {
	if(currentStart!=-1)	{
		// System.out.println("An end must have been forgotten for "+getName());
	}
	
	currentStart = System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
	String ret=getName()+" : ";
	Enumeration enumI = intervals.elements();
	while(enumI.hasMoreElements()) {
		ret+=  ((Interval)enumI.nextElement())+" ";
	}

	return ret;
    }
}
