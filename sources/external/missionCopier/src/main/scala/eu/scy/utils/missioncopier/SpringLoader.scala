package eu.scy.utils.missioncopier

import roolo.elo.api.IMetadataTypeManager
import org.springframework.context.support.ClassPathXmlApplicationContext
import roolo.api.{IRepository, IExtensionManager}

object SpringLoader {
  val metadataTypeManagerConfigLocation = "/roolo/elo/metadataTypeManagerConfig.xml"
  val localMetadataTypeManagerBeanName = "localMetadataTypeManager"
  var metadataTypeManager: IMetadataTypeManager = null;
  val extensionManagerConfigLocation = "/eu/scy/utils/missioncopier/localRooloExtensions.xml"
  val localExtensionManagerBeanName = "localExtensionManager"
  var extensionManager: IExtensionManager = null;
  val hostPropertyName = "serverName"
  val portPropertyName = "httpPort"
  val contextPathPropertyName = "contextPath"
  val repositoryServiceName = "repositoryService"
  val remoteRepositoryConfigLocation = "/eu/scy/utils/missioncopier/remoteRooloServer.xml"
  val remoteRepositoryBeanName = "remoteRepository"

  def getMetadataTypeManager(): IMetadataTypeManager = {
    if (metadataTypeManager == null) {
      metadataTypeManager = loadBean(metadataTypeManagerConfigLocation, localMetadataTypeManagerBeanName).asInstanceOf[IMetadataTypeManager]
    }
    metadataTypeManager
  }

  def getExtensionManager(): IExtensionManager = {
    if (extensionManager == null) {
      extensionManager = loadBean(extensionManagerConfigLocation, localExtensionManagerBeanName).asInstanceOf[IExtensionManager]
    }
    extensionManager
  }

  def loadRemoteRepository(host: String, port: java.lang.Integer, contextPath: String, repositoryService : String): IRepository = {
    System.setProperty(hostPropertyName,host)
    System.setProperty(portPropertyName,port.toString)
    System.setProperty(contextPathPropertyName,contextPath)
    System.setProperty(repositoryServiceName,repositoryService)
    loadBean(remoteRepositoryConfigLocation, remoteRepositoryBeanName).asInstanceOf[IRepository]
  }

  private def loadBean(path: String, beanName: String): Object = {
    val context = new ClassPathXmlApplicationContext(path);
    if (context == null) {
      throw new IllegalArgumentException("failed to load context from class path: {file}");
    }
    context.getBean(beanName)

  }
}