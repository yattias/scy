package eu.scy.common.mission.impl;

import eu.scy.common.mission.EloToolConfigsElo;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionRuntimeEloContent;
import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.RuntimeSettingsManager;
import eu.scy.common.mission.TemplateElosElo;
import eu.scy.common.scyelo.RooloServices;

public class BasicMissionRuntimeModel implements MissionRuntimeModel
{
   private final MissionRuntimeElo missionRuntimeElo;
   private final RooloServices rooloServices;
   private final MissionModelElo missionModelElo;
   private final EloToolConfigsElo eloToolConfigsElo;
   private final TemplateElosElo templateElosElo;
   private final RuntimeSettingsElo runtimeSettingsElo;

   public BasicMissionRuntimeModel(MissionRuntimeElo missionRuntimeElo, RooloServices rooloServices)
   {
      super();
      this.missionRuntimeElo = missionRuntimeElo;
      this.rooloServices = rooloServices;
      final MissionRuntimeEloContent missionRuntimeEloContent = missionRuntimeElo.getTypedContent();
      missionModelElo = MissionModelElo.loadLastVersionElo(missionRuntimeEloContent
               .getMissionMapModelEloUri(), rooloServices);
      eloToolConfigsElo = EloToolConfigsElo.loadElo(missionRuntimeEloContent
               .getEloToolConfigsEloUri(), rooloServices);
      templateElosElo = TemplateElosElo.loadElo(missionRuntimeEloContent.getTemplateElosEloUri(),
               rooloServices);
      runtimeSettingsElo = RuntimeSettingsElo.loadElo(missionRuntimeEloContent
               .getRuntimeSettingsEloUri(), rooloServices);
   }

   public BasicMissionRuntimeModel(MissionRuntimeElo missionRuntimeElo,
            RooloServices rooloServices, MissionModelElo missionModelElo,
            EloToolConfigsElo eloToolConfigsElo, TemplateElosElo templateElosElo,
            RuntimeSettingsElo runtimeSettingsElo)
   {
      super();
      this.missionRuntimeElo = missionRuntimeElo;
      this.rooloServices = rooloServices;
      this.missionModelElo = missionModelElo;
      this.eloToolConfigsElo = eloToolConfigsElo;
      this.templateElosElo = templateElosElo;
      this.runtimeSettingsElo = runtimeSettingsElo;
   }

   @Override
   public EloToolConfigsElo getEloToolConfigsElo()
   {
      return eloToolConfigsElo;
   }

   @Override
   public MissionModelElo getMissionModelElo()
   {
      return missionModelElo;
   }

   @Override
   public MissionRuntimeElo getMissionRuntimeElo()
   {
      return missionRuntimeElo;
   }

   @Override
   public RuntimeSettingsElo getRuntimeSettingsElo()
   {
      return runtimeSettingsElo;
   }

   @Override
   public TemplateElosElo getTemplateElosElo()
   {
      return templateElosElo;
   }

   @Override
   public MissionModel getMissionModel()
   {
      return missionModelElo.getMissionModel();
   }

   @Override
   public RuntimeSettingsManager getRuntimeSettingsManager()
   {
      // TODO Auto-generated method stub
      return null;
   }

}
