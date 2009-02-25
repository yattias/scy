/*
 * Analyseur.java
 * Created on 10 mai 2007, 16:39
 */

package com.fitex.analyseFn;

import com.fitex.dataStruct.*;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Cedric
 */
public class Analyseur
{
    
    private StreamTokenizer lexical;
    // cette fonction est passe en parametre 
    private Function fonction ;

    /* locale */
    protected Locale locale ;
    /* ressource bundle */
    protected ResourceBundle bundle;
    
    /** Creates a new instance of Analyseur */
    public Analyseur(Locale locale, String texte) throws IOException
    {
        // i18n
        try{
            //this.bundle = ResourceBundle.getBundle("analyseFn.Bundle", locale);
            this.bundle = ResourceBundle.getBundle("Bundle", locale);
        }catch(MissingResourceException e){
          try{
              // par defaut on prend l'anglais
              locale = new Locale("en", "GB");
              bundle = ResourceBundle.getBundle("analyseFn.Bundle");
          }catch (MissingResourceException e2){
            System.out.println("ERROR , la langue sp�cifi�e "+locale+" n'existe pas : "+e2);
            }
        }

            lexical = new StreamTokenizer(new StringReader(texte));
            lexical.ordinaryChar('/');
            lexical.ordinaryChar('-');
            lexical.ordinaryChar('E');
            //lexical.ordinaryChar('x');
            System.out.println("f(x) = "+texte);
    }
    
    /** autre constructeur qui passe la fonction dans laquelle va
     * etre stockee le produit de l'analyseur 
     * n�cessaire lorsque l'on a une fonction param�tr�e
     */
    public Analyseur(Locale locale,String texte, Function fonction) throws IOException
    {
            this(locale,texte) ;
            this.fonction = fonction ;
    }
    
    public class ErreurDeSyntaxe extends Exception
    {
         ErreurDeSyntaxe(String message)
         {
            super(message);
         }
    }

     /* retourne un message selon cle*/
    public String getBundleString(String key){
        String s = "";
        try{
            s = this.bundle.getString(key);
        }catch(Exception e){
            System.out.println("Error : no message found ("+key+") !");
            return "";
        }
        return s;
    }
    
    public Expression analyser() throws IOException, ErreurDeSyntaxe
    {

        // message d'erreur
        /*String err = "La fonction n'a pas pu �tre analys�e.\n\n" +
                        "Liste des op�rateurs reconnus :\n" +
                        "  +   -   *   /   ^   E\n" +
                        "Liste des fonctions reconnues :\n" +
                        "  sqrt( )  exp( )  ln( )  log( )\n" +
                        "  sin( )  cos( )  tan( )\n" +
                        "  sinh( )  cosh( )  tanh( )\n" +
                        "  asin( )  acos( )  atan( )\n" +
                        "Liste des constantes reconnues :\n" +
                        "  pi\n\n" ;
                        // "L'op�rateur * doit �tre syst�matiquement indiqu�. Par exemple, �crivez 3*x et non 3x." ;

         */
        String err = getBundleString("MSG_ERROR_FUNCTION_ANALYSE")+"\n\n" +
                getBundleString("MSG_ERROR_LISTE_OP")+"\n"+
                getBundleString("MSG_ERROR_LISTE_OP_2")+"\n" +
                getBundleString("MSG_ERROR_LIST_FUNCTION")+"\n" +
                getBundleString("MSG_ERROR_LIST_FUNCTION_2")+"\n" +
                getBundleString("MSG_ERROR_LIST_FUNCTION_3")+"\n" +
                getBundleString("MSG_ERROR_LIST_FUNCTION_4")+"\n" +
                getBundleString("MSG_ERROR_LIST_FUNCTION_5")+"\n" +
                getBundleString("MSG_ERROR_LIST_CST")+"\n" +
                getBundleString("MSG_ERROR_LIST_CST_2")+"\n\n" ;
        //System.out.println("fonction analyser");
        lexical.nextToken();
        Expression resultat = analyserExpression();
        // System.out.println(lexical.ttype) ;
        if (lexical.ttype != StreamTokenizer.TT_EOF) {
            throw new ErreurDeSyntaxe(err) ;
        }
        System.out.println("Expression analys�e � parenthesage al�atoire : f(x) = " + resultat);
        return resultat;
    }
    
    private Expression analyserExpression() throws IOException, ErreurDeSyntaxe
    {
        //System.out.println("Analyse d'une expression");
                    
        Expression resultat = analyserTerme();

        while (lexical.ttype == '+' || lexical.ttype == '-')
        {
            if (resultat == null)
		resultat = new Constante(0);
            boolean estUneAddition = (lexical.ttype == '+');
            lexical.nextToken();
            Expression terme = analyserTerme();
            if (terme == null)
		throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_EXPR_ZERO"));
            if (estUneAddition)
                resultat = new Addition(resultat, terme);
            else
                resultat = new Soustraction(resultat, terme);
        }
        return resultat;   
    }

