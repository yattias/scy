/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de modifier une feuille de données
 * @author MBO
 */
public class UpdateDataSheetAction extends TraceAction {
    // ATTRIBUTS
    /* nombre de lignes*/
    private int nbRows;
    /* nombre de colonnes */
    private int nbCol;

    // CONSTRUCTEURS
    public UpdateDataSheetAction(long dbKeyProc, String procName, int nbRows, int nbCol) {
        super("UPDATE_DATASHEET", dbKeyProc, procName);
        this.nbRows = nbRows;
        this.nbCol = nbCol;
    }
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getNbRowsToXML();
        xml += getNbColToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        addParameters();
    }
    public void addParameters(){
        ParameterUserAction p =new ParameterUserAction("nb_rows", ""+this.nbRows);
        listParameter.add(p);
        p = new ParameterUserAction("nb_col", ""+this.nbCol);
        listParameter.add(p);
    }
    
     /* nombre de lignes */
    private String getNbRowsToXML(){
        return "        <nb_rows>"+this.nbRows+"</nb_rows>\n";
    }
     /* nombre de colonnes */
    private String getNbColToXML(){
        return "        <nb_col>"+this.nbCol+"</nb_col>\n";
    }
}
