package eu.scy.agents.roolo.impl;

import obwious.base.feature.Features;
import obwious.operator.ObjectIdentifiers;
import obwious.operator.Operator;
import obwious.operator.meta.Workflow;
import obwious.operator.type.Container;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import de.fhg.iais.kd.tm.toolbox.document.Document;

public class ObwiousAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractAgent<T, K> {
    
    class TestObwious extends Workflow {
        
        public TestObwious() {
            addParameterType("elo", IELO.class);
            
            addOperatorSpecification("ELO2OBWIOUSDocument", ELO2OBWIOUSDocument.class);
            addNamespaceLink("ELO2OBWIOUSDocument", "elo", "elo");
            
            addOutputType(ObjectIdentifiers.DOCUMENT, Document.class);
        }
    }
    
    public void processElo(T elo) {
        Operator workflow = (new TestObwious()).getOperator("Main");
        workflow.setInputParameter("elo", elo);
        Container result = workflow.run();
        Document doc = result.getObject(ObjectIdentifiers.DOCUMENT);
        System.out.println(doc.getFeature(Features.TEXT));
    }
    
}
