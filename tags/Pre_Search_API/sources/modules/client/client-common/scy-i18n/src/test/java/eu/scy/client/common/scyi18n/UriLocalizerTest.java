/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.common.scyi18n;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
public class UriLocalizerTest
{

   private UriLocalizer uriLocalizer;

   private final String uri1 = "http://www.scy-lab.eu/content/mission1/LAS_Build_design/Assignments/A_House_drawings.html";
   private final String uri2o = "http://www.scy-lab.eu/content/mission1_nl/LAS_Build_design/Assignments/A_House_drawings.html";
   private final String uri2r = "http://www.scy-lab.eu/content/mission1_en/LAS_Build_design/Assignments/A_House_drawings.html";
   private final String uri3o = "http://www.scy-lab.eu/content/mission1/LAS_Build_design/Assignments/A_House_drawings_nl.html";
   private final String uri3r = "http://www.scy-lab.eu/content/mission1/LAS_Build_design/Assignments/A_House_drawings_en.html";

   private final String uriEn = "http://www.scy-lab.eu/content/en/mission1/LAS_Build_design/Assignments/A_House_drawings.html";
   private final String uriFr = "http://www.scy-lab.eu/content/fr/mission1/LAS_Build_design/Assignments/A_House_drawings.html";
   private final String uriNl = "http://www.scy-lab.eu/content/nl/mission1/LAS_Build_design/Assignments/A_House_drawings.html";

