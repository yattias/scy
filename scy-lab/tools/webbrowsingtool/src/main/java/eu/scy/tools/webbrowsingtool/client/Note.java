package eu.scy.tools.webbrowsingtool.client;

import java.util.Date;


public class Note {
    
    private String title;
    private String text;
    private String annotation;
    private String url;
    private Date date;
    
    public Note(String header, String text, String annotation,String url) {
        this.title=header;
        this.text=text;
        this.annotation=annotation;
        this.url=url;
        this.date=new Date();
    }
    
    public Note(String header, String text, String annotation) {
        this(header,text,annotation,"");
    }
    
    public Note(String header, String text) {
        this(header,text,"","");
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

    
    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    
    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    

    
}
