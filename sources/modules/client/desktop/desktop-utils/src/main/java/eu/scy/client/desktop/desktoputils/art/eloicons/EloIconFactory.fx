package eu.scy.client.desktop.desktoputils.art.eloicons;

import eu.scy.client.desktop.desktoputils.art.javafx.LogoEloIcon;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
/**
* @author lars
*/

public class EloIconFactory {
 def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

def names = ["Elo_finished","Import_2","Import_1","Import","Export_2","Export_1","Export","Save_as_dataset","Save_as_1","Save_as","Save_2","Save_1","Save","Data_sync","Elo_intermediate","Elo_anchor","Buddies_me_and_others","Buddies_others","Buddy_other","Buddy_me","Minimize_1","Minimize","Unrotate","Maximize","Center_1","Center","Mission_map_2","Mission_map_1","Mission_map","E_portfolio_new","E_portfolio","Give_feedback_new","Give_feedback","Get_feedback_new","Get_feedback","New_author_2","New_author","Search_2","New_template_2","New_template","Archive","Video","Search","Research_question","Report","Presentation","Reflection","Pizza","Orientation2","Orientation","New","Model","Interview","Information2","Information","Idea","Hypothese2","Hypothese","House","Exp_design","Evaluation_report","Drawing2","Drawing","Designed_artifact","Design_of_artifact","Debate_argument","Dataset_processed","Dataset","Data","Conclusion","Concept_map3","Concept_map2","Concept_map","Concept_hypothese","Collaboration_invitation","Collaboration_accepted","Collaboration_denied","Choices","Assignment","Analyse","Alert_question","Alert_message"];

public function getNames(): String[] {
return names;
}

public function createEloIcon(name: String): EloIcon {
if (name.equalsIgnoreCase("dummy")) {
return null;
} else if (name.equalsIgnoreCase("Elo_finished")) {
return Elo_finishedIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Import_2")) {
return Import_2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Import_1")) {
return Import_1Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Import")) {
return ImportIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Export_2")) {
return Export_2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Export_1")) {
return Export_1Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Export")) {
return ExportIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Save_as_dataset")) {
return Save_as_datasetIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Save_as_1")) {
return Save_as_1Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Save_as")) {
return Save_asIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Save_2")) {
return Save_2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Save_1")) {
return Save_1Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Save")) {
return SaveIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Data_sync")) {
return Data_syncIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Elo_intermediate")) {
return Elo_intermediateIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Elo_anchor")) {
return Elo_anchorIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Buddies_me_and_others")) {
return Buddies_me_and_othersIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Buddies_others")) {
return Buddies_othersIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Buddy_other")) {
return Buddy_otherIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Buddy_me")) {
return Buddy_meIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Minimize_1")) {
return Minimize_1Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Minimize")) {
return MinimizeIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Unrotate")) {
return UnrotateIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Maximize")) {
return MaximizeIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Center_1")) {
return Center_1Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Center")) {
return CenterIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Mission_map_2")) {
return Mission_map_2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Mission_map_1")) {
return Mission_map_1Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Mission_map")) {
return Mission_mapIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("E_portfolio_new")) {
return E_portfolio_newIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("E_portfolio")) {
return E_portfolioIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Give_feedback_new")) {
return Give_feedback_newIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Give_feedback")) {
return Give_feedbackIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Get_feedback_new")) {
return Get_feedback_newIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Get_feedback")) {
return Get_feedbackIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("New_author_2")) {
return New_author_2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("New_author")) {
return New_authorIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Search_2")) {
return Search_2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("New_template_2")) {
return New_template_2Icon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("New_template")) {
return New_templateIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Archive")) {
return ArchiveIcon{windowColorScheme:windowColorScheme};
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
} else if (name.equalsIgnoreCase("Collaboration_accepted")) {
return Collaboration_acceptedIcon{windowColorScheme:windowColorScheme};
} else if (name.equalsIgnoreCase("Collaboration_denied")) {
return Collaboration_deniedIcon{windowColorScheme:windowColorScheme};
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
