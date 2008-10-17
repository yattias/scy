package eu.scy.pages;

import eu.scy.pages.TapestryContextAware;

import java.util.Date;

/**
 * Start page of application scy-useradmin-web.
 */
public class Index  extends TapestryContextAware {
	public Date getCurrentTime() 
	{ 
		return new Date(); 
	}
}
