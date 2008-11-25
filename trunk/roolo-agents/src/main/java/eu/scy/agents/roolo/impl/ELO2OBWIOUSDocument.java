package eu.scy.agents.roolo.impl;

import java.util.Locale;

import obwious.base.feature.Features;
import obwious.operator.ObjectIdentifiers;
import obwious.operator.OperatorSpecification;
import obwious.operator.type.Container;
import roolo.elo.api.IELO;
import de.fhg.iais.kd.tm.toolbox.document.Document;

public class ELO2OBWIOUSDocument extends OperatorSpecification {
    
    private static final String ELO = "elo";
    private static final String LOCALE = "locale";
    
    public ELO2OBWIOUSDocument() {
        super();
        
        addParameterType(ELO, IELO.class);
        addParameterType(LOCALE, Locale.class, false, null);
        
        this.addOutputType(ObjectIdentifiers.DOCUMENT, Document.class);
    }
    
    @Override
    public Container run(Container inputParameters) {
        IELO elo = inputParameters.getObject(ELO);
        Locale usedLanguage = inputParameters.getObject(LOCALE);
        
        Container output = new Container(getOutputSignature());
        
        Document doc = new Document(elo.getUri().toString());
        String text = "";
        if (usedLanguage != null) {
            text = elo.getContent(usedLanguage).getXml().replaceAll("<[^>]*>", "");
        } else {
            text = elo.getContent(usedLanguage).getXml().replaceAll("<[^>]*>", "");
        }
        doc.setFeature(Features.TEXT, text);
        
        output.setObject(ObjectIdentifiers.DOCUMENT, doc);
        return output;
    }
}
