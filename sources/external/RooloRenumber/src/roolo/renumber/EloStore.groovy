/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package roolo.renumber


/**
 *
 * @author sikken
 */
class EloStore {
	List<VersionList> versionLists = []

	String toString(){
		"EloStore"+
		versionLists.each { "$it" }
	}
	
	int getNumberOfElos(){
		int nrOfElos = 0;
		versionLists.each{ nrOfElos += it.elos.size()}
		nrOfElos
	}
}