   public UriLocalizerTest()
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
      Locale.setDefault(Locale.ENGLISH);
      uriLocalizer = new UriLocalizer();
      UriLocalizer.localUriReplacements = null;
   }

   @After
   public void tearDown()
   {
   }

   @Test
   public void localizeUrl()
   {
      assertEquals(null, uriLocalizer.localizeUrl((URL) null));
   }

   @Test
   public void localizeUrlwithChecking() throws MalformedURLException
   {
      assertEquals(null, uriLocalizer.localizeUrlwithChecking((URL) null));
      assertEquals(uriEn, uriLocalizer.localizeUrlwithChecking(new URL(uriEn)).toString());
      assertEquals(uriEn, uriLocalizer.localizeUrlwithChecking(new URL(uriFr)).toString());
      Locale.setDefault(Locale.FRENCH);
      uriLocalizer = new UriLocalizer();
      assertEquals(uriFr, uriLocalizer.localizeUrlwithChecking(new URL(uriEn)).toString());
      assertEquals(uriFr, uriLocalizer.localizeUrlwithChecking(new URL(uriFr)).toString());
   }

   @Test
   public void localizeUri_URI()
   {
      assertEquals(null, uriLocalizer.localizeUri((URI) null));
   }

   @Test
   public void localizeUri_String1() throws URISyntaxException
   {
      assertEquals(null, uriLocalizer.localizeUri((String) null));
      assertSameResultWithUri("");
      assertSameResultWithUri(uri1);
      assertSameResultWithUri(uri2r);
      assertSameResultWithUri(uri3r);
      testLocalizeUri(uri2r, uri2o);
      testLocalizeUri(uri3r, uri3o);
   }

   @Test
   public void localizeUri_String2() throws URISyntaxException
   {
      UriLocalizer.localUriReplacements = UriLocalizer.createLocalUriReplacementFromString("https->http;http://www.scy-net.eu/->file://c:/");
      localizeUri_String1();
      testLocalizeUri("file://c:/content/en/mission1","https://www.scy-net.eu/content/en/mission1");
   }

   private void assertSameResultWithUri(String uri) throws URISyntaxException
   {
      assertEquals(uri, uriLocalizer.localizeUri(uri));
      assertEquals(new URI(uri), uriLocalizer.localizeUri(new URI(uri)));
   }

   private void testLocalizeUri(String uriA, String uriB) throws URISyntaxException
   {
      assertEquals(uriA, uriLocalizer.localizeUri(uriB));
      assertEquals(new URI(uriA), uriLocalizer.localizeUri(new URI(uriB)));
   }

   @Test
   public void localizePath1()
   {
      assertSameResultWithPath(null);
      assertSameResultWithPath("");
      assertSameResultWithPath("/test/test.test");
      assertSameResultWithPath("/_test/test.test");
      assertSameResultWithPath("/test/_test.test");
      assertSameResultWithPath("/tes_t/test.test");
      assertSameResultWithPath("/test/test._test");
      assertSameResultWithPath("/test/test._te_st");
      assertSameResultWithPath("/test_nl.test/test.test");
      assertSameResultWithPath("/test/nl.test");
      assertSameResultWithPath("/test/test_nl.");
      assertEquals("/test_en/test.test", uriLocalizer.localizePath("/test_nl/test.test"));
      assertEquals("/test_en/test_nl/test.test", uriLocalizer
               .localizePath("/test_nl/test_nl/test.test"));
      assertEquals("/test/test_en/test.test", uriLocalizer.localizePath("/test/test_nl/test.test"));
      assertEquals("_en/test.test", uriLocalizer.localizePath("_nl/test.test"));
      assertEquals("/test/en/test.test", uriLocalizer.localizePath("/test/nl/test.test"));
      assertEquals("/en/test.test", uriLocalizer.localizePath("/nl/test.test"));
      assertEquals("/en/nl/test.test", uriLocalizer.localizePath("/nl/nl/test.test"));
      assertEquals("en/test.test", uriLocalizer.localizePath("nl/test.test"));
      assertEquals("test_en.test", uriLocalizer.localizePath("test_nl.test"));
      assertEquals("test_en.testing", uriLocalizer.localizePath("test_nl.testing"));
      assertEquals("test_en.t", uriLocalizer.localizePath("test_nl.t"));
      assertEquals("/test/test_en/test_nl.test", uriLocalizer
               .localizePath("/test/test_nl/test_nl.test"));
   }

   @Test
   public void localizePath2()
   {
      UriLocalizer.localUriReplacements = UriLocalizer.createLocalUriReplacementFromString("http->file;file://www.scy-lab.eu/->c:/");
      localizePath1();
      assertEquals("c:/content/en/mission1", uriLocalizer.makePathLocalIfSpecified("http://www.scy-lab.eu/content/en/mission1"));
   }
   
   private void assertSameResultWithPath(String path)
   {
      assertEquals(path, uriLocalizer.localizePath(path));
   }

   @Test
   public void createLocalUriReplacementFromString1()
   {
      assertNull(UriLocalizer.createLocalUriReplacementFromString(null));
      checkCreateLocalUriReplacementFromString("", 0);
      checkCreateLocalUriReplacementFromString("source->replacement", 1, "source", "replacement");
      checkCreateLocalUriReplacementFromString("source->", 1, "source", "");
      checkCreateLocalUriReplacementFromString("source->replacement;", 1, "source", "replacement");
      checkCreateLocalUriReplacementFromString("source1->replacement1;source2->replacement2", 2, "source1", "replacement1", "source2", "replacement2");
   }

   private void checkCreateLocalUriReplacementFromString(String specification, int nrOfElements,
            String... elements)
   {
      LocalUriReplacement[] localUriReplacements = UriLocalizer
               .createLocalUriReplacementFromString(specification);
      assertEquals(nrOfElements, localUriReplacements.length);
      for (int i = 0; i < nrOfElements; i++)
      {
         assertEquals(elements[2 * i], localUriReplacements[i].getSource());
         assertEquals(elements[2 * i + 1], localUriReplacements[i].getReplacement());
      }
   }
   
   @Test
   public void doLocalUriReplacement1(){
      checkDoLocalUriReplacement("http://www.scy-lab.eu/content/en/mission1","source","","http://www.scy-lab.eu/content/en/mission1");
      checkDoLocalUriReplacement("http://www.scy-lab.eu/content/en/mission1","http://www.scy-lab.eu/","","content/en/mission1");
      checkDoLocalUriReplacement("http://www.scy-lab.eu/content/en/mission1","http://www.scy-lab.eu/","file://","file://content/en/mission1");
   }
   
   private void checkDoLocalUriReplacement(String path, String source, String replacement,String resultPath){
      LocalUriReplacement localUriReplacement = new LocalUriReplacement(source,replacement);
      String localPath = uriLocalizer.doLocalUriReplacement(localUriReplacement,path);
      assertEquals(resultPath,localPath);
   }
   
   @Test
   public void makePathLocalIfSpecified1(){
      UriLocalizer.localUriReplacements = UriLocalizer.createLocalUriReplacementFromString("http->file;file://www.scy-lab.eu/->c:/");
      assertNull( uriLocalizer.makePathLocalIfSpecified(null));
      assertEquals("", uriLocalizer.makePathLocalIfSpecified(""));
      assertEquals("c:/content/en/mission1", uriLocalizer.makePathLocalIfSpecified("http://www.scy-lab.eu/content/en/mission1"));
   }
   
   @Test
   public void testFilePath() throws URISyntaxException{
      final String filePath = "d:\\projects\\SCY\\code\\scy-trunk\\content\\";
      UriLocalizer.localUriReplacements = UriLocalizer.createLocalUriReplacementFromString("http://www.scy-lab.eu/content->file:///"+filePath);
      String newPath = uriLocalizer.makePathLocalIfSpecified("http://www.scy-lab.eu/content/en/mission1/startPage/Welcome.html");
      URI uri = new URI(newPath);
   }
}
