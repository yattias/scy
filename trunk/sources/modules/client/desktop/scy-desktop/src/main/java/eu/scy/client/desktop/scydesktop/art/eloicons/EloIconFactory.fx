package eu.scy.client.desktop.scydesktop.art.eloicons;

import eu.scy.client.desktop.scydesktop.art.javafx.LogoEloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
/**
* @author lars
*/

public class EloIconFactory {

def names = ["Layer_1","Informatiion","Orientation","Model","Reflection","Pizza","Concept_map","Concept_hypothese","Analysis_representation_of_data_related_to_hypothesis","Research_question","Debate_argument","Idea","Info2","Home2","Drawing2","Layer_26","Presentation","Hypothese2","Video","Report2","Evaluation_report","Data","Designed_artefact","Design_of_artifacr"];

public function getNames(): String[] {
return names;
}

public function createEloIcon(name: String): EloIcon {
if (name.equalsIgnoreCase("dummy")) {
return null;
} else if (name.equalsIgnoreCase("Layer_1")) {
return new Layer_1Icon();
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
} else if (name.equalsIgnoreCase("Analysis_representation_of_data_related_to_hypothesis")) {
return new Analysis_representation_of_data_related_to_hypothesisIcon();
} else if (name.equalsIgnoreCase("Research_question")) {
return new Research_questionIcon();
} else if (name.equalsIgnoreCase("Debate_argument")) {
return new Debate_argumentIcon();
} else if (name.equalsIgnoreCase("Idea")) {
return new IdeaIcon();
} else if (name.equalsIgnoreCase("Info2")) {
return new Info2Icon();
} else if (name.equalsIgnoreCase("Home2")) {
return new Home2Icon();
} else if (name.equalsIgnoreCase("Drawing2")) {
return new Drawing2Icon();
} else if (name.equalsIgnoreCase("Layer_26")) {
return new Layer_26Icon();
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
} else if (name.equalsIgnoreCase("Designed_artefact")) {
return new Designed_artefactIcon();
} else if (name.equalsIgnoreCase("Design_of_artifacr")) {
return new Design_of_artifacrIcon();
} else {
return new LogoEloIcon();
}
}
}
