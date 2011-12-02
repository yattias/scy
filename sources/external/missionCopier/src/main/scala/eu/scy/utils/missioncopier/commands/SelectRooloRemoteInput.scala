package eu.scy.utils.missioncopier.commands

import roolo.api.IRepository
import eu.scy.utils.missioncopier.{SpringLoader, StateModel}
import roolo.search.{Query, MetadataQueryComponent}
import org.springframework.remoting.RemoteAccessException

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 2-12-11
 * Time: 16:06
 */

class SelectRooloRemoteInput(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("[select] input [from] remote")
  val description = "select roolo remote input"
  override val paramDescription = "host [port]"

  protected def isForSource = true

  def repositoryName = "roolo remote "

  var hostName = ""

  def execute(params: Seq[String]): Unit = {
    val hostPort = findHostAndPort(params)
    hostPort match {
      case Some((host: String, port: java.lang.Integer)) =>
        hostName = host + ":" + port
        val repository = connectToRepository(host, port)
        if (repository != null)
          setRepository(repository)
      case _ =>
    }
  }

  private def findHostAndPort(params: Seq[String]): Option[(String, java.lang.Integer)] = {
    var host = ""
    var port: java.lang.Integer = 80
    if (!params.isEmpty) {
      host = params.head
      val remainingParams = params.tail
      if (!remainingParams.isEmpty) {
        val portString = remainingParams.head
        try {
          port = Integer.parseInt(portString)
        } catch {
          case e: NumberFormatException =>
            println("Port (" + portString + ") is not a number: " + e.getMessage)
            return None
        }
      }
    }
    Some(host, port)
  }

  private def connectToRepository(host: String, port: java.lang.Integer): IRepository = {
    val repository = SpringLoader.loadRemoteRepository(host, port)
    val queryComponent = new MetadataQueryComponent("contents", "just for testing, to see if the remote roolo works");
    val query = new Query(queryComponent);
    try {
      repository.search(query)
    } catch {
      case e: RemoteAccessException =>
        println("Failed to connect, " + e.getMessage)
        return null
    }
    repository
  }

  private def setRepository(repository: IRepository) = {
    if (isForSource) {
      stateModel.setSource(repository, repositoryName + hostName, false)
    } else {
      stateModel.setDestination(repository, repositoryName + hostName, false)
    }
  }

}