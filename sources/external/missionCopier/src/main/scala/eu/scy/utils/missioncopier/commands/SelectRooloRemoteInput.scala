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
  override val paramDescription = "host [port] [context path]"

  protected def isForSource = true

  private val knownServers = List(
    ("Twente 1", "gw-scyserver.gw.utwente.nl", 80, "", ""),
    ("Twente 2", "scyserver2.gw.utwente.nl", 80, "", ""),
    ("Twente 3", "scyserver3.gw.utwente.nl", 80, "", ""),
    ("Dev server", "scy.collide.info", 80, "", ""),
    ("Review server", "scy-review.collide.info", 80, "", ""),
    ("Local roolo mock server", "localhost", 8080, "/roolo-mock-server", "mock-repository-httpinvoker")
  )

  private var hostName = ""
  private var port: java.lang.Integer = 80
  private var contextPath = "/extcomp"
  private var repositoryService = "repository-httpinvoker"
  private var errors = false

  def execute(params: Seq[String]): Unit = {
    findHostAndPort(params)
    if (!errors) {
      val repository = connectToRepository(hostName, port, contextPath, repositoryService)
      if (repository != null)
        setRepository(repository)
    }
  }

  private def findHostAndPort(params: Seq[String]) = {
    if (!params.isEmpty) {
      extractConfigFromParameters(params)
    } else {
      askUser()
    }
  }

  private def extractConfigFromParameters(params: scala.Seq[String]) {
    hostName = params.head
    var restParams = params.tail
    if (!restParams.isEmpty) {
      extractPort(restParams.head)
      restParams = restParams.tail
      if (!restParams.isEmpty) {
        contextPath = restParams.head
        restParams = restParams.tail
        if (!restParams.isEmpty) {
          repositoryService = restParams.head
        }
      }
    }
  }

  private def extractPort(portString: String) = {
    try {
      port = Integer.parseInt(portString)
    } catch {
      case e: NumberFormatException =>
        println("Port (" + portString + ") is not a number: " + e.getMessage)
        errors = true
    }
  }

  private def askUser() = {
    errors = true
    println("Known servers:")
    var i = 1
    for (server <- knownServers) {
      printf("%d: %s\n", i, server._1)
      i += 1
    }
    print("Select server: ")
    val input = stateModel.readLine().trim
    if (!input.isEmpty) {
      val option = Integer.parseInt(input)
      if (option > knownServers.size) {
        println("there are not that many known servers")
      } else {
        val server = knownServers(option - 1)
        hostName = server._2
        port = server._3
        if (!server._4.isEmpty)
          contextPath = server._4
        if (!server._5.isEmpty)
          repositoryService = server._5
        errors = false
      }
    }
  }

  private def connectToRepository(host: String, port: java.lang.Integer, contextPath: String, repositoryService: String): IRepository = {
    val repository = SpringLoader.loadRemoteRepository(host, port, contextPath, repositoryService)
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
    val repositoryName = "roolo remote: " + hostName + ":" + port + contextPath + "/remoting/" + repositoryService
    if (isForSource) {
      stateModel.setSource(repository, repositoryName, false)
    } else {
      stateModel.setDestination(repository, repositoryName, false)
    }
  }

}