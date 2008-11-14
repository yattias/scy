package eu.scy.tools.webbrowsingtool.client;


public class Note {
    
    private String title;
    private String text;
    private String annotation;
    
    public Note(String header, String text, String annotation) {
        this.title=header;
        this.text=text;
        this.annotation=annotation;
 
    }
    
    public Note(String header, String text) {
        this(header,text,"");
    }
    
    public Note(String text){
        this("",text,"");
    }

    
    
    
    /**
     * @return the header
     */
    public String getTitle() {
        return title;
    }

    
    /**
     * @param header the header to set
     */
    public void setTitle(String header) {
        this.title = header;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    
    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    
    /**
     * @return the annotation
     */
    public String getAnnotation() {
        return annotation;
    }

    
    /**
     * @param annotation the annotation to set
     */
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    
    @Override
    public String toString() {
        String str = "Title: \t\t"+title+"\n" +"Text: \t\t"+text+"\n"+"Annotation: \t"+annotation;
        return str;
    }
    

}
