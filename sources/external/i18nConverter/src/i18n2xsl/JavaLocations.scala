package i18n2xsl

import java.io.File

class JavaLocations
{
  val clientRoot = new File("d:\\Projects\\scy\\code\\scy-trunk\\sources\\modules\\client\\");
  val javaLanguagePath = "/src/main/java/languages/"
  val resourcesLanguagePath = "/src/main/resources/languages/"

  //  case class JavaLocation(val name: String, val modulePath: String, val moduleName: String, val languagePath: String, val javaFX: Boolean = false)

  val locations = Seq(
    new JavaLocation("scydesktop", "desktop/scy-desktop", "scydesktop", javaLanguagePath, false),
    new JavaLocation("scydesktopFX", "desktop/scy-desktop", "scydesktop", javaLanguagePath, true),
    new JavaLocation("copex", "tools/copex", "copex", resourcesLanguagePath, false),
    new JavaLocation("fitex", "tools/dataprocesstool", "fitex", resourcesLanguagePath, false),
    new JavaLocation("copexFX", "tools/fx-copex", "fxcopex", javaLanguagePath, false),
    new JavaLocation("fx-socialtaggingtool", "tools/fx-socialtaggingtool", "fxsocialtaggingtool", javaLanguagePath, true),
    new JavaLocation("healthpassport", "tools/resultBinder", "resultbinder", resourcesLanguagePath, false),
    new JavaLocation("scydynamics", "tools/scydynamics", "scydynamics", javaLanguagePath, false),
    new JavaLocation("scymapper", "tools/scymapper", "scymapper", resourcesLanguagePath, false),
    new JavaLocation("simulatorFX", "tools/fx-simulator", "fxsimulator", javaLanguagePath, true),
    new JavaLocation("scysimulator", "tools/scysimulator", "scysimulator", javaLanguagePath, false),
    new JavaLocation("flyingsaucer", "tools/fx-flying-saucer", "fxflyingsaucer", javaLanguagePath, false),
    new JavaLocation("interview", "tools/fx-interviewtool", "interviewtool", javaLanguagePath, false),
    new JavaLocation("interviewFX", "tools/fx-interviewtool", "interviewtool", javaLanguagePath, true),
    new JavaLocation("richtexteditor", "client-common/rich-text-editor", "richtexteditor", resourcesLanguagePath, false)
  )

  def findJavaLocation(name: String): JavaLocation =
  {
    for (location <- locations) {
      if (location.name == name) {
        return location
      }
    }
    return null
  }

}