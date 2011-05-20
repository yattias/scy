/*
 * Parametre.java
 * Created on 28 mai 2007, 13:25
 */

package eu.scy.client.tools.fitex.dataStruct;

import eu.scy.client.tools.fitex.analyseFn.Function;

/**
 *
 * @author Cedric
 */
public class Parametre implements Expression {
    
    String name;
    double valeur ;
    
    /** Creates a new instance of Constante */
    public Parametre(String name, Function fonction)
    {
        this.name = name ;
        fonction.ajouterParametre(name, this) ;
    }
    
    public void setValeur(double val)
    {
        valeur = val ;
    }
    
    @Override
    public double valeur(double x)
    {
        return valeur ;
    } 

    public double valeur()
    {
        return valeur ;
    } 
    
    @Override
    public String toString()
    {
        return name ;
    }
}
