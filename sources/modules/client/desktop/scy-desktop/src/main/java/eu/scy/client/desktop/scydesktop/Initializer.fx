/*
 * Initializer.fx
 *
 * Created on 9-dec-2009, 10:28:50
 */
package eu.scy.client.desktop.scydesktop;

import javafx.scene.image.Image;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
import java.io.File;
import java.lang.IllegalArgumentException;
import java.lang.System;
import java.io.FileOutputStream;
import java.io.PrintStream;
import eu.scy.client.desktop.scydesktop.utils.ExceptionCatcher;
import java.lang.Thread;
import java.lang.Exception;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.login.LoginValidator;
import eu.scy.client.desktop.scydesktop.dummy.DummyLoginValidator;
import org.apache.log4j.Logger;

/**
 * @author sikken
 */
// place your code here
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.Initializer");

public class Initializer {

   public-init var log4JInitFile = "";
   public-init var backgroundImageUrl = "{__DIR__}images/bckgrnd2.jpg";
   public-init var loggingDirectoryName = "logging";
   public-init var redirectSystemStream = false;
   public-init var lookAndFeel = "nimbus";
   public-read var backgroundImage: Image;
   public-read var loggingDirectory: File;
   public-read var loginValidator: LoginValidator = new DummyLoginValidator();
   def systemOutFileName = "systemOut";
   def systemErrFileName = "systemErr";

   init {
      Thread.setDefaultUncaughtExceptionHandler(new ExceptionCatcher("SCY-LAB"));
      setupLog4J();
      setupBackgroundImage();
      loggingDirectory = findLoggingDirectory();
      if (loggingDirectory != null and redirectSystemStream) {
         doRedirectSystemStream();
      }
      setLookAndFeel();
   }

   public function getBackgroundImageView(scene: Scene): ImageView {
      var backgroundImageView: ImageView;
      if (backgroundImage != null) {
         backgroundImageView = ImageView {
            image: Image {
               url: "{__DIR__}bckgrnd2.jpg"
            }
            fitWidth: bind scene.width
            fitHeight: bind scene.height
            preserveRatio: false
            cache: true
         }
      }
      return backgroundImageView;
   }

   function setupLog4J() {
      if (log4JInitFile.length() > 0) {
         InitLog4JFX.initLog4J(log4JInitFile);
      } else {
         InitLog4JFX.initLog4J();
      }
   }

   function setupBackgroundImage() {
      if (backgroundImageUrl.length() > 0) {
         backgroundImage = Image {
            url: "{__DIR__}bckgrnd2.jpg"
         }
      }
   }

   function findLoggingDirectory(): File {
      var logDirectory: File;
      if (loggingDirectoryName.length() > 0) {
         logDirectory = new File(loggingDirectoryName);
         if (not logDirectory.exists()) {
            throw new IllegalArgumentException("logging directory does not exists: {logDirectory.getAbsolutePath()}");
         }
         if (not logDirectory.isDirectory()) {
            throw new IllegalArgumentException("logging directory does not a directory: {logDirectory.getAbsolutePath()}");
         }
      }
      return logDirectory;
   }

   function doRedirectSystemStream() {
      System.setOut(createPrintStream(systemOutFileName));
      System.setErr(createPrintStream(systemErrFileName));
   }

   function createPrintStream(fileName: String): PrintStream// throws IOException
   {
      var fileCount = 0;
      var streamFile = new File(loggingDirectory, "{fileName}_{fileCount}.txt");
      while (streamFile.exists()) {
         ++fileCount;
         streamFile = new File(loggingDirectory, "{fileName}_{fileCount}.txt");
      }
      var outputStream = new FileOutputStream(streamFile, false);
      return new PrintStream(outputStream, true);
   }

   function setLookAndFeel() {
      if (lookAndFeel.length() > 0) {
         var lookAndFeelClassName = findLookAndFeelClassName();
         if (lookAndFeelClassName != null) {
            try {
               UIManager.setLookAndFeel(lookAndFeelClassName);
            } catch (e: Exception) {
               logger.error("problems with setting the look and feel: {lookAndFeel}", e);
            }
         }
      }
   }

   function findLookAndFeelClassName(): String {
      if (lookAndFeel.indexOf(".") >= 0) {
         // its a class name
         return lookAndFeel;
      } else {
         var lookAndFeelInfos: LookAndFeelInfo[] = UIManager.getInstalledLookAndFeels();
         for (lookAndFeelInfo in lookAndFeelInfos) {
            if (lookAndFeel.equalsIgnoreCase(lookAndFeelInfo.getName())) {
               return lookAndFeelInfo.getClassName();
            }
         }
         logger.info("failed to find look and feel named: {lookAndFeel}");
         return null;
      }
   }
}
