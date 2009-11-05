/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * represente une tache de l'arbre (classe mere), cela peut etre :
 * - une question / sous-question
 * - une etape
 * - une action
 * MBO le 28/04/09 : taches iteratives
 * @author MBO
 */
public abstract  class CopexTask implements Cloneable {
    /*tag names */
    public final static String TAG_TASK_NAME = "task_name";
    public final static String TAG_TASK_DESCRIPTION = "description";
    public final static String TAG_TASK_COMMENTS = "comments";
    public final static String TAG_TASK_IMAGE = "image";
    public final static String TAG_TASK_DRAW="draw";



    /* identifiant base de donnees */
    protected long dbKey;
    /* nom de la tache*/
    protected List<LocalText> listName;
    /*description de la tache */
    protected List<LocalText> listDescription;
    /* commentaires */
    protected List<LocalText> listComments;
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
    protected TaskRepeat taskRepeat;

    // CONSTRUCTEURS
    public CopexTask(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, boolean root, TaskRepeat taskRepeat) {
        this.dbKey = dbKey;
        this.listName = listName;
        this.listDescription = listDescription;
        this.listComments = listComments;
        this.taskRight = taskRight;
        this.taskImage = taskImage;
        this.draw = draw;
        this.isVisible = isVisible;
        this.root = root;
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        this.taskRepeat = taskRepeat;
    }

    
    public CopexTask(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, boolean root, long dbKeyBrother, long dbKeyChild, TaskRepeat taskRepeat) {
        this.dbKey = dbKey;
        this.listName = listName;
        this.listDescription = listDescription;
        this.listComments = listComments;
        this.taskRight = taskRight;
        this.taskImage = taskImage ;
        this.draw = draw;
        this.root = root;
        this.isVisible = isVisible;
        this.dbKeyBrother = dbKeyBrother;
        this.dbKeyChild = dbKeyChild;
        this.taskRepeat = taskRepeat;
    }
    
    public CopexTask(Locale locale){
        this.dbKey = -1;
        this.listName = new LinkedList();
        this.listName.add(new LocalText("FICTIV", locale));
        this.listDescription = new LinkedList();
        this.listDescription.add(new LocalText("root fictive", locale));
        this.listComments = new LinkedList();
        this.listComments.add(new LocalText("", locale));
        this.taskImage = null ;
        this.draw = null;
        this.taskRight = new TaskRight(MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        this.root = true;
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        this.isVisible = true;
        this.taskRepeat = null;
    }
   

    public CopexTask(Element xmlElem) throws JDOMException {
		
	}

    // GETTER AND SETTER
     public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public List<LocalText> getListComments() {
        return listComments;
    }

    public void setListComments(List<LocalText> listComments) {
        this.listComments = listComments;
    }

    public List<LocalText> getListDescription() {
        return listDescription;
    }

    public void setListDescription(List<LocalText> listDescription) {
        this.listDescription = listDescription;
    }

    public List<LocalText> getListName() {
        return listName;
    }

    public void setListName(List<LocalText> listName) {
        this.listName = listName;
    }

    public String getComments(Locale locale) {
        return CopexUtilities.getText(listComments, locale);
    }

    public void setComments(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listComments);
        if(id ==-1){
            this.listComments.add(text);
        }else{
            this.listComments.set(id, text);
        }
    }

    public String getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(String taskImage) {
        this.taskImage = taskImage;
    }

    public String getDescription(Locale locale) {
        return CopexUtilities.getText(listDescription, locale);
    }

    public void setDescription(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listDescription);
        if(id ==-1){
            this.listDescription.add(text);
        }else{
            this.listDescription.set(id, text);
        }
    }

    public String getName(Locale locale) {
        return CopexUtilities.getText(listName, locale);
    }

    public void setName(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listName);
        if(id ==-1){
            this.listName.add(text);
        }else{
            this.listName.set(id, text);
        }
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
        updateParameter();
    }
    
    
    // OVERRIDE
    public String toString(Locale locale) {
        return getDescription(locale);
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
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            task.setListName(listNameC);
            List<LocalText> listDescriptionC = new LinkedList();
            for (Iterator<LocalText> t = listDescription.iterator(); t.hasNext();) {
                listDescriptionC.add((LocalText)t.next().clone());
            }
            task.setListDescription(listDescriptionC);
            List<LocalText> listCommentsC = new LinkedList();
            for (Iterator<LocalText> t = listComments.iterator(); t.hasNext();) {
                listCommentsC.add((LocalText)t.next().clone());
            }
            task.setListComments(listCommentsC);
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

   
    protected void updateParameter(){
        
    }

    // toXML
    public Element toXML(){
        return null;
    }

    
    public Element toXML(Element element){
        if(listName != null && listName.size() > 0){
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_TASK_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if(listDescription != null && listDescription.size() > 0){
            for (Iterator<LocalText> t = listDescription.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_TASK_DESCRIPTION);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if(listComments != null && listComments.size() > 0){
            for (Iterator<LocalText> t = listComments.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_TASK_COMMENTS);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        if (taskImage != null && !taskImage.equals(""))
            element.addContent(new Element(TAG_TASK_IMAGE).setText(taskImage));
        if(draw != null){
            Element e = new Element(TAG_TASK_DRAW) ;
            e.addContent(draw);
            element.addContent(e);
        }
        element.addContent(taskRight.toXML());
        if(taskRepeat != null){
            element.addContent(taskRepeat.toXML());
        }
		return element;
    }

    public List<Material> getAllMaterialProd(){
        return new LinkedList();
    }

    public List<QData> getAllDataProd(){
        return new LinkedList();
    }

    public List<Material> getMaterialUsed(){
        return new LinkedList();
    }
}
