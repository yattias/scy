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

def names = ["Video","Search","Research_question","Report","Presentation","Reflection","Pizza","Orientation2","Orientation","New","Model","Interview","Information2","Information","Idea","Hypothese2","Hypothese","House","Exp_design","Evaluation_report","Drawing2","Drawing","Designed_artifact","Design_of_artifact","Debate_argument","Dataset_processed","Dataset","Data","Conclusion","Concept_map3","Concept_map2","Concept_map","Concept_hypothese","Collaboration_invitation","Collaboration_denied","Collaboration_accepted","Choices","Assignment","Analyse","Alert_question","Alert_message"];

public function getNames(): String[] {
return names;
}

public function createEloIcon(name: String): EloIcon {
if (name.equalsIgnoreCase("dummy")) {
return null;
} else if (name.equalsIgnoreCase("Video")) {
return VideoIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Search")) {
return SearchIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Research_question")) {
return Research_questionIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Report")) {
return ReportIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Presentation")) {
return PresentationIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Reflection")) {
return ReflectionIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Pizza")) {
return PizzaIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Orientation2")) {
return Orientation2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Orientation")) {
return OrientationIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("New")) {
return NewIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Model")) {
return ModelIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Interview")) {
return InterviewIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Information2")) {
return Information2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Information")) {
return InformationIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Idea")) {
return IdeaIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Hypothese2")) {
return Hypothese2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Hypothese")) {
return HypotheseIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("House")) {
return HouseIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Exp_design")) {
return Exp_designIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Evaluation_report")) {
return Evaluation_reportIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Drawing2")) {
return Drawing2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Drawing")) {
return DrawingIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Designed_artifact")) {
return Designed_artifactIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Design_of_artifact")) {
return Design_of_artifactIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Debate_argument")) {
return Debate_argumentIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Dataset_processed")) {
return Dataset_processedIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Dataset")) {
return DatasetIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Data")) {
return DataIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Conclusion")) {
return ConclusionIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Concept_map3")) {
return Concept_map3Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Concept_map2")) {
return Concept_map2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Concept_map")) {
return Concept_mapIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Concept_hypothese")) {
return Concept_hypotheseIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Collaboration_invitation")) {
return Collaboration_invitationIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Collaboration_denied")) {
return Collaboration_deniedIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Collaboration_accepted")) {
return Collaboration_acceptedIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Choices")) {
return ChoicesIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Assignment")) {
return AssignmentIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Analyse")) {
return AnalyseIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Alert_question")) {
return Alert_questionIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Alert_message")) {
return Alert_messageIcon{windowColorScheme:windowColorScheme};
} else {
return new LogoEloIcon();
}
}
}
