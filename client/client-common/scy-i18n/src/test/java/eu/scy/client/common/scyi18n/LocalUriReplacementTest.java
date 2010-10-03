/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.common.scyi18n;

import java.lang.NullPointerException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SikkenJ
 */
public class LocalUriReplacementTest
{

   public LocalUriReplacementTest()
   {
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
   public void setUp()
   {
   }

   @After
   public void tearDown()
   {
   }

   @Test (expected=NullPointerException.class)
   public void testCreation1()
   {
      new LocalUriReplacement(null);
      fail("NullPointerException expection");
   }

   @Test (expected=IllegalArgumentException.class)
   public void testCreation2()
   {
      new LocalUriReplacement("");
      fail("IllegalArgumentException expection");
   }

   @Test (expected=IllegalArgumentException.class)
   public void testCreation3()
   {
      new LocalUriReplacement("source,replacement");
      fail("IllegalArgumentException expection");
   }

   @Test (expected=IllegalArgumentException.class)
   public void testCreation4()
   {
      new LocalUriReplacement("->replacement");
      fail("IllegalArgumentException expection");
   }

   @Test
   public void testCreation5()
   {
      testLocalUriReplacement("source->replacement","source","replacement");
      testLocalUriReplacement("source->","source","");
   }

   private void testLocalUriReplacement(String specification, String source, String replacement){
      LocalUriReplacement localUriReplacement = new LocalUriReplacement(specification);
      assertEquals(source, localUriReplacement.getSource());
      assertEquals(replacement, localUriReplacement.getReplacement());
   }
}
