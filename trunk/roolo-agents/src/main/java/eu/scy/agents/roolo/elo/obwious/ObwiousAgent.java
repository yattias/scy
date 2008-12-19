package eu.scy.agents.roolo.elo.obwious;

import java.util.List;
import java.util.Locale;

import roolo.elo.api.I18nType;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.value.validators.StringValidator;
import de.fhg.iais.kd.tm.obwious.base.feature.Features;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.operator.document.TermFrequency;
import de.fhg.iais.kd.tm.obwious.operator.document.Tokens;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.type.Container;
import eu.scy.agents.impl.elo.AbstractELOAgent;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class ObwiousAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractELOAgent<T, K> {

	private ToolBrokerAPI<K> toolbroker;

	public ObwiousAgent() {
		this.toolbroker = new ToolBrokerImpl<K>();
	}

	class TestObwious extends Workflow {

		public TestObwious() {
			this.addParameterType("elo", IELO.class);

			this.addOperatorSpecification("ELO2OBWIOUSDocument", ELO2OBWIOUSDocument.class);
			this.addNamespaceLink("ELO2OBWIOUSDocument", "elo", "elo");
			this.setInputParameter("ELO2OBWIOUSDocument", "locale", Locale.GERMAN);

			this.addOperatorSpecification("Tokens", Tokens.class);
			this.setInputParameter("Tokens", "toLower", true);
			this.setInputParameter("Tokens", "delimiter", " -;,.()\"\n\t\r\f");
			this.setInputParameter("Tokens", "update", true);

			// this.addOperatorSpecification("Stopwords", StopWords.class);

			this.addOperatorSpecification("TermFrequencies", TermFrequency.class);
			this.setInputParameter("TermFrequencies", "update", true);

			this.addOutputType(ObjectIdentifiers.DOCUMENT, Document.class);
		}
	}

	@SuppressWarnings("unchecked")
	public void processElo(T elo) {
		System.out.println("Starting obwious agent");
		Operator workflow = new TestObwious().getOperator("Main");
		workflow.setInputParameter("elo", elo);
		Container result = workflow.run();
		Document doc = result.getObject(ObjectIdentifiers.DOCUMENT);
		System.out.println(doc.getFeature(Features.TOKENS));
		doc.getFeature(Features.TOKENS);

		K tokenKey = this.toolbroker.getMetaDataTypeManager().getMetadataKey("token");
		if (tokenKey == null) {
			IMetadataKey token = new StringMetadataKey("token",
					"/agentdata/obwiousdata/tokens/token", I18nType.UNIVERSAL,
					MetadataValueCount.LIST, new StringValidator());
			this.toolbroker.getMetaDataTypeManager().registerMetadataKey((K) token);
			tokenKey = (K) token;
		}

		K workflowKey = this.toolbroker.getMetaDataTypeManager().getMetadataKey("workflow");
		if (workflowKey == null) {
			IMetadataKey workflowK = new StringMetadataKey("workflow",
					"/agentdata/obwiousdata/workflow", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, new StringValidator());
			this.toolbroker.getMetaDataTypeManager().registerMetadataKey((K) workflowK);
			workflowKey = (K) workflowK;
		}

		IMetadataValueContainer tokenContainer = elo.getMetadata().getMetadataValueContainer(
				tokenKey);
		tokenContainer.setValueList((List<String>) doc.getFeature(Features.TOKENS));

		IMetadataValueContainer workflowContainer = elo.getMetadata().getMetadataValueContainer(
				workflowKey);
		workflowContainer.setValue("<operator class=\"Workflow\">\n"
				+ "  <operator class=\"ELO2OBWIOUSDocument\"\n"
				+ "  <parameter name=\"locale\" value=\"" + Locale.GERMAN + "\" />"
				+ "  </operator>\n" + "  <operator class=\"Tokens\">\n"
				+ "    <parameter name=\"toLower\" value=\"true\" />\n"
				+ "    <parameter name=\"delimiter\" value=\" -;,.()\"\" />\\n\\t\\f"
				+ "    <parameter name=\"update\" value=\"true\"\n" + "  </operator>"
				+ "  <operator class=\"Stopwords\">\n" + "  </operator>\n" + "</operator>\n");

		System.out.println(doc.getFeature(Features.TERMFREQUENCY));
	}
}
