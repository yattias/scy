package eu.scy.core.model;

import roolo.api.IELO;
import roolo.api.IMetadata;
import roolo.api.IContent;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.net.URI;

/**
 * Created by Intermedia User: Jeremy / Thomas Date: 11.aug.2008 Time: 04:00:01
 * An attempt at an ELO
 */

@Entity
@Table(name = "elo")
public class Elo extends SCYBaseObject implements IELO {
    
    private String eloName;
    private String eloContent;
    private URI uri;
    
    @Column(name = "eloName", nullable = false, unique = false)
    public String getEloName() {
        return eloName;
    }
    
    public void setEloName(String eloName) {
        this.eloName = eloName;
    }

    @Column(name = "eloContent", nullable = false, unique = false)
    public String getEloContent() {
        return eloContent;
    }
    
    public void setEloContent(String eloContent) {
        this.eloContent = eloContent;
    }

    @XmlTransient
    @Transient
    public IMetadata getMetadata() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setMetadata(IMetadata iMetadata) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Transient
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;

    }

    @XmlTransient
    @Transient
    public IContent getContent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public IContent getContent(Locale locale) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public Map getContentI18nMap() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public void setContent(IContent iContent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Transient
    public void addContent(IContent iContent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void clearContent() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteContent(Locale locale) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDefaultLanguage(Locale locale) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @XmlTransient
    @Transient
    public Locale getDefaultLanguage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IELO clone() throws CloneNotSupportedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public String getXml() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setContent(List list) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
