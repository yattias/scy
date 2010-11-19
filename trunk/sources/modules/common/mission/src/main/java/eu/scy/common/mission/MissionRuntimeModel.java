package eu.scy.common.mission;

public interface MissionRuntimeModel
{
   public MissionRuntimeElo getMissionRuntimeElo();

   public MissionModelElo getMissionModelElo();

   public EloToolConfigsElo getEloToolConfigsElo();

   public TemplateElosElo getTemplateElosElo();

   public RuntimeSettingsElo getRuntimeSettingsElo();
   
   public MissionModel getMissionModel();
   
   public RuntimeSettingsManager getRuntimeSettingsManager();
}
