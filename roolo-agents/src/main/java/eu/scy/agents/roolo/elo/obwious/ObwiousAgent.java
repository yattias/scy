package eu.scy.agents.roolo.elo.obwious;

import java.util.List;
import java.util.Locale;

import roolo.elo.api.I18nType;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.value.validators.StringValidator;
import de.fhg.iais.kd.tm.obwious.base.feature.Features;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.operator.document.ProvideTermFrequency;
import de.fhg.iais.kd.tm.obwious.operator.document.ProvideTokens;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.type.Container;
import eu.scy.agents.impl.elo.AbstractELOAgent;

public class ObwiousAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractELOAgent<T, K> {

	class TestObwious extends Workflow {

		public TestObwious() {
			this.addParameterType("elo", IELO.class);

			this.addOperatorSpecification("ELO2OBWIOUSDocument", ELO2OBWIOUSDocument.class);
			this.addNamespaceLink("ELO2OBWIOUSDocument", "elo", "elo");
			this.setInputParameter("ELO2OBWIOUSDocument", "locale", Locale.GERMAN);

			this.addOperatorSpecification("Tokens", ProvideTokens.class);
			this.setInputParameter("Tokens", "toLower", true);
			this.setInputParameter("Tokens", "delimiter", " -;,.()\"\n\t\r\f");
			this.setInputParameter("Tokens", "update", true);

			// this.addOperatorSpecification("Stopwords", StopWords.class);

			this.addOperatorSpecification("TermFrequencies", ProvideTermFrequency.class);
			this.setInputParameter("TermFrequencies", "update", true);

			this.addOutputType(ObjectIdentifiers.DOCUMENT, Document.class);
		}
	}

	private IMetadataTypeManager<IMetadataKey> typeManager;

	@SuppressWarnings("unchecked")
	public void processElo(T elo) {
		System.out.println("Starting obwious agent");
		Operator workflow = new TestObwious().getOperator("Main");
		workflow.setInputParameter("elo", elo);
		Container result = workflow.run();
		Document doc = result.getObject(ObjectIdentifiers.DOCUMENT);
		System.out.println(doc.getFeature(Features.TOKENS));
		doc.getFeature(Features.TOKENS);

		K tokenKey = (K) typeManager.getMetadataKey("token");
		if (tokenKey == null) {
			K token = (K) new StringMetadataKey("token", "/agentdata/obwiousdata/tokens/token",
					I18nType.UNIVERSAL, MetadataValueCount.LIST, new StringValidator());
			typeManager.registerMetadataKey(token);
			tokenKey = token;
		}

		K workflowKey = (K) typeManager.getMetadataKey("workflow");
		if (workflowKey == null) {
			K workflowK = (K) new StringMetadataKey("workflow", "/agentdata/obwiousdata/workflow",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, new StringValidator());
			typeManager.registerMetadataKey(workflowK);
			workflowKey = workflowK;
		}

		IMetadataValueContainer tokenContainer = elo.getMetadata().getMetadataValueContainer(
				tokenKey);
		tokenContainer.setValueList((List<String>) doc.getFeature(Features.TOKENS));

		IMetadataValueContainer workflowContainer = elo.getMetadata().getMetadataValueContainer(
				workflowKey);
		workflowContainer.setValue(workflow.toXml());

		System.out.println(doc.getFeature(Features.TERMFREQUENCY));
	}
}
