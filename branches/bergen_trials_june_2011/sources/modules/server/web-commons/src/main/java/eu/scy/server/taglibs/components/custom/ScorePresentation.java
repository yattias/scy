package eu.scy.server.taglibs.components.custom;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jul.2010
 * Time: 06:39:04
 * To change this template use File | Settings | File Templates.
 */
public class ScorePresentation extends TagSupport {

    private String imageId;
    private Integer score;


     public int doEndTag() throws JspException {
         if(getScore() == null) setScore(0);

        try {
            double id = Math.random() ;
            //pageContext.getOut().write("SCORE " + score);
            for(int counter = 0; counter < getScore(); counter++) {
                pageContext.getOut().write("<img src=\"/webapp/components/resourceservice.html?id=" + getImageId() +"\"/>");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
