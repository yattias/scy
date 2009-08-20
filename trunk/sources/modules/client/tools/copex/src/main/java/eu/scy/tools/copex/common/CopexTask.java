/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import org.jdom.Element;


/**
 * represente une tache de l'arbre (classe mere), cela peut etre :
 * - une question / sous-question
 * - une etape
 * - une action
 * MBO le 28/04/09 : taches iteratives
 * @author MBO
 */
public abstract  class CopexTask implements Cloneable {
    // ATTRIBUTS
    /* identifiant base de donnees */
    private long dbKey;
    /* nom de la tache*/
    protected String name;
    /*description de la tache */
    protected String description;
    /* commentaires */
    protected String comments;
    /* image associee => nom du fichier */
    protected String taskImage ;
    /* dessin associe : element xml */
    protected Element draw;
    /* dernier etat affiche*/
    protected boolean isVisible;
    /* droits de la tache */
    protected TaskRight taskRight;
    
    /* identifiant frere */
    private long dbKeyBrother;
    /* identifiant enfant */
    private long dbKeyChild;
    /* boolean indiquant s'il s'agit de la racine ou non */
    private boolean root;
    /* repetition - null sinon */
    private TaskRepeat taskRepeat;

    // CONSTRUCTEURS
    public CopexTask(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, boolean root, TaskRepeat taskRepeat) {
        this.dbKey = dbKey;
        this.name = name;
        this.description = description;
        this.comments = comments;
        this.taskRight = taskRight;
        this.taskImage = taskImage;
        this.draw = draw;
        this.isVisible = isVisible;
        this.root = root;
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        this.taskRepeat = taskRepeat;
    }

    
    public CopexTask(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, boolean root, long dbKeyBrother, long dbKeyChild, TaskRepeat taskRepeat) {
        this.dbKey = dbKey;
        this.name = name;
        this.description = description;
        this.comments = comments;
        this.taskRight = taskRight;
        this.taskImage = taskImage ;
        this.draw = draw;
        this.root = root;
        this.isVisible = isVisible;
        this.dbKeyBrother = dbKeyBrother;
        this.dbKeyChild = dbKeyChild;
        this.taskRepeat = taskRepeat;
    }
    
    public CopexTask(){
        this.dbKey = -1;
        this.name = "FICTIV";
        this.description = "root fictive";
        this.comments = "";
        this.taskImage = null ;
        this.draw = null;
        this.taskRight = new TaskRight(MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        this.root = true;
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        this.isVisible = true;
        this.taskRepeat = null;
    }
   
   

    // GETTER AND SETTER
     public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(String taskImage) {
        this.taskImage = taskImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDbKey() {
        return dbKey;
    }

    public Element getDraw() {
        return draw;
    }

    public void setDraw(Element draw) {
        this.draw = draw;
    }

    public void setTaskRight(TaskRight taskRight) {
        this.taskRight = taskRight;
    }

    public TaskRight getTaskRight() {
        return taskRight;
    }

    public long getDbKeyBrother() {
        return dbKeyBrother;
    }

    public void setDbKeyBrother(long dbKeyBrother) {
        this.dbKeyBrother = dbKeyBrother;
    }

    public long getDbKeyChild() {
        return dbKeyChild;
    }

    public void setDbKeyChild(long dbKeyChild) {
        this.dbKeyChild = dbKeyChild;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
    public void setVisible(boolean visible){
        this.isVisible = visible;
    }
    public boolean isVisible(){
        return this.isVisible;
    }

    public TaskRepeat getTaskRepeat() {
        return taskRepeat;
    }

    public void setTaskRepeat(TaskRepeat taskRepeat) {
        this.taskRepeat = taskRepeat;
    }
    
    
    // OVERRIDE
    @Override
    public String toString() {
        return getDescription();
    }
    
    // METHODES
    /* retourne vrai s'il s'agit de la racine */
    public boolean isQuestionRoot(){
        return root;
    }
    /* retourne vrai si de type question */
    public boolean isQuestion(){
        return this instanceof Question;
    }
    /* retourne vrai si de type etape */
    public boolean isStep(){
        return this instanceof Step;
    }
    /* retourne vrai si de type action */
    public boolean isAction(){
        return this instanceof CopexAction;
    }

    /* retourne le droit editer */
    public char getEditRight(){
        return taskRight.getEditRight();
    }
     /* retourne le droit supprimer */
    public char getDeleteRight(){
        return taskRight.getDeleteRight();
    }
     /* retourne le droit copier */
    public char getCopyRight(){
        return taskRight.getCopyRight();
    }
     /* retourne le droit deplacer */
    public char getMoveRight(){
        return taskRight.getMoveRight();
    }
     /* retourne le droit parent */
    public char getParentRight(){
        return taskRight.getParentRight();
    }
     /* retourne le droit dessin */
    public char getDrawRight(){
        return taskRight.getDrawRight();
    }
     /* met le droit editer */
    public void setEditRight(char right){
        taskRight.setEditRight(right);
    }
     /* met le droit supprimer */
    public void setDeleteRight(char right){
        taskRight.setDeleteRight(right);
    }
     /* met le droit copier */
    public void setCopyRight(char right){
        taskRight.setCopyRight(right);
    }
     /* met le droit deplacer */
    public void setMoveRight(char right){
        taskRight.setMoveRight(right);
    }
     /* met le droit parent */
    public void setParentRight(char right){
        taskRight.setParentRight(right);
    }
     /* met le droit draw */
    public void setDrawRight(char right){
        taskRight.setDrawRight(right);
    }

    /* retourne vrai s'il s'agit d'une tache repetable */
    public boolean isTaskRepeat(){
        return this.taskRepeat != null;
    }
    
    @Override
    public Object clone()  {
        try {
            CopexTask task = (CopexTask) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            String descriptionC = "";
            if (this.description != null)
                   descriptionC= new String(this.description);
            String commentsC = "";
            if (this.comments != null)
                    commentsC = new String (this.comments);
            String taskImgC = "";
            if (this.taskImage != null)
                    taskImgC = new String (this.taskImage);
            Element d = null;
            if(this.draw != null){
                d = (Element)this.draw.clone() ;
            }
            TaskRight taskRightC = (TaskRight)this.taskRight.clone();
            long dbKeyBrotherC = this.dbKeyBrother;
            long dbKeyChildC = this.dbKeyChild;
            boolean rootC = this.root;
            boolean isVisibleC = this.isVisible;
            TaskRepeat tr = null;
            if(this.taskRepeat != null){
                tr = (TaskRepeat)this.taskRepeat.clone();
            }
            task.setDbKey(dbKeyC);
            task.setName(nameC);
            task.setDescription(descriptionC);
            task.setComments(commentsC);
            task.setTaskRight(taskRightC);
            task.setDbKeyBrother(dbKeyBrotherC);
            task.setDbKeyChild(dbKeyChildC);
            task.setRoot(rootC);
            task.setTaskImage(taskImgC);
            task.setDraw(d);
            task.setVisible(isVisibleC);
            task.setTaskRepeat(tr);
            return task;
        } catch (CloneNotSupportedException e) { 
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /* retourne l'element correspondant a  la chaine*/
    public static Element getElement(String s){
        if(s == null ||s.length() == 0)
            return null;
        return CopexUtilities.stringToXml(s);
    }
    
    
}
