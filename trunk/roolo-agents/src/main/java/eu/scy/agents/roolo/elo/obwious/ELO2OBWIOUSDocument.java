package eu.scy.agents.roolo.elo.obwious;

import java.util.Locale;

import roolo.elo.api.IELO;
import de.fhg.iais.kd.tm.obwious.base.feature.FeatureProvider;
import de.fhg.iais.kd.tm.obwious.base.feature.Features;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.OperatorSpecification;
import de.fhg.iais.kd.tm.obwious.type.Container;

public class ELO2OBWIOUSDocument extends OperatorSpecification implements
		FeatureProvider {

	private static final String ELO = "elo";
	private static final String LOCALE = "locale";

	public ELO2OBWIOUSDocument() {
		super();

		this.addParameterType(ELO, IELO.class);
		this.addParameterType(LOCALE, Locale.class, false, null);

		this.addOutputType(ObjectIdentifiers.DOCUMENT, Document.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Container run(Container inputParameters) {
		IELO elo = inputParameters.getObject(ELO);
		Locale usedLanguage = inputParameters.getObject(LOCALE);

		Container output = new Container(getOutputSignature());

		Document doc = new Document("test");
		String text = "";
		if (usedLanguage != null) {
			text = elo.getContent().getXml().replaceAll("<[^>]*>", "");
		} else {
			text = elo.getContent(usedLanguage).getXml().replaceAll("<[^>]*>",
					"");
		}
		doc.setFeature(Features.TEXT, text);

		output.setObject(ObjectIdentifiers.DOCUMENT, doc);
		return output;
	}
}
