package eu.scy.utils.missioncopier
import roolo.elo.api.IMetadataTypeManager
import org.springframework.context.support.ClassPathXmlApplicationContext
import roolo.api.IExtensionManager

object SpringLoader {
   val metadataTypeManagerConfigLocation = "/roolo/elo/metadataTypeManagerConfig.xml"
   val localMetadataTypeManagerBeanName = "localMetadataTypeManager"
   var metadataTypeManager: IMetadataTypeManager = null;
   val extensionManagerConfigLocation = "/eu/scy/utils/missioncopier/localRooloExtensions.xml"
   val localExtensionManagerBeanName = "localExtensionManager"
   var extensionManager: IExtensionManager = null;

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

   private def loadBean(path: String, beanName: String): Object = {
      val context = new ClassPathXmlApplicationContext(path);
      if (context == null) {
         throw new IllegalArgumentException("failed to load context from class path: {file}");
      }
      context.getBean(beanName)

   }
}