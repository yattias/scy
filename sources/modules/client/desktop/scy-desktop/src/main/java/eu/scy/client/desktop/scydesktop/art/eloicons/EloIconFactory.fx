package eu.scy.client.desktop.scydesktop.art.eloicons;

import eu.scy.client.desktop.scydesktop.art.javafx.LogoEloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
/**
* @author lars
*/

public class EloIconFactory {

def names = ["Orientation2","Informatiion","Orientation","Model","Reflection","Pizza","Concept_map","Concept_hypothese","Analyse","Research_question","Debate_argument","Idea2","Info2","House2","Drawing2","Conclusion2","Presentation","Hypothese2","Video","Report2","Evaluation_report","Data","Designed_artifact","Design_of_artifact","Choises2","Assignment","Concept_map","Concept_map2","Dataset_processing","Dataset","Drawing","New","Exp_design","Hypothese","Interview","Search"];

public function getNames(): String[] {
return names;
}

public function createEloIcon(name: String): EloIcon {
if (name.equalsIgnoreCase("dummy")) {
return null;
} else if (name.equalsIgnoreCase("Orientation2")) {
return new Orientation2Icon();
} else if (name.equalsIgnoreCase("Informatiion")) {
return new InformatiionIcon();
} else if (name.equalsIgnoreCase("Orientation")) {
return new OrientationIcon();
} else if (name.equalsIgnoreCase("Model")) {
return new ModelIcon();
} else if (name.equalsIgnoreCase("Reflection")) {
return new ReflectionIcon();
} else if (name.equalsIgnoreCase("Pizza")) {
return new PizzaIcon();
} else if (name.equalsIgnoreCase("Concept_map")) {
return new Concept_mapIcon();
} else if (name.equalsIgnoreCase("Concept_hypothese")) {
return new Concept_hypotheseIcon();
} else if (name.equalsIgnoreCase("Analyse")) {
return new AnalyseIcon();
} else if (name.equalsIgnoreCase("Research_question")) {
return new Research_questionIcon();
} else if (name.equalsIgnoreCase("Debate_argument")) {
return new Debate_argumentIcon();
} else if (name.equalsIgnoreCase("Idea2")) {
return new Idea2Icon();
} else if (name.equalsIgnoreCase("Info2")) {
return new Info2Icon();
} else if (name.equalsIgnoreCase("House2")) {
return new House2Icon();
} else if (name.equalsIgnoreCase("Drawing2")) {
return new Drawing2Icon();
} else if (name.equalsIgnoreCase("Conclusion2")) {
return new Conclusion2Icon();
} else if (name.equalsIgnoreCase("Presentation")) {
return new PresentationIcon();
} else if (name.equalsIgnoreCase("Hypothese2")) {
return new Hypothese2Icon();
} else if (name.equalsIgnoreCase("Video")) {
return new VideoIcon();
} else if (name.equalsIgnoreCase("Report2")) {
return new Report2Icon();
} else if (name.equalsIgnoreCase("Evaluation_report")) {
return new Evaluation_reportIcon();
} else if (name.equalsIgnoreCase("Data")) {
return new DataIcon();
} else if (name.equalsIgnoreCase("Designed_artifact")) {
return new Designed_artifactIcon();
} else if (name.equalsIgnoreCase("Design_of_artifact")) {
return new Design_of_artifactIcon();
} else if (name.equalsIgnoreCase("Choises2")) {
return new Choises2Icon();
} else if (name.equalsIgnoreCase("Assignment")) {
return new AssignmentIcon();
} else if (name.equalsIgnoreCase("Concept_map")) {
return new Concept_mapIcon();
} else if (name.equalsIgnoreCase("Concept_map2")) {
return new Concept_map2Icon();
} else if (name.equalsIgnoreCase("Dataset_processing")) {
return new Dataset_processingIcon();
} else if (name.equalsIgnoreCase("Dataset")) {
return new DatasetIcon();
} else if (name.equalsIgnoreCase("Drawing")) {
return new DrawingIcon();
} else if (name.equalsIgnoreCase("New")) {
return new NewIcon();
} else if (name.equalsIgnoreCase("Exp_design")) {
return new Exp_designIcon();
} else if (name.equalsIgnoreCase("Hypothese")) {
return new HypotheseIcon();
} else if (name.equalsIgnoreCase("Interview")) {
return new InterviewIcon();
} else if (name.equalsIgnoreCase("Search")) {
return new SearchIcon();
} else {
return new LogoEloIcon();
}
}
}
