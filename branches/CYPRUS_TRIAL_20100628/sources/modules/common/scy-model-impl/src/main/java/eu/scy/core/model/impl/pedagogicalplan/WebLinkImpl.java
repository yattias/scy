package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.WebLink;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 09:32:17
  */
@Entity
@DiscriminatorValue("weblink")
public class WebLinkImpl extends LearningMaterialImpl implements WebLink {

    private String url;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

}
