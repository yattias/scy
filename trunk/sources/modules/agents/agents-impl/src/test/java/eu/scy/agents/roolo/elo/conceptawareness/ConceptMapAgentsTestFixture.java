package eu.scy.agents.roolo.elo.conceptawareness;

import java.util.Arrays;
import java.util.List;

import eu.scy.agents.AbstractTestFixture;

public class ConceptMapAgentsTestFixture extends AbstractTestFixture {

	public static final String CONCEPT_MAP_CONTENT = "<conceptmap>\n"
			+ "  <nodes>\n"
			+ "    <node id=\"28208946\" name=\"total moment\" xpos=\"304\" ypos=\"681\"/>\n"
			+ "    <node id=\"6051961\" name=\"moment child 1\" xpos=\"203\" ypos=\"517\"/>\n"
			+ "    <node id=\"13768021\" name=\"force\" xpos=\"298\" ypos=\"184\"/>\n"
			+ "    <node id=\"19971724\" name=\"moment child 2\" xpos=\"362\" ypos=\"516\"/>\n"
			+ "    <node id=\"18791494\" name=\"mass\" xpos=\"296\" ypos=\"24\"/>\n"
			+ "    <node id=\"29873045\" name=\"moment\" xpos=\"299\" ypos=\"358\"/>\n"
			+ "    <node id=\"28208946\" name=\"total moment\" xpos=\"304\" ypos=\"681\"/>\n"
			+ "    <node id=\"13768021\" name=\"force\" xpos=\"298\" ypos=\"184\"/>\n"
			+ "    <node id=\"18791494\" name=\"mass\" xpos=\"296\" ypos=\"24\"/>\n"
			+ "    <node id=\"29873045\" name=\"moment\" xpos=\"299\" ypos=\"358\"/>\n"
			+ "    <node id=\"11136287\" name=\"balance state\" xpos=\"617\" ypos=\"682\"/>\n"
			+ "    <node id=\"19971724\" name=\"moment child 2\" xpos=\"362\" ypos=\"516\"/>\n"
			+ "    <node id=\"6051961\" name=\"moment child 1\" xpos=\"203\" ypos=\"517\"/>\n"
			+ "    <node id=\"11136287\" name=\"balance state\" xpos=\"617\" ypos=\"682\"/>\n"
			+ "    <node id=\"20639637\" name=\"distance\" xpos=\"624\" ypos=\"206\"/>\n"
			+ "    <node id=\"20639637\" name=\"distance\" xpos=\"624\" ypos=\"206\"/>\n"
			+ "  </nodes>\n"
			+ "  <links>\n"
			+ "    <link from=\"19971724\" id=\"ID-1238181132839\" label=\"-\" to=\"28208946\" />\n"
			+ "    <link from=\"20639637\" id=\"ID-1238180980021\" label=\"+\" to=\"29873045\" />\n"
			+ "    <link from=\"19971724\" id=\"ID-1238181084173\" label=\"is a\" to=\"29873045\" />\n"
			+ "    <link from=\"6051961\" id=\"ID-1238181065678\" label=\"is a\" to=\"29873045\" />\n"
			+ "    <link from=\"20639637\" id=\"ID-1238180921348\" label=\"+\" to=\"13768021\" />\n"
			+ "    <link from=\"18791494\" id=\"ID-1238180900951\" label=\"+\" to=\"13768021\" />\n"
			+ "    <link from=\"13768021\" id=\"ID-1238180965087\" label=\"+\" to=\"29873045\" />\n"
			+ "    <link from=\"13768021\" id=\"ID-1238181206589\" label=\"13768021-13768021\" to=\"13768021\" />\n"
			+ "    <link from=\"28208946\" id=\"ID-1238181177527\" label=\"Determines\" to=\"11136287\" />\n"
			+ "    <link from=\"6051961\" id=\"ID-1238181123288\" label=\"+\" to=\"28208946\" />\n"
			+ "  </links>\n" + "</conceptmap>";
	public static final String CONCEPT_MAP1 = "<conceptmap>\n"
			+ "  <nodes>\n"
			+ "    <node id=\"25507275\" name=\"total moment\" xpos=\"443\" ypos=\"509\"/>\n"
			+ "    <node id=\"21773414\" name=\"moment child 1\" xpos=\"261\" ypos=\"309\"/>\n"
			+ "    <node id=\"21214799\" name=\"moment child 2\" xpos=\"621\" ypos=\"306\"/>\n"
			+ "    <node id=\"837138\" name=\"moment\" xpos=\"415\" ypos=\"83\"/>\n"
			+ "    <node id=\"25507275\" name=\"total moment\" xpos=\"443\" ypos=\"509\"/>\n"
			+ "    <node id=\"21214799\" name=\"moment child 2\" xpos=\"621\" ypos=\"306\"/>\n"
			+ "  </nodes>\n"
			+ "  <links>\n"
			+ "    <link from=\"21214799\" id=\"ID-1238181943959\" label=\"is a\" to=\"837138\"/>\n"
			+ "    <link from=\"21214799\" id=\"ID-1238181998609\" label=\"+\" to=\"25507275\"/>\n"
			+ "    <link from=\"21773414\" id=\"ID-1238182022119\" label=\"equals\" to=\"21214799\"/>\n"
			+ "    <link from=\"21773414\" id=\"ID-1238181986379\" label=\"+\" to=\"25507275\"/>\n"
			+ "    <link from=\"21773414\" id=\"ID-1238181922662\" label=\"is a\" to=\"837138\"/>\n"
			+ "  </links>\n" + "</conceptmap>\n";

