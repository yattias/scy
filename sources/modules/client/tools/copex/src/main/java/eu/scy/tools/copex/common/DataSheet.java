/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * represente une feuille de donnees 
 * @author MBO
 */
public class DataSheet implements Cloneable {

    // ATTRIBUTS
    /* cle primaire */
    private long dbKey;
    /* donnees */
    private CopexData[][] data;
    
    // CONSTRUCTEURS
    public DataSheet(CopexData[][] data){
        this.dbKey = -1;
        this.data = data;
    }
    public DataSheet(int nbL, int nbC){
        this.dbKey = -1;
        this.data = new CopexData[nbL][nbC];
    }
    public DataSheet(long dbKey, int nbL, int nbC){
        this.dbKey = dbKey;
        this.data = new CopexData[nbL][nbC];
    }

   
    // METHODES
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }
    public int getNbRows(){
        return this.data.length;
    }
    public int getNbColumns(){
        return this.data[0].length;
    }
    public String getTitleRow(int noRow){
        return data[noRow][0].getData();
    }
    public String getTitleColumn(int noCol){
        return data[0][noCol].getData();
    }

    public CopexData[][] getData() {
        return data;
    }

    public void setData(CopexData[][] data) {
        this.data = data;
    }
    
     @Override
    public Object clone()  {
        try {
            DataSheet ds = (DataSheet) super.clone() ;
            if (ds.getNbRows() > 0 ){
                CopexData[][] d = new CopexData[getNbRows()][getNbColumns()];
                for (int i=0; i<getNbRows(); i++){
                    for (int j=0; j<getNbColumns(); j++){
                        if (this.data[i][j] != null)
                            d[i][j] = (CopexData)data[i][j].clone();
                    }
                }
                ds.setData(d);
            }
            ds.setDbKey(new Long(this.dbKey));
            return ds;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
     
     /* mise a jour d'une valeur */
     public void setValueAt(CopexData data, int noRow, int noCol){
         this.data[noRow][noCol] = data;
     }
     
     /* retourne les valeurs du tableau */
     public String[][] getStringData() {
       String[][] dataString = null;
       if (this.getNbRows() > 0){
           dataString = new String[getNbRows()][getNbColumns()];
           for (int i=0; i<getNbRows(); i++)
               for (int j=0; j<getNbColumns(); j++){
                   if (this.data[i][j] != null)
                        dataString[i][j] = this.data[i][j].getData();
                   else
                       dataString[i][j] = "";
               }
       }
       return dataString;
     }
     
     /* retourne l'identifiant de la donnee, -1 si null */
     public long getDbKeyData(int noRow, int noCol){
         long dbKeyData = -1;
         if (this.data[noRow][noCol] != null){
             dbKeyData = this.data[noRow][noCol].getDbKey();
         }
         return dbKeyData;
     }
     
     /* retourne la donnee */
     public CopexData getDataAt(int noRow, int noCol){
         return this.data[noRow][noCol] ;
     }
     
     /* modification du nombre de colonnes et de lignes */
     public void update(int nbR, int nbC){
         int oldR = getNbRows();
         int oldC = getNbColumns();
         CopexData[][] newData = new CopexData[nbR][nbC];
         int ml = Math.min(oldR, nbR);
         int mc = Math.min(oldC, nbC);
         for (int i=0; i<ml; i++){
             for (int j=0; j<mc; j++){
                 newData[i][j] = this.data[i][j];
             }
         }
         setData(newData);
     }
}