    private Expression analyserTerme() throws IOException, ErreurDeSyntaxe
    {
        //System.out.println("fonction analyserTerme");
        
        Expression resultat = analyserTermeDePow();

        while (lexical.ttype == '*' || lexical.ttype == '/')
        {
            if (resultat == null)
		throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_LEFT_FACT_ZERO"));
            boolean estUnProduit = (lexical.ttype == '*');
            lexical.nextToken();
            System.out.println(lexical.sval) ;
            Expression termeDePow = analyserTermeDePow();
            if (termeDePow == null)
		throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_RIGHT_FACT_ZERO"));
            if (estUnProduit)
                resultat = new Multiplication(resultat, termeDePow);
            else
                resultat = new Division(resultat, termeDePow);
        }
        // mise en place du cas ou le signe * est implicite
         while (resultat instanceof com.fitex.dataStruct.Constante && lexical.ttype == StreamTokenizer.TT_WORD) {
             Expression termeDePow = analyserTermeDePow();
             if (termeDePow == null)
		throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_RIGHT_FACT_ZERO"));
             resultat = new Multiplication(resultat, termeDePow);
        } 
        return resultat;
    }
    
    private Expression analyserTermeDePow() throws IOException, ErreurDeSyntaxe
    {
        Expression resultat = analyserFacteur();
        while (lexical.ttype == '^' || lexical.ttype == 'E')
        {
            System.out.println("detection d'une puissance");
            if (resultat == null)
		throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_LEFT_FACT_PUISS"));
            boolean estUnePuissance10 = (lexical.ttype == 'E');
            lexical.nextToken();
            Expression facteur = analyserFacteur();
            if (facteur == null)
		throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_RIGHT_FACT_PUISS"));
            if (estUnePuissance10)
                resultat = new PowE(resultat, facteur);
            else
                resultat = new Power(resultat, facteur);
        }
        return resultat;
    }
    
    private Expression analyserFacteur() throws IOException, ErreurDeSyntaxe
    {
        Expression resultat=null ;
        
        //System.out.println("fonction analyserFacteur");
        //System.out.println(lexical.ttype);
        //System.out.println("**"+lexical.sval+"**");
        
        if (lexical.ttype == StreamTokenizer.TT_NUMBER)
        {
            resultat = new Constante(lexical.nval) ;
            lexical.nextToken();
        }
        else if (lexical.ttype == '(')
        {
            lexical.nextToken();
            resultat = analyserExpression();
            if (lexical.ttype != ')')
                throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_LACK_CLOSE_PARENTHESIS"));
            lexical.nextToken();
        }
        else if (lexical.ttype == '-')
        {
            // prise en charge des nombres n�gatifs
            Expression exp = null;
            lexical.nextToken();
            exp = analyserTerme();
            resultat = new Soustraction(new Constante(0), exp);
        } 
        else if (lexical.ttype == StreamTokenizer.TT_WORD)  
        {
	    int expType = -1;
            if (lexical.sval.equals("sqrt"))
                expType = 0;
            else if (lexical.sval.equals("exp"))
		expType = 1;
            else if (lexical.sval.equals("ln"))
		expType = 2;
            else if (lexical.sval.equals("log"))
		expType = 3;
            else if (lexical.sval.equals("sin"))
		expType = 4;
	    else if (lexical.sval.equals("cos"))
		expType = 5;
	    else if (lexical.sval.equals("tan"))
		expType = 6;
            else if (lexical.sval.equals("sinh"))
		expType = 7;
            else if (lexical.sval.equals("cosh"))
		expType = 8;
            else if (lexical.sval.equals("tanh"))
		expType = 9;
            else if (lexical.sval.equals("asin"))
		expType = 10;
            else if (lexical.sval.equals("acos"))
		expType = 11;
            else if (lexical.sval.equals("atan"))
		expType = 12;
            else if (lexical.sval.equals("x") || lexical.sval.equals("X"))
		expType = 20;
            else if (lexical.sval.equals("pi"))
		expType = 21;
	    
	    if (expType == -1)
	    {
                resultat = new Parametre(lexical.sval, fonction);
                lexical.nextToken();
	    }
	    else if (expType == 20)
	    {
		resultat = new Variable();
		lexical.nextToken();
	    }
            else if (expType == 21)
	    {
		resultat = new Constante(Math.PI) ;
                lexical.nextToken();
	    }
	    else
	    {
		Expression exp = null;
		lexical.nextToken();
		if (lexical.ttype == '(')
		{
		    lexical.nextToken();
		    exp = analyserExpression();
		    if (exp == null)
			throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_ATTRIBUTE_ZERO"));
		    if (lexical.ttype != ')')
			throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_LACK_CLOSE_PARENTHESIS"));
		    lexical.nextToken();
		    
		    switch (expType)
		    {
			case 0:
			    resultat = new Rac(exp);
			    break;
			case 1:
			    resultat = new Exp(exp);
			    break;
			case 2:
			    resultat = new Ln(exp);
			    break;
			case 3:
			    resultat = new Log(exp);
			    break;
                        case 4:
			    resultat = new Sin(exp);
			    break;
                        case 5:
			    resultat = new Cos(exp);
			    break;
                        case 6:
			    resultat = new Tan(exp);
			    break;
                        case 7:
			    resultat = new Sinh(exp);
			    break;
                        case 8:
			    resultat = new Cosh(exp);
			    break;
                        case 9:
			    resultat = new Tanh(exp);
			    break;
                        case 10:
			    resultat = new Asin(exp);
			    break;
                        case 11:
			    resultat = new Acos(exp);
			    break;
                        case 12:
			    resultat = new Atan(exp);
			    break;
		    }
		}
		else
		    throw new ErreurDeSyntaxe(getBundleString("MSG_ERROR_LACK_OPEN_PARENTHESIS"));
	    }
	}
        return resultat ;
    }

   
}
