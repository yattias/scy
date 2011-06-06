/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.profiler;

import java.util.*;

/**
 * Outil de profiling.
 * 
 * Permet d'enregistrer le temps d'execution du code et de realiser des stats basiques
 * Fonctionnement : inserer des appels a Profiler.start("nom de la methode") et Profiler.end("nom de la methode") 
 * dans une methode en oubliant pas de mettre un end par return;
 * L'appel a la fonction start declenche l'enregistrement
 * Ensuite la methode getStats() permet d'afficher l'ensemble des statistiques recoltees.
 *
 * Attention, les mesures etant fondee sur le temps systeme, l'environnement doit etre le meme si l'on cherche a comparer deux executions successives
 *
 * Attention aussi aux methodes recursives, il est conseille d'inclure par exemple le niveau de recursivite dans le nom afin de ne pas avoir par exemple deux "start" se suivant
 *
 * Voir la methode main pour un exemple d'utilisation
 * @author MBO
 */
public class Profiler {
    // ATTRIBUTS
    private static Vector profiles = new Vector();
    private static long reference=0;
    private static boolean started=false;
    
    // METHODES
    /**
    * Retourne la description de l'ensemble des appels sous forme de chaane de carracteres
    * @return java.lang.String
    */
    public static String display() {
	String ret="";
	Enumeration enumProfiles = profiles.elements();
	while(enumProfiles.hasMoreElements()) {
		ret+=  ((Profile)enumProfiles.nextElement())+"\n";
	}
	return ret;
    }
    
    /**
    * Arrete le chronometrage d'une methode
    */
    public static void end(String name) {
	if(started) {
		int index = profiles.indexOf(new Profile(name));
		if(index != -1) {
			((Profile)profiles.elementAt(index)).end();
		}
	}
    }
    
    /**
    * Retourne un profil suivant son nom
    * @return profiler.Profile
    * @param name java.lang.String
    */
    public static Profile getProfile(String name) {
	Profile ret=null;
	int index = profiles.indexOf(new Profile(name));
	if(index != -1) {
		ret=(Profile)profiles.elementAt(index);
	}
	
	return ret;
    }
    /**
    * Retourne le temps de reference (temps systeme au start)
    * @return long
    */
    public static long getReference() {
	return reference;
    }
    
    /**
    * Affiche les statistiques des differents profils.
    * @return java.lang.String
    */
    public static String getStats() {
	String ret="";
	Enumeration enumProfiles = profiles.elements();
	while(enumProfiles.hasMoreElements()) {
		ret+=  ((Profile)enumProfiles.nextElement()).getStats()+"\n";
	}
	return ret;
    }
    
    /**
    * Intersection entre deux profils
    * @return profiler.Profile
    * @param name1 java.lang.String
    * @param name2 java.lang.String
    */
    public static Profile intersection(String name1, String name2) {
	Profile prof1 = getProfile(name1);
	Profile prof2 = getProfile(name2);
	if((prof1!=null)&&(prof2!=null)){
		return prof1.intersection(prof2);
	}else{
		return null;
	}
    }
    
    /**
    * Vide le profiler (ne pas oublier avant une seconde utilisation).
    */
    public static void reset() {
	profiles = new Vector();
	started = false;
    }
    
    /**
    * Demarre le profiler (temps de reference).
    */
    public static void start() {
	started = true;
	profiles = new Vector();
	reference = System.currentTimeMillis();
    }
    
    /**
    * Demarre l'enregistrement d'un profil, l'ajoute s'il n'existe pas.
    * @param num int
    */
    public static void start(String name) {
	if(started){
		int index = profiles.indexOf(new Profile(name));
		if(index != -1) {
			((Profile)profiles.elementAt(index)).start();
		}else {
			profiles.addElement(new Profile(name));
			start(name);
		}
	}
    }

    /**
    * Exemple d'utilisation de Profiler
    * Creation date: (09/07/2003 17:02:46)
 * @param args java.lang.String[]
    */
public static void main(String[] args) {
	
	start();
	start("main");
	start("Boucle 1");
	for(int j=0;j<10;j++) {
		start("Boucle 2");
		for(int k=0;k<=1000000;k++) {
				continue;
	}
		end("Boucle 2");
	}
	end("Boucle 1");
		
	for(int j=0;j<10;j++) {
		start("Boucle 2");
		for(int k=0;k<=1000000;k++) {
                    continue;
		}
		end("Boucle 2");
	}
			
	end("main");
	

	// System.out.println("Resultat :\n"+display());
	// System.out.println("\nStats  :\n"+getStats());
	// System.out.println("Intersection 1 :\n"+getProfile("main").intersection(getProfile("Boucle 2")).getStats());
	// System.out.println("Intersection 2 :\n"+getProfile("Boucle 1").intersection(getProfile("Boucle 2")).getStats());
	reset();
	start();
	start("test");
	end("test");
	// System.out.println("\nStats  :\n"+getStats());
    }
}
