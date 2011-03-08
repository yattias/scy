package eu.scy.agents.keywords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;

import de.fhg.iais.kd.tm.obwious.system.documentfrequency.DocumentFrequencyModel;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.workflow.KeywordConstants;

public class ExtractKeywordsDecisionMakerAgentTest extends AbstractTestFixture {

	private IELO elo;
	private Object eloUri;
	private static final String ELO_CONTENT = "<preview><![CDATA[<document><head> <style type=\"text/css\">h1{font-family:Georgia,\"Times New Roman\",Times,serif; font-weight:normal; border-bottom:3px solid #E2E1DE; font-size:200%; margin-bottom:0.5em;} h2{ font-family:Georgia,\"Times New Roman\",Times,serif; font-weight:normal; font-size:130%; } p{ color:#25221D; font-family:Verdana,Tahoma,sans-serif;font-size:14px;font-size-adjust:none;font-style:normal;font-variant:normal;font-weight:normal;line-height:1.7; margin:0 0 1.7em; padding:0;} ul{ color:#25221D; font-family:Verdana,Tahoma,sans-serif;font-size:14px;font-size-adjust:none;font-style:normal;font-variant:normal;font-weight:normal;line-height:1.7; margin:0 0 1.7em; list-style-type: disc;} li{ color:#25221D; font-family:Verdana,Tahoma,sans-serif;font-size:14px;font-size-adjust:none;font-style:normal;font-variant:normal;font-weight:normal;line-height:1.7; margin:0 0 1.7em; margin-bottom:0.25em; } a{ font-family:Verdana,Sans-Serif;} </style></head><body><h1>Some dude</h1><h2>Summary</h2><ul><li>Environmentally Friendly, Non Toxic Paint All paints contain three main components: pigment (colour), a binder (holds the paint together) and a carrier (disperses the binder). With many modern paints these ingredients are made using toxic chemicals that are harmful to both the environment and human health. Cadmium, lead and chromium are frequently used in pigments; and petrochemicals, solvents, benzene, formaldehyde and other volatile organic compounds (VOCs) are used in binders and carriers. Toxic, environmentally harmful, chemicals are also used in modern paints as preservatives, stabilisers, thickeners and driers. VOCs are organic (carbon based) chemical compounds that evaporate easily in the atmosphere, and are known to be a major contributor to global climate change. Many of them are highly toxic and linked with numerous health problems such as respiratory disease, asthma, dizziness, headaches, nausea, fatigue, skin disorders, eye irritation, liver and kidney damage and even cancer. Modern chemical paints continue to emit VOCs many years after their application. Increasing concerns about the impact of chemical paints on health and the environment have led to a growing market in non-toxic paints. Environmental regulations have forced conventional paint companies to significantly reduce their VOC content, and most of the large paint companies now offer one or more varieties of non-toxic paints. However, many of these still contain VOC solvents, chemical pigments and fungicides. Eco Labels for Paints Non-toxic paints are often called Low-VOC, No-VOC, VOC-Free, odourless, odour-free and green, natural or organic paints. There are no set standards for defining these labels, and they are widely misused for marketing purposes. To help consumers make informed decisions on their paint purchases, various ecological labels have been developed by different countries to indicate that the paint has fulfilled certain environmental requirements, in accordance with respective government regulations. These eco-labels can be found as logos on paint cans, and include the European Eco-Label, Blue Angel in Germany, and Green Seal and Greenguard in the USA. In the UK, VOC labels are used, and indicate the content of VOCs using one of five classifications: Minimal (0-0.29%), Low, Medium, High and Very High (VOC content greater than 50%). Low-VOC paints tend to use water as a carrier instead of petrochemical solvents, and so their emissions are minimal. Many conventional paints have achieved relatively low VOC levels. No-VOC or VOC-Free paints may still contain very low levels of VOCs in their pigments or additives. Although reducing VOC content is a move in the right direction, it is questionable whether either of these paint types can be considered non-toxic. Natural Paints Natural paints are the only true non-toxic paint since they contain no VOCs, and are made from natural ingredients such as water, vegetable oils, plant dyes, and natural minerals. The main binders used in natural paints are: linseed oil (from flax seeds), clay, lime, and milk protein. Lime and milk paints give an authentic period look, and are often used in antique restoration projects. Chalk is used as an extender to thicken paint; turpentine (distilled from pine trees) is used as a solvent; essential oils from citrus fruits (d-limonene) are used as a solvent and fragrance; and natural mineral and earth pigments are used as colorants. The main benefits of natural paints are: Non-toxic - no hazardous fumes or harmful effects on health. This is significant for allergy sufferers and chemically sensitive people who are unable to tolerate chemical paints. Environmentally Friendly - use renewable resources; are biodegradable, can even be composted. Micro-Porous - allow walls and surfaces to breathe, preventing condensation and damp problems, and reducing associated indoor allergens. They are also less prone to paint flaking, peeling and blistering.  On the downside, natural paints can be more expensive because they are made on a smaller scale, although this situation is changing as they become more popular. Natural paints can also take longer to dry (sometimes up to 24 hours or more) and there is less of a colour range to choose from. Natural mineral pigments tend to produce paints that come in pastel shades only, and this has led some natural paint companies to use synthetic pigments to create a more extensive range of colours. Paints for a Sustainable Future Unlike conventional paint companies, natural paint suppliers are committed to making sure their materials derive from sustainable sources and are manufactured in an environmentally friendly way. They aim to minimise pollution, energy and waste throughout the life cycle of their products. Natural paint companies give thought to the recyclable packaging of their products, and they also declare their product ingredients so that consumers know exactly what they are using. Because natural paints are non-toxic, they are completely safe and this makes them the obvious choice for any consumer concerned with protecting the earth and its people for future generations.<li> <br><img src=\"http://upload.wikimedia.org/wikipedia/commons/b/b1/Detail_Ali_Hassan_al-Majid.jpg\"></ul><h2>Comments</h2><p>Add comment</p><h2>Sources</h2><p><a href=\"http://de.wikipedia.org/wiki/Torotoro\" target=\"_blank\">http://de.wikipedia.org/wiki/Torotoro</a> <br><a href=\"http://upload.wikimedia.org/wikipedia/commons/b/b1/Detail_Ali_Hassan_al-Majid.jpg\" target=\"_blank\">http://upload.wikimedia.org/wikipedia/commons/b/b1/Detail_Ali_Hassan_al-Majid.jpg</a> <br></p></body> </document>]]></preview><annotations> <document><title>Some dude</title><summary><bullet>Environmentally Friendly, Non Toxic Paint All paints contain three main components: pigment (colour), a binder (holds the paint together) and a carrier (disperses the binder). With many modern paints these ingredients are made using toxic chemicals that are harmful to both the environment and human health. Cadmium, lead and chromium are frequently used in pigments; and petrochemicals, solvents, benzene, formaldehyde and other volatile organic compounds (VOCs) are used in binders and carriers. Toxic, environmentally harmful, chemicals are also used in modern paints as preservatives, stabilisers, thickeners and driers. VOCs are organic (carbon based) chemical compounds that evaporate easily in the atmosphere, and are known to be a major contributor to global climate change. Many of them are highly toxic and linked with numerous health problems such as respiratory disease, asthma, dizziness, headaches, nausea, fatigue, skin disorders, eye irritation, liver and kidney damage and even cancer. Modern chemical paints continue to emit VOCs many years after their application. Increasing concerns about the impact of chemical paints on health and the environment have led to a growing market in non-toxic paints. Environmental regulations have forced conventional paint companies to significantly reduce their VOC content, and most of the large paint companies now offer one or more varieties of non-toxic paints. However, many of these still contain VOC solvents, chemical pigments and fungicides. Eco Labels for Paints Non-toxic paints are often called Low-VOC, No-VOC, VOC-Free, odourless, odour-free and green, natural or organic paints. There are no set standards for defining these labels, and they are widely misused for marketing purposes. To help consumers make informed decisions on their paint purchases, various ecological labels have been developed by different countries to indicate that the paint has fulfilled certain environmental requirements, in accordance with respective government regulations. These eco-labels can be found as logos on paint cans, and include the European Eco-Label, Blue Angel in Germany, and Green Seal and Greenguard in the USA. In the UK, VOC labels are used, and indicate the content of VOCs using one of five classifications: Minimal (0-0.29%), Low, Medium, High and Very High (VOC content greater than 50%). Low-VOC paints tend to use water as a carrier instead of petrochemical solvents, and so their emissions are minimal. Many conventional paints have achieved relatively low VOC levels. No-VOC or VOC-Free paints may still contain very low levels of VOCs in their pigments or additives. Although reducing VOC content is a move in the right direction, it is questionable whether either of these paint types can be considered non-toxic. Natural Paints Natural paints are the only true non-toxic paint since they contain no VOCs, and are made from natural ingredients such as water, vegetable oils, plant dyes, and natural minerals. The main binders used in natural paints are: linseed oil (from flax seeds), clay, lime, and milk protein. Lime and milk paints give an authentic period look, and are often used in antique restoration projects. Chalk is used as an extender to thicken paint; turpentine (distilled from pine trees) is used as a solvent; essential oils from citrus fruits (d-limonene) are used as a solvent and fragrance; and natural mineral and earth pigments are used as colorants. The main benefits of natural paints are: Non-toxic - no hazardous fumes or harmful effects on health. This is significant for allergy sufferers and chemically sensitive people who are unable to tolerate chemical paints. Environmentally Friendly - use renewable resources; are biodegradable, can even be composted. Micro-Porous - allow walls and surfaces to breathe, preventing condensation and damp problems, and reducing associated indoor allergens. They are also less prone to paint flaking, peeling and blistering.  On the downside, natural paints can be more expensive because they are made on a smaller scale, although this situation is changing as they become more popular. Natural paints can also take longer to dry (sometimes up to 24 hours or more) and there is less of a colour range to choose from. Natural mineral pigments tend to produce paints that come in pastel shades only, and this has led some natural paint companies to use synthetic pigments to create a more extensive range of colours. Paints for a Sustainable Future Unlike conventional paint companies, natural paint suppliers are committed to making sure their materials derive from sustainable sources and are manufactured in an environmentally friendly way. They aim to minimise pollution, energy and waste throughout the life cycle of their products. Natural paint companies give thought to the recyclable packaging of their products, and they also declare their product ingredients so that consumers know exactly what they are using. Because natural paints are non-toxic, they are completely safe and this makes them the obvious choice for any consumer concerned with protecting the earth and its people for future generations.</bullet><bullet>http://upload.wikimedia.org/wikipedia/commons/b/b1/Detail_Ali_Hassan_al-Majid.jpg</bullet></summary><comments>Add comment</comments><sources>http://de.wikipedia.org/wiki/Torotorohttp://upload.wikimedia.org/wikipedia/commons/b/b1/Detail_Ali_Hassan_al-Majid.jpg</sources></document> </annotations><html><![CDATA[<head><title>Detail_Ali_Hassan_al-Majid.jpg (JPEG-Grafik, 734x722 Pixel) - Skaliert (80%)</title></head><body><img style=\"cursor: -moz-zoom-in;\" alt=\"http://upload.wikimedia.org/wikipedia/commons/b/b1/Detail_Ali_Hassan_al-Majid.jpg\" src=\"http://upload.wikimedia.org/wikipedia/commons/b/b1/Detail_Ali_Hassan_al-Majid.jpg\" height=\"579\" width=\"588\"></body>]]></html>";

