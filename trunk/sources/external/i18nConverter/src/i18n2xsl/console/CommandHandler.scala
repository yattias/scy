package i18n2xsl.console

trait CommandHandler {
  val commands: Seq[String]

  val description: String

  val paramDescription = ""

  def execute(params: Seq[String]): Unit

  def createCommandsSequences(commandStrings: String*): Seq[Seq[String]] = {
    for (commandString <- commandStrings) yield commandString.split(" ").toSeq
  }
}