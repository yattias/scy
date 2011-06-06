package eu.scy.agents.conceptmap.proposer;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.util.XMLUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.conceptmap.Edge;
import eu.scy.agents.conceptmap.Graph;
import eu.scy.agents.conceptmap.Node;
import eu.scy.agents.conceptmap.RankedKeywordList;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class CMProposerAgent extends AbstractThreadedAgent {

    public enum EnrichmentStrategy {
        SUB,
        SUPER,
        SIBLING,
        RELATIONS;
    }

    public enum Relation {
        isA,
        hasRelationTo,
        dependsOn,
        hasInfluenceOn,
        decreases,
        increases,
        includes;

        public String getLabel(String language) {
            if (language.equals("de")) {
                switch (this) {
                    case isA:
                        return "ist ein";
                    case hasRelationTo:
                        return "hat Verbindung zu";
                    case dependsOn:
                        return "beruht auf";
                    case hasInfluenceOn:
                        return "hat Einfluss auf";
                    case decreases:
                        return "verringert";
                    case increases:
                        return "erhöht";
                    case includes:
                        return "bezieht ein";
                }
            } else {
                switch (this) {
                    case isA:
                        return "is a";
                    case hasRelationTo:
                        return "has relation to";
                    case dependsOn:
                        return "depends on";
                    case hasInfluenceOn:
                        return "has influence on";
                    case decreases:
                        return "decreases";
                    case increases:
                        return "increases";
                    case includes:
                        return "includes";
                }
            }
            return "";
        }

        public static boolean isKnown(String relation) {
            for (Relation r : Relation.values()) {
                if (r.name().equalsIgnoreCase(relation)) {
                    return true;
                }
            }
            return false;
        }

    }

    private static final String TEXT_DE = "Als globale Erwärmung bezeichnet man den in den vergangenen Jahrzehnten beobachteten Anstieg der Durchschnittstemperatur der erdnahen Atmosphäre und der Meere sowie deren künftig erwartete Erwärmung. Zwischen 1906 und 2005 hat sich die Durchschnittstemperatur in Bodennähe um 0,74 °C (+/- 0,18 °C) erhöht. Das Jahrzehnt von 2000 bis 2009 war mit Abstand das wärmste je gemessene, gefolgt von den 1990er Jahren, die wiederum wärmer waren als die 1980er Jahre.\nNach gegenwärtigem wissenschaftlichen Verständnis ist hierfür 'sehr wahrscheinlich' die Verstärkung des natürlichen Treibhauseffektes durch menschliches Einwirken ursächlich. Die menschengemachte Erwärmung entsteht durch Verbrennen fossiler Brennstoffe, durch weltumfassende Abholzung sowie Land- und Viehwirtschaft. Dadurch wird das Treibhausgas Kohlendioxid (CO2) sowie weitere Treibhausgase wie Methan und Lachgas in der Erdatmosphäre angereichert, so dass weniger Wärmestrahlung von der Erdoberfläche in das Weltall abgestrahlt werden kann.\nDer mit Abstand größte Teil der abgelaufenen wie auch der erwarteten anthropogenen Erwärmung ist auf den bisherigen und bis heute zunehmenden Anstieg des Treibhausgases Kohlendioxid zurückzuführen. Durch starke Rückkopplungsprozesse ist die direkte Wärmewirkung des Kohlendioxids jedoch mit hoher Wahrscheinlichkeit deutlich kleiner als die erwarteten, aus der Erwärmung resultierenden, ebenfalls wärmenden Sekundäreffekte.\nBis zum Jahr 2100 wird, abhängig von künftigen CO2-Emissionen und der tatsächlichen Reaktion des Klimasystems darauf, eine Erwärmung um 1,1 bis 6,4 °C erwartet. Dies hätte eine Reihe von Folgen: Verstärkte Gletscherschmelze, Anstieg des Meeresspiegels, Versauerung der Ozeane, veränderte Niederschlagsmuster, mehr extreme Wetterereignisse, u.a.\nHinsichtlich des projizierten Anstiegs des Meeresspiegels sind noch viele Fragen offen. Die ermittelte Bandbreite des Anstiegs der Meeresspiegel bis zum Ende des 21. Jahrhunderts beträgt je nach Szenario 18 bis 59 Zentimeter. Die Szenarios berücksichtigen allerdings noch nicht die polare Eisdynamik (beispielsweise das Abbrechen großer Eismassen in der Antarktis) und Unsicherheiten in den Klima-Kohlenstoffkreislauf-Rückkopplungen: In einem wärmeren Klima reduziert sich die Aufnahmefähigkeit der Ozeane und der Landoberfläche für vom Menschen verursachtes CO2, weil in wärmerem Wasser weniger Gas gelöst wird und die Böden bei höheren Temperaturen mehr Biomasse abbauen. Damit würden die atmosphärischen CO2-Konzentrationen stärker steigen als ohne diesen Rückkopplungsmechanismus. Ein besseres Verständnis dieser Prozesse und deren Berücksichtigung in künftigen Modellsimulationen könnten zur Projektion höherer Meeresspiegelanstiege bis zum Ende des 21. Jahrhunderts führen.\nFolge der erhöhten CO2-Emissionen ist auch eine zunehmende Versauerung der Ozeane. Die Meere nahmen bisher etwa ein Drittel der vom Menschen verursachten CO2-Emissionen auf, was bereits zu einer signifikanten Versauerung des Meerwassers führte. Eine ungebremste Fortsetzung dieses Trends würde erhebliche Gefahren für das Leben im Meer bergen. Saures Wasser behindert die Kalkbildung, das heißt den Knochen- und Schalenaufbau der Meeresbewohner. Korallenriffe, die ohnehin im wärmeren Wasser unter Stress stehen und alle davon abhängigen Arten sind in ihrer Existenz gefährdet. Das könnte negative Auswirkungen für die gesamte Nahrungskette im Meer haben und damit auch für die menschliche Ernährung.\nDie größte Erwärmung zeigen die Klimaprojektionen über dem Festland und in nördlichen Breiten. Mit der geringsten Erwärmung ist über dem südlichen Ozean und Teilen des Nordatlantiks zu rechnen. Die Modelle ergeben für alle Emissionsszenarios einen Rückgang des Meereises sowohl in der Arktis als auch in der Antarktis. In einigen Projektionen verschwindet in der zweiten Hälfte des 21. Jahrhunderts das Meereis in der Arktis im Sommer fast völlig.\nExtreme Wetterereignisse - wie Hitzewellen und starke Niederschläge - werden sehr wahrscheinlich weiterhin zunehmen. Darüber hinaus ist es wahrscheinlich (Eintrittswahrscheinlichkeit > 66 Prozent), dass tropische Wirbelstürme künftig intensiver werden und höhere Spitzenwindgeschwindigkeiten sowie mehr starke Niederschläge mit sich bringen werden.\nNach gegenwärtigen Erkenntnissen wird das Abschmelzen des Grönländischen Eisschildes nach 2100 weiterhin zum Anstieg des Meeresspiegels beitragen. Es besteht die Gefahr, dass bei einer Zunahme der globalen Durchschnittstemperatur von mehr als 2°C gegenüber dem vorindustriellen Wert das Grönländische Eisschild über Jahrhunderte hinweg vollständig abschmilzt und langfristig zu einem Anstieg des Meeresspiegels von etwa sieben Metern führt. Die Antarktis ist - nach gegenwärtigem Kenntnisstand - hingegen zu kalt für ein verbreitetes Abschmelzen des Festlandeises.\nDie Vielzahl der Auswirkungen, die sich je nach Ausmaß der Erwärmung ergeben, ist jedoch kaum abschätzbar. Die große Schwankungsbreite der Temperaturprognosen ist dabei weniger auf ein fehlendes Verständnis der natürlichen Faktoren, als viel mehr der unbekannten Reaktion der Menschheit auf die sich verändernden Bedingungen zuzurechnen. Die menschlich beschleunigte Erwärmung und der Anstieg des Meeresspiegels würden sich über Jahrhunderte fortsetzen, selbst falls es gelänge, die Konzentration der Treibhausgase zu stabilisieren. Dies liegt an der langen, mit Klimaprozessen und Rückkopplungen im Klimasystem verbundenen Zeitskala.\nDie Vermeidung dieser Phänomene wie auch die Anpassung an die zu erwartende Erwärmung, sind Ziele nationaler und internationaler Klimapolitik. Der wissenschaftliche Erkenntnisstand zur globalen Erwärmung wird durch den Intergovernmental Panel on Climate Change (IPCC, im Deutschen oft als Weltklimarat bezeichnet) diskutiert und zusammengefasst. Die Analysen des Weltklimarates, dessen Vierter Sachstandsbericht 2007 veröffentlicht wurde, bilden den Forschungsstand über menschliche Einflussnahmen auf das Klimasystem der Erde ab. Sie sind eine Hauptgrundlage der politischen und wissenschaftlichen Diskussion des Themas wie auch der Aussagen dazu in diesem Artikel. Die Darstellung des Weltklimarates und die daraus zu ziehenden Folgerungen stehen zugleich im Mittelpunkt der Kontroverse um die globale Erwärmung.\nAusgehend von dem Bericht des Weltklimarates, stellt die Klimapolitik der Europäischen Union ausdrücklich das sog. Zwei-Grad-Ziel in den Mittelpunkt und versucht, einen Beitrag dazu zu leisten, dass der Anstieg der globalen Durchschnittstemperatur nicht über 2 Grad Celsius hinausgeht.\nDer Gesamtausstoß an Treibhausgasen in der EU soll nach geltender Rechtslage bis 2020 um 20 Prozent gegenüber dem Basisjahr 1990 gesenkt werden. Derzeit diskutiert die EU, ihr Reduktionsziel für 2020 auf 30 Prozent zu erhöhen, auch ohne vergleichbare Selbstverpflichtungen weiterer Industrie- und Schwellenländer.\nDas im Dezember 2008 verabschiedete Klimapaket sieht Maßnahmen zur Intensivierung des europäischen Emissionshandels vor. Von 2013 an gilt ein gemeinsames CO2-Budget für alle Mitgliedstaaten. Während die (westeuropäische) Energiewirtschaft die CO2-Zertifikate von 2013 an bereits vollständig ersteigern muss, werden Raffinerien, Chemieindustrie und Fluglinien noch bis 2020 teilweise kostenlos Zertifikate zugeteilt bekommen.";

    private static final String TEXT_EN = "Global warming refers to the increase observed in recent decades the average temperature of the lower atmosphere and the oceans and their expected future warming. Between 1906 and 2005, the average temperature near the ground by 0.74 ° C (+ / - 0.18 ° C) has increased. The decade 2000-2009 was by far the warmest on record, followed by the 1990s, which in turn were warmer than the 1980s. According to current scientific understanding of this is 'very likely' the reinforcement of the natural greenhouse effect of human influence cause. The man-made warming caused by burning fossil fuels, deforestation and world-embracing agriculture and livestock. As a result, the greenhouse gas carbon dioxide (CO2) and other greenhouse gases like methane and nitrous oxide accumulates in the atmosphere so that less heat radiation emitted by the Earth's surface into space. By far the largest part of the past and expected anthropogenic warming is due to the recent and still growing increase of the greenhouse gas carbon dioxide. Through strong feedback processes is the direct heating effect of the carbon dioxide but with significantly less likely than expected, resulting from the heating, warming also secondary effects. By the year 2100, depending on future CO2 emissions and the actual response of the climate system to ensuring expect a warming of 1.1 to 6.4 ° C. This would have a number of consequences: increased glacier melt, sea level rise, ocean acidification, changes in precipitation patterns, more extreme weather events, including With regard to the projected rise in sea level, many questions remain unanswered. The measured bandwidth of the rise in sea level by the end of the 21 Century is, depending on the scenario, 18 to 59 centimeters. The scenarios take into account but not the polar ice dynamics (eg, the collapse of large ice sheets in Antarctica) and uncertainties in climate-carbon cycle feedbacks: In a warmer climate will reduce the capacity of the oceans and the land surface of man-made CO2, because in warmer water, less gas is dissolved and remove the soil at higher temperatures, more biomass. So that the atmospheric CO2 concentration would increase more than without the feedback mechanism. A better understanding of these processes and their consideration in future model simulations for the projection of higher sea levels could rise by the end of the 21 Century lead. Result of increased CO2 emissions is an increasing acidification of the oceans. The seas were so far to about a third of man-made CO2 emissions, which has already led to a significant acidification of the seawater. Unabated continuation of this trend would pose significant risks to marine life. Acidic water prevents calcification, that is the bone and shell structure of marine life. Coral reefs that are already in the warmer water under stress and all dependent species are threatened in their existence. This could have negative implications for the entire marine food chain and thus also for human nutrition. The largest warming of the climate projections show on the mainland and in northern latitudes. With the least warming over the Southern Ocean and parts of the North Atlantic is expected. The models shown for all emission scenarios a decline of sea ice in both the Arctic and the Antarctic. In some projections, disappears in the second half of the 21 Century, the sea ice in the Arctic in summer almost completely. Extreme weather events - such as heat waves and heavy rain - are likely to increase. In addition, it is likely (probability> 66 percent), that tropical cyclones are more intense in the future and bring higher peak wind velocities and more heavy rainfalls are. According to current knowledge, the melting of the Greenland ice sheet after 2100 will continue to contribute to sea level rise. There is a risk that an increase in average global temperature of more than 2 ° C above pre-industrial value of the Greenland ice sheet melts completely for centuries and long term leads to a rise in sea level of about seven meters. Antarctica is - according to present knowledge - however, too cold for widespread melting of continental ice. The variety of effects that arise depending on the degree of warming, but is almost impossible to quantify. The large variation in temperature predictions is not so much a lack of understanding of the natural factors, as much more attributable to the unknown reaction of humanity to the changing conditions. The human accelerated warming and sea level rise would continue for centuries even if it were possible to stabilize the concentration of greenhouse gases. This is due to the long, with climate processes and feedbacks in the climate system associated time scale. The prevention of these phenomena and the adaptation to the expected warming, objectives of national and international climate policy. The scientific evidence for global warming by the Intergovernmental Panel on Climate Change (IPCC, the Germans often referred to as the IPCC) are discussed and summarized. The analysis of the IPCC, whose Fourth Assessment Report 2007, published form, from the state of research on human influences on the climate system. They are an essential basis of political and scientific discussion of the topic as well as to the statements in this article. The presentation of the IPCC and the conclusions to be drawn conclusions are also central to the controversy over global warming. Based on the report of the IPCC, is the climate policy of the European Union explicitly the so-called two-degree target in the center and tries to make a contribution to the rise in global average temperature does not exceed 2 degrees Celsius. The total emissions of greenhouse gases in the EU shall be reduced by 20 percent compared with 1990 according to current law in 2020. Currently discussing the EU to increase its reduction target for 2020 to 30 percent, even without similar commitments of other developed and developing countries. The climate package adopted in December 2008 provides for measures to strengthen the European emissions trading. From 2013 to a common CO2 budget applies to all Member States. While the (Western) Energy CO2 allowances from 2013 to bid has been completely, refineries, chemical industry and airlines will have until 2020 to get some free allowances allocated.";

    private static final String ECO_TEXT_EN = "Ecosystems We're All in This Together Ducks on a lake Everything in the natural world is connected. An ecosystem is a community of living and non-living things that work together. Ecosystems have no particular size. An ecosystem can be as large as a desert or a lake or as small as a tree or a puddle. If you have a terrarium, that is an artificial ecosystem. The water, water temperature, plants, animals, air, light and soil all work together. If there isn't enough light or water or if the soil doesn't have the right nutrients, the plants will die. If the plants die, animals that depend on them will die. If the animals that depend on the plants die, any animals that depends on those animals will die. Ecosystems in nature work the same way. All the parts work together to make a balanced system! The More the Merrier Frog A healthy ecosystem has lots of species diversity and is less likely to be seriously damaged by human interaction, natural disasters and climate changes. Every species has a niche in its ecosystem that helps keep the system healthy. We are learning about new species every day, and we are just figuring out the roles they play in the natural world. By studying and maintaining biodiversity, we help keep our planet healthy. Life in a Lake Lake In a lake ecosystem, the sun hits the water and helps the algae grow. Algae produces oxygen for animals like fish, and provides food for microscopic animals. Small fish eat the microscopic animals, absorb oxygen with their gills and expel carbon dioxide, which plants then use to grow. If the algae disappeared, everything else would be impacted. Microscopic animals wouldn't have enough food, fish wouldn't have enough oxygen and plants would lose some of the carbon dioxide they need to grow. Getting Along Deer Ecosystems have lots of different living organisms that interact with each other. The living organisms in an ecosystem can be divided into three categories: producers, consumers and decomposers. They are all important parts of an ecosystem. Producers are the green plants. They make their own food. Consumers are animals and they get their energy from the producers or from organisms that eat producers. There are three types of consumers:  herbivores are animals that  eat plants,  carnivores are animals that eat herbivores and sometimes other carnivores and omnivores are animals that eat plants and other animals. The third type of living organism in an ecosystem are the decomposers. Decomposers are plants and animals that break down dead plants and animals into organic materials that go back into the soil. Which is where we started! Parts and Pieces sunlight What are the major parts of an ecosystem? An ecosystem includes soil, atmosphere, heat and light from the sun, water and living organisms. Getting Down and Dirty Dirt Soil is a critical part of an ecosystem. It provides important nutrients for the plants in an ecosystem. It helps anchor the plants to keep them in place. Soil absorbs and holds water for plants and animals to use and provides a home for lots of living organisms. Give Me a Little Air Clouds The atmosphere provides oxygen and carbon dioxide for the plants and animals in an ecosystem. The atmosphere is also part of the water cycle. Without the complex interactions and elements in the atmosphere, there would be no life at all! Getting Some Sun Flowers The heat and light from the sun are critical parts of an ecosystem. The sun's heat helps water evaporate and return to the atmosphere where it is cycled back into water. The heat also keeps plants and animals warm. Without light from the sun there would be no photosynthesis and plants wouldn't have the energy they need to make food. Water Everywhere Water Without water there would be no life. Water is a large percentage of the cells that make up all living organisms. In fact, you may have heard that humans can go longer without food than they can without water. It's true!  Without water all life would die. In addition to being an important part of cells, water is also used by plants to carry and distribute the nutrients they need to survive.";

    private OntologyConnection con;

    private EnrichmentStrategy[] strategies = new EnrichmentStrategy[] { EnrichmentStrategy.SUPER, EnrichmentStrategy.RELATIONS, EnrichmentStrategy.SIBLING };

    private TupleSpace commandSpace;

    private CMProposerObserver observer;

    public static void main(String[] args) throws Exception {
        TupleSpace ts = new TupleSpace("scy.collide.info", 2525, "http://www.scy.eu/ecosystem#");
        Tuple[] tuples = ts.readAll(new Tuple(Field.createWildCardField(), Field.createSemiformalField("*http://www.w3.org/2001/XMLSchema#string^^http://www.w3.org/2001/XMLSchema#string")));
        for (Tuple t : tuples) {
            String s = t.getField(3).getValue().toString();
            s = s.substring(0, s.length() - "^^http://www.w3.org/2001/XMLSchema#string".length());
             System.out.println(s);
            t.getField(3).setValue(s);
            ts.update(t.getTupleID(), t);
        }
        tuples = ts.readAll(new Tuple(Field.createWildCardField(), Field.createSemiformalField("*^^http://www.w3.org/2001/XMLSchema#string")));
        for (Tuple t : tuples) {
            String s = t.getField(3).getValue().toString();
            if (!s.startsWith("\"")) {
                s = "\"" + s.substring(0, s.length() - "^^http://www.w3.org/2001/XMLSchema#string".length()) + "\"^^http://www.w3.org/2001/XMLSchema#string";
                 System.out.println(s);
                t.getField(3).setValue(s);
                ts.update(t.getTupleID(), t);
    }
        }
        tuples = ts.readAll(new Tuple(Field.createWildCardField(), Field.createSemiformalField("*de")));
        for (Tuple t : tuples) {
            // System.out.println(t);
        }
        ts.disconnect();
        // HashMap<String, Object> map = new HashMap<String, Object>();
        // map.put(AgentProtocol.PARAM_AGENT_ID, "proposer id");
        // map.put(AgentProtocol.TS_HOST, "localhost");
        // map.put(AgentProtocol.TS_PORT, 2525);
        // CMProposerAgent agent = new CMProposerAgent(map);
        // agent.start();
    }

    public CMProposerAgent(Map<String, Object> map) {
        super(CMProposerAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        if (map.get("observer") != null) {
            this.observer = (CMProposerObserver) map.get("observer");
        } else {
            this.observer = new NullObserver();
        }
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }

        Arrays.sort(strategies);
        // default, but will adapt at each call
        con = new SWATConnection("en", "http://www.scy.eu/ecosystem#");
    }

    @Override
    protected void doRun() throws TupleSpaceException {

        if (!(observer instanceof NullObserver)) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        while (status == Status.Running) {
                            sendAliveUpdate();
                            Thread.sleep(5000);
                        }

                    } catch (TupleSpaceException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        while (status == Status.Running) {
            observer.setStatusText("Agent running");
            if ((observer instanceof NullObserver)) {
                sendAliveUpdate();
            }
            Tuple returnTuple = commandSpace.waitToTake(new Tuple(String.class, "CMProposer", "cm proposal", String.class, String.class, Integer.class, String.class), AgentProtocol.COMMAND_EXPIRATION);
            if (returnTuple != null) {
                observer.clearState();
                observer.setStatusText("Received request ...");
                commandSpace.write(generateResponse(returnTuple));
            }
        }
    }

    private Tuple generateResponse(Tuple returnTuple) {
        String id = returnTuple.getField(0).getValue().toString();
        String user = returnTuple.getField(3).getValue().toString();
        String elouri = returnTuple.getField(4).getValue().toString();
        int amount = (Integer) returnTuple.getField(5).getValue();
        String centralityAlgorithm = returnTuple.getField(6).getValue().toString();
        Field[] proposals = getProposals(user, elouri, amount, centralityAlgorithm);
        Tuple t = new Tuple(id, "response");
        for (Field proposal : proposals) {
            t.add(proposal);
        }
        observer.setStatusText("Responding with proposals");
        return t;
    }

    @Override
    public void doStop() throws AgentLifecycleException {
        observer.setStatusText("Stopping....");
        con.close();
        try {
            commandSpace.disconnect();
            observer.setStatusText("Stopped");
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        return null;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    private Field[] getProposals(String user, String elouri, int amount, String centralityAlgorithm) {
        String ontologyNS = getOntologyNamespace(user);
        String lang = determineLanguage(user);
        if (!con.getLanguage().equals(lang) || !con.getOntologyNamespace().equals(ontologyNS)) {
            con.close();
            con = new SWATConnection(lang, ontologyNS);
        }

        observer.setStatusText("Retrieving text");
        String text = getText(user);
        observer.setCMText(text);

        observer.setStatusText("Determining keywords");
        Map<String, Double> keywordsFromText = getKeywordsFromText(text, ontologyNS);

        observer.setStatusText("Retrieving student concept map");
        Graph userGraph = getUserGraph(elouri, user);
        observer.foundStudentsMap(userGraph);

        Graph ontologyGraph = findOntologySubgraph(keywordsFromText.keySet(), ontologyNS);

        RankedKeywordList rankedStrings = new RankedKeywordList();
        if (centralityAlgorithm.equals("degree")) {
            TreeMap<Double, Set<Node>> rankedNodes = ontologyGraph.getNodeDegree();
            for (Entry<Double, Set<Node>> e : rankedNodes.entrySet()) {
                for (Node n : e.getValue()) {
                    double ranking = e.getKey() * keywordsFromText.get(n.getLabel());
                    rankedStrings.put(ranking, n.getLabel());
                }
            }
        }

        List<String> conceptProposals = getConceptProposals(userGraph, rankedStrings);
        if (conceptProposals.size() > amount) {
            conceptProposals = conceptProposals.subList(0, amount);
        }

        RankedKeywordList relationProposals = getRelationProposals(ontologyGraph, userGraph, rankedStrings);

        ArrayList<Field> result = new ArrayList<Field>();
        for (String s : conceptProposals) {
            observer.markConceptAsProposal(s);
            result.add(new Field("concept_proposal=" + s));
        }
        List<String> relationProposalList = relationProposals.getKeywords();
        for (int i = 0; i < amount && i < relationProposalList.size(); i++) {
            String s = relationProposalList.get(i);
            if (relationProposals.getWeightFor(s) > 0) {
                observer.markRelationAsProposal(s);
                result.add(new Field("relation_proposal=" + s));
            }
        }
        for (int i = 0; i < amount && i < relationProposalList.size(); i++) {
            String s = relationProposalList.get(relationProposalList.size() - 1 - i);
            if (relationProposals.getWeightFor(s) < 0) {
                observer.markRelationAsProposal(s);
                result.add(new Field("relation_specification_proposal=" + s));
            }
        }

        return (Field[]) result.toArray(new Field[result.size()]);
    }

    private RankedKeywordList getRelationProposals(Graph ontologyGraph, Graph userGraph, RankedKeywordList rankedStrings) {
        observer.setStatusText("Find relation proposals");
        RankedKeywordList list = new RankedKeywordList();
        try {
            HashSet<String> userNodes = new HashSet<String>();
            for (Node n : userGraph.getNodes()) {
                userNodes.add(n.getLabel().toLowerCase());
            }
            String delim = ";";
            HashSet<String> foundRelations = new HashSet<String>();
            Map<String, Set<String>> relationHierarchy = con.getRelationHierarchy();
            for (Edge e : ontologyGraph.getEdges()) {
                String fromLabel = e.getFromNode().getLabel();
                String toLabel = e.getToNode().getLabel();
                if (userNodes.contains(fromLabel) && userNodes.contains(toLabel)) {
                    Edge e1 = userGraph.getEdgeForLabels(fromLabel, toLabel, true);
                    Edge e2 = userGraph.getEdgeForLabels(toLabel, fromLabel, true);
                    if (e1 == null && e2 == null) {
                        if (fromLabel.compareTo(toLabel) > 0) {
                            String tmp = toLabel;
                            toLabel = fromLabel;
                            fromLabel = tmp;
                        }
                        if (!foundRelations.contains(fromLabel + delim + toLabel)) {
                            list.put(rankedStrings.getWeightFor(fromLabel) + rankedStrings.getWeightFor(toLabel), fromLabel + delim + toLabel);
                            foundRelations.add(fromLabel + delim + toLabel);
                        }
                    } else {
                        Edge foundEdge = (e1 != null) ? e1 : e2;
                        String userRelation = foundEdge.getLabel();
                        String ontologyRelation = e.getLabel();
                        if (relationHierarchy.get(userRelation).contains(ontologyRelation)) {
                            if (!foundRelations.contains(fromLabel + delim + toLabel)) {
                                list.put(-rankedStrings.getWeightFor(fromLabel) - rankedStrings.getWeightFor(toLabel), fromLabel + delim + toLabel);
                                foundRelations.add(fromLabel + delim + toLabel);
                            }
                        }
                    }
                }
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> getConceptProposals(Graph userGraph, RankedKeywordList rankedStrings) {
        observer.setStatusText("Find concept proposals");
        List<String> ontologyConcepts = new ArrayList<String>();
        ontologyConcepts.addAll(rankedStrings.getKeywords());
        for (Node n : userGraph.getNodes()) {
            ontologyConcepts.remove(n.getLabel().toLowerCase());
            observer.markConceptAsMatching(n.getLabel());
        }
        return ontologyConcepts;
    }

    private String getText(String user) {
//        return ECO_TEXT_EN;
        try {
            Tuple t = getSessionSpace().read(new Tuple("tool", user, "webresource", String.class));
            if (t == null) {
                // dude, no webresourcer open!
                return "";
            }
            String eloUri = t.getField(3).getValue().toString();
            String id = new VMID().toString();
            commandSpace.write(new Tuple(id, "roolo-agent", "elo", eloUri));
            t = commandSpace.waitToTakeFirst(new Tuple(id, "roolo-response", String.class));
            Document doc = XMLUtils.parseString(t.getField(2).getValue().toString());
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList highlights = (NodeList) xPath.evaluate("/elo/content/webresource/annotations/document/quotes/quote", doc, XPathConstants.NODESET);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < highlights.getLength(); i++) {
                sb.append(highlights.item(i).getTextContent());
            }
            return sb.toString();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        // some error
        return "";
    }

    private Graph getUserGraph(String elouri, String user) {
        Graph g = new Graph();
        try {
            String nid = new VMID().toString();
            String eid = new VMID().toString();
            commandSpace.write(new Tuple(nid, "userconceptmapagent", "conceptmap", user, elouri, "nodes"));
            commandSpace.write(new Tuple(eid, "userconceptmapagent", "conceptmap", user, elouri, "edges"));
            Tuple userNodesTuple = null;
            Tuple userEdgesTuple = null;
            for (int i = 0; userNodesTuple == null && i < 30; i++) {
                userNodesTuple = commandSpace.waitToTake(new Tuple(nid, "response", Field.createWildCardField()), 1000);
            }
            for (int i = 0; userEdgesTuple == null && i < 30; i++) {
                userEdgesTuple = commandSpace.waitToTake(new Tuple(eid, "response", Field.createWildCardField()), 1000);
            }
            Field[] nodeFields = (userNodesTuple == null) 
            				? new Field[0]
            				: new Field[userNodesTuple.getNumberOfFields() - 2];
            System.arraycopy(userNodesTuple.getFields(), 2, nodeFields, 0, nodeFields.length);
            Field[] edgesFields = (userEdgesTuple == null) 
            				? new Field[0] 
            	            : new Field[userEdgesTuple.getNumberOfFields() - 2]; 
            System.arraycopy(userEdgesTuple.getFields(), 2, edgesFields, 0, edgesFields.length);
            g.fillFromFields(edgesFields, nodeFields);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return g;
    }

    private String getOntologyNamespace(String user) {
        try {
            Tuple t = getSessionSpace().read(new Tuple("mission", user, String.class, String.class));
            if (t != null) {
                String missionString = (String) t.getField(3).getValue();
                return Mission.getForName(missionString).getNamespace();
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return Mission.UNKNOWN_MISSION.getNamespace();
    }

    private String determineLanguage(String user) {
        try {
            Tuple t = getSessionSpace().read(new Tuple("language", user, String.class));
            if (t != null) {
                String languageString = (String) t.getField(2).getValue();
                return languageString;
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return "en";
    }

    public Map<String, Double> getKeywordsFromText(String text, String namespace) {
        HashSet<String> stopwords = new HashSet<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/ger_stopwords.txt")));
            String buffer = null;
            while ((buffer = br.readLine()) != null) {
                try {
                    stopwords.add(Stemmer.stem(buffer));
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> ontologyTerms = getOntologyTerms(namespace);
        HashMap<String, Set<String>> ontologySingleTerms = new HashMap<String, Set<String>>();
        for (String stemmedTerm : ontologyTerms.keySet()) {
            if (stemmedTerm.contains(" ")) {
                for (String s : stemmedTerm.split(" ")) {
                    if (!stopwords.contains(s)) {
                        Set<String> set = ontologySingleTerms.get(s);
                        if (set == null) {
                            set = new HashSet<String>();
                            ontologySingleTerms.put(s, set);
                        }
                        set.add(ontologyTerms.get(stemmedTerm));
                    }
                }
            } else {
                Set<String> set = ontologySingleTerms.get(stemmedTerm);
                if (set == null) {
                    set = new HashSet<String>();
                    ontologySingleTerms.put(stemmedTerm, set);
                }
                set.add(ontologyTerms.get(stemmedTerm));
            }
        }

        Map<String, Set<String>> ontologyClouds = getOntologyClouds(namespace);
        Map<String, Double> keywords = new HashMap<String, Double>();
        for (String term : text.split(" ")) {
            String stemmed = Stemmer.stem(term);
            if (ontologyClouds.containsKey(stemmed)) {
                for (String keyword : ontologyClouds.get(stemmed)) {
                    keywords.put(keyword.toLowerCase(), 0.2);
                    // // System.out.println("cloud: " + keyword);
                    observer.foundTextKeyword(term);
                }
            }
            if (ontologySingleTerms.containsKey(stemmed)) {
                for (String keyword : ontologySingleTerms.get(stemmed)) {
                    keywords.put(keyword.toLowerCase(), 0.6);
                    // // System.out.println("ontology-stemmed: " + keyword);
                }
                observer.foundTextKeyword(term);
            }
        }
        for (String oTerm : ontologyTerms.values()) {
            if (text.toLowerCase().contains(oTerm.toLowerCase())) {
                keywords.put(oTerm.toLowerCase(), 1.0);
                observer.foundTextKeyword(oTerm.toLowerCase());
                // // System.out.println("ontology-full: " + oTerm);
            }
        }

        return keywords;
    }

    private Graph findConnections(List<String> terms, String namespace) {
        Graph g = new Graph();
        for (String term : terms) {
            try {
                String category = con.lookupEntityType(term, namespace);
                if ("individual".equals(category)) {
                    findConceptsOfInstance(term.toLowerCase(), g, terms, namespace);
                } else if ("class".equals(category)) {
                    findConceptsOfClass(term.toLowerCase(), g, terms, namespace);
                }
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        return g;
    }

    private Map<String, String> getOntologyTerms(String ontologyNamespace) {
        Map<String, String> term2Stem = new HashMap<String, String>();
        try {
            String[] terms = con.getOntologyTerms(ontologyNamespace);
            for (String term : terms) {
                term2Stem.put(Stemmer.stemWordWise(term), term);
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return term2Stem;
    }

    private Map<String, Set<String>> getOntologyClouds(String namespace) {
        Map<String, Set<String>> keyword2Concept = new HashMap<String, Set<String>>();
        try {
            Map<String, Set<String>> unstemmedKeywords2Clouds = con.getOntologyClouds(namespace);
            for (Entry<String, Set<String>> e : unstemmedKeywords2Clouds.entrySet()) {
                String keyword = Stemmer.stemWordWise(e.getKey());
                Set<String> clouds = keyword2Concept.get(keyword);
                if (clouds == null) {
                    clouds = new HashSet<String>();
                    keyword2Concept.put(keyword, clouds);
                }
                clouds.addAll(e.getValue());
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return keyword2Concept;
    }

    public Graph findOntologySubgraph(Set<String> terms, String ontologyNamespace) {
        observer.setStatusText("Retrieving ontology conceps");
        Map<String, String> ontologyTerms = getOntologyTerms(ontologyNamespace);
        List<String> termsFound = new ArrayList<String>();
        for (String term : terms) {
            String stemmedTerm = ontologyTerms.get(Stemmer.stemWordWise(term));
            if (stemmedTerm != null) {
                termsFound.add(term);
                observer.foundOntoConcept(term);
            }
        }
        observer.setStatusText("Retrieving ontology relations");
        Graph g = findConnections(termsFound, ontologyNamespace);
        for (Edge e : g.getEdges()) {
            observer.foundOntoRelation(e.getFromNode().getLabel(), e.getToNode().getLabel(), e.getLabel());
        }
        return g;
    }

    private void findConceptsOfInstance(String entity, Graph g, List<String> terms, String ontologyNamespace) throws TupleSpaceException {
        String[] classes = null;
        String[][] propValues = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            classes = con.getClassesOfInstance(entity, ontologyNamespace);
            for (String clazz : classes) {
                if (terms.contains(clazz.toLowerCase())) {
                    g.addNodeAndEdges(entity, Relation.isA.getLabel(con.getLanguage()), clazz.toLowerCase());
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
            propValues = con.getPropValuesOfInstance(entity, ontologyNamespace);
            for (String[] pv : propValues) {
                if (pv != null && pv.length == 2 && terms.contains(pv[1].toLowerCase()) && Relation.isKnown(pv[0])) {
                    g.addNodeAndEdges(entity, Relation.valueOf(pv[0]).getLabel(con.getLanguage()), pv[1].toLowerCase());
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0 || Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
            if (classes == null) {
                classes = con.getClassesOfInstance(entity, ontologyNamespace);
            }
            for (String clazz : classes) {
                if (Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
                    String[] instances = con.getInstancesOfClass(clazz, ontologyNamespace);
                    if (instances.length > 0 && instances[0].length() > 0) {
                        for (String i : instances) {
                            if (!i.equalsIgnoreCase(entity) && terms.contains(i.toLowerCase())) {
                                g.addNodeAndEdges(entity, Relation.hasRelationTo.getLabel(con.getLanguage()), i.toLowerCase());
                            }
                        }
                    }
                }
                // if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
                // String[] subClasses = con.getSubclassesOfClass(clazz, ontologyNamespace);
                // if (subClasses.length > 0 && subClasses[0].length() > 0) {
                // for (String sc : subClasses) {
                // if (terms.contains(sc.toLowerCase())) {
                // g.addNodeAndEdges(entity, "hasSubSibling", sc.toLowerCase());
                // }
                // }
                // }
                // }
            }
        }
    }

    private void findConceptsOfClass(String entity, Graph g, List<String> terms, String ontologyNamespace) throws TupleSpaceException {
        String[] instances = null;
        String[] superClasses = null;
        String[] subClasses = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
            instances = con.getInstancesOfClass(entity, ontologyNamespace);
            if (instances.length > 0 && instances[0].length() > 0) {
                for (String i : instances) {
                    if (terms.contains(i.toLowerCase())) {
                        g.addNodeAndEdges(entity, "hasExample", i.toLowerCase());
                    }
                }
            }
            subClasses = con.getSubclassesOfClass(entity, ontologyNamespace);
            if (subClasses.length > 0 && subClasses[0].length() > 0) {
                for (String sc : subClasses) {
                    if (terms.contains(sc.toLowerCase())) {
                        g.addNodeAndEdges(entity, "has subclass", sc.toLowerCase());
                    }
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            superClasses = con.getSuperclassesOfClass(entity, ontologyNamespace);
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    if (terms.contains(sc.toLowerCase())) {
                        g.addNodeAndEdges(entity, Relation.isA.getLabel(con.getLanguage()), sc.toLowerCase());
                    }
                }
            }
        }
        // if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
        // if (instances == null) {
        // instances = con.getInstancesOfClass(entity, ontologyNamespace);
        // }
        // if (instances.length > 0 && instances[0].length() > 0) {
        // for (String i : instances) {
        // String[][] propValues = con.getPropValuesOfInstance(i, ontologyNamespace);
        // for (String[] pv : propValues) {
        // if (pv != null && pv.length == 2 && terms.contains(pv[1].toLowerCase())) {
        // g.addNodeAndEdges(entity, "maybe " + pv[0], pv[1].toLowerCase());
        // }
        // }
        // }
        // }
        // }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    subClasses = con.getSubclassesOfClass(sc, ontologyNamespace);
                    for (String subc : subClasses) {
                        if (!subc.equals(entity) && terms.contains(subc.toLowerCase())) {
                            g.addNodeAndEdges(entity, Relation.hasRelationTo.getLabel(con.getLanguage()), subc.toLowerCase());
                        }
                    }
                }
            }
        }
    }

}
