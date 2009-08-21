/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;


/**
 * droit d'une tache
 * @author MBO
 */
public class TaskRight implements Cloneable {
    // ATTRIBUTS
    /* droit edition */
    private char editRight;
    /* droit suppression */
    private char deleteRight;
    /* droit copie */
    private char copyRight;
    /* droit de deplacer */
    private char moveRight;
    /* droit ajouter sous tache */
    private char parentRight;
    /* droit de creer un dessin associe a la tache */
    private char drawRight;
    /* droit a la repetition */
    private char repeatRight;

    // CONSTRUCTEURS
    public TaskRight(char editRight, char deleteRight, char copyRight, char moveRight, char parentRight, char drawRight, char repeatRight ) {
        this.editRight = editRight;
        this.deleteRight = deleteRight;
        this.copyRight = copyRight;
        this.moveRight = moveRight;
        this.parentRight = parentRight;
        this.drawRight = drawRight ;
        this.repeatRight = repeatRight;
    }

    // GETTER AND SETTER
    public char getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(char copyRight) {
        this.copyRight = copyRight;
    }

    public char getDeleteRight() {
        return deleteRight;
    }

    public void setDeleteRight(char deleteRight) {
        this.deleteRight = deleteRight;
    }

    public char getEditRight() {
        return editRight;
    }

    public void setEditRight(char editRight) {
        this.editRight = editRight;
    }

    public char getMoveRight() {
        return moveRight;
    }

    public void setMoveRight(char moveRight) {
        this.moveRight = moveRight;
    }

    public char getParentRight() {
        return parentRight;
    }

    public void setParentRight(char parentRight) {
        this.parentRight = parentRight;
    }

    public char getDrawRight() {
        return drawRight;
    }

    public void setDrawRight(char drawRight) {
        this.drawRight = drawRight;
    }

    public char getRepeatRight() {
        return repeatRight;
    }

    public void setRepeatRight(char repeatRight) {
        this.repeatRight = repeatRight;
    }

    // OVERRIDE
    @Override
    protected Object clone() throws CloneNotSupportedException {
       try {
            TaskRight taskRight = (TaskRight) super.clone() ;
            
            taskRight.setEditRight(new Character(this.getEditRight()));
            taskRight.setDeleteRight(new Character(this.getDeleteRight()));
            taskRight.setCopyRight(new Character(this.getCopyRight()));
            taskRight.setMoveRight(new Character(this.getMoveRight()));
            taskRight.setParentRight(new Character(this.getParentRight()));
            taskRight.setDrawRight(new Character(this.getDrawRight()));
            taskRight.setRepeatRight(new Character(this.getRepeatRight()));
            
            return taskRight;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    @Override
    public String toString() {
        String s = "Droits de la tache : ";
        s += "editer : "+getEditRight();
        s += "supprimer : "+getDeleteRight();
        s += "copier : "+getCopyRight();
        s += "deplacer : "+getMoveRight();
        s += "ajouter des sous taches : "+getParentRight();
        s += "creer dessin : "+getDrawRight() ;
        s += "repeter : "+getRepeatRight() ;
        return s;
    }
    
    
    
}
