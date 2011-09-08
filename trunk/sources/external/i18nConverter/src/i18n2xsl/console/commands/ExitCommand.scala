package i18n2xsl.console.commands
import i18n2xsl.console.CommandHandler
import i18n2xsl.console.PropertiesModelCommandHandler
import i18n2xsl.console.PropertiesModel

class ExitCommand(override val propertiesModel: PropertiesModel) extends PropertiesModelCommandHandler(propertiesModel) {
	val commands = Seq("exit","quit")
	val description = "exits application"
	def execute(params: Seq[String]):Unit = {
	  propertiesModel.exit = true
	}
}