	public static final String CONCEPT_MAP2 = "<conceptmap>\n"
			+ "<nodes>\n"
			+ "<node id=\"2698971\" name=\"force\" xpos=\"35\" ypos=\"129\"/>\n"
			+ "<node id=\"24575663\" name=\"mass\" xpos=\"275\" ypos=\"131\"/>\n"
			+ "<node id=\"31452704\" name=\"moment\" xpos=\"32\" ypos=\"5\"/>\n"
			+ "<node id=\"14074896\" name=\"distance\" xpos=\"271\" ypos=\"45\"/>\n"
			+ "<node id=\"24575663\" name=\"mass\" xpos=\"275\" ypos=\"131\"/>\n"
			+ "<node id=\"2698971\" name=\"force\" xpos=\"35\" ypos=\"129\"/>\n"
			+ "<node id=\"14074896\" name=\"distance\" xpos=\"271\" ypos=\"45\"/>\n"
			+ "<node id=\"31452704\" name=\"moment\" xpos=\"32\" ypos=\"5\"/>\n"
			+ "</nodes>\n"
			+ "<links>\n"
			+ "<link from=\"14074896\" id=\"ID-1238182344922\" label=\"+\" to=\"2698971\"/>\n"
			+ "<link from=\"2698971\" id=\"ID-1238182364029\" label=\"+\" to=\"31452704\"/>\n"
			+ "<link from=\"14074896\" id=\"ID-1238182384867\" label=\"+\" to=\"31452704\"/>\n"
			+ "<link from=\"24575663\" id=\"ID-1238182326498\" label=\"+\" to=\"2698971\"/>\n"
			+ "</links>\n" + "</conceptmap>\n";

	public static List<String> elo2NodeLabelList = Arrays.asList(new String[] {
			"total moment", "moment child 1", "moment child 2", "moment", });
	public static List<String> elo2LinkLabelList = Arrays.asList(new String[] {
			"is_a", "+", "equals" });
	public static List<String> elo3NodeLabelList = Arrays.asList(new String[] {
			"force", "mass", "moment", "distance" });
	public static List<String> elo3LinkLabelList = Arrays
			.asList(new String[] { "+" });

	public static List<String> elo1NodeLabelList = Arrays.asList(new String[] {
			"total moment", "moment child 1", "force", "moment child 2",
			"mass", "moment", "balance state", "distance" });

	public static List<String> elo1LinkLabelList = Arrays.asList(new String[] {
			"-", "+", "is_a", "13768021-13768021", "Determines" });
}