/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package roolo.renumber


/**
 *
 * @author sikken
 */
class VersionList implements Comparable<VersionList> {
	int id
	List<Elo> elos = []

	String toString(){
		"id= $id, " +
		elos.each { it } + "\n"
	}
	
	int compareTo(VersionList versionList){
		if (versionList==null){
			return 1
		}
		return id - versionList.id
	}
}

