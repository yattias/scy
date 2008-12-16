package eu.scy.agents.roolo.elo.obwious;

import java.util.List;
import java.util.Locale;

import obwious.base.feature.Features;
import obwious.operator.ObjectIdentifiers;
import obwious.operator.Operator;
import obwious.operator.document.TermFrequency;
import obwious.operator.document.Tokens;
import obwious.operator.meta.Workflow;
import obwious.operator.sandbox.StopWords;
import obwious.operator.type.Container;
import roolo.elo.api.I18nType;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.value.validators.StringValidator;
import de.fhg.iais.kd.tm.toolbox.document.Document;
import eu.scy.agents.impl.elo.AbstractELOAgent;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class ObwiousAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractELOAgent<T, K> {
    
    private ToolBrokerAPI<K> toolbroker;
    
    public ObwiousAgent() {
        toolbroker = new ToolBrokerImpl<K>();
    }
    
    class TestObwious extends Workflow {
        
        public TestObwious() {
            addParameterType("elo", IELO.class);
            
            addOperatorSpecification("ELO2OBWIOUSDocument", ELO2OBWIOUSDocument.class);
            addNamespaceLink("ELO2OBWIOUSDocument", "elo", "elo");
            setInputParameter("ELO2OBWIOUSDocument", "locale", Locale.GERMAN);
            
            addOperatorSpecification("Tokens", Tokens.class);
            setInputParameter("Tokens", "toLower", true);
            setInputParameter("Tokens", "delimiter", " -;,.()\"\n\t\r\f");
            setInputParameter("Tokens", "update", true);
            
            addOperatorSpecification("Stopwords", StopWords.class);
            
            addOperatorSpecification("TermFrequencies", TermFrequency.class);
            setInputParameter("TermFrequencies", "update", true);
            
            addOutputType(ObjectIdentifiers.DOCUMENT, Document.class);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void processElo(T elo) {
        System.out.println("Starting obwious agent");
        Operator workflow = (new TestObwious()).getOperator("Main");
        workflow.setInputParameter("elo", elo);
        Container result = workflow.run();
        Document doc = result.getObject(ObjectIdentifiers.DOCUMENT);
        System.out.println(doc.getFeature(Features.TOKENS));
        doc.getFeature(Features.TOKENS);
        
        K tokenKey = toolbroker.getMetaDataTypeManager().getMetadataKey("token");
        if (tokenKey == null) {
            IMetadataKey token = new StringMetadataKey("token", "/agentdata/obwiousdata/tokens/token", I18nType.UNIVERSAL, MetadataValueCount.LIST, new StringValidator());
            toolbroker.getMetaDataTypeManager().registerMetadataKey((K) token);
            tokenKey = (K) token;
        }
        
        K workflowKey = toolbroker.getMetaDataTypeManager().getMetadataKey("workflow");
        if (workflowKey == null) {
            IMetadataKey workflowK = new StringMetadataKey("workflow", "/agentdata/obwiousdata/workflow", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, new StringValidator());
            toolbroker.getMetaDataTypeManager().registerMetadataKey((K) workflowK);
            workflowKey = (K) workflowK;
        }
        
        IMetadataValueContainer tokenContainer = elo.getMetadata().getMetadataValueContainer(tokenKey);
        tokenContainer.setValueList((List<String>) doc.getFeature(Features.TOKENS));
        
        IMetadataValueContainer workflowContainer = elo.getMetadata().getMetadataValueContainer(workflowKey);
        workflowContainer.setValue("<operator class=\"Workflow\">\n" + "  <operator class=\"ELO2OBWIOUSDocument\"\n" + "  <parameter name=\"locale\" value=\"" + Locale.GERMAN + "\" />" + "  </operator>\n" + "  <operator class=\"Tokens\">\n" + "    <parameter name=\"toLower\" value=\"true\" />\n" + "    <parameter name=\"delimiter\" value=\" -;,.()\"\" />\\n\\t\\f" + "    <parameter name=\"update\" value=\"true\"\n" + "  </operator>" + "  <operator class=\"Stopwords\">\n" + "  </operator>\n"
                + "</operator>\n");
        
        System.out.println(doc.getFeature(Features.TERMFREQUENCY));
    }
}
