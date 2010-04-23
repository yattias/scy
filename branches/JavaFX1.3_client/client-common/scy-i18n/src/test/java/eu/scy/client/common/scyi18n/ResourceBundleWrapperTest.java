/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.common.scyi18n;

import java.text.NumberFormat;
import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sikken
 */
public class ResourceBundleWrapperTest {

   private ResourceBundleWrapper resourceBundleWrapper;

    public ResourceBundleWrapperTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

    @Before
    public void setUp() {
       resourceBundleWrapper = new ResourceBundleWrapper(this);
    }

    @After
    public void tearDown() {
    }

//   /**
//    * Test of getString method, of class ResourceBundleWrapper.
//    */
//   @Test
//   public void testGetString()
//   {
//      System.out.println("getString");
//      String key = "";
//      ResourceBundleWrapper instance = null;
//      String expResult = "";
//      String result = instance.getString(key);
//      assertEquals(expResult, result);
//      // TODO review the generated test code and remove the default call to fail.
//      fail("The test case is a prototype.");
//   }
//
//   /**
//    * Test of getNumberToString method, of class ResourceBundleWrapper.
//    */
//   @Test
//   public void testGetNumberToString()
//   {
//      System.out.println("getNumberToString");
//      Locale locale = null;
//      double number = 0.0;
//      ResourceBundleWrapper instance = null;
//      String expResult = "";
//      String result = instance.getNumberToString(locale, number);
//      assertEquals(expResult, result);
//      // TODO review the generated test code and remove the default call to fail.
//      fail("The test case is a prototype.");
//   }
//
//   /**
//    * Test of getNumberFormat method, of class ResourceBundleWrapper.
//    */
//   @Test
//   public void testGetNumberFormat()
//   {
//      System.out.println("getNumberFormat");
//      Locale locale = null;
//      ResourceBundleWrapper instance = null;
//      NumberFormat expResult = null;
//      NumberFormat result = instance.getNumberFormat(locale);
//      assertEquals(expResult, result);
//      // TODO review the generated test code and remove the default call to fail.
//      fail("The test case is a prototype.");
//   }

    @Test
    public void testGetModuleName(){
        resourceBundleWrapper = new ResourceBundleWrapper(this);
        assertEquals("dataProcessTool",resourceBundleWrapper.getModuleName("eu.scy.client.tools.dataProcessTool.dataTool.DataProcessToolPanel"));
        assertEquals("scydesktop",resourceBundleWrapper.getModuleName("eu.scy.client.desktop.scydesktop.name"));
        assertEquals("scydesktop",resourceBundleWrapper.getModuleName("eu.scy.client.desktop.scydesktop"));
        assertNull(resourceBundleWrapper.getModuleName("eu.scy.Xclient.desktop.scydesktop"));
        assertNull(resourceBundleWrapper.getModuleName("eu.scy.client.desktop"));
        assertNull(resourceBundleWrapper.getModuleName("eu.scy.client.tools"));
    }
}