/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * task in copex, could be
 * - a question
 * - a step (or task)
 * - an action
 * MBO 28/04/09 : repeat task
 * @author Marjolaine
 */
public abstract  class CopexTask implements Cloneable {
    /*tag names */
    public final static String TAG_TASK_NAME = "task_name";
    public final static String TAG_TASK_DESCRIPTION = "description";
    public final static String TAG_TASK_COMMENTS = "comments";
    public final static String TAG_TASK_IMAGE = "image";
    public final static String TAG_TASK_DRAW="task_draw";



    /* database identifier */
    protected long dbKey;
    /* task name*/
    protected List<LocalText> listName;
    /*description */
    protected List<LocalText> listDescription;
    /* comments */
    protected List<LocalText> listComments;
    /* image linked => file name */
    protected String taskImage ;
    /* drawing : => xml element  */
    protected Element draw;
    /* last state*/
    protected boolean isVisible;
    /* rights */
    protected TaskRight taskRight;
    
    /* brother identifier */
    protected long dbKeyBrother;
    /* child identifier */
    protected long dbKeyChild;
    /* true if it is the root */
    protected boolean root;
    /* repeat of the task - null otherwise*/
    protected TaskRepeat taskRepeat;

    protected List<TaskRepeat> listTaskRepeatParent;

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
        this.listTaskRepeatParent = new LinkedList();
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
        this.listTaskRepeatParent = new LinkedList();
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
        this.listTaskRepeatParent = new LinkedList();
    }
   

    public CopexTask(Element xmlElem) throws JDOMException {
		
    }

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

    public void setComments(String text){
        for(Iterator<LocalText> t = listComments.iterator();t.hasNext();){
            t.next().setText(text);
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

    public void setDescription(String text){
        for(Iterator<LocalText> t = listDescription.iterator();t.hasNext();){
            t.next().setText(text);
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

    public void setName(String text){
        for(Iterator<LocalText> t = listName.iterator();t.hasNext();){
            t.next().setText(text);
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

    public List<TaskRepeat> getListTaskRepeatParent() {
        return listTaskRepeatParent;
    }

    public void setListTaskRepeatParent(List<TaskRepeat> listTaskRepeatParent) {
        this.listTaskRepeatParent = listTaskRepeatParent;
    }
    
    
    public String toString(Locale locale) {
        return getDescription(locale);
    }
    
    /* returns true if it is the root  */
    public boolean isQuestionRoot(){
        return root;
    }
    /* returns true if it a question*/
    public boolean isQuestion(){
        return this instanceof Question;
    }
    /* returns true if it is a step */
    public boolean isStep(){
        return this instanceof Step;
    }
    /* return true if it's an action*/
    public boolean isAction(){
        return this instanceof CopexAction;
    }

    /* returns the edit right */
    public char getEditRight(){
        return taskRight.getEditRight();
    }
     /* returns the delete rights */
    public char getDeleteRight(){
        return taskRight.getDeleteRight();
    }
     /*returns the copy right */
    public char getCopyRight(){
        return taskRight.getCopyRight();
    }
     /* returns the move right */
    public char getMoveRight(){
        return taskRight.getMoveRight();
    }
     /* returns the parent right*/
    public char getParentRight(){
        return taskRight.getParentRight();
    }
     /* returns the drawing right */
    public char getDrawRight(){
        return taskRight.getDrawRight();
    }
     /* returns the repeat right */
    public char getRepeatRight(){
        return taskRight.getRepeatRight();
    }
     /* set the edit right */
    public void setEditRight(char right){
        taskRight.setEditRight(right);
    }
     /* set the delete right */
    public void setDeleteRight(char right){
        taskRight.setDeleteRight(right);
    }
     /* set the copy right */
    public void setCopyRight(char right){
        taskRight.setCopyRight(right);
    }
     /* set the move right*/
    public void setMoveRight(char right){
        taskRight.setMoveRight(right);
    }
     /* set the parent right*/
    public void setParentRight(char right){
        taskRight.setParentRight(right);
    }
     /* set the drawing right */
    public void setDrawRight(char right){
        taskRight.setDrawRight(right);
    }
     /* set the repeat right */
    public void setRepeatRight(char right){
        taskRight.setRepeatRight(right);
    }

    /* returns true if the task can be repeat*/
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
            List<TaskRepeat> listTr = new LinkedList();
            if(listTaskRepeatParent != null){
                for (Iterator<TaskRepeat> t = listTaskRepeatParent.iterator(); t.hasNext();) {
                    listTr.add((TaskRepeat)t.next().clone());
                }
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

    public void addTaskRepeatParent(TaskRepeat tr){
        if(this.listTaskRepeatParent == null)
            this.listTaskRepeatParent = new LinkedList();
        this.listTaskRepeatParent.add(tr);
    }

    public boolean removeTaskRepeatParent(TaskRepeat tr){
        return this.listTaskRepeatParent.remove(tr);
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
            e.addContent(draw.cloneContent());
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
