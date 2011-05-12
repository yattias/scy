/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

import org.apache.log4j.Logger;

/**
 *
 * @author SikkenJ
 */
public class FilteringExceptionCatcher extends ExceptionCatcher
{

   private static final Logger logger = Logger.getLogger(FilteringExceptionCatcher.class);
   private static final String emmbeddedEventQueueClass = "com.sun.embeddedswing.EmbeddedEventQueue";
   private static final String moveToVisibleAndOnScreenMethod = "moveToVisibleAndOnScreen";

   private int ignoreCounter = 0;
   private static final int reprotFrequency = 10;

   public FilteringExceptionCatcher(String appName)
   {
      super(appName);
   }

   public FilteringExceptionCatcher(String appName, boolean quit)
   {
      super(appName, quit);
   }

   @Override
   public void uncaughtException(Thread t, Throwable e)
   {
      if (ignoreException(t, e))
      {
         super.uncaughtException(t, e);
      }
   }

   private boolean ignoreException(Thread t, Throwable e)
   {
      String errorMessage = e.getMessage();
      if (errorMessage==null)
      {
         StackTraceElement[] stackTraceElements = e.getStackTrace();
         if (stackTraceElements.length > 0)
         {
            String className = stackTraceElements[0].getClassName();
            String methodName = stackTraceElements[0].getMethodName();
            if (emmbeddedEventQueueClass.equals(className) && moveToVisibleAndOnScreenMethod.equals(methodName))
            {
               // 100622, Jakob
               // it is most likely the nullpointer exception cause by a swing component, which is not correctly setup in the scene graph
               // the nullpointer exception occurs when the most is moved above the swing component,
               // after a resize of the window, the problem is solved
               // I have no idea how to add the swing component in a way that does not cause the nullpointer exception
               // It is just trial and error, for most situation I found out a way without getting the nullpointer exception
               // Sofar the nullpointer exception does not seem to cause any other problems
               // So untill I (or someone else) find the real solution, I'll just ignore the nullpointer exception
               ++ignoreCounter;
               logger.debug("Ignoring uncatched NullPointerException during moveToVisibleAndOnScreen in thread " + t.getName() + " (#" + ignoreCounter + ")");
//               System.out.println("Stack trace:");
//               e.printStackTrace();
               if (ignoreCounter % reprotFrequency==0){
                  logger.info("Number of ignored uncatched NullPointerExceptions during moveToVisibleAndOnScreen: "  + ignoreCounter);
               }
               return false;
            }
         }
      }
      return true;
   }
}
