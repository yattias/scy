package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel
import eu.scy.common.mission.MissionSpecificationElo
import scala.collection.mutable.ArrayBuffer
import eu.scy.utils.missioncopier.copying.RealMissionCopier

class CopyMissionCommand(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("copy mission")
  val description = "copy mission"
  override val paramDescription = "mission names"

  def execute(params: Seq[String]): Unit = {
    if (stateModel.source == null) {
      println("cannot copy missions if the source repository is not defined")
      return
    }
    if (stateModel.destination == null) {
      println("cannot copy missions if the destination repository is not defined")
      return
    }
    val realMissionCopier = new RealMissionCopier(stateModel)
    if (realMissionCopier.missionSpecificationElos.isEmpty) {
      println("there are no missions defined")
      return
    }
    if (params.isEmpty) {
      askUserForMissions
    } else {
      copyMissions(params)
    }
  }

  private def askUserForMissions() = {
    val realMissionCopier = new RealMissionCopier(stateModel)
    val missionSpecificationElos = ArrayBuffer[MissionSpecificationElo]()
    missionSpecificationElos ++= realMissionCopier.missionSpecificationElos
    var stop = false
    while (!stop) {
      if (missionSpecificationElos.isEmpty) {
        println("there are no missions left to copy")
        stop = true
      } else {
        printMissions(missionSpecificationElos)
        print("mission>")
        val input = stateModel.readLine().trim
        if (input.isEmpty()) {
          stop = true
        } else {
          val option = Integer.parseInt(input)
          if (option > missionSpecificationElos.size) {
            println("there are not that many missions")
          } else if (option == 0) {
            val missionSpecificationElosCopied = ArrayBuffer[MissionSpecificationElo]()
            for (missionSpecificationElo <- missionSpecificationElos) {
              if (realMissionCopier.copyMission(missionSpecificationElo)) {
                missionSpecificationElosCopied += missionSpecificationElo
              }
            }
            missionSpecificationElos --= missionSpecificationElosCopied
          } else {
            if (realMissionCopier.copyMission(missionSpecificationElos(option - 1)))
              missionSpecificationElos.remove(option - 1)
          }
        }
      }
    }
  }

  private def printMissions(missionSpecificationElos: Seq[MissionSpecificationElo]) = {
    println("Select mission to copy")
    println("0: all missions")
    var i = 1;
    for (missionSpecificationElo <- missionSpecificationElos) {
      println(i + ": " + Utils.getEloDisplayDescription(missionSpecificationElo,stateModel))
      i += 1
    }
  }

  private def copyMissions(missionNames: Seq[String]) = {
    val realMissionCopier = new RealMissionCopier(stateModel)
    for (missionName <- missionNames) {
      realMissionCopier.copyMission(missionName)
    }
  }
}