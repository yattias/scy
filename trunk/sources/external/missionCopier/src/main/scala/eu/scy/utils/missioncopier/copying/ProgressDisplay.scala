package eu.scy.utils.missioncopier.copying

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 9-12-11
 * Time: 12:42
 */

class ProgressDisplay {
  private val stepsPerLine = 100
  private var nrOfSteps = 0
  private val lineBegin = "%3d "

  private def printLineBegin {
    printf("%3d ", nrOfSteps)
  }

  def start() = {
    nrOfSteps = 0
    printLineBegin
  }

  def step() = {
    print(".")
    nrOfSteps += 1
    if (nrOfSteps % stepsPerLine==0){
      println()
      printLineBegin
    }
  }

  def stop() = {
    println()
  }

}