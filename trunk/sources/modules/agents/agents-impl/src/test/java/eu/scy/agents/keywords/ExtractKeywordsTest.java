package eu.scy.agents.keywords;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.roolo.rooloaccessor.RooloAccessorAgent;

public class ExtractKeywordsTest extends AbstractTestFixture {

	private static final String TEXT = "Environmentally Friendly, Non Toxic Paint All paints contain three main components: pigment (colour), a binder (holds the paint together) and a carrier (disperses the binder). With many modern paints these ingredients are made using toxic chemicals that are harmful to both the environment and human health. Cadmium, lead and chromium are frequently used in pigments; and petrochemicals, solvents, benzene, formaldehyde and other volatile organic compounds (VOCs) are used in binders and carriers. Toxic, environmentally harmful, chemicals are also used in modern paints as preservatives, stabilisers, thickeners and driers. VOCs are organic (carbon based) chemical compounds that evaporate easily in the atmosphere, and are known to be a major contributor to global climate change. Many of them are highly toxic and linked with numerous health problems such as respiratory disease, asthma, dizziness, headaches, nausea, fatigue, skin disorders, eye irritation, liver and kidney damage and even cancer. Modern chemical paints continue to emit VOCs many years after their application. Increasing concerns about the impact of chemical paints on health and the environment have led to a growing market in non-toxic paints. Environmental regulations have forced conventional paint companies to significantly reduce their VOC content, and most of the large paint companies now offer one or more varieties of non-toxic paints. However, many of these still contain VOC solvents, chemical pigments and fungicides. Eco Labels for Paints Non-toxic paints are often called Low-VOC, No-VOC, VOC-Free, odourless, odour-free and green, natural or organic paints. There are no set standards for defining these labels, and they are widely misused for marketing purposes. To help consumers make informed decisions on their paint purchases, various ecological labels have been developed by different countries to indicate that the paint has fulfilled certain environmental requirements, in accordance with respective government regulations. These eco-labels can be found as logos on paint cans, and include the European Eco-Label, Blue Angel in Germany, and Green Seal and Greenguard in the USA. In the UK, VOC labels are used, and indicate the content of VOCs using one of five classifications: Minimal (0-0.29%), Low, Medium, High and Very High (VOC content greater than 50%). Low-VOC paints tend to use water as a carrier instead of petrochemical solvents, and so their emissions are minimal. Many conventional paints have achieved relatively low VOC levels. No-VOC or VOC-Free paints may still contain very low levels of VOCs in their pigments or additives. Although reducing VOC content is a move in the right direction, it is questionable whether either of these paint types can be considered non-toxic. Natural Paints Natural paints are the only true non-toxic paint since they contain no VOCs, and are made from natural ingredients such as water, vegetable oils, plant dyes, and natural minerals. The main binders used in natural paints are: linseed oil (from flax seeds), clay, lime, and milk protein. Lime and milk paints give an authentic period look, and are often used in antique restoration projects. Chalk is used as an extender to thicken paint; turpentine (distilled from pine trees) is used as a solvent; essential oils from citrus fruits (d-limonene) are used as a solvent and fragrance; and natural mineral and earth pigments are used as colorants. The main benefits of natural paints are: Non-toxic - no hazardous fumes or harmful effects on health. This is significant for allergy sufferers and chemically sensitive people who are unable to tolerate chemical paints. Environmentally Friendly - use renewable resources; are biodegradable, can even be composted. Micro-Porous - allow walls and surfaces to breathe, preventing condensation and damp problems, and reducing associated indoor allergens. They are also less prone to paint flaking, peeling and blistering.  On the downside, natural paints can be more expensive because they are made on a smaller scale, although this situation is changing as they become more popular. Natural paints can also take longer to dry (sometimes up to 24 hours or more) and there is less of a colour range to choose from. Natural mineral pigments tend to produce paints that come in pastel shades only, and this has led some natural paint companies to use synthetic pigments to create a more extensive range of colours. Paints for a Sustainable Future Unlike conventional paint companies, natural paint suppliers are committed to making sure their materials derive from sustainable sources and are manufactured in an environmentally friendly way. They aim to minimise pollution, energy and waste throughout the life cycle of their products. Natural paint companies give thought to the recyclable packaging of their products, and they also declare their product ingredients so that consumers know exactly what they are using. Because natural paints are non-toxic, they are completely safe and this makes them the obvious choice for any consumer concerned with protecting the earth and its people for future generations.";

	String[] expectedKeywords = new String[] { "VOC content", "binder",
			"used as solvent", "solvent", "labels", "paint", "voc",
			"natural paints", "natural paint", "modern paints",
			"chemical paints", "nontoxic", "ingredients", "paint companies",
			"still contain", "Natural paints", "Environmentally Friendly",
			"chemical", "toxic", "conventional paint companies", "natural",
			"conventional paint", "pigment" };

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

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
		this.agentMap.put(ExtractKeywordsAgent.NAME, params);

		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		this.agentMap.put(ExtractTfIdfKeywordsAgent.NAME, params);

		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);

		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		this.agentMap.put(ExtractKeyphrasesAgent.NAME, params);

		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		this.agentMap.put(RooloAccessorAgent.class.getName(), params);
		// agentMap.put(OntologyLookupAgent.class.getName(), params);
		this.startAgentFramework(this.agentMap);
	}

	@Override
	@After
	public void tearDown() {
		try {
			this.stopAgentFrameWork();
			super.tearDown();
		} catch (AgentLifecycleException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRun() throws TupleSpaceException {
		VMID queryId = new VMID();
		this.getCommandSpace()
				.write(new Tuple(ExtractKeywordsAgent.EXTRACT_KEYWORDS,
						AgentProtocol.QUERY, queryId.toString(), TEXT, MISSION1));

		Tuple response = this.getCommandSpace().waitToTake(
				new Tuple(ExtractKeywordsAgent.EXTRACT_KEYWORDS,
						AgentProtocol.RESPONSE, queryId.toString(),
						Field.createWildCardField()),
				AgentProtocol.ALIVE_INTERVAL * 16);
		assertNotNull("no response received", response);
		String keywords[] = new String[response.getNumberOfFields() - 3];
		for (int i = 3; i < response.getNumberOfFields(); i++) {
			String keyword = (String) response.getField(i).getValue();
			System.out.print("\"");
			System.out.print(keyword);
			System.out.print("\",");
			keywords[i - 3] = keyword;
		}
		System.out.println();
		assertArrayEquals(expectedKeywords, keywords);
	}
}
