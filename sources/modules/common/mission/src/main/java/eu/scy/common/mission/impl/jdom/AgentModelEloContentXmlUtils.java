package eu.scy.common.mission.impl.jdom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jdom.Element;

import eu.scy.common.mission.AgentModelEloContent;
import eu.scy.common.mission.impl.BasicAgentModelEloContent;

public class AgentModelEloContentXmlUtils {

	private static final String MODEL_ELO_URI = "modelEloUri";
	private static final String AGENT_MODELS = "agentModels";
	private static final String KEY = "key";
	private static final String LANGUAGE = "language";

	@SuppressWarnings("unchecked")
	public static AgentModelEloContent agentModelFromXml(String xml)
			throws URISyntaxException {
		Element root = new JDomStringConversion().stringToXml(xml);
		if (root == null || !AGENT_MODELS.equals(root.getName())) {
			return null;
		}
		AgentModelEloContent agentModelEloContent = new BasicAgentModelEloContent();
		List<Element> keyElements = root.getChildren(KEY);
		for (Element keyElement : keyElements) {
			String key = keyElement.getTextTrim();
			List<Element> languageElements = keyElement.getChildren(LANGUAGE);
			for (Element languageElement : languageElements) {
				String language = languageElement.getTextTrim();
				URI modelEloUri = JDomConversionUtils.getUriValue(
						languageElement, MODEL_ELO_URI);
				agentModelEloContent.setModelEloUri(key, language, modelEloUri);
			}
		}
		return agentModelEloContent;
	}

	public static String agentModelToXml(AgentModelEloContent typedContent) {
		Element root = new Element(AGENT_MODELS);
		for (String key : typedContent.getKeys()) {
			Element keyElement = JDomConversionUtils.createElement(KEY, key);
			for (String language : typedContent.getLanguages(key)) {
				Element languageElement = JDomConversionUtils.createElement(
						LANGUAGE, language);
				URI modelUri = typedContent.getModelEloUri(key, language);
				languageElement.addContent(JDomConversionUtils.createElement(
						MODEL_ELO_URI, modelUri));
				keyElement.addContent(languageElement);
			}
			root.addContent(keyElement);
		}
		return new JDomStringConversion().xmlToString(root);
	}
}