	// nontoxic, ingredients, binder, solvent, chemical, toxic, labels, paint, voc, health, natural, pigment

	private String[] expectedKeywords = new String[] { "keyword=nontoxic", "keyword=ingredients", "keyword=binder",
			"keyword=solvent", "keyword=chemical", "keyword=toxic", "keyword=labels", "keyword=paint", "keyword=voc",
			"keyword=health", "keyword=natural", "keyword=pigment" };

	@BeforeClass
	public static void startTS() {
		startTupleSpaceServer();
	}

	@AfterClass
	public static void stopTS() {
		stopTupleSpaceServer();
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		initTopicModel();
		initDfModel();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
		params.put(ExtractKeywordsDecisionMakerAgent.IDLE_TIME_INMS, 2000L);
		params.put(ExtractKeywordsDecisionMakerAgent.MINIMUM_NUMBER_OF_CONCEPTS, 2);
		agentMap.put(ExtractKeywordsAgent.NAME, params);
		agentMap.put(ExtractTfIdfKeywordsAgent.NAME, params);
		agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
		agentMap.put(ExtractKeywordsDecisionMakerAgent.NAME, params);

		startAgentFramework(agentMap);

		elo = createNewElo("Test", "scy/webresourcer");
		IContent content = new BasicContent();
		content.setXmlString(ELO_CONTENT);
		elo.setContent(content);

		IMetadata metadata = repository.addNewELO(elo);
		eloUri = metadata.getMetadataValueContainer(typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER))
				.getValue();
		System.out.println(eloUri.toString());
	}

	@Override
	@After
	public void tearDown() {
		try {
			getPersistentStorage().remove(KeywordConstants.DOCUMENT_FREQUENCY_MODEL);
			removeTopicModel();
			stopAgentFrameWork();
			super.tearDown();
		} catch (AgentLifecycleException e) {
			e.printStackTrace();
		}
	}

	private void initDfModel() throws ClassNotFoundException, IOException {
		InputStream inStream = this.getClass().getResourceAsStream("/models/df.out");
		ObjectInputStream in = new ObjectInputStream(inStream);
		DocumentFrequencyModel dfModel = (DocumentFrequencyModel) in.readObject();
		getPersistentStorage().put(KeywordConstants.DOCUMENT_FREQUENCY_MODEL, dfModel);
	}

	@Test
	public void testRun() throws InterruptedException, TupleSpaceException {
		sendWebresourcerStarted();
		sendScyMapperStarted();
		sendELoLoaded(eloUri.toString());

		sendConceptAdded();
		Thread.sleep(5000);

		Tuple notificationTuple = getCommandSpace().waitToTake(
				new Tuple(AgentProtocol.NOTIFICATION, String.class, String.class, "scymapper", String.class,
						String.class, Field.createWildCardField()), AgentProtocol.ALIVE_INTERVAL);
		assertNotNull("no notification received", notificationTuple);
		assertEquals(AgentProtocol.NOTIFICATION, notificationTuple.getField(0).getValue());
		assertEquals("jeremy@scy.collide.info/Smack", notificationTuple.getField(2).getValue());
		assertEquals(ExtractKeywordsDecisionMakerAgent.SCYMAPPER, notificationTuple.getField(3).getValue());
		assertEquals(ExtractKeywordsDecisionMakerAgent.class.getName(), notificationTuple.getField(4).getValue());
		assertEquals("mission1", notificationTuple.getField(5).getValue());
		assertEquals("n/a", notificationTuple.getField(6).getValue());
		assertEquals("type=concept_proposal", notificationTuple.getField(7).getValue());
		for (int i = 8; i < notificationTuple.getNumberOfFields(); i++) {
			String keyword = (String) notificationTuple.getField(i).getValue();
			// System.out.println(keyword);
			assertEquals(expectedKeywords[i - 8], keyword);
		}
	}

	@Test
	public void testNoEloRun() throws InterruptedException, TupleSpaceException {
		sendWebresourcerStarted();
		sendScyMapperStarted();
		sendELoLoaded(null);

		sendConceptAdded();
		Thread.sleep(5000);

		Tuple notificationTuple = getCommandSpace().waitToTake(
				new Tuple(AgentProtocol.NOTIFICATION, String.class, String.class, "scymapper", String.class,
						String.class, Field.createWildCardField()), AgentProtocol.ALIVE_INTERVAL);
		assertNull("notification received", notificationTuple);
	}

	private void sendELoLoaded(String eloPath) {
		try {
			getActionSpace().write(
					new Tuple(AgentProtocol.ACTION, "id3", System.currentTimeMillis(), AgentProtocol.ACTION_ELO_LOADED,
							"jeremy@scy.collide.info/Smack", ExtractKeywordsDecisionMakerAgent.WEBRESOURCER,
							"mission1", "n/a", AgentProtocol.ACTIONLOG_ELO_URI + "=" + eloPath));
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void sendConceptAdded() {
		try {
			getActionSpace().write(
					new Tuple(AgentProtocol.ACTION, "id1", System.currentTimeMillis(), AgentProtocol.ACTION_NODE_ADDED,
							"jeremy@scy.collide.info/Smack", ExtractKeywordsDecisionMakerAgent.SCYMAPPER, "mission1",
							"n/a", "id=111", "name=label"));
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void sendWebresourcerStarted() {
		try {
			getActionSpace().write(
					new Tuple(AgentProtocol.ACTION, "id1", System.currentTimeMillis(),
							AgentProtocol.ACTION_TOOL_STARTED, "jeremy@scy.collide.info/Smack",
							ExtractKeywordsDecisionMakerAgent.WEBRESOURCER, "mission1", "n/a"));
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void sendScyMapperStarted() {
		try {
			getActionSpace().write(
					new Tuple(AgentProtocol.ACTION, "id1", System.currentTimeMillis(),
							AgentProtocol.ACTION_TOOL_STARTED, "jeremy@scy.collide.info/Smack",
							ExtractKeywordsDecisionMakerAgent.CONCEPTMAP, "mission1", "n/a"));
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
}