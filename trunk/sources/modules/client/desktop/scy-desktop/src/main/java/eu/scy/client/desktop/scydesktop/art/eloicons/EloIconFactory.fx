package eu.scy.client.desktop.scydesktop.art.eloicons;

import eu.scy.client.desktop.scydesktop.art.javafx.LogoEloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
/**
* @author lars
*/

public class EloIconFactory {
 def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

def names = ["Orientation2","Information","Orientation","Model","Reflection","Pizza","Concept_map","Concept_hypothese","Analyse","Research_question","Debate_argument","Idea2","Info2","House2","Drawing2","Conclusion2","Presentation","Hypothese2","Video","Report2","Evaluation_report","Data","Designed_artifact","Design_of_artifact","Choises2","Assignment","Concept_map","Concept_map2","Dataset_processing","Dataset","Drawing","New","Exp_design","Hypothese","Interview","Search"];

public function getNames(): String[] {
return names;
}

public function createEloIcon(name: String): EloIcon {
if (name.equalsIgnoreCase("dummy")) {
return null;
} else if (name.equalsIgnoreCase("Orientation2")) {
return Orientation2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Information")) {
return InformationIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Orientation")) {
return OrientationIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Model")) {
return ModelIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Reflection")) {
return ReflectionIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Pizza")) {
return PizzaIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Concept_map")) {
return Concept_mapIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Concept_hypothese")) {
return Concept_hypotheseIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Analyse")) {
return AnalyseIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Research_question")) {
return Research_questionIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Debate_argument")) {
return Debate_argumentIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Idea2")) {
return Idea2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Info2")) {
return Info2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("House2")) {
return House2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Drawing2")) {
return Drawing2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Conclusion2")) {
return Conclusion2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Presentation")) {
return PresentationIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Hypothese2")) {
return Hypothese2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Video")) {
return VideoIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Report2")) {
return Report2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Evaluation_report")) {
return Evaluation_reportIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Data")) {
return DataIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Designed_artifact")) {
return Designed_artifactIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Design_of_artifact")) {
return Design_of_artifactIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Choises2")) {
return Choises2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Assignment")) {
return AssignmentIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Concept_map")) {
return Concept_mapIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Concept_map2")) {
return Concept_map2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Dataset_processing")) {
return Dataset_processingIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Dataset")) {
return DatasetIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Drawing")) {
return DrawingIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("New")) {
return NewIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Exp_design")) {
return Exp_designIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Hypothese")) {
return HypotheseIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Interview")) {
return InterviewIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Search")) {
return SearchIcon{windowColorScheme:windowColorScheme};
} else {
return new LogoEloIcon();
}
}
}